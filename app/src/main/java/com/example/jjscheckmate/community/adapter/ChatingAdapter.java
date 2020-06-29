package com.example.jjscheckmate.community.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.messageservice.MessageDTO;

import java.util.ArrayList;

public class ChatingAdapter extends RecyclerView.Adapter<ChatingAdapter.ChatingHolder>{

    private final ArrayList<MessageDTO> items;
    private Context mContext;
    private View itemView;

    public ChatingAdapter(Context context, ArrayList<MessageDTO> items){
        this.mContext=context;
        this.items=items;
    }

    @NonNull
    @Override
    public ChatingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_community_chating_item,parent,false);
        return new ChatingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatingHolder holder, int position) {
        MessageDTO msg=items.get(position);

        //자신의 메세지
        if(Session.getUserEmail().equals(msg.getUserEmail())){
            holder.rightView.setVisibility(View.GONE); // like kakao talk
//            holder.leftView.setVisibility(View.VISIBLE);

            holder.txtRightDate.setVisibility(View.INVISIBLE); // NVISIBLE 이 보기에 더 좋음
            //holder.txtLeftDate.setVisibility(View.VISIBLE); // 왼쪽에 날짜 , 이미지뷰에 가려짐
            holder.txtLeftDate.setText(msg.getDate()); // 이쪽은 날짜가 2줄로 나오네..

//            Log.d("mawang", "ChatingAdapter onBindViewHolder 1 - msg.getDate() :"+msg.getDate());

            holder.txtMessage.setText(msg.getMessage());
            holder.txtMessage.setBackgroundResource(R.drawable.outbox2);

            holder.txtUserEmail.setVisibility(View.INVISIBLE); // 이메일 hide , INVISIBLE 이 보기에 더 좋음
            holder.profileImage.setVisibility(View.INVISIBLE); // 프로필 hide , INVISIBLE 로 해야 유저쪽 날짜가 보임

//            Glide.with(holder.itemView.getContext())
//                    .load("https://i.pinimg.com/originals/b1/cf/92/b1cf921d73165843db30fa1c4333af8b.jpg")
//                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);


        }else{ //다른 사람의 메세지
//            holder.rightView.setVisibility(View.VISIBLE);
            holder.leftView.setVisibility(View.GONE);

            holder.txtLeftDate.setVisibility(View.GONE);
//            holder.txtRightDate.setVisibility(View.VISIBLE);
            holder.txtRightDate.setText(msg.getDate());
//            Log.d("mawang", "ChatingAdapter onBindViewHolder - msg.getDate() :"+msg.getDate()); // 보임

            holder.txtMessage.setText(msg.getMessage());
            holder.txtMessage.setBackgroundResource(R.drawable.inbox2);

            holder.txtUserEmail.setText(msg.getUserEmail().split("@")[0]);
//            holder.txtUserEmail.setText("다현"); // 닉네임 , 안보임
            //Log.d("mawang", "ChatingAdapter onBindViewHolder - msg.getUserEmail() :"+msg.getUserEmail()); // work

//            Glide.with(holder.itemView.getContext())
//                    .load(mContext.getString(R.string.baseUrl)+"user/profile/select/"+items.get(position).getUserEmail()+".jpg")
//                    .error(R.drawable.profile)
//                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);

//            holder.profileImage.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load("https://i.pinimg.com/originals/42/7d/44/427d44c440a86869d717de340c6bd57f.jpg")
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(MessageDTO msg){
        items.add(msg);
        notifyItemInserted(items.size()); // why ??
        //notifyDataSetChanged();
    }
    public void addAll(ArrayList<MessageDTO> msg){
        items.addAll(items.size(),msg);
        notifyDataSetChanged();
    }
    class ChatingHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView txtUserEmail;
        TextView txtMessage;
        TextView txtLeftDate;
        TextView txtRightDate;
        View leftView;
        View rightView;

        ChatingHolder(View itemView){
            super(itemView);
            profileImage=(ImageView)itemView.findViewById(R.id.profileImage);
            txtUserEmail=(TextView)itemView.findViewById(R.id.txtUserEmail);
            txtMessage=(TextView)itemView.findViewById(R.id.txtMessage);
            txtLeftDate=(TextView)itemView.findViewById(R.id.txtLeftDate);
            txtRightDate=(TextView)itemView.findViewById(R.id.txtRightDate);
            leftView=(View)itemView.findViewById(R.id.leftView);
            rightView=(View)itemView.findViewById(R.id.rightView);
        }

    }
}
