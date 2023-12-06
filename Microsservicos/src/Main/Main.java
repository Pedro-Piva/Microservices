package Main;

import cadastro.Cadastro;
import login.LoginController;
import produtos.ProdutoController;
import pagamento.PagamentoController;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author pedro
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Main.Main.java.main: criado");
        Cadastro cadastro = new Cadastro();
        cadastro.start();
        LoginController lc = new LoginController();
        lc.start();
        ProdutoController pc = new ProdutoController();
        pc.start();
        PagamentoController pac = new PagamentoController();
        pac.start();
        Scanner sc = new Scanner(System.in);
        String entrada = sc.nextLine();

    }
}
