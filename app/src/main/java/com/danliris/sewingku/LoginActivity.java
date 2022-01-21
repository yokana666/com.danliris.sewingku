package com.danliris.sewingku;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.danliris.sewingku.config.Endpoint;
import com.danliris.sewingku.object.Identity;
import com.danliris.sewingku.object.Session;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public EditText npkTxt;
    public Button loginBtn;
    public ProgressBar progressBar;
    ConstraintLayout mainparent;
    Identity identity;
    String npk;
    Session ses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ses = new Session(getApplicationContext());
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        npkTxt = (EditText)findViewById(R.id.npkTxt);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        mainparent = (ConstraintLayout)findViewById(R.id.main_parent);

        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.GONE);

        CheckSession();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                npk = npkTxt.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                loginBtn.setVisibility(View.GONE);

                Login(npk);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckSession();
    }

    public void Login(String npk){
    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
    StringRequest request = new StringRequest(Request.Method.POST, Endpoint.AUTH_PROCESS_ENDPOINT_BASE_URL, new com.android.volley.Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e("TAG", "RESPONSE IS " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                // on below line we are displaying a success toast message.
                if(jsonObject.getString("error").equals("false")){
                    JSONObject data = new JSONObject(jsonObject.getString("data"));
                    identity = new Identity(data.getString("npk"),data.getString("firstname"), data.getString("lastname"), data.getString("unit"));

                    progressBar.setVisibility(View.GONE);
                    loginBtn.setVisibility(View.VISIBLE);

                    ses = new Session(getApplicationContext());
                    ses.AddSession(identity.getNpk(),identity.getFirstname(),identity.getLastname(),identity.getUnit(), data.getString("id"));
//                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userdetails",0);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                    editor.putString("npk", identity.getNpk());
//                    editor.putString("firstname", identity.getFirstname());
//                    editor.putString("lastname", identity.getLastname());
//                    editor.putString("unit", identity.getUnit());
//                    editor.putString("id_proses", data.getString("id"));
//
//                    editor.apply();

                    Intent i = new Intent(LoginActivity.this, InputJobActivity.class);
                    startActivity(i);

                } else {
                    Snackbar.make(mainparent, jsonObject.getString("message"), Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                    progressBar.setVisibility(View.GONE);
                    loginBtn.setVisibility(View.VISIBLE);
                }
                //Toast.makeText(InputJobActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
            }
        }
    }, new com.android.volley.Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // method to handle errors.
            Toast.makeText(LoginActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
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

            Map<String, String> params = new HashMap<String, String>();
            params.put("npk", npk);
            return params;
        }
    };
    queue.add(request);
    }

    public void CheckSession(){
        if(!TextUtils.isEmpty(ses.getNpk())){
            Intent i = new Intent(LoginActivity.this, InputJobActivity.class);
            startActivity(i);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}