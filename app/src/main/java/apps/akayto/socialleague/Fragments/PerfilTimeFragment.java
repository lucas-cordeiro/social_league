package apps.akayto.socialleague.Fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import apps.akayto.socialleague.Adapters.AdapterMembros;
import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.Helper.RecyclerItemClickListener;
import apps.akayto.socialleague.MainActivity;
import apps.akayto.socialleague.Models.Membros;
import apps.akayto.socialleague.Models.Notificacao;
import apps.akayto.socialleague.Models.NotificacaoSolicitarEntrada;
import apps.akayto.socialleague.Models.Time;
import apps.akayto.socialleague.Models.UsuarioLogado;
import apps.akayto.socialleague.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilTimeFragment extends Fragment {

    private String stg_NomeTime = "nd";
    private Time time;

    private FloatingActionButton fab;

    private Dialog dialog;
    private EditText nome_dialog;
    private ProgressBar progressBar_dialog;
    private Button bnt_Enviar;
    private SolicitarMembro solicitarMembro = new SolicitarMembro();
    private SolicitarEntrada solicitarEntrada = new SolicitarEntrada();

    private int tipo;//0-Lider | 1-Membro | 2-Não membro

    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private AdapterMembros adapter;

    private View parentLayout;

    private long count;

    private ArrayList<Membros> membrosArrayList = new ArrayList<>();

    private TextView txtNaoTemMembros;

    private boolean nickExists;

    private TextView nome;
    private TextView lider;
    private TextView campeonatos;
    private TextView vitorias;
    private TextView membros;

    private View view;

    private BuscarBancodeDados bancodeDados = new BuscarBancodeDados();
    private RemoverMembro removerMembro = new RemoverMembro();


    private static final String TAG = "INFO_PERFIL_TIME";
    public PerfilTimeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_perfil_time, container, false);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ((MainActivity) getActivity()).setTitleToolbar(getString(R.string.stg_Time));

        fab = view.findViewById(R.id.perfilTime_FloatingActionButton);
        fab.setVisibility(View.VISIBLE);
        fab.setEnabled(false);

        dialog = new Dialog(getContext(), R.style.MyDialogTheme);


        switch (tipo) {
            case 0:
                fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_add_membro));

                dialog = new Dialog(getContext(), R.style.MyDialogTheme);

                dialog.setContentView(R.layout.adicionar_membro_dialog);

                progressBar_dialog = dialog.findViewById(R.id.dialogSolicitar_ProgressBar);

                nome_dialog = dialog.findViewById(R.id.dialogAcidionar_NomeMembro);

                bnt_Enviar = dialog.findViewById(R.id.dialogSolicitar_BntEnviar);
                bnt_Enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        solicitarMembro.execute();
                    }
                });

                break;

            case 1:
                fab.setVisibility(View.INVISIBLE);
                break;

            case 2:
                fab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_solicitar_entrar));
                dialog = new Dialog(getContext(), R.style.MyDialogTheme);

                dialog.setContentView(R.layout.solicitar_entrar_dialog);

                progressBar_dialog = dialog.findViewById(R.id.dialogSolicitar_ProgressBar);

                bnt_Enviar = dialog.findViewById(R.id.dialogSolicitar_BntEnviar);
                bnt_Enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (solicitarEntrada.getStatus().equals(AsyncTask.Status.PENDING) || solicitarEntrada.getStatus().equals(AsyncTask.Status.FINISHED))
                            solicitarEntrada = new SolicitarEntrada();
                        solicitarEntrada.execute();
                    }
                });
                break;
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
            }
        });

        nome = view.findViewById(R.id.perfilTime_TxtNome);
        lider = view.findViewById(R.id.perfilTime_TxtLider);
        campeonatos = view.findViewById(R.id.perfilTime_TxtCampeonatos);
        vitorias = view.findViewById(R.id.perfilTime_TxtVitorias);
        membros = view.findViewById(R.id.perfilTime_TxtMembros);

        txtNaoTemMembros = view.findViewById(R.id.perfilTime_TxtSemMembros);

        recyclerView = view.findViewById(R.id.perfilTime_RecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        parentLayout = view.findViewById(R.id.framePerfilTime);

        progressBar = view.findViewById(R.id.perfilTime_ProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        progressBar.setVisibility(View.VISIBLE);

        if (bancodeDados.getStatus().equals(AsyncTask.Status.PENDING) || bancodeDados.getStatus().equals(AsyncTask.Status.FINISHED))
            bancodeDados = new BuscarBancodeDados();

        bancodeDados.execute();
    }

    private class BuscarBancodeDados extends AsyncTask<Void,Void,Long>{

        @Override
        protected Long doInBackground(Void... voids) {

            Log.i(TAG,"BuscarBancodeDados");
            Log.i(TAG,"Time "+stg_NomeTime);
            final DatabaseReference referenceTime = FirebaseConfiguracoes.getFirebaseDatabase().child("Time/");
            referenceTime.orderByKey().equalTo(stg_NomeTime).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i(TAG,"onChildAdded: "+dataSnapshot.getValue(Time.class).getNome());
                    setTime(dataSnapshot.getValue(Time.class));
                    referenceTime.removeEventListener(this);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                   /* Log.i(TAG,"onChildChanged: "+dataSnapshot.getValue(Time.class).getNome());
                    setTime(dataSnapshot.getValue(Time.class));*/
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Error: "+databaseError.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
                }
            });

            return null;
        }
    }

    private void setTime(Time time){
        this.time = time;
        nome.setText(time.getNome());
        lider.setText(getString(R.string.stg_Lider)+" "+time.getLider());
        campeonatos.setText(getString(R.string.stg_Campeonatos)+" "+time.getCampeonatos());
        vitorias.setText(getString(R.string.stg_Vitorias)+" "+time.getVitorias());
        membrosTime();
    }

    private void membrosTime() {
        membrosArrayList = new ArrayList<>();
        adapter = new AdapterMembros(membrosArrayList);
        recyclerView.setAdapter(adapter);

        count = 0;

        DatabaseReference referenceMembros = FirebaseConfiguracoes.getFirebaseDatabase().child("Time").child(time.getNome()).child("membros");
        referenceMembros.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "Key: " + dataSnapshot.getKey());

                if (!dataSnapshot.getKey().toString().equals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")) {
                    count += 1;

                    Log.i(TAG, "Key: " + dataSnapshot.getKey());

                    Membros membro = dataSnapshot.getValue(Membros.class);

                    Log.i(TAG, "I: " + membro.getIcone());
                    membrosArrayList.add(membro);
                    adapter.notifyItemInserted(adapter.getItemCount()-1);
                } else {
                    if (count == 0) {
                        count = 1;
                        txtNaoTemMembros.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        fab.setEnabled(true);

                    } else {
                        count++;
                    }
                    setAdapterRecyclerView();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                /*Membros membro = dataSnapshot.getValue(Membros.class);
                membrosArrayList.*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(parentLayout, "Erro: " + databaseError.getClass().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                fab.setEnabled(true);
            }
        });
    }

    private void setAdapterRecyclerView() {
        membros.setText(getActivity().getResources().getString(R.string.stg_Membro) + " " + count + "/7");
        Log.i(TAG, "Size: " + membrosArrayList.size());
        progressBar.setVisibility(View.INVISIBLE);
        fab.setEnabled(true);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {
                        if (tipo == 0) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                            dialog.setTitle("Excluir Membro");
                            dialog.setMessage("Deseja remover o membro do Time?");
                            dialog.setCancelable(true);
                            dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!removerMembro.isCancelled())
                                        removerMembro.cancel(true);
                                    removerMembro = new RemoverMembro();
                                    removerMembro.execute(position);
                                }
                            });
                            dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            dialog.create();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                })
        );

    }


    private class SolicitarMembro extends AsyncTask<Void,Void,Long>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar_dialog.setVisibility(View.VISIBLE);
            bnt_Enviar.setEnabled(false);
        }

        @Override
        protected Long doInBackground(Void... voids) {

            final String stg_Nome = nome_dialog.getText().toString();
            if (stg_Nome.length() > 3) {
                if (!stg_Nome.toLowerCase().equals(UsuarioLogado.getNome().toLowerCase())) {

                    boolean membro_do_time = false;

                    for (int i = 0; i < membrosArrayList.size(); i++) {
                        if (membrosArrayList.get(i).getNome().toLowerCase().equals(stg_Nome.toLowerCase())) {
                            membro_do_time = true;
                            break;
                        }
                    }

                    if (!membro_do_time) {

                        nickExists = false;

                        DatabaseReference referenceNomeUser = FirebaseConfiguracoes.getFirebaseDatabase().child("nick");
                        referenceNomeUser.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.getValue().toString().toLowerCase().equals(stg_Nome.toLowerCase()) && !nickExists) {
                                    nickExists = true;

                                    Notificacao notificacao = new Notificacao(
                                            "Solicitação de Entrada",
                                            "Você está convidado para entrar no Time: " + time.getNome(),
                                            "1",
                                            true);

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                                    Date data = new Date();
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(data);
                                    Date data_atual = cal.getTime();

                                    String data_completa = dateFormat.format(data_atual);

                                    DatabaseReference referenceMembro = FirebaseConfiguracoes.getFirebaseDatabase().child("User").child(dataSnapshot.getKey()).child("notificacoes").child(data_completa);
                                    referenceMembro.setValue(notificacao).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            String msg;
                                            if (task.isSuccessful()) {

                                                msg = getActivity().getResources().getString(R.string.stg_SolicitacaoSucesso);

                                            } else {

                                                msg = "Erro: " + task.getException().getClass().getSimpleName();

                                            }
                                            Snackbar.make(parentLayout, msg, Snackbar.LENGTH_SHORT).show();
                                            progressBar_dialog.setVisibility(View.INVISIBLE);
                                            bnt_Enviar.setEnabled(true);
                                            dialog.dismiss();
                                        }
                                    });

                                } else if (dataSnapshot.getValue().toString().equals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz") && !nickExists) {
                                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.stg_MembroNaoEncontrado), Toast.LENGTH_SHORT).show();
                                    progressBar_dialog.setVisibility(View.INVISIBLE);
                                    bnt_Enviar.setEnabled(true);
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
                                Toast.makeText(getContext(), "Erro: " + databaseError.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                                progressBar_dialog.setVisibility(View.INVISIBLE);
                                bnt_Enviar.setEnabled(true);
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), getActivity().getResources().getString(R.string.stg_MembroJaNoTime), Toast.LENGTH_SHORT).show();
                        progressBar_dialog.setVisibility(View.INVISIBLE);
                        bnt_Enviar.setEnabled(true);
                    }

                } else {
                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.stg_MembroJaNoTime), Toast.LENGTH_SHORT).show();
                    progressBar_dialog.setVisibility(View.INVISIBLE);
                    bnt_Enviar.setEnabled(true);
                }
            } else {
                Toast.makeText(getContext(), getActivity().getResources().getString(R.string.stg_NomeErro), Toast.LENGTH_SHORT).show();
                progressBar_dialog.setVisibility(View.INVISIBLE);
                bnt_Enviar.setEnabled(true);
            }

            return 0L;
        }
    }

    private class SolicitarEntrada extends AsyncTask<Void, Void, Long>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar_dialog.setVisibility(View.VISIBLE);
            bnt_Enviar.setEnabled(false);
        }

        @Override
        protected Long doInBackground(Void... voids) {
            DatabaseReference referenceId = FirebaseConfiguracoes.getFirebaseDatabase().child("nick");
            referenceId.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue().toString().toLowerCase().equals(time.getLider().toLowerCase())) {
                        DatabaseReference referenceLider = FirebaseConfiguracoes.getFirebaseDatabase().child("User").child(dataSnapshot.getKey());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                        Date data = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(data);
                        Date data_atual = cal.getTime();

                        String data_completa = dateFormat.format(data_atual);

                        FirebaseAuth firebaseAuth= FirebaseConfiguracoes.getFirebaseAuth();
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        NotificacaoSolicitarEntrada notificacao = new NotificacaoSolicitarEntrada(
                                "Pedido de Entrada",
                                "O membro " + UsuarioLogado.getNome() + " deseja se juntar ao seu Time",
                                "2",
                                true,
                                user.getUid());

                        referenceLider.child("notificacoes").child(data_completa).setValue(notificacao).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg;

                                if (task.isSuccessful()) {
                                    msg = getActivity().getResources().getString(R.string.stg_SolicitacaoSucesso);
                                } else {
                                    msg = "Error: " + task.getException().getClass().getSimpleName();
                                }

                                if(progressBar_dialog!=null){
                                    progressBar_dialog.setVisibility(View.INVISIBLE);
                                    bnt_Enviar.setEnabled(true);
                                }
                                dialog.dismiss();
                                Snackbar.make(parentLayout, msg, Snackbar.LENGTH_SHORT).show();
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
                    if(progressBar_dialog!=null){
                        progressBar_dialog.setVisibility(View.INVISIBLE);
                        bnt_Enviar.setEnabled(true);
                    }
                    Toast.makeText(getActivity(), "Error: " + databaseError.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                }
            });


            return null;
        }
    }


    private class RemoverMembro extends AsyncTask<Integer,Void,Long>{

        @Override
        protected Long doInBackground(final Integer... integers) {

            Log.i(TAG,"RemoveMembro");

            final int position = integers[0];
            Log.i(TAG,"position:"+position);
            final Membros membro = membrosArrayList.get(position);

            DatabaseReference referenceMembro = FirebaseConfiguracoes.getFirebaseDatabase().child("Time").child(time.getNome()).child("membros");
            referenceMembro.child(membro.getId()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Snackbar.make(parentLayout, getActivity().getResources().getString(R.string.stg_MembroRemovido), Snackbar.LENGTH_SHORT).show();
                        membrosArrayList.remove(position);
                        adapter.notifyItemRemoved(position);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count--;
                                membros.setText(getActivity().getResources().getString(R.string.stg_Membro) + " " + count + "/7");
                            }
                        });
                    } else {
                        Snackbar.make(parentLayout, "Erro: " + task.getException().getClass().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

            return null;
        }
    }

    public void setStg_NomeTime(String stg_NomeTime) {
        this.stg_NomeTime = stg_NomeTime;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public void onStop() {
        if (bancodeDados != null)
            if (!bancodeDados.isCancelled())
                bancodeDados.cancel(true);

        if(!solicitarEntrada.isCancelled())
            solicitarEntrada.cancel(true);

        if(!solicitarMembro.isCancelled())
            solicitarMembro.cancel(true);

        if(!removerMembro.isCancelled())
            removerMembro.cancel(true);
        super.onStop();
    }

}