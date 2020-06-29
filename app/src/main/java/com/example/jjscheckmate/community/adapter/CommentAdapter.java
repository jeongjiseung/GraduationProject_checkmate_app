package com.example.jjscheckmate.community.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.activity.PostActivity;
import com.example.jjscheckmate.community.model.CommentDTO;
import com.example.jjscheckmate.community.model.CommentReplyDTO;
import com.example.jjscheckmate.community.model.OnItemClick;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private ArrayList<CommentDTO> items;
    private Context mContext;
    private View itemView;
//    private boolean isDeleted = false;
    private OnItemClick mCallback;

    private final static int COUNT = 5;
    private RecyclerView.LayoutManager layoutManager;
    Integer offset=0;

    private int orderIMG = 1;

    public CommentAdapter(Context context, OnItemClick callback){
        this.mContext = context;
        this.items = new ArrayList<>();
        this.mCallback = callback; // PostActivity 에서부터 넘어왔다.
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_community_comment_item,parent,false);
        return new CommentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {
//        if(items.get(position).getUserEmail()==null){
//            Glide.with(holder.itemView.getContext())
//                    .load(R.drawable.profile)
//                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
//        }else{
//            Glide.with(mContext).load(mContext.getString(R.string.baseUrl)+"user/profile/select/"+items.get(position).getUserEmail()+".jpg")
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
//        }

        // 임시 방편
        switch (orderIMG) {
            case 1: // 레이디가가
                Glide.with(mContext).load(R.drawable.tmp_profileimg)
                        .into(holder.profileImage);
//                orderIMG++;
                break;
//            case 2: // 마라도나
//                Glide.with(mContext).load("https://lh3.googleusercontent.com/proxy/CGK2CUrv8H8uqI_A3QjKlHvCetMinzbWvmL-U2YryR2btELrenOsYV3ug8N65QyMoTCEhP401w8y5UwSHntIrx-tj3Jtac18cdaAg2oI7ba7suXG19TW7HV6Wlk4AhaxWDqmD10CyjCDaezBqcnXm-yJSCC6tkM")
//                        .into(holder.profileImage);
//                orderIMG++;
//                break;
//           default:
//                orderIMG = 1;
//                break;
        }



        //        holder.userEmail.setText(items.get(position).getUserEmail().split("@")[0]);
        holder.userEmail.setText(items.get(position).getNickname());

        holder.time.setText(getTime(items.get(position).getTime()));
        holder.content.setText(items.get(position).getContent());

        loadCommentReply(holder.adapter, items.get(holder.getAdapterPosition()).get_id());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<CommentDTO> datas){
        this.items.addAll(datas);
        notifyDataSetChanged();
    }

    public void clear(){
        this.items.clear();
    }

    class CommentHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView userEmail;
        TextView time;
        TextView content;

//        Button delButton;
        TextView delButton;

        RecyclerView replyRV;
        CommentReplyAdapter adapter;

        LinearLayout commentObject;

        CommentHolder(View view){
            super(view);

            profileImage=(ImageView)view.findViewById(R.id.comment_profile);
//            Glide.with(mContext).load(Session.getUserImage()).into(profileImage); // 구글 프사로 하자

            userEmail=(TextView)view.findViewById(R.id.comment_id);
            time=(TextView)view.findViewById(R.id.comment_time);
            content=(TextView)view.findViewById(R.id.comment_text);
            delButton=view.findViewById(R.id.comment_del);
            commentObject = (LinearLayout)view.findViewById(R.id.comment_object);
            replyRV = (RecyclerView)view.findViewById(R.id.commentReply_rv);

            layoutManager = new LinearLayoutManager(mContext);
            replyRV.setLayoutManager(layoutManager);

            adapter = new CommentReplyAdapter(mContext, mCallback);
            replyRV.setAdapter(adapter);

            commentObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onPostObjectClick(items.get(getAdapterPosition()).get_id(), PostActivity.COMMENT_REPLY);
                    mCallback.setTargetUserEmail(items.get(getAdapterPosition()).getUserEmail());
                    mCallback.setTargetNickname(items.get(getAdapterPosition()).getNickname());
//                    Toast.makeText(mContext, "대댓글", Toast.LENGTH_LONG).show();
                    Log.d("mawang","CommentAdapter CommentHolder commentObject - getAdapterPosition(): "+getAdapterPosition());
                }
            });

            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("mawang","CommentAdapter CommentHolder delButton - clicked");

                    mCallback.onCommentDelClick(items.get(getAdapterPosition()).get_id(), PostActivity.COMMENT);
                    items.remove(getAdapterPosition());
                    notifyDataSetChanged();

//                    Toast.makeText(mContext, "댓삭", Toast.LENGTH_LONG).show();

                }
            });

        }
    }

    public String getTime(String str){
//        Log.d("mawang","CommentAdapter getTime - str: "+str);
        long now=Long.valueOf(str);
        Date date=new Date(now);
//        Log.d("mawang","CommentAdapter getTime - date: "+date);
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd일");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }

    public void loadCommentReply(CommentReplyAdapter adapter, int comment_id){ //int post_id)
        Log.d("mawang","CommentAdapter loadCommentReply - comment_id :"+comment_id);

        RetrofitApi.getService().getReply(comment_id,COUNT,offset).enqueue(new retrofit2.Callback<ArrayList<CommentReplyDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<CommentReplyDTO>> call, Response<ArrayList<CommentReplyDTO>> response) {
//                if(response.body()!=null){ } // 왜 이중으로?
                    if(response.body()!=null){
                        ArrayList<CommentReplyDTO> data = response.body();
                        adapter.clear();
                        adapter.addItems(data);
                        Log.d("mawang","CommentAdapter loadCommentReply - onResponse ");
//                        Log.d("mawang","CommentAdapter loadCommentReply onResponse - response.body() ="+response.body());
                    }

            }
            @Override
            public void onFailure(Call<ArrayList<CommentReplyDTO>> call, Throwable t) {
                Log.d("mawang", "CommentAdapter loadCommentReply - onFailure :" + t.getMessage());
            }
        });
    }

}
