package domain.crack.sergigrau.myauto3.Domain_Package;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import domain.crack.sergigrau.myauto3.R;

/**
 * Created by sergigrau on 15/03/17.
 */

public class InfoManteniment_Fragment extends Fragment {


    String  kmetresrestants;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View v;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean network = SP.getBoolean("network",true);

        if(network){
            if(checkNetwork()){
                v = inflater.inflate(R.layout.infomanteniment_fragment,container,false);
                final TextView t1 = (TextView)v.findViewById(R.id.km);
                final TextView t2 = (TextView)v.findViewById(R.id.kmrestants);
                final TextView t3 = (TextView)v.findViewById(R.id.mechanic);
                final TextView t4 = (TextView)v.findViewById(R.id.process);
                final EditText t5 = (EditText) v.findViewById(R.id.k);
                Button b3 = (Button)v.findViewById(R.id.kmsave);
                Button b1 = (Button)v.findViewById(R.id.realized);
                Button b2 = (Button)v.findViewById(R.id.init);
                Button remove = (Button) v.findViewById(R.id.remove);


                final FirebaseDatabase db = FirebaseDatabase.getInstance();
                final DatabaseReference ref_kilometers = db.getReference("Kilometers");
                final DatabaseReference ref_coche = db.getReference("Coche_id");
                final DatabaseReference ref_brand = db.getReference("Brand");
                final DatabaseReference ref_model = db.getReference("Model");
                final DatabaseReference ref_motor = db.getReference("Motor");
                final DatabaseReference ref_antiguity = db.getReference("Antiguity");
                final DatabaseReference ref_olicontador = db.getReference("Oli Contador");
                final DatabaseReference ref_proces = db.getReference("Proces");
                final DatabaseReference ref_taller = db.getReference("Taller");



                ref_olicontador.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int tmp =  dataSnapshot.getValue(Integer.class);
                        kmetresrestants = Integer.toString(tmp);
                        t2.setText("Have realized:"+(kmetresrestants)+" KM");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                t1.setText("Change the Oil at:" + 15000 + " KM");


                b3.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ref_kilometers.setValue(Integer.parseInt(t5.getText().toString()));
                    }
                });

                b1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //sqLiteDatabase.execSQL("UPDATE Manteniment SET Oli_contador ='0'WHERE Manteniment_id = 'manteniment'");
                        if(t4.getText().toString().equals("Maintenance is being done") ){
                            t4.setText("");
                            ref_proces.setValue("Manteniment acabat"); // Node agafa la data i calcula el temps tardat
                            ref_olicontador.setValue(0);
                            t2.setText("Have realized:0 KM");
                        }else{
                            Toast.makeText(getContext(),"No se ha iniciado el mantenimiento todavía",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                b2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //sqLiteDatabase.execSQL("UPDATE Manteniment SET Oli_contador ='0'WHERE Manteniment_id = 'manteniment'");
                        if(t3.getText().toString().equals("")){
                            Toast.makeText(getContext(),"Introduce un taller",Toast.LENGTH_SHORT).show();
                        }else{
                            t4.setText("Maintenance is being done");
                            t4.setBackgroundResource(R.color.colorGreen);
                            ref_proces.setValue("Manteniment en proces");
                            ref_taller.setValue(t3.getText().toString());// Node agafa la data i el taller
                        }
                    }
                });

                remove.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final ImageView navi_image = (ImageView) getActivity().findViewById(R.id.imageView);
                        final TextView navi_text = (TextView) getActivity().findViewById(R.id.textView);

                        ref_coche.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue(String.class).equals("coche")) {


                                    navi_image.setImageResource(R.mipmap.ic_code_black_24dp);
                                    navi_text.setText("Not Car Introduced");

                                    ref_coche.setValue("");
                                    ref_brand.setValue("");
                                    ref_model.setValue("");
                                    ref_motor.setValue("");
                                    ref_antiguity.setValue("");
                                    ref_kilometers.setValue("");
                                    ref_olicontador.setValue(0);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container,new description_fragment()).commit();

                    }
                });

                ref_kilometers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int km = Integer.parseInt(dataSnapshot.getValue(String.class));
                        int tmp = km % 15000;
                        ref_olicontador.setValue(tmp);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }else{
                v = inflater.inflate(R.layout.no_network, container,false);

            }
        }else{
            v = inflater.inflate(R.layout.infomanteniment_fragment,container,false);
            final TextView t1 = (TextView)v.findViewById(R.id.km);
            final TextView t2 = (TextView)v.findViewById(R.id.kmrestants);
            final TextView t3 = (TextView)v.findViewById(R.id.mechanic);
            final TextView t4 = (TextView)v.findViewById(R.id.process);
            final EditText t5 = (EditText) v.findViewById(R.id.k);
            Button b3 = (Button)v.findViewById(R.id.kmsave);
            Button b1 = (Button)v.findViewById(R.id.realized);
            Button b2 = (Button)v.findViewById(R.id.init);
            Button remove = (Button) v.findViewById(R.id.remove);



            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            final DatabaseReference ref_kilometers = db.getReference("Kilometers");
            final DatabaseReference ref_coche = db.getReference("Coche_id");
            final DatabaseReference ref_brand = db.getReference("Brand");
            final DatabaseReference ref_model = db.getReference("Model");
            final DatabaseReference ref_motor = db.getReference("Motor");
            final DatabaseReference ref_antiguity = db.getReference("Antiguity");
            final DatabaseReference ref_olicontador = db.getReference("Oli Contador");
            final DatabaseReference ref_proces = db.getReference("Proces");
            final DatabaseReference ref_taller = db.getReference("Taller");



            ref_olicontador.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int tmp =  dataSnapshot.getValue(Integer.class);
                    kmetresrestants = Integer.toString(tmp);
                    t2.setText("Have realized:"+(kmetresrestants)+" KM");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            t1.setText("Change the Oil at:" + 15000 + " KM");


            b3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ref_kilometers.setValue(Integer.parseInt(t5.getText().toString()));
                }
            });

            b1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //sqLiteDatabase.execSQL("UPDATE Manteniment SET Oli_contador ='0'WHERE Manteniment_id = 'manteniment'");
                    if(t4.getText().toString().equals("Maintenance is being done") ){
                        t4.setText("");
                        ref_proces.setValue("Manteniment acabat"); // Node agafa la data i calcula el temps tardat
                        ref_olicontador.setValue(0);
                        t2.setText("Have realized:0 KM");
                    }else{
                        Toast.makeText(getContext(),"No se ha iniciado el mantenimiento todavía",Toast.LENGTH_SHORT).show();
                    }

                }
            });

            b2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //sqLiteDatabase.execSQL("UPDATE Manteniment SET Oli_contador ='0'WHERE Manteniment_id = 'manteniment'");
                    if(t3.getText().toString().equals("")){
                        Toast.makeText(getContext(),"Introduce un taller",Toast.LENGTH_SHORT).show();
                    }else{
                        t4.setText("Maintenance is being done");
                        t4.setBackgroundResource(R.color.colorGreen);
                        ref_proces.setValue("Manteniment en proces");
                        ref_taller.setValue(t3.getText().toString());// Node agafa la data i el taller
                    }
                }
            });

            ref_kilometers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int km = Integer.parseInt(dataSnapshot.getValue(String.class));
                    int tmp = km % 15000;
                    ref_olicontador.setValue(tmp);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            remove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    final ImageView navi_image = (ImageView) getActivity().findViewById(R.id.imageView);
                    final TextView navi_text = (TextView) getActivity().findViewById(R.id.textView);

                    ref_coche.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue(String.class).equals("coche")) {


                                navi_image.setImageResource(R.mipmap.ic_code_black_24dp);
                                navi_text.setText("Not Car Introduced");

                                ref_coche.setValue("");
                                ref_brand.setValue("");
                                ref_model.setValue("");
                                ref_motor.setValue("");
                                ref_antiguity.setValue("");
                                ref_kilometers.setValue("");
                                ref_olicontador.setValue(0);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container,new description_fragment()).commit();

                }
            });

        }

        return v;

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
