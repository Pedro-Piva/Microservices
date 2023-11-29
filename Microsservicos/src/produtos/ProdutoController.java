/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package produtos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.ArrayList;

/**
 *
 * @author pedro
 */
public class ProdutoController extends Thread{

    private final ServerSocket servidor;
    boolean isOn;
    Socket mensageria;
    
    public ProdutoController() throws IOException{
        this.servidor = new ServerSocket(8083);
        
        int porta = 9999;
        InetAddress ip = InetAddress.getByName("localhost");
        mensageria = new Socket(ip, porta);
        
        DataOutputStream output = new DataOutputStream(mensageria.getOutputStream());
        String enviar = "Produto";
        byte[] tipoEnviar = enviar.getBytes();
        output.write(tipoEnviar);

        isOn = true;
        System.out.println("produtos.ProdutoController.java criado");
    }
    
    @Override
    public void run(){
        try {
            System.out.println("produtos.ProdutoController.java.run(): Thread iniciada");
            Socket user = servidor.accept();
            System.out.println("produtos.ProdutoController.java.run(): Cliente " + user.getInetAddress() + " Conectado");
        } catch (IOException ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
