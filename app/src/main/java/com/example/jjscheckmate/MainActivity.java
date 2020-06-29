package com.example.jjscheckmate;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jjscheckmate.form.FormActivity;
import com.example.jjscheckmate.login.Session;
import com.example.jjscheckmate.mainActivityViwePager.MainVPAdapter;
import com.google.android.material.navigation.NavigationView;  // ??
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;



    private ViewPager viewPager;
    private MainVPAdapter mainVPAdapter;
    private TabLayout mTabLayout;

    public static final int categoryNumber=110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Forms");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.navigation);

//        url=getString(R.string.baseUrl);

        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);



        NavigationView navigationView=(NavigationView)findViewById(R.id.navigationView);
        View NavHeader = navigationView.getHeaderView(0); // LinearLayout
        TextView txtUserID = NavHeader.findViewById(R.id.txtUserID);
        txtUserID.setText(Session.getUserName());
        ImageView imvUserImg = NavHeader.findViewById(R.id.imvUserImg);
        Glide.with(this).load(Session.getUserImage()).into(imvUserImg);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true); //false
                drawerLayout.closeDrawer(GravityCompat.START); // touch 하면 닫히게

                switch (menuItem.getItemId()){
                    case R.id.offline: {
//                        Intent intent = new Intent(getApplicationContext(), old_OfflineFormActivity.class);
//                        intent.putExtra("userEmail", userEmail);
//                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "offline closed", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.template: {
                        Toast.makeText(getApplicationContext(), "template", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.notification: {
                        Toast.makeText(getApplicationContext(), "notification", Toast.LENGTH_SHORT).show();
                    }
                    case R.id.help:{
                        Toast.makeText(getApplicationContext(), "help", Toast.LENGTH_SHORT).show();

                    }
                    case R.id.share: {
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_SEND);
//                        intent.setType("text/plain");
//                        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.baseUrl) + "survey/6");
//                        Intent chooser = Intent.createChooser(intent, "공유");
//                        startActivity(chooser);

                        Toast.makeText(getApplicationContext(), "share 뭘 공유해", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return true;
            }
        });

//        Bundle args = new Bundle();
//        args.putString("userEmail", userEmail);
//        mainVPAdapter = new MainVPAdapter(getSupportFragmentManager(), args);
        mainVPAdapter = new MainVPAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainVPAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {viewPager.setCurrentItem(tab.getPosition());}

            // when?
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            // when?
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START); // start 는 왼쪽 , end는 오른쪽
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.bottom_menu:
                Intent intent=new Intent(this, FormActivity.class);
                intent.putExtra("category",categoryNumber);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(intent);
                break;
        }
    }

}
