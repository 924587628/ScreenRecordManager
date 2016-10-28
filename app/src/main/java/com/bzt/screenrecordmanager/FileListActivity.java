package com.bzt.screenrecordmanager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bzt.screenrecordmanager.adapter.FileListAdapter;
import com.bzt.screenrecordmanager.itemview.DividerItemDecoration;
import com.bzt.screenrecordmanager.listener.OnRecycleViewItemClickListener;
import com.bzt.screenrecordmanager.util.IntentUtil;
import com.bzt.screenrecordmanager.util.Utils;

import java.io.File;

/**
 * Created by sunxy on 2016/7/12.
 */
public class FileListActivity extends AppCompatActivity implements OnRecycleViewItemClickListener {


    private RecyclerView mRecyclerView;
    private FileListAdapter adapter;

    private File[] files ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        initData();
        initView();
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                String path = Utils.getsaveDirectory();
                if (path == null){
                    handler.sendEmptyMessage(1);
                    return;
                }
                files = Utils.readMp4(path);

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    // 设置adapter
                    adapter = new FileListAdapter(FileListActivity.this,files);
                    mRecyclerView.setAdapter(adapter);
                    // 条目点击事件
                    adapter.setItemClickListener(FileListActivity.this);
                    break;
                case 1:
                    Toast.makeText(FileListActivity.this,"SD卡读取失败 !",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);


        // 设置布局管理器
        LinearLayoutManager layout = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layout);

        // 设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }

    @Override
    public <T> void onItemClick(View view, int position, T t) {
        File file = (File) t;
        /**
         * 1.使用手机自带的播放器
         * 使用Intent.ACTION_VIEW常量构造一个活动，并通过setDataAndType方法传入文件的URI和MIME类型
         */

//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
//        Uri data = Uri.parse(file.getAbsolutePath());
//        intent.setDataAndType(data, "video/mp4");
//        startActivity(intent);

        IntentUtil.goPalyertActivity(this,file.getAbsolutePath());
    }
}
