package apps.akayto.socialleague.Models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.Helper.Dados;
import apps.akayto.socialleague.R;

/**
 * Created by LUCASGABRIELALVESCOR on 15/03/2018.
 */

public class UsuarioLogado {

    private static boolean nickExists = false;

    private static boolean nickLoLExists = false;

    private static String FirebaseToken;
    private static String email;
    private static String nome;
    private static String nickLol;
    private static String senha;
    private static String time;
    private static int icone;
    private static int campeonatos;
    private static int vitorias;
    private static FirebaseAuth firebaseAuth;

    private static final String ARQUIVO = "PREFERENCIAS_USUARIO";


    public static String getEmail() {
        return email;
    }


    public static String getNome() {
        return nome;
    }


    public static String getNickLol() {
        return nickLol;
    }


    public static String getSenha() {
        return senha;
    }

    public static int getIcone() {
        return icone;
    }


    public static int getCampeonatos() {
        return campeonatos;
    }

    public static int getVitorias() {
        return vitorias;
    }

    public static String getTime() {
        return time;
    }

    public static String getFirebaseToken() {
        return FirebaseToken;
    }

    public static void setFirebaseToken(String firebaseToken){
        FirebaseToken = firebaseToken;
    }

    public static void setUser(Usuario usuario) {
        UsuarioLogado.campeonatos = usuario.getCampeonatos();
        UsuarioLogado.email = usuario.getEmail();
        UsuarioLogado.icone = usuario.getIcone();
        UsuarioLogado.nickLol = usuario.getNickLol();
        UsuarioLogado.nome = usuario.getNome();
        UsuarioLogado.senha = usuario.getSenha();
        UsuarioLogado.vitorias = usuario.getVitorias();
        UsuarioLogado.time = usuario.getTime();
    }

    public static void salvarValores(final Activity activity, final View view, final Context context, final ProgressBar progressBar, final String[][] valores, final Button button) {
        firebaseAuth = FirebaseConfiguracoes.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < 5; i++) {
                        if (valores[1][i] != null)
                            if (valores[1][i].length() > 0) {
                                switch (i) {
                                    case 0:
                                        salvarEmail(activity, valores, view, context, progressBar, button);
                                        break;
                                    case 1:
                                        salvarNome(activity, valores, view, context, progressBar, button);
                                        break;
                                    case 2:
                                        salvarNickLoL(activity, valores, view, context, progressBar, button);
                                        break;
                                    case 3:
                                        salvarSenha(activity, valores, view, context, progressBar, button);
                                        break;
                                    case 4:
                                        salvarIcone(activity, valores, view, context, progressBar, button);
                                        break;
                                }

                                break;
                            }
                    }
                } else {
                    Snackbar.make(view, context.getResources().getText(R.string.stg_ErroConectarConta), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private static void salvarEmail(final Activity activity, final String[][] valores, final View view, final Context context, final ProgressBar progressBar, final Button button) {
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if (Dados.verificaEmail(valores[1][0])) {
            if (!valores[1][0].equals("")) {
                user.updateEmail(valores[1][0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            if (!valores[1][0].equals("")) {
                                DatabaseReference referenceUser = FirebaseConfiguracoes.getFirebaseDatabase().child("User").child(user.getUid());
                                referenceUser.child("email").setValue(valores[1][0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            UsuarioLogado.email = valores[1][0];
                                            Snackbar.make(view, "Email " + context.getResources().getText(R.string.stg_Sucesso_Alterar_Dados), Snackbar.LENGTH_SHORT).show();
                                            SharedPreferences preferences = context.getSharedPreferences(ARQUIVO, 0);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            if (preferences.getBoolean("ContinuarLogado", false))
                                                editor.putString("Email", valores[1][0]);
                                            editor.apply();
                                        } else {
                                            Snackbar.make(view, context.getResources().getText(R.string.stg_Erro_Alterar_Dados) + " Email", Snackbar.LENGTH_SHORT).show();
                                        }

                                        valores[1][0] = "";
                                        forRecuperarValores(activity, valores, view, context, progressBar, button);
                                    }
                                });

                            }
                        } else {
                            if (!valores[1][0].equals("")) {
                                Snackbar.make(view, context.getResources().getText(R.string.stg_Erro_Alterar_Dados) + "  Email", Snackbar.LENGTH_SHORT).show();
                                valores[1][0] = "";
                                forRecuperarValores(activity, valores, view, context, progressBar, button);
                            }

                        }
                    }
                });
            } else {
                if (!valores[1][0].equals("")) {
                    Snackbar.make(view, context.getResources().getText(R.string.stg_Email_Invalido), Snackbar.LENGTH_SHORT).show();
                    valores[1][0] = "";
                    forRecuperarValores(activity, valores, view, context, progressBar, button);
                }
            }
        }
    }

    private static void salvarSenha(final Activity activity, final String[][] valores, final View view, final Context context, final ProgressBar progressBar, final Button button) {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final DatabaseReference referenceUser = FirebaseConfiguracoes.getFirebaseDatabase().child("User").child(user.getUid());

        if (Dados.verificaSenha(valores[1][3])) {
            if (!valores[1][3].equals("")) {
                AuthCredential credential = EmailAuthProvider.getCredential(email, senha);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(valores[1][3]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        referenceUser.child("senha").setValue(valores[1][3]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    UsuarioLogado.senha = valores[1][3];
                                                    Snackbar.make(view, "Password" + context.getResources().getText(R.string.stg_Sucesso_Alterar_Dadas), Snackbar.LENGTH_SHORT).show();
                                                    SharedPreferences preferences = context.getSharedPreferences(ARQUIVO, 0);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    if (preferences.getBoolean("ContinuarLogado", false))
                                                        editor.putString("Senha", valores[1][3]);
                                                    editor.apply();

                                                } else
                                                    Snackbar.make(view, context.getResources().getText(R.string.stg_Erro_Alterar_Dados) + " Password", Snackbar.LENGTH_SHORT).show();


                                                valores[1][3] = "";
                                                forRecuperarValores(activity, valores, view, context, progressBar, button);

                                            }
                                        });
                                    } else if (!valores[1][3].equals("")) {
                                        Snackbar.make(view, context.getResources().getText(R.string.stg_Erro_Alterar_Dados) + " Password", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            if (!valores[1][3].equals("")) {
                                Snackbar.make(view, context.getResources().getText(R.string.stg_Erro_Alterar_Dados) + " Password", Snackbar.LENGTH_SHORT).show();
                                valores[1][3] = "";
                                forRecuperarValores(activity, valores, view, context, progressBar, button);
                            }
                        }
                    }
                });
            }
        } else {
            if (!valores[1][3].equals("")) {
                Snackbar.make(view, context.getResources().getText(R.string.stg_Senha_Invalida), Snackbar.LENGTH_SHORT).show();
                valores[1][3] = "";
                forRecuperarValores(activity, valores, view, context, progressBar, button);
            }
        }
    }

    private static void salvarNome(final Activity activity, final String[][] valores, final View view, final Context context, final ProgressBar progressBar, final Button button) {
        if (valores[1][1].length() > 3) {
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            final DatabaseReference referenceUser = FirebaseConfiguracoes.getFirebaseDatabase().child("User").child(user.getUid());
            final DatabaseReference referenceNick = FirebaseConfiguracoes.getFirebaseDatabase().child("nick");
            referenceNick.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue().toString().toLowerCase().equals(valores[1][1].toLowerCase()) && !nickExists) {
                        if(!valores[1][1].equals("")) {
                            nickExists = true;
                            Snackbar.make(view, context.getResources().getString(R.string.stg_NickExistente), Snackbar.LENGTH_SHORT).show();
                            valores[1][1] = "";
                            forRecuperarValores(activity, valores, view, context, progressBar, button);
                        }
                    } else if (dataSnapshot.getValue().toString().equals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")) {
                        if (!nickExists) {
                            if(!valores[1][1].equals("")) {
                                referenceNick.child(user.getUid()).setValue(valores[1][1]);
                                referenceUser.child("nome").setValue(valores[1][1]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Snackbar.make(view, context.getResources().getText(R.string.stg_Nome_da_Conta) + " " + context.getResources().getText(R.string.stg_Sucesso_Alterar_Dados), Snackbar.LENGTH_SHORT).show();
                                            UsuarioLogado.nome = valores[1][1];
                                        } else
                                            Snackbar.make(view, context.getResources().getText(R.string.stg_Erro_Alterar_Dados) + " " + context.getResources().getText(R.string.stg_Nome_da_Conta), Snackbar.LENGTH_SHORT).show();

                                        valores[1][1] = "";
                                        forRecuperarValores(activity, valores, view, context, progressBar, button);
                                    }
                                });
                            }
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
                    if(!valores[1][1].equals("")) {
                        Snackbar.make(view, context.getResources().getText(R.string.stg_Erro_Alterar_Dados) + " " + context.getResources().getText(R.string.stg_Nome_da_Conta), Snackbar.LENGTH_SHORT).show();

                        valores[1][1] = "";
                        forRecuperarValores(activity, valores, view, context, progressBar, button);
                    }
                }
            });
        } else {
            if(!valores[1][1].equals("")) {
                Snackbar.make(view, context.getResources().getText(R.string.stg_NomeErro), Snackbar.LENGTH_SHORT).show();

                valores[1][1] = "";
                forRecuperarValores(activity, valores, view, context, progressBar, button);
            }
        }
    }

    private static void salvarNickLoL(final Activity activity, final String[][] valores, final View view, final Context context, final ProgressBar progressBar, final Button button) {
        if (valores[1][2].length() > 3) {

            final FirebaseUser user = firebaseAuth.getCurrentUser();
            final DatabaseReference referenceUser = FirebaseConfiguracoes.getFirebaseDatabase().child("User").child(user.getUid());
            final DatabaseReference referenceNickLoL = FirebaseConfiguracoes.getFirebaseDatabase().child("nickLol");
            referenceNickLoL.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue().toString().toLowerCase().equals(valores[1][2].toLowerCase()) && !nickLoLExists) {
                        if(!valores[1][2].equals("")) {
                            nickLoLExists = true;
                            Snackbar.make(view, context.getResources().getString(R.string.stg_NickLoLExistente), Snackbar.LENGTH_SHORT).show();
                            valores[1][2] = "";
                        }
                        forRecuperarValores(activity, valores, view, context, progressBar, button);
                    } else if (dataSnapshot.getValue().toString().equals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")) {
                        if (!nickLoLExists) {
                            if(!valores[1][2].equals("")) {
                                referenceNickLoL.child(user.getUid()).setValue(valores[1][2]);
                                referenceUser.child("nickLol").setValue(valores[1][2]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Snackbar.make(view, context.getResources().getText(R.string.stg_Nick_do_LoL) + "" + context.getResources().getText(R.string.stg_Sucesso_Alterar_Dados), Snackbar.LENGTH_SHORT).show();
                                            UsuarioLogado.nickLol = valores[1][2];
                                        } else
                                            Snackbar.make(view, context.getResources().getText(R.string.stg_Erro_Alterar_Dados) + " " + context.getResources().getText(R.string.stg_Nick_do_LoL), Snackbar.LENGTH_SHORT).show();

                                        valores[1][2] = "";
                                        forRecuperarValores(activity, valores, view, context, progressBar, button);
                                    }
                                });
                            }
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
                    if(!valores[1][2].equals("")) {
                        Snackbar.make(view, context.getResources().getText(R.string.stg_Erro_Alterar_Dados) + " " + context.getResources().getText(R.string.stg_Nick_do_LoL), Snackbar.LENGTH_SHORT).show();

                        valores[1][2] = "";
                        forRecuperarValores(activity, valores, view, context, progressBar, button);
                    }
                }
            });
        } else {
            if(!valores[1][2].equals("")) {
                Snackbar.make(view, context.getResources().getText(R.string.stg_NickErro), Snackbar.LENGTH_SHORT).show();

                valores[1][2] = "";
                forRecuperarValores(activity, valores, view, context, progressBar, button);
            }
        }

    }

    private static void salvarIcone(final Activity activity, final String[][] valores, final View view, final Context context, final ProgressBar progressBar, final Button button) {

        if (Integer.valueOf(valores[1][4]) != 0) {
            if(!valores[1][4].equals("")) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                final DatabaseReference referenceUser = FirebaseConfiguracoes.getFirebaseDatabase().child("User").child(user.getUid());
                referenceUser.child("icone").setValue(Integer.valueOf(valores[1][4])).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(view, context.getResources().getText(R.string.stg_Icone) + " " + context.getResources().getText(R.string.stg_Sucesso_Alterar_Dados), Snackbar.LENGTH_SHORT).show();
                            UsuarioLogado.icone = Integer.valueOf(valores[1][4]);
                        } else
                            Snackbar.make(view, context.getResources().getText(R.string.stg_Erro_Alterar_Dados) + " " + context.getResources().getText(R.string.stg_Icone), Snackbar.LENGTH_SHORT).show();

                        valores[1][4] = "";
                        forRecuperarValores(activity, valores, view, context, progressBar, button);
                    }
                });
            }
        } else {
            if (!valores[1][4].equals("")) {
                Snackbar.make(view, context.getResources().getText(R.string.stg_IconenaoSelecionado), Snackbar.LENGTH_SHORT).show();
                valores[1][4] = "";
                forRecuperarValores(activity, valores, view, context, progressBar, button);
            }
        }
    }

    private static void forRecuperarValores(final Activity activity, final String[][] valores, final View view, final Context context, final ProgressBar progressBar, final Button button) {
        Log.i("INFO", "forRecuperarValores");


        String campo = "0";

        if (forTemValor(valores)) {
            for (int i = 0; i < 5; i++) {
                if (valores[1][i] != null)
                    if (valores[1][i].length() > 1) {
                        campo = valores[0][i];
                        break;
                    }
            }

            switch (campo) {
                case "email":
                    Log.i("INFO", "email");
                    salvarEmail(activity, valores, view, context, progressBar, button);
                    break;
                case "nome":
                    Log.i("INFO", "nome");
                    salvarNome(activity, valores, view, context, progressBar, button);
                    break;
                case "nickLol":
                    Log.i("INFO", "nickLol");
                    salvarNickLoL(activity, valores, view, context, progressBar, button);
                    break;
                case "senha":
                    Log.i("INFO", "senha");
                    salvarSenha(activity, valores, view, context, progressBar, button);
                    break;
                case "icone":
                    Log.i("INFO", "icone");
                    salvarIcone(activity, valores, view, context, progressBar, button);
                    break;

                default:
                    progressBar.setVisibility(View.INVISIBLE);
                    button.setEnabled(true);
                    break;
            }
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            button.setEnabled(true);
        }
    }

    private static boolean forTemValor(String[][] valores) {
        boolean temValor = false;

        for (int i = 0; i < 5; i++) {
            if (valores[1][i] != null)
                if (valores[1][i].length() > 0) {
                    temValor = true;
                    break;
                }
        }

        return temValor;
    }
}
