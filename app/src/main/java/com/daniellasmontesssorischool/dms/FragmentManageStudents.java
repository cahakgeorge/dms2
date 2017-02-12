package com.daniellasmontesssorischool.dms;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentManageStudents extends Fragment {
    Button _upload;

    private static String TAG_SUCCESS = "success";
    private static String TAG_SERVER_MSG="message";

    private static String TAG_ERROR="error";
    private static String TAG_SERVERMSG="message";

    NetworkCheck netCheck;
    View rootView;

    MaterialDialog dialog;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_manage_students, container, false);
        netCheck = new NetworkCheck(this.getActivity().getApplicationContext());
        //CoordinatorLayout coordinatorLayout = (CoordinatorLayout)rootView.findViewById(R.id.coordinatorlayout);

        if(((Home)getActivity()).getSupportActionBar() != null)
            ((Home)getActivity()).getSupportActionBar().setTitle("Manage Students");

        date = (TextView)rootView.findViewById(R.id._date2);
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

        _fname = (EditText)rootView.findViewById(R.id._fname);
        _lname = (EditText)rootView.findViewById(R.id._surname);
        _oname = (EditText)rootView.findViewById(R.id._oname);
        _dob = (EditText)rootView.findViewById(R.id._dob);
        _dob.setClickable(false);
        _dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which_date = "dob";
                DialogFragment dialogfragment = new DatePickerDialogTheme1();

                dialogfragment.show(getFragmentManager(), "Theme 1");
            }
        });

        if(((Home)getActivity()).getSupportActionBar() != null)
            ((Home)getActivity()).getSupportActionBar().setTitle("Contact DMS");

        _email = (EditText)rootView.findViewById(R.id._email);
        _phone = (EditText)rootView.findViewById(R.id._phone);
        _pass = (EditText)rootView.findViewById(R.id._pass1);
        _pass2 = (EditText)rootView.findViewById(R.id._pass2);

        chooseimage = (CircleImageView)rootView.findViewById(R.id.chooseImage);

        chooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == chooseimage){
                    openImageIntent();
                }
            }
        });

        _gender = (MaterialSpinner)rootView.findViewById(R.id._gender);
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
                Snackbar.make(rootView, "Please select student gender", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(getActivity().getApplicationContext(), "Please select an article type", Toast.LENGTH_LONG).show();
            }

        });

        _stclass = (MaterialSpinner)rootView.findViewById(R.id._class);
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
                Snackbar.make(rootView, "Please select student gender", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(getActivity().getApplicationContext(), "Please select an article type", Toast.LENGTH_LONG).show();
            }

        });

        add = (Button)rootView.findViewById(R.id._add_button);
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
                    ((Home)getActivity()).alertNetErr();
                }
            }
        });

        return rootView;
    }

/*    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == false){
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        Home activity = (Home)getActivity();
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                activity.setCapturedImageURI(fileUri);
                activity.setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        activity.getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }*/

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

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                    // downsizing image as it throws OutOfMemory Exception for larger
                    // images
                    options.inSampleSize = 2;
                    final Bitmap bitmap = BitmapFactory.decodeFile(selectedImageUri.getPath(), options);
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
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
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
            Snackbar.make(rootView, "Select class of student", Snackbar.LENGTH_SHORT).setAction("Error", null).show();
            valid=false;
        }

        if(gender.isEmpty()){
            Snackbar.make(rootView, "Gender must be Male or Female!", Snackbar.LENGTH_SHORT).setAction("Error", null).show();
            valid=false;
        }

        if(image.isEmpty()){
            Snackbar.make(rootView, "Please upload an image", Snackbar.LENGTH_SHORT).setAction("Error", null).show();
            valid=false;
        }

        return valid;
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


    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
           // outRect.bottom = mVerticalSpaceHeight;
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = mVerticalSpaceHeight;
            }
        }
    }

    void teacherAction(final String username){
        dialog = new MaterialDialog.Builder(getActivity())
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
            ((Home)getActivity()).progressbarStartAction("Block Teacher", "Blocking...");
        }else if (action.equals("Delete")) {
            ((Home)getActivity()).progressbarStartAction("Delete Teacher", "Deleting...");
        }else if (action.equals("Reset")) {
            ((Home)getActivity()).progressbarStartAction("Reset Teacher", "Resetting...");
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
    }

    void addStudents(){
        ((Home)getActivity()).progressbarStartAction("Adding student","Wait while server performs request...");
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
                        ((Home)getActivity()).progressbarStop(); //dismiss active progressbar

                        if(response != null){
                            Log.e("Student", "Not null "+response.toString());

                            if (resp.equalsIgnoreCase("1")) {
                                Log.e("Tag Student", response.toString());

                                /*Snackbar.make(rootView, data.getString(TAG_SERVER_MSG), Snackbar.LENGTH_LONG)
                                        .setAction("Result", null).show();*/
                                ((Home)getActivity()).dialogDisplay("Result", data.getString(TAG_SERVER_MSG));

                            }else{
                                ((Home)getActivity()).progressbarStop();
                                ((Home)getActivity()).dialogError("Action couldn't be performed. Please try again! \n"+data.getString(TAG_SERVER_MSG), "addstudent");
                            }
                        }else{
                            ((Home)getActivity()).progressbarStop();
                            ((Home)getActivity()).dialogError("Please check again in a few seconds..", "addstudent");
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
                ((Home)getActivity()).dialogError("Please try again!", "addstudent");
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
