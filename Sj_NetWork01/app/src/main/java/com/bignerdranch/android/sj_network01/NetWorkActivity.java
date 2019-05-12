package com.bignerdranch.android.sj_network01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class NetWorkActivity extends AppCompatActivity {

    private static final String TAG = "NetWorkActivity";
    private TextView mTextView;
    private Button mBtnGet;
    private Button mBtnparse;
    private String mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_work);

        initView();
        initData();

    }

    private void initData() {
        mBtnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        requestDataByGet();
                    }
                }).start();

            }
        });

        mBtnparse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 传入mResult解析Json
                 */
                parseJsonData(mResult);
            }
        });
    }

    private void parseJsonData(String result) {
        /**
         * jsonObject是总的传入result的 包括status和List datas
         * .getInt("status") 是根据网页来的
         */
        try {
            Bean res = new Bean();
            JSONObject jsonObject = new JSONObject(result);

            List<Bean.data> dataList = new ArrayList<>();

            int status = jsonObject.getInt("status");
            JSONArray datas = jsonObject.getJSONArray("data");

            res.setStatus(status);

            if(datas !=null && datas.length() > 0){
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject cur = (JSONObject) datas.get(i);

                    int id = cur.getInt("id");
                    int learner = cur.getInt("learner");
                    String name = cur.getString("name");
                    String smallPic = cur.getString("picSmall");
                    String bigPic = cur.getString("picBig");
                    String description = cur.getString("description");

                    Bean.data item = new Bean.data();
                    item.setID(id);
                    item.setName(name);
                    item.setLearnerNumber(learner);
                    item.setSmallPictureUrl(smallPic);
                    item.setBigPictureUrl(bigPic);
                    item.setDescription(description);
                }
                res.setDatas(dataList);
            }

            mTextView.setText(res.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void requestDataByGet() {
        try {
            URL url = new URL("http://www.imooc.com/api/teacher?type=2&page=1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30*1000);
            conn.setRequestMethod("GET");
            /**
             * 告诉服务器Client设置的各种属性
             * key-value Charset 编码格式
             */
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Charset","UTF-8");
            conn.setRequestProperty("Accept-Charset","UTF-8");
            conn.connect();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                /**
                 * 获取相对于Client内存的输入流开始写入内存
                 * streamToString 自定义的把流变成字符串的res byte[]逐行读取
                 */
                InputStream inputStream = conn.getInputStream();
                mResult = streamToString(inputStream);


                /**
                 * 不在IO里做UI操作
                 * decode就是把字符串搞成UTF-8的编码格式
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResult = decode(mResult);
                        Log.e(TAG, "run: 结果是"+ mResult);
                        mTextView.setText(mResult);
                    }
                });
            } else {
                /**
                 * 获取失败
                 */
                Log.e(TAG, "requestDataByGet: " + responseCode + responseMessage);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void requestDataByPost() {
        try {
            /**
             * Post里的URL后面不会明文的挂上?type=2&page=1 类似于账号密码的东西
             */
            URL url = new URL("http://www.imooc.com/api/teacher");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30*1000);
            conn.setRequestMethod("POST");
            /**
             * 告诉服务器Client设置的各种属性
             * key-value Charset 编码格式
             */
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Charset","UTF-8");
            conn.setRequestProperty("Accept-Charset","UTF-8");

            /**
             * 因为Post是往服务器写数据 所以需要
             * httpUrlConnection.setDoOutput(true);以后就可以使用conn.getOutputStream().write()
             */
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setUseCaches(false);
            conn.connect();

            /**
             * 网页提交字符串 URLEncoder.encode
             * outputStream 内存提交给服务端
             */
            String data = "username=" + URLEncoder.encode("imooc") + "&number=" +URLEncoder.encode("150088886666");
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            if(responseCode == HttpURLConnection.HTTP_OK){
                /**
                 * 获取相对于Client内存的输入流开始写入内存
                 * streamToString 自定义的把流变成字符串的res byte[]逐行读取
                 */
                InputStream inputStream = conn.getInputStream();
                mResult = streamToString(inputStream);

                /**
                 * 不在IO里做UI操作
                 * decode就是把字符串搞成UTF-8的编码格式
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResult = decode(mResult);
                        mTextView.setText(mResult);
                    }
                });
            } else {
                /**
                 * 获取失败
                 */
                Log.e(TAG, "requestDataByGet: " + responseCode + responseMessage);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        mTextView = findViewById(R.id.textView);
        mBtnGet = findViewById(R.id.btn_get);
        mBtnparse = findViewById(R.id.btn_parse);
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return 字符串
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    /**
     * 将Unicode字符转换为UTF-8类型字符串
     */
    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuilder retBuf = new StringBuilder();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5)
                        && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr
                        .charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else {
                    retBuf.append(unicodeStr.charAt(i));
                }
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }
}
