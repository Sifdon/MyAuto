package domain.crack.sergigrau.myauto3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by sergigrau on 15/03/17.
 */

public class AddAuto_Fragment extends Fragment{

    private ImageView cancel, save;

    private EditText model_edit, kilometers_edit;

    private Spinner brands_spinner, motors_spinner,antiguity_spinner;

    private int manteniment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.add_auto,container,false);


        return view;

    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cancel = (ImageView) getView().findViewById(R.id.cancel);

        save = (ImageView)getView().findViewById(R.id.save);

        brands_spinner = (Spinner) getView().findViewById(R.id.brand);

        motors_spinner = (Spinner) getView().findViewById(R.id.motors);

        antiguity_spinner = (Spinner) getView().findViewById(R.id.years);

        model_edit = (EditText) getView().findViewById(R.id.model_edit);

        kilometers_edit = (EditText)getView().findViewById(R.id.kilometers_edit);

        antiguity_spinner.setOnItemSelectedListener(new SpinnerSelected());

        brands_spinner.setOnItemSelectedListener(new SpinnerSelected());

        motors_spinner.setOnItemSelectedListener(new SpinnerSelected());



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

                String model = null;
                model =  model_edit.getText().toString();

                if(model.equals("")){

                    Toast.makeText(getActivity().getBaseContext(),"Please Introduce the model",Toast.LENGTH_SHORT).show();

                }else{


                    TextView textView_header = (TextView) getActivity().findViewById(R.id.textView);
                    ImageView imageView_header = (ImageView)getActivity().findViewById(R.id.imageView);
                    kilometers_edit = (EditText)getView().findViewById(R.id.kilometers_edit);


                    imageView_header.setImageResource(R.mipmap.car);

                    String brand = brands_spinner.getSelectedItem().toString();
                    String motor = motors_spinner.getSelectedItem().toString();
                    String antiguity = antiguity_spinner.getSelectedItem().toString();
                    String kilometers = kilometers_edit.getText().toString();
                    textView_header.setText(brand + " " + model + " " + motor );

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference ref_cocheid = db.getReference("Coche_id");
                    DatabaseReference ref_brand = db.getReference("Brand");
                    DatabaseReference ref_model = db.getReference("Model");
                    DatabaseReference ref_motor = db.getReference("Motor");
                    DatabaseReference ref_antiguity = db.getReference("Antiguity");
                    DatabaseReference ref_kilometers = db.getReference("Kilometers");
                    DatabaseReference ref_olicontador = db.getReference("Oli Contador");

                    ref_cocheid.setValue("coche");
                    ref_brand.setValue(brand);
                    ref_model.setValue(model);
                    ref_motor.setValue(motor);
                    ref_antiguity.setValue(antiguity);
                    ref_kilometers.setValue(kilometers);

                    manteniment = (Integer.parseInt(kilometers)*1000)%15000000;

                    ref_olicontador.setValue(manteniment);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container,new description_fragment()).commit();

                }



            }
        });


    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static class SpinnerSelected implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    }

}
