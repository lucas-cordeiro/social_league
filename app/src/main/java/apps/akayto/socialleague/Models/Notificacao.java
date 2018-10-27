package apps.akayto.socialleague.Models;

import java.io.Serializable;

/**
 * Created by LUCASGABRIELALVESCOR on 19/03/2018.
 */

public class Notificacao implements Serializable {
    private String title;
    private String menssagem;
    private String tipo;
    private boolean status;

    public Notificacao(String title, String menssagem, String tipo, boolean status) {
        this.title = title;
        this.menssagem = menssagem;
        this.tipo = tipo;
        this.status = status;
    }

    public Notificacao() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMenssagem() {
        return menssagem;
    }

    public void setMenssagem(String menssagem) {
        this.menssagem = menssagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
