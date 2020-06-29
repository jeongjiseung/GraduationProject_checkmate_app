package com.example.jjscheckmate.form;

import android.content.Context;

public class FormFactory {
    private int mType;
    private Context mContext;

    private FormFactory(Context context, int type){
        this.mContext=context;
        this.mType=type;
    }

    public static FormFactory getInstance(Context context, int type){ // null 체크를 안한거 보면 완전 싱글턴은 아니네
       return new FormFactory(context,type);
    }

    public FormAbstract createForm(){
        if(mType==FormType.SHORTTEXT||mType==FormType.LONGTEXT||mType==FormType.DATE||mType==FormType.TIME){
            return new FormTypeText(mContext,mType);
        }
        else if(mType==FormType.RADIOCHOICE||mType==FormType.CHECKBOXES||mType==FormType.DROPDOWN){
            return new FormTypeOption(mContext,mType);
        }
        else if(mType==FormType.LINEARSCALE){
            return new FormTypeLinear(mContext,mType);
        }
        else if(mType==FormType.RADIOCHOICEGRID || mType==FormType.CHECKBOXGRID){
            return new FormTypeGrid(mContext,mType);
        }
        else if(mType==FormType.ADDSECTION){
            return new FormTypeSection(mContext,mType);
        }
        else if(mType==FormType.SUBTEXT){
            return new FormTypeSubText(mContext,mType);
        }
        else if(mType==FormType.IMAGE){
            return new FormTypeImage(mContext,mType);
        }
        else if(mType==FormType.VIDEO){ // 변경해야되 fab 로
            return new FormTypeVideo(mContext,mType);
        }
        return null;
    }


}
