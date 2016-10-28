package com.bzt.screenrecordmanager.util;

import android.content.Context;
import android.content.Intent;

import com.bzt.screenrecordmanager.ClipFileListActivity;
import com.bzt.screenrecordmanager.FileListActivity;
import com.bzt.screenrecordmanager.PlayerActivity;
import com.bzt.screenrecordmanager.config.Config;

/**
 * Created by sunxy on 2016/7/13.
 */

public class IntentUtil {

    public static void goFileListActivity(Context context) {
        Intent intent = new Intent(context, FileListActivity.class);
        context.startActivity(intent);
    }

    public static void goPalyertActivity(Context context, String filePath) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(Config.File_Path,filePath);
        context.startActivity(intent);
    }

    public static void goClipListActivity(Context context) {
        Intent intent = new Intent(context, ClipFileListActivity.class);
        context.startActivity(intent);
    }

}
