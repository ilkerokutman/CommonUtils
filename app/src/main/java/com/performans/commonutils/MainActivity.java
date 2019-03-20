package com.performans.commonutils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import com.performans.commonutilshelper.*;

public class MainActivity extends AppCompatActivity {

    private Context mContext = this;
    MyAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<MyModel> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        demonstrate();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, RecyclerView.VERTICAL));
        adapter = new MyAdapter(mContext, items);
        recyclerView.setAdapter(adapter);
    }

    private void appendItem(String t, String d, boolean h) {
        items.add(new MyModel(t, d, h));
        adapter.notifyDataSetChanged();
    }

    private void demonstrate() {
        appendItem("getAppVersionName", CommonUtils.getAppVersionName(), false);
        appendItem("getAppVersionCode", String.valueOf(CommonUtils.getAppVersionCode()), false);
        appendItem("getDeviceId", CommonUtils.getDeviceId(mContext), false);
        appendItem("getOsVersion", CommonUtils.getOsVersion(), false);
        appendItem("getBatteryPercentage", String.valueOf(CommonUtils.getBatteryPercentage(mContext)), false);
        appendItem("getBatteryStatus", String.valueOf(CommonUtils.getBatteryStatus(mContext)), false);
        appendItem("isRooted", String.valueOf(CommonUtils.isRooted()), false);
        appendItem("generateFileName", CommonUtils.generateFileName(), false);
        appendItem("isConnected", String.valueOf(CommonUtils.isConnected(mContext)), false);
    }

}
