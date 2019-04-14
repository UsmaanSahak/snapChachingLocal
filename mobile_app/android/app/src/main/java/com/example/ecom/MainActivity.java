package com.example.ecom;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.JsonReader;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.SYSTEM_ALERT_WINDOW;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LocationManager locManager;
    Context mContext;
    String resJson;
    LocationListener llistener;
    String upc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        /*
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */
        /*New Code Here*/

    /*

        locManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        llistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("test");
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,10, llistener);

*/
        final Context yah = this;
        Button scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator((Activity) yah).initiateScan();

            }
        });








        final EditText searchStuff = findViewById(R.id.searchStuff);
        searchStuff.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(MainActivity.this, "Running Search on " + searchStuff.getText() + "\n(PLACEHOLDER/DEBUGGING)", Toast.LENGTH_SHORT).show();
                    //SearchFunction();
                    return true;
                }
                return false;
            }
        });


    }
    /*
    protected void onResume() {
        super.onResume();
        isLocationEnabled();
    }

    private void isLocationEnabled() {
        if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setTitle("Enable Location.");
            alert.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert2 = alert.create();
            alert.show();
        }
        else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Your location is enabled.");
            alertDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
    }

    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode,data);
        System.out.println("TEST123");
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,"Result not found",Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    //JSONObject obj = new JSONObject(result.getContents());
                    //String thing = obj.getString("name") + obj.getString("address");
                    final String obj = result.getContents();
                    LookupThread t = new LookupThread();
                    t.execute(obj,resJson);

                    //Toast.makeText(this,obj,Toast.LENGTH_LONG).show();

                    /*
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String search = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + obj;
                                URL upcite = new URL(search);
                                HttpsURLConnection conn = (HttpsURLConnection) upcite.openConnection();
                                if (conn.getResponseCode() == 200) {
                                    InputStream response = conn.getInputStream();
                                    int size = response.available();
                                    byte[] buffer = new byte[size];
                                    response.read(buffer);
                                    response.close();
                                    resJson = new String(buffer, "UTF-8");
                                    Toast.makeText(mContext, resJson, Toast.LENGTH_LONG).show();

                                }
                                else {
                                    System.out.println(search + " failed!");
                                }
                                //https://api.upcitemdb.com/prod/trial/lookup?upc=3232


                                System.out.println("PRINTING RESULTS");
                                //System.out.println(products);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
                    */



                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                }



            }
        }
    }

    public class LookupThread extends AsyncTask<String,Void,String> {

        String result;
        protected String doInBackground(String... params) {
            try {

                    //String send = "http://146.95.77.23:8081/search?upc="; //Sends to server

                upc = params[0];
                String search = "https://api.upcitemdb.com/prod/trial/lookup?upc="; //Locally searches
                search += params[0];

                System.out.println(search);

                URL upcite = new URL(search);
                HttpsURLConnection conn = (HttpsURLConnection) upcite.openConnection();

                if (conn.getResponseCode() == 200) {
                    InputStream response = new BufferedInputStream(conn.getInputStream());
                    result = readStream(response);
                    response.close();
                    System.out.println("Results are " + result);

                }
                else {
                    System.out.println(search + " failed!");
                }
                //https://api.upcitemdb.com/prod/trial/lookup?upc=3232


                //System.out.println(products);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "hi";
        }
        protected void onPostExecute(String res) {
            System.out.println("BLAH BLAH POSTEXECUTE");
            Toast.makeText(MainActivity.this, result + " is result.", Toast.LENGTH_SHORT).show();
            System.out.println("BLAH BLAH POSTEXECUTE Fonished");
            //REsults placeo

            //0ean/upc
            //1title
            //2description
            //3isbn/upc
            //4publisher
            //5images
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray items = new JSONArray(obj.getString("items"));
                //String blah = obj.getJSONObject("items").getJSONArra;
                Intent intent = new Intent(mContext,ResultActivity.class);
                if (!items.getJSONObject(0).getString("title").isEmpty()) {
                    intent.putExtra("title", items.getJSONObject(0).getString("title"));
                }
                //intent.putExtra("desc",items.getJSONObject(0).getString("isbn"));
                intent.putExtra("upc",upc);
                System.out.println((new JSONArray(items.getJSONObject(0).getString("images"))).get(0) + " is first image");
                intent.putExtra("prodPic", ((String) (new JSONArray(items.getJSONObject(0).getString("images"))).get(0)));


                startActivity(intent);

                //System.out.println(items.getJSONObject(0).getString("title"));
                //System.out.println(items.getJSONObject(0).getString("isbn"));
                //System.out.println(items.getJSONObject(0).getString("images"));

                //System.out.println(items.getString("isbn"));
                //System.out.println(items.getString("images"));
            } catch(Exception e) {
                System.out.println("An exception with json.");
                e.printStackTrace();
            }







        }

    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
    /*New Code stops here*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
