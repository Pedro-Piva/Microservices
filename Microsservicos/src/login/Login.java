package login;

import tratar.Tratamento;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author pedro
 */
public class Login extends Thread {

    LoginController lc;
    Socket user;
    DataInputStream input;
    DataOutputStream output;

    public Login(Socket user, LoginController login) throws IOException {
        this.user = user;
        this.input = new DataInputStream(user.getInputStream());
        this.output = new DataOutputStream(user.getOutputStream());
        this.lc = login;
        System.out.println("login.Login.java Criado");
    }

    @Override
    public void run() {
        try {

            byte[] buf = new byte[4096];
            input.read(buf);
            String login = Tratamento.trataEntrada(buf);
            System.out.println("login.Login.java.run(): Login: " + login);

            buf = new byte[4096];
            input.read(buf);
            String senha = Tratamento.trataEntrada(buf);
            System.out.println("login.Login.java.run(): Senha: " + senha);

            if (lc.verificaLoginSenha(login, senha)) {
                System.out.println("login.Login.java.run(): Login, senha Validos");
                String enviar = "Logou!";
                byte[] logou = enviar.getBytes();
                output.write(logou);
                lc.mandarMensageriaProduto(login, senha);
            } else {
                String enviar = "ERRO";
                byte[] logou = enviar.getBytes();
                output.write(logou);
                System.out.println("login.Login.java.run(): Login, senha Invalidos");
            }

        } catch (IOException ex) {
            System.out.println("login.Login.java.run(): Erro " + ex);
        }
    }
}
