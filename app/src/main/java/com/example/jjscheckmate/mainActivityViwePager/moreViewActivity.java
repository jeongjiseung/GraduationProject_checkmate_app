package com.example.jjscheckmate.mainActivityViwePager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.UploadedSurveyDTO;
import com.example.jjscheckmate.UploadedSurveyRV;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.offlineform.FormItem;
import com.example.jjscheckmate.offlineform.OfflineFormRVAdapter;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;

public class moreViewActivity extends AppCompatActivity {
    private static final int SERVER_SURVEY=0;
    private static final int OFFLINE_SURVEY=1;
    private int type;
    private RecyclerView recyclerView;

    private UploadedSurveyRV surveysAdapter;
    private OfflineFormRVAdapter offlineFormAdapter;

    private ArrayList<UploadedSurveyDTO> serverSurveyDatas;
    private ArrayList<FormItem> offlineSurveyDatas;
    private RecyclerView.LayoutManager layoutManager;

//    public String userEmail;
//    private String url;

    private TextView toolbarTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_view);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbarTV = findViewById(R.id.toolbarTV);

        serverSurveyDatas=new ArrayList<>();
        offlineSurveyDatas=new ArrayList<>();

//        url=getString(R.string.baseUrl);

        Intent intent=getIntent();
//        userEmail=intent.getStringExtra("userEmail");
        type=intent.getIntExtra("type",-1);

        layoutManager=new LinearLayoutManager(this);

        surveysAdapter=new UploadedSurveyRV(this,serverSurveyDatas);
        offlineFormAdapter=new OfflineFormRVAdapter(this,offlineSurveyDatas);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,1));

        load(type); // 여기면 괜찮을려나
    }

    public void load(int type){
        switch (type){
            case SERVER_SURVEY:{
                recyclerView.setAdapter(surveysAdapter);
                toolbarTV.setText("응답중인 설문");

                getMySubmittedSurveyList();
                break; }
            case OFFLINE_SURVEY:{ // sq 라서 offline 이였구나
                recyclerView.setAdapter(offlineFormAdapter);
                toolbarTV.setText("작성중인 설문");
                getMyDraftedSurveyList();
                break;
            }
        }
    }

    public void getMySubmittedSurveyList(){

        RetrofitApi.getService().getSurveyList(Session.getUserEmail()).enqueue(new retrofit2.Callback<ArrayList<UploadedSurveyDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<UploadedSurveyDTO>> call, retrofit2.Response<ArrayList<UploadedSurveyDTO>> response) {
                if(response.isSuccessful()){
                    surveysAdapter.addDatas(response.body()); // 한꺼번에
                    // 여기는 2번해도 안터짐
//                    Log.d("mawang","moreViewActivity getMySurveyList - response.body = "+response.body());
                    //Log.d("mawang","moreViewActivity getMySurveyList - response.body.toString = "+response.body().toString()); // response.body() 와 동일
                    //Log.d("mawang","moreViewActivity getMySurveyList - response.code = "+response.code()); // status 200
                }else{
                    Log.d("mawang","moreViewActivity getMySubmittedSurveyList - response.code = "+response.code());
                }
            }
            @Override
            public void onFailure(retrofit2.Call<ArrayList<UploadedSurveyDTO>> call, Throwable t) {
                Log.d("mawang","moreViewActivity getMySubmittedSurveyList - onFailure ");
                t.getMessage();
                t.printStackTrace();
            }
        });
    }
    public void getMyDraftedSurveyList(){

        RetrofitApi.getService().getDraftSurveyList(Session.getUserEmail()).enqueue(new retrofit2.Callback<ArrayList<FormItem>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<FormItem>> call, retrofit2.Response<ArrayList<FormItem>> response) {
                if(response.isSuccessful()){
                    offlineFormAdapter.addItems(response.body());
                    // 여기는 2번해도 안터짐
//                    Log.d("mawang","moreViewActivity getMyDraftedSurveyList - response.body = "+response.body());
                    //Log.d("mawang","moreViewActivity getMySurveyList - response.body.toString = "+response.body().toString()); // response.body() 와 동일
                    //Log.d("mawang","moreViewActivity getMySurveyList - response.code = "+response.code()); // status 200
                }else{
                    Log.d("mawang","moreViewActivity getMyDraftedSurveyList - response.code = "+response.code());
                }
            }
            @Override
            public void onFailure(retrofit2.Call<ArrayList<FormItem>> call, Throwable t) {
                Log.d("mawang","moreViewActivity getMyDraftedSurveyList - onFailure ");
                t.getMessage();
                t.printStackTrace();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
