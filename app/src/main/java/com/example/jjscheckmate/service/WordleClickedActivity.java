package com.example.jjscheckmate.service;

// WordCloud 에 출력된 키워드를 클릭 시 WordleClickedActivity 가 실행되도록 합니다.
// 제목에 키워드가 포함된 설문조사의 목록을 출력하도록 합니다.
// 설문조사를 클릭 시 설문 결과를 표시하도록 합니다.

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.mainActivityViwePager.SurveyDTO;
import com.example.jjscheckmate.mainActivityViwePager.SurveyRV;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;

public class WordleClickedActivity extends AppCompatActivity {

    private String title;
    private RecyclerView recyclerView;
    private SurveyRV surveyRV;
    private ArrayList<SurveyDTO> datas;
//    private boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordle_clicked);

        datas = new ArrayList<>();
        Intent intent = getIntent();

        surveyRV = new SurveyRV(getBaseContext(), datas);
        recyclerView = (RecyclerView) findViewById(R.id.wordle_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), 1));
        recyclerView.setAdapter(surveyRV);

        title = intent.getStringExtra("Keyword"); // args 가 온다.
        Log.d("mawang","WordleClickedActivity onCreate - title = "+title);

        androidx.appcompat.widget.Toolbar tb = (Toolbar) findViewById(R.id.wordleClicked_toolbar);
        tb.setTitle(title); // 제목 바꾸고

        getKeywordSurveyList(title);
    }

    public void getKeywordSurveyList(String keyword) {

        RetrofitApi.getService().getKeywordSurveyList(keyword).enqueue(new retrofit2.Callback<ArrayList<SurveyDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<SurveyDTO>> call, retrofit2.Response<ArrayList<SurveyDTO>> response) {
//                isFinished = true;
                if (response.isSuccessful()) {
                    surveyRV.addDatas(response.body());

//                    Log.d("mawang", "WordleClickedActivity getKeywordSurveyList - response = " + response);
//                    Log.d("mawang", "WordleClickedActivity getKeywordSurveyList - response.body() = " + response.body());
//                    Log.d("mawang", "WordleClickedActivity getKeywordSurveyList - surveyRV getDatas = " + surveyRV.getDatas());


                    Log.d("mawang", "WordleClickedActivity getKeywordSurveyList - 성공");
                } else {
                    Log.d("mawang", "WordleClickedActivity getKeywordSurveyList - 실패");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<SurveyDTO>> call, Throwable t) {
                Log.d("mawang", "WordleClickedActivity getKeywordSurveyList - onFailure:"+t.getMessage());
            }
        });
    }
}
