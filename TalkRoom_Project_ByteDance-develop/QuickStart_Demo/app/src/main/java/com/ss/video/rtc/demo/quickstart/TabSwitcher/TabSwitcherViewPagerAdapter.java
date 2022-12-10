package com.ss.video.rtc.demo.quickstart.TabSwitcher;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class TabSwitcherViewPagerAdapter extends FragmentStateAdapter {
    private static final int FRAGMENTS_COUNT = 2;
    public static final int FRAGMENT_ENTER_ROOM = 0;
    public static final int FRAGMENT_NEWS = 1;

    public TabSwitcherViewPagerAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
    }
    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        Log.i("tabSwitcher:", "create Fragment @pos" + position);
        switch (position) {
            case FRAGMENT_ENTER_ROOM:
                return  new EnterRoomFragment();
            case FRAGMENT_NEWS:
                return  new NewsFragment();
            default:
                return  new Fragment();
        }
    }
    @Override
    public int getItemCount() {
        return FRAGMENTS_COUNT;
    }
}
