package login;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author pedro
 */
public class LoginController extends Thread {

    private final ServerSocket servidor;
    boolean isOn;
    ArrayList<ArrayList<String>> bd = new ArrayList();
    Socket mensageria;

    public LoginController() throws IOException {
        this.servidor = new ServerSocket(8082);
        ArrayList<String> login = new ArrayList();
        login.add("adm");
        ArrayList<String> senha = new ArrayList();
        senha.add("0000");
        bd.add(login);
        bd.add(senha);
        
        int porta = 9999;
        InetAddress ip = InetAddress.getByName("localhost");
        mensageria = new Socket(ip, porta);
        
        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        String enviar = "Login";
        byte[] tipoEnviar = enviar.getBytes();
        output.write(tipoEnviar);
        
        LoginDatabase ld = new LoginDatabase(this, mensageria);

        isOn = true;
        System.out.println("login.LoginController.java criado");
    }

    public void addDatabase(String login, String senha) {
        bd.get(0).add(login);
        bd.get(1).add(senha);
        System.out.println(bd);
    }
    
    public boolean verificaLoginSenha(String login, String senha){
        for(String s: bd.get(0)){
            if(s.equals(login)){
                if(senha.equals(bd.get(1).get(bd.get(0).indexOf(s)))){
                    return true;
                }
            }
        }
        return false;
    }

    public void logou(String login, String senha) throws IOException{
        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        String enviar = login + ":" + senha + ":True";
        byte[] tipoEnviar = enviar.getBytes();
        output.write(tipoEnviar);
    }
    
    @Override
    public void run() {
        System.out.println("login.LoginController.java.run() Thread iniciada");
        try {
            while (true) {
                if (isOn) {
                    Socket user = servidor.accept();
                    System.out.println("login.LoginController.java.run(): Cliente " + user.getInetAddress() + " Conectado");
                    Login login = new Login(user, this);
                    login.start();
                }
            }
        } catch (IOException ex) {
            System.out.println("login.LoginController.java.run(): Erro " + ex);
        }
    }
}
