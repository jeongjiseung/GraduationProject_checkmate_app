package com.example.jjscheckmate.mainActivityViwePager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.UploadedSurveyDTO;
import com.example.jjscheckmate.UploadedSurveyRV;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.offlineform.FormItem;
import com.example.jjscheckmate.offlineform.OfflineFormRVAdapter;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainVPMySurveyFragment extends Fragment {
    public static final int SERVER_SURVEY = 0;
    public static final int OFFLINE_SURVEY = 1;

    RecyclerView offlineSurveyRecycleView;
    RecyclerView responseWaitSurveyRecycleView;
    OfflineFormRVAdapter offlineFormAdapter; //    RecyclerView.Adapter offlineFormAdapter;
    UploadedSurveyRV uploadedSurveyAdapter; //    RecyclerView.Adapter uploadedSurveyAdapter;

    private TextView txtMoreOfflineView;
    private TextView txtMoreMySurveyView;
    private String url; // 서버주소

    private ArrayList<UploadedSurveyDTO> datas; // summit
    private ArrayList<FormItem> items; // draft


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) { // 여기서 이메일 받는군
//            this.userEmail = getArguments().getString("userEmail");
//        }
        url = getString(R.string.baseUrl);
        datas = new ArrayList<>();
        items = new ArrayList<>();
//        Log.d("mawang","MainVPMySurveyFragment onCreate called");
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.d("mawang","MainVPMySurveyFragment onCreateView called");
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_main_vp_my_survey, container, false);

        txtMoreOfflineView = (TextView) rootView.findViewById(R.id.moreOfflineView); // 더보기
        txtMoreMySurveyView = (TextView) rootView.findViewById(R.id.moreMySurveyView); // 더보기
        txtMoreOfflineView.setOnClickListener(new ClickListener());
        txtMoreMySurveyView.setOnClickListener(new ClickListener());

        offlineSurveyRecycleView = (RecyclerView) rootView.findViewById(R.id.offlineSurveyRecycleView);
        offlineSurveyRecycleView.setLayoutManager(new LinearLayoutManager(getContext())); // 이걸 생략하면 화면에 안그려짐 !
        offlineSurveyRecycleView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        offlineFormAdapter = new OfflineFormRVAdapter(getContext(), items);
        offlineSurveyRecycleView.setAdapter(offlineFormAdapter);


        responseWaitSurveyRecycleView = (RecyclerView) rootView.findViewById(R.id.responseWaitSurveyRecycleView);
        responseWaitSurveyRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        responseWaitSurveyRecycleView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        uploadedSurveyAdapter = new UploadedSurveyRV(getContext(), datas,this);
        responseWaitSurveyRecycleView.setAdapter(uploadedSurveyAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getResponseWaitSurvey(userEmail); // 다시 돌아올때마다 해줘야해
//        Log.d("mawang","MainVPMySurveyFragment onActivityCreated called");
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.moreMySurveyView: {
                    Intent intent = new Intent(getContext(), moreViewActivity.class);
                    intent.putExtra("type", SERVER_SURVEY);
//                    intent.putExtra("userEmail", userEmail);
                    startActivity(intent);
                    break;
                }
                case R.id.moreOfflineView: {
                    Intent intent = new Intent(getContext(), moreViewActivity.class);
                    intent.putExtra("type", OFFLINE_SURVEY);
//                    intent.putExtra("userEmail", userEmail);
                    startActivity(intent);
                    break;
                }
            }
        }
    }

    public String getTime(String str) { //very good
        long now = Long.valueOf(str);
        Date date = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }


    public void getResponseWaitSurvey() { //final String userEmail

        OkHttpClient client = new OkHttpClient();

        RequestBody requestbody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //.addFormDataPart("userEmail", userEmail)
//                .addFormDataPart("userEmail", MainActivity.getUserEmail())
                .addFormDataPart("userEmail", Session.getUserEmail())
                .build();


        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url + "user/forms")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - 폼 전송 실패");
            }

            @Override
            // response.body().string() 2번 쓰면 터짐
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string(); //string !
//                Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - 성공 res  = " + res);
//                Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - res 길이 = " + res.length());

                if (res.length() == 2) { // 아하!
//                    Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - 첫방문");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getActivity(), "첫 방문을 환영합니다.", Toast.LENGTH_SHORT).show();
                            // 이 멘트는 디비 이메일 체크해서 말해줘야겠다.
                            Toast.makeText(getActivity(), "초기화 상태입니다.", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    // 수정 success
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        Gson gson = new Gson();
//                        Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - jsonArray = " + jsonArray);
                        //Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - jsonArray.length = " + jsonArray.length()); // unique 항상 1

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            //old_FormDTO formDTO = gson.fromJson(jsonObject.toString(), old_FormDTO.class);

                            UploadedSurveyDTO uploadedSurveyDTO = new UploadedSurveyDTO();
                            uploadedSurveyDTO.set_id(jsonObject.getInt("_id"));
                            uploadedSurveyDTO.setTitle(jsonObject.getString("title"));
                            uploadedSurveyDTO.setResponse_cnt(jsonObject.getInt("response_cnt")); // 응답횟수
                            uploadedSurveyDTO.setTime(getTime(jsonObject.getString("time")));

                            datas.add(uploadedSurveyDTO);
                        }

//                        Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey onResponse - datas.size = " + datas.size());


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                uploadedSurveyAdapter = new UploadedSurveyRV(getContext(), userEmail, datas);
//                                uploadedSurveyAdapter = new UploadedSurveyRV(getContext(), datas);
//                                responseWaitSurveyRecycleView.setAdapter(uploadedSurveyAdapter);
                                Toast.makeText(getActivity(), "설문지들을 불러옵니다.", Toast.LENGTH_SHORT).show(); // work
                                uploadedSurveyAdapter.setDatas(datas); // 안에다 집어넣어야 하나보다
                            }
                        });

//                        Log.d("mawang", "MainVPMySurveyFragment onStart -after datas =  " + datas);
//                        Log.d("mawang", "MainVPMySurveyFragment onStart -after uploadedSurveyAdapter datas =  " + uploadedSurveyAdapter.getDatas());

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - 폼 error: " + e.getMessage());
                    }
                }


            }
        });
    }

    public void getDraftSurvey() {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestbody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
//                .addFormDataPart("userEmail", MainActivity.getUserEmail())
                .addFormDataPart("userEmail", Session.getUserEmail())
                .build();


        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url + "user/draftForms")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey - 폼 전송 실패");
            }

            @Override
            // response.body().string() 2번 쓰면 터짐
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string(); //string !
//                Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey - 성공 res  = " + res);
//                Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - res 길이 = " + res.length());

                if (res.length() == 2) {
//                    Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey - 첫방문");

                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
//                        Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey - jsonArray = " + jsonArray);
                        //Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - jsonArray.length = " + jsonArray.length()); // unique 항상 1

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            FormItem formitem = new FormItem();
                            formitem.set_id(jsonObject.getInt("_id"));
                            formitem.setTitle(jsonObject.getString("title"));
                            formitem.setTime(getTime(jsonObject.getString("time")));

                            items.add(formitem);
                        }
//                        Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey onResponse - OFFdatas.size = " + items.size());

                        getActivity().runOnUiThread(new Runnable() {
                            // 살짝늦는군 실행이
                            @Override
                            public void run() {

                                offlineFormAdapter.setItems(items); // 안에다 집어넣어야 하나보다
                            }
                        });

//                        Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey -after items =  " + items);
//                        Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey -after offlineFormAdapter datas =  " + offlineFormAdapter.getItems());

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey - 폼 error: " + e.getMessage());
                    }
                }


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

//        Log.d("mawang","MainVPMySurveyFragment onStart called");

        datas.clear();
        items.clear();
        uploadedSurveyAdapter.datasClear();
        offlineFormAdapter.ItemsClear();

        getResponseWaitSurvey();
        getDraftSurvey();
        // 편집은 수정한 것이기 때문에 refresh 바로 해야함
    }

//    public void onResume(){
//        super.onResume();
//        Log.d("mawang","MainVPMySurveyFragment onResume called");
//
//      // 공유,보기는 수정한 것이 아니기 때문에 refresh 할 필요없음
//    }
//    public void onPause(){
//        super.onPause();
//        Log.d("mawang","MainVPMySurveyFragment onPause called");
//    }
//    public void onStop(){
//        super.onStop();
//        Log.d("mawang","MainVPMySurveyFragment onStop called");
//
//    }
//    public void onDestroy(){
//        super.onDestroy();
//        Log.d("mawang","MainVPMySurveyFragment onDestroy called");
//    }


    public void refreshData(){
        Log.d("mawang","MainVPMySurveyFragment refreshData called");
        datas.clear();
        items.clear();
        uploadedSurveyAdapter.datasClear();
        offlineFormAdapter.ItemsClear();

        getResponseWaitSurvey();
        getDraftSurvey();
// 삭제는 refresh 바로 해야함
    }


}
