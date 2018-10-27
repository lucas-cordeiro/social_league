package apps.akayto.socialleague.Models;

/**
 * Created by LUCASGABRIELALVESCOR on 13/03/2018.
 */

public class NovoUsuario extends Usuario {

    private String confimacaoSenha;

    public NovoUsuario(String email, String nome, String nickLol, String senha, String confimacaoSenha) {
        super(email, nome, nickLol, senha, 0,0,0, "nd");
        this.confimacaoSenha = confimacaoSenha;
    }

    public NovoUsuario(String confimacaoSenha) {
        this.confimacaoSenha = confimacaoSenha;
    }

    public String getConfimacaoSenha() {
        return confimacaoSenha;
    }

    public void setConfimacaoSenha(String confimacaoSenha) {
        this.confimacaoSenha = confimacaoSenha;
    }
}
