package com.zhou.lawson.slideindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private static final String[] TITLES = {
      "Home", "Notifications", "Messages"
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    SlideIndicator indicator = (SlideIndicator) findViewById(R.id.indicator);
    ViewPager pager = (ViewPager) findViewById(R.id.pager);
    pager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
    indicator.setViewPager(pager);
  }

  final class TabPagerAdapter extends FragmentPagerAdapter {
    TabPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public CharSequence getPageTitle(int position) {
      return TITLES[position];
    }

    @Override public int getCount() {
      return TITLES.length;
    }

    @Override public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return new Fragment1();
        case 1:
          return new Fragment2();
        case 2:
          return new Fragment3();
        default:
          throw new UnsupportedOperationException("position = " + position);
      }
    }
  }
}
