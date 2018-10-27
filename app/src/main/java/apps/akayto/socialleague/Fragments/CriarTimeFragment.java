package apps.akayto.socialleague.Fragments;


import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.Control.UserControl;
import apps.akayto.socialleague.MainActivity;
import apps.akayto.socialleague.Models.Time;
import apps.akayto.socialleague.Models.Usuario;
import apps.akayto.socialleague.Models.UsuarioLogado;
import apps.akayto.socialleague.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CriarTimeFragment extends Fragment {


    private boolean existe;

    private FrameLayout frameLayout;
    private ProgressBar progressBar;


    private Button bnt_Enviar;

    private EditText edt_Nome;
    private EditText edt_Membro;
    private EditText edt_Membro1;
    private EditText edt_Membro2;
    private EditText edt_Membro3;
    private EditText edt_Membro4;
    private EditText edt_Membro5;

    private CriarTime criarTime = new CriarTime();

    public CriarTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_criar_time, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.criarTime_ProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        frameLayout = view.findViewById(R.id.containerCriarTime);

        edt_Nome = view.findViewById(R.id.criarTime_EdtNomeTime);
        edt_Membro = view.findViewById(R.id.criarTime_EdtMembro);
        edt_Membro1 = view.findViewById(R.id.criarTime_EdtMembro1);
        edt_Membro2 = view.findViewById(R.id.criarTime_EdtMembro2);
        edt_Membro3 = view.findViewById(R.id.criarTime_EdtMembro3);
        edt_Membro4 = view.findViewById(R.id.criarTime_EdtMembro4);
        edt_Membro5 = view.findViewById(R.id.criarTime_EdtMembro5);

        bnt_Enviar = view.findViewById(R.id.criarTime_BntEnviar);
        bnt_Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    criarTime = new CriarTime();
                    criarTime.execute();
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        criarTime = new CriarTime();
        ((MainActivity) getActivity()).setTitleToolbar(getString(R.string.txt_Criar_Time));


    }

    private class CriarTime extends AsyncTask<Void,Void,Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            bnt_Enviar.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Long doInBackground(Void... voids) {

            if (edt_Nome.getText().toString().length() >= 4) {
                final String nome_time = edt_Nome.getText().toString();
                final ArrayList<String> membros = new ArrayList<>();
                if (edt_Membro.getText().toString().length() > 0)
                    membros.add(edt_Membro.getText().toString());

                if (edt_Membro1.getText().toString().length() > 0)
                    membros.add(edt_Membro1.getText().toString());

                if (edt_Membro2.getText().toString().length() > 0)
                    membros.add(edt_Membro2.getText().toString());

                if (edt_Membro3.getText().toString().length() > 0)
                    membros.add(edt_Membro3.getText().toString());

                if (edt_Membro4.getText().toString().length() > 0)
                    membros.add(edt_Membro4.getText().toString());

                if (edt_Membro5.getText().toString().length() > 0)
                    membros.add(edt_Membro5.getText().toString());

                existe = false;

                final DatabaseReference referenceNomeTime = FirebaseConfiguracoes.getFirebaseDatabase().child("nomeTimes");
                referenceNomeTime.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue().toString().toLowerCase().equals(nome_time.toLowerCase()) && !existe) {
                            existe = true;
                            bnt_Enviar.setEnabled(true);
                            Snackbar.make(frameLayout, getActivity().getResources().getString(R.string.stg_NomeTimeExiste), Snackbar.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        } else if (dataSnapshot.getKey().equals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz") && !existe) {

                            existe = true;
                            Log.i("INFO", "Existe: " + existe);

                            DatabaseReference referenceUsers = FirebaseConfiguracoes.getFirebaseDatabase().child("nick");
                            referenceUsers.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                    if (!dataSnapshot.getValue().toString().equals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")) {
                                        if (membros.size() != 0) {
                                            for (int i = 0; i < membros.size(); i++) {
                                                if (dataSnapshot.getValue().toString().toLowerCase().equals(membros.get(i).toLowerCase())) {
                                                    //Enviar Socilicitação
                                                    membros.remove(i);
                                                }
                                            }
                                        }
                                    } else {

                                        if (membros.size() != 0) {
                                            for (int i = 0; i < membros.size(); i++) {

                                                final int finalI = i;


                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Snackbar.make(frameLayout, Html.fromHtml("" + getActivity().getResources().getString(R.string.stg_MembroNaoEncontrado) + ": <font color=#ff0000>" + membros.get(finalI) + "</font"), Snackbar.LENGTH_SHORT).show();
                                                    }
                                                });


                                                if (i == (membros.size() - 1)) {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    bnt_Enviar.setEnabled(true);

                                                    if (criarTime != null)
                                                        if (!criarTime.isCancelled())
                                                            criarTime.cancel(true);
                                                }

                                            }

                                        } else {
                                            criarTime(nome_time, referenceNomeTime, bnt_Enviar);

                                            if (criarTime != null)
                                                if (!criarTime.isCancelled())
                                                    criarTime.cancel(true);
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
                                    progressBar.setVisibility(View.INVISIBLE);
                                    bnt_Enviar.setEnabled(true);
                                    Snackbar.make(frameLayout, "Erro: " + databaseError, Snackbar.LENGTH_SHORT).show();

                                    if (criarTime != null)
                                        if (!criarTime.isCancelled())
                                            criarTime.cancel(true);
                                }
                            });
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

                        progressBar.setVisibility(View.INVISIBLE);
                        bnt_Enviar.setEnabled(true);
                        Snackbar.make(frameLayout, "Erro: " + databaseError, Snackbar.LENGTH_SHORT).show();

                        if (criarTime != null)
                            if (!criarTime.isCancelled())
                                criarTime.cancel(true);
                    }
                });
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                bnt_Enviar.setEnabled(true);
                Snackbar.make(frameLayout, getActivity().getResources().getString(R.string.stg_NomeTimeInvalido), Snackbar.LENGTH_SHORT).show();

                if (criarTime != null)
                    if (!criarTime.isCancelled())
                        criarTime.cancel(true);
            }

            return null;
        }
    }

    public void criarTime(final String nome_time, final DatabaseReference referenceNomeTime, final Button bnt_Enviar) {
        Time time = new Time();
        time.setNome(nome_time);
        time.setCampeonatos(0);
        time.setVitorias(0);
        time.setQuantMembros(1);
        time.setDivisao("3ª Divisão");
        time.setLider(UsuarioLogado.getNome());

        final DatabaseReference referenceTime = FirebaseConfiguracoes.getFirebaseDatabase().child("Time");
        referenceTime.child(nome_time).setValue(time).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                   UserControl.carregarUsuario(getActivity(), progressBar, bnt_Enviar);
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    bnt_Enviar.setEnabled(true);
                    Snackbar.make(frameLayout, "Erro: " + task.getException().getClass().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                }
                if (criarTime != null)
                    if (!criarTime.isCancelled())
                        criarTime.cancel(true);
            }
        });
    }

    @Override
    public void onPause() {
        if (criarTime != null)
            if (!criarTime.isCancelled()) {
                criarTime.cancel(true);
                super.onPause();
            } else {
                super.onPause();
            }
        else {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (criarTime != null)
            if (!criarTime.isCancelled()) {
                criarTime.cancel(true);
                super.onStop();
            } else {
                super.onStop();
            }
        else {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (criarTime != null)
            if (!criarTime.isCancelled()) {
                criarTime.cancel(true);
                super.onDestroy();
            } else {
                super.onDestroy();
            }
        else {
            super.onDestroy();
        }
    }
}
