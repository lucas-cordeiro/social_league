package apps.akayto.socialleague.Fragments;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import apps.akayto.socialleague.MainActivity;
import apps.akayto.socialleague.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TelaInicialFragment extends Fragment {


    private FloatingActionButton fab;

    private Fragment fragmentAnterior;

    public TelaInicialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).setTitleToolbar(getResources().getString(R.string.app_name));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tela_inicial, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        final VideoView videoView = view.findViewById(R.id.videoView);


        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });*/
    }

    public Fragment getFragmentAnterior() {
        return fragmentAnterior;
    }

    public void setFragmentAnterior(Fragment fragmentAnterior) {
        this.fragmentAnterior = fragmentAnterior;
    }
}
