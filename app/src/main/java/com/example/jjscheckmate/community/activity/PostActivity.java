package com.example.jjscheckmate.community.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.adapter.PostAdapter;
import com.example.jjscheckmate.community.model.OnItemClick;
import com.example.jjscheckmate.community.model.PostDTO;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity implements View.OnClickListener, OnItemClick {
    public final static int COMMENT = 0;
    public final static int COMMENT_REPLY = 1;

    private final static Integer COUNT=20;
    Integer offset=0;
    Integer clickedPostObjectId = -1;
    Integer clickedPostObjectType = -1;
    String clickedTargetUserEmail = "";
    String clickedTargetUserNickname = "";
    private Integer groupID;

    ImageView cover;
    ImageView profileImage;
    TextView userEmail;
    EditText commentEditor;
    Button btnPost;
    Button btnComment;
    LinearLayout editLayout;
    Button content;
//    SwipeRefreshLayout swipeRefreshLayout;


    RecyclerView recyclerView;
    PostAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PostDTO> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        groupID=getIntent().getIntExtra("groupID",-1);
//        Log.d("mawang","PostActivity onCreate - groupID = "+groupID);

//        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                load();
//            }
//        });

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        datas=new ArrayList<>();
        layoutManager= new LinearLayoutManager(this);
        adapter=new PostAdapter(this,datas,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        cover=(ImageView)findViewById(R.id.cover);
        profileImage=(ImageView)findViewById(R.id.profile_image);
        userEmail=(TextView)findViewById(R.id.userEmail);

        btnComment=(Button)findViewById(R.id.post_commentBtn);
        btnComment.setOnClickListener(this);
        commentEditor = (EditText)findViewById(R.id.post_editComment);
        editLayout = (LinearLayout) findViewById(R.id.post_editLayout);

        content=(Button) findViewById(R.id.content);
        content.setOnClickListener(this);

        load();
    }

    public void load(){

        Glide.with(this).load(getString(R.string.baseUrl)+"group/image/cover/"+groupID)
                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(10)))
                .into(cover);

        Glide.with(this).load(getString(R.string.baseUrl)+"user/profile/select/"+ Session.getUserEmail())
                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(30)))
                .into(profileImage);


        RetrofitApi.getService().getPost(groupID,COUNT,offset).enqueue(new retrofit2.Callback<ArrayList<PostDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<PostDTO>> call, Response<ArrayList<PostDTO>> response) {
                if(response.body()!=null){
                    datas=response.body();
                    offset+=datas.size();
                    adapter.addItems(datas);

                    //                        Log.d("mawang","PostActivity load - response.body() = "+response.body());
                    Log.d("mawang","PostActivity load - datas.size() = "+datas.size());
//                        Log.d("mawang","PostActivity load - datas.get(0).getContent() = "+datas.get(0).getContent());
//                        Log.d("mawang","PostActivity load - offset = "+offset);

//                    swipeRefreshLayout.setRefreshing(false); // Whether or not the view should show refresh progress.
                }
            }
            @Override
            public void onFailure(Call<ArrayList<PostDTO>> call, Throwable t) {
                Log.d("mawang","PostActivity load - onFailure = "+t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.content:{
                Intent intent=new Intent(getApplicationContext(),PostCreateActivity.class);
                intent.putExtra("groupID",groupID);
                startActivity(intent);
//                Toast.makeText(this,"content",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.post_commentBtn:{
                commentCreate();
                commentEditor.setText("");
                editLayout.setVisibility(View.INVISIBLE); // GONE 으로 하면 안되나?

                InputMethodManager immhide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

//                Toast.makeText(this,"post_commentBtn 댓글등록",Toast.LENGTH_SHORT).show();
                Log.d("mawang","PostActivity onClick post_commentBtn- 댓글등록");
                break;
            }
        }
    }


    @Override
    public void onPostObjectClick(int _id, int type){
        // 게시물 클릭하면 열림

        EditText editText = (EditText) findViewById(R.id.post_editComment);
        editLayout.setVisibility(View.VISIBLE);
        editText.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        clickedPostObjectId = _id;
        clickedPostObjectType = type;

        Log.d("mawang","PostActivity onPostObjectClick - type = "+type);
        if(type == 0){
            Log.d("mawang","PostActivity onPostObjectClick - post_id = "+_id);
        }else if(type == 1){
            Log.d("mawang","PostActivity onPostObjectClick - comment_id = "+_id);
        }


    }

    @Override
    public void setTargetUserEmail(String target){ // 대댓글 용이구나
        clickedTargetUserEmail = target;
    }
    @Override
    public void setTargetNickname(String target){ // 대댓글 용이구나
        clickedTargetUserNickname = target;
    }

    @Override
    public void onCommentDelClick(int _id, int type){ // 댓글삭제
        Log.d("mawang","PostActivity onCommentDelClick - _id : "+_id);

        if(type == COMMENT){

            RetrofitApi.getService().deleteComment(_id).enqueue(new retrofit2.Callback<Boolean>(){
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    adapter.notifyDataSetChanged();
                    Log.d("mawang","PostActivity onCommentDelClick COMMENT - onResponse");
//                    Log.d("mawang","PostActivity onCommentDelClick COMMENT - response : "+response);
//                    Log.d("mawang","PostActivity onCommentDelClick COMMENT - response body: "+response.body());
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    // work well 인데 여기로 받아오네?
                    Log.d("mawang","PostActivity onCommentDelClick COMMENT - onFailure : "+t.getMessage());
                }
            });

        }

        if(type == COMMENT_REPLY){
            RetrofitApi.getService().deleteReply(_id).enqueue(new retrofit2.Callback<Boolean>(){
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    adapter.notifyDataSetChanged(); // 왜지?
                    Log.d("mawang","PostActivity onCommentDelClick COMMENT_REPLY - onResponse");
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d("mawang","PostActivity onCommentDelClick COMMENT_REPLY - onFailure : "+t.getMessage());
                }
            });
        }
    }

    public void commentCreate(){
        if(clickedPostObjectId == -1 || clickedPostObjectType == -1){
            Log.d("mawang","PostActivity commentCreate - 입구컷");
            Toast.makeText(this,"PostActivity commentCreate - 입구컷",Toast.LENGTH_SHORT).show();
            return;
        } // null 방지용 인가보다.

        if(clickedPostObjectType == COMMENT){

            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("post_id",clickedPostObjectId);
            hashMap.put("content",commentEditor.getText().toString());
            hashMap.put("userEmail",Session.getUserEmail());
            hashMap.put("time",System.currentTimeMillis());
            hashMap.put("Nickname",Session.getUserName());
            Log.d("mawang","PostActivity commentCreate COMMENT- hashMap :"+hashMap);

            RetrofitApi.getService().commentCreate(hashMap).enqueue(new retrofit2.Callback<Boolean>(){
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    adapter.notifyDataSetChanged(); // 댓글 바로 적용
                    Log.d("mawang","PostActivity commentCreate COMMENT- response.body :"+response.body());
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d("mawang","PostActivity commentCreate COMMENT- onFailure :"+t.getMessage());
                }
            });
        }

        if(clickedPostObjectType == COMMENT_REPLY){

            HashMap<String,Object> hashMap=new HashMap<>();
//            hashMap.put("post_id",clickedPostObjectId);
            hashMap.put("comment_id",clickedPostObjectId);
            hashMap.put("content",commentEditor.getText().toString());
            hashMap.put("target_userEmail",clickedTargetUserEmail);
            hashMap.put("userEmail",Session.getUserEmail());
            hashMap.put("time",System.currentTimeMillis());
            hashMap.put("target_Nickname",clickedTargetUserNickname);
            hashMap.put("Nickname",Session.getUserName());
            Log.d("mawang","PostActivity commentCreate COMMENT_REPLY- hashMap :"+hashMap);

            RetrofitApi.getService().replyCreate(hashMap).enqueue(new retrofit2.Callback<Boolean>(){
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    adapter.notifyDataSetChanged(); // 아 ~ 새로 고침역할을 해준다!
                    Log.d("mawang","PostActivity commentCreate COMMENT_REPLY - response :"+response);
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d("mawang","PostActivity commentCreate COMMENT_REPLY- onFailure :"+t.getMessage());
                }
            });
        }

        clickedPostObjectId = -1;
        clickedPostObjectType = -1;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        InputMethodManager immhide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onBackPressed() {
        if (editLayout.getVisibility()== View.VISIBLE) { // 닫아줘야 할 듯하다.
            editLayout.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        datas.clear();
        adapter.datasClear();
        Log.d("mawang","PostActivity onResume - called = "+datas.size());
        load();
    }
}
