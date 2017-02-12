package com.daniellasmontesssorischool.dms;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentContact extends Fragment {



   /* ArrayList<String> articleContent = new ArrayList<>();


    RecyclerView recyclerView;
    ArrayList<RowItems> itemsList = new ArrayList<>();
    RecyclerViewAdapter adapter;*/
    LinearLayout _retry, _call;

    RelativeLayout _callus, _getdirection;
    Intent calldms;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 4;

    Button _upload, _directions;

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_contact, container, false);


        _callus = (RelativeLayout)rootView.findViewById(R.id._callus);//bottom button

        _call = (LinearLayout)rootView.findViewById(R.id._calllay); //middle layout

        _getdirection = (RelativeLayout)rootView.findViewById(R.id._getdirections);
        _directions = (Button)rootView.findViewById(R.id._getDir);

        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.anim_toolbar);
        //toolbar.setTitle("Contact DMS");
        ((Home)getActivity()).setSupportActionBar(toolbar);
        /*if(((Home)getActivity()).getSupportActionBar() != null)
            ((Home)getActivity()).getSupportActionBar().setTitle("Contact DMS");*/
        //Start the collapsing toolbar here
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Contact DMS");
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));


        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.app_img10);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                collapsingToolbar.setContentScrimColor(mutedColor);
            }
        });*/

        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.app_img10);
        Palette.from(bitmap).maximumColorCount(numberOfColors).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // Get the "vibrant" color swatch based on the bitmap
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant != null) {
                    // Set the background color of a layout based on the vibrant color
                    containerView.setBackgroundColor(vibrant.getRgb());
                    // Update the title TextView with the proper text color
                    titleView.setTextColor(vibrant.getTitleTextColor());
                }
            }
        });*/

        /*setSupportActionBar(mActionBarToolbar);
        if(getSupportActionBar() != null)
        getSupportActionBar().setTitle("My title");*/

        _call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calldms = new Intent(Intent.ACTION_CALL);
                calldms.setData(Uri.parse("tel:+2348023438285"));
                //http://developer.android.com/training/permissions/requesting.html
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CALL_PHONE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    }

                }else{
                    startActivity(calldms);
                }
            }
        });

        _callus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                calldms = new Intent(Intent.ACTION_CALL);
                calldms.setData(Uri.parse("tel:+2348023438285"));
                //http://developer.android.com/training/permissions/requesting.html
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CALL_PHONE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    }

                }else{
                    startActivity(calldms);
                }

                return true;
            }
        });


        _getdirection.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(((Home)getActivity()).netCheck.isConnected()) {
                    Fragment frag = new FragmentMap();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id._frame_home, frag, "fragmap");
                    fragmentTransaction.commit();
                }else{
                    ((Home)getActivity()).dialogError("Your network seems to be disabled. Please try again!", "manageteachersdismiss");
                }
                return true;
            }
        });

        _directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((Home)getActivity()).netCheck.isConnected()) {
                    Fragment frag = new FragmentMap();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id._frame_home, frag, "fragmap");
                    fragmentTransaction.commit();
                }else{
                    ((Home)getActivity()).dialogError("Your network seems to be disabled. Please try again!", "manageteachersdismiss");
                }

            }
        });

        return rootView;
    }

    /*public static String getCurrentTimeStamp(String dates) {
        long date = Long.parseLong(dates);
        Date d=new Date(((long)date)*1000);
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        //SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss a", Locale.ENGLISH);
        sdfDate.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
        String strDate = sdfDate.format(d);
        return strDate;
    }*/

}
