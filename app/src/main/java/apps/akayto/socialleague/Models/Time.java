package apps.akayto.socialleague.Models;

/**
 * Created by LUCASGABRIELALVESCOR on 17/03/2018.
 */

public class Time {
    private String nome;
    private int campeonatos;
    private int vitorias;
    private int quantMembros;
    private String divisao;
    private String lider;
    private String membro1;
    private String membro2;
    private String membro3;
    private String membro4;
    private String membro5;
    private String membro6;

    public Time() {
    }

    public Time(String nome, int campeonatos, int vitorias, int quantMembros, String divisao, String lider, String membro1, String membro2, String membro3, String membro4, String membro5, String membro6) {
        this.nome = nome;
        this.campeonatos = campeonatos;
        this.vitorias = vitorias;
        this.quantMembros = quantMembros;
        this.divisao = divisao;
        this.lider = lider;
        this.membro1 = membro1;
        this.membro2 = membro2;
        this.membro3 = membro3;
        this.membro4 = membro4;
        this.membro5 = membro5;
        this.membro6 = membro6;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public int getQuantMembros() {
        return quantMembros;
    }

    public void setQuantMembros(int quantMembros) {
        this.quantMembros = quantMembros;
    }

    public String getDivisao() {
        return divisao;
    }

    public void setDivisao(String divisao) {
        this.divisao = divisao;
    }

    public String getLider() {
        return lider;
    }

    public void setLider(String lider) {
        this.lider = lider;
    }

    public String getMembro1() {
        return membro1;
    }

    public void setMembro1(String membro1) {
        this.membro1 = membro1;
    }

    public String getMembro2() {
        return membro2;
    }

    public void setMembro2(String membro2) {
        this.membro2 = membro2;
    }

    public String getMembro3() {
        return membro3;
    }

    public void setMembro3(String membro3) {
        this.membro3 = membro3;
    }

    public String getMembro4() {
        return membro4;
    }

    public void setMembro4(String membro4) {
        this.membro4 = membro4;
    }

    public String getMembro5() {
        return membro5;
    }

    public void setMembro5(String membro5) {
        this.membro5 = membro5;
    }

    public String getMembro6() {
        return membro6;
    }

    public void setMembro6(String membro6) {
        this.membro6 = membro6;
    }
}
