package com.danliris.sewingku;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.danliris.sewingku.config.Endpoint;
import com.danliris.sewingku.object.ProcessItem;
import com.danliris.sewingku.object.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//import android.os.AsyncTask;


public class InputJobActivity extends AppCompatActivity {

    EditText npkSession, unitSession, nameSession, lineJob, roJob;
    String processSession;
    TextView date;
    Button processJob, logout, recap;
    Session ses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ses = new Session(getApplicationContext());

        Date now = new Date();
        String d = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(now);

        npkSession = (EditText)findViewById(R.id.npkSession);
        nameSession = (EditText)findViewById(R.id.nameSession);
        unitSession = (EditText)findViewById(R.id.unitSession);
        lineJob = (EditText)findViewById(R.id.lineTxt);
        roJob = (EditText)findViewById(R.id.roTxt);

        processJob = (Button)findViewById(R.id.processBtn);
        logout = (Button)findViewById(R.id.logoutBtn);
        recap = (Button)findViewById(R.id.recapBtn);

        npkSession.setText(ses.getNpk());
        nameSession.setText(String.format("%s %s", ses.getFirstname(), ses.getLastname()));
        unitSession.setText(ses.getUnit());
        processSession = ses.getIdProcess();

        CheckRecap(ses.getIdProcess());

        processJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(lineJob.getText().toString())){
                    lineJob.setError("Nomor Line tidak boleh kosong");
                }
                else if(TextUtils.isEmpty(roJob.getText().toString())){
                    roJob.setError("Nomor RO tidak boleh kosong");
                }
                else {
                    ProcessItem data = new ProcessItem(0, lineJob.getText().toString().toUpperCase(), roJob.getText().toString(), 0, 0,0, Integer.parseInt(processSession));
                    InsertData(data);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ses.DestroySession();
                finish();
            }
        });

        recap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputJobActivity.this, RecapActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    public void InsertData(ProcessItem processItem){

        RequestQueue queue = Volley.newRequestQueue(InputJobActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, Endpoint.CREATE_PROCESS_ENDPOINT_BASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("error").equals("false")){
                        Intent i = new Intent(InputJobActivity.this, OnProcessActivity.class);
                        i.putExtra("id_proses_item", jsonObject.getString("id"));
                        startActivity(i);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                roJob.setText("");
                lineJob.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(InputJobActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our
                // key and value pair to our parameters.
                params.put("line", processItem.getLine());
                params.put("ro", processItem.getRo());
                params.put("idproses", String.valueOf(processItem.getId_proses()));

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    public void CheckRecap(String id){

        RequestQueue queue = Volley.newRequestQueue(InputJobActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, Endpoint.CHECK_PROGRESS_BY_ID_ENDPOINT_BASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("error").equals("false")){
                        recap.setEnabled(true);
                    } else {
                        recap.setEnabled(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                roJob.setText("");
                lineJob.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(InputJobActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our
                // key and value pair to our parameters.
                params.put("id", id);
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}