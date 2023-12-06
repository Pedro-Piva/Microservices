package pagamento;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author pedro
 */
public class Pagamento {

    PagamentoController pc;
    DataInputStream input;
    DataOutputStream output;
    String login;

    public Pagamento(PagamentoController pc, Socket user, String login) throws IOException, InterruptedException {
        this.pc = pc;
        input = new DataInputStream(user.getInputStream());
        output = new DataOutputStream(user.getOutputStream());
        this.login = login;
        System.out.println("pagamento.Pagamento.java: Apenas MOSTRAR");
        mostraProdutos();
    }

    public Pagamento(PagamentoController pc, Socket user, String login, float dinheiro) throws IOException {
        this.pc = pc;
        input = new DataInputStream(user.getInputStream());
        output = new DataOutputStream(user.getOutputStream());
        this.login = login;
        System.out.println("pagamento.Pagamento.java: Criado");
        pagando(dinheiro);
    }

    private void pagando(float dinheiro) throws IOException {
        User u = pc.findUser(login);
        float total = 0;
        for (Produto p : u.getProdutos()) {
            pc.mensageriaProduto(p.getNome(), p.getQuantidade());
            total += p.getPreco() * p.getQuantidade();
        }
        u.setSaldo(dinheiro - total);
        u.clearProdutos();
    }

    private void mostraProdutos() throws IOException, InterruptedException {
        User u = pc.findUser(login);
        String primeiro = u.getLogin() + ":" + u.getSaldo() + ":" + u.getSenha();
        byte[] buff = primeiro.getBytes();
        output.write(buff);
        Thread.sleep(10);
        for (Produto p : u.getProdutos()) {
            String enviar = p.getNome() + ":" + p.getPreco() + ":" + p.getQuantidade();
            byte[] buf = enviar.getBytes();
            output.write(buf);
            Thread.sleep(10);
        }
        String enviar = "FIM";
        byte[] buf = enviar.getBytes();
        output.write(buf);
    }
}
