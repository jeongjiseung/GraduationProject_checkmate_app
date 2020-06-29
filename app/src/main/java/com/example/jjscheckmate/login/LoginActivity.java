package com.example.jjscheckmate.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.jjscheckmate.MainActivity;
import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.activity.CommunityMainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;

    boolean fileReadPermission;
    boolean fileWritePermission;
    boolean internetPermission;

    private Session loginSession;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;

//    private SharedPreferences mPref;
//    private SharedPreferences.Editor editor;

    CheckBox chkAutoLogin;
    private Button btnGoSurvey;
    private Button btnGoCommunity;
    private SignInButton btnLogin;
    private LinearLayout autoLogin;

    //Use for Widget Login
    private SharedPreferences widgetLoginPref;
    private SharedPreferences.Editor widgetLoginEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkPermission();

        loginSession = (Session) getApplication();
        btnGoSurvey = (Button) findViewById(R.id.btnGoSurvey);
        btnGoCommunity = (Button) findViewById(R.id.btnGoCommunity);
        btnLogin=(SignInButton)findViewById(R.id.sign_in_button);
        autoLogin=(LinearLayout)findViewById(R.id.autoLogin);




//        userEmail = "jjs@checkmate";
//        userName = "정지승";
//        userImage = Uri.parse("https://img1.daumcdn.net/thumb/R1280x0.fjpg/?fname=http://t1.daumcdn.net/brunch/service/guest/image/FYdV1JIJn_8yupol5d9fVh44L9Y.PNG");

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        firebaseAuth= FirebaseAuth.getInstance();
        SignInButton signInButton=findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener((view)->{
            onClick(view);
        });
        chkAutoLogin=(CheckBox)findViewById(R.id.chkAutoLogin);

//        mPref=getSharedPreferences("login",MODE_PRIVATE);
//        editor=mPref.edit();

        widgetLoginPref=getSharedPreferences("widget_LoginOnApp", MODE_PRIVATE);
        widgetLoginEdit=widgetLoginPref.edit();
    }

    @Override
    public void onStart() {
        super.onStart();
//        if(mPref.getBoolean("isAutoLogin",false)){
//            chkAutoLogin.setChecked(true);
//        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chkAutoLogin:
//                if(chkAutoLogin.isChecked()){
//                    editor.putBoolean("isAutoLogin",true);
//                    editor.apply();
//                }else{
//                    editor.putBoolean("isAutoLogin",false);
//                    editor.apply();
//                }
                break;
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.btnGoSurvey:
                goToSurvey();
                break;
            case R.id.btnGoCommunity:
                goToCommunity();
                break;
        }
    }


    private void goToSurvey() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class); // new
        startActivity(intent);
    }

    private void goToCommunity() {
        Intent intent = new Intent(LoginActivity.this, CommunityMainActivity.class); // new
        startActivity(intent);
//        Toast.makeText(getApplicationContext(),"goToCommunity",Toast.LENGTH_SHORT).show();
    }

    public void checkPermission(){
        // 한 번 체크해놓으면 항상 실행됨
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            fileReadPermission=true;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            fileWritePermission=true;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED){
            internetPermission=true;
        }
        // 처음실행 때만 실행된다.
        if(!fileReadPermission||!fileWritePermission||!internetPermission){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET},100);
        }
//        else{
//            Log.d("mawang", "LoginActivity checkPermission - already got permissions " );
//        }
    }

    private void signIn(){
        Intent intent= mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completeTask){
        try{
            GoogleSignInAccount account=completeTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
//            String email=account.getEmail();
//            Log.d("mawang","LoginActivity handleSignInResult - email : "+email);

        }catch (Exception e){}
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            FirebaseUser user=firebaseAuth.getCurrentUser();

                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

//                            Log.d("mawang","LoginActivity firebaseAuthWithGoogle - email : "+user.getEmail());
//                            Log.d("mawang","LoginActivity firebaseAuthWithGoogle - name : "+user.getDisplayName());
//                            Log.d("mawang","LoginActivity firebaseAuthWithGoogle - uri : "+user.getPhotoUrl());

                            loginSession.setSession(user.getEmail(),user.getDisplayName(),user.getPhotoUrl());
//                            saveLoginInfo(user.getEmail(),user.getDisplayName(),user.getPhotoUrl());
                            loginSuccess();

                            //위젯 로그인용
                            widgetLoginEdit.putString("LoginID",user.getEmail());
                            widgetLoginEdit.putBoolean("IsUserLogin",true);
                            widgetLoginEdit.apply();

                        } else {
                            // 로그인 실패
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

//    private void saveLoginInfo(String userEmail,String userName,Uri userImage){
//        SharedPreferences login_info=getSharedPreferences("loginConfig",0);
//        SharedPreferences.Editor editor=login_info.edit();
//        editor.putString("userEmail",userEmail);
//        editor.putString("userName",userName);
//        editor.putString("userImage",String.valueOf(userImage));
//        editor.commit();
//    }

    private void loginSuccess(){
        btnGoSurvey.setVisibility(View.VISIBLE);
        btnGoCommunity.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
        autoLogin.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("저장").setMessage("앱을 종료하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
