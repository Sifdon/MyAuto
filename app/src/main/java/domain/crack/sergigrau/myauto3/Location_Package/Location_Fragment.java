package domain.crack.sergigrau.myauto3.Location_Package;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import domain.crack.sergigrau.myauto3.R;

/**
 * Created by sergigrau on 18/03/17.
 */

public class Location_Fragment extends Fragment implements OnMapReadyCallback {


    double Latitud, Longitud;


    private MapView mapView;

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View v;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean network = SP.getBoolean("network",true);

        if(network){
            if(checkNetwork()){
                v = inflater.inflate(R.layout.location_fragment,container,false);
                mapView = (MapView) v.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                mapView.onResume();
                mapView.getMapAsync(this);
            }else{
                v = inflater.inflate(R.layout.no_network, container,false);

            }
        }else{
            v = inflater.inflate(R.layout.location_fragment,container,false);
            mapView = (MapView) v.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        return v;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean network = SP.getBoolean("network",true);

        if(network) {
            if (checkNetwork()) {
                FirebaseDatabase db =  FirebaseDatabase.getInstance();
                final DatabaseReference ref_latitud =  db.getReference("Latitud");

                final DatabaseReference ref_longitud =  db.getReference("Longitud");


                ref_latitud.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Latitud = dataSnapshot.getValue(Double.class);

                        ref_longitud.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Longitud = dataSnapshot.getValue(Double.class);
                                googleMap.addMarker(new MarkerOptions().position(new LatLng(Latitud, Longitud)).title("Your Car is Here"));


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
        }else{
            FirebaseDatabase db =  FirebaseDatabase.getInstance();
            final DatabaseReference ref_latitud =  db.getReference("Latitud");

            final DatabaseReference ref_longitud =  db.getReference("Longitud");


            ref_latitud.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Latitud = dataSnapshot.getValue(Double.class);

                    ref_longitud.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Longitud = dataSnapshot.getValue(Double.class);
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(Latitud, Longitud)).title("Your Car is Here"));


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

    public boolean checkNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            if(networkInfo.getType() ==  ConnectivityManager.TYPE_WIFI){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }


}
