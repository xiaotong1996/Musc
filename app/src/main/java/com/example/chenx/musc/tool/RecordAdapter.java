package com.example.chenx.musc.tool;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chenx.musc.R;
import com.example.chenx.musc.model.Record;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<Record> mRecordList;

    static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView date;
        TextView group;
        TextView action;

        public ViewHolder(View view)
        {
            super(view);
            date=(TextView) view.findViewById(R.id.record_date);
            group=(TextView) view.findViewById(R.id.record_group);
            action=(TextView) view.findViewById(R.id.record_action);
        }
    }

    public RecordAdapter(List<Record> recordList){
        mRecordList=recordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Record record=mRecordList.get(i);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        viewHolder.date.setText(simpleDateFormat.format(record.getDate()));
        viewHolder.group.setText(record.getAction().getGroup());
        viewHolder.action.setText(record.getAction().getName());
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }
}
