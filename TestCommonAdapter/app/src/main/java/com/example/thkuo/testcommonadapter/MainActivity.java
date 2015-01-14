package com.example.thkuo.testcommonadapter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    private BrandDBProxy proxy;
    private int mCount;
    private String[] brandAreas;
    private String[] brandContents;
    private String[] brandIDs;
    private String[] brandImgs;
    private String[] brandTitles;
    private String[] posX;
    private String[] posY;

    private ArrayList<HashMap> dataList;
    private ListView mListView;

    private RequestQueue queue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brand_list_layout);

        queue = VolleySingleton.getInstance().getRequestQueue();
        mImageLoader = VolleySingleton.getInstance().getImageLoader();

        proxy = new BrandDBProxy(this);
        setComponent();
    }

    private void setComponent() {
        initListData();
        dataList = getData();

        mListView = (ListView) findViewById(R.id.listView1);
        mListView.setAdapter(new CommonAdapter(this, dataList, R.layout.brand_list_items) {
            @Override
            public void setViewData(CommonViewHolder commonViewHolder, View currentView, Object item) {
                TextView title = (TextView) commonViewHolder.get(commonViewHolder, currentView, R.id.brandTitle);
                TextView content = (TextView) commonViewHolder.get(commonViewHolder, currentView, R.id.brandContent);
                NetworkImageView networkImage = (NetworkImageView) commonViewHolder.get(commonViewHolder,
                        currentView, R.id.network_image_view);

                HashMap<String, Object> map = (HashMap<String, Object>) item;
                title.setText((String) map.get("title"));
                content.setText((String) map.get("content"));
                networkImage.setImageUrl((String) map.get("img"), mImageLoader);
            }
        });

    }

    private ArrayList<HashMap> getData() {
        // TODO Auto-generated method stub

        @SuppressWarnings("rawtypes")
        ArrayList<HashMap> list = new ArrayList<HashMap>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        for (int i = 0; i < mCount; i++) {
            map = new HashMap<String, Object>();

            map.put("id", brandIDs[i]);
            map.put("title", brandTitles[i]);
            map.put("content", brandContents[i]);
            map.put("img", brandImgs[i]);
            //map.put("dis", mDistance[i]);
            list.add(map);
        }

        return list;
    }

    private void initListData() {
        proxy.queryBrandData(1);

        mCount = proxy.getBrandCount();
        brandAreas = new String[mCount];
        brandContents = new String[mCount];
        brandIDs = new String[mCount];
        brandImgs = new String[mCount];
        brandTitles = new String[mCount];
        posX = new String[mCount];
        posY = new String[mCount];

        if (mCount > 0) {
            brandAreas = proxy.getBrandAreas();
            brandContents = proxy.getBrandContent();
            brandIDs = proxy.getBrandID();
            brandImgs = proxy.getBrandImg();
            brandTitles = proxy.getBrandTitles();
            posX = proxy.getBrandPosX();
            posY = proxy.getBrandPosY();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
