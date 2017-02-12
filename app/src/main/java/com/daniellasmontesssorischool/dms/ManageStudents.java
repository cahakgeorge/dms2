package com.daniellasmontesssorischool.dms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daniellasmontesssorischool.dms.CoreMethods.NetworkCheck;
import com.daniellasmontesssorischool.dms.app.AppController;
import com.daniellasmontesssorischool.dms.utils.Const;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageStudents extends AppCompatActivity{

    public Toolbar toolbar;
    public TextView toolbarTitle;

    SharedPreferences pref;
    public static final String PREFS_NAME = "DmsPrefsFile";

    Button _upload;

    private static String TAG_SUCCESS = "success";
    private static String TAG_SERVER_MSG="message";

    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";

    NetworkCheck netCheck;


    ArrayList<String> articleview = new ArrayList<>();
    ArrayList<String> newsview = new ArrayList<>();
    ArrayList<String> assignmentview = new ArrayList<>();
    //ArrayList<String> calendarview = new ArrayList<>();

    private static String PREF_LOGINAME = "pref_login_name";
    private static String PREF_LOGINSEX = "pref_login_sex";
    private static String PREF_LOGINPHONE = "pref_login_phone";
    private static String PREF_LOGINCLASS = "pref_login_class";
    private static String PREF_LOGINEMAIL = "pref_login_email";
    private static String PREF_HEADTEACHER = "pref_login_headteacher";
    private static String PREF_IMAGEID_HEADT = "pref_login_imghead";
    private static String PREF_IMAGEID_PROP = "pref_login_imgprop";


    public static String TEACHER_FULLNAME = "";
    public static String TEACHER_SEX = "";
    public static String TEACHER_POSITION = "";
    public static String TEACHER_USERNAME = "";
    public static String TEACHER_TYPE = "";

    public static String TEACHER_STARTDATE = "";


    private static String PREF_LOGINAME_TEACHER="pref_login_name_teacher";
    private static String PREF_LOGINSEX_TEACHER = "pref_login_sex_teacher";
    private static String PREF_POSITION_TEACHER = "pref_position_teacher";
    private static String PREF_USERNAME_TEACHER = "pref_login_username";

    private static String PREF_USERTYPE_TEACHER="usertype_teacher";
    private static String PREF_STARTDATE_TEACHER = "pref_startdate_teacher";


    private static String PREF_PREVIOUSLOGIN_TEACHER = "pref_prevlogin_teacher";

    MaterialDialog dialog;
    CoordinatorLayout coordinatorLayout;

    CircleImageView chooseimage;
    MaterialSpinner _gender, _stclass;

    public static EditText _fname, _lname, _oname, _dob, _email, _phone, _pass, _pass2;
    TextView date;
    String addname, st_class="", gender="", image="", fname, lname, oname, dob, email, phone, pass, pass2;
    Button add;
    public static String which_date = "dob";

    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1888;

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    public void onBackPressed() {
        AppController.getInstance().getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });

        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();

        //Revert name to home
        //toolbarTitle.setText("Home");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        netCheck = new NetworkCheck(getApplicationContext());
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id._coord);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.navicon);
        //toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        toolbarTitle = (TextView)findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Manage Students");

        pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        getTeacher();

        date = (TextView)findViewById(R.id._date2);
        Calendar c = Calendar.getInstance();

        DateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy");
        fromFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");
        toFormat.setLenient(false);
        Date datt = null;
        try {
            datt = fromFormat.parse(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date.setText(toFormat.format(datt));

        _fname = (EditText)findViewById(R.id._fname);
        _lname = (EditText)findViewById(R.id._surname);
        _oname = (EditText)findViewById(R.id._oname);
        _dob = (EditText)findViewById(R.id._dob);
        _dob.setClickable(false);
        _dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which_date = "dob";
                DialogFragment dialogfragment = new DatePickerDialogTheme1();

                dialogfragment.show(getSupportFragmentManager(), "Theme 1");
            }
        });

        _email = (EditText)findViewById(R.id._email);
        _phone = (EditText)findViewById(R.id._phone);
        _pass = (EditText)findViewById(R.id._pass1);
        _pass2 = (EditText)findViewById(R.id._pass2);

       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }*/

        chooseimage = (CircleImageView)findViewById(R.id.chooseImage);

        chooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == chooseimage){
                    openImageIntent();
                }
            }
        });

        _gender = (MaterialSpinner)findViewById(R.id._gender);
        _gender.setItems("Male", "Female");
        _gender.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        gender = "Male";
                        break;
                    case 1:
                        gender = "Female";
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Snackbar.make(coordinatorLayout, "Please select student gender", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(getActivity().getApplicationContext(), "Please select an article type", Toast.LENGTH_LONG).show();
            }

        });

        _stclass = (MaterialSpinner)findViewById(R.id._class);
        _stclass.setItems("Year 1", "Year 2", "Year 3", "Year 4", "Year 5");
        _stclass.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        st_class = "Year 1";
                        break;
                    case 1:
                        st_class = "Year 2";
                        break;
                    case 2:
                        st_class = "Year 3";
                        break;
                    case 3:
                        st_class = "Year 4";
                        break;
                    case 4:
                        st_class = "Year 5";
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Snackbar.make(coordinatorLayout, "Please select student gender", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(getActivity().getApplicationContext(), "Please select an article type", Toast.LENGTH_LONG).show();
            }

        });

        add = (Button)findViewById(R.id._add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(netCheck.isConnected()){
                    //addname, st_class, gender

                    addname = addname;

                    fname = _fname.getText().toString();
                    lname = _lname.getText().toString();
                    oname = _oname.getText().toString();


                    email = _email.getText().toString();
                    phone = _phone.getText().toString();

                    pass = _pass.getText().toString();
                    pass2 = _pass2.getText().toString();

                    dob = _dob.getText().toString();
                    if(!dob.isEmpty() && !dob.equals(null)) {
                        dob = "" + getUnixTime(_dob.getText().toString());
                    }

                    if(validate()){
                        addStudents();
                    }
                }else{
                    alertNetErr();
                }
            }
        });

    }


    public void alertNetErr(){
        dialog = new MaterialDialog.Builder(this)
                .title("Network")
                .content("Your network seems to be disabled. Please try again!")
                .positiveText("OK")
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


    public void dialogError(String cont){
        dialog = new MaterialDialog.Builder(this)
                .content(cont)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();

                        /*}else{
                            dialog.dismiss();
                        }*/
                        //_title.setText("Creditswitch *931#");
                    }
                })
                .show();
    }

    public void dialogOutput(String tit, String cont){

        dialog = new MaterialDialog.Builder(this)
                .title(tit)
                .content(cont)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                       dialog.dismiss();
                    }
                })
                .show();
    }

    public void progressbarStop(){
        dialog.dismiss();
    }

    //https://www.simplifiedcoding.net/android-volley-tutorial-to-upload-image-to-server/
    /*private void showFileChooser() {
       *//* Intent intent = new Intent();
        intent.setType("image*//**//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);*//*
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                chooseimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    //Create the following method to convert Bitmap to base64 String.
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private Uri outputFileUri;
    private Bitmap resized = null;
    //http://stackoverflow.com/questions/4455558/allow-user-to-select-camera-or-gallery-for-image
    private void openImageIntent() {

// Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname ="img2_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

     /*   String path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
        File file = new File(path,"img2_"+System.currentTimeMillis()+".jpg");
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        }catch (IOException e) {
            e.printStackTrace();
        }
        outputFileUri2 = Uri.fromFile(file);*/

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        /*final Intent galleryIntent = new Intent();
        galleryIntent.setType("image*//*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);*/

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, 1888);//YOUR_SELECT_PICTURE_REQUEST_CODE chooseimage.setImageBitmap(bitmap);
    }

    /*public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }
*/

  /*  public void cameraIntent(){
        Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, 1888);
    }*/

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            // Get Extra from the intent
            Bundle extras = data.getExtras();
            // Get the returned image from extra
            Bitmap bmp = (Bitmap) extras.get("data");

            resized = Bitmap.createScaledBitmap(bmp, 200, 200, true);

            chooseimage.setImageBitmap(resized);

            //convert to base 64 string
            image = getStringImage(resized);
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK && data !=null) {
            return;
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == 1888) {
                final boolean isCamera;
                if (data == null || data.getData() == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = MediaStore.ACTION_IMAGE_CAPTURE.equals(data.getAction());
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                    //Bitmap factory
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    // downsizing image as it throws OutOfMemory Exception for larger
                    // images
                    options.inSampleSize = 8;
                    final Bitmap bitmap;
                        bitmap = BitmapFactory.decodeFile(selectedImageUri.getPath(), options);
                    resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);

                    chooseimage.setImageBitmap(resized);

                    //convert to base 64 string
                    image = getStringImage(resized);

                } else {
                    selectedImageUri = data == null ? null : data.getData();
                    Log.d("ImageURI", selectedImageUri.getLastPathSegment());
                    // /Bitmap factory
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // downsizing image as it throws OutOfMemory Exception for larger
                    // images
                    options.inSampleSize = 2;
                    try {//Using Input Stream to get uri did the trick
                        //InputStream input = getActivity().getContentResolver().openInputStream(selectedImageUri);
                        //final Bitmap bitmap = BitmapFactory.decodeStream(input);
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                        chooseimage.setImageBitmap(resized);
                        //convert to base 64 string
                        image = getStringImage(resized);
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
                /*if (isCamera) {
                    selectedImageUri = outputFileUri;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Setting the Bitmap to ImageView
                    chooseimage.setImageBitmap(bitmap);
                } else {
                    selectedImageUri = data == null ? null : data.getData();

                    try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    chooseimage.setImageBitmap(bitmap);
                }
            }*/
            }
        }else if (resultCode == RESULT_CANCELED) {
            // User cancelled the image capture
            Toast.makeText(getApplicationContext(), "Picture taking canceled..", Toast.LENGTH_LONG).show();
        }else{
            dialogDisplay("Camera Error", "Error taking picture, try again!");
        }
    }

    private boolean validate(){
        boolean valid=true;
        //VALIDATE PHONE
        if (phone.isEmpty()){
            _phone.setError("Enter a valid phone number");
            valid=false;
        }else if(phone.startsWith("0")){
            phone = phone.replaceAll("\\D", "");
            phone = "+234"+ phone.substring(1);
        }else if(phone.startsWith("234")){
            phone = "+234"+ phone.substring(3);
        }

        if (phone.startsWith("0") && phone.length()!=11){
            _phone.setError("Enter a valid phone number");
            valid=false;
        }else if(phone.startsWith("234") && phone.length()!=13){
            _phone.setError("Enter a valid phone number");
            valid=false;
        }else if(phone.startsWith("+234") && phone.length()!=14){
            _phone.setError("Enter a valid phone number");
            valid=false;
        }


        if(pass.isEmpty()){
            _pass.setError("Enter password");
            valid=false;
        }

        if(pass2.isEmpty()){
            _pass2.setError("Enter password");
            valid=false;
        }

        if(!(pass.equals(pass2))){
            _pass2.setError("Passwords do not match");
            valid=false;
        }

        if(fname.isEmpty()){
            _fname.setError("Enter your first name");
            valid=false;
        }

        if(lname.isEmpty()){
            _lname.setError("Enter your last name");
            valid=false;
        }

        if(email.isEmpty()){
            _lname.setError("Enter your email address");
            valid=false;
        }

        if(dob=="" || dob==null){
            _dob.setError("Enter your email address");
            valid=false;
        }

        if(st_class.isEmpty()){
            Snackbar.make(coordinatorLayout, "Select class of student", Snackbar.LENGTH_SHORT).setAction("Error", null).show();
            valid=false;
        }

        if(gender.isEmpty()){
            Snackbar.make(coordinatorLayout, "Gender must be Male or Female!", Snackbar.LENGTH_SHORT).setAction("Error", null).show();
            valid=false;
        }

        if(image.isEmpty()){
            Snackbar.make(coordinatorLayout, "Please upload an image", Snackbar.LENGTH_SHORT).setAction("Error", null).show();
            valid=false;
        }

        return valid;
    }

    @Override
    protected void onPause() {
        chooseimage.setImageURI(null);
        super.onPause();
    }

    public static String getCurrentTimeStamp(String dates) {
        long date = Long.parseLong(dates);
        Date d=new Date(((long)date)*1000);
        SimpleDateFormat sdfDate = new SimpleDateFormat("ddd MMMM, yyyy", Locale.ENGLISH);
        //SimpleDateFormat sdfDate = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss a", Locale.ENGLISH);
        sdfDate.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
        String strDate = sdfDate.format(d);
        return strDate;
    }

    private void getTeacher(){

        if (pref.contains(PREF_LOGINAME_TEACHER)) {
            TEACHER_FULLNAME = pref.getString(PREF_LOGINAME_TEACHER, null);
        }
        if (pref.contains(PREF_LOGINSEX_TEACHER)) {
            TEACHER_SEX = pref.getString(PREF_LOGINSEX_TEACHER, null);
        }
        if (pref.contains(PREF_POSITION_TEACHER)) {
            TEACHER_POSITION = pref.getString(PREF_POSITION_TEACHER, null);
        }
        if (pref.contains(PREF_USERNAME_TEACHER)) {
            TEACHER_USERNAME = pref.getString(PREF_USERNAME_TEACHER, null);
        }
        if (pref.contains(PREF_USERTYPE_TEACHER)) {
            TEACHER_TYPE = pref.getString(PREF_USERTYPE_TEACHER, null);
        }
        if (pref.contains(PREF_STARTDATE_TEACHER)) {
            TEACHER_STARTDATE = pref.getString(PREF_STARTDATE_TEACHER, null);
        }

    }

    /*void teacherAction(final String username){
        dialog = new MaterialDialog.Builder(this)
                .title("Perform Action")
                .items(R.array.taction)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            perfTeacherAction("Block", username);
                        }else if (which == 1) {
                            perfTeacherAction("Delete", username);
                        }else if (which == 3) {
                            perfTeacherAction("Reset", username);
                        }
                        dialog.dismiss();
                    }
                })
                //.positiveText("Continue")
                .show();
    }

    void perfTeacherAction(final String action, final String user){
        if (action.equals("Block")) {
            progressbarStartAction("Block Teacher", "Blocking...");
        }else if (action.equals("Delete")) {
            progressbarStartAction("Delete Teacher", "Deleting...");
        }else if (action.equals("Reset")) {
            progressbarStartAction("Reset Teacher", "Resetting...");
        }

        String tag_json_obj_actonteachers = "json_obj_req_actonteachers";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Teachers Retrieval", "Not null "+response.toString());
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("Teachers Retrieval", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar

                        if(response != null){
                            Log.e("Teachers", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Teachers", response.toString());

                                Snackbar.make(rootView, "Action performed successfully!", Snackbar.LENGTH_LONG)
                                        .setAction("Result", null).show();

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("Action couldn't be performed. Please try again!", "actteachers");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few seconds..", "actteachers");
                        }
                    }else{ //response from server is null
                        ((Home) getActivity()).progressbarStop();
                        ((Home) getActivity()).dialogOutput("Retrieval Error", "Please check your data connection", false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ((Home)getActivity()).progressbarStop();
                //   Handle Error
                ((Home)getActivity()).dialogError("Please try again!", "actteachers");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_target", "11");
                params.put("action", action);
                params.put("username", user);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_actonteachers);
    }*/

    void addStudents(){
        progressbarStartAction("Adding student","Wait while server performs request...");
        String tag_json_obj_student = "json_obj_req_addstudent";

        //Had to make use of a custom json object request class due to errors with the normal JsonObjectRequest
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.loginapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Student Add", "Not null "+response.toString());
                try {
                    if(!response.equals(null) && (response.contains("{") && response.contains("}"))) {
                        Log.e("Student Addition", "Not null "+response.toString());

                        JSONObject data = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        String resp = data.getString(TAG_SUCCESS);
                        progressbarStop(); //dismiss active progressbar

                        if(response != null){
                            Log.e("Student", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Student", response.toString());

                                /*Snackbar.make(rootView, data.getString(TAG_SERVER_MSG), Snackbar.LENGTH_LONG)
                                        .setAction("Result", null).show();*/
                                dialogDisplay("Result", data.getString(TAG_SERVER_MSG));

                            }else{
                                progressbarStop();
                                dialogError("Action couldn't be performed. Please try again! \n"+data.getString(TAG_SERVER_MSG));
                            }
                        }else{
                            progressbarStop();
                            dialogError("Please check again in a few seconds..");
                        }
                    }else{ //response from server is null
                        progressbarStop();
                        dialogOutput("Retrieval Error", "Please check your data connection");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressbarStop();
                //   Handle Error
                dialogError("Please try again!");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_target", "13");

                params.put("admin_name", "Mr Destiny");
                params.put("class", st_class);
                params.put("fname", fname);
                params.put("lname", lname);
                params.put("oname", oname);
                params.put("gender", gender);
                params.put("dob", dob);
                params.put("email", email);
                params.put("phone", phone);
                params.put("pass", pass);
                params.put("image", image);

                Log.e("Tag Students",params.toString());
                Log.e("Tag Students",params.toString());
                Log.e("Tag Students",params.toString());
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj_student);
    }


    public static class DatePickerDialogTheme1 extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            @SuppressWarnings("ResourceType") DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);

            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            //Long getUnix = getUnixTime(day + "/" + (month+1) + "/" + year);
            _dob.setText(day + "/" + (month+1) + "/" + year);
        }
    }


    public static Long getUnixTime(String dateString){
        //DateFormat dfm = new SimpleDateFormat("d/MMM/yyyy");
        //String dateString = "Fri, 09 Nov 2012 23:40:18 GMT";
        DateFormat dateFormat = new SimpleDateFormat("d/MM/yy");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long unixTime = (long)date.getTime()/1000;

        return unixTime;
    }
}
