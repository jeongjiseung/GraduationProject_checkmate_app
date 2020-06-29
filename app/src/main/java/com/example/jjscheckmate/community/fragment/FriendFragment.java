package com.example.jjscheckmate.community.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.adapter.InviteAdapter;
import com.example.jjscheckmate.community.model.FriendDTO;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class FriendFragment extends Fragment {
    public static final int FRIENDSTATEINVITE=-1;
    public static final int FRIENDSTATEREJECT=0;
    public static final int FRIENDSTATEACCEPT=1;

    RecyclerView recyclerView;

    InviteAdapter adapter;
    TextView tvFriendCnt;

//    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager layoutManager;

    private ArrayList<FriendDTO> datas;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        Log.d("mawang", "FriendFragment onCreateView - pass");

        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.community_fragment_friend,container,false);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        tvFriendCnt=(TextView)rootView.findViewById(R.id.tvFriendCnt);



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Log.d("mawang", "FriendFragment onActivityCreated - mContext = "+mContext);

        datas=new ArrayList<>();

//        layoutManager= new LinearLayoutManager(getContext());
        layoutManager= new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter=new InviteAdapter(getContext(),datas);
        recyclerView.setAdapter(adapter);

        getInviteList();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

//        Log.d("mawang", "FriendFragment onAttach - mContext = "+mContext);
    }

    public void getInviteList(){

        //초대 요청 만 가져오기  state -> -1
        //거절 목록  state -> 0
        //수락 목록 (진짜 친구목록)  state -> 1

        // state 는 현재 역할을 하고 있지는 않다.
        RetrofitApi.getService().getFriendList(Session.getUserEmail(),FRIENDSTATEINVITE).enqueue(new retrofit2.Callback<ArrayList<FriendDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<FriendDTO>> call, Response<ArrayList<FriendDTO>> response) {
                if(response.body()!=null){
                    datas=response.body();
                    tvFriendCnt.setText(datas.size()+"명");
                    adapter.addItems(datas);

//                    Log.d("mawang", "FriendFragment getInviteList - response = " + response);
                    Log.d("mawang", "FriendFragment getInviteList - response toString = " + response.toString());
//                    Log.d("mawang", "FriendFragment getInviteList - response.body() = " + response.body());
                    Log.d("mawang", "FriendFragment getInviteList - datas size= " + datas.size());

                }
            }
            @Override
            public void onFailure(Call<ArrayList<FriendDTO>> call, Throwable t) {
                Log.d("mawang", "FriendFragment getInviteList - onFailure ="+t.getMessage());
            }
        });
    }
}
