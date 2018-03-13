package com.example.jerryzhou.drysisters.net;

import android.util.Log;

import com.example.jerryzhou.drysisters.entity.Sister;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author admin
 *
 * 网络请求处理相关类
 */
public class SisterApi {
    private static final String TAG = "Network";
    private static final String BASE_URL = "https://gank.io/api/data/福利/";

    /**
     * 读取流中数据的方法
     * @param inputStream 输入流
     * @return outputStream.toByteArray()
     * @throws Exception 异常
     */
    public byte[] readFromStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        while ((len = inputStream.read(bytes)) != -1){
            outputStream.write(bytes,0,len);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * 解析返回的json数据的方法
     * @param conent 传入的参
     * @return sisterArrayList
     * @throws JSONException
     */
    public ArrayList<Sister> parseSister(String conent) throws JSONException {
        ArrayList<Sister> sisterArrayList = new ArrayList<>();
        JSONObject object = new JSONObject(conent);
        JSONArray array = object.getJSONArray("results");
        for(int i = 0;i < array.length();i++){
            JSONObject results = (JSONObject) array.get(i);
            Sister sister = new Sister();
            sister.setId(results.getString("_id"));
            sister.setCreateAt(results.getString("createdAt"));
            sister.setDesc(results.getString("desc"));
            sister.setPublishedAt(results.getString("publishedAt"));
            sister.setSource(results.getString("source"));
            sister.setType(results.getString("type"));
            sister.setUrl(results.getString("url"));
            sister.setUsed(results.getBoolean("used"));
            sister.setWho(results.getString("who"));
            sisterArrayList.add(sister);
        }
        return sisterArrayList;
    }

    /**
     * 查询妹子的方法
     * @param count 每页显示个数
     * @param page 第几页
     * @return sisters 解析完毕的数据集
     */
    public ArrayList<Sister> QuerySister(int count,int page){
        String queryUrl = BASE_URL + count + "/" + page;
        ArrayList<Sister> sisters = new ArrayList<>();
        try {
            URL url = new URL(queryUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            int successResponse = 200;
            Log.v(TAG,"Server response:" + code);
            if(code == successResponse){
                InputStream in = conn.getInputStream();
                byte[] data = readFromStream(in);
                String result = new String(data,"UTF-8");
                sisters = parseSister(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sisters;
    }
}
