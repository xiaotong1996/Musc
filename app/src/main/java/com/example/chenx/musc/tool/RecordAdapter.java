package com.example.chenx.musc.tool;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chenx.musc.R;
import com.example.chenx.musc.model.Record;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<Record> mRecordList;

    private int opened= -1;

    static class ViewHolder extends  RecyclerView.ViewHolder{
        View recordView;
        TextView date;
        TextView group;
        TextView action;
        TextView weight;
        TextView times;
        LinearLayout line0;
        LinearLayout line1;
        LinearLayout line2;


        public ViewHolder(View view)
        {
            super(view);
            recordView=view;
            date=(TextView) view.findViewById(R.id.record_date);
            group=(TextView) view.findViewById(R.id.record_group);
            action=(TextView) view.findViewById(R.id.record_action);
            weight=(TextView) view.findViewById(R.id.record_weight);
            times=(TextView) view.findViewById(R.id.record_times);
            line0 = (LinearLayout) view.findViewById(R.id.record_line0);
            line1 = (LinearLayout) view.findViewById(R.id.record_line1);
            line2 = (LinearLayout) view.findViewById(R.id.record_line2);
        }
    }


    public RecordAdapter(List<Record> recordList){
        mRecordList=recordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.recordView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                if(opened==position)
                {
                    opened=-1;
                    notifyItemChanged(position);
                }
                else {
                    int oldOpened=opened;
                    opened=position;
                    notifyItemChanged(oldOpened);
                    notifyItemChanged(opened);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Record record=mRecordList.get(i);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        viewHolder.date.setText(simpleDateFormat.format(record.getDate()));
        viewHolder.group.setText(record.getAction().getGroup());
        viewHolder.action.setText(record.getAction().getName());
        viewHolder.weight.setText(String.valueOf(record.getWoWeight()));
        viewHolder.times.setText(String.valueOf(record.getTimes()));

        if(i == opened){
            viewHolder.line1.setVisibility(View.VISIBLE);
            viewHolder.line2.setVisibility(View.VISIBLE);
        }else{
            viewHolder.line1.setVisibility(View.GONE);
            viewHolder.line2.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mRecordList.size();
    }
}
