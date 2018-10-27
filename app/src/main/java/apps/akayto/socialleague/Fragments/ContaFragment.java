package apps.akayto.socialleague.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import apps.akayto.socialleague.Adapters.AdapterIcones;
import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.Helper.RecyclerItemClickListener;
import apps.akayto.socialleague.Models.Usuario;
import apps.akayto.socialleague.Models.UsuarioLogado;
import apps.akayto.socialleague.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContaFragment extends Fragment {

    private RecyclerView recyclerView;

    private ArrayList<Integer> arrayList = new ArrayList<>();

    private int icone;

    private boolean[] mudou = new boolean[5];

    private EditText edt_Email;
    private EditText edt_Nome;
    private EditText edt_NickLoL;
    private EditText edt_Senha;

    private View parentLayout;

    private ProgressBar progressBar;

    private Button bnt_Salvar;

    private String[][] valores = new String[5][5];

    public ContaFragment() {
        // Required empty public constructor
        valores[0][0]="email";
        valores[0][1]="nome";
        valores[0][2]="nickLol";
        valores[0][3]="senha";
        valores[0][4]="icone";

        icone = 0;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conta, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Esconder teclado
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mudouFalse();

        setIcones();

        parentLayout = view.findViewById(R.id.frameConta);

        edt_Email = view.findViewById(R.id.conta_EdtEmail);
        edt_Email.setText(UsuarioLogado.getEmail());

        edt_Nome = view.findViewById(R.id.conta_EdtNome);
        edt_Nome.setText(UsuarioLogado.getNome());

        edt_NickLoL = view.findViewById(R.id.conta_EdtNickLol);
        edt_NickLoL.setText(UsuarioLogado.getNickLol());

        edt_Senha = view.findViewById(R.id.conta_EdtSenha);
        edt_Senha.setText(UsuarioLogado.getSenha());

        icone = UsuarioLogado.getIcone();

        bnt_Salvar = view.findViewById(R.id.conta_BntSalvar);

        progressBar = view.findViewById(R.id.conta_ProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        recyclerView = view.findViewById(R.id.conta_RecyclerView);

        AdapterIcones adapter = new AdapterIcones(arrayList);
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(100);
                        if(getActivity()!=null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!edt_Email.getText().toString().equals(UsuarioLogado.getEmail())) {
                                    if(getActivity()!=null)
                                    edt_Email.setTextColor(getActivity().getResources().getColor(R.color.Theme1));
                                    mudou[0] = true;
                                    valores[1][0]=edt_Email.getText().toString();
                                }else{
                                    if(getActivity()!=null)
                                    edt_Email.setTextColor(getActivity().getResources().getColor(R.color.darkGray));
                                    mudou[0] = false;
                                    valores[1][0]=null;
                                }
                                if(!edt_Nome.getText().toString().equals(UsuarioLogado.getNome())) {
                                    if(getActivity()!=null)
                                    edt_Nome.setTextColor(getActivity().getResources().getColor(R.color.Theme2));
                                    mudou[1] = true;
                                    valores[1][1]=edt_Nome.getText().toString();
                                }else{
                                    if(getActivity()!=null)
                                    edt_Nome.setTextColor(getActivity().getResources().getColor(R.color.darkGray));
                                    mudou[1] = false;
                                    valores[1][1]=null;
                                }
                                if(!edt_NickLoL.getText().toString().equals(UsuarioLogado.getNickLol())) {
                                    if(getActivity()!=null)
                                    edt_NickLoL.setTextColor(getActivity().getResources().getColor(R.color.Theme3));
                                    mudou[2] = true;
                                    valores[1][2]=edt_NickLoL.getText().toString();
                                }else{
                                    if(getActivity()!=null)
                                    edt_NickLoL.setTextColor(getActivity().getResources().getColor(R.color.darkGray));
                                    mudou[2] = false;
                                    valores[1][2]=null;
                                }
                                if(!edt_Senha.getText().toString().equals(UsuarioLogado.getSenha())) {
                                    if(getActivity()!=null)
                                    edt_Senha.setTextColor(getActivity().getResources().getColor(R.color.colorNavLetras));
                                    mudou[3] = true;
                                    valores[1][3]=edt_Senha.getText().toString();
                                }else{
                                    if(getActivity()!=null)
                                    edt_Senha.setTextColor(getActivity().getResources().getColor(R.color.darkGray));
                                    mudou[3] = false;
                                    valores[1][3]=null;
                                }
                                if(icone != UsuarioLogado.getIcone()) {

                                    mudou[4] = true;
                                    valores[1][4]=String.valueOf(icone);
                                }else{
                                    mudou[4] = false;
                                    valores[1][4]=null;
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Snackbar.make(parentLayout, getText(R.string.stg_IconeSelecionado), Snackbar.LENGTH_SHORT).show();
                        icone = position;
                        Log.i("INFO", "icone: " + icone);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                })
        );


        bnt_Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bnt_Salvar.setEnabled(false);

                boolean conectar = false;

                for (int i = 0; i < 5; i++) {
                    if (mudou[i]) {
                        conectar = true;
                        break;
                    }
                }

                if (conectar) {
                    progressBar.setVisibility(View.VISIBLE);

                    final FirebaseAuth firebaseAuth = FirebaseConfiguracoes.getFirebaseAuth();
                    firebaseAuth.signInWithEmailAndPassword(UsuarioLogado.getEmail(), UsuarioLogado.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final FirebaseUser user = firebaseAuth.getCurrentUser();

                                final Usuario usuario = new Usuario();

                                final DatabaseReference referenceUser = FirebaseConfiguracoes.getFirebaseDatabase().child("User").child(user.getUid());
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

                                                mudarValoresBancodeDados();
                                                UsuarioLogado.setUser(usuario);
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
                                        String msg = "";
                                        msg = "Erro: " + databaseError;

                                        progressBar.setVisibility(View.INVISIBLE);
                                        Snackbar.make(parentLayout, msg, Snackbar.LENGTH_SHORT).show();
                                        bnt_Salvar.setEnabled(true);
                                    }
                                });

                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Snackbar.make(parentLayout, getText(R.string.stg_ErroConectarConta), Snackbar.LENGTH_SHORT).show();
                                bnt_Salvar.setEnabled(true);
                            }
                        }
                    });
                }else{
                    bnt_Salvar.setEnabled(true);
                    Snackbar.make(parentLayout, getText(R.string.stg_NenhumCampoAlterado), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mudarValoresBancodeDados() {
        UsuarioLogado.salvarValores(getActivity(), parentLayout, getContext(), progressBar, valores, bnt_Salvar);
    }

    private void mudouFalse() {
        mudou[0] = false;
        mudou[1] = false;
        mudou[2] = false;
        mudou[3] = false;
        mudou[4] = false;
    }


    public void setIcones(){


        for(int i=0;i<146;i++){
            arrayList.add(2131165504+i);
            Log.i("INFO","ID: "+arrayList.get(i));
        }

        Log.i("INFO","ID 181: "+arrayList.get(145));
        Log.i("INFO","ID IMG: "+R.drawable.icone_146);
    }
}
