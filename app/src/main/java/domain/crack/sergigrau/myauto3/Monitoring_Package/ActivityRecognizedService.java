package domain.crack.sergigrau.myauto3.Monitoring_Package;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by sergigrau on 17/03/17.
 */

public class ActivityRecognizedService extends IntentService {


    public ActivityRecognizedService(){
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name){
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {



        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            FirebaseDatabase db1 =  FirebaseDatabase.getInstance();
            DatabaseReference ref_moviment = db1.getReference("Moviment");

            DetectedActivity detectedActivity = result.getMostProbableActivity();

            switch (detectedActivity.getType()){
                case DetectedActivity.IN_VEHICLE: {
                    ref_moviment.setValue("vehicle");
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    ref_moviment.setValue("caminant");
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    ref_moviment.setValue("caminant");
                    break;
                }
                case DetectedActivity.RUNNING: {
                    ref_moviment.setValue("caminant");
                    break;
                }
                case DetectedActivity.STILL: {
                    ref_moviment.setValue("caminant");
                    break;
                }
                case DetectedActivity.TILTING: {
                    ref_moviment.setValue("caminant");
                    break;
                }
                case DetectedActivity.WALKING: {
                    ref_moviment.setValue("caminant");
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    ref_moviment.setValue("caminant");
                    break;
                }
            }

        }

    }


}