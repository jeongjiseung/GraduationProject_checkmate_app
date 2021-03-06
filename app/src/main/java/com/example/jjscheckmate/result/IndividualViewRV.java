package com.example.jjscheckmate.result;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;

import java.util.ArrayList;

public class IndividualViewRV extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private ArrayList<IndividualViewDTO> datas;
    private int form_id;

    public IndividualViewRV(Context context, ArrayList<IndividualViewDTO> datas, int form_id){
        this.mContext=context;
        this.datas=datas;
        this.form_id=form_id;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtIndex;
        TextView txtTime;

        public ViewHolder(View v){
            super(v);
            txtIndex=(TextView)v.findViewById(R.id.txtIndex);
            txtTime=(TextView)v.findViewById(R.id.txtTime);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,IndividualResultActivity.class);
                    intent.putExtra("form_id",form_id);
                    intent.putExtra("result",datas.get(getAdapterPosition()));
                    mContext.startActivity(intent);
//                    Toast.makeText(mContext,"결과로 갑니다", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.activity_individualview_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        IndividualViewDTO vo=datas.get(position);
        ((ViewHolder)holder).txtIndex.setText(String.valueOf(vo.getIndex()));
        ((ViewHolder)holder).txtTime.setText(vo.getTime());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setDatas(ArrayList<IndividualViewDTO> datas){
        this.datas=datas;
        notifyDataSetChanged();
    }
}
