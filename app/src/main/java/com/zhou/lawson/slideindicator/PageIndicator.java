package com.zhou.lawson.slideindicator;

import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lawson on 16/8/29.
 *
 * from viewpagerindicator by jack wharton
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener, View.OnTouchListener {
  /**
   * Bind the indicator to a ViewPager.
   */
  void setViewPager(ViewPager view);

  /**
   * Bind the indicator to a ViewPager.
   */
  void setViewPager(ViewPager view, int initialPosition);

  /**
   * <p>Set the current page of both the ViewPager and indicator.</p>
   *
   * <p>This <strong>must</strong> be used if you need to set the page before
   * the views are drawn on screen (e.g., default start page).</p>
   */
  void setCurrentItem(int item);

  /**
   * Notify the indicator that the fragment list has changed.
   */
  void notifyDataSetChanged();

  boolean onTouch(View var1, MotionEvent var2);
}