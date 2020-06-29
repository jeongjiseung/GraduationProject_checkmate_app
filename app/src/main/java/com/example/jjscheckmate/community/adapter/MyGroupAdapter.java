package com.example.jjscheckmate.community.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.activity.PostActivity;
import com.example.jjscheckmate.community.model.GroupDTO;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;
import com.example.jjscheckmate.retrofitinterface.RetrofitResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyGroupHolder>{

    private ArrayList<GroupDTO> items;
    private Context mContext;
    private View itemView;
    AlertDialog alertDialog;

    private int orderIMG = 1;

    public MyGroupAdapter(Context context, ArrayList<GroupDTO> items){
        this.mContext=context;
        this.items=items;
    }

    @NonNull
    @Override
    public MyGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_my_group_item,parent,false);
        return new MyGroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGroupHolder holder, int position) {

//        Glide.with(mContext).load(mContext.getString(R.string.baseUrl)+"group/image/cover/"+items.get(position).get_id())
//                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(30)))
//                .into(holder.cover);

// 임시
        switch (orderIMG) {
//            case 1: // 풋살
//                Glide.with(mContext).load("https://mblogthumb-phinf.pstatic.net/20160907_242/zuckcorp_1473216448589mnfLH_JPEG/2.JPG?type=w2")
//                        .into(holder.cover);
//                orderIMG++;
//                break;

            default:

                break;

        }



        holder.title.setText(items.get(position).getTitle().substring(1,items.get(position).getTitle().length()-1));
        holder.member_cnt.setText("멤버 : "+items.get(position).getMember_cnt());
        holder.grout_out.setOnClickListener(new ClickListener(holder,position));
        holder.layout.setOnClickListener(new ClickListener(holder,position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<GroupDTO> datas){
        int position=items.size();
        this.items.addAll(position,datas);
        notifyItemRangeChanged(position,datas.size());
    }

    class MyGroupHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView member_cnt;
        TextView grout_out;
        ImageView cover;
        ConstraintLayout layout;

        MyGroupHolder(View itemView){
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            member_cnt=(TextView)itemView.findViewById(R.id.member_cnt);
            grout_out=(TextView)itemView.findViewById(R.id.group_out);
            cover=(ImageView)itemView.findViewById(R.id.cover);
            layout=(ConstraintLayout)itemView.findViewById(R.id.my_group_layout);
        }
    }
    class ClickListener implements View.OnClickListener{
        int position;
        MyGroupHolder holder;
        ClickListener(@NonNull MyGroupHolder holder, int position){
            this.holder=holder;
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.my_group_layout:{

                    Intent intent =new Intent(mContext, PostActivity.class);
                    intent.putExtra("groupID",items.get(position).get_id());
                    mContext.startActivity(intent);

//                    Toast.makeText(mContext,"my_group_layout",Toast.LENGTH_SHORT).show();
//                    Log.d("mawang","MyGroupAdapter ClickListener onClick - items.get(position).get_id() = "+items.get(position).get_id());
                    break;
                }
                case R.id.group_out:{
                    groupOutDialog(position);
//                    Toast.makeText(mContext,"group_out",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }
    public void groupOutDialog(Integer position){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("그룹을 탈퇴하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                withdrawGroup(position);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void withdrawGroup(int position){
        RetrofitApi.getService().groupWithdraw(items.get(position).get_id()).enqueue(new retrofit2.Callback<RetrofitResponse>(){
            @Override
            public void onResponse(Call<RetrofitResponse> call, @NonNull Response<RetrofitResponse> response) {
                if(response.body().isRight()){
                    items.remove(position);
                    notifyItemRemoved(position); // 변화 즉시 적용

                    Log.d("mawang","MyGroupAdapter withdrawGroup - items.get(position).get_id() = "+items.get(position).get_id());
                    Log.d("mawang","MyGroupAdapter withdrawGroup - response.body().isRight() = "+response.body().isRight());
                }
            }
            @Override
            public void onFailure(Call<RetrofitResponse> call, Throwable t) {
                Log.d("mawang","MyGroupAdapter withdrawGroup - onFailure = "+t.getMessage());
            }
        });
    }
}