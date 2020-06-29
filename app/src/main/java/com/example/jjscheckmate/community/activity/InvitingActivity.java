package com.example.jjscheckmate.community.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.adapter.FriendAdapter;
import com.example.jjscheckmate.community.model.FriendDTO;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class InvitingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FriendAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FriendDTO> datas;

    private Toolbar toolbar;
    private Button btnChatRoomCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviting);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnChatRoomCreate = (Button) findViewById(R.id.btnChatRoomCreate);
        btnChatRoomCreate.setOnClickListener(new ClickListener());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        datas = new ArrayList<>();
        adapter = new FriendAdapter(InvitingActivity.this, datas, 1);

        getFriendList();
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
                    recyclerView.setAdapter(adapter);

//                    Log.d("mawang", "InvitingActivity getFriendList - response = " + response);
                    Log.d("mawang", "InvitingActivity getFriendList - response = " + response.toString());
//                    Log.d("mawang", "InvitingActivity getFriendList - response.body() = " + response.body());
                    Log.d("mawang", "InvitingActivity getFriendList - datas size = " + datas.size());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FriendDTO>> call, Throwable t) {
                Log.d("mawang", "InvitingActivity getFriendList - onFailure :"+t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnChatRoomCreate: {
                    chatRoomCreateRequest();
                    break;
                }
            }
        }
    }

    public void chatRoomCreateRequest() {

        HashMap<String, Object> input = new HashMap<>();
        input.put("userEmail", Session.getUserEmail());
        input.put("friends", adapter.getCheckedFriends());
        input.put("pk", Session.getUserEmail() + System.currentTimeMillis()); // 아하..
        Log.d("mawang", "InvitingActivity chatRoomCreateRequest - input = " + input);

        RetrofitApi.getService().chatRoomCreateRequest(input).enqueue(new retrofit2.Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d("mawang", "InvitingActivity chatRoomCreateRequest - onResponse");
                finish();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("mawang", "InvitingActivity chatRoomCreateRequest - onFailure :"+t.getMessage());
            }
        });
    }
}
