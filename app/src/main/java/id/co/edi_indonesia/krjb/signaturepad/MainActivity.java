package id.co.edi_indonesia.krjb.signaturepad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String URL_DATA = "http://192.168.11.137/edii/activity-report-edii/androidsql/get_data.php";
    public static final String ARRAY = "activity";
    private static final String TAG_ID = "id";
    private static final String TAG_NAMA = "nama_perusahaan";

    ArrayList<HashMap<String, String>> productsList;
    HashMap<String, String> map;

    Spinner spinner;

    String selected;

    ArrayList<String> listNama = new ArrayList<>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnadmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PetugasActivity.class));
            }
        });

        findViewById(R.id.btncs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CustomerActivity.class));
            }
        });

        productsList = new ArrayList<HashMap<String, String>>();

        spinner = (Spinner) findViewById(R.id.spinnerNama);

        loadRecyclerViewData();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listNama);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray(ARRAY);

                            Log.i("ARRAY1", array.toString());


                            for(int i = 0; i < array.length(); i++) {
                                JSONObject c = array.getJSONObject(i);

                                String id = c.getString(TAG_ID);
                                String nama_perusahaan = c.getString(TAG_NAMA);

                                listNama.add(nama_perusahaan);
                                Log.i("ISI LISTITEM", listNama.toString());
                                map = new HashMap<String, String>();

                                map.put(TAG_ID, id);
                                map.put(TAG_NAMA, nama_perusahaan);

                                Log.i("ARRAY2", listNama.toString());
                            }
                            spinner.setAdapter(adapter);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplication(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.i("ERRORNYA", volleyError.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
