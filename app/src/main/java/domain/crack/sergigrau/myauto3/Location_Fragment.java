package domain.crack.sergigrau.myauto3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sergigrau on 18/03/17.
 */

public class Location_Fragment extends Fragment implements OnMapReadyCallback {


    double Latitud, Longitud;

    private final GoogleMap map =  null;

    private MapView mapView;

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.location_fragment,container,false);

        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);


        return v;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

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
