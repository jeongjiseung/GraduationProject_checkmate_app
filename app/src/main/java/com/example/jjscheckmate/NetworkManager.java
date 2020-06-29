package com.example.jjscheckmate;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.jjscheckmate.form.FormType;
import com.example.jjscheckmate.retrofitinterface.RetrofitService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkManager {
    //    private Retrofit retrofit;
//    private RetrofitService retrofitService;
    private String url;
    private Context mContext;
    private OkHttpClient client;

    private static NetworkManager networkManager = null;

    private NetworkManager(Context context) {
        this.mContext = context;
        client = new OkHttpClient();
        url = mContext.getString(R.string.baseUrl);

    }

    public static NetworkManager getInstance(Context context) {
        if (networkManager == null) {
            networkManager = new NetworkManager(context);
        }
        return networkManager;
    }



    public void submit(JSONObject jsonObject) { // work
//        Log.d("mawang", "NetworkManager submit - jsonObject = " + jsonObject.toString());

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("json", jsonObject.toString());

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("formComponents");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                // 이미지는 string url 만 보내줘도 된다.
                // 하지만 웹에서 불러오려면 로컬저장을 해야하기 때문에
                // 파일도 보내야 한다.
                if (jsonObject1.getInt("type") == FormType.IMAGE && jsonObject1.getBoolean("posted") == true) {
                    // 사진이 첨부된 이미지 항목만 파일까지 보내기
                    File file = (File) jsonObject1.get("real_file_data");
                    builder.addFormDataPart(String.valueOf(jsonObject1.getInt("real_file_name")), file.getName(), RequestBody.create(file, MediaType.parse("image/jpeg")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody requestbody = builder.build();

        Request request = new Request.Builder()
                .url(url + "save")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "NetworkManager submit onFailure - 폼 전송 실패 " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                Log.d("mawang", "NetworkManager submit onResponse - 폼 전송 성공 " + response.body().string());
            }
        });


    }

    public void update(JSONObject jsonObject, int form_id) {
        Log.d("mawang", "NetworkManager update - jsonObject = " + jsonObject.toString());
//        RequestBody requestbody = new MultipartBody.Builder().
//                setType(MultipartBody.FORM)
//                .addFormDataPart("form_id", String.valueOf(form_id))
//                .addFormDataPart("json", jsonObject.toString())
//                .build();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("form_id", String.valueOf(form_id));
        builder.addFormDataPart("json", jsonObject.toString());

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("formComponents");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                if (jsonObject2.getInt("type") == FormType.IMAGE && jsonObject2.getBoolean("posted") == true) {
                    File file = (File) jsonObject2.get("real_file_data");
                    builder.addFormDataPart(String.valueOf(jsonObject2.getInt("real_file_name")), file.getName(), RequestBody.create(file, MediaType.parse("image/jpeg")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody requestbody = builder.build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(mContext.getString(R.string.baseUrl) + "update")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "NetworkManager update onResponse - 폼 전송 실패 " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("mawang", "NetworkManager update onResponse - 폼 전송 성공 " + response.body().string());
            }
        });

    }

    public void draftSave(JSONObject jsonObject) {
        Log.d("mawang", "NetworkManager draftSave - jsonObject = " + jsonObject.toString());

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("json", jsonObject.toString());

        RequestBody requestbody = builder.build();

        Request request = new Request.Builder()
                .url(url + "draftsave")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "NetworkManager draftSave onFailure - 폼 전송 실패 " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("mawang", "NetworkManager draftSave onResponse - 폼 전송 성공 " + response.body().string());
            }
        });


    }

    public void draftUpdate(JSONObject jsonObject, int form_id) {

        RequestBody requestbody = new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addFormDataPart("form_id", String.valueOf(form_id))
                .addFormDataPart("json", jsonObject.toString())
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(mContext.getString(R.string.baseUrl) + "draftupdate")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "NetworkManager draftUpdate onResponse - 폼 전송 실패 " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("mawang", "NetworkManager draftUpdate onResponse - 폼 전송 성공 " + response.body().string());
            }
        });

    }
}
