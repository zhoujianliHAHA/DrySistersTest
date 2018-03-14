package com.example.jerryzhou.drysisters.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.jerryzhou.drysisters.R;
import com.example.jerryzhou.drysisters.entity.Sister;
import com.example.jerryzhou.drysisters.imgloader.PictureLoader;
import com.example.jerryzhou.drysisters.net.SisterApi;

import java.util.ArrayList;

/**
 * @author admin
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView showImg;
    private ArrayList<Sister> data;
    /**
     * curPos 当前显示的是哪一张
     * page 当前页数
     */
    private int curPos = 0;
    private int page = 1;
    private PictureLoader loader;
    private SisterApi sisterApi;
    private SisterTask sisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sisterApi = new SisterApi();
        loader = new PictureLoader();
        initData();
        initView();

    }

    private void initData() {
        data = new ArrayList<>();
    }

    private void initView() {
        Button showBtn = (Button) findViewById(R.id.btn_sister);
        Button refreshBtn = (Button) findViewById(R.id.btn_next);
        showImg = (ImageView) findViewById(R.id.img_sister);

        showBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_sister:
                if(data != null && !data.isEmpty()){
                    if(curPos > 9){
                        curPos = 0;
                    }
                    loader.load(showImg,data.get(curPos).getUrl());
                    curPos++;
                }
                break;

            case R.id.btn_next:
                sisterTask = new SisterTask();
                sisterTask.execute();
                curPos = 0;
                break;

            default:
                break;
        }
    }

    private class SisterTask extends AsyncTask<Void,Void,ArrayList<Sister>>{


        SisterTask(){}

        @Override
        protected ArrayList<Sister> doInBackground(Void... params) {
            return sisterApi.QuerySister(10,page);
        }

        @Override
        protected void onPostExecute(ArrayList<Sister> sisters) {
            super.onPostExecute(sisters);
            data.clear();
            data.addAll(sisters);
            page++;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            sisterTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sisterTask.cancel(true);
    }
}
