package produtos;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import tratar.Tratamento;

/**
 *
 * @author pedro
 */
public class ProdutoDatabase extends Thread {

    private final ProdutoController pc;
    private final DataInputStream input;

    public ProdutoDatabase(ProdutoController pc, Socket mensageria) throws IOException {
        this.pc = pc;
        this.input = new DataInputStream(mensageria.getInputStream());
        this.start();
        System.out.println("produtos.ProdutoDatabase.java criado");
    }

    @Override
    public void run() {
        System.out.println("produtos.ProdutoDatabase..java.run() Thread iniciada ");
        try {
            while (true) {
                byte[] buf = new byte[8192];
                input.read(buf);
                String in = Tratamento.trataEntrada(buf);
                System.out.println("produtos.ProdutoDatabase.java.run(): " + in);
                String[] split = in.split(":");
                if (split[0].equals("LOGIN")) {
                    String login = split[1];
                    System.out.println("produtos.ProdutoDatabase.java.run() Thread login: " + login);
                    pc.addLogin(login);
                } else if (split[0].equals("PAGAMENTO")) {
                    String produto = split[1];
                    int quantidade = Integer.parseInt(split[2]);
                    pc.diminuiEstoque(produto, quantidade);
                }
                System.out.println("produtos.ProdutoDatabase.java.run() Thread Finalizada");
            }
        } catch (IOException ex) {
            System.out.println("produtos.ProdutoDatabase.java.run(): ERRO: " + ex);
        }
    }
}
