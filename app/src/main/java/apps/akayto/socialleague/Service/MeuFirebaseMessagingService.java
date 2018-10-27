package apps.akayto.socialleague.Service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import apps.akayto.socialleague.Activity.SplashScreenActivity;
import apps.akayto.socialleague.Helper.NotificationUtil;
import apps.akayto.socialleague.MainActivity;
import apps.akayto.socialleague.Models.NotificacaoSolicitarEntrada;

/**
 * Created by LUCASGABRIELALVESCOR on 20/03/2018.
 */

public class MeuFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "INFO_NOTIFICACAO";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.i(TAG,"onMessageReceived");

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.i(TAG, "Message data payload: " + remoteMessage.getData());

            if (remoteMessage.getData().containsKey("tipo")) {

                String key = remoteMessage.getData().get("key");
                String tipo = remoteMessage.getData().get("tipo");

                if (tipo.equals("2")) {
                    NotificacaoSolicitarEntrada notificacaoDados = new NotificacaoSolicitarEntrada(
                            remoteMessage.getData().get("title"),
                            remoteMessage.getData().get("body"),
                            tipo,
                            true,
                            remoteMessage.getData().get("membroId")
                    );

                    Log.i(TAG, "Notificação: " + notificacaoDados.getTitle() + "| Key: " + key);

                    Intent notificationIntent;

                    if (MainActivity.isAppRunning) {//App Aberto

                        notificationIntent = new Intent(this, MainActivity.class);

                    } else {//App Fechado
                        notificationIntent = new Intent(this, SplashScreenActivity.class);
                    }

                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    notificationIntent.putExtra("notificacao",notificacaoDados);
                    Context context = this;
                    NotificationUtil.create(
                            context,
                            notificationIntent,
                            notificacaoDados.getTitle(),
                            notificacaoDados.getMenssagem(),
                            2
                    );


                }

            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }
}
