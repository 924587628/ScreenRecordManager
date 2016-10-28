package com.bzt.screenrecordmanager;

import android.net.Uri;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;

/**
 * Created by sunxy on 2016/7/26.
 */

public class ClipFileListActivity extends AppCompatActivity implements OnRecycleViewItemClickListener {

    private RecyclerView mRecyclerView;
    private FileListAdapter adapter;

    private File[] files;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        initData();
        initView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                String path = Utils.getsaveDirectory();
                if (path == null) {
                    handler.sendEmptyMessage(1);
                    return;
                }

                path = path + Utils.getClipPath();

                files = Utils.readMp4(path);

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // 设置adapter
                    adapter = new FileListAdapter(ClipFileListActivity.this, files);
                    mRecyclerView.setAdapter(adapter);
                    // 条目点击事件
                    adapter.setItemClickListener(ClipFileListActivity.this);
                    break;
                case 1:
                    Toast.makeText(ClipFileListActivity.this, "SD卡读取失败 !", Toast.LENGTH_SHORT).show();
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

        IntentUtil.goPalyertActivity(this, file.getAbsolutePath());
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ClipFileList Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
