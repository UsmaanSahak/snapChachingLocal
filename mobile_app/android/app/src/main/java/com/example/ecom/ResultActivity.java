package com.example.ecom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.transform.Result;

public class ResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);

        final Intent intent = getIntent();
        ((TextView) findViewById(R.id.titleView)).setText(intent.getStringExtra("title"));
        ((TextView) findViewById(R.id.descView)).setText(intent.getStringExtra("desc"));
        if (intent.getStringExtra("prodPic").equals("Not Found")) {
            System.out.println("Image Not Available");
        } else {
            Picasso.get().load(intent.getStringExtra("prodPic")).into(((ImageView) findViewById(R.id.prodPicView)));
        }

        final EditText enterHours = (EditText) findViewById(R.id.enterHours);
        final EditText enterMinutes = (EditText) findViewById(R.id.enterMinutes);
        final EditText enterDollars = (EditText) findViewById(R.id.enterDollars);
        final EditText enterCents = (EditText) findViewById(R.id.enterCents);

        ((Button) findViewById(R.id.submitProduct)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enterHours.getText().toString().isEmpty()) { return; }//Later set separate responses
                if (enterMinutes.getText().toString().isEmpty()) { return; }
                if (enterDollars.getText().toString().isEmpty()) { return; }
                if (enterCents.getText().toString().isEmpty()) { return; }

                int hours = Integer.parseInt(enterHours.getText().toString());
                final int minutes = Integer.parseInt(enterMinutes.getText().toString()) + (hours * 60);

                int dollars = Integer.parseInt(enterDollars.getText().toString());
                int cents = Integer.parseInt(enterCents.getText().toString());
                if (minutes < 45 || minutes > 120) {
                    Toast.makeText(ResultActivity.this,"Must be between 45 minutes to 2 hours long.",Toast.LENGTH_SHORT).show();
                } else if (cents > 100 || dollars > 1000) {
                    Toast.makeText(ResultActivity.this, "Cents must be less than 100, dollars less than 1000.",Toast.LENGTH_SHORT).show();
                }else {

                        ///*
                        new AlertDialog.Builder(ResultActivity.this)
                                .setTitle("Confirm?")
                                .setMessage("Do you want to post this item?")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Submitto the posto
                                        //Create json with title, upc, time (minutes), and cost.
                                        JSONObject prod = new JSONObject();
                                        try {
                                            prod.put("title",intent.getStringExtra("title"));
                                            prod.put("description","");
                                            prod.put("brand","");
                                            prod.put("color","");
                                            prod.put("size","");
                                            if (intent.getStringExtra("prodPic").equals("Not Found")) {
                                                prod.put("img","");
                                            } else {
                                                JSONArray arr = new JSONArray().put(intent.getStringExtra("prodPic"));
                                                prod.put("img",arr);
                                            }
                                            prod.put("weight","");
                                            prod.put("competitor","");
                                            prod.put("seller","");
                                            prod.put("upc",intent.getStringExtra("upc"));
                                            prod.put("time",minutes);
                                            prod.put("price_paid",enterDollars.getText().toString() + "." + enterCents.getText().toString());
                                            prod.put("quantity","");
                                            postThread p = new postThread();
                                            p.execute(prod);

                                            System.out.println(prod);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(ResultActivity.this, "Submitted!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ResultActivity.this,MainActivity.class));
                                    }
                                })
                                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(ResultActivity.this,MainActivity.class));
                                    }
                                }).show();
                        //*/
                        //startActivity(new Intent(ResultActivity.this,MainActivity.class));
                }
            }
        });
    }
    public class postThread extends AsyncTask<JSONObject,Void,String> {

        String result;
        protected String doInBackground(JSONObject... params) {
            try {
                //String send = "http://54.198.165.68:3002/get_info?prod=";
                String send = "http://146.95.79.164:8081/search?upc=";
                send += params[0];
                System.out.println(send);
                URL snapServer = new URL(send);
                HttpURLConnection connSend = (HttpURLConnection) snapServer.openConnection();
                connSend.setConnectTimeout(1000);
                if (connSend.getResponseCode() == 200) {
                    System.out.println("Successfully sent!");
                } else {
                    System.out.println("Could not send?");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            return "hi";
        }
        protected void onPostExecute(String res) {
        }

    }
}
