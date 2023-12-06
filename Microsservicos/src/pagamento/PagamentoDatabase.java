package pagamento;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import tratar.Tratamento;

/**
 *
 * @author pedro
 */
public class PagamentoDatabase extends Thread{
    
    private PagamentoController pc;
    private DataInputStream input;
    
    public PagamentoDatabase(PagamentoController pc, Socket mensageria) throws IOException{
        this.pc = pc;
        this.input = new DataInputStream(mensageria.getInputStream());
        this.start();
        System.out.println("pagamento.PagamentoDatabase.java criado");
    }
    
    @Override
    public void run(){
        System.out.println("pagamento.PagamentoDatabase.java.run(): Thread iniciada");
        try {
            while (true) {
                byte[] buf = new byte[8192];
                input.read(buf);
                String in = Tratamento.trataEntrada(buf);
                System.out.println("pagamento.PagamentoDatabase.java.run(): " + in);
                String[] split = in.split(":");
                if(split[0].equals("CADASTRO")){
                    String login = split[1];
                    String senha = split[2];
                    float saldo = Float.parseFloat(split[3]);
                    User u = new User(login, saldo, senha);
                    pc.addUser(u);
                } else if(split[0].equals("PRODUTO")){
                    String login = split[1];
                    String produto = split[2];
                    float preco = Float.parseFloat(split[3]);
                    User u = getUser(login);
                    if(u != null){
                        u.setProdutos(produto, preco);
                    }
                    System.out.println("pagamento.PagamentoDatabase.java.run(): Adicionando Produto: " + produto);
                }
            }
        } catch (IOException ex) {
            System.out.println("produtos.ProdutoDatabase.java.run(): ERRO: " + ex);
        }
    }
    
    public User getUser(String login){
        for(User u: pc.getUsers()){
            if(u.getLogin().equals(login)){
                return u;
            }
        }
        return null;
    }
}
