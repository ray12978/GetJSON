package com.ray.getjson;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity {

    private String szJson = "{ \"weatherinfo\":{\"city\":\"北京\", \"cityid\" : \"101010100\", " +
            "\"temp\" : \"18\", \"WD\" : \"東南風\", \"WS\" : \"1級\", \"SD\" : \"17 % \", " +
            "\"WSE\" : \"1\", \"time\" : \"17:05\", \"isRadar\" : \"1\", \"Radar\"  : \"JC_RADAR_AZ9010_JB\"," +
            " \"njd\" : \"暫無實況\", \"qy\" : \"1011\", \"rain\" : \"0\"} }";

    private String szJsonAry = "{\n" +
            "\"games\" :\n" +
            "        [\n" +
            "        {\"url\" :\"http:://www.qq.com\",\"name\" :\"jyjh\",\"server\" :\"S123\"},\n" +
            "        {\"url\" :\"http:://www.yy.com\",\"name\" :\"sjhz\",\"server\" :\"S456\"},\n" +
            "        {\"url\" :\"http:://www.sina.com\",\"name\" :\"ttxy\",\"server\" :\"S500\"}\n" +
            "        ]\n" +
            "}";

    private TextView mTextViewJson;
    private TextView mTextViewJsonAry;
    private TextView mTextViewWriteJson;
    private TextView mTextViewReadJson;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewJson = (TextView) findViewById(R.id.tv_json);
        mTextViewJsonAry = (TextView) findViewById(R.id.tv_jsonary);
        mTextViewWriteJson = (TextView)findViewById(R.id.tv_writejson);
        mTextViewReadJson = (TextView)findViewById(R.id.tv_readjson);

        mFile = new File(getFilesDir(),"games.json");//獲取到應用在內部的私有資料夾下對應的games.json檔案
        ((TextView)findViewById(R.id.tv_file)).setText("getFilesDir() : " + getFilesDir().toString());

        try {
            JSONObject root = new JSONObject(szJson);
            JSONObject weatherinfo = root.getJSONObject("weatherinfo");
            String city = weatherinfo.optString("city");
            String cityid = weatherinfo.optString("cityid");
            String WD = weatherinfo.optString("WD");
            String WS = weatherinfo.optString("WS");
            String SD = weatherinfo.optString("SD");
            String WSE = weatherinfo.optString("WSE");
            String time = weatherinfo.optString("time");

            //執行效率：StringBuilder > StringBuffer > String
            StringBuilder _builder = new StringBuilder()
                    .append("city:" + city)
                    .append("\ncityid:" + cityid)
                    .append("\nWD:" + WD)
                    .append("\nWS:" + WS)
                    .append("\nSD:" + SD)
                    .append("\nWSE:" + WSE)
                    .append("\ntime:" + time);
            String strTemp = _builder.toString();
            mTextViewJson.setText(strTemp);

            Log.i("@@@ weatherinfo", strTemp);
            System.out.printf("@@@ weatherinfo : %s", strTemp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ///////////////////////////////////////////////////////////////////////////////////////
        try {
            JSONObject root2 = new JSONObject(szJsonAry);
            JSONArray ary = root2.getJSONArray("games");

            StringBuilder _builder2 = new StringBuilder();
            Integer len = ary.length();//陣列大小
            for (Integer i=0; i<len; i++){
                JSONObject _obj = ary.getJSONObject(i);
                String url = _obj.optString("url");
                String name = _obj.optString("name");
                String server = _obj.optString("server");
                _builder2.append("\nurl : " + url)
                        .append("\nname : " + name)
                        .append("\nserver : " + server);
            }
            String strTemp2 = _builder2.toString();
            mTextViewJsonAry.setText(strTemp2);

            Log.i("@@@ games", strTemp2);
            System.out.printf("@@@ games : %s", strTemp2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ///////////////////////////////////////////////////////////////////////////////////////
        try{
            JSONObject root3 = new JSONObject();//例項一個JSONObject物件
            root3.put("updatetime","20180325");//對其新增一個數據

            JSONArray games = new JSONArray();//例項一個JSON陣列
            JSONObject game1 = new JSONObject();//例項一個game1的JSON物件
            game1.put("url","https://www.baidu.com");//對game1物件新增資料
            game1.put("name","csgogogo");
            game1.put("server","s100");

            JSONObject game2 = new JSONObject();//例項一個game2的JSON物件
            game2.put("url","https://www.aliyun.com");//對game2物件新增資料
            game2.put("name","thisiskandy");
            game2.put("server","s200");

            games.put(0, game1);//將game1物件新增到JSON陣列中去，角標為0
            games.put(1, game2);//將game2物件新增到JSON陣列中去，角標為1

            root3.put("games", games);//然後將JSON陣列新增到名為root的JSON物件中去
            mTextViewWriteJson.setText(root3.toString());

            FileOutputStream fos = new FileOutputStream(mFile);//建立一個檔案輸出流
            fos.write(root3.toString().getBytes());//寫入JSON資料
            fos.close();//關閉輸出流
        }catch (JSONException e) {
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ///////////////////////////////////////////////////////////////////////////////////////
        try {
            FileInputStream fis = new FileInputStream(mFile);//獲取一個檔案輸入流
            InputStreamReader isr = new InputStreamReader(fis);//讀取檔案內容
            BufferedReader bf = new BufferedReader(isr);//將字元流放入快取中
            String line;//定義一個用來臨時儲存資料的變數
            StringBuilder sb = new StringBuilder();//例項化一個字串序列化
            while((line = bf.readLine()) != null){
                sb.append(line);//將資料新增到字串序列化中
            }
            //關閉流
            fis.close();
            isr.close();
            bf.close();

            JSONObject root = new JSONObject(sb.toString());//用JSONObject進行解析
            String updatetime = root.getString("updatetime");//獲取字串型別的鍵值對
            JSONArray array = root.getJSONArray("games");//獲取JSON資料中的陣列資料

            StringBuilder _builder = new StringBuilder().append("updatetime : " + updatetime);
            for (int i=0; i<array.length(); i++){
                JSONObject object = array.getJSONObject(i);//遍歷得到陣列中的各個物件
                String url = object.getString("url");//獲取第一個值
                String name = object.getString("name");//獲取第二個值
                String server = object.getString("server");//獲取第三個值

                _builder.append("\nurl : " + url)
                        .append("\nname : " + name)
                        .append("\nserver : " + server);
            }
            mTextViewReadJson.setText(_builder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}