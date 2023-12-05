package cadastro;

import tratar.Tratamento;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
        try {
            byte[] buf = new byte[4096];
            input.read(buf);
            String nome = Tratamento.trataEntrada(buf);
            System.out.println("cadastro.Cadastrar.java.run(): Nome = " + nome);

            buf = new byte[4096];
            input.read(buf);
            String login = Tratamento.trataEntrada(buf);
            System.out.println("cadastro.Cadastrar.java.run(): User = " + login);

            buf = new byte[4096];
            input.read(buf);
            String senha = Tratamento.trataEntrada(buf);
            System.out.println("cadastro.Cadastrar.java.run(): Senha = " + senha);

            buf = new byte[4096];
            input.read(buf);
            String idade = Tratamento.trataEntrada(buf);
            System.out.println("cadastro.Cadastrar.java.run(): idade = " + idade);

            buf = new byte[4096];
            input.read(buf);
            String saldo = Tratamento.trataEntrada(buf);
            System.out.println("cadastro.Cadastrar.java.run(): saldo = " + saldo);

            int i = Integer.parseInt(idade);

            float s = Float.parseFloat(saldo);

            if (cadastro.verificaLogin(login)) {
                cadastro.addUser(nome, login, senha, i, s);
                String acabou = "FIM";
                byte[] fim = acabou.getBytes();
                output.write(fim);
            } else {
                String acabou = "ERRO";
                byte[] fim = acabou.getBytes();
                output.write(fim);
            }
        } catch (IOException ex) {

            System.out.println("cadastro.Cadastrar.java.run(): Erro " + ex);
        }
        System.out.println("cadastro.Cadastrar.java.run(): Cadastro Finalizado!");
    }
}
