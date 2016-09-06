package com.zhou.lawson.slideindicator;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by lawson on 16/9/6.
 *
 * todo make better refresh
 */
public class Indicator extends FrameLayout {

  private float distance;
  private int width;
  private int height;
  private TextView textView;

  public Indicator(Context context) {
    this(context, null);
  }

  public Indicator(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public Indicator(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setClickable(false);
  }

  public void setText(CharSequence text) {
    if (textView != null) {
      textView.setText(text);
    } else {
      textView = getDefaultIndicatorTextView(text);
      addView(textView);
    }
  }

  private TextView getDefaultIndicatorTextView(CharSequence text) {
    TextView textView = new TextView(getContext());
    textView.setText(text);
    textView.setTextSize(14);
    textView.getPaint().setFakeBoldText(true);
    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.indicator_text_color));
    textView.setGravity(Gravity.CENTER);
    textView.setClickable(false);
    textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_indicator));
    return textView;
  }

  public void setSize(int w, int h) {
    if (width <= 0 && height <= 0) {
      width = w;
      height = h;
      LayoutParams params = new LayoutParams(w, h);
      textView.setLayoutParams(params);
      textView.invalidate();
      setLayoutParams(params);
      invalidate();
    }
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
  }

  public void setDownY(float distance) {
    textView.setTranslationY(textView.getTranslationY() + distance);
  }

  public void setBounds(int left, int right) {
    Drawable drawable = textView.getBackground();
    Rect rect = drawable.getBounds();
    left += rect.left;
    right = rect.right - right;
    if (right - left <= rect.bottom - rect.top) {
      downFall();
    } else {
      drawable.setBounds(left, rect.top, right, rect.bottom);
      textView.setBackground(drawable);
    }
  }

  private void downFall() {

  }
}
