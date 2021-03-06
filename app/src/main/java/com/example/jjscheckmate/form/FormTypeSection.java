package com.example.jjscheckmate.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.jjscheckmate.R;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import org.json.JSONObject;

public class FormTypeSection extends FormAbstract{
    private Context mContext;
    private int mType;

    private LayoutInflater mInflater;
    private View customView;

    private EditText mEditTitle;
    private ImageButton mCopyView;
    private ImageButton mDeleteView;
    private EditText mEditDescription;

    public FormTypeSection(Context context, int type){
        super(context,type);
        mContext=context;
        this.mType=type;

        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_section,this,true);

        mEditTitle = (EditText) findViewById(R.id.editTitle);
        mCopyView = (ImageButton) findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);
        mEditDescription = (EditText) findViewById(R.id.editDescription);

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());
    }
    public FormTypeSection(FormCopyFactory fcf) {
        super(fcf.getmContext(), fcf.getmType());
        mContext = fcf.getmContext();
        this.mType = fcf.getmType();

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_section, this, true);

        mEditTitle = (EditText) findViewById(R.id.editTitle);
        mCopyView = (ImageButton) findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);
        mEditDescription = (EditText) findViewById(R.id.editDescription);

        mEditTitle.setText(fcf.getEditQuestion_text());
        mEditDescription.setText(fcf.getEditExplanation_text());

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());
    }


    @Override
    public JSONObject getJsonObject(){
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("type",mType);
            jsonObject.put("question",mEditTitle.getText().toString()); // title
            jsonObject.put("description",mEditDescription.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject;
    }
    @Override
    public void formComponentSetting(FormComponentVO vo) {
        mEditTitle.setText(vo.getQuestion());
        mEditDescription.setText(vo.getDescription());
    }

    public class ClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == mDeleteView) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                parentView.removeView(customView);
            } else if (view == mCopyView) {
                DragLinearLayout parentView = (DragLinearLayout) customView.getParent();
                int index = parentView.indexOfChild(customView);

                FormAbstract layout = new FormCopyFactory.Builder(mContext, mType)
                        .Question(mEditTitle.getText().toString())
                        .Explanation(mEditDescription.getText().toString())
                        .build()
                        .createCopyForm();

                ViewGroup customlayout = (ViewGroup) layout.getChildAt(0);
                ImageView dragHandle = (ImageView)customlayout.getChildAt(0);
                parentView.addDragView(layout, dragHandle, index + 1);
            }
        }
    }
}
