package com.example.chenx.musc.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.chenx.musc.R;
import com.example.chenx.musc.model.Action;
import com.example.chenx.musc.model.Record;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PieCharFragment extends Fragment {

    private List<Record> recordList;
    private List<Action> actionList;

    private void initRecords(){
        // List<Action> actions=LitePal.findAll(Action.class);
        recordList=new ArrayList<>();
        actionList=new ArrayList<>();
        actionList=LitePal.findAll(Action.class);
        //recordList=LitePal.findAll(Record.class);
        for(Action action : actionList){
            List<Record> records=action.getRecords();
            for(Record record : records){
                record.setAction(action);
                recordList.add(record);
            }
        }
    }

    PieChart mChart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.analyse_piechart,container,false);

        mChart=view.findViewById(R.id.pieChart);
        initChart();

        return view;
    }

    private void initChart()
    {
        List<PieEntry> entries = new ArrayList<>();
        int total=LitePal.findAll(Action.class).size();

        ArrayList<Integer> colors = new ArrayList<>();

        List<Float> values=new ArrayList<>() ;
        List<String> labels=new ArrayList<>();

        Random random = new Random();
        initRecords();
        for(Record r : recordList)
        {
            String label=r.getAction().getGroup();
            if(!labels.contains(label)){
                labels.add(label);
                int result=LitePal.where("group = ?",label).find(Action.class).size();
                float value=(float)result/(float)total;
                values.add(value);

                entries.add(new PieEntry(value, label));
                int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
                colors.add(ranColor);
            }
        }


        PieDataSet set = new PieDataSet(entries, "Muscles");
        set.setColors(colors);
        PieData data = new PieData(set);
        mChart.setData(data);
        mChart.invalidate(); // refresh

    }
}
