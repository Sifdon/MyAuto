package domain.crack.sergigrau.myauto3.Domain_Package;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import domain.crack.sergigrau.myauto3.R;

/**
 * Created by SergiGrau on 24/5/17.
 */

public class History_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean network = SP.getBoolean("network",true);

        if(network){
            if(checkNetwork()){
                v =  inflater.inflate(R.layout.history_fragment,container,false);
                final TextView t1 = (TextView) v.findViewById(R.id.info_historial);
                final FirebaseDatabase db = FirebaseDatabase.getInstance();
                final DatabaseReference ref_history = db.getReference("Log/History/message");
                ref_history.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        t1.setText(dataSnapshot.getValue(String.class));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else{
                v = inflater.inflate(R.layout.no_network,container,false);
            }
        }else{
            v =  inflater.inflate(R.layout.history_fragment,container,false);
            final TextView t1 = (TextView) v.findViewById(R.id.info_historial);
            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            final DatabaseReference ref_history = db.getReference("Log/History/message");
            ref_history.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    t1.setText(dataSnapshot.getValue(String.class));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
