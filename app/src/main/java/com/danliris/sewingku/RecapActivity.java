package com.danliris.sewingku;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.danliris.sewingku.config.Endpoint;
import com.danliris.sewingku.object.ProcessItem;
import com.danliris.sewingku.object.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RecapActivity extends AppCompatActivity {

    TableLayout tl;
    TableRow tr;
    String dataList;
    ScrollView mainLayout;
    ProgressDialog progressBar;
    TextView npkSession, nameSession, unitSession;
    Session ses;
    Button okBtn;
    ArrayList<ProcessItem> data = new ArrayList<ProcessItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);
        //SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userdetails",0);

        ses = new Session(getApplication());

        mainLayout = (ScrollView) findViewById(R.id.scrollView);
        npkSession = (TextView) findViewById(R.id.recNpk);
        nameSession = (TextView) findViewById(R.id.recNama);
        unitSession = (TextView) findViewById(R.id.recUnit);

        tl = (TableLayout) mainLayout.findViewById(R.id.mainTable);
        okBtn = (Button) findViewById(R.id.okRecapBtn);
        tl.setStretchAllColumns(true);

//        npkSession.setText(sharedPreferences.getString("npk","0"));
//        nameSession.setText(sharedPreferences.getString("firstname","0")+" "+sharedPreferences.getString("lastname","0"));
//        unitSession.setText(sharedPreferences.getString("unit","0"));

        npkSession.setText(ses.getNpk());
        nameSession.setText(String.format("%s %s", ses.getFirstname(), ses.getLastname()));
        unitSession.setText(ses.getUnit());

        progressBar = new ProgressDialog(this);

        progressBar.setCancelable(false);
        progressBar.setMessage("Fetching Data..");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();

        //ReadData(sharedPreferences.getString("id_proses","0"));

        ReadData(ses.getIdProcess());

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.dismiss();
                finish();
            }
        });
    }

    private void ReadData(String Id) {

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(RecapActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, Endpoint.READ_RECAP_BY_ID_ENDPOINT_BASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("data") == null) {
                        Toast.makeText(RecapActivity.this, "Please enter valid id.", Toast.LENGTH_SHORT).show();
                    } else {
                        dataList = jsonObject.getString("data").trim();
                        data = parseJSON(dataList);
                        addHeader();
                        addData(data);
                        progressBar.hide();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(RecapActivity.this, "Fail to get course" + error, Toast.LENGTH_SHORT).show();
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

    public ArrayList<ProcessItem> parseJSON(String result) {
        ArrayList<ProcessItem> datas = new ArrayList<ProcessItem>();
        try {
            JSONArray jArray = new JSONArray(result);
            Log.e("log_tag", jArray.toString());
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                ProcessItem item = new ProcessItem(json_data.getInt("id"), json_data.getString("line_produksi"), json_data.getString("nomor_ro"), json_data.getInt("barang_ok"), json_data.getInt("barang_bs"), json_data.getInt("total_komponen"), json_data.getInt("idproses"));
                datas.add(item);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return datas;
    }

    private void addHeader(){
        /** Create a TableRow dynamically **/
        tr = new TableRow(this);

        /** Creating a TextView to add to the row **/
        TextView line = new TextView(this);
        line.setText("LINE");
        line.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        line.setPadding(5, 5, 5, 5);
        line.setBackgroundColor(Color.GRAY);
        line.setTextColor(Color.WHITE);

        LinearLayout Ll = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        //Ll.setPadding(10, 5, 5, 5);
        Ll.addView(line,params);
        tr.addView((View)Ll); // Adding textView to tablerow.

        /** Creating a TextView to add to the row **/
        TextView ro = new TextView(this);
        ro.setText("NO RO");
        ro.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        ro.setPadding(5, 5, 5, 5);
        ro.setBackgroundColor(Color.GRAY);
        ro.setTextColor(Color.WHITE);

        Ll = new LinearLayout(this);
        params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        //Ll.setPadding(10, 5, 5, 5);
        Ll.addView(ro,params);
        tr.addView((View)Ll); // Adding textView to tablerow.

        /** Creating a TextView to add to the row **/
        TextView ok = new TextView(this);
        ok.setText("BARANG OK");
        ok.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        ok.setPadding(5, 5, 5, 5);
        ok.setBackgroundColor(Color.GRAY);
        ok.setTextColor(Color.WHITE);

        Ll = new LinearLayout(this);
        params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        //Ll.setPadding(10, 5, 5, 5);
        Ll.addView(ok,params);
        tr.addView((View)Ll); // Adding textView to tablerow.

        /** Creating a TextView to add to the row **/
        TextView bs = new TextView(this);
        bs.setText("BARANG REJECT");
        bs.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        bs.setPadding(5, 5, 5, 5);
        bs.setBackgroundColor(Color.GRAY);
        bs.setTextColor(Color.WHITE);

        Ll = new LinearLayout(this);
        params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        //Ll.setPadding(10, 5, 5, 5);
        Ll.addView(bs,params);
        tr.addView((View)Ll); // Adding textView to tablerow.

        /** Creating a TextView to add to the row **/
        TextView tk = new TextView(this);
        tk.setText("TOTAL KOMPONEN");
        tk.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tk.setPadding(5, 5, 5, 5);
        tk.setBackgroundColor(Color.GRAY);
        tk.setTextColor(Color.WHITE);

        Ll = new LinearLayout(this);
        params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        //Ll.setPadding(10, 5, 5, 5);
        Ll.addView(tk,params);
        tr.addView((View)Ll); // Adding textView to tablerow.

        tl.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
    }
    @SuppressWarnings({ "rawtypes" })
    public void addData(ArrayList<ProcessItem> datas) {

        for (Iterator i = datas.iterator(); i.hasNext();) {

            ProcessItem p = (ProcessItem) i.next();

            /** Create a TableRow dynamically **/
            tr = new TableRow(this);

            /** Creating a TextView to add to the row **/
            TextView line = new TextView(this);
            line.setText(p.getLine());
            line.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            line.setPadding(5, 5, 5, 5);
            line.setBackgroundColor(Color.WHITE);

            LinearLayout Ll = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 2, 2, 2);
            //Ll.setPadding(10, 5, 5, 5);
            Ll.addView(line,params);
            tr.addView((View)Ll); // Adding textView to tablerow.

            /** Creating a TextView to add to the row **/
            TextView ro = new TextView(this);
            ro.setText(p.getRo());
            ro.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            ro.setSingleLine(false);
            ro.setMinLines(2);
            ro.setMaxLines(4);
            ro.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            ro.setPadding(5, 5, 5, 5);
            ro.setBackgroundColor(Color.WHITE);

            Ll = new LinearLayout(this);
            params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 2, 2, 2);
            //Ll.setPadding(10, 5, 5, 5);
            Ll.addView(ro,params);
            tr.addView((View)Ll); // Adding textView to tablerow.

            /** Creating a TextView to add to the row **/
            TextView ok = new TextView(this);
            ok.setText(String.valueOf(p.getBarang_ok()));
            //ok.setId(p.getId());
            ok.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            ok.setPadding(5, 5, 5, 5);
            ok.setBackgroundColor(Color.WHITE);
            Ll = new LinearLayout(this);
            params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 2, 2, 2);
            //Ll.setPadding(10, 5, 5, 5);
            Ll.addView(ok,params);
            tr.addView((View)Ll); // Adding textView to tablerow.

            /** Creating a TextView to add to the row **/
            TextView bs = new TextView(this);
            bs.setText(String.valueOf(p.getBarang_bs()));
            //bs.setId(p.getId());
            bs.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            bs.setPadding(5, 5, 5, 5);
            bs.setBackgroundColor(Color.WHITE);

            Ll = new LinearLayout(this);
            params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 2, 2, 2);
            //Ll.setPadding(10, 5, 5, 5);
            Ll.addView(bs,params);
            tr.addView((View)Ll); // Adding textView to tablerow.

            /** Creating a TextView to add to the row **/
            TextView tk = new TextView(this);
            tk.setText(String.valueOf(p.getTotal_komponen()));
            //tk.setId(p.getId());
            tk.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tk.setPadding(5, 5, 5, 5);
            tk.setBackgroundColor(Color.WHITE);

            Ll = new LinearLayout(this);
            params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 2, 2, 2);
            //Ll.setPadding(10, 5, 5, 5);
            Ll.addView(tk,params);
            tr.addView((View)Ll); // Adding textView to tablerow.

//            /** Creating Qty Button **/
//            TextView opo = new TextView(this);
//            opo.setText(p.getRo());
//            opo.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                    TableRow.LayoutParams.WRAP_CONTENT));
//            opo.setPadding(5, 5, 5, 5);
//            opo.setBackgroundColor(Color.GRAY);
//            Ll = new LinearLayout(this);
//            params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
//                    TableRow.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, 2, 2, 2);
//            //Ll.setPadding(10, 5, 5, 5);
//            Ll.addView(opo,params);
//            tr.addView((View)Ll); // Adding textview to tablerow.

            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    public void onBackPressed() {

    }
}