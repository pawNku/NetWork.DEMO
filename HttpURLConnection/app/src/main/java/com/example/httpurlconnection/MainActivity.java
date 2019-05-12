package com.example.httpurlconnection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mResponseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendRequest = (Button) findViewById(R.id.send_request);
        mResponseText = (TextView) findViewById(R.id.response_text);

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if()
                    sendRequestWithHttpURLConnection();


            }
        });
    }

    //0.开启线程 1.1new URL 然后open出connection连接 1.2各种自由set
    //2.为读取：2.1输入流 和2.2缓存Buffer(老二老三补上) 3.存放的sb开始high
    private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {


            @Override
            public void run() {
                 BufferedReader mReader = null;
                //这里应该是HttpURLConnection
                //private URLConnection mConnection;
                 HttpURLConnection mConnection = null;
                try {
                    //1.
                    URL url = new URL("http://www.baidu.com");
                    mConnection = (HttpURLConnection) url.openConnection();
                    mConnection.setRequestMethod("GET");
                    mConnection.setConnectTimeout(8000);
                    mConnection.setReadTimeout(8000);
                    //2.
                    InputStream inputStream = mConnection.getInputStream();
                    //括号外用到所以部分全局了
                    mReader = new BufferedReader(new InputStreamReader(inputStream));
                    //3.
                    StringBuilder SB = new StringBuilder();
                    //String line;
                    while (mReader.readLine() != null){
                        SB.append(mReader.readLine());
                    }
                    showResponse(SB.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(mReader!=null)
                        try {
                            mReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    if(mConnection!=null){
                        mConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mResponseText.setText(s);
            }
        });
    }
}
