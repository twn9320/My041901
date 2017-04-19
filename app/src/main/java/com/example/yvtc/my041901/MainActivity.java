package com.example.yvtc.my041901;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView)findViewById(R.id.textView);
        MyTask m = new MyTask();
        m.execute("https://uc.udn.com.tw/photo/2017/04/06/1/3364635.jpg");
    }

    class MyTask extends AsyncTask<String,Integer,Bitmap>{


        @Override
        protected Bitmap doInBackground(String... params) {
            InputStream inputStream;
            URL url = null;
            byte[] buffer = new byte[64];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Bitmap bitmap = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                int total = conn.getContentLength();
                int current = 0;
                inputStream = conn.getInputStream();
                int readSize = 0;
                while ((readSize=inputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, readSize);
                    current += readSize;
                    final int c = current;
                    final  int t = total;
                    publishProgress(100*c/t);
                }
                byte[] result = os.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(result,0,result.length);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }  catch (ProtocolException e) {
                e.printStackTrace();
            }  catch (IOException e) {
                e.printStackTrace();
            }


            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("MTA","onPreExcute");
            tv.setText("準備下載");
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView img = (ImageView)findViewById(R.id.imageView);
            img.setImageBitmap(bitmap);
            tv.setText("下載完成");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("MTA","onProgressUpdate:" + values[0]);
            tv.setText("下載中..."+String.valueOf(values[0])+"%");
        }


    }
}
