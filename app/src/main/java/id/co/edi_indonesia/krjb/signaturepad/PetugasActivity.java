package id.co.edi_indonesia.krjb.signaturepad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import id.co.edi_indonesia.krjb.signaturepad.handler.RequestHandler;

public class PetugasActivity extends AppCompatActivity {

    public static final String UPLOAD_URL = "http://192.168.11.9/admin-activity-baru/petugas_upload.php";
    public static final String TAG_ID = "id";
    public static final String TAG_PETUGAS = "ttd_petugas";
//    public static final String TAG_NAMA = "nama_perusahaan";


    private SignaturePad mSignaturePad;
    private Button ClearButton;
    private Button SaveButton;

    private Bitmap bitmap;

    private Uri filePath;

    String id;
//    String nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(PetugasActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                SaveButton.setEnabled(true);
                ClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                SaveButton.setEnabled(false);
                ClearButton.setEnabled(false);
            }
        });

        ClearButton = (Button) findViewById(R.id.btnclear);
        SaveButton = (Button) findViewById(R.id.btnsave);
        id = getIntent().getStringExtra(MainActivity.PERUSAHAAN);

        ClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = mSignaturePad.getSignatureBitmap();

                Log.i("BITMAPNYA", bitmap.toString());
                uploadImage();
            }
        });


    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PetugasActivity.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.i("DI POST", "IYA");
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(TAG_ID, id);
//                data.put(TAG_NAMA, nama);
                data.put(TAG_PETUGAS, uploadImage);

                Log.i("UPLOADIMAGE", uploadImage);
                String result = rh.sendPostRequest(UPLOAD_URL,data);
//                startActivity(new Intent(PetugasActivity.this, CustomerActivity.class));

                Intent i = new Intent(PetugasActivity.this, CustomerActivity.class);
                i.putExtra("TAG_ID", id);
                startActivity(i);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}