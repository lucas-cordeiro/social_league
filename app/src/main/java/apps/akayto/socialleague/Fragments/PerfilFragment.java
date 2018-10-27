package apps.akayto.socialleague.Fragments;


import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.MainActivity;
import apps.akayto.socialleague.Models.Time;
import apps.akayto.socialleague.Models.Usuario;
import apps.akayto.socialleague.Models.UsuarioLogado;
import apps.akayto.socialleague.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {


    private int icone=0;
    private String stg_Nome;
    private String stg_Nick;
    private String stg_Campeonatos;
    private String stg_Vitorias;
    private String stg_Time;

    private ImageView imgTime;
    private TextView nomeTime;
    private TextView liderTime;
    private TextView campeonatosTime;
    private TextView vitoriasTime;
    private TextView quantTime;

    private ImageView img;
    private TextView nome;
    private TextView nick;
    private TextView campeonatos;
    private TextView vitorias;
    private TextView semTime;
    private Button bnt_Time;

    private ArrayList<Integer> arrayList = new ArrayList<>();

    private ProgressBar progressBar;

    private View parentLayout;

    private ConstraintLayout layout;

    private Time time = new Time();

    private CarregarTime carregarTime = new CarregarTime();

    private boolean carregou = false;

    public PerfilFragment() {
        setIcones();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        parentLayout = view.findViewById(R.id.framePerfil);

        progressBar = view.findViewById(R.id.perfil_ProgressBar);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        imgTime = view.findViewById(R.id.time_ImgIcoce);
        nomeTime = view.findViewById(R.id.time_TxtNome);
        liderTime = view.findViewById(R.id.time_TxtLider);
        campeonatosTime = view.findViewById(R.id.time_TxtCampeonatos);
        vitoriasTime = view.findViewById(R.id.time_TxtVitorias);
        quantTime = view.findViewById(R.id.time_TxtQuantMembros);

        img = view.findViewById(R.id.perfil_ImagemConta);
        nome = view.findViewById(R.id.perfil_NomeConta);
        nick = view.findViewById(R.id.perfil_NickLoLConta);
        campeonatos = view.findViewById(R.id.perfil_Campeonatos);
        vitorias = view.findViewById(R.id.perfil_Vitorias);
        semTime = view.findViewById(R.id.perfil_TxtSemTime);

        layout = view.findViewById(R.id.perfil_LayoutTime);

        layout.setVisibility(View.INVISIBLE);

        bnt_Time = view.findViewById(R.id.perfil_BntTime);
        bnt_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimeBuscaFragment fragment = new TimeBuscaFragment();
                ((MainActivity) getActivity()).novoFrame(fragment);
            }
        });

        carregou = false;

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ((MainActivity) getActivity()).setTitleToolbar(getString(R.string.stg_Perfil));

            if (!carregou) {
                if (icone == 0) {

                    if (UsuarioLogado.getTime().equals("nd")) {
                        semTime.setVisibility(View.VISIBLE);
                        bnt_Time.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);

                    } else {
                        time.setNome(UsuarioLogado.getTime());
                        if (getActivity() != null) {
                            carregarTime = new CarregarTime();
                            carregarTime.execute();
                        }
                    }

                    Log.i("INFO", "Time: " + UsuarioLogado.getTime());

                    img.setBackgroundResource(arrayList.get(UsuarioLogado.getIcone()));
                    nome.setText(UsuarioLogado.getNome());

                    nick.setText(nick.getText() + " " + UsuarioLogado.getNickLol());

                    campeonatos.setText(campeonatos.getText() + " " + String.valueOf(UsuarioLogado.getCampeonatos()));

                    vitorias.setText(vitorias.getText() + " " + String.valueOf(UsuarioLogado.getVitorias()));

                } else {
                    img.setBackgroundResource(arrayList.get(icone));
                    nome.setText(stg_Nome);
                    nick.setText(stg_Nick);
                    campeonatos.setText(stg_Campeonatos);
                    vitorias.setText(stg_Vitorias);

                    time.setNome(stg_Time);

                    if (getActivity() != null) {
                        carregarTime = new CarregarTime();
                        carregarTime.execute();
                    }
                }
            }
    }

    private class CarregarTime extends AsyncTask<Void,Void,Long>{

        @Override
        protected Long doInBackground(Void... voids) {

            final DatabaseReference referenceTime = FirebaseConfiguracoes.getFirebaseDatabase().child("Time");
            referenceTime.orderByKey().equalTo(time.getNome()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    time = dataSnapshot.getValue(Time.class);
                    mostrarTime();
                    referenceTime.removeEventListener(this);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    time = dataSnapshot.getValue(Time.class);
                    mostrarTime();
                    referenceTime.removeEventListener(this);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Snackbar.make(parentLayout, "Error: "+databaseError.getClass().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

            return null;
        }
    }

    private void mostrarTime() {

        int image = 0;
        if(time.getDivisao().equals("3ª Divisão")){
            image = R.drawable.div3;
        }
        imgTime.setBackgroundResource(image);
        nomeTime.setText(time.getNome());
        liderTime.setText(liderTime.getText()+" "+time.getLider());
        quantTime.setText(quantTime.getText()+" "+time.getQuantMembros()+"/7");
        campeonatosTime.setText(campeonatosTime.getText()+" "+time.getCampeonatos());
        vitoriasTime.setText(vitoriasTime.getText()+" "+time.getVitorias());

        layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        carregou = true;
    }

    public void setIcone(int icone) {
        this.icone = icone;
    }

    public void setStg_Nome(String stg_Nome) {
        this.stg_Nome = stg_Nome;
    }

    public void setStg_Nick(String stg_Nick) {
        this.stg_Nick = stg_Nick;
    }

    public void setStg_Campeonatos(String stg_Campeonatos) {
        this.stg_Campeonatos = stg_Campeonatos;
    }

    public void setStg_Vitorias(String stg_Vitorias) {
        this.stg_Vitorias = stg_Vitorias;
    }

    public void setStg_Time(String stg_Time) {
        this.stg_Time = stg_Time;
    }

    public void setIcones(){


        for(int i=0;i<146;i++){
            arrayList.add(2131165504+i);
        }

        //Log.i("INFO","ID 181: "+arrayList.get(171));
        Log.i("INFO","ID IMG: "+R.drawable.icone_001);
        Log.i("INFO","ID IMG: "+R.drawable.icone_146);
    }


    @Override
    public void onStop() {
        if (carregarTime != null)
            if (!carregarTime.isCancelled()) {
                carregarTime.cancel(true);
                carregou = false;
                super.onStop();
            } else {
                super.onStop();
            }
        else {
            super.onStop();
        }
    }
}
