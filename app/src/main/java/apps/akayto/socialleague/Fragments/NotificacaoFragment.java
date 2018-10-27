package apps.akayto.socialleague.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import apps.akayto.socialleague.MainActivity;
import apps.akayto.socialleague.Models.NotificacaoSolicitarEntrada;
import apps.akayto.socialleague.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificacaoFragment extends Fragment {

    public NotificacaoSolicitarEntrada notificacao;

    public NotificacaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_notificacao, container, false);

        if(notificacao!=null)
        Toast.makeText(getContext(), "Notificacao: "+notificacao.getTitle()+" Membro ID: "+notificacao.getMembroId(), Toast.LENGTH_LONG).show();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).setTitleToolbar(getString(R.string.stg_Notificacoes));
    }
}
