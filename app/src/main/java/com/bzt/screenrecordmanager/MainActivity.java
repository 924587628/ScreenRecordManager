package com.bzt.screenrecordmanager;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bzt.screenrecordmanager.service.RecordService;
import com.bzt.screenrecordmanager.util.IntentUtil;
import com.bzt.screenrecordmanager.util.ScreenUtils;

/**
 * 只支持5.0以上
 */
public class MainActivity extends AppCompatActivity {

    private static final int RECORD_REQUEST_CODE = 101;
    private static final int STORAGE_REQUEST_CODE = 102;
    private static final int AUDIO_REQUEST_CODE = 103;

    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private RecordService recordService;

    private Button startBtnPort;
    private Button startBtnLand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main1);

        startBtnPort = (Button) findViewById(R.id.start_record_port);
        startBtnLand = (Button) findViewById(R.id.start_record_land);

//        startBtnPort.setEnabled(false);
//        startBtnLand.setEnabled(false);

        //竖屏
        startBtnPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recordService.isRunning()) {
                    String filePath = recordService.getCurrentFilePath();
                    recordService.stopRecord();
                    startBtnPort.setText("开始竖屏录制");
                    IntentUtil.goPalyertActivity(MainActivity.this,filePath);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    Intent captureIntent = projectionManager.createScreenCaptureIntent();
                    startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
                }
            }
        });

        //横屏
        startBtnLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordService.isRunning()) {
                    String filePath = recordService.getCurrentFilePath();
                    recordService.stopRecord();
                    startBtnLand.setText("开始橫屏录制");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    IntentUtil.goPalyertActivity(MainActivity.this,filePath);
                } else {
                    //如果是竖排,则改为横排
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    Intent captureIntent = projectionManager.createScreenCaptureIntent();
                    startActivityForResult(captureIntent, RECORD_REQUEST_CODE);

                }
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_REQUEST_CODE);
        }

        Intent intent = new Intent(this, RecordService.class);
        //绑定service后才能进行 操作
        bindService(intent, connection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {

            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            recordService.setMediaProject(mediaProjection);

            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                startBtnPort.setText("停止");
            } else {
                startBtnLand.setText("停止");
            }

            recordService.startRecord();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_REQUEST_CODE || requestCode == AUDIO_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            RecordService.RecordBinder binder = (RecordService.RecordBinder) service;
            recordService = binder.getRecordService();


            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                startBtnLand.setEnabled(true);
                startBtnLand.setText(recordService.isRunning() ? "停止" : "开始横屏录制");
                recordService.setConfig(
                        ScreenUtils.getScreenHeight(MainActivity.this),
                        ScreenUtils.getScreenWidth(MainActivity.this),
                        ScreenUtils.getScreenDpi(MainActivity.this));
            } else {
                startBtnPort.setEnabled(true);
                startBtnPort.setText(recordService.isRunning() ? "停止" : "开始竖屏录制");
                recordService.setConfig(
                        ScreenUtils.getScreenWidth(MainActivity.this),
                        ScreenUtils.getScreenHeight(MainActivity.this),
                        ScreenUtils.getScreenDpi(MainActivity.this));
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    public void startFileList(View view)
    {
        IntentUtil.goFileListActivity(this);
    }
    public void clipFileList(View view){
        IntentUtil.goClipListActivity(this);
    }


}
