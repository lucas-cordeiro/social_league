package apps.akayto.socialleague.Models;

import java.io.Serializable;

/**
 * Created by LUCASGABRIELALVESCOR on 13/03/2018.
 */

public class Usuario implements Serializable{

    private String email;
    private String nome;
    private String nickLol;
    private String senha;
    private String time;
    private int icone;
    private int campeonatos;
    private int vitorias;

    public Usuario(String email, String nome, String nickLol, String senha, int icone, int campeonatos, int vitorias, String time) {
        this.email = email;
        this.nome = nome;
        this.nickLol = nickLol;
        this.senha = senha;
        this.icone = icone;
        this.campeonatos = campeonatos;
        this.vitorias = vitorias;
        this.time = time;
    }

    public Usuario() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNickLol() {
        return nickLol;
    }

    public void setNickLol(String nickLol) {
        this.nickLol = nickLol;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIcone() {
        return icone;
    }

    public void setIcone(int icone) {
        this.icone = icone;
    }

    public int getCampeonatos() {
        return campeonatos;
    }

    public void setCampeonatos(int campeonatos) {
        this.campeonatos = campeonatos;
    }

    public int getVitorias() {
        return vitorias;
    }

    public void setVitorias(int vitorias) {
        this.vitorias = vitorias;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
