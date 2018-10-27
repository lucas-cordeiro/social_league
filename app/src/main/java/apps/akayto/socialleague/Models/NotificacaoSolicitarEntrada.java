package apps.akayto.socialleague.Models;

import java.io.Serializable;

/**
 * Created by LUCASGABRIELALVESCOR on 25/03/2018.
 */

public class NotificacaoSolicitarEntrada extends Notificacao implements Serializable{

    private String membroId;

    public NotificacaoSolicitarEntrada(String title, String menssagem, String tipo, boolean status, String membroId) {
        super(title, menssagem, tipo, status);
        this.membroId = membroId;
    }

    public String getMembroId() {
        return membroId;
    }

    public void setMembroId(String membroId) {
        this.membroId = membroId;
    }
}
