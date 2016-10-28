package com.bzt.screenrecordmanager.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.coremedia.iso.IsoFile;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 视屏合成
 * Created by sunxy on 2016/7/25.
 */

public class MergeVideos extends AsyncTask<String, Integer, String> {

    private Context context;
    //保存路径
    private String savePath;

    // The working path where the video files are located
    private String workingPath;
    // The file names to merge
    private ArrayList<String> videosToMerge;
    // Dialog to show to the user
    private ProgressDialog progressDialog;

    static final long[] ROTATE_0 = new long[]{1, 0, 0, 1, 0, 0, 1, 0, 0};
    static final long[] ROTATE_90 = new long[]{0, 1, -1, 0, 0, 0, 1, 0, 0};
    static final long[] ROTATE_180 = new long[]{-1, 0, 0, -1, 0, 0, 1, 0, 0};
    static final long[] ROTATE_270 = new long[]{0, -1, 1, 0, 0, 0, 1, 0, 0};

    /**
     * @param context
     * @param workingPath   本地文件的路径 本路径下存放的是要合成的视屏文件
     * @param videosToMerge 存放文件名称 例: "xxxxxxx.MP4"
     */
    public MergeVideos(Context context, String workingPath, ArrayList<String> videosToMerge) {
        this.context = context;
        this.workingPath = workingPath;
        this.videosToMerge = videosToMerge;

        init();
    }

    private void init() {
        if (workingPath.endsWith("/"))
            savePath = workingPath + "merge/";
        else
            savePath = workingPath + "/merge/";
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, "Merging videos", "Please wait...", true);
    }

    ;

    @Override
    protected String doInBackground(String... params) {
        int count = videosToMerge.size();
        try {
            Movie[] inMovies = new Movie[count];
            for (int i = 0; i < count; i++) {
                File file = new File(workingPath, videosToMerge.get(i));

                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    FileChannel fc = fis.getChannel();
                    inMovies[i] = MovieCreator.build(fc);
                    fis.close();
                    fc.close();
                }
            }
            List<Track> videoTracks = new LinkedList<Track>();
            List<Track> audioTracks = new LinkedList<Track>();

            for (Movie m : inMovies) {
                for (Track t : m.getTracks()) {
                    if (t.getHandler().equals("soun")) {
                        audioTracks.add(t);
                    }
                    if (t.getHandler().equals("vide")) {
                        videoTracks.add(t);
                    }
                    if (t.getHandler().equals("")) {

                    }
                }
            }

            Movie result = new Movie();

            if (audioTracks.size() > 0) {
                result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
            }
            IsoFile out = new DefaultMp4Builder().build(result);

            // rotate video

            out.getMovieBox().getMovieHeaderBox().setMatrix(ROTATE_270);

            long timestamp = new Date().getTime();
            String timestampS = "" + timestamp;

            File storagePath = new File(savePath);
            storagePath.mkdirs();

            File myMovie = new File(storagePath, String.format("output-%s.mp4", timestampS));

            FileOutputStream fos = new FileOutputStream(myMovie);
            FileChannel fco = fos.getChannel();
            fco.position(0);
            out.getBox(fco);
            fco.close();
            fos.close();

        } catch (FileNotFoundException e) {
            Log.d("TAG", "合成失败 ------ " + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("TAG", "合成失败 ------ " + e);
            e.printStackTrace();
        }
        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/output.mp4";
        Log.d("TAG", "合成成功 ------ " + mFileName);
        return mFileName;
    }

    @Override
    protected void onPostExecute(String value) {
        super.onPostExecute(value);
        progressDialog.dismiss();
    }

}
