package com.danliris.sewingku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.danliris.sewingku.config.Endpoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OnProcessActivity extends AppCompatActivity {

    TextView okText, bsText, sumText, roText;
    Button doneButton, okButton, bsButton, cancleButton;
    String id_proses_item, id_proses, ok, bs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Date now = new Date();
        String d = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(now);

        roText = (TextView)findViewById(R.id.roText);

        okText = (TextView)findViewById(R.id.okTxt);
        bsText = (TextView)findViewById(R.id.bsTxt);
        sumText = (TextView)findViewById(R.id.sumTxt);

        okButton = (Button) findViewById(R.id.okBtn);
        bsButton = (Button)findViewById(R.id.bsBtn);
        doneButton = (Button)findViewById(R.id.endProcessBtn);
        cancleButton = (Button)findViewById(R.id.cancelProcessBtn);

        Bundle extra = getIntent().getExtras();
        id_proses_item = extra.getString("id_proses_item");
        Log.e("TAG", "RESPONSE IS " + id_proses_item);
        ReadData(id_proses_item);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ok = String.valueOf(Integer.parseInt(okText.getText().toString()) + 1);
                String type = "ok";
                String total = String.valueOf(Integer.parseInt(sumText.getText().toString()) + 1);
                UpdateData(id_proses_item, type, ok, total);
            }
        });

        bsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bs =  String.valueOf(Integer.parseInt(bsText.getText().toString()) + 1);
                String type = "bs";
                String total = String.valueOf(Integer.parseInt(sumText.getText().toString()) + 1);
                UpdateData(id_proses_item, type, bs, total);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseProcess(id_proses);
            }
        });

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelProcess(id_proses_item);
            }
        });
    }

    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    private void ReadData(String Id) {

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(OnProcessActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, Endpoint.READ_PROCESS_BY_ID_ENDPOINT_BASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are checking if the response is null or not.
                    if (jsonObject.getString("data") == null) {
                        // displaying a toast message if we get error
                        Toast.makeText(OnProcessActivity.this, "Please enter valid id.", Toast.LENGTH_SHORT).show();
                    } else {
                        // if we get the data then we are setting it in our text views in below line.
                        JSONObject data = new JSONObject(jsonObject.getString("data"));
                        id_proses = data.getString("id_proses");
                        roText.setText(data.getString("ro"));
                        okText.setText(data.getString("barang_ok"));
                        bsText.setText(data.getString("barang_bs"));
                        sumText.setText(data.getString("total_komponen"));
                    }
                    // on below line we are displaying
                    // a success toast message.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(OnProcessActivity.this, "Fail to get course" + error, Toast.LENGTH_SHORT).show();
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

                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key and value pair to our parameters.
                params.put("id", Id);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void UpdateData(String Id, String Type, String Counter, String Total) {

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(OnProcessActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, Endpoint.UPDATE_PROCESS_BY_ID_ENDPOINT_BASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("TAG", "RESPONSE IS " + response);
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are checking if the response is null or not.
                    if(jsonObject.getString("error").equals("true")){
                        // displaying a toast message if we get error
                        Toast.makeText(OnProcessActivity.this, "Please enter valid id.", Toast.LENGTH_SHORT).show();
                    } else {
                        // if we get the data then we are setting it in our text views in below line.
                        if (jsonObject.getString("type").equals("ok")){
                            okText.setText(jsonObject.getString("counter"));
                        } else if(jsonObject.getString("type").equals("bs")){
                            bsText.setText(jsonObject.getString("counter"));
                        }
                        sumText.setText(jsonObject.getString("total"));
                    }
                    // on below line we are displaying
                    // a success toast message.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(OnProcessActivity.this, "Fail to get data" + error, Toast.LENGTH_SHORT).show();
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

                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key and value pair to our parameters.
                params.put("id", id_proses_item);
                params.put("counter", Counter);
                params.put("type", Type);
                params.put("total", Total);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void CancelProcess(String Id){
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(OnProcessActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, Endpoint.CANCEL_PROCESS_BY_ID_ENDPOINT_BASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("TAG", "RESPONSE IS " + response);
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are checking if the response is null or not.
                    if(jsonObject.getString("error").equals("true")){
                        // displaying a toast message if we get error
                        Toast.makeText(OnProcessActivity.this, "Please enter valid id.", Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                    }
                    // on below line we are displaying
                    // a success toast message.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(OnProcessActivity.this, "Fail to get data" + error, Toast.LENGTH_SHORT).show();
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

                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key and value pair to our parameters.
                params.put("id", id_proses_item);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private  void CloseProcess(String Id){
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(OnProcessActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, Endpoint.END_PROCESS_BY_ID_ENDPOINT_BASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("TAG", "RESPONSE IS " + response);
                    // on below line passing our response to json object.
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are checking if the response is null or not.
                    if(jsonObject.getString("error").equals("true")){
                        // displaying a toast message if we get error
                        Toast.makeText(OnProcessActivity.this, "Please enter valid id.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(OnProcessActivity.this, InputJobActivity.class);
                        startActivity(i);
                        finish();
                    }
                    // on below line we are displaying
                    // a success toast message.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(OnProcessActivity.this, "Fail to get data" + error, Toast.LENGTH_SHORT).show();
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

                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key and value pair to our parameters.
                params.put("id", Id);
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}