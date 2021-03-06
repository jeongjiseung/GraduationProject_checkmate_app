package com.example.jjscheckmate.community.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.community.adapter.SearchAdapter;
import com.example.jjscheckmate.community.model.FriendDTO;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchAdapter adapter;
    Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FriendDTO> datas=new ArrayList<>();
    String type;
    String query;
    int page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchView=(SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchListener());

        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        query=intent.getStringExtra("query");
        searchView.setQuery(query,false);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        searchType(type);
    }

    public void searchType(String type){
        if(type.equals("friend")){
            adapter=new SearchAdapter(this,datas);
            recyclerView.setAdapter(adapter);
            getProfile();
        }
    }

    public void getProfile(){
        RetrofitApi.getService().getSearchResult(query,page).enqueue(new retrofit2.Callback<ArrayList<FriendDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<FriendDTO>> call, Response<ArrayList<FriendDTO>> response) {
                if(response.body()!=null){
                    ArrayList<FriendDTO> friendsDTO=response.body();
                    adapter.addItems(friendsDTO);
                    page++;
                }
            }
            @Override
            public void onFailure(Call<ArrayList<FriendDTO>> call, Throwable t) {
                Toast.makeText(SearchResultActivity.this,"검색 결과가 없습니다",Toast.LENGTH_SHORT).show();
            }
        });
    }
    class SearchListener implements SearchView.OnQueryTextListener{
        @Override
        public boolean onQueryTextSubmit(String q) {
            query=q;
            getProfile();
            return true;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }
}
