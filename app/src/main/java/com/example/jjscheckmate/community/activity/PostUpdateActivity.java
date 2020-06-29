package com.example.jjscheckmate.community.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.jjscheckmate.community.model.PostImageDTO;
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

public class PostUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_FROM_ALBUM = 1;
    private Integer imageLen;
    private Integer postID;
    private File file;

    private ArrayList<MultipartBody.Part> files;
    private ArrayList<PostImageDTO> images;
    private ArrayList<Integer> deleteImageList;
    private AlertDialog alertDialog;

    Button btnPost;
    ImageButton imageAdd;
    EditText content;
    LinearLayout imageList;
    LinearLayout existedImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_update);

        postID = getIntent().getIntExtra("postID", -1);
        content = (EditText) findViewById(R.id.content);
        content.setText(getIntent().getStringExtra("content"));

        btnPost = (Button) findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);
        imageAdd = (ImageButton) findViewById(R.id.imageAdd);
        imageAdd.setOnClickListener(this);

        imageList = (LinearLayout) findViewById(R.id.imageListView);
        deleteImageList = new ArrayList<>();
        existedImageList = (LinearLayout) findViewById(R.id.existedImageListView);
        files = new ArrayList<>();

        getImageInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPost: {
                postUpdate();
                break;
            }
            case R.id.imageAdd: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
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

            Glide.with(PostUpdateActivity.this).load(fileUri).into(img);
            Log.d("mawang", "PostUpdateActivity onActivityResult - fileUri = " + fileUri);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageDeleteDialog(img, -1);
//                    Toast.makeText(getApplicationContext(),"imageDeleteDialog",Toast.LENGTH_SHORT).show();
                }
            });
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

    public void getImageInfo() { // ??

        RetrofitApi.getService().getImageLen(postID).enqueue(new retrofit2.Callback<ArrayList<PostImageDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<PostImageDTO>> call, @NonNull Response<ArrayList<PostImageDTO>> response) {
                images = response.body();
                imageLoad();

                Log.d("mawang", "PostUpdateActivity getImageInfo - onResponse");
                Log.d("mawang", "PostUpdateActivity getImageInfo - images size :" + images.size());
            }

            @Override
            public void onFailure(Call<ArrayList<PostImageDTO>> call, Throwable t) {
                Log.d("mawang", "PostUpdateActivity getImageInfo - onFailure :" + t.getMessage());
            }
        });
    }

    public void imageLoad() {
        int orderIMG = 1;

        for (PostImageDTO image : images) {

            ImageView img = new ImageView(this);
            img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

//            Glide.with(PostUpdateActivity.this).load(getString(R.string.baseUrl)+"post/image/thumbnail/"+postID+"/"+image.get_id()).into(img);

            // 임시 방편
            switch (orderIMG) {
                case 1: // 엘사
                    Glide.with(this).load("https://vignette.wikia.nocookie.net/arendelle/images/2/2c/Elsa4.jpg/revision/latest/scale-to-width-down/340?cb=20151103112320&path-prefix=ko")
                            .into(img);
                    orderIMG++;
                    break;
                case 2: // 안나
                    Glide.with(this).load("https://pbs.twimg.com/media/EQaqvRyUwAEyNeX.jpg")
                            .into(img);
                    orderIMG++;
                    break;
                default:
                    Glide.with(this).load("data:image/jpeg;base64,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")
                            .into(img);
                    break;
            }


            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //삭제되는 id를 서버로 보내자
                    imageDeleteDialog(img, image.get_id());
                    Log.d("mawang", "PostUpdateActivity imageLoad - image.get_id() :" + image.get_id());
//                    Toast.makeText(getApplicationContext(),"imageDeleteDialog",Toast.LENGTH_SHORT).show();
                }
            });

            existedImageList.addView(img);
        }

    }

    public void postUpdate() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("postID", postID);
        hashMap.put("content", content.getText().toString());
//        hashMap.put("userEmail", Session.getUserEmail()); // DB 쪽에서 필요없지 않은가
//        hashMap.put("time",System.currentTimeMillis()); // DB 쪽에서 필요없지 않은가
        hashMap.put("deleteList", deleteImageList);
        Log.d("mawang", "PostUpdateActivity postUpdate - hashMap :" + hashMap);

        RetrofitApi.getService().postUpdate(hashMap, files).enqueue(new retrofit2.Callback<PostDTO>() {
            @Override
            public void onResponse(Call<PostDTO> call, @NonNull Response<PostDTO> response) {
//                Log.d("mawang","PostUpdateActivity postUpdate - onResponse ");
//                Log.d("mawang", "PostUpdateActivity postUpdate - response body = " + response.body());
                Log.d("mawang", "PostUpdateActivity postUpdate - response toString = " + response.toString());
                finish();
            }

            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) {
                Log.d("mawang", "PostUpdateActivity postUpdate - onFailure : " + t.getMessage());
            }
        });

    }

    public void imageDeleteDialog(final ImageView img, int imageID) {
        Log.d("mawang", "PostUpdateActivity imageDeleteDialog - imageID :" + imageID);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (imageID != -1) { // 기존 이미지
                    deleteImageList.add(imageID);
                    existedImageList.removeView(img);
                }
                if (imageID == -1) { // 새로 추가한 이미지
                    int index = imageList.indexOfChild(img);
                    files.remove(index);
                    imageList.removeView(img);
                }
//                dialogInterface.cancel(); // 안해도 닫힘 , overwrite
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
