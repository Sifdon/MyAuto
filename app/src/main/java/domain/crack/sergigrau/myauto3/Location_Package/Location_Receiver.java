package domain.crack.sergigrau.myauto3.Location_Package;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.LocationResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by sergigrau on 17/03/17.
 */

public class Location_Receiver extends BroadcastReceiver {

    private double Longitud_anterior, Latitud_anterior, Longitud_actual, Latitud_actual;


    private int distance;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        FirebaseDatabase db =  FirebaseDatabase.getInstance();
        final DatabaseReference ref_latitud =  db.getReference("Latitud");
        final DatabaseReference ref_longitud =  db.getReference("Longitud");
        final DatabaseReference ref_moviment = db.getReference("Moviment");
        final DatabaseReference ref_olicontador = db.getReference("Oli Contador");

        if(LocationResult.hasResult(intent)){
            LocationResult locationResult = LocationResult.extractResult(intent);
            final Location location = locationResult.getLastLocation();

            ref_moviment.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class).equals("vehicle")){
                        ref_latitud.setValue(location.getLatitude());
                        ref_longitud.setValue(location.getLongitude());
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            ref_latitud.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Latitud_anterior = dataSnapshot.getValue(Double.class);
                    ref_longitud.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Longitud_anterior = dataSnapshot.getValue(Double.class);
                            Latitud_actual =  location.getLatitude();
                            Longitud_actual =  location.getLongitude();
                            ref_moviment.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue(String.class).equals("vehicle")){
                                        distance = getDistance((int)Latitud_anterior,(int)Longitud_anterior,(int)Latitud_actual,(int)Longitud_actual);
                                        ref_olicontador.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                int tmp = dataSnapshot.getValue(Integer.class);
                                                tmp = tmp + distance;
                                                ref_olicontador.setValue(tmp);
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }


    public static int getDistance(int lat_a,int lng_a, int lat_b, int lon_b){
        int radius = 6371000; //Radio de la tierra
        double lat1 = lat_a / 1E6;
        double lat2 = lat_b / 1E6;
        double lon1 = lng_a / 1E6;
        double lon2 = lon_b / 1E6;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon /2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (int) (radius * c);

    }




}
