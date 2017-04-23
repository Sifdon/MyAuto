package domain.crack.sergigrau.myauto3;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sergigrau on 17/03/17.
 */

public class Preferences_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.preferences_fragment,container,false);




        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        final EditText editText_km = (EditText) getView().findViewById(R.id.kilometers_edit);

        ImageView cancel = (ImageView) getView().findViewById(R.id.cancel);

        ImageView save = (ImageView) getView().findViewById(R.id.save);

        Button remove = (Button) getView().findViewById(R.id.remove);

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        final DatabaseReference ref_kilometers = db.getReference("Kilometers");
        final DatabaseReference ref_coche = db.getReference("Coche_id");
        final DatabaseReference ref_brand = db.getReference("Brand");
        final DatabaseReference ref_model = db.getReference("Model");
        final DatabaseReference ref_motor = db.getReference("Motor");
        final DatabaseReference ref_antiguity = db.getReference("Antiguity");
        final DatabaseReference ref_olicontador = db.getReference("Oli Contador");


        ref_kilometers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editText_km.setText(dataSnapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container,new description_fragment()).commit();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ref_coche.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue(String.class).equals("coche")){
                            String km = editText_km.getText().toString();

                            ref_kilometers.setValue(km);
                        }else{
                            Toast.makeText(getActivity(),"No vehicle added",Toast.LENGTH_SHORT).show();


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

}
