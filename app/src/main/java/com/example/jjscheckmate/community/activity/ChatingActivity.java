package com.example.jjscheckmate.community.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.adapter.ChatingAdapter;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.messageservice.MessageDTO;
import com.example.jjscheckmate.messageservice.MessageManager;
import com.example.jjscheckmate.messageservice.MessageSaveManager;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class ChatingActivity extends AppCompatActivity {

    private static final int COUNT=50;
    private int offset = 0;

    String roomKey;
    String msg;

    Button btnSendfile; // not yet
    EditText editSendMessage;
    Button btnSend;

    RecyclerView recyclerView;
    ChatingAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MessageDTO> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_chating);

//        registerReceiver(receiver,new IntentFilter("com.example.RECEIVE_ACTION"));

        items=new ArrayList<>();

        Intent intent=getIntent();
        roomKey=intent.getStringExtra("roomKey");
//        Log.d("mawang", "ChatingActivity onCreate - roomKeys :"+roomKey);

        btnSendfile=(Button)findViewById(R.id.fileSendBtn);
        btnSendfile.setOnClickListener(new ClickListener());
        editSendMessage=(EditText)findViewById(R.id.editSendMessage);
        btnSend=(Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new ClickListener());


        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addOnScrollListener(new ScrollListener());
        adapter=new ChatingAdapter(this,items);
        recyclerView.setAdapter(adapter);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        getMessageCache(roomKey,offset);
//    }
    @Override
    protected void onResume() {
        super.onResume();

        // onResume 에서 하면 화면 나갔다 들어오면 refresh 된다.
//        getMessageCache(roomKey,offset);
        Log.d("mawang", "ChatingActivity onResume - called");
        getRoomMessages(roomKey,offset);
    }

    //로컬 캐시
//    public void getMessageCache(String roomKey,int offset){
//        ArrayList<MessageDTO> msg = MessageSaveManager.getInstance(this).findRoomMessage(roomKey, offset);
//        adapter.addAll(msg);
//        Log.d("mawang", "ChatingActivity getMessageCache - msg.size() :"+msg.size());
//
//        getRoomMessages(roomKey,msg.size());
//    }

    public void getRoomMessages(String roomKey,int offset){

        RetrofitApi.getService().getRoomMessages(roomKey,COUNT,offset).enqueue(new retrofit2.Callback<ArrayList<MessageDTO>>(){

            @Override
            public void onResponse(Call<ArrayList<MessageDTO>> call, Response<ArrayList<MessageDTO>> response) {

                if(response.body()!=null){
                    ArrayList<MessageDTO> msg=response.body();
                    adapter.addAll(msg);
                    Log.d("mawang", "ChatingActivity getRoomMessages - msg.size() :"+msg.size());

//                    MessageSaveManager.getInstance(ChatingActivity.this).insertAll(msg);
                    //recyclerView.scrollToPosition(adapter.getItemCount()-1); // 바로 call 하는것 같은데
                }else{

                }
            }
            @Override
            public void onFailure(Call<ArrayList<MessageDTO>> call, Throwable t) {
                Log.d("mawang", "ChatingActivity getRoomMessages - onFailure :"+t.getMessage());
            }
        });
    }

//    BroadcastReceiver receiver=new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            MessageDTO msg=(MessageDTO)intent.getSerializableExtra("msg");
//            if(roomKey.equals(msg.getRoomKey())){
//                adapter.addItem(msg);
//                recyclerView.scrollToPosition(adapter.getItemCount()-1);
//            }
//        }
//    };

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fileSendBtn:


                    Toast.makeText(getApplicationContext(),"파일전송",Toast.LENGTH_SHORT).show();
                    Log.d("mawang", "ChatingActivity ClickListener - fileSendBtn clicked ");

                    break;

                case R.id.btnSend:

                    MessageDTO msg = new MessageDTO(roomKey
                            , Session.getUserEmail()
                            , editSendMessage.getText().toString()
                            , Session.getTime());

                    MessageManager.msgSend(msg);

                    adapter.addItem(msg);

                    //recyclerView.scrollToPosition(adapter.getItemCount() - 1); // 아래로 한 칸 내려가는거? 아하
                    editSendMessage.setText("");

                    Log.d("mawang", "ChatingActivity ClickListener - btnSend clicked ");

                    break;
            }
        }
    }

//    public class ScrollListener extends RecyclerView.OnScrollListener{
//        @Override
//        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
//            super.onScrolled(recyclerView, dx, dy);
//
//            int firstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
//            int lastVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//            Log.d("mawang", "ChatingActivity ScrollListener - firstVisiblePosition = "+firstVisiblePosition);
//            Log.d("mawang", "ChatingActivity ScrollListener - lastVisiblePosition = "+lastVisiblePosition);
//
//            if(lastVisiblePosition==adapter.getItemCount()-1){
//               offset+=50; // 50개씩?
////               getRoomMessages(roomKey,offset);
//                Log.d("mawang", "ChatingActivity ScrollListener @@- offset = "+offset);
//            }
//
//        }
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(receiver);
//    }

}
