package id.co.edi_indonesia.krjb.signaturepad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import id.co.edi_indonesia.krjb.signaturepad.adapter.PerusahaanAdapter;
import id.co.edi_indonesia.krjb.signaturepad.model.Perusahaan;

public class MainActivity extends AppCompatActivity implements PerusahaanAdapter.IPerusahaaAdapter {
    public static final String PERUSAHAAN = "perusahaan";

    private static final String URL_DATA = "http://192.168.11.137/edii/get_data.php";
    public static final String ARRAY = "activity";
    private static final String TAG_ID= "id";
    private static final String TAG_NAMA= "nama_perusahaan";
    private static final String TAG_KELUHAN= "detail_permasalahan";

//    private static final String URL_DATA = "https://api.themoviedb.org/3/movie/now_playing?api_key=cc2b705c11164d940874ff87f19e62f4&language=en-US&page=1";
//    public static final String ARRAY = "results";
//    private static final String TAG_NAMA= "title";
//    private static final String TAG_KELUHAN= "overview";

//    ArrayList<HashMap<String, String>> productsList;
//    HashMap<String, String> map;
//
//
//    ArrayList<String> listNama = new ArrayList<>();
//    ArrayAdapter<String> adapter;

    ArrayList<Perusahaan> mList = new ArrayList<>();
    PerusahaanAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PerusahaanAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

//        loadRecyclerViewData();

        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mList.clear();
        loadRecyclerViewData();
        mAdapter.notifyDataSetChanged();
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
                                String nmUsaha = c.getString(TAG_NAMA);
                                String keluhan = c.getString(TAG_KELUHAN);

                                mList.add(new Perusahaan(id, nmUsaha, keluhan));
                                mAdapter.notifyDataSetChanged();
                            }

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

    @Override
    public void doClick(String pos) {
        Intent intent = new Intent(this, PetugasActivity.class);
        intent.putExtra(PERUSAHAAN, pos);
        startActivity(intent);
    }
}