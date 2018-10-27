package apps.akayto.socialleague.Service;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.Models.UsuarioLogado;

/**
 * Created by LUCASGABRIELALVESCOR on 20/03/2018.
 */


public class MeuFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "INFO"+MeuFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String refreshedToken) {
        UsuarioLogado.setFirebaseToken(refreshedToken);
    }
}
