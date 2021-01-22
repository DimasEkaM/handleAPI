package com.example.handleapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextView tvShow,tvSembuh,tvPositif,tvMeninggal,tvDirawat;
    private Button btnRefresh;

    private String apiURL ="https://api.kawalcorona.com/indonesia/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        getDataHome();
    }

    private void getDataHome() {
        getHomeAsyntask getHomeAsyntask = new getHomeAsyntask();
        getHomeAsyntask.execute();
    }

    private void initListener() {
    btnRefresh.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getDataHome();
        }
    });
    }

    private void initView() {
        tvShow      = findViewById(R.id.tv_show);
        tvSembuh    = findViewById(R.id.tv_sembuh);
        tvPositif   = findViewById(R.id.tv_positif);
        tvMeninggal = findViewById(R.id.tv_meninggal);
        tvDirawat   = findViewById(R.id.tv_dirawat);
        btnRefresh  = findViewById(R.id.btn_refresh);
    }

    public class getHomeAsyntask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiURL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while(data != -1){
                        current += (char) data;
                        data = isw.read();
                    }
                    return current;

                }catch (Exception e){
                    e.printStackTrace();
                } finally{
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
                return "Exception :"+e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            try{
                JSONArray jsonArray = new JSONArray(s);
                JSONObject object = jsonArray.getJSONObject(0 );
                String showData         = "Nama Negara      : "+object.getString("name");
                String showPositif      = "Jumlah Positif   : "+object.getString("positif");
                String showSembuh       = "Jumlah Sembuh    : "+object.getString("sembuh");
                String showMeninggal    = "Jumlah Meninggal : "+object.getString("meninggal");
                String showDirawat      = "Jumlah Dirawat   : "+object.getString("dirawat");

                tvShow.setText(showData);
                tvPositif.setText(showPositif);
                tvSembuh.setText(showSembuh);
                tvMeninggal.setText(showMeninggal);
                tvDirawat.setText(showDirawat);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}