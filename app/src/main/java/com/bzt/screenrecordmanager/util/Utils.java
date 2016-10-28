package com.bzt.screenrecordmanager.util;

import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by sunxy on 2016/7/12.
 */

public class Utils extends DateUtils{


    public static String getClipPath(){
        return "clip/";
    }

    /**
     * 得到路径
     *
     * @return
     */
    public static String getsaveDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ScreenRecord" + "/";

            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }
            Log.d("TAG", "保存的路径 : " + rootDir);
            return rootDir;
        } else {
            return null;
        }
    }

    /**
     * 读取MP4 文件
     *
     * @return
     */
    public static File[] readMp4(String path) {
        File file = new File(path);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {

                if (file.getName().endsWith(".mp4"))
                    return true;
                return false;
            }
        });

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File file, File t1) {
                String f1 = file.getName().replace(".mp4", "");
                String f2 = t1.getName().replace(".mp4", "");
                long l1Time = Long.parseLong(f1);
                long l2Time = Long.parseLong(f2);
                if (l1Time > l2Time)
                    return -1;
                if (l1Time < l2Time)
                    return 1;
                return 0;
            }
        });
        return files;
    }

    /**
     * 得到时间
     *
     * @param fileName
     * @return
     */
    public static String getDate(String fileName) {

        if (TextUtils.isEmpty(fileName))
            return "";


        String time = fileName.replace(".mp4","");

        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(date);
    }

    public static  double getTimeForDate(String date){

        String[] strings = date.split(":");

        int length = strings.length;
        double douTime = 0;
        if (length==3){
             douTime = Double.parseDouble(strings[0])*60
                    +Double.parseDouble(strings[1])*60
                    +Double.parseDouble(strings[2])*1000;
        }else  if (length==2){
            douTime = Double.parseDouble(strings[0])*60
                    +Double.parseDouble(strings[1])*1000;
        }

        return douTime;
    }
}
