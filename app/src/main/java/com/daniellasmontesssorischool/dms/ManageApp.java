package com.daniellasmontesssorischool.dms;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

public class ManageApp extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    //Declaring All The Variables Needed
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Drawable oldBackground = null;


    private int currentColor;
    private MyPagerAdapter adapter;

    /*public void onBackPressed() {

        Fragment fragmenthome   = getSupportFragmentManager().findFragmentByTag("fraghome");
        Fragment fragmentass = getSupportFragmentManager().findFragmentByTag("fragassignment");
        Fragment fragmentarticle   = getSupportFragmentManager().findFragmentByTag("fragarticle");
        Fragment fragmentupload   = getSupportFragmentManager().findFragmentByTag("fragupload");
        Fragment fragmentcontact   = getSupportFragmentManager().findFragmentByTag("fragcontact");

        if (fragmentupload != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentupload).commit();

            Fragment frag = new FragmentArticle();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fragarticle");
            fragmentTransaction.commit();

        }else if (fragmentarticle != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentarticle).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();
        }else{
            Intent intent = new Intent(ManageApp.this, Home.class);
            startActivity(intent);
            finish();
        }

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_app);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        //setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        //tabLayout.setupWithViewPager(viewPager);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/


        toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (viewPager != null) {
            adapter = new MyPagerAdapter(getSupportFragmentManager());
        }
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorHeight(8);
       /*
       */
    }

 /*   private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.accent_material_light)), "CAT");
        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.ripple_material_light)), "DOG");
        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.button_material_dark)), "MOUSE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
*/

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "ARTICLES", "NEWS", "CALENDAR", "EVENT", "ASSIGNMENT"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    // BrowseFrag Category fragment activity
                    return new FragmentManageTeachers();
                case 1:
                    // Plan Code fragment activity
                    //return new FragmentTransfer();
                case 2:
                    // Take QuizFrag fragment activity
                    //return new FragmentUssd();
                case 3:
                    // HelpFrag fragment activity
                    //return new FragmentHistory();
                /*case 4:
                    // About fragment activity
                    return new AboutFrag();*/
            }
            return null;
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int pageint) {
        // TODO Auto-generated method stub

		/*ft = getSupportFragmentManager().beginTransaction();
		// Replace the contents of the container with the new fragment
					ft.replace(R.id.frameLay, new AboutFrag());
					ft.commit();*/

        /*int c = 0;
        if (pageint == 1)
            c= Color.parseColor(col2);
        else if (pageint == 2){
            getActionBar().setIcon(R.drawable.quiz);
            c = Color.parseColor(col3);
        }else if (pageint == 3){
            getActionBar().setIcon(R.drawable.help);
            c = Color.parseColor(col4);
        }else if (pageint == 4){
            getActionBar().setIcon(R.drawable.about);
            c = Color.parseColor(col5);
        }else{
            getActionBar().setIcon(R.drawable.browse);
            c = Color.parseColor(col1);
        }
        changeColor(c);*/

    }

}
