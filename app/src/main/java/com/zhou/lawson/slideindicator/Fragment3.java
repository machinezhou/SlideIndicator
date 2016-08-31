package com.zhou.lawson.slideindicator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by lawson on 16/8/30.
 */
public class Fragment3 extends Fragment {

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    TextView textView = new TextView(getContext());
    textView.setText("fragment 3");
    textView.setGravity(Gravity.CENTER);
    textView.setBackgroundColor(Color.parseColor("#ffa2a2bf"));
    textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
    return textView;
  }
}
