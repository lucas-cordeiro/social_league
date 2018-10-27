package apps.akayto.socialleague.Models;

/**
 * Created by LUCASGABRIELALVESCOR on 19/03/2018.
 */

public class Membros {

    private int icone;
    private String nome;
    private String id;
    private int campeonatos;
    private int vitorias;

    public Membros(int icone, String nome, String id, int campeonatos, int vitorias) {
        this.icone = icone;
        this.nome = nome;
        this.id = id;
        this.campeonatos = campeonatos;
        this.vitorias = vitorias;
    }

    public Membros() {
    }

    public int getIcone() {
        return icone;
    }

    public void setIcone(int icone) {
        this.icone = icone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
