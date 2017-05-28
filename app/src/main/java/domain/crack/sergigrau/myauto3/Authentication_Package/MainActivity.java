package domain.crack.sergigrau.myauto3.Authentication_Package;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import domain.crack.sergigrau.myauto3.Domain_Package.Home;
import domain.crack.sergigrau.myauto3.R;

/**
 * Created by SergiGrau on 20/4/17.
 */

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    private  FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        Button login = (Button) findViewById(R.id.login);
        final Button register = (Button) findViewById(R.id.register);
        final EditText user_log = (EditText) findViewById(R.id.user_login);
        final EditText pass_log = (EditText) findViewById(R.id.pass_login);
        final EditText user_reg = (EditText) findViewById(R.id.user_register);
        final EditText pass_reg = (EditText) findViewById(R.id.pass_register);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    // User is signed in
                    Log.d("SIGIN", "onAuthStateChanged:sig_in:" + user.getUid());
                }else{
                    // User is signed out
                    Log.d("SIGOUT", "onAuthStateChanged:sig_out:");
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user_log.getText().toString().equals("") || pass_log.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Hay algún campo vacío", Toast.LENGTH_SHORT).show();
                }else{
                    login();
                }

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_reg.getText().toString().equals("") || pass_reg.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Hay algún campo vacío", Toast.LENGTH_SHORT).show();
                }else{
                    register();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void register(){
        final EditText user_reg = (EditText) findViewById(R.id.user_register);
        final EditText pass_reg = (EditText) findViewById(R.id.pass_register);
        mAuth.createUserWithEmailAndPassword(user_reg.getText().toString(), pass_reg.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("AUTH", "createUserWithEmail:onComplete" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.d("FIREBASE AUTH", "onComplete: Failed-" + task.getException().getMessage());
                            Toast.makeText(getApplicationContext(), "REGISTER FAILED", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "REGISTER SUCCESSFUL", Toast.LENGTH_SHORT).show();
                            String tok = FirebaseInstanceId.getInstance().getToken();
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference ref = db.getReference("tokendevices");
                            ref.push().setValue(tok);
                        }
                    }
                });
    }

    public void login(){
        final EditText user_log = (EditText) findViewById(R.id.user_login);
        final EditText pass_log = (EditText) findViewById(R.id.pass_login);
        mAuth.signInWithEmailAndPassword(user_log.getText().toString(), pass_log.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LOGIN","sigInWithEmail:onComplete" + task.isSuccessful());
                        if(!task.isSuccessful()){
                            Log.w("LOGIN","sigInWithEmail:failed" + task.getException());
                            Toast.makeText(getApplicationContext(),"LOGIN ERRÓNEO",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"LOGIN CORRECTO",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}


