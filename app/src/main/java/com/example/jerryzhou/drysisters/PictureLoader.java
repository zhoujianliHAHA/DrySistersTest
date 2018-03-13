package com.example.jerryzhou.drysisters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片加载类，用线程将输入流转换成字节数组，
 * 转换完毕后 handler里decodeByteArray将字节数组解码成bitmap
 * @author admin
 */
public class PictureLoader {
    private ImageView loadImg;
    private String imgUrl;
    private byte[] picByte;

    private static int hm = 0x123;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == hm)   {
                Bitmap bitmap = BitmapFactory.decodeByteArray(picByte,0,picByte.length);
                loadImg.setImageBitmap(bitmap);
            }
        }
    };

    public void load(ImageView loadImg,String imgUrl){
        this.loadImg = loadImg;
        this.imgUrl = imgUrl;
        Drawable drawable = loadImg.getDrawable();
        if(drawable != null && drawable instanceof BitmapDrawable){
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if(bitmap != null && !bitmap.isRecycled()){
                bitmap.recycle();
            }
        }
        new Thread(runnable).start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(imgUrl);
                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);
                int responseCode = 200;
                if(connection.getResponseCode() == responseCode){
                    InputStream inputStream = connection.getInputStream();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int length;
                    while((length = inputStream.read(bytes)) != -1){
                        outputStream.write(bytes,0,length);
                    }
                    picByte = outputStream.toByteArray();
                    inputStream.close();
                    outputStream.close();
                    handler.sendEmptyMessage(hm);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
