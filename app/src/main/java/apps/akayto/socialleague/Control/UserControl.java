package apps.akayto.socialleague.Control;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import apps.akayto.socialleague.Activity.SplashScreenActivity;
import apps.akayto.socialleague.Activity.VerificarEmailActivity;
import apps.akayto.socialleague.Helper.Dados;
import apps.akayto.socialleague.MainActivity;
import apps.akayto.socialleague.Models.NovoUsuario;
import apps.akayto.socialleague.Models.Usuario;
import apps.akayto.socialleague.Models.UsuarioLogado;
import apps.akayto.socialleague.R;

/**
 * Created by LUCASGABRIELALVESCOR on 13/03/2018.
 */

public class UserControl {

    private static  String TAG ="INFO_USERCONTROL";

    private static boolean nickExists = false;

    private static boolean nickLoLExists = false;

    private static final String ARQUIVO = "PREFERENCIAS_USUARIO";

    public static void logarUser(final View parentLayout, final ProgressBar progressBar, final FirebaseAuth firebaseAuth, final Activity activity, final Class<?> activityClass, final String email, final String senha, final Button button, final boolean continuarLogado){

        button.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        if(Dados.verificaEmail(email))
        { Log.i(TAG,"verificaEmail");

            if(Dados.verificaSenha(senha)){
                Log.i(TAG,"verificaSenha");

                firebaseAuth.signOut();
                firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.i(TAG,"Logado");

                            SharedPreferences preferences = activity.getSharedPreferences(ARQUIVO, 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("ContinuarLogado",true);
                            editor.putString("Email",email);
                            editor.putString("Senha",senha);
                            editor.apply();

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            final DatabaseReference referenceUserLogado = FirebaseConfiguracoes.getFirebaseDatabase().child("UserLogado");
                            referenceUserLogado.child(user.getUid()).child("email").setValue(user.getEmail());
                            referenceUserLogado.orderByKey().equalTo(user.getUid()).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                    Log.i(TAG,"onChildAdded: "+usuario.getNome());
                                    if(usuario.getNome()!=null)
                                        if(usuario.getNome().length()>0){
                                            referenceUserLogado.removeEventListener(this);

                                            UsuarioLogado.setUser(usuario);//Set define que vai receber notificacoes
                                            activity.startActivity(new Intent(activity,activityClass));
                                            activity.finish();
                                        }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                    Log.i(TAG,"onChildChanged: "+usuario.getNome());
                                    if(usuario.getNome()!=null)
                                        if(usuario.getNome().length()>0){
                                            referenceUserLogado.removeEventListener(this);

                                            UsuarioLogado.setUser(usuario);//Set define que vai receber notificacoes
                                            activity.startActivity(new Intent(activity,activityClass));
                                            activity.finish();
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
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Snackbar.make(parentLayout, "Erro: "+databaseError.getClass().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                                    button.setEnabled(true);
                                }
                            });

                        }else{

                            String msg="";

                            if(task.getException().getClass().getSimpleName().equals("FirebaseAuthInvalidCredentialsException"))
                                msg=activity.getResources().getString(R.string.stg_Senha_Invalida);

                            else if(task.getException().getClass().getSimpleName().equals("FirebaseAuthInvalidUserException"))
                                msg=activity.getResources().getString(R.string.stg_Email_Invalido);

                            else
                                msg="Erro: "+task.getException().getClass().getSimpleName();

                            progressBar.setVisibility(View.INVISIBLE);
                            Snackbar.make(parentLayout, msg, Snackbar.LENGTH_SHORT).show();
                            button.setEnabled(true);
                        }
                    }
                });
            }else{
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_Senha_Invalida), Snackbar.LENGTH_SHORT).show();
                button.setEnabled(true);
            }
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_Email_Invalido), Snackbar.LENGTH_SHORT).show();
            button.setEnabled(true);
        }
    }

    public static void cadastrarUser(final View parentLayout, final ProgressBar progressBar, final FirebaseAuth firebaseAuth, final DatabaseReference databaseReference, final Activity activity, final Class<?> activityClass, final NovoUsuario usuario, ArrayList<EditText> arrayList, final Button button) {

        button.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);

        nickExists = false;

        nickLoLExists = false;

        if (Dados.verificaEmail(usuario.getEmail())) {
            if (usuario.getNome().length() >= 4) {
                if (Dados.verificaSenha(usuario.getSenha())) {
                    if (usuario.getSenha().equals(usuario.getConfimacaoSenha())) {
                        if (usuario.getNickLol().length() >= 4) {
                            final DatabaseReference referenceNick = FirebaseConfiguracoes.getFirebaseDatabase().child("nick");
                            referenceNick.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    if (dataSnapshot.getValue().toString().toLowerCase().equals(usuario.getNome().toLowerCase()) && !nickExists) {
                                        nickExists = true;
                                        Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_NickExistente), Snackbar.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        button.setEnabled(true);
                                    } else if (dataSnapshot.getValue().toString().equals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")) {
                                        if (!nickExists) {
                                            final DatabaseReference referenceNickLoL = FirebaseConfiguracoes.getFirebaseDatabase().child("nickLol");
                                            referenceNickLoL.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                    if (dataSnapshot.getValue().toString().toLowerCase().equals(usuario.getNickLol().toLowerCase()) && !nickLoLExists) {
                                                        nickLoLExists = true;
                                                        Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_NickLoLExistente), Snackbar.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        button.setEnabled(true);
                                                    } else if (dataSnapshot.getValue().toString().equals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")) {
                                                        if (!nickLoLExists) {
                                                            firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if (task.isSuccessful()) {
                                                                        final FirebaseUser user = task.getResult().getUser();
                                                                        final Usuario usuarioBanco = new Usuario();
                                                                        usuarioBanco.setTime("nd");
                                                                        usuarioBanco.setEmail(usuario.getEmail());
                                                                        usuarioBanco.setNome(usuario.getNome());
                                                                        usuarioBanco.setNickLol(usuario.getNickLol());
                                                                        usuarioBanco.setSenha(usuario.getSenha());
                                                                        usuarioBanco.setIcone(0);
                                                                        usuarioBanco.setCampeonatos(0);
                                                                        usuarioBanco.setVitorias(0);


                                                                        databaseReference.child("User").child(user.getUid()).setValue(usuarioBanco).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    //Token
                                                                                    UsuarioLogado.setFirebaseToken(FirebaseInstanceId.getInstance().getToken());
                                                                                    DatabaseReference referenceUser = FirebaseConfiguracoes.getFirebaseDatabase().child("User/" + user.getUid() + "/FirebaseToken");
                                                                                    referenceUser.setValue(UsuarioLogado.getFirebaseToken());

                                                                                    referenceNick.child(user.getUid()).setValue(usuarioBanco.getNome());
                                                                                    referenceNickLoL.child(user.getUid()).setValue(usuarioBanco.getNickLol());
                                                                                    databaseReference.child("User").child(user.getUid()).child("notificacoes").child("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz").setValue("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
                                                                                    Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_Sucesso), Snackbar.LENGTH_SHORT).show();

                                                                                    activity.startActivity(new Intent(activity, activityClass));
                                                                                    activity.finish();
                                                                                } else {

                                                                                    Snackbar.make(parentLayout, "Erro: " + task.getException().getClass().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                                                                                }

                                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                                button.setEnabled(true);
                                                                            }
                                                                        });

                                                                    } else {
                                                                        //Erro criar
                                                                        if (task.getException().getClass().getSimpleName().equals("FirebaseAuthUserCollisionException"))
                                                                            Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_Conta_Existente), Snackbar.LENGTH_SHORT).show();

                                                                        else
                                                                            Snackbar.make(parentLayout, "Erro: " + task.getException().getClass().getSimpleName(), Snackbar.LENGTH_SHORT).show();

                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                        button.setEnabled(true);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                                }

                                                @Override
                                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                }

                                                @Override
                                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Snackbar.make(parentLayout, "Erro: " + databaseError.toString(), Snackbar.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    button.setEnabled(true);
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Snackbar.make(parentLayout, "Erro: " + databaseError.toString(), Snackbar.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    button.setEnabled(true);
                                }
                            });
                        } else {
                            //Erro NickLoL
                            Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_NickErro), Snackbar.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            arrayList.get(2).requestFocus();
                            button.setEnabled(true);
                        }
                    } else {
                        //Erro Confirmacao Senha
                        Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_ConfirmacaoSenhaErro), Snackbar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        arrayList.get(4).requestFocus();
                        button.setEnabled(true);
                    }
                } else {
                    //Erro senha
                    Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_Senha_Invalida), Snackbar.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    arrayList.get(3).requestFocus();
                    button.setEnabled(true);
                }
            } else {
                //Erro nome
                Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_NickErro), Snackbar.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                arrayList.get(1).requestFocus();
                button.setEnabled(true);
            }
        } else {
            //Erro email
            Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_Email_Invalido), Snackbar.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            arrayList.get(0).requestFocus();
            button.setEnabled(true);
        }

    }

    public static void recuperarSenha(final View parentLayout,final ProgressBar progressBar, final String email, FirebaseAuth firebaseAuth, final Activity activity)
    {
        progressBar.setVisibility(View.VISIBLE);

        if(Dados.verificaEmail(email)) {

            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_RecuperarSucesso) + email, Snackbar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    } else if (task.getException().getClass().getSimpleName().equals("FirebaseAuthInvalidUserException")) {
                        Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_NaoEncontrado), Snackbar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);

                    } else {
                        Snackbar.make(parentLayout, "Erro: " + task.getException().getClass().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });

        }else{
            Snackbar.make(parentLayout, activity.getResources().getString(R.string.stg_Email_Invalido), Snackbar.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public static void carregarUsuario(final Activity activity, final ProgressBar progressBar, final Button button){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final Usuario usuario = new Usuario();

        DatabaseReference referenceUser = FirebaseConfiguracoes.getFirebaseDatabase().child("User").child(user.getUid());
        referenceUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                switch (dataSnapshot.getKey()) {
                    case "campeonatos":
                        usuario.setCampeonatos(Integer.valueOf(dataSnapshot.getValue().toString()));
                        break;
                    case "email":
                        usuario.setEmail(dataSnapshot.getValue().toString());
                        break;
                    case "icone":
                        usuario.setIcone(Integer.valueOf(dataSnapshot.getValue().toString()));
                        break;
                    case "nickLol":
                        usuario.setNickLol(dataSnapshot.getValue().toString());
                        break;
                    case "nome":
                        usuario.setNome(dataSnapshot.getValue().toString());
                        break;
                    case "senha":
                        usuario.setSenha(dataSnapshot.getValue().toString());
                        break;
                    case "time":
                        usuario.setTime(dataSnapshot.getValue().toString());
                        break;
                    case "vitorias":
                        usuario.setVitorias(Integer.valueOf(dataSnapshot.getValue().toString()));

                        UsuarioLogado.setUser(usuario);
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((MainActivity) activity).telaInicial();
                                progressBar.setVisibility(View.INVISIBLE);
                                button.setEnabled(true);
                            }

                        });
                        Log.i("INFO","Time: "+UsuarioLogado.getTime());
                        Log.i("INFO","Time: "+usuario.getTime());
                        break;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
