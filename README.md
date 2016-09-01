# SlideIndicator
Indicator for viewpager which you can drag around. 

It come from [dribbble](https://dribbble.com/shots/2393786-Event-App-Concept).

Usage
=====
Initialize your viewpager and set it into SlideIndicator.


```
@Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    SlideIndicator indicator = (SlideIndicator) findViewById(R.id.indicator);
    ViewPager pager = (ViewPager) findViewById(R.id.pager);
    pager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
    indicator.setViewPager(pager);
  }
  ```




It is like:

![SlideIndicator](/demo.gif)

have fun.
