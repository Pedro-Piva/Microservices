package produtos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import tratar.Tratamento;

/**
 *
 * @author pedro
 */
public class ProdutoController extends Thread {

    private final ServerSocket servidor;
    private boolean isOn;
    private Socket mensageria;
    private ArrayList<Item> itens;

    public ProdutoController() throws IOException {
        itens = new ArrayList();
        criarEstoque();
        this.servidor = new ServerSocket(8083);

        int porta = 9999;
        InetAddress ip = InetAddress.getByName("localhost");
        mensageria = new Socket(ip, porta);

        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        String enviar = "Produto";
        byte[] tipoEnviar = enviar.getBytes();
        output.write(tipoEnviar);

        isOn = true;
        System.out.println("produtos.ProdutoController.java criado");
    }

    public void filaMensageria(int opcao, String login) throws IOException {
        String enviar = login + ":" + itens.get(opcao).getNome() + ":" + itens.get(opcao).getPreco();
        byte[] buf = enviar.getBytes();
        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        output.write(buf);
        System.out.println("produtos.ProdutoController.java.filaMensageria(): " + "{" + enviar + "}");
    }

    @Override
    public void run() {
        System.out.println("produtos.ProdutoController.java.run(): Thread iniciada");
        try {
            while (true) {
                Socket user = servidor.accept();
                System.out.println("produtos.ProdutoController.java.run(): Cliente " + user.getInetAddress() + ":" + user.getPort() + " Conectado");
                DataInputStream input = new DataInputStream(user.getInputStream());
                byte[] buff = new byte[2048];
                input.read(buff);
                String login = Tratamento.trataEntrada(buff);
                if (login.equals("NULL")) {
                    Produtos prod = new Produtos(user, this);
                    System.out.println("produtos.ProdutoController.java.run(): login: NULL");
                } else {
                    Produtos produtos = new Produtos(user, this, login);
                    System.out.println("produtos.ProdutoController.java.run(): login: " + login);
                    produtos.start();
                }
            }
        } catch (IOException ex) {
            System.out.println("produtos.ProdutoController.java.run(): ERRO: " + ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Item> getItens() {
        return itens;
    }

    private void criarEstoque() {
        Item i = new Item("Celular A", 1499.99, 1230);
        itens.add(i);
        i = new Item("Celular B", 8999.99, 3230);
        itens.add(i);
        i = new Item("Notebook A", 2999.99, 330);
        itens.add(i);
        i = new Item("Notebook B", 7999.99, 516);
        itens.add(i);
        i = new Item("Monitor A", 599.99, 2013);
        itens.add(i);
        i = new Item("Monitor B", 1499.99, 1080);
        itens.add(i);
        i = new Item("Computador A", 13499.99, 1230);
        itens.add(i);
    }
}
