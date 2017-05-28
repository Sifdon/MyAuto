package domain.crack.sergigrau.myauto3.Domain_Package;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import domain.crack.sergigrau.myauto3.Authentication_Package.MainActivity;
import domain.crack.sergigrau.myauto3.Location_Package.Location_Fragment;
import domain.crack.sergigrau.myauto3.Location_Package.Location_Receiver;
import domain.crack.sergigrau.myauto3.Monitoring_Package.ActivityRecognizedService;
import domain.crack.sergigrau.myauto3.Preferences_Package.Preferences;
import domain.crack.sergigrau.myauto3.R;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {



    // LogCat tag
    private static final String TAG = Home.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation, location;


    private Intent locationIntent;

    private boolean conect_location = false;


    // Google client to interact with Google API
    public GoogleApiClient mGoogleApiClient,mGoogleApiClientMonitor;


    private LocationRequest mLocationRequest;


    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private static final int REQUEST_LOCATION = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container,new description_fragment()).commit();





        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                final FragmentManager fragmentManager = getSupportFragmentManager();

                FirebaseDatabase db = FirebaseDatabase.getInstance();


                DatabaseReference ref_coche = db.getReference("Coche_id");



                if (id == R.id.addauto) {



                    TextView t = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView);

                    ref_coche.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue(String.class).equals("")){
                                fragmentManager.beginTransaction().replace(R.id.container,new AddAuto_Fragment()).commit();

                            }else{
                                Toast.makeText(getApplication(),"There is already an added vehicle", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else if (id == R.id.preferences) {
                    startActivity(new Intent(getApplication(), Preferences.class));
                } else if(id == R.id.help){
                    fragmentManager.beginTransaction().replace(R.id.container, new Help_Fragment()).commit();
                } else if (id == R.id.location){
                    fragmentManager.beginTransaction().replace(R.id.container, new Location_Fragment()).commit();
                } else if(id == R.id.info_manteniment){
                    fragmentManager.beginTransaction().replace(R.id.container, new InfoManteniment_Fragment()).commit();
                }else if(id ==  R.id.logout){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }else if(id == R.id.log){
                    fragmentManager.beginTransaction().replace(R.id.container, new History_Fragment()).commit();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        mGoogleApiClientMonitor.connect();
        super.onStart();

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        mGoogleApiClientMonitor.disconnect();
        super.onStop();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
        if (mGoogleApiClientMonitor == null) {
            mGoogleApiClientMonitor = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(ActivityRecognition.API).build();
        }



    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);


    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }




    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
        Toast.makeText(this, "GoogleApiClient FAILED",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        locationIntent = new Intent(getApplicationContext(), Location_Receiver.class);
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent locationPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

            if(mGoogleApiClient.isConnected() && conect_location == false){
                FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationPendingIntent);
                conect_location = true;
            }


            if(mGoogleApiClientMonitor.isConnected()){
                ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClientMonitor, 3000, pendingIntent);
            }




    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        mGoogleApiClientMonitor.connect();

    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;



    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        final DatabaseReference ref_brand = db.getReference("Brand");
        final DatabaseReference ref_model = db.getReference("Model");
        final DatabaseReference ref_motor = db.getReference("Motor");

        DatabaseReference ref_cocheid = db.getReference("Coche_id");

        SharedPreferences preferencias = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final ImageView imageView_header = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        final TextView textView_header = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView);
        TextView welcome = (TextView)navigationView.getHeaderView(0).findViewById(R.id.welcome);
        welcome.setText("Welcome " + preferencias.getString("name",""));


            ref_cocheid.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class).equals("coche")){
                        ref_brand.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final String brand = dataSnapshot.getValue(String.class);

                                ref_model.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final String model = dataSnapshot.getValue(String.class);

                                        ref_motor.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String motor = dataSnapshot.getValue(String.class);
                                                String result = brand + " " + model + " " + motor;
                                                textView_header.setText(result);
                                                imageView_header.setImageResource(R.drawable.coche);


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
                    }else{
                        imageView_header.setImageResource(R.drawable.empty);
                        textView_header.setText("Not Car Introduced");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container_tittle,new Tittle_Fragment()).commit();

    }

   @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }




}
