/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastro;

/**
 *
 * @author pedro
 */
public class User {

    private String nome;
    private String login;
    private String senha;
    private int idade;
    private float saldo;

    public User(String nome, String login, String senha, int idade, float saldo) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.idade = idade;
        this.saldo = saldo;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public int getIdade() {
        return idade;
    }

    public float getSaldo() {
        return saldo;
    }

}
