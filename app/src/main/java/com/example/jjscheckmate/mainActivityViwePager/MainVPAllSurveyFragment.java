package com.example.jjscheckmate.mainActivityViwePager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jjscheckmate.R;
import com.example.jjscheckmate.retrofitinterface.RetrofitApi;

import java.util.ArrayList;

public class MainVPAllSurveyFragment extends Fragment {
    private Spinner spinner;
    private RecyclerView AllsurveysRecycleView;
    //private RecyclerView.Adapter  surveysAdapter;
    private SurveyRV surveysAdapter;
    private ArrayList<SurveyDTO> Alldatas;

    private int pageNum;
    private int spinnerIndex;
    private int totalCount = 10; // test

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Alldatas = new ArrayList<>();
        pageNum = 0; //1
        spinnerIndex = 0;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_main_vp_all_survey, container, false);

        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setSelection(0); // spinner.setSelection(0,false);
        spinner.setOnItemSelectedListener(new ItemSelectListener());


        surveysAdapter = new SurveyRV(getContext(), Alldatas);

        AllsurveysRecycleView = (RecyclerView) rootView.findViewById(R.id.allSurveyRecycleView);
        AllsurveysRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        AllsurveysRecycleView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        AllsurveysRecycleView.addOnScrollListener(new ScrollListener());
        AllsurveysRecycleView.setAdapter(surveysAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getAllSurveyList(RequestType.ALL, pageNum); // 어차피 스피너를 통해 불러오니까 일단 주석
        // 여기서도 호출하면 데이터 중복됨
    }

    public class ItemSelectListener implements AdapterView.OnItemSelectedListener { // 스피너
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            Alldatas.clear(); // 필요

            pageNum = 0; // 초기화이고 , 1
            spinnerIndex = position;
//            Log.d("mawang", "MainVPAllSurveyFragment ItemSelectListener onItemSelected - spinnerIndex = "+spinnerIndex);

            switch (position) {
                case 0: {
                    getAllSurveyList(RequestType.ALL, pageNum);
                    break;
                }
                case 1: {
                    getAllSurveyList(RequestType.RECENT, pageNum);
                    break;
                }
                case 2: {
                    getAllSurveyList(RequestType.RECOMMEND, pageNum);
                    break;
                }
                // 어떤걸 또 랭크화할까?
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }

    public class ScrollListener extends RecyclerView.OnScrollListener { // 오 스크롤 최적화인가
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            // 완전히 view 가 보여질때의 인덱스이다.
            int firstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            int lastVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            //int totalCount = recyclerView.getAdapter().getItemCount(); // 현재 보여지는 view 개수이겠지?
            // 아님, 현재 존재하는 view 개수임 , 많으면 이게 맞음
//            Log.d("mawang", "MainVPAllSurveyFragment ScrollListener onScrolled - firstVisiblePosition = "+firstVisiblePosition);
//            Log.d("mawang", "MainVPAllSurveyFragment ScrollListener onScrolled - lastVisiblePosition = "+lastVisiblePosition);
//            Log.d("mawang", "MainVPAllSurveyFragment ScrollListener onScrolled - totalCount = "+totalCount);

            if (lastVisiblePosition == totalCount - 1) { // 0부터 시작하니까
                pageNum++;
//                Log.d("mawang", "MainVPAllSurveyFragment ScrollListener onScrolled - page up = "+pageNum);
                switch (spinnerIndex) {
                    // 서버통신인데
                    case 0: {
                        getAllSurveyList(RequestType.ALL, pageNum);
                        break;
                    }
                    case 1: {
                        getAllSurveyList(RequestType.RECENT, pageNum);
                        break;
                    }
                    case 2: {
                        getAllSurveyList(RequestType.RECOMMEND, pageNum);
                        break;
                    }
                }
            }
            if (firstVisiblePosition == 0 && pageNum >0) {
                pageNum--;
                if(pageNum<0){pageNum=0;}
                Log.d("mawang", "MainVPAllSurveyFragment ScrollListener onScrolled - page down = "+pageNum);
                switch (spinnerIndex) {
                    // 서버통신인데
                    case 0: {
                        getAllSurveyList(RequestType.ALL, pageNum);
                        break;
                    }
                    case 1: {
                        getAllSurveyList(RequestType.RECENT, pageNum);
                        break;
                    }
                    case 2: {
                        getAllSurveyList(RequestType.RECOMMEND, pageNum);
                        break;
                    }
                }
            }
        }
    }


    public void getAllSurveyList(String type, int page) {
        // Retrofit 개쩔어 ㅈㄴ 편해
//        Log.d("mawang", "MainVPAllSurveyFragment getAllSurveyList - type/page = "+type+"/"+page);

        RetrofitApi.getService().getAllSurveyList(type, page).enqueue(new retrofit2.Callback<ArrayList<SurveyDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<SurveyDTO>> call, retrofit2.Response<ArrayList<SurveyDTO>> response) {
                surveysAdapter.addDatas(response.body());
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<SurveyDTO>> call, Throwable t) {
                Log.d("mawang", "MainVPAllSurveyFragment getAllSurveyList onFailure ");
                t.printStackTrace();
            }
        });
    }

}
