package com.example.jjscheckmate.community.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.activity.ChatingActivity;
import com.example.jjscheckmate.community.model.ChatRoomDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomHolder>{
    private final ArrayList<ChatRoomDTO> items;
    private Context mContext;
    private View itemView;

    private int orderIMG = 1;

    public ChatRoomAdapter(Context context, ArrayList<ChatRoomDTO> items){
        this.mContext=context;
        this.items=items;
    }

    @NonNull
    @Override
    public ChatRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_community_chatroom_item,parent,false);
        return new ChatRoomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomHolder holder, int position) {

//        Glide.with(holder.itemView.getContext())
//                .load(mContext.getString(R.string.baseUrl)+"user/profile/select/"+items.get(position).getUserEmails().get(0)+".jpg")
//                .error(R.drawable.profile)
//                .apply(new RequestOptions().circleCrop()).into(holder.chatRoomImage);

        // 임시 방편
        switch (orderIMG) {
            case 1: // 딤섬
                Glide.with(holder.itemView.getContext())
                .load("https://img.wkorea.com/w/2015/12/style_565fb2ba12451-1200x1027.jpg")
                .apply(new RequestOptions().circleCrop())
                        .into(holder.chatRoomImage);

                holder.lastReceivedMessage.setText("dimsom");

                orderIMG++;
                break;
            case 2: // 치즈케이크
                Glide.with(holder.itemView.getContext())
                .load("https://img.danawa.com/prod_img/500000/028/566/img/4566028_1.jpg?shrink=500:500&_v=20181023143311")
                .apply(new RequestOptions().circleCrop())
                        .into(holder.chatRoomImage);

                holder.lastReceivedMessage.setText("cheesCAKE");
                orderIMG++;
                break;
            default: // 채팅방 아이콘
                Glide.with(holder.itemView.getContext())
                .load("data:image/png;base64,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")
                .apply(new RequestOptions().circleCrop())
                        .into(holder.chatRoomImage);

                holder.lastReceivedMessage.setText("chat");

                orderIMG =1; //
                break;
        }


//        int len = items.get(position).getUserEmails().size()>2 ? 2 : items.get(position).getUserEmails().size();
//        int len = items.get(position).getUserEmails().size();
        int len = items.get(position).getUserNicknames().size();
//        Log.d("mawang", "ChatRoomAdapter onBindViewHolder - len = "+len);

        holder.userEmails.setText(""); // 한 번 비워주고
        // text view 길이로 '...' 붙여야겠다.
        if (len > 4){
            for(int i=0;i<4;i++){ // 4개까지만 표시하고
//                holder.userEmails.append(items.get(position).getUserEmails().get(i).split("@")[0]+",");
                holder.userEmails.append(items.get(position).getUserNicknames().get(i) +",");
            }
            holder.userEmails.append("..."); // 점 표시
        }else{ // 4개 이하면
            for(int i=0;i<len-1;i++){
//                holder.userEmails.append(items.get(position).getUserEmails().get(i).split("@")[0]+",");
                holder.userEmails.append(items.get(position).getUserNicknames().get(i) +",");
            }
//            holder.userEmails.append(items.get(position).getUserEmails().get(len-1).split("@")[0]);
            holder.userEmails.append(items.get(position).getUserNicknames().get(len-1)); // 마지막 참여자
        }



        holder.userCnt.setText("참여 "+items.get(position).getUserCnt()+"명");

        holder.time.setText(getTime(items.get(position).getTime()));
//        holder.time.setText("0.0.0.0");

//        holder.lastReceivedMessage.setText("지난 문자");

        itemView.setOnClickListener(new ClickListener(holder.getAdapterPosition()));

//        Log.d("mawang", "ChatRoomAdapter onBindViewHolder - holder.getAdapterPosition() = "+holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addData(ArrayList<ChatRoomDTO> data){
//        Log.d("mawang", "ChatRoomAdapter addData - items.size() = "+items.size());
//        Log.d("mawang", "ChatRoomAdapter addData - data.size() = "+data.size());

        //this.items.addAll(data); // ChatServiceActivity 에서
        // adapter=new ChatRoomAdapter(this,items); 로 연결되었기 때문에 하면 x2 가된다..

//        Log.d("mawang", "ChatRoomAdapter addData 2 - items.size() = "+items.size());
//        Log.d("mawang", "ChatRoomAdapter addData 2 - data.size() = "+data.size());

        notifyDataSetChanged();
    }

    public void datasClear() { // 삭제 즉각 반영
        items.clear();
        notifyDataSetChanged();
        Log.d("mawang", "ChatRoomAdapter datasClear - called");
    }

    class ChatRoomHolder extends RecyclerView.ViewHolder{
        ImageView chatRoomImage;
        TextView userEmails; // userNicknames
        TextView userCnt;
        TextView lastReceivedMessage;
        TextView time;

        ChatRoomHolder(View itemView){
            super(itemView);

            chatRoomImage=(ImageView)itemView.findViewById(R.id.chatRoom_image);
            userEmails=(TextView)itemView.findViewById(R.id.userEmails);
            userCnt=(TextView)itemView.findViewById(R.id.userCnt);
            time=(TextView)itemView.findViewById(R.id.date);
            lastReceivedMessage=(TextView)itemView.findViewById(R.id.lastReceivedMessage);

        }
    }
    class ClickListener implements View.OnClickListener{
        int pos;

        ClickListener(int pos){
            this.pos=pos;
        }

        @Override
        public void onClick(View v) {


//            Toast.makeText(mContext,"채팅방 시작",Toast.LENGTH_SHORT).show();

            // 채팅방 시작
            Intent intent=new Intent(mContext, ChatingActivity.class);
            intent.putExtra("roomKey",items.get(pos).getRoomKey());
            mContext.startActivity(intent);

            // 뒤로는 실행 안돼? 되네
//            Log.d("mawang", "ChatRoomAdapter ClickListener - onClick called #################");
        }
    }
    public String getTime(String str){
        //long now=Long.valueOf(str); // null 이다.

        //임시
        long now=System.currentTimeMillis();
        Date date=new Date(now);
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }

}
