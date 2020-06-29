package com.example.jjscheckmate.result;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.login.Session;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IndividualViewFragment extends Fragment {
    private RecyclerView individualViewRV;
    private IndividualViewRV individualViewAdapter;

    private ArrayList<IndividualViewDTO> datas = new ArrayList<>();

    //    private String userEmail;
    private int form_id;
    private String url;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getString(R.string.baseUrl);

        if (getArguments() != null) {
            form_id = getArguments().getInt("form_id");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_result_individual, container, false);

        individualViewRV = (RecyclerView) rootView.findViewById(R.id.recycleView);
        individualViewRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        individualViewRV.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        individualViewAdapter = new IndividualViewRV(getContext(), datas, form_id);
        individualViewRV.setAdapter(individualViewAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSurveyResult(form_id);
    }

    public void getSurveyResult(final int form_id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestbody = new MultipartBody.Builder().
                setType(MultipartBody.FORM)
//                .addFormDataPart("userEmail", MainActivity.getUserEmail())
                .addFormDataPart("userEmail", Session.getUserEmail())
                .addFormDataPart("form_id", String.valueOf(form_id))
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url + "load")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "IndividualViewFragment getSurveyResult onFailure - 폼 전송 실패");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
//                Log.d("mawang","IndividualViewFragment getSurveyResult onResponse befo- res : "+res);
                res = res.replace("\\", "")
                        .replace("\"[", "[")
                        .replace("]\"", "]")
                        .replace("}\"", "}")
                        .replace("\"{", "{");
//                Log.d("mawang","IndividualViewFragment getSurveyResult onResponse after- res : "+res);

                try {
                    JSONArray jsonArray = new JSONArray(res);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        IndividualViewDTO individualViewDTO = new IndividualViewDTO();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonObjResult = jsonObject.getJSONObject("surveyResult");

                        jsonObjResult.remove("form_id"); // 필요없으니
                        jsonObjResult.remove("userEmail"); // 필요없으니
//                        Log.d("mawang","IndividualViewFragment getSurveyResult onResponse - jsonObjResult : "+jsonObjResult);
                        String time = jsonObject.getString("time");
                        individualViewDTO.setIndex(i + 1);
                        individualViewDTO.setTime(time);

                        Iterator<String> keys = jsonObjResult.keys();
                        HashMap<String, ArrayList<String>> gridParser = new HashMap<>();
                        ArrayList<String> removeKeys = new ArrayList<>();

                        while (keys.hasNext()) {
                            String key = keys.next();
//                            Log.d("mawang","IndividualViewFragment getSurveyResult onResponse hasNext- key값 : "+key);

                            if (key.split("-").length == 2) { // 그리드 때문이구나

                                String[] grids = key.split("-");
                                String gridKey = grids[0];
                                String gridVal = jsonObjResult.getString(key);

                                removeKeys.add(key);

                                if (gridParser.containsKey(gridKey)) {
                                    gridParser.get(gridKey).add(gridVal);
//                                    Log.d("mawang", "IndividualViewFragment getSurveyResult onResponse containsKey - gridParser : " + gridParser);
                                } else {
                                    ArrayList<String> temp = new ArrayList<>();
                                    temp.add(gridVal);
                                    gridParser.put(gridKey, temp);
//                                    Log.d("mawang", "IndividualViewFragment getSurveyResult onResponse else - gridParser : " + gridParser);
                                }
                            } else {

                                int keyInt = Integer.parseInt(key);
                                //individualViewDTO.keyArray.add(keyInt); // key값 저장

                                Object json = jsonObjResult.get(String.valueOf(keyInt)); // get value by key

                                if (json instanceof JSONArray) { // when?
                                    JSONArray multiAnswerJson = (JSONArray) json;
                                    ArrayList<String> multiAnswer = new ArrayList<>();

                                    for (int k = 0; k < multiAnswerJson.length(); k++) {
                                        multiAnswer.add(multiAnswerJson.getString(k));
                                    }
                                    individualViewDTO.setResult(keyInt, multiAnswer);
//                                    Log.d("mawang", "IndividualViewFragment getSurveyResult onResponse JSONArray- multiAnswerJson : " + multiAnswerJson);
                                } else {
                                    ArrayList<String> multiAnswer = new ArrayList<>();
                                    multiAnswer.add(String.valueOf(json));
                                    individualViewDTO.setResult(keyInt, multiAnswer);
//                                    Log.d("mawang", "IndividualViewFragment getSurveyResult onResponse - multiAnswer : " + multiAnswer);
                                }
                            }
                        }
//                        Log.d("mawang", "IndividualViewFragment getSurveyResult onResponse - removeKeys : " + removeKeys);
//                        Log.d("mawang", "IndividualViewFragment getSurveyResult onResponse - gridParser : " + gridParser);

                        for (int j = 0; j < removeKeys.size(); j++) {
                            jsonObjResult.remove(removeKeys.get(j));
                        }


                        Iterator<String> iterator = gridParser.keySet().iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            individualViewDTO.setResult(Integer.valueOf(key), gridParser.get(key));
                        }

//                        Log.d("mawang", "IndividualViewFragment getSurveyResult onResponse - individualViewDTO HashMap : " + individualViewDTO.getResult());

                        datas.add(individualViewDTO);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            individualViewAdapter.setDatas(datas);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("mawang", "IndividualViewFragment getSurveyResult onResponse - catch = " + e.getMessage());
                }

            }
        });
    }
}
