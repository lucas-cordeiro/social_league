package apps.akayto.socialleague.Fragments;


import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import apps.akayto.socialleague.Adapters.AdapterTime;
import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.Helper.RecyclerItemClickListener;
import apps.akayto.socialleague.MainActivity;
import apps.akayto.socialleague.Models.Time;
import apps.akayto.socialleague.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeBuscaFragment extends Fragment {


    private ProgressBar progressBar;

    private ImageView img_Criar;
    private TextView txt_Criar;

    private TextView txt_Disponibilidade;

    private RecyclerView recyclerView;

    private long quantTimes;

    private FrameLayout frameLayout;

    private ArrayList<Time> timeArrayList = new ArrayList<>();

    private BuscarTimes buscarTimes = new BuscarTimes();

    private boolean carregou = false;

    private AdapterTime adapter;

    public TimeBuscaFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_busca, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.time_ProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        img_Criar = view.findViewById(R.id.time_ImgCriarNovoTime);
        img_Criar.setOnClickListener(listener);

        txt_Criar = view.findViewById(R.id.time_TxTCriarNovoTime);
        txt_Criar.setOnClickListener(listener);

        txt_Disponibilidade = view.findViewById(R.id.time_TxtDisponibilidade);

        recyclerView = view.findViewById(R.id.time_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        frameLayout = view.findViewById(R.id.containerTimeBusca);

        controleComponentes(false);

        carregou = false;

    }

    @Override
    public void onStart() {
        super.onStart();

        ((MainActivity) getActivity()).setTitleToolbar(getString(R.string.bnt_EncontrarTime));

        if (getActivity() != null)
            if (!carregou) {
                buscarTimes = new BuscarTimes();
                buscarTimes.execute();
            }
    }

    private class BuscarTimes extends AsyncTask<Void,Void,Long>{

        @Override
        protected Long doInBackground(Void... voids) {

            final DatabaseReference referenceTime = FirebaseConfiguracoes.getFirebaseDatabase().child("Times_Disponiveis");
            referenceTime.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    quantTimes = dataSnapshot.getChildrenCount();
                    Log.i("INFO", "Count: " + quantTimes);

                    referenceTime.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getKey().equals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")) {
                                if(getActivity()!=null)
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(quantTimes>1){
                                                txt_Disponibilidade.setText(getActivity().getResources().getString(R.string.txt_Times_Disponiveis)+" "+(quantTimes-1));
                                                buscarTimes();
                                            }else{
                                                txt_Disponibilidade.setText(getActivity().getResources().getString(R.string.txt_Nao_Times_Disponiveis));
                                                controleComponentes(true);
                                            }
                                        }
                                    });
                            }

                            Log.i("INFO", "dataSnapshot.getKey(): " + dataSnapshot.getKey());
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
                            Snackbar.make(frameLayout, "Erro: "+databaseError, Snackbar.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Snackbar.make(frameLayout, "Erro: "+databaseError, Snackbar.LENGTH_SHORT).show();
                }
            });

            return null;
        }
    }

    private void buscarTimes() {

        timeArrayList = new ArrayList<>();
        adapter = new AdapterTime(timeArrayList);
        recyclerView.setAdapter(adapter);

        final DatabaseReference referenceTime = FirebaseConfiguracoes.getFirebaseDatabase().child("Time");
        referenceTime.orderByChild("quantMembros").endAt(7).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getKey().equals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")) {
                    Time time = dataSnapshot.getValue(Time.class);
                    timeArrayList.add(time);
                    adapter.notifyItemInserted(adapter.getItemCount()-1);
                } else {
                    setAdapterRecyclerView();
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
                Snackbar.make(frameLayout, "Error: "+databaseError.getClass().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                controleComponentes(true);
                referenceTime.removeEventListener(this);
            }
        });
    }

    private void setAdapterRecyclerView() {
        carregou = true;

        controleComponentes(true);

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Time time = timeArrayList.get(position);

                PerfilTimeFragment fragment = new PerfilTimeFragment();
                fragment.setStg_NomeTime(time.getNome());
                fragment.setTipo(2);
                ((MainActivity) getActivity()).novoFrame(fragment);
                if (buscarTimes != null)
                    if (!buscarTimes.isCancelled())
                        buscarTimes.cancel(true);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    public void controleComponentes(boolean mostrar){
        if(mostrar){
            progressBar.setVisibility(View.INVISIBLE);
            img_Criar.setEnabled(true);
            txt_Criar.setEnabled(true);
            recyclerView.setEnabled(true);
        }else{
            progressBar.setVisibility(View.VISIBLE);
            img_Criar.setEnabled(false);
            txt_Criar.setEnabled(false);
            recyclerView.setEnabled(false);
        }
    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CriarTimeFragment fragment = new CriarTimeFragment();
                    ((MainActivity) getActivity()).novoFrame(fragment);
                    if (buscarTimes != null)
                        if (!buscarTimes.isCancelled()) {
                            buscarTimes.cancel(true);
                        }
                }

            });
        }
    };


    @Override
    public void onStop() {
        if (buscarTimes != null)
            if (!buscarTimes.isCancelled()) {
                buscarTimes.cancel(true);
                carregou = false;
                super.onStop();
                carregou = false;
            } else {
                super.onStop();
            }
        else {
            super.onStop();
        }
    }
}
