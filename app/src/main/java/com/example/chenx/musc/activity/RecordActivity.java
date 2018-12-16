package com.example.chenx.musc.activity;

import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.chenx.musc.MyApplication;
import com.example.chenx.musc.R;
import com.example.chenx.musc.model.Action;
import com.example.chenx.musc.model.Record;
import com.example.chenx.musc.tool.TenorTool;

import org.litepal.LitePal;
import org.litepal.crud.callback.SaveCallback;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordActivity extends AppCompatActivity{

    private  Spinner spinnerGroup;
    private Spinner spinnerAction;

    private ImageView imageViewActionGif;
    private NumberPicker timesChooser;
    private NumberPicker weightChoose;

    private Button buttonSvae;
    private Button buttonClear;

    private ArrayList<String> data1;
    private ArrayList<List<String>> data2;

    public static final int SHOW_GIF=1;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        spinnerGroup=(Spinner)findViewById(R.id.spinnerGroup);
        spinnerAction=(Spinner)findViewById(R.id.spinnerAction);

        imageViewActionGif=(ImageView)findViewById(R.id.imageViewActionGif);

        timesChooser=(NumberPicker)findViewById(R.id.timesChooser);
        weightChoose=(NumberPicker)findViewById(R.id.weightChooser);

        buttonSvae=(Button)findViewById(R.id.buttonSave);
        buttonClear=(Button)findViewById(R.id.buttonClearAll);

        initNumberPicker();

        setSpinnerGroup();
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(RecordActivity.this, R.layout.support_simple_spinner_dropdown_item,data1);
        spinnerGroup.setAdapter(adapter1);

       spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               ArrayAdapter<String> adapter2=new ArrayAdapter<String>(RecordActivity.this,R.layout.support_simple_spinner_dropdown_item, data2.get(position));
               spinnerAction.setAdapter(adapter2);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       spinnerAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               getGifUrl();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       buttonSvae.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(save()) {
                   finish();
               }else{
                   Toast.makeText(RecordActivity.this,"Save failed. Try again",Toast.LENGTH_SHORT).show();
               }
           }
       });

       buttonClear.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               clearall();
           }
       });

    }

    public void clearall(){
        timesChooser.setValue(1);
        weightChoose.setValue(1);
    }

    private  void initNumberPicker(){
        String[] nums={"0","2.5","5","7.5","10","12.5","15","17.5","20","25","30","35","40","45","50","55","60"};

        timesChooser.setMinValue(1);
        timesChooser.setMaxValue(200);
        weightChoose.setMinValue(0);
        weightChoose.setMaxValue(nums.length-1);
        weightChoose.setMinValue(0);
        weightChoose.setDisplayedValues(nums);
    }

    public boolean save(){
        String actionName=spinnerAction.getSelectedItem().toString();
        String actionGroup=spinnerGroup.getSelectedItem().toString();
        Action action=new Action();
        action.setGroup(actionGroup);
        action.setName(actionName);
        action.save();

        Record record=new Record();
        record.setAction(action);
        record.setDate(new Date(System.currentTimeMillis()));
        record.setTimes(timesChooser.getValue());
        record.setWoWeight(weightChoose.getValue());
        if(record.save()){
            return true;
        }else{
            return false;
        }
    }

    public void saveSaync(){
        String actionName=spinnerAction.getSelectedItem().toString();
        String actionGroup=spinnerGroup.getSelectedItem().toString();
        Action action=new Action();
        action.setGroup(actionGroup);
        action.setName(actionName);
        final Record record=new Record();
        record.setAction(action);
        record.setDate(new Date(System.currentTimeMillis()));
        record.setTimes(timesChooser.getValue());
        record.setWoWeight(weightChoose.getValue());
        if(LitePal.where("name = ? and group = ?",actionName,actionGroup).find(Action.class).isEmpty())
        {
            action.saveAsync().listen(new SaveCallback() {
                @Override
                public void onFinish(boolean success) {
                    if(success){
                        record.saveAsync().listen(new SaveCallback() {
                                                      @Override
                                                      public void onFinish(boolean success) {
                                                          //Toast.makeText(MyApplication.getContext(), "save success", Toast.LENGTH_SHORT).show();
                                                      }
                                                  });

                    }else{
                        //Toast.makeText(MyApplication.getContext(),"save failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            record.saveAsync().listen(new SaveCallback() {
                @Override
                public void onFinish(boolean success) {
//                    Toast.makeText(MyApplication.getContext(), "save success", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }





    public void getGifUrl(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences mPrefs = getSharedPreferences("tenor", 0);
                String anonId = mPrefs.getString("anonymousId","");

                if(anonId == "") // first time user, so get an anonymous ID for them and store it for later use
                {
                    anonId = TenorTool.getAnonId();
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putString("anonymousId", anonId).commit();
                }
                final String searchTerm=spinnerAction.getSelectedItem().toString();

                //final String searchTerm="workout";
                // make initial search request for the first 8 items
                JSONObject searchResult = TenorTool.getSearchResults(anonId, searchTerm, 1);

                // load the results for the user
                //Log.v(LogTag, "Search Results: " + searchResult.toString());

                JSONArray data0 = searchResult.getJSONArray("results");
                if(data0!=null)
                {
                    JSONObject object0 = data0.getJSONObject(0);
                    JSONArray media=object0.getJSONArray("media");
                    JSONObject types=media.getJSONObject(0);
                    JSONObject tinygif=types.getJSONObject("tinygif");
                    JSONObject gif=types.getJSONObject("gif");

                    final String tinyGifUrl=tinygif.getString("url");
                    final String gifUrl = gif.getString("url");

                    ShowGif(gifUrl);
                }

            }
        }).start();
    }

    private void ShowGif(final String tinyGifUrl){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(RecordActivity.this).load(tinyGifUrl).into(imageViewActionGif);
            }
        });
    }

    /**
     * set spinner resources
     */
    public void setSpinnerGroup()
    {
        XmlResourceParser parser=getResources().getXml(R.xml.action);
        int type;
        try{
            type =parser.getEventType();

            List<String> actions=null;

            while (type!=XmlPullParser.END_DOCUMENT){
                if(type==XmlPullParser.START_DOCUMENT){
                    data1 = new ArrayList<String>();
                    data2=new ArrayList<List<String>>();
                }else if(type== XmlPullParser.START_TAG){
                    String nodeName=parser.getName();
                    if("group".equals(nodeName)){
                        data1.add(parser.getAttributeValue(0));
                        actions=new ArrayList<String>();
                    }else if("action".equals(nodeName)){
                        actions.add(parser.nextText());
                    }
                }else if(type ==XmlPullParser.END_TAG){
                    String nodeName=parser.getName();
                    if("group".equals(nodeName)){
                        data2.add(actions);
                    }
                }
                type=parser.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }



    }

}
