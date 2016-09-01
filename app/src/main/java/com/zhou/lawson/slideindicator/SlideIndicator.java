package com.zhou.lawson.slideindicator;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lawson on 16/8/27.
 */
public class SlideIndicator extends FrameLayout implements PageIndicator {

  private static final CharSequence EMPTY_TITLE = "";

  private LinearLayout tabLayout;
  private FrameLayout indicator;
  private TextView indicatorTextView;
  private ViewDragHelper helper;
  private ViewPager viewPager;
  private OnTouchListener pagerTouchListener;

  private boolean isIndicatorTouched = false;

  private int startPointX;
  private int endPointX;
  private int indicatorLeft = 0;
  private int mSelectedTabIndex = 0;
  private int width;
  private int size;

  public SlideIndicator(Context context) {
    this(context, null);
  }

  public SlideIndicator(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SlideIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    setBackgroundResource(R.drawable.shape_corner);
    helper = ViewDragHelper.create(this, 1.0f, callback);

    addTabLayout();
  }

  private void addTabLayout() {
    tabLayout = new LinearLayout(getContext());
    tabLayout.setLayoutParams(
        new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    addView(tabLayout);
  }

  @Override public void computeScroll() {
    if (helper.continueSettling(true)) {
      invalidate();
    }
  }

  public void setOnPageTouchListener(OnTouchListener l) {
    pagerTouchListener = l;
  }

  @Override public void setViewPager(ViewPager view) {
    if (viewPager == view) {
      return;
    }
    if (viewPager != null) {
      viewPager.removeOnPageChangeListener(this);
      viewPager.setOnTouchListener(null);
    }
    final PagerAdapter adapter = view.getAdapter();
    if (adapter == null) {
      throw new IllegalStateException("ViewPager does not have adapter instance.");
    }
    viewPager = view;
    viewPager.addOnPageChangeListener(this);
    viewPager.setOnTouchListener(this);
    notifyDataSetChanged();
  }

  @Override public void setViewPager(ViewPager view, int initialPosition) {
    setViewPager(view);
    setCurrentItem(initialPosition);
  }

  @Override public void setCurrentItem(int index) {
    if (viewPager == null) {
      throw new IllegalStateException("ViewPager has not been bound.");
    }
    slideTo(index);
  }

  @Override public boolean onTouch(View var1, MotionEvent ev) {
    isIndicatorTouched = false;
    if (pagerTouchListener != null) {
      pagerTouchListener.onTouch(var1, ev);
    }
    return false;
  }

  @Override public void notifyDataSetChanged() {
    tabLayout.removeAllViews();
    removeView(indicator);
    PagerAdapter adapter = viewPager.getAdapter();
    size = adapter.getCount();
    if (size < 2) {
      throw new IllegalArgumentException("at least 2 tabs");
    }
    for (int i = 0; i < size; i++) {
      CharSequence title = adapter.getPageTitle(i);
      if (title == null) {
        title = EMPTY_TITLE;
      }
      addTab(i, title);
    }

    addIndicator();
    viewPager.setCurrentItem(mSelectedTabIndex);
    requestLayout();
  }

  private void addIndicator() {
    indicatorTextView = getDefaultIndicatorTextView(getIndicatorText(mSelectedTabIndex));

    indicator = new FrameLayout(getContext());
    indicator.setBackgroundResource(R.drawable.shape_indicator);
    indicator.setClickable(false);
    indicator.addView(indicatorTextView);
    addView(indicator);
  }

  private void addTab(int index, CharSequence text) {
    TabView tabView = new TabView(getContext());
    tabView.mIndex = index;
    tabView.setText(text);
    tabView.setTextSize(14);
    tabView.getPaint().setFakeBoldText(true);
    tabView.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
    tabView.setGravity(Gravity.CENTER);
    tabView.setClickable(true);
    tabView.setOnClickListener(listener);
    tabLayout.addView(tabView, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    width = w;

    resetPoints(mSelectedTabIndex);
    indicator.setLayoutParams(new LayoutParams(width / size, ViewGroup.LayoutParams.MATCH_PARENT));
    indicator.invalidate();
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        isIndicatorTouched = helper.isViewUnder(indicator, (int) ev.getX(), (int) ev.getY());
    }
    return helper.shouldInterceptTouchEvent(ev);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    helper.processTouchEvent(event);
    return true;
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    final int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = getChildAt(i);
      if (child == indicator) {
        int childTop = getPaddingTop();
        final int childBottom = childTop + getMeasuredHeight();
        final int childLeft = indicatorLeft;
        final int childRight = childLeft + child.getMeasuredWidth();
        child.layout(childLeft, childTop, childRight, childBottom);
      }
    }
  }

  /**
   * handle drag event
   */
  final ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
    @Override public boolean tryCaptureView(View child, int pointerId) {
      return child == indicator;
    }

    @Override public int clampViewPositionHorizontal(View child, int left, int dx) {
      final int leftBound = getPaddingLeft();
      final int rightBound = getWidth() - indicator.getWidth() - leftBound;
      return Math.min(Math.max(left, leftBound), rightBound);
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      super.onViewPositionChanged(changedView, left, top, dx, dy);
    }

    @Override public void onViewReleased(View releasedChild, float xvel, float yvel) {
      if (indicator == releasedChild) {
        indicatorLeft = releasedChild.getLeft();

        int index = releasedChild.getLeft() / (width / size);
        resetPoints(index);
        if (releasedChild.getLeft() > getCurrentTabCenterX()) {
          helper.settleCapturedViewAt(endPointX, getPaddingTop());
          mSelectedTabIndex = index + 1;
        } else {
          helper.settleCapturedViewAt(startPointX, getPaddingTop());
          mSelectedTabIndex = index;
        }
        setIndicatorText(mSelectedTabIndex);
        viewPager.setCurrentItem(mSelectedTabIndex);

        invalidate();
      }
    }

    @Override public int getViewHorizontalDragRange(View child) {
      return indicator == child ? child.getWidth() : 0;
    }
  };

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    if (isIndicatorTouched) {
      return;
    }
    slideBy(position, positionOffset);
  }

  @Override public void onPageSelected(int position) {
    mSelectedTabIndex = position;
    setIndicatorText(position);
  }

  @Override public void onPageScrollStateChanged(int state) {

  }

  /**
   * get current tab center X
   */
  private int getCurrentTabCenterX() {
    return startPointX + ((endPointX - startPointX) / 2);
  }

  /**
   * listener of tab click
   */
  final OnClickListener listener = new OnClickListener() {
    @Override public void onClick(View v) {
      if (isIndicatorTouched) {
        return;
      }
      slideTo(((TabView) v).getIndex());
    }
  };

  /**
   * reset indicator position
   */
  private void resetPoints(int index) {
    startPointX = getPaddingLeft() + (width / size) * index;
    endPointX = startPointX + (width / size);
  }

  /**
   * get indicator text from page titles
   */
  private String getIndicatorText(int index) {
    return viewPager.getAdapter().getPageTitle(index).toString();
  }

  /**
   * set indicator text
   */
  private void setIndicatorText(String text) {
    if (indicatorTextView != null) {
      indicatorTextView.setText(text);
    }
  }

  /**
   * set indicator text by index
   */
  private void setIndicatorText(int index) {
    if (indicatorTextView != null) {
      setIndicatorText(getIndicatorText(index));
    }
  }

  /**
   * let indicator slide to specific position by index
   */
  private void slideTo(int index) {
    mSelectedTabIndex = index;
    indicatorLeft = endPointX;
    setIndicatorText(index);
    viewPager.setCurrentItem(index);

    if (index < 1) {
      resetPoints(0);
      helper.smoothSlideViewTo(indicator, startPointX, getPaddingTop());
    } else {
      resetPoints(index - 1);
      helper.smoothSlideViewTo(indicator, endPointX, getPaddingTop());
    }
  }

  /**
   * keep indicator sliding by position and positionOffset
   */
  private void slideBy(int position, float positionOffset) {
    indicatorLeft = (int) (getPaddingLeft() + (width / size) * (position + positionOffset));
    requestLayout();
  }

  private TextView getDefaultIndicatorTextView(CharSequence text) {
    if (indicatorTextView != null) {
      return indicatorTextView;
    }
    TextView textView = new TextView(getContext());
    textView.setText(text);
    textView.setTextSize(14);
    textView.getPaint().setFakeBoldText(true);
    textView.setTextColor(Color.WHITE);
    textView.setGravity(Gravity.CENTER);
    FrameLayout.LayoutParams params = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
        FrameLayout.LayoutParams.MATCH_PARENT);
    params.gravity = Gravity.CENTER;
    textView.setLayoutParams(params);
    textView.setClickable(false);
    return textView;
  }

  private class TabView extends TextView {
    private int mIndex;

    public TabView(Context context) {
      super(context);
    }

    public int getIndex() {
      return mIndex;
    }
  }
}
