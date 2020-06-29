package com.example.jjscheckmate.community.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.fragment.FriendFragment;
import com.example.jjscheckmate.community.model.FriendDTO;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.InviteHolder> {
    private ArrayList<FriendDTO> items=new ArrayList<>();
    private Context mContext;
    private View itemView;

    private int orderIMG = 1;

    public InviteAdapter(Context context, ArrayList<FriendDTO> items){
        this.mContext=context;
        this.items=items;
    }

    @NonNull
    @Override
    public InviteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_fragment_friend_request_item,parent,false);
        return new InviteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteHolder holder, int position) {

        if(items.get(position).getProfileImageUrl()==null){
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.profile)
                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
//            Log.d("mawang", "InviteAdapter onBindViewHolder - pass 1");

        }else{
//            Glide.with(holder.itemView.getContext())
//                    .load(mContext.getString(R.string.baseUrl)+"user/profile/select/"+items.get(position).getProfileImageUrl()+".jpg")
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);


            switch (orderIMG) {
                case 1: // 대학일기
                    Glide.with(holder.itemView.getContext())
                            .load("https://pbs.twimg.com/media/DZ3I8wRU8AAQ7GU.jpg")
                            .apply(new RequestOptions().circleCrop())
                            .into(holder.profileImage);
                    orderIMG++;
                    break;
                case 2: // 피카츄
                    Glide.with(holder.itemView.getContext())
                            .load("https://lh3.googleusercontent.com/proxy/bVuEOpdWYtQhNI_yeCFYlAyq4jPzECXPFRBVGfTfV0doYN10U5L5kjWOwDQdyl0WmieeVjAz-l7r6ZrolQ2F_yibcdbD2Bqy_uFYuYK_UdyW_GlBtgw5")
                            .apply(new RequestOptions().circleCrop())
                            .into(holder.profileImage);

                    orderIMG++;
                    break;
                case 3: // 도라에몽
                    Glide.with(holder.itemView.getContext())
                            .load("https://i.pinimg.com/originals/2b/53/aa/2b53aa45b761b66b554cdf52270f0be9.jpg")
                            .apply(new RequestOptions().circleCrop())
                            .into(holder.profileImage);
                    orderIMG++;
                    break;
                case 4: // 사이타마
                    Glide.with(holder.itemView.getContext())
                            .load("https://pbs.twimg.com/profile_images/675485041539940352/yyhgNf_M_400x400.jpg")
                            .apply(new RequestOptions().circleCrop())
                            .into(holder.profileImage);
                    orderIMG++;
                    break;
                default: // 아이돌

                    break;
            }

//            Log.d("mawang", "InviteAdapter onBindViewHolder - pass 2");
        }

        holder.btnGrant.setOnClickListener(new InviteAdapter.ClickListener(holder,items.get(position).getUserEmail()));
        holder.btnReject.setOnClickListener(new InviteAdapter.ClickListener(holder,items.get(position).getUserEmail()));

//        holder.userEmail.setText(items.get(position).getUserEmail().split("@")[0]);
//        holder.userEmail.setText(items.get(position).getUserEmail());
        holder.userEmail.setText(items.get(position).getUserNickname()); // 닉네임

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<FriendDTO> items){
        this.items=items;
        notifyDataSetChanged();
    }
    public void addItems(ArrayList<FriendDTO> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    class InviteHolder extends RecyclerView.ViewHolder{
        TextView userEmail;
        ImageView profileImage;
        Button btnGrant;
        Button btnReject;
//        TextView requestTime;

        InviteHolder(View itemView){
            super(itemView);

            profileImage=(ImageView)itemView.findViewById(R.id.profile_image);
            userEmail=(TextView)itemView.findViewById(R.id.userEmail);
            btnGrant=(Button)itemView.findViewById(R.id.btnGrant);
            btnReject=(Button)itemView.findViewById(R.id.btnReject);
//            requestTime=(TextView)itemView.findViewById(R.id.requestTime);
        }
    }

    class ClickListener implements View.OnClickListener{
        String sender;
        InviteHolder holder;

        ClickListener(@NonNull InviteHolder holder,String sender){
            this.sender=sender;
            this.holder=holder;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnGrant:{
                    update(FriendFragment.FRIENDSTATEACCEPT);
                    holder.userEmail.append(" 님과 친구가 되었습니다.");

                    // 중복 클릭 방지
                    holder.btnGrant.setVisibility(View.GONE);
                    holder.btnReject.setVisibility(View.GONE);
                    break;
                }
                case R.id.btnReject:{
                    update(FriendFragment.FRIENDSTATEREJECT);
                    holder.userEmail.append(" 님을 거절했습니다.");

                    // 중복 클릭 방지
                    holder.btnGrant.setVisibility(View.GONE);
                    holder.btnReject.setVisibility(View.GONE);
                    break;
                }
            }
        }

        void update(int state){

            HashMap<String, Object> input = new HashMap<>();
            input.put("sender", sender);
            input.put("receiver",Session.getUserEmail());
            input.put("state",state);
            Log.d("mawang", "InviteAdapter update - input :"+input);

            RetrofitApi.getService().friendUpdate(input).enqueue(new retrofit2.Callback<Boolean>(){
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Log.d("mawang", "InviteAdapter update - onResponse");
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d("mawang", "InviteAdapter update - onFailure :"+t.getMessage());
                }
            });

        }
    }

}
