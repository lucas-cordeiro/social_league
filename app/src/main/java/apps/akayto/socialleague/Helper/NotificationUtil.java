package apps.akayto.socialleague.Helper;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.List;

import apps.akayto.socialleague.R;

/**
 * Created by LUCASGABRIELALVESCOR on 25/03/2018.
 */

public class NotificationUtil {

    private static PendingIntent getPendingIntent(Context context, Intent intent, int id){
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        //Mantém activity pai na pilha de activities
        stackBuilder.addParentStack(intent.getComponent());

        //Configura a intent que vai abrir ao clicar na notificacao
        stackBuilder.addNextIntent(intent);

        //Cria a PendingIntent e atualiza caso exista uma com o mesmo id
        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }

    public static void create(Context context, Intent intent, String contentTitle, String contentText, int id){
        //Cria e PendingIntent para a Notificação (contém a intent original)
        PendingIntent p = getPendingIntent(context, intent, id);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setDefaults(Notification.DEFAULT_ALL);//Ativa configuração padrão
        builder.setSmallIcon(R.drawable.ic_menu_notifications);//Icone
        builder.setColor(context.getResources().getColor(R.color.colorAccent));//Cor da Notificacao
        builder.setContentTitle(contentTitle);//Titulo
        builder.setContentText(contentText);//Mensagem
        builder.setContentIntent(p);//Intent que será chama ao clicar na Notificação
        builder.setAutoCancel(true);//Autocancela a notificação

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id, builder.build());
    }

    public static void createHeadsUpNotification(Context context, Intent intent, String contentTitle, String contentText, int id){
        //Cria e PendingIntent para a Notificação (contém a intent original)
        PendingIntent p = getPendingIntent(context, intent, id);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setDefaults(Notification.DEFAULT_ALL);//Ativa configuração padrão
        builder.setSmallIcon(R.drawable.ic_menu_notifications);//Icone
        builder.setColor(context.getResources().getColor(R.color.colorAccent));//Cor da Notificacao
        builder.setFullScreenIntent(p,false);//Heads-ip notification
        builder.setContentTitle(contentTitle);//Titulo
        builder.setContentText(contentText);//Mensagem
        builder.setContentIntent(p);//Intent que será chama ao clicar na Notificação
        builder.setAutoCancel(true);//Autocancela a notificação

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id, builder.build());
    }

    public static void createBig(Context context, Intent intent, String contentTitle, String contentText, List<String> lines, int id){
        PendingIntent p = getPendingIntent(context, intent, id);

        int size = lines.size();
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        for(String s:lines){
            inboxStyle.addLine(s);
        }

        inboxStyle.setSummaryText(contentText);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setDefaults(Notification.DEFAULT_ALL);//Ativa configuração padrão
        builder.setSmallIcon(R.drawable.ic_menu_notifications);//Icone
        builder.setColor(context.getResources().getColor(R.color.colorAccent));//Cor da Notificacao
        builder.setContentTitle(contentTitle);//Titulo
        builder.setContentText(contentText);//Mensagem
        builder.setContentIntent(p);//Intent que será chama ao clicar na Notificação
        builder.setAutoCancel(true);//Autocancela a notificação
        builder.setNumber(size);//Número que aparece na notificação
        builder.setStyle(inboxStyle);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id, builder.build());
    }

    public static void createWithAction(Context context, Intent intent, String contentTitle, String contentText,List<Integer> icones, List<String> actions, List<PendingIntent> pendingIntents, int id){
        PendingIntent p = getPendingIntent(context, intent, id);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setDefaults(Notification.DEFAULT_ALL);//Ativa configuração padrão
        builder.setSmallIcon(R.drawable.ic_menu_notifications);//Icone
        builder.setColor(context.getResources().getColor(R.color.colorAccent));//Cor da Notificacao
        builder.setContentTitle(contentTitle);//Titulo
        builder.setContentText(contentText);//Mensagem
        builder.setContentIntent(p);//Intent que será chama ao clicar na Notificação
        builder.setAutoCancel(true);//Autocancela a notificação

        for(int i=0;i<icones.size();i++){
            builder.addAction(icones.get(i), actions.get(i), pendingIntents.get(i));
        }

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id, builder.build());
    }

    public static void cancell(Context context, int id){
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.cancel(id);
    }

    public static void cancellAll(Context context){
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.cancelAll();
    }
}
