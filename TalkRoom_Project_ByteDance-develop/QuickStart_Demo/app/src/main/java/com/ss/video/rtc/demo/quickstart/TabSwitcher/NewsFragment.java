package com.ss.video.rtc.demo.quickstart.TabSwitcher;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ss.rtc.demo.quickstart.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends BaseFragment {
    private RecyclerView newsRV;
    private List<NewsItem> newsList;

    private NewsRecyclerAdapter newsRVAdapter;


    private static final String TAG = "NewsFragment";

    @Override
    protected String getLogTag() {
        return TAG;
    }

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news, container, false);

        newsRV = view.findViewById(R.id.newsRecyclerView);
        newsList =new ArrayList<>();
        addNewsItem();
        newsRVAdapter = new NewsRecyclerAdapter(getActivity(), newsList, this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        newsRV.setLayoutManager(manager);
        newsRV.setAdapter(newsRVAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Loading in...
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void addNewsItem(){
        newsList.add(new NewsItem("1. 梅西：你在看什么，蠢货","https://m.hupu.com/bbs/56876571.html"));
        newsList.add(new NewsItem("2. 世界杯前瞻：英法大战一触即发，摩洛哥能否改写非洲球队最高8强的历史？","https://m.hupu.com/bbs/56875954.html"));
        newsList.add(new NewsItem("3. [每日之星]利瓦科维奇当选世界杯今日最佳球员","https://m.hupu.com/bbs/56876192.html"));
        newsList.add(new NewsItem("4. [话题区圆桌]荷阿之战共计17张黄牌！世界杯你还知道哪些奇葩纪录","https://m.hupu.com/bbs/56879760.html"));
        newsList.add(new NewsItem("5. [流言板]蒂特：克罗地亚除了进球没伤害我们；巴西新一代正不断涌现","https://m.hupu.com/bbs/56881735.html"));
        newsList.add(new NewsItem("6. [流言板]内马尔：这对我来说像是噩梦；会慎重考虑自己国家队的未来","https://m.hupu.com/bbs/56881367.html"));
        newsList.add(new NewsItem("7. 点球之夜巴西、荷兰出局！提起点球大战，那些经典是否还历历在目？","https://m.hupu.com/bbs/56877919.html"));
        newsList.add(new NewsItem("8. [流言板]阿根廷进入世界杯半决赛后，梅西有望成为世界杯历史出场王","https://m.hupu.com/bbs/56881101.html"));
    }
}