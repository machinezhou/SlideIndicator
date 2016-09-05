package com.zhou.lawson.slideindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by lawson on 16/9/3.
 */
public class TabLayout extends LinearLayout {

  private static final int BORDERLINE_COLOR = Color.parseColor("#FF4E8186");
  private static final float STROKE_WIDTH = 4.2f;

  private Paint paint;
  private Path borderPath;
  private RectF startArcRect;
  private RectF endArcRect;
  private int height;
  private int fixedHeight;
  private int radius;
  private boolean fixed = false;

  public TabLayout(Context context) {
    this(context, null);
  }

  public TabLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    paint = new Paint();
    borderPath = new Path();
    paint.setColor(BORDERLINE_COLOR);
    paint.setStrokeWidth(STROKE_WIDTH);
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);
    paint.setDither(true);
    paint.setStrokeJoin(Paint.Join.ROUND);

    setBackgroundColor(ContextCompat.getColor(context, R.color.tab_bg));
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    height = h;

    resetChild(w, h);
  }

  private void resetChild(int w, int h) {
    if (!fixed) {
      radius = h / 2;
      startArcRect = new RectF(getPaddingLeft() + STROKE_WIDTH, getPaddingTop() + STROKE_WIDTH,
          getPaddingLeft() + 2 * radius + STROKE_WIDTH, h - getPaddingBottom() - STROKE_WIDTH);
      endArcRect = new RectF(w - getPaddingRight() - 2 * radius - STROKE_WIDTH,
          getPaddingTop() + STROKE_WIDTH, w - getPaddingRight() - STROKE_WIDTH,
          h - getPaddingBottom() - STROKE_WIDTH);

      final int childCount = getChildCount();
      for (int i = 0; i < childCount; i++) {
        final View child = getChildAt(i);
        LinearLayout.LayoutParams params = (LayoutParams) child.getLayoutParams();
        params.height = h;
        child.setLayoutParams(params);
      }
      if (h > 0) {
        fixedHeight = h;
        fixed = true;
      }
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    drawStrokePath(canvas);
    super.onDraw(canvas);
  }

  private void drawStrokePath(Canvas canvas) {
    double distanceToAngle = distanceToAngle();
    borderPath.reset();
    borderPath.moveTo(getPaddingLeft() + radius + STROKE_WIDTH, getPaddingTop() + STROKE_WIDTH);
    borderPath.lineTo(getMeasuredWidth() - getPaddingRight() - radius - STROKE_WIDTH,
        getPaddingTop() + STROKE_WIDTH);
    borderPath.arcTo(endArcRect, -90, (float) (180 - distanceToAngle));
    borderPath.quadTo(getMeasuredWidth() / 2, height,
        (float) getLastPointXAfterArcTo(distanceToAngle),
        (float) getLastPointYAfterArcTo(distanceToAngle));
    borderPath.moveTo(getPaddingLeft() + radius + STROKE_WIDTH, getPaddingTop() + STROKE_WIDTH);
    borderPath.arcTo(startArcRect, -90, -(float) (180 - distanceToAngle));
    canvas.drawPath(borderPath, paint);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
  }

  private double distanceToAngle() {
    return Math.toDegrees(Math.atan(
        ((double) (height - fixedHeight)) / ((double) (getMeasuredWidth()
            - getPaddingRight()
            - radius
            - STROKE_WIDTH
            - (getMeasuredWidth() / 2)))));
  }

  private double getLastPointYAfterArcTo(double angle) {
    return fixedHeight - getPaddingBottom() - STROKE_WIDTH - (radius - radius * Math.cos(
        angle * Math.PI / 180));
  }

  private double getLastPointXAfterArcTo(double angle) {
    return getPaddingLeft() + STROKE_WIDTH + radius - radius * Math.sin(angle * Math.PI / 180);
  }

  public float getStrokeWidth() {
    return STROKE_WIDTH;
  }
}
