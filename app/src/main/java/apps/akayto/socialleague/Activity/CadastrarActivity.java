package apps.akayto.socialleague.Activity;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.Control.UserControl;
import apps.akayto.socialleague.MainActivity;
import apps.akayto.socialleague.Models.NovoUsuario;
import apps.akayto.socialleague.R;

public class CadastrarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        //Esconder teclado
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final EditText edt_Email = findViewById(R.id.cadastro_EdtEmail);
        final EditText edt_Nome = findViewById(R.id.cadastro_EdtNome);
        final EditText edt_NickLoL = findViewById(R.id.cadastro_EdtNick);
        final EditText edt_Senha = findViewById(R.id.cadastro_EdtSenha);
        final EditText edt_ConfirmacaoSenha = findViewById(R.id.cadastro_EdtConfirmarSenha);

        final ProgressBar progressBar = findViewById(R.id.cadastrar_ProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        final FirebaseAuth firebaseAuth = FirebaseConfiguracoes.getFirebaseAuth();

        final DatabaseReference databaseReference = FirebaseConfiguracoes.getFirebaseDatabase();

        final View parentLayout = findViewById(R.id.containerCadastro);

        final ArrayList<EditText> arrayList = new ArrayList<>();
        arrayList.add(edt_Email);
        arrayList.add(edt_Nome);
        arrayList.add(edt_NickLoL);
        arrayList.add(edt_Senha);
        arrayList.add(edt_ConfirmacaoSenha);

        final Button bnt_Enviar = findViewById(R.id.casdastrar_BntEnviar);
        bnt_Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edt_Email.getText().toString();
                String nome = edt_Nome.getText().toString();
                String nickLol = edt_NickLoL.getText().toString();
                String senha = edt_Senha.getText().toString();
                String confirmacaoSenha = edt_ConfirmacaoSenha.getText().toString();

                final NovoUsuario novoUsuario = new NovoUsuario(email, nome, nickLol, senha, confirmacaoSenha);

                UserControl.cadastrarUser(parentLayout, progressBar, firebaseAuth, databaseReference, CadastrarActivity.this, VerificarEmailActivity.class, novoUsuario, arrayList, bnt_Enviar);
            }
        });
    }
}
