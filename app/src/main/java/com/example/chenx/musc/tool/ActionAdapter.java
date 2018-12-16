package com.example.chenx.musc.tool;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenx.musc.GlideApp;
import com.example.chenx.musc.MyApplication;
import com.example.chenx.musc.R;
import com.example.chenx.musc.model.Action;


import java.util.List;


public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> {
    private List<Action> mActionList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView actionName;
        ImageView actionTinyGif;

        public ViewHolder(View view)
        {
            super(view);
            actionName=(TextView)view.findViewById(R.id.action_name);
            actionTinyGif=(ImageView)view.findViewById(R.id.action_tinygif);
        }
    }

    public ActionAdapter(List<Action> actionList)
    {
        mActionList=actionList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_item, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Action action=mActionList.get(position);
        holder.actionName.setText(action.getName());
        String imgUrl=action.getUrl();
        //holder.actionTinyGif.setImageURI(Uri.parse(action.getTinyUrl()));

       // Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
        //Picasso.get().load(action.getUrl()).into( holder.actionTinyGif);
        GlideApp.with(MyApplication.getContext()).load(imgUrl).placeholder(R.drawable.kulian).into(holder.actionTinyGif);
    }

    @Override
    public int getItemCount() {
        return mActionList.size();
    }
}
