package com.bzt.screenrecordmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bzt.screenrecordmanager.R;
import com.bzt.screenrecordmanager.util.Utils;
import com.bzt.screenrecordmanager.listener.OnRecycleViewItemClickListener;

import java.io.File;

/**
 * Created by sunxy on 2016/7/12.
 */

public class FileListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private File[] files;

    public FileListAdapter(Context context, File[] files) {
        this.context = context;
        this.files = files;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)//
                .inflate(R.layout.item_file_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final File file = files[position];

        MyViewHolder holder = (MyViewHolder) viewHolder;

        String text = "文件名称:" + file.getName() + "\n"+ "\n"
                + "文件路径：" + file.getAbsolutePath();

        holder.fileDetail.setText(text);
        holder.fileDate.setText(Utils.getDate(file.getName()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(viewHolder.itemView, position, file);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return files == null ? 0 : files.length;
    }

    private OnRecycleViewItemClickListener itemClickListener;

    public void setItemClickListener(OnRecycleViewItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fileDetail;
        TextView fileDate;

        public MyViewHolder(View itemView) {
            super(itemView);

            fileDetail = (TextView) itemView.findViewById(R.id.file_detail);
            fileDate = (TextView) itemView.findViewById(R.id.file_date);
        }
    }
}
