package apps.akayto.socialleague.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.MainActivity;
import apps.akayto.socialleague.Models.NotificacaoSolicitarEntrada;
import apps.akayto.socialleague.Models.Usuario;
import apps.akayto.socialleague.Models.UsuarioLogado;
import apps.akayto.socialleague.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String ARQUIVO = "PREFERENCIAS_USUARIO";

    private static final String TAG = "INFO_SPLASH";

    private LogarUser logarUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        ProgressBar progressBar = findViewById(R.id.splash_ProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        logarUser = new LogarUser();
        logarUser.execute();
    }

    private class LogarUser extends AsyncTask<Void, Void, Long> {

        @Override
        protected Long doInBackground(Void... voids) {
            final FirebaseAuth firebaseAuth = FirebaseConfiguracoes.getFirebaseAuth();

            SharedPreferences preferences = getSharedPreferences(ARQUIVO, 0);
            if (preferences.contains("ContinuarLogado")) {
                if (preferences.getBoolean("ContinuarLogado", false)) {
                    String email = preferences.getString("Email", "");
                    String senha = preferences.getString("Senha", "");
                    firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseAuth firebaseAuth = FirebaseConfiguracoes.getFirebaseAuth();
                                final FirebaseUser user = firebaseAuth.getCurrentUser();

                                //Token
                                UsuarioLogado.setFirebaseToken(FirebaseInstanceId.getInstance().getToken());
                                DatabaseReference referenceUser = FirebaseConfiguracoes.getFirebaseDatabase().child("User/" + user.getUid() + "/FirebaseToken");
                                referenceUser.setValue(UsuarioLogado.getFirebaseToken()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.i(TAG,"isSuccessful()");


                                            final DatabaseReference referenceUserLogado = FirebaseConfiguracoes.getFirebaseDatabase().child("UserLogado");
                                            referenceUserLogado.child(user.getUid()).child("email").setValue(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.i(TAG,"isSuccessful()");

                                                        referenceUserLogado.orderByKey().equalTo(user.getUid()).addChildEventListener(new ChildEventListener() {
                                                            @Override
                                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                                                Log.i(TAG,"onChildAdded: "+usuario.getNome());
                                                                if(usuario.getNome()!=null)
                                                                    if(usuario.getNome().length()>0) {
                                                                        Log.i(TAG, "onChildAdded: " + usuario.getTime());

                                                                        UsuarioLogado.setUser(usuario);//Set define que vai receber notificacoes
                                                                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                                                        if (getIntent().getExtras() != null)
                                                                            if (getIntent().getExtras().containsKey("notificacao"))
                                                                                intent.putExtra("notificacao", (NotificacaoSolicitarEntrada)getIntent().getExtras().get("notificacao"));
                                                                        startActivity(intent);
                                                                        finish();

                                                                        referenceUserLogado.removeEventListener(this);
                                                                    }
                                                            }

                                                            @Override
                                                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                                                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                                                Log.i(TAG,"onChildChanged: "+usuario.getNome());
                                                                if(usuario.getNome()!=null)
                                                                    if(usuario.getNome().length()>0){
                                                                        Log.i(TAG,"onChildAdded: "+usuario.getTime());

                                                                        UsuarioLogado.setUser(usuario);//Set define que vai receber notificacoes
                                                                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                                                        if (getIntent().getExtras() != null)
                                                                            if (getIntent().getExtras().containsKey("notificacao"))
                                                                                intent.putExtra("notificacao", (NotificacaoSolicitarEntrada)getIntent().getExtras().get("notificacao"));
                                                                        startActivity(intent);
                                                                        finish();

                                                                        referenceUserLogado.removeEventListener(this);
                                                                    }
                                                            }

                                                            @Override
                                                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                            }

                                                            @Override
                                                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {
                                                                login();
                                                            }
                                                        });

                                                    }else{
                                                        Log.i(TAG,"Error: "+task.getException().getClass().getSimpleName());

                                                        login();
                                                    }
                                                }
                                            });
                                        }else{
                                            login();
                                        }
                                    }
                                });


                            } else {
                                login();
                            }
                        }
                    });
                } else {
                    login();
                }
            } else {
                login();
            }
            return null;
        }
    }

    public void login() {
        if (logarUser != null)
            if (!logarUser.isCancelled()) {
                logarUser.cancel(true);
            }
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void main() {
        if (logarUser != null)
            if (!logarUser.isCancelled()) {
                logarUser.cancel(true);
            }
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        if (logarUser != null)
            if (!logarUser.isCancelled()) {
                logarUser.cancel(true);
                super.onDestroy();
            } else {
                super.onDestroy();
            }
        else {
            super.onDestroy();
        }
    }
}
