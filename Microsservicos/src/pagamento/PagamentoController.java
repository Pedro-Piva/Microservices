package pagamento;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import produtos.Item;
import tratar.Tratamento;

/**
 *
 * @author pedro
 */
public class PagamentoController extends Thread {

    private ArrayList<User> users;
    private final ServerSocket servidor;
    private boolean isOn;
    private Socket mensageria;
    private ArrayList<Item> itens;

    public PagamentoController() throws IOException {
        this.servidor = new ServerSocket(8084);

        int porta = 9999;
        InetAddress ip = InetAddress.getByName("localhost");
        mensageria = new Socket(ip, porta);

        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        String enviar = "Pagamento";
        byte[] tipoEnviar = enviar.getBytes();
        output.write(tipoEnviar);
        
        PagamentoDatabase pd = new PagamentoDatabase(this, mensageria);
        users = new ArrayList();
        User adm = new User("adm", 999999, "0000");
        users.add(adm);
        System.out.println("pagamento.PagamentoController.java Criado");
    }

    public void mensageriaProduto(String produto, int quantidade) throws IOException{
        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        String saida = "PRODUTOS/PAGAMENTO/" +  produto + ":" + quantidade;
        byte[] buf = saida.getBytes();
        output.write(buf);
    }
    
    public ArrayList<User> getUsers() {
        return users;
    }
    
    public User findUser(String name){
        for(User u: users){
            if(name.equals(u.getLogin())){
                return u;
            }
        }
        return null;
    }

    public void addUser(User user) {
        this.users.add(user);
        System.out.println("pagamento.PagamentoController.java.addUser(): Adicionando User: " + user.getLogin()  + " "+ user.getSaldo());
    }

    @Override
    public void run() {
        System.out.println("pagamento.PagamentoController.java.run(): Thread iniciada");
        try {
            while (true) {
                Socket user = servidor.accept();
                System.out.println("pagamento.PagamentoController.java.run(): Cliente " + user.getInetAddress() + ":" + user.getPort() + " Conectado");
                DataInputStream input = new DataInputStream(user.getInputStream());
                byte[] buff = new byte[4096];
                input.read(buff);
                String login = Tratamento.trataEntrada(buff);
                String[] split = null;
                try{
                    split = login.split(":");
                    System.out.println(split[1]);
                } catch(Exception e){
                    split = null;
                }
                if(split == null){
                    Pagamento pg = new Pagamento(this, user, login);
                } else{
                    System.out.println(Arrays.toString(split));
                    Pagamento pg = new Pagamento(this, user, split[0], Float.parseFloat(split[1]));
                }
            }
        } catch (IOException ex) {
            System.out.println("produtos.ProdutoController.java.run(): ERRO: " + ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(PagamentoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
