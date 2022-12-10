package com.ss.video.rtc.demo.quickstart.TabSwitcher;

import static androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ss.rtc.demo.quickstart.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabSwitcherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabSwitcherFragment extends Fragment {
    private static final String TAG = "TabSwitcherFragment";
    private static final String TITLE_ENTER_ROOM = "进房";
    private static final String TITLE_NEWS = "新闻";
    private static final int TAB_COUNTS = 2;

    private static final String ARG_USER_ID = "defaultUserID";
    private String mUserID;

    private final String[] tabTitles = new String[TAB_COUNTS];



    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;

    public TabSwitcherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabSwitcherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabSwitcherFragment newInstance(String param1, String param2) {
        TabSwitcherFragment fragment = new TabSwitcherFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static TabSwitcherFragment newInstance(String userId) {
        TabSwitcherFragment fragment = new TabSwitcherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID,userId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserID = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {        // set ViewPagerAdapter
        View view =  inflater.inflate(R.layout.fragment_tab_switcher, container, false);
        mViewPager = view.findViewById(R.id.view_pager_main);
        mViewPager.setAdapter(new TabSwitcherViewPagerAdapter(this));
        mViewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT_DEFAULT);

        // create connection between mViewPager & mTabLayout
        tabTitles[TabSwitcherViewPagerAdapter.FRAGMENT_ENTER_ROOM] = TITLE_ENTER_ROOM;
        tabTitles[TabSwitcherViewPagerAdapter.FRAGMENT_NEWS] = TITLE_NEWS;

        mTabLayout = view.findViewById(R.id.tab_layout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                mTabLayout,
                mViewPager,
                true,
                false,
                (tab,position) -> tab.setText(tabTitles[position]));
        tabLayoutMediator.attach();

        return view;   }
}