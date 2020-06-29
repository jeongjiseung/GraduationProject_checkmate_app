package com.example.jjscheckmate.form;

import android.content.Context;
import android.widget.LinearLayout;

import org.json.JSONObject;

public abstract class FormAbstract extends LinearLayout {

    public abstract JSONObject getJsonObject();
    public abstract void formComponentSetting(FormComponentVO vo);

    private Context mContext;
    public int mType; // 잠깐만 public ,,private

    public FormAbstract(Context context, int type){
        super(context);
        this.mType=type;
    }

}
