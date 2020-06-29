package com.example.jjscheckmate.community.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.activity.GroupCreateActivity;
import com.example.jjscheckmate.community.activity.MyGroupActivity;
import com.example.jjscheckmate.community.adapter.GroupAdapter;
import com.example.jjscheckmate.community.model.GroupDTO;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class GroupFragment extends Fragment {
    private final static Integer COUNT=20;
    Integer offset=0;

    Button btnGroupCreate;
    Button btnCategory;
    Button btnMyGroup;

    //TextView tvCategory;// for later
    RecyclerView recyclerView;
    GroupAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GroupDTO> datas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.community_fragment_group,container,false);
        btnGroupCreate=(Button)rootView.findViewById(R.id.btnGroupCreate);
        btnCategory=(Button)rootView.findViewById(R.id.btnCategory);
        btnMyGroup=(Button)rootView.findViewById(R.id.btnMyGroup);
        btnGroupCreate.setOnClickListener(new ClickListener());
        btnCategory.setOnClickListener(new ClickListener());
        btnMyGroup.setOnClickListener(new ClickListener());

        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        datas=new ArrayList<>();
        layoutManager= new LinearLayoutManager(getContext());
        adapter=new GroupAdapter(getContext(),datas);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

//        Log.d("mawang","GroupFragment onCreateView - 짠");

        return rootView;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.d("mawang","GroupFragment onActivityCreated - 짠");
        //findAllGroup(); // 위치를 바꾸자
//    }

    public void findAllGroup(){

        // 아직 가입하지 않은 그룹들을 불러오는구나
        RetrofitApi.getService().grouptGet(Session.getUserEmail(),COUNT,offset).enqueue(new retrofit2.Callback<ArrayList<GroupDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<GroupDTO>> call, Response<ArrayList<GroupDTO>> response) {
                if(response.body()!=null){
                    datas=response.body();
                    adapter.addItems(datas);

                    Log.d("mawang","GroupFragment findAllGroup - datas.size() = "+datas.size());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<GroupDTO>> call, Throwable t) {
                Log.d("mawang","GroupFragment findAllGroup - onFailure :"+t.getMessage());
            }
        });
    }

    public class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnGroupCreate:{
                    Intent intent=new Intent(getContext(), GroupCreateActivity.class); // ??
                    startActivity(intent);
//                    Toast.makeText(getContext(),"btnGroupCreate",Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.btnCategory:{
// 뭐하지?
                    Toast.makeText(getContext(),"btnCategory",Toast.LENGTH_SHORT).show();

                    break;
                }
                case R.id.btnMyGroup:{
                    Intent intent=new Intent(getContext(), MyGroupActivity.class); //??
                    startActivity(intent);
//                    Toast.makeText(getContext(),"btnMyGroup",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.d("mawang","GroupFragment onStart - 짠");
//    }

    @Override
    public void onResume() {
        super.onResume();

        datas.clear();
        adapter.datasClear();
        findAllGroup();
//        Log.d("mawang","GroupFragment onResume - 짠");
    }

}
