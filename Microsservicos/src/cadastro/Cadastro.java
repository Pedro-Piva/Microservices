package cadastro;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pedro
 */
public class Cadastro extends Thread {

    private final ServerSocket servidor;
    boolean isOn;
    ArrayList<ArrayList<String>> bd = new ArrayList();
    Socket mensageria;

    public Cadastro() throws IOException {
        this.servidor = new ServerSocket(8081);
        ArrayList<String> login = new ArrayList();
        login.add("adm");
        ArrayList<String> senha = new ArrayList();
        senha.add("0000");
        bd.add(login);
        bd.add(senha);
        System.out.println("cadastro.Cadastro.java: " + bd.get(0) + " " + bd.get(1));

        int porta = 9999;
        InetAddress ip = InetAddress.getByName("localhost");
        mensageria = new Socket(ip, porta);
        
        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        String enviar = "Cadastro";
        byte[] tipoEnviar = enviar.getBytes();
        output.write(tipoEnviar);
        
        System.out.println("cadastro.Cadastro.java: criado");
        isOn = true;
    }

    public void closeServer() throws IOException {
        isOn = false;
    }

    public void openServer() throws IOException {
        isOn = true;
    }

    public void addUser(String login, String senha) throws IOException {
        this.bd.get(0).add(login);
        this.bd.get(1).add(senha);
        mandarMensageria(login, senha);
        System.out.println("cadastro.Cadastro.java.addUser() " + login + ":" + senha);
    }

    public boolean verificaLogin(String login) {
        for (String s : bd.get(0)) {
            if (s.equals(login)) {
                System.out.println("cadastro.Cadastro.java.VerificaLogin() " + login + " Ja existente");
                return false;
            }
        }
        System.out.println("cadastro.Cadastro.java.VerificaLogin() " + login + " verificado");
        return true;
    }

    public void mandarMensageria(String login, String senha) throws IOException {
        //DataInputStream input = new DataInputStream(mensageria.getInputStream());
        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        String enviar = login + ":" + senha;
        byte[] loginEnviar = enviar.getBytes();
        output.write(loginEnviar);
    }

    @Override
    public void run() {
        System.out.println("cadastro.Cadastro.java.run(): Thread Rodando");
        try {
            while (true) {
                if (isOn) {
                    Socket user = servidor.accept();
                    System.out.println("cadastro.Cadastro.java.run(): Cliente " + user.getInetAddress() + " Conectado");
                    Cadastrar cadastrar = new Cadastrar(user, this);
                    cadastrar.start();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Cadastro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
