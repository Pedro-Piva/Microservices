package pagamento;

import java.util.ArrayList;

/**
 *
 * @author pedro
 */
public class User {

    private String login;
    private ArrayList<Produto> produtos;
    float saldo;
    private String senha;

    public User(String login, float saldo, String senha) {
        this.login = login;
        this.produtos = new ArrayList();
        this.saldo = saldo;
        this.senha = senha;
    }

    public void clearProdutos(){
        this.produtos.clear();
    }
    
    public String getSenha() {
        return senha;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public float getSaldo() {
        return saldo;
    }

    public String getLogin() {
        return login;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(String produto, float preco) {
        Produto pr = null;
        for (Produto p : produtos) {
            if (p.getNome().equals(produto)) {
                pr = p;
            }
        }
        if(pr == null){
            pr = new Produto(preco, produto);
            produtos.add(pr);
            System.out.println("pagamento.User.setProdutos(): novo Produto: " + produto);
        } else {
            System.out.println("pagamento.User.setProdutos(): Produto antigo: " + produto);
            pr.addQuantidade();
        }

    }

}
