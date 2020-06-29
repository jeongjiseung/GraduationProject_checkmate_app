package com.example.jjscheckmate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.jjscheckmate.form.FormActivity;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.result.ResultActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class reserve_UploadedFormEditableActivity extends AppCompatActivity {
    private int form_id;
    private OkHttpClient client;

    private ImageView imgSurveyWriterPhoto;
    private TextView tvSurveyWriterEmail;
    private TextView tvSurveyRoomTitle;
    private String SurveyRoomTitle;

    public static final int categoryNumber = 100; // 숫자는 아무의미 없음, 그냥 정한거


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_uploaded_form_editable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        form_id = intent.getIntExtra("form_id", -1);
        SurveyRoomTitle = intent.getStringExtra("title");


        client = new OkHttpClient();

//        mStatusSwitch = (Switch) findViewById(R.id.status_switch); // true 상태로시작
//        mStatusSwitch.setChecked(true);
//        txtStatus = (TextView) findViewById(R.id.txtStatus);
//        txtStatus.setTextColor(Color.GREEN);
//        txtShare = findViewById(R.id.btnShareText);

        imgSurveyWriterPhoto = findViewById(R.id.survey_writer_photo);
        Glide.with(this).load(Session.getUserImage()).into(imgSurveyWriterPhoto);
        tvSurveyWriterEmail = findViewById(R.id.survey_writer_email);
        tvSurveyWriterEmail.setText(Session.getUserEmail());
        tvSurveyRoomTitle = findViewById(R.id.survey_room_title);
        tvSurveyRoomTitle.setText(SurveyRoomTitle);




    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnResult: {
                resultRequest();
//                Toast.makeText(getApplicationContext(),"btnResult",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btnShare: {
                shareRequest();

                break;
            }
            case R.id.btnEdit: {
                editRequest();
//                Toast.makeText(getApplicationContext(),"btnEdit",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btnPreview: {
                previewRequest();
//                Toast.makeText(getApplicationContext(),"btnPreview",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btnDelete: {
                deleteRequest();
//                Toast.makeText(getApplicationContext(),"btnDelete",Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    public void editRequest() {
//        Log.d("mawang", "reserve_UploadedFormEditableActivity editRequest 실행");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(getString(R.string.baseUrl) + "load/" + form_id) // load 폴더
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "reserve_UploadedFormEditableActivity editRequest Error = " + e.toString());
                Log.d("mawang", "reserve_UploadedFormEditableActivity editRequest Error = " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
//                Log.d("mawang", "reserve_UploadedFormEditableActivity editRequest - res = " + res);

                if (res.equals("load error")) {
                    // node 에서 error 문자열을 보냄
                    Toast.makeText(getApplicationContext(), "요청 실패", Toast.LENGTH_SHORT).show();
                } else {
                    // 받아온 데이터를 토대로 뛰우기
                    Intent intent = new Intent(reserve_UploadedFormEditableActivity.this, FormActivity.class);
                    intent.putExtra("form_id", form_id);
                    intent.putExtra("json", res); // item datas
                    intent.putExtra("category", categoryNumber);
                    startActivity(intent);
                }
            }
        });
    }
    public void deleteRequest() {
        Log.d("mawang", "reserve_UploadedFormEditableActivity deleteRequest - called");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(getString(R.string.baseUrl) + "deleteform/" + form_id+"/"+ Session.getUserEmail())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Toast.makeText(getApplicationContext(),"요청 실패", Toast.LENGTH_SHORT).show();
                Log.d("mawang", "reserve_UploadedFormEditableActivity deleteRequest onFailure - 에러 = " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
                Log.d("mawang", "reserve_UploadedFormEditableActivity deleteRequest onResponse - res = " + res);

                if (res.equals("delete error")) {
//                    Toast.makeText(getApplicationContext(),"요청 실패", Toast.LENGTH_SHORT).show();
                    Log.d("mawang", "reserve_UploadedFormEditableActivity deleteRequest onResponse - 에러 = " + res);
                } else {
                    finish();
                }
            }
        });
    }
    public void resultRequest() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("form_id", form_id);
//        intent.putExtra("userEmail",userEmail);
        startActivity(intent);
    }
    public void shareRequest() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.baseUrl) + "survey/" + form_id); // 링크

        Intent chooser = Intent.createChooser(intent, "공유"); // 다른앱으로 보내기
        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(chooser);
    }
    public void previewRequest() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.baseUrl) + "survey/" + form_id));
        startActivity(intent);
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

    protected void onRestart() {
        // 편집후 바로 메인에 와야 수정사항이 refresh 되기 때문에
        // reserve_UploadedFormEditableActivity 액티비티는 바로 꺼야함
        super.onRestart();
//        Log.d("mawang", "reserve_UploadedFormEditableActivity onRestart 실행");
        finish();
    }

//    protected void onStart(){
//        super.onStart();
//        Log.d("mawang", "reserve_UploadedFormEditableActivity onStart 실행");
//    }
//    protected void onResume(){
//        super.onResume();
//        Log.d("mawang", "reserve_UploadedFormEditableActivity onResume 실행");
//    }
//    protected void onPause(){
//        super.onPause();
//        Log.d("mawang", "reserve_UploadedFormEditableActivity onPause 실행");
//    }
//    protected void onStop(){
//        super.onStop();
//        Log.d("mawang", "reserve_UploadedFormEditableActivity onStop 실행");
////        finish();
//    }
//    protected void onDestroy(){
//        super.onDestroy();
//        Log.d("mawang", "reserve_UploadedFormEditableActivity onDestroy 실행");
//    }


}
