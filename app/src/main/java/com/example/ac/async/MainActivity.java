package com.example.ac.async;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private TextView mTextView;
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView)findViewById(R.id.textView);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getData();
            }
        });

    }
    public void getData(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if((mNetworkInfo != null) && (mNetworkInfo.isConnected())){
            new DownloadPage().execute();
        }else{
            mTextView.setText("NO NETWORK AVAILABLE");
        }
    }

    private class DownloadPage extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            try {
                return downloadUrl();
            } catch (IOException e) {
                return "Unable to retrieve web page";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            mTextView.setText(result);
        }

    }

    private String downloadUrl() throws IOException {
        InputStream is = null;
        int len = 500;

        try {
            URL url = new URL("https://www.iiitd.ac.in/about");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();


            String contentAsString = readIt(is, len);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            
            char[] buffer = new char[len];
            reader.read(buffer);
            System.out.print(new String(buffer));
            //if(len == -1){
                int data = reader.read();
                while(data != -1) {
                    System.out.print((char) data);
                    data = reader.read();
            //    }
            }
            
            
            return new String(buffer);
        }

}
