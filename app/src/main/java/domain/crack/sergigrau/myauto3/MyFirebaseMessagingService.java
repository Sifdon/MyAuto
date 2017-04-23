package domain.crack.sergigrau.myauto3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by SergiGrau on 20/4/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFireBaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG,"From: "+ remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0){
            Log.d(TAG,"Message data payload: "+ remoteMessage.getData());
        }

        if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Message Notification Body: "+ remoteMessage.getNotification().getBody());
        }

        sendNotifications(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
    }


    private void sendNotifications (String messageBody, String tittle){

        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */ , intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(tittle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

        Toast.makeText(getApplicationContext(),"CAMBIOOO", Toast.LENGTH_SHORT).show();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */ , notificationBuilder.build());








    }
}
