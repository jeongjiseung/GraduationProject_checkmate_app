package com.example.jjscheckmate.community.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.adapter.ChatRoomAdapter;
import com.example.jjscheckmate.community.model.ChatRoomDTO;
import com.example.jjscheckmate.community.model.ChatRoomTempDTO;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.messageservice.MessageManager;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

public class ChatServiceActivity extends AppCompatActivity {

    private Button btnChatRoomCreate;
    private Toolbar toolbar;

    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    ChatRoomAdapter adapter;
    private ArrayList<ChatRoomDTO> items;
    private HashMap<String,ArrayList<String>> rooms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_service);

        //채팅 서버와 연결 테스트
//        MessageManager.getInstance(this).connect();
        rooms=new HashMap<>();
        items=new ArrayList<>();

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnChatRoomCreate=(Button)findViewById(R.id.btnChatRoomCreate);
        btnChatRoomCreate.setOnClickListener(new ClickListener());

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ChatRoomAdapter(this,items);
        recyclerView.setAdapter(adapter);

    }

    class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnChatRoomCreate:{

                    Intent intent =new Intent(ChatServiceActivity.this,InvitingActivity.class);
                    startActivity(intent);

//                    Toast.makeText(getApplicationContext(),"btnChatRoomCreate",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    public void getChatRoomList(){

        String userEmail= Session.getUserEmail();

        RetrofitApi.getService().getRoomList(userEmail).enqueue(new retrofit2.Callback<ArrayList<ChatRoomTempDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<ChatRoomTempDTO>> call, Response<ArrayList<ChatRoomTempDTO>> response) {

                if(response.body()!=null){
                    ArrayList<ChatRoomTempDTO> res = response.body();
//                    ArrayList<ChatRoomDTO> datas = new ArrayList<>(); // 여기가 맞나?
                    Log.d("mawang", "ChatServiceActivity getChatRoomList - res.size() = "+res.size());

                    for(int i=0;i<res.size();i++){

                        if(rooms.containsKey(res.get(i).getRoomKey())){
                            // 채팅방에 참여자 추가
//                            rooms.get(res.get(i).getRoomKey()).add(res.get(i).getUserEmail());
                            rooms.get(res.get(i).getRoomKey()).add(res.get(i).getUserNickname());
                        }else{
//                            ArrayList<String> userEmails=new ArrayList<>();
//                            userEmails.add(res.get(i).getUserEmail());
                            ArrayList<String> userNicks=new ArrayList<>();
                            userNicks.add(res.get(i).getUserNickname());

                            // 채팅방 개설 ,채팅방 방장 추가
                            rooms.put(res.get(i).getRoomKey(),userNicks);
                        }
                    }

//                    Log.d("mawang", "ChatServiceActivity getChatRoomList - rooms = "+rooms); // HashMap

                    Set<String> roomKeys=rooms.keySet();
//                    Log.d("mawang", "ChatServiceActivity getChatRoomList - roomKeys = "+roomKeys);

                    for(String roomKey:roomKeys){
                        ChatRoomDTO room=new ChatRoomDTO();
                        room.setRoomKey(roomKey);
                        room.setUserCnt(String.valueOf(rooms.get(roomKey).size())); // ArrayList , 참여자수

//                        room.setUserEmails(rooms.get(roomKey)); // ArrayList , 참여자들 메일
                        room.setUserNicknames(rooms.get(roomKey)); // ArrayList , 참여자들 닉네임

                        room.setChatRoomImageUrl(rooms.get(roomKey).get(0)); // 사진 파일 경로가 이메일이다.
                        // 시간과
                        // lastReceivedMessage 가 아직 미구현

                        //datas.add(room);
                        items.add(room);

                    }
//                    adapter.addData(datas);
                    adapter.addData(items);

                }
            }
            @Override
            public void onFailure(Call<ArrayList<ChatRoomTempDTO>> call, Throwable t) {
                Log.d("mawang", "ChatServiceActivity getChatRoomList - onFailure = "+t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

//        Log.d("mawang","ChatServiceActivity onResume - 짠");
        items.clear();
        rooms.clear();
        adapter.datasClear();


        getChatRoomList(); // 채팅방 불러오기
//        Log.d("mawang", "ChatServiceActivity onResume - called");
    }


}
