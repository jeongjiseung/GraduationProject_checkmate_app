package com.example.jjscheckmate.result;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.ItemDecorate;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.form.FormComponentVO;
import com.example.jjscheckmate.form.FormType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class IndividualResultActivity extends AppCompatActivity {
    private String url;//getString(R.string.baseUrl);
    private int form_id;

    private ArrayList<IndividualResultDTO> datas;
    private IndividualViewDTO individualViewDTO;

    private RecyclerView individualResultRV;
    private IndividualResultRV individualResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_result);

        url = getString(R.string.baseUrl);

        Intent intent = getIntent();
        form_id = intent.getIntExtra("form_id", -1);
        individualViewDTO = (IndividualViewDTO) intent.getSerializableExtra("result"); // arraylist 제대로 온거?

        datas = new ArrayList<>();

        individualResultRV = (RecyclerView) findViewById(R.id.recycleView);
        individualResultRV.setLayoutManager(new LinearLayoutManager(this));
        individualResultRV.addItemDecoration(new ItemDecorate());
        individualResultAdapter = new IndividualResultRV(getApplicationContext(), datas); //
        individualResultRV.setAdapter(individualResultAdapter);

//        Log.d("mawang", "IndividualResultActivity onCreate - form_id : " + form_id);
        //Log.d("mawang", "IndividualResultActivity onCreate - individualViewDTO : " + individualViewDTO);

        getServerForm(form_id);
    }

    public void getServerForm(int form_id) {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url + "individual/" + form_id)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "IndividualResultActivity getServerForm onFailure - 폼 전송 실패  e : " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String res = response.body().string(); //jsonarray로 오는군


                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<FormComponentVO>>() {}.getType();
                    ArrayList<FormComponentVO> componentVOS = gson.fromJson(res, type);

//                    if (componentVOS == null) {
//                        Log.d("mawang", "IndividualResultActivity onResponse - componentVOS is null");
//                    } else {
//                        Log.d("mawang", "IndividualResultActivity getServerForm onResponse - componentVOS.size() : " + componentVOS.size());
//                    }


                    for (int i = 0; i < componentVOS.size(); i++) {
                        IndividualResultDTO individualResultDTO = new IndividualResultDTO();
                        String answer = "";
                        ArrayList<String> answers = individualViewDTO.getResult().get(i);


                        if (answers != null) {
//                            Log.d("mawang", "IndividualResultActivity getServerForm onResponse - answers : " + answers);
                            if (componentVOS.get(i).getType() == FormType.RADIOCHOICEGRID)
                            {
                                for (int j = 0; j < answers.size(); j++) {
                                    answer += (componentVOS.get(i).getAddedRowOption().get(j)
                                            + " - "
                                            + componentVOS.get(i).getAddedColOption().get(Integer.parseInt(answers.get(j))));
                                    if (j != answers.size() - 1) {
                                        answer += "\n";
                                    }
                                }
                            } else if (componentVOS.get(i).getType() == FormType.CHECKBOXGRID)
                            {
                                for (int j = 0; j < answers.size(); j++) {
                                    //answer += ((j + 1) + ". " + componentVOS.get(i).getAddedColOption().get(Integer.valueOf(answers.get(j))) + "\n");
                                    answer += componentVOS.get(i).getAddedRowOption().get(j) + " - ";

                                    answers.set(j, answers.get(j).replace("[\"", "")
                                            .replace("\",\"", "")
                                            .replace("\"]", ""));

                                    for (int k = 0; k < answers.get(j).length(); k++) {

                                        if (k == answers.get(j).length() - 1) {
                                            answer += componentVOS.get(i).getAddedColOption().get(Character.getNumericValue(answers.get(j).charAt(k)));

                                        } else {
                                            answer += componentVOS.get(i).getAddedColOption().get(Character.getNumericValue(answers.get(j).charAt(k)));
                                            answer += ",";
                                        }
                                    }

                                    if (j != answers.size() - 1) {
                                        answer += "\n";
                                    }
//                                    Log.d("mawang", "IndividualResultActivity onResponse FormType.CHECKBOXGRID- answer = " + answer);
                                }
                            } else if (componentVOS.get(i).getType() == FormType.CHECKBOXES)
                            {
//                                Log.d("mawang", "IndividualResultActivity onResponse FormType.CHECKBOXES - answers = " + answers);
//                                Log.d("mawang", "IndividualResultActivity onResponse FormType.CHECKBOXES - answers.size() = " + answers.size());

                                for (int j = 0; j < answers.size(); j++) {
                                    if (j == answers.size() - 1 && !answers.get(j).isEmpty()) { // etc에 text존재
                                        answer += answers.get(j);
                                    }else if(j == answers.size() - 1 && answers.get(j).isEmpty()){ // etc에 text 없음
                                        answer = answer.substring(0,answer.length()-1); // 끝에 comma 제거
                                    }else if(j == answers.size() - 2 && answers.get(j).isEmpty()){ // etc 표시
                                        answer += " 기타) ";
                                    } else { // 체크박스들 답안
                                        answer += answers.get(j) + ",";
                                    }
                                }
                            }else if (componentVOS.get(i).getType() == FormType.RADIOCHOICE)
                            {
                                for (int j = 0; j < answers.size(); j++) {
                                    if (j == answers.size() - 2 && answers.get(j).isEmpty()) {
                                        answer += "기타) ";
                                    } else {
                                        answer += answers.get(j);
                                    }
                                }
                            }else if (componentVOS.get(i).getType() == FormType.LONGTEXT)
                            {
//                                if(answers.get(0).isEmpty()){
//                                    answer += "(응답X)";
//                                }else{
//                                    answer +=answers.get(0).replace("rn", "\n"); // escape 문자 처리
//                                }
                                answer +=answers.get(0).replace("rn", "\n"); // escape 문자 처리
                            } else {
                                // loop 필요없음
//                            for (int j = 0; j < answers.size(); j++)
//                            {
//                                answer += answers.get(j);
//                            }
//                                if(answers.get(0).isEmpty()){
//                                    answer += "(응답X)";
//                                }else{
//                                    answer += answers.get(0);
//                                }
                                answer += answers.get(0);
                            }

                        }else{ // 일반항목에서 답을 고르지 않았을 경우
                            Log.d("mawang", "IndividualResultActivity getServerForm onResponse - answers is null");
                            answer +="(응답X)";
                        }


                        individualResultDTO.setAnswer(answer);
                        individualResultDTO.setQuestion(componentVOS.get(i).getQuestion());
                        individualResultDTO.setType(componentVOS.get(i).getType());

                        datas.add(individualResultDTO);
//                        Log.d("mawang", "IndividualResultActivity getServerForm onResponse - 결과 : " + answer);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            individualResultAdapter.setDatas(datas);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
