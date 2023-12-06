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
    ArrayList<User> bd = new ArrayList();
    Socket mensageria;

    public Cadastro() throws IOException {
        this.servidor = new ServerSocket(8081);
        User adm = new User("adm", "adm", "0000", 100, 999999);
        bd.add(adm);
        System.out.println("cadastro.Cadastro.java: " + bd.get(0).getLogin() + " " + bd.get(0).getSenha());

        int porta = 9999;
        InetAddress ip = InetAddress.getByName("localhost");
        mensageria = new Socket(ip, porta);

        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        String enviar = "Cadastro";
        byte[] tipoEnviar = enviar.getBytes();
        output.write(tipoEnviar);

        System.out.println("cadastro.Cadastro.java: criado");
    }

    public void addUser(String nome, String login, String senha, int idade, float saldo) throws IOException {
        User u = new User(nome, login, senha, idade, saldo);
        bd.add(u);
        mandarMensageriaLogin(login, senha);
        mandarMensageriaPagamento(login, senha, saldo);
        System.out.println("cadastro.Cadastro.java.addUser() " + login + ":" + senha);
    }

    public boolean verificaLogin(String login) {
        for (User u : bd) {
            String s = u.getLogin();
            if (s.equals(login)) {
                System.out.println("cadastro.Cadastro.java.VerificaLogin() " + login + " Ja existente");
                return false;
            }
        }
        System.out.println("cadastro.Cadastro.java.VerificaLogin() " + login + " verificado");
        return true;
    }

    public void mandarMensageriaLogin(String login, String senha) throws IOException {
        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        String enviar = "LOGIN/CADASTRO/" + login + ":" + senha;
        byte[] loginEnviar = enviar.getBytes();
        output.write(loginEnviar);
    }

    public void mandarMensageriaPagamento(String login, String senha, float saldo) throws IOException {
        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        String enviar = "PAGAMENTO/CADASTRO/" + login + ":" + senha + ":" + saldo;
        byte[] loginEnviar = enviar.getBytes();
        output.write(loginEnviar);
    }

    @Override
    public void run() {
        System.out.println("cadastro.Cadastro.java.run(): Thread Rodando");
        try {
            while (true) {
                Socket user = servidor.accept();
                System.out.println("cadastro.Cadastro.java.run(): Cliente " + user.getInetAddress() + " Conectado");
                Cadastrar cadastrar = new Cadastrar(user, this);
                cadastrar.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Cadastro.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("cadastro.Cadastro.java.run(): Thread Finalizada");
    }
}
