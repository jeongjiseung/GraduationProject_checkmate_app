package com.example.jjscheckmate.community.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.fragment.SettingFragment;
import com.example.jjscheckmate.community.model.FriendDTO;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder> {
    private ArrayList<FriendDTO> items;
    private Context mContext;
    private int type;

    private static final int FRIEND_LIST = 0;//단순 친구목록 , 옆에 친구 삭제 버튼이 함께한다
    private static final int INVITE_LIST = 1;//친구 삭제버튼이 사라지고 채팅방초대 체크박스가 활성화 된다

    private View itemView;

    private int orderIMG = 1;

    public FriendAdapter(Context context, ArrayList<FriendDTO> items, int type) {
        this.mContext = context;
        this.items = items;
        this.type = type;
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_fragment_friend_item, parent, false);
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {

//        if (items.get(position).getProfileImageUrl() == null) {
//            Glide.with(mContext)
//                    .load(R.drawable.profile)
//                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
////            Log.d("mawang", "FriendAdapter onBindViewHolder - pass 1");
//        } else {
//            Glide.with(mContext)
//                    .load(mContext.getString(R.string.baseUrl)+"user/profile/select/"+items.get(position).getProfileImageUrl()+".jpg")
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .load(R.drawable.tmp_friendprofile)
//                    .apply(new RequestOptions().circleCrop()).into(holder.profileImage);
////            Log.d("mawang", "FriendAdapter onBindViewHolder - pass 2");
//        }


        // 임시 방편
        switch (orderIMG) {
            case 1: // 하르모니아 , 혜성
                Glide.with(mContext).load(R.drawable.tmp_friendprofile)
                        .apply(new RequestOptions().circleCrop())
                        .into(holder.profileImage);
                orderIMG++;
                break;
            case 2: // 데스노트 엘 , 지효
                Glide.with(mContext).load("https://lh3.googleusercontent.com/proxy/w8bho0DyxqOhUocG8fwH_vJhR0BqDlFhQFhecSfDGoK089iEx7uT2a1KiV2Xp0afD7kzS_klc9TrfxSCFrh7178N5KTauTAl17G_kc2VkeryMsyHfuOaPD2OIzRKr0IDxoI0hV3XlW2qARaohT2hS49YnWlfpd-su_6B")
                        .apply(new RequestOptions().circleCrop())
                        .into(holder.profileImage);
                orderIMG++;
                break;
            case 3: // 어쌔크리드
                Glide.with(mContext).load("https://ww.namu.la/s/9ba56e9b2b70c9e864edc1dc7467729c9f7fc4e324677df0bfba8b2b22bec7dd8da17c82d64ba81786c027cff61fe45424eac8272e8945f7cececf049c3a745f1f757c077a462ca8001142533864d2aa2e4eb12abe3ab73f5fcff35b8d05d755")
                        .apply(new RequestOptions().circleCrop())
                        .into(holder.profileImage);
                orderIMG++;
                break;
            default: // 꼬부기
                Glide.with(mContext).load("data:image/jpeg;base64,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")
                        .apply(new RequestOptions().circleCrop())
                        .into(holder.profileImage);

                break;

        }



//        holder.userEmail.setText(items.get(position).getUserEmail().split("@")[0]);
        holder.userEmail.setText(items.get(position).getUserEmail());

        if (type == FRIEND_LIST) {
            holder.chkInviteChatRoom.setVisibility(View.GONE);
            holder.btnFriendDelete.setOnClickListener(new ClickListener());

        } else if (type == INVITE_LIST) {
            holder.btnFriendDelete.setVisibility(View.GONE);
//            holder.chkInviteChatRoom.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    items.get(position).setSelected(holder.chkInviteChatRoom.isChecked());
//                }
//            });
            holder.chkInviteChatRoom.setOnClickListener(view ->items.get(position).setSelected(holder.chkInviteChatRoom.isChecked()));
        }

//        holder.btnFriendDelete.setOnClickListener(new ClickListener());
//        holder.chkInviteChatRoom.setOnClickListener(view ->items.get(position).setSelected(holder.chkInviteChatRoom.isChecked()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<FriendDTO> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<FriendDTO> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public JSONArray getCheckedFriends() {
        ArrayList<String> friends = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                jsonArray.put(items.get(i).getUserEmail());
            }
        }
        return jsonArray;
    }

    class FriendHolder extends RecyclerView.ViewHolder {
        TextView userEmail;
        ImageView profileImage;
        Button btnFriendDelete;
        CheckBox chkInviteChatRoom;

        FriendHolder(View itemView) {
            super(itemView);
            profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            userEmail = (TextView) itemView.findViewById(R.id.userEmail);
            btnFriendDelete = (Button) itemView.findViewById(R.id.btnFriendDelete);
            chkInviteChatRoom = (CheckBox) itemView.findViewById(R.id.chkInviteChatRoom);
        }

    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnFriendDelete:
                    Snackbar.make(itemView, "btnFriendDelete.", Snackbar.LENGTH_LONG).show();
                    break;

            }
        }
    }
}
