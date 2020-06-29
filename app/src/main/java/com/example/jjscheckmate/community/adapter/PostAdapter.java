package com.example.jjscheckmate.community.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.activity.PostActivity;

import com.example.jjscheckmate.community.activity.PostCreateActivity;
import com.example.jjscheckmate.community.activity.PostUpdateActivity;
import com.example.jjscheckmate.community.model.CommentDTO;
import com.example.jjscheckmate.community.model.OnItemClick;
import com.example.jjscheckmate.community.model.PostDTO;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;
import com.example.jjscheckmate.retrofitinterface.RetrofitResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

public class PostAdapter extends  RecyclerView.Adapter<PostAdapter.PostHolder>{

    private ArrayList<PostDTO> items;
    private final static int COUNT = 5;
    private RecyclerView.LayoutManager layoutManager;
    Integer offset=0;

    private Context mContext;
    private View itemView;
    private OnItemClick mCallback;
    AlertDialog alertDialog;

    private int orderIMG = 1;

    public PostAdapter(Context context, ArrayList<PostDTO> items, OnItemClick callback){
        this.mContext=context;
        this.items=items;
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_community_post_item,parent,false);
        return new PostHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

//        if(items.get(position).getUserEmail()==null){
//            Glide.with(holder.itemView.getContext())
//                    .load(R.drawable.profile)
//                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
//            Log.d("mawang","PostAdapter onBindViewHolder - items.get(position).getUserEmail()==null");
//        }else{
//            Glide.with(mContext).load(mContext.getString(R.string.baseUrl)+"user/profile/select/"+items.get(position).getUserEmail()+".jpg")
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
//            Log.d("mawang","PostAdapter onBindViewHolder - items.get(position).getUserEmail() ="+items.get(position).getUserEmail());
//        }
//        Glide.with(mContext).load(mContext.getString(R.string.baseUrl)+"post/image/thumbnail/"+items.get(position).get_id())
//                .into(holder.thumbnail);

//        // 레이디가가
        Glide.with(mContext).load(R.drawable.tmp_profileimg)
                .apply(new RequestOptions().circleCrop())
                .into(holder.profileImage);
        // 파스터
        Glide.with(mContext).load(R.drawable.tmp_everyone)
                .into(holder.thumbnail);


        // 임시 방편
//        switch (orderIMG) {
//            case 1: // 풋살
//                Glide.with(mContext).load("https://mblogthumb-phinf.pstatic.net/20160907_242/zuckcorp_1473216448589mnfLH_JPEG/2.JPG?type=w2")
//                        .into(holder.cover);
//                orderIMG++;
//                break;
//            case 2: // 화장품
//                Glide.with(mContext).load("http://blogfiles.naver.net/MjAxNzA3MDFfNDUg/MDAxNDk4ODQ5MDQ0OTIz.Iz3-vmetXiz2ThhU7jWvmpo6yZDIOmDOO_L-dc1O-08g.0nQXBcZJ9oI0YjRfFxsKjdeJJqSXOanFRMTHzTQl5Skg.PNG.jjww4016/%C0%B1%C1%B6%BF%A1%BC%BE%BD%BA.png")
//                        .into(holder.cover);
//                orderIMG++;
//                break;
//            case 3: // 음식
//                Glide.with(mContext).load("http://cafefiles.naver.net/MjAxOTEyMDRfMTYy/MDAxNTc1NDI2OTgzNjc1.NWx_VpubVCLY2g3aI-BSqYQglPEnY2-bsiIITnpNAFYg.iTljN2vnoSU41UMKdvae40oBV07Sprd5T4QIN5u9gxog.JPEG/externalFile.jpg")
//                        .into(holder.cover);
//                orderIMG++;
//                break;
//            case 4: // 등산
//                Glide.with(mContext).load("https://img.newspim.com/news/2019/07/30/1907300801254570.jpg")
//                        .into(holder.cover);
//                orderIMG++;
//                break;
//            case 5: // 아이돌
//                Glide.with(mContext).load("https://img.hankyung.com/photo/201910/BF.20824282.1.jpg")
//                        .into(holder.cover);
//                orderIMG = 1;
//                break;
//        }


//        holder.userEmail.setText(items.get(position).getUserEmail().split("@")[0]);
        holder.userEmail.setText(items.get(position).getNickname()); // 이거 닉넴으로 바꾼다.

        holder.time.setText(getTime(items.get(position).getTime()));
        holder.content.setText(items.get(position).getContent());

//        Log.d("mawang","PostAdapter onBindViewHolder - loadComment 호출");
        loadComment(holder.adapter, items.get(holder.getAdapterPosition()).get_id()); // 댓글 불러오기

        holder.postDelete.setOnClickListener(new ClickListener(holder,position));
        holder.postUpdate.setOnClickListener(new ClickListener(holder,position));

        if(items.get(position).getUserEmail().equals(Session.getUserEmail())){ // 다른 사용자가 본인의 게시물을 수정하면 안되니까
            holder.postDelete.setVisibility(View.VISIBLE);
            holder.postUpdate.setVisibility(View.VISIBLE);
//            Log.d("mawang","PostAdapter onBindViewHolder - 이메일 일치");
        }else{
            holder.postDelete.setVisibility(View.GONE);
            holder.postUpdate.setVisibility(View.GONE);
            Log.d("mawang","PostAdapter onBindViewHolder - 이메일 불일치");
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<PostDTO> datas){
        this.items.addAll(0,datas);
        notifyDataSetChanged();
    }
    public void datasClear() { // 삭제 즉각 반영
        items.clear();
        notifyDataSetChanged();
        Log.d("mawang", "PostAdapter datasClear - called");
    }


    class ClickListener implements View.OnClickListener{
        PostHolder holder;
        int position;

        ClickListener(@NonNull PostHolder holder, int position){
            this.holder=holder;
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.post_delete:{
                    postDeleteDialog(position);

//                    Toast.makeText(mContext,"post_delete",Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.post_update:{ // soon
//                    Toast.makeText(mContext,"post_update 클릭",Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(mContext, PostUpdateActivity.class);
                    intent.putExtra("postID",items.get(position).get_id());
                    intent.putExtra("content",items.get(position).getContent());
                    mContext.startActivity(intent);

                    break;
                }
            }
        }
    }
    public void postDeleteDialog(int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("게시글을 삭제하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postDelete(position);
//                dialogInterface.cancel(); // 안해도 됨
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
    public void postDelete(int position){
        Log.d("mawang","PostAdapter postDelete - position:"+position);

        RetrofitApi.getService().postDelete(items.get(position).get_id()).enqueue(new retrofit2.Callback<RetrofitResponse>(){
            @Override
            public void onResponse(Call<RetrofitResponse> call, @NonNull Response<RetrofitResponse> response) {
                if(response.body().isRight()){
                    items.remove(position);
                    notifyItemRemoved(position); // refresh ?

                    Log.d("mawang","PostAdapter postDelete onResponse - response ="+response.body());
                    Log.d("mawang","PostAdapter postDelete onResponse - items.size ="+items.size());
                }
            }
            @Override
            public void onFailure(Call<RetrofitResponse> call, Throwable t) {
                Log.d("mawang","PostAdapter postDelete - onFailure = "+t.getMessage());
            }
        });
    }

    class PostHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        ImageView thumbnail;
        TextView userEmail;
        TextView time;
        TextView content;
        Button commentBtn;
        ConstraintLayout postObject;
        RecyclerView commentRV;
        CommentAdapter adapter;

        TextView postDelete;
        TextView postUpdate;

        PostHolder(View v){
            super(v);

            profileImage=(ImageView)v.findViewById(R.id.profile_image);
            thumbnail=(ImageView)v.findViewById(R.id.thumbnail);
            userEmail=(TextView)v.findViewById(R.id.userEmail);
            time=(TextView)v.findViewById(R.id.time);
            content=(TextView)v.findViewById(R.id.content);

            postObject = (ConstraintLayout)v.findViewById(R.id.post_object); // 게시물 전체 레이아웃
            commentRV = (RecyclerView)v.findViewById(R.id.comment_rv);
            commentBtn = (Button)v.findViewById(R.id.comment_btn);

            layoutManager = new LinearLayoutManager(mContext);
            commentRV.setLayoutManager(layoutManager);

            adapter = new CommentAdapter(mContext, mCallback);
            commentRV.setAdapter(adapter);

            postObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onPostObjectClick(items.get(getAdapterPosition()).get_id(), PostActivity.COMMENT);
                }
            });

            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(commentRV.getVisibility()== View.VISIBLE){
                        commentRV.setVisibility(View.GONE);
                        commentBtn.setText("댓글 보기");
                    }else{
                        commentRV.setVisibility(View.VISIBLE);
                        commentBtn.setText("댓글 닫기");
                    }
                }
            });

            postDelete=(TextView)v.findViewById(R.id.post_delete);
            postUpdate=(TextView)v.findViewById(R.id.post_update);

        }

    }

    public String getTime(String str){
//        Log.d("mawang","PostAdapter getTime - str: "+str);
        long now=Long.valueOf(str);
        Date date=new Date(now);
//        Log.d("mawang","PostAdapter getTime - date: "+date);
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd일");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }

    public void loadComment(CommentAdapter adapter, int post_id){

//        Log.d("mawang","PostAdapter loadComment - post_id:"+post_id);

        RetrofitApi.getService().getComment(post_id,COUNT,offset).enqueue(new retrofit2.Callback<ArrayList<CommentDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<CommentDTO>> call, Response<ArrayList<CommentDTO>> response) {
                if(response.body()!=null){
                    if(response.body()!=null){
                        ArrayList<CommentDTO> data = response.body();
                        adapter.clear();
                        adapter.addItems(data);
//                        Log.d("mawang","PostAdapter loadComment onResponse - response.body() :"+response.body());
//                        Log.d("mawang","PostAdapter loadComment onResponse - adapter.getItemCount() :"+adapter.getItemCount());
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<CommentDTO>> call, Throwable t) {
                Log.d("mawang","PostAdapter loadComment - onFailure:"+t.getMessage());
            }
        });
    }

}
