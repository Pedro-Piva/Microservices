/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import tratar.Tratamento;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author pedro
 */
public class LoginDatabase extends Thread {

    LoginController lc;
    DataInputStream input;

    public LoginDatabase(LoginController lc, Socket mensageria) throws IOException {
        this.lc = lc;
        this.input = new DataInputStream(mensageria.getInputStream());
        this.start();
        System.out.println("login.LoginDatabase.java criado");
    }

    @Override
    public void run() {
        System.out.println("login.LoginDatabase.java.run() Thread iniciada ");
        try {
            while (true) {
                byte[] buf = new byte[8192];
                input.read(buf);
                String in = Tratamento.trataEntrada(buf);
                System.out.println("login.LoginDatabase.java.run(): " + in);
                String[] split = in.split(":");
                if (split[0].equals("CADASTRO")) {
                    String login = split[1];
                    String senha = split[2];
                    lc.addDatabase(login, senha);
                }
                String login = split[1];
                String senha = split[2];
                System.out.println("login.LoginDatabase.java.run() Thread login: " + login + " Senha: " + senha);
            }
        } catch (IOException ex) {
            System.out.println("login.LoginDatabase.java.run(): ERRO: " + ex);
        }
    }
}
