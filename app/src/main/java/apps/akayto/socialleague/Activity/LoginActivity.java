package apps.akayto.socialleague.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.Control.UserControl;
import apps.akayto.socialleague.Helper.Dados;
import apps.akayto.socialleague.MainActivity;
import apps.akayto.socialleague.Models.UsuarioLogado;
import apps.akayto.socialleague.R;

public class LoginActivity extends AppCompatActivity {

    private static final String ARQUIVO = "PREFERENCIAS_USUARIO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Esconder teclado
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final FirebaseAuth firebaseAuth = FirebaseConfiguracoes.getFirebaseAuth();
        firebaseAuth.signOut();

        final Button bnt_Entrar = findViewById(R.id.login_BntEntrar);

        TextView txt_Cadastrar = findViewById(R.id.login_TxtCadastrar);
        TextView txt_RecuperarSenha = findViewById(R.id.login_TxtEsqueciSenha);

        final EditText edt_Email = findViewById(R.id.login_EdtEmail);
        final EditText edt_Senha = findViewById(R.id.login_EdtSenha);

        final ProgressBar progressBar = findViewById(R.id.login_ProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);


        final CheckBox checkBox = findViewById(R.id.login_CheckBox);

        bnt_Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_Email.getText().toString();
                String senha = edt_Senha.getText().toString();

                View parentLayout = findViewById(R.id.containerLogin);

                UserControl.logarUser(parentLayout, progressBar, firebaseAuth, LoginActivity.this, MainActivity.class, email, senha, bnt_Entrar, checkBox.isChecked());

            }
        });

        txt_Cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadastrarActivity.class));
            }
        });

        txt_RecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_Email.getText().toString();

                View parentLayout = findViewById(R.id.containerLogin);
                UserControl.recuperarSenha(parentLayout, progressBar, email, firebaseAuth, LoginActivity.this);
            }
        });

    }
}
