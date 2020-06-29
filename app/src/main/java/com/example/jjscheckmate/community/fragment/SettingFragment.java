package com.example.jjscheckmate.community.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.adapter.FriendAdapter;
import com.example.jjscheckmate.community.model.FriendDTO;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {
    static final int PICK_FROM_ALBUM = 1;
    static final int SETTING_FRAGMENT_FRIEND_ADAPTER = 0;

    ImageView profileImage;
    ImageView imageAdd;
    TextView friendCount;
    TextView findFriend;

    RecyclerView recyclerView;
    FriendAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FriendDTO> datas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.community_fragment_setting, container, false);
        profileImage = (ImageView) rootView.findViewById(R.id.profile_image);
        imageAdd = (ImageView) rootView.findViewById(R.id.imageAdd);
        imageAdd.setOnClickListener(new ClickListener());

        friendCount = rootView.findViewById(R.id.tvFriendCnt);
        findFriend = rootView.findViewById(R.id.tvFindFriend);
        findFriend.setOnClickListener(new ClickListener());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        datas = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new FriendAdapter(getContext(), datas, SETTING_FRAGMENT_FRIEND_ADAPTER);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFriendList(); // refresh 할려면 어디서..?
    }

    @Override
    public void onStart() {
        super.onStart();

//        Log.d("mawang", "SettingFragment onStart - 프로필 변경?");
        // 왜 onstart 에 넣었을까?

//        Glide.with(getContext()).load(getContext().getString(R.string.baseUrl)+"user/profile/select/"+Session.getUserEmail()+".jpg")
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(profileImage);

//        Log.d("mawang", "SettingFragment onStart - 주소 :"+getContext().getString(R.string.baseUrl));
        // PNG,png 차이는 없다. 내쪽 노드서버로는 못 불러오나 보다 ..
//        Glide.with(getContext()).load(getContext().getString(R.string.baseUrl)+"save/profile/"+"newpro.png")
//                .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 패스
//                .skipMemoryCache(true) // 메모리 캐시 패스
//                .into(profileImage);

//        Glide.with(getContext()).load("http://imgnews.naver.net/image/011/2019/12/03/0003660668_002_20191203092805039.jpg") // 인터넷 uri 는? work
//                .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 패스
//                .skipMemoryCache(true) // 메모리 캐시 패스
//                .into(profileImage);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_ALBUM) {
            Uri fileUri = data.getData();
            Log.d("mawang", "SettingFragment onActivityResult - fileUri =" + fileUri);

            Glide.with(SettingFragment.this).load(fileUri).into(profileImage); // work


//            profileChange(getRealPathFromURI(fileUri));
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void profileChange(String imgPath) {

        File file = new File(imgPath);
//        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody fileReqBody = RequestBody.create(file, MediaType.parse("image/*"));
//        MultipartBody.Part part = MultipartBody.Part.createFormData(Session.getUserEmail(), file.getName(), fileReqBody);
        MultipartBody.Part part = MultipartBody.Part.createFormData(file.getName(), file.getName(), fileReqBody);

        RetrofitApi.getService().profileImageUpload(Session.getUserEmail(), part).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("mawang", "SettingFragment profileChange - onResponse");
//                Log.d("mawang", "SettingFragment profileChange - response.body() ="+response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("mawang", "SettingFragment profileChange -onFailure :" + t.getMessage());
            }
        });
    }

    public void getFriendList() {
        HashMap<String, Object> input = new HashMap<>();
        input.put("userEmail", Session.getUserEmail());

        RetrofitApi.getService().friendSelect(input).enqueue(new retrofit2.Callback<ArrayList<FriendDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<FriendDTO>> call, Response<ArrayList<FriendDTO>> response) {
                if (response.body() != null) {
                    datas = response.body();
                    adapter.addItems(datas);
                    adapter.notifyDataSetChanged(); // refresh ?

                    friendCount.setText("친구 " + datas.size() + "명");
//                    Log.d("mawang", "SettingFragment getFriendList - response.body() =" + response.body());
//                    Log.d("mawang", "SettingFragment getFriendList - datas size =" + datas.size());
//                    Log.d("mawang", "SettingFragment getFriendList - adapter.getItemCount =" + adapter.getItemCount());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FriendDTO>> call, Throwable t) {
                Log.d("mawang", "SettingFragment getFriendList -onFailure :" + t.getMessage());
            }
        });
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageAdd:
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);

//                    Snackbar.make(getView(),"imageAdd.",Snackbar.LENGTH_LONG).show();

                    break;

                case R.id.tvFindFriend:
                    Snackbar.make(getView(),"tvFindFriend.",Snackbar.LENGTH_LONG).show();
                    break;

            }
        }
    }



}