package com.example.jjscheckmate.community.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.activity.PostActivity;
import com.example.jjscheckmate.community.model.CommentReplyDTO;
import com.example.jjscheckmate.community.model.OnItemClick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.CommentReplyHolder>{
    private ArrayList<CommentReplyDTO> items;
    private Context mContext;
    private View itemView;
    private OnItemClick mCallback;

    private int orderIMG = 1;

    public CommentReplyAdapter(Context context, OnItemClick callback){
        this.mContext = context;
        this.items = new ArrayList<>();
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public CommentReplyAdapter.CommentReplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_community_comment_reply_item,parent,false);
        return new CommentReplyAdapter.CommentReplyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentReplyAdapter.CommentReplyHolder holder, int position) {
        if(items.get(position).getUserEmail()==null){
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.profile)
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }else{
            Glide.with(mContext).load(mContext.getString(R.string.baseUrl)+"user/profile/select/"+items.get(position).getUserEmail()) //+".jpg"
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }

        // 임시 방편
//        switch (orderIMG) {
//            case 1: // 레이디가가
//                Glide.with(mContext).load(R.drawable.tmp_profileimg)
//                        .into(holder.profileImage);
//                orderIMG++;
//                break;
//            case 2: // 메시가형이다
//                Glide.with(mContext).load("https://www.lkp.news/data/photos/20200414/art_15857300809078_405b93.jpg")
//                        .into(holder.profileImage);
//                orderIMG++;
//                break;
//            case 3: // 치달선수
//                Glide.with(mContext).load("https://img.hani.co.kr/imgdb/resize/2018/0527/00500095_20180527.JPG")
//                        .into(holder.profileImage);
//                orderIMG++;
//                break;
//            case 4: // 중거리는zd
//                Glide.with(mContext).load("https://image.fmkorea.com/files/attach/new/20191017/486616/1663616953/2288407512/829e22ee26828f958a8036755fd1d0e4.jpeg")
//                        .into(holder.profileImage);
//                orderIMG++;
//                break;
//            default:
//                orderIMG = 1;
//                break;
//        }


//        holder.userEmail.setText(items.get(position).getUserEmail().split("@")[0]);
        holder.userEmail.setText(items.get(position).getNickname());

        holder.time.setText(getTime(items.get(position).getTime()));

//        String targetEmailText = "@" + items.get(position).getTargetUserEmail().split("@")[0];
        String targetEmailText = "@" + items.get(position).getTarget_Nickname(); // 타겟 닉넴으로 바꾸어보자

        String contentText = targetEmailText + "   " + items.get(position).getContent(); // 3칸 공백
        SpannableString spannableString = new SpannableString(contentText);
        int start = contentText.indexOf(targetEmailText); // 태그 아이디 시작점
        int end = start + targetEmailText.length(); // 태그 아이디 마지막 인덱스
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6702")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.content.setText(spannableString);

        holder.replyImage.setImageResource(R.drawable.ic_enter_78501); // 대댓글 이미지
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<CommentReplyDTO> datas){
        this.items.addAll(datas);
        notifyDataSetChanged();
    }

    public void clear(){
        this.items.clear();
    }

    class CommentReplyHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        ImageView replyImage;
        TextView userEmail;
        TextView time;
        TextView content;

//        Button delButton;
TextView delButton;

        LinearLayout replyObject;

        CommentReplyHolder(View view){
            super(view);

            profileImage=(ImageView)view.findViewById(R.id.commentReply_profile);
            replyImage = (ImageView)view.findViewById(R.id.reply_image);
            userEmail=(TextView)view.findViewById(R.id.commentReply_id);
            time=(TextView)view.findViewById(R.id.commentReply_time);
            content=(TextView)view.findViewById(R.id.commentReply_text);
            delButton=view.findViewById(R.id.commentReply_del);
            replyObject = (LinearLayout)view.findViewById(R.id.commentReply_object);

            replyObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onPostObjectClick(items.get(getAdapterPosition()).getComment_id(), PostActivity.COMMENT_REPLY);
                    mCallback.setTargetUserEmail(items.get(getAdapterPosition()).getUserEmail());
                    mCallback.setTargetNickname(items.get(getAdapterPosition()).getNickname());
                }
            });

            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onCommentDelClick(items.get(getAdapterPosition()).get_id(), PostActivity.COMMENT_REPLY);
                    items.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }

    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd일");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
}
