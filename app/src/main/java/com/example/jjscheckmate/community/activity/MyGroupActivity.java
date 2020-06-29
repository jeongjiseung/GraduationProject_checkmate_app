package com.example.jjscheckmate.community.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.adapter.MyGroupAdapter;
import com.example.jjscheckmate.community.model.GroupDTO;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class MyGroupActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyGroupAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GroupDTO> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        datas=new ArrayList<>();
        layoutManager= new LinearLayoutManager(this);
        adapter=new MyGroupAdapter(this,datas);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getMyGroup();
    }

    public void getMyGroup(){
        RetrofitApi.getService().getMyGroup(Session.getUserEmail()).enqueue(new retrofit2.Callback<ArrayList<GroupDTO>>(){

            @Override
            public void onResponse(Call<ArrayList<GroupDTO>> call, Response<ArrayList<GroupDTO>> response) {
                if(response.body()!=null){
                    datas=response.body();
                    adapter.addItems(datas);

//                    Log.d("mawang","MyGroupActivity getMyGroup - datas.size() = "+datas.size());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<GroupDTO>> call, Throwable t) {
                Log.d("mawang","MyGroupActivity getMyGroup - onFailure :"+t.getMessage());
            }
        });
    }
}
