package domain.crack.sergigrau.myauto3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sergigrau on 15/03/17.
 */

public class InfoManteniment_Fragment extends Fragment {


    String kmetres, kmetresrestants;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.infomanteniment_fragment,container,false);

        final TextView t1 = (TextView)v.findViewById(R.id.km);
        final TextView t2 = (TextView)v.findViewById(R.id.kmrestants);
        final TextView t3 = (TextView)v.findViewById(R.id.mechanic);
        final TextView t4 = (TextView)v.findViewById(R.id.process);
        Button b1 = (Button)v.findViewById(R.id.realized);
        Button b2 = (Button)v.findViewById(R.id.init);


        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference ref_olicontador = db.getReference("Oli Contador");
        final DatabaseReference ref_proces = db.getReference("Proces");
        final DatabaseReference ref_taller = db.getReference("Taller");



        ref_olicontador.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int tmp =  dataSnapshot.getValue(Integer.class);
                tmp = tmp/1000;
                kmetresrestants = Integer.toString(tmp);
                t2.setText("Have realized:"+(kmetresrestants)+" KM");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        t1.setText("Change the Oil at:" + 15000 + " KM");


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



        return v;

    }
}
