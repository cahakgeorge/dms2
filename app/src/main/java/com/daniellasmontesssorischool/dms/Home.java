package com.daniellasmontesssorischool.dms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.daniellasmontesssorischool.dms.CoreMethods.NetworkCheck;
import com.daniellasmontesssorischool.dms.app.AppController;
import com.daniellasmontesssorischool.dms.app.Config;
import com.daniellasmontesssorischool.dms.utils.DatabaseHandler;
import com.daniellasmontesssorischool.dms.utils.NotificationUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Home extends AppCompatActivity implements OnMapReadyCallback {

    MaterialDialog dialog;

    static final LatLng school = new LatLng(6.658116, 3.305341);

    private DrawerLayout drawerLayout;
    public Toolbar toolbar;
    public TextView toolbarTitle;

    SharedPreferences pref;
    public static final String PREFS_NAME = "DmsPrefsFile";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static String USER_FULLNAME = "";
    public static String USER_SEX = "";
    public static String USER_PHONE = "";
    public static String USER_CLASS = "";
    public static String USER_EMAIL = "";

    public static String USER_IMAGEID = "";
    public static String USER_HEADTEACHER = "";
    public static String USER_IMAGEID_HEADT = "";
    public static String USER_IMAGEID_PROPR = "";

    ArrayList<String> articleview = new ArrayList<>();
    ArrayList<String> newsview = new ArrayList<>();
    ArrayList<String> assignmentview = new ArrayList<>();
    //ArrayList<String> calendarview = new ArrayList<>();

    private static String PREF_LOGINAME = "pref_login_name";
    private static String PREF_LOGINSEX = "pref_login_sex";
    private static String PREF_LOGINPHONE = "pref_login_phone";
    private static String PREF_LOGINCLASS = "pref_login_class";
    private static String PREF_LOGINEMAIL = "pref_login_email";
    private static String PREF_IMAGEID = "pref_login_imageid";
    private static String PREF_HEADTEACHER = "pref_login_headteacher";
    private static String PREF_IMAGEID_HEADT = "pref_login_imghead";
    private static String PREF_IMAGEID_PROP = "pref_login_imgprop";


    public static String TERM = "";
    public static String WEEK = "";
    public static String YEAR = "";

    private static String PREF_TERM = "term";
    private static String PREF_WEEK="week";
    private static String PREF_YEAR="year";

    private static String PREF_PREVIOUSLOGIN = "pref_prevlogin";


    public static String TEACHER_FULLNAME = "";
    public static String TEACHER_SEX = "";
    public static String TEACHER_POSITION = "";
    public static String TEACHER_USERNAME = "";
    public static String TEACHER_TYPE = "";

    public static String TEACHER_STARTDATE = "";
    public static String WhosLoggedIn = "";

    private static String PREF_LOGINAME_TEACHER="pref_login_name_teacher";
    private static String PREF_LOGINSEX_TEACHER = "pref_login_sex_teacher";
    private static String PREF_POSITION_TEACHER = "pref_position_teacher";
    private static String PREF_USERNAME_TEACHER = "pref_login_username";

    private static String PREF_USERTYPE_TEACHER="usertype_teacher";

    private static String PREF_STARTDATE_TEACHER = "pref_startdate_teacher";



    private static String PREF_QUIZCHAMP = "pref_quizchamp";
    protected static String[][] quiz;
    protected static int quizcount = 0;
    protected static int scorecount = 0;

    public static String usertype = null;


    //Quiz selection
    public static String quiztype = "";
    public static String quizdiff = "";

    NetworkCheck netCheck;

    public void onBackPressed() {
        AppController.getInstance().getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });

        Fragment fragmenthome = getSupportFragmentManager().findFragmentByTag("fraghome");

        Fragment fragmentass = getSupportFragmentManager().findFragmentByTag("fragassignment");
        Fragment fragmentarticle = getSupportFragmentManager().findFragmentByTag("fragarticle");
        Fragment fragmentquiz = getSupportFragmentManager().findFragmentByTag("fragquiz");
        Fragment fragmentquizstart = getSupportFragmentManager().findFragmentByTag("fragquizstart");

        Fragment fragmentupload = getSupportFragmentManager().findFragmentByTag("fragupload");
        Fragment fragmentcontact = getSupportFragmentManager().findFragmentByTag("fragcontact");
        Fragment fragmentevent = getSupportFragmentManager().findFragmentByTag("fragevent");
        Fragment fragmentassignmentview = getSupportFragmentManager().findFragmentByTag("fragassignmentview");
        Fragment fragmentcalendar = getSupportFragmentManager().findFragmentByTag("fragcalendar");
        Fragment fragmentmap = getSupportFragmentManager().findFragmentByTag("fragmap");
        Fragment fragmentarticleview = getSupportFragmentManager().findFragmentByTag("fragarticleview");

        Fragment fragmentnews = getSupportFragmentManager().findFragmentByTag("fragnews");
        Fragment fragmentnewsview = getSupportFragmentManager().findFragmentByTag("fragnewsview");

        Fragment fragmentmteacher = getSupportFragmentManager().findFragmentByTag("fragmteachers");

        Fragment fragmentmassignments = getSupportFragmentManager().findFragmentByTag("fragmassignments");

        Fragment fragmentmstudents = getSupportFragmentManager().findFragmentByTag("fragmstudents");

        Fragment fragmentmevents = getSupportFragmentManager().findFragmentByTag("fragmevents");
        Fragment fragmentmnews = getSupportFragmentManager().findFragmentByTag("fragmnews");

        //Revert name to home
        toolbarTitle.setText("Home");

        if (fragmentupload != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentupload).commit();

            toolbarTitle.setText("Articles");

            Fragment frag = new FragmentArticle();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fragarticle");
            fragmentTransaction.commit();

        } else if (fragmentarticleview != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentarticleview).commit();

            toolbarTitle.setText("Articles");

            Fragment frag = new FragmentArticle();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        } else if (fragmentarticle != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentarticle).commit();

            toolbarTitle.setText("Home");

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        } else if (fragmentquiz != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentquiz).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        } else if (fragmentquizstart != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentquizstart).commit();

            Fragment frag = new FragmentQuiz();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fragquiz");
            fragmentTransaction.commit();

        } else if (fragmentassignmentview != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentassignmentview).commit();

            Fragment frag = new FragmentAssignment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fragassignment");
            fragmentTransaction.commit();

        } else if (fragmentass != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentass).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        } else if (fragmentmap != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentmap).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        } else if (fragmentnews != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentnews).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        } else if (fragmentnewsview != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentnewsview).commit();

            Fragment frag = new FragmentNews();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fragnews");
            fragmentTransaction.commit();

        } else if (fragmentcontact != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentcontact).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        } else if (fragmentevent != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentevent).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        } else if (fragmentcalendar != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentcalendar).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        } else if (fragmentmteacher != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentmteacher).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        }else if (fragmentmstudents != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentmstudents).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        }else if (fragmentmassignments != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentmassignments).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        }else if (fragmentmnews != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentmnews).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        }else if (fragmentmevents != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentmevents).commit();

            Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
            fragmentTransaction.commit();

        } else if (fragmenthome != null) {
            //getSupportFragmentManager().beginTransaction().remove(fragmenthome).commit();

            confirmExit();

            /*Fragment frag = new FragmentHome();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id._frame_home, frag, "fragarticle");
            fragmentTransaction.commit();
*/
        } else {
            confirmExit();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        netCheck = new NetworkCheck(getApplicationContext());

        loadDb();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.navicon);
        //toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Home");

        pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(pref.contains("pref_prevlogin_teacher")) {
            WhosLoggedIn = "teacher";
            getTeacher();
        }else {
            WhosLoggedIn = "student";
            getUser();
        }

        Fragment frag = new FragmentHome();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
        fragmentTransaction.commit();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

               if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    //Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    Log.e("News Addition", "PUSH NOTIFICATION "+message.toString());

                    dialog2("DMS INFO", message);
                    //txtMessage.setText(message);
                }
            }
        };

        initNavigationDrawer();
    }

    public void loadDb() {
        new Thread(new Runnable() {
            public void run() {
                //Do Work here
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());//perform database creation in background
            }
        }).start();
    }

    public void getquiz() {
       /* new Thread(new Runnable() {
            public void run() {*/
        //Do Work here
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());//perform database creation in background
        quiz = db.getQuizData();

        //Toast.makeText(Home.this, "Quiz from db"+quiz.toString(), Toast.LENGTH_LONG).show();

        Log.e("DB QUESTiONS", "QSER Row 9 >> " + quiz[9][0].toString());
           /* }
        }).start();*/
    }

    //fragment already handles this
    /*public void getquizoption(){
        dialog = new MaterialDialog.Builder(Home.this)
                .title("Select Difficulty Level")
                .items(R.array.difficulty)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            quizdiff = "easy";
                        }else if (which == 1) {
                            quizdiff = "medium";
                        }else if (which == 2) {
                            quizdiff = "hard";
                        }else if (which == 3) {
                            quizdiff = "challenge";
                        }

                    }
                })
                //.positiveText("Continue")
                .show();

    }*/

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        CameraPosition googlePlex = CameraPosition.builder()
                .target(school)
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();

        Marker sch = map.addMarker(new MarkerOptions()
                .position(school)
                .title("DMS")
                .snippet("Daniellas Montessori School"));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);

        map.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex)); //for animated camera move ///can also move camera

    }
    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();
       // menu.add("Manage Students");

            if(pref.contains("pref_prevlogin_teacher") && pref.getString("usertype_teacher", "normal").equalsIgnoreCase("admin")) {
                //final SubMenu subMenu = menu.addSubMenu("Manage Teachers");
                //subMenu.add("Manage Teachers");
                //navigationView = (NavigationView)findViewById(R.id.navigation_view);
                //navigationView.inflateMenu(R.menu.menu_navigation_admin);
                usertype = "admin";

                MenuItem menItem4 = menu.add(R.id._admin_group,100015, Menu.NONE, "Manage Events");
                //menItem4.setIcon(R.drawable.dms_logo);

                MenuItem menItem5 = menu.add(R.id._admin_group,100016, Menu.NONE, "Manage News");
                menItem4.setIcon(R.drawable.dms_logo);

                MenuItem menItem1 = menu.add(R.id._admin_group,100012, Menu.NONE, "Manage Assignments");
                menItem1.setIcon(R.drawable.img_test);

                MenuItem menItem2 = menu.add(R.id._admin_group,100013, Menu.NONE, "Manage Students");
                //menItem2.setIcon(R.drawable.dms_logo);

                MenuItem menItem3 = menu.add(R.id._admin_group,100014, Menu.NONE, "Manage Teachers");
                menItem3.setIcon(R.drawable.dms_logo);

            }else if(pref.contains("pref_prevlogin_teacher") && pref.getString("usertype_teacher", "normal").equalsIgnoreCase("normal")){
                usertype = "teacher";
                MenuItem menItem1 = menu.add(R.id._admin_group,100012, Menu.NONE, "Manage Assignments");
                menItem1.setIcon(R.drawable.img_test);
            }else
                usertype = "student";

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                Fragment frag;
                FragmentTransaction fragmentTransaction;
                FragmentManager fragmentManager;
                switch (id){
                    case R.id.home:
                        frag = new FragmentHome();

                        toolbarTitle.setText("Home");

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
                        fragmentTransaction.commit();
                        break;
                    /*case R.id.curriculum:
                        frag = new FragmentCurriculum();

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragcurriculum");
                        fragmentTransaction.commit();
                        break;*/
                    case R.id.calendar:
                        frag = new FragmentCalendar();

                        toolbarTitle.setText("School Calendar");

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragcalendar");
                        fragmentTransaction.commit();
                        break;
                    case R.id.event:
                        frag = new FragmentEvents();

                        toolbarTitle.setText("Events");

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragevent");
                        fragmentTransaction.commit();
                        break;
                    case R.id.news:
                        frag = new FragmentNews();

                        toolbarTitle.setText("School News");

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragnews");
                        fragmentTransaction.commit();
                        break;
                    case R.id.about:
                        frag = new FragmentAbout();

                        toolbarTitle.setText("About DMS");

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragabout");
                        fragmentTransaction.commit();
                        break;
                    case R.id.contact:
                        frag = new FragmentContact();
                        toolbarTitle.setText("Contact DMS");

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragcontact");
                        fragmentTransaction.commit();
                        break;
                    case 100015:
                        frag = new FragmentManageEvents();

                        toolbarTitle.setText("Manage Events");

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragmevents");
                        fragmentTransaction.commit();
                        break;
                    case 100016:
                        frag = new FragmentManageNews();

                        toolbarTitle.setText("Manage News");

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragmnews");
                        fragmentTransaction.commit();
                        break;
                    case 100012:
                        frag = new FragmentManageAssignments();

                        toolbarTitle.setText("Manage Assignments");

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragmassignments");
                        fragmentTransaction.commit();
                        break;
                    case 100013:
                       /* frag = new ManageStudents();
                        toolbarTitle.setText("Manage Students");

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragmstudents");
                        fragmentTransaction.commit();*/
                        Intent intent = new Intent(Home.this, ManageStudents.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 100014:
                        frag = new FragmentManageTeachers();

                        toolbarTitle.setText("Manage Teachers");

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id._frame_home, frag, "fragmteachers");
                        fragmentTransaction.commit();
                        break;
                    case R.id.logout:
                        confirmExit();

                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("info@daniellasmontessori.com");
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void getTeacher(){

        if (pref.contains(PREF_LOGINAME_TEACHER)) {
            TEACHER_FULLNAME = pref.getString(PREF_LOGINAME, null);
        }
        if (pref.contains(PREF_LOGINSEX_TEACHER)) {
            TEACHER_SEX = pref.getString(PREF_LOGINSEX, null);
        }
        if (pref.contains(PREF_POSITION_TEACHER)) {
            TEACHER_POSITION = pref.getString(PREF_LOGINPHONE, null);
        }
        if (pref.contains(PREF_USERNAME_TEACHER)) {
            TEACHER_USERNAME = pref.getString(PREF_LOGINCLASS, null);
        }
        if (pref.contains(PREF_USERTYPE_TEACHER)) {
            TEACHER_TYPE = pref.getString(PREF_LOGINEMAIL, null);
        }

        if (pref.contains(PREF_STARTDATE_TEACHER)) {
            TEACHER_STARTDATE = pref.getString(PREF_IMAGEID, null);
        }

        if (pref.contains(PREF_TERM)) {
            TERM = pref.getString(PREF_TERM, "First term");
        }
        if (pref.contains(PREF_WEEK)) {
            WEEK = pref.getString(PREF_WEEK, "WEEK");
        }
        if (pref.contains(PREF_YEAR)) {
            YEAR = pref.getString(PREF_YEAR, "2016");
        }

    }

    private void getUser(){
        if (pref.contains(PREF_LOGINAME)) {
            USER_FULLNAME = pref.getString(PREF_LOGINAME, null);
        }
        if (pref.contains(PREF_LOGINSEX)) {
            USER_SEX = pref.getString(PREF_LOGINSEX, null);
        }
        if (pref.contains(PREF_LOGINPHONE)) {
            USER_PHONE = pref.getString(PREF_LOGINPHONE, null);
        }
        if (pref.contains(PREF_LOGINCLASS)) {
            USER_CLASS = pref.getString(PREF_LOGINCLASS, null);
        }
        if (pref.contains(PREF_LOGINEMAIL)) {
            USER_EMAIL = pref.getString(PREF_LOGINEMAIL, null);
        }
        if (pref.contains(PREF_IMAGEID)) {
            USER_IMAGEID = pref.getString(PREF_IMAGEID, null);
        }
        if (pref.contains(PREF_HEADTEACHER)) {
            USER_HEADTEACHER = pref.getString(PREF_HEADTEACHER, null);
        }
        if (pref.contains(PREF_IMAGEID_HEADT)) {
            USER_IMAGEID_HEADT = pref.getString(PREF_IMAGEID_HEADT, null);
        }
        if (pref.contains(PREF_IMAGEID_PROP)) {
            USER_IMAGEID_PROPR = pref.getString(PREF_IMAGEID_PROP, null);
        }

        if (pref.contains(PREF_TERM)) {
            TERM = pref.getString(PREF_TERM, "First term");
        }
        if (pref.contains(PREF_WEEK)) {
            WEEK = pref.getString(PREF_WEEK, "WEEK");
        }
        if (pref.contains(PREF_YEAR)) {
            YEAR = pref.getString(PREF_YEAR, "2016");
        }

    }


    public void confirmExit(){
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.logoutAuth)
                .content(R.string.logoutConf)
                .positiveText(R.string.exitPos)
                .negativeText(R.string.exitNeg)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (pref.contains("pref_prevlogin")) {
                            pref.edit()
                                    .remove("pref_login_name")
                                    .remove("pref_login_sex")
                                    .remove("pref_login_phone")
                                    .remove("pref_login_class")
                                    .remove("pref_login_email")
                                    .remove("pref_login_imageid")
                                    .remove("pref_prevlogin")
                                    .commit();
                        }else if(pref.contains("pref_prevlogin_teacher")){
                            pref.edit()
                                    .remove("pref_login_name_teacher")
                                    .remove("pref_login_sex_teacher")
                                    .remove("pref_position_teacher")
                                    .remove("pref_login_username")
                                    .remove("usertype_teacher")
                                    .remove("pref_startdate_teacher")
                                    .remove("pref_prevlogin_teacher")
                                    .commit();
                        }
                            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                finishAffinity();
                            } else
                                finish();*/

                        Intent intent = new Intent(Home.this, LandingPage.class);
                        startActivity(intent);
                        finish();


                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void progressbarStartRetrieve(String title){
        dialog = new MaterialDialog.Builder(this)
                .title(title)
                .content("Retrieving...")
                .backgroundColor(Color.TRANSPARENT)
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    public void progressbarStartAction(String title,String action){
        dialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(action)
                .cancelable(false)
                .backgroundColor(Color.TRANSPARENT)
                .progress(true, 0)
                .show();
    }


    public void dialogOutput(String tit, String cont, final Boolean ok){

        dialog = new MaterialDialog.Builder(this)
                .title(tit)
                .content(cont)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        Fragment fragmentarticle   = getSupportFragmentManager().findFragmentByTag("fragarticle");
                        getSupportFragmentManager().beginTransaction().remove(fragmentarticle).commit();

                        Fragment frag = new FragmentHome();

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.sliderightenter, 0);
                        fragmentTransaction.replace(R.id._frame_home, frag, "fraghome");
                        fragmentTransaction.commit();
                        }
                })
                .show();
    }

    public void dialog(String title, String body){
        dialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(body+"\n")
                .positiveText(R.string.exitPos)
                .negativeText(R.string.exitNeg)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        } else
                            finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                    }
                })
                .show();

    }

    public void dialog2(String title, String body){
        dialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(body+"\n Continue")
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .positiveText(R.string.exitPos)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public void dialogDisplay(String title, String body){
        dialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(body)
                .positiveText(R.string.exitPos)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })

                .show();
    }


    public void dialogError(String cont, final String whichscreen){
        dialog = new MaterialDialog.Builder(this)
                .content(cont)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        Fragment frag = null;
                        Fragment newfrag = null;
                        FragmentManager fragmentManager = null;
                        FragmentTransaction fragmentTransaction = null;

                        if(whichscreen == "news"){
                            frag   = getSupportFragmentManager().findFragmentByTag("fragnews");
                            getSupportFragmentManager().beginTransaction().remove(frag).commit();

                            newfrag = new FragmentHome();
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id._frame_home, newfrag, "fraghome");
                            fragmentTransaction.commit();

                        }else if(whichscreen == "article"){
                            frag   = getSupportFragmentManager().findFragmentByTag("fragarticle");
                            getSupportFragmentManager().beginTransaction().remove(frag).commit();

                            newfrag = new FragmentHome();
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id._frame_home, newfrag, "fraghome");
                            fragmentTransaction.commit();
                        }else if(whichscreen == "events"){
                            frag   = getSupportFragmentManager().findFragmentByTag("fragevents");
                            getSupportFragmentManager().beginTransaction().remove(frag).commit();

                            newfrag = new FragmentHome();
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id._frame_home, newfrag, "fraghome");
                            fragmentTransaction.commit();
                        }else if(whichscreen == "calendar"){
                            frag   = getSupportFragmentManager().findFragmentByTag("fragcalendar");
                            getSupportFragmentManager().beginTransaction().remove(frag).commit();

                            newfrag = new FragmentHome();
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id._frame_home, newfrag, "fraghome");
                            fragmentTransaction.commit();
                        }else if(whichscreen == "article"){
                            frag   = getSupportFragmentManager().findFragmentByTag("fragarticle");
                            getSupportFragmentManager().beginTransaction().remove(frag).commit();

                            newfrag = new FragmentHome();
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id._frame_home, newfrag, "fraghome");
                            fragmentTransaction.commit();
                        }else if(whichscreen == "assignment"){
                            frag   = getSupportFragmentManager().findFragmentByTag("fragassignment");
                            getSupportFragmentManager().beginTransaction().remove(frag).commit();

                            newfrag = new FragmentHome();
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id._frame_home, newfrag, "fraghome");
                            fragmentTransaction.commit();
                        }else if(whichscreen == "assignment"){
                            frag   = getSupportFragmentManager().findFragmentByTag("fragassignment");
                            getSupportFragmentManager().beginTransaction().remove(frag).commit();

                            newfrag = new FragmentHome();
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id._frame_home, newfrag, "fraghome");
                            fragmentTransaction.commit();
                        }else if(whichscreen == "manageteachers"){
                            frag   = getSupportFragmentManager().findFragmentByTag("fragmteachers");
                            getSupportFragmentManager().beginTransaction().remove(frag).commit();

                            newfrag = new FragmentHome();
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id._frame_home, newfrag, "fraghome");
                            fragmentTransaction.commit();
                        }else if(whichscreen == "manageteachersdismiss"){
                            dialog.dismiss();
                        }else if(whichscreen == "managestudents"){
                           dialog.dismiss();
                        }else if(whichscreen == "quiz"){
                           dialog.dismiss();
                        }else{
                            dialog.dismiss();
                        }
                        //_title.setText("Creditswitch *931#");
                    }
                })
                .show();
    }

    public void progressbarStop(){
        dialog.dismiss();
    }



    public void eventout(String tit, String cont){
       dialog = new MaterialDialog.Builder(this)
                .title(tit)
                .content(cont)
               .canceledOnTouchOutside(true)
               .autoDismiss(true)
                .show();
    }

    private static String md5(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void alertNetErr(){
        dialog = new MaterialDialog.Builder(this)
                .title("Network")
                .content("Your network seems to be disabled. Please try again!")
                .positiveText("OK")
                .show();
    }

    private static String base(String text){

        byte[] data = null;
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1){
            e1.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        return base64;
    }



    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Home.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Home.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
//                    if(e.getAction() == MotionEvent.ACTION_DOWN)
//                        return true;
//                    else
//                        return false;
                }


                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e) ) {
                clickListener.onClick(child, rv.getChildPosition(child));
                return true;//https://disqus.com/home/discussion/androidhive/android_working_with_recycler_view/
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            /*if(e.getAction() != MotionEvent.ACTION_DOWN){
                return false;
            }*/
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    protected void cancAllRequest(){
        AppController.getInstance().getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }


}
