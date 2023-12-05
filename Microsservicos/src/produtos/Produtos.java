package produtos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import tratar.Tratamento;

public class Produtos extends Thread {

    ProdutoController pc;

    DataInputStream input;
    DataOutputStream output;
    String login;

    public Produtos(Socket user, ProdutoController pc, String login) throws IOException {
        this.pc = pc;
        input = new DataInputStream(user.getInputStream());
        output = new DataOutputStream(user.getOutputStream());
        this.login = login;
        System.out.println("produtos.Produtos.java: Criado");
    }

    public Produtos(Socket user, ProdutoController pc) throws IOException, InterruptedException {
        input = new DataInputStream(user.getInputStream());
        output = new DataOutputStream(user.getOutputStream());
        this.pc = pc;
        mostrarItens();
        String acabou = "Acabou";
        byte[] fim = acabou.getBytes();
        output.write(fim);
    }

    @Override
    public void run() {
        System.out.println("produtos.Produtos.java.run(): Rodando");
        try {
            mostrarItens();
            String acabou = "Acabou";
            byte[] fim = acabou.getBytes();
            output.write(fim);
            byte[] buff = new byte[2048];
            input.read(buff);
            int opcao = Integer.parseInt(Tratamento.trataEntrada(buff));
            if (opcao != -1) {
                pc.filaMensageria(opcao, login);
            }
        } catch (IOException ex) {
            System.out.println("produtos.Produtos.java.run(): ERRO: " + ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Produtos.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("produtos.Produtos.java.run(): Finalizado");
    }

    private void mostrarItens() throws IOException, InterruptedException {
        int cont = 1;
        System.out.println("produtos.Produtos.java.mostrarItens(): Mostrando");
        for (Item i : pc.getItens()) {
            if (i.getEstoque() > 0) {
                String enviar = cont + ";" + i.getNome() + ";" + i.getPreco();
                byte[] item = enviar.getBytes();
                output.write(item);
                cont++;
            }
            Thread.sleep(10);
        }
    }
}
