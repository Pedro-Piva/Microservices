
package cadastro;

import tratar.Tratamento;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pedro
 */
public class Cadastrar extends Thread {

    Socket user;
    DataInputStream input;
    DataOutputStream output;
    Cadastro cadastro;

    public Cadastrar(Socket user, Cadastro cadastro) throws IOException {
        this.user = user;
        this.input = new DataInputStream(user.getInputStream());
        this.output = new DataOutputStream(user.getOutputStream());
        this.cadastro = cadastro;

        System.out.println("cadastro.Cadastrar.java: criado");
    }

    @Override
    public void run() {
        System.out.println("cadastro.Cadastrar.java.run(): Thread iniciada");
        boolean cadastrado = false;
        while (!cadastrado) {
            try {
                //Enviar para o Cliente
                String enviar = "Informe um Login Valido: ";
                byte[] loginEnviar = enviar.getBytes();
                output.write(loginEnviar);

                //Receber do Cliente
                byte[] buf = new byte[1024];
                input.read(buf);
                String login = Tratamento.trataEntrada(buf);
                System.out.println("cadastro.Cadastrar.java.run(): User " + login);

                //Verificacao de login repetido
                if (cadastro.verificaLogin(login)) {
                    //Enviar para o Cliente
                    enviar = "Informe uma Senha Valida: ";
                    System.out.println("cadastro.Cadastrar.java.run(): " + enviar);
                    byte[] senhaEnviar = enviar.getBytes();
                    output.write(senhaEnviar);

                    //Receber do Cliente
                    buf = new byte[1024];
                    input.read(buf);
                    String senha = Tratamento.trataEntrada(buf);
                    System.out.println("cadastro.Cadastrar.java.run(): Senha " + senha);

                    //Add cadastro no BD
                    cadastro.addUser(login, senha);
                    
                    System.out.println("cadastro.Cadastrar.java.run(): Login: " + login + " Senha: " + senha);

                    //Finalizar loop
                    cadastrado = true;
                }
            } catch (IOException ex) {
                Logger.getLogger(Cadastrar.class.getName()).log(Level.SEVERE, null, ex);
                cadastrado = true;
                System.out.println("cadastro.Cadastrar.java.run(): Erro " + ex);
            }
        }
        System.out.println("cadastro.Cadastrar.java.run(): Cadastro Finalizado!");
    }

}
