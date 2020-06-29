package com.example.jjscheckmate.community.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.model.PostDTO;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class PostCreateActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int PICK_FROM_ALBUM=1;
    private Integer groupID;

    private File file;
    private ArrayList<MultipartBody.Part> files;

    Button btnPost;
    ImageButton imageAdd;
    EditText content;
    LinearLayout imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_create);

        groupID=getIntent().getIntExtra("groupID",-1);
        Log.d("mawang","PostCreateActivity onCreate - groupID = "+groupID);

        btnPost=(Button)findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);
        imageAdd=(ImageButton)findViewById(R.id.imageAdd);
        imageAdd.setOnClickListener(this);
        content=(EditText) findViewById(R.id.content);
        imageList=(LinearLayout) findViewById(R.id.imageListView);

        files=new ArrayList<>();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPost:{
                postCreate();
                //content.setText(""); // 안해도 초기화 되지 않을까?

//                Toast.makeText(this,"btnPost",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.imageAdd:{
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
//                Toast.makeText(this,"imageAdd",Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_ALBUM) {
            Uri fileUri = data.getData();

            ImageView img = new ImageView(this);
            img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(PostCreateActivity.this).load(fileUri).into(img);

            imageList.addView(img);
            file = new File(getRealPathFromURI(fileUri));

            RequestBody fileReqBody = RequestBody.create(file, MediaType.parse("image/*"));
            MultipartBody.Part part = MultipartBody.Part.createFormData(file.getName(), file.getName(), fileReqBody);
            files.add(part);
        }

    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getBaseContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void postCreate(){

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("group_id",groupID);
        hashMap.put("content",content.getText().toString()); // edittext 값
        hashMap.put("userEmail", Session.getUserEmail());
        hashMap.put("time",System.currentTimeMillis());
        hashMap.put("Nickname", Session.getUserName()); // 닉네임
        Log.d("mawang","PostCreateActivity postCreate - hashMap = "+hashMap);

        RetrofitApi.getService().postCreate(hashMap,files).enqueue(new retrofit2.Callback<PostDTO>(){
            @Override
            public void onResponse(Call<PostDTO> call, @NonNull Response<PostDTO> response) {
                Log.d("mawang","PostCreateActivity postCreate onResponse - response body = "+response.body());
                finish();
            }
            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {
                Log.d("mawang","PostCreateActivity postCreate - onFailure = "+t.getMessage());
                finish(); // 둘 다 끝내
            }
        });
    }
}
