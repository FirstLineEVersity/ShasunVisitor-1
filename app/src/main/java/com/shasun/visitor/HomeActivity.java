package com.shasun.visitor;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String strEmployee = "";
    SQLiteDatabase db;
    private final LinkedHashMap<String, String> toWhomMeet_data = new LinkedHashMap<String, String>();
    RadioGroup radioGroup;
    private long lngEmployeeId = 0;
    ImageView imageView;
    private static final int UPDATE_REQUEST_CODE = 530;
    private static String ResultString = "";
    int appdownload = 0;
    boolean isAppUpdateAvailable = false;
    ImageButton btnTakePhoto;
    ImageButton  btnSave, btnRefresh, btnPrint;
    ImageButton btnVisitorOut;
    TextView txtVisitorOut;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int RequestPermissionCode = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };
    TextInputEditText txtVisitorName, txtMobile, txtMobile1, txtAddress, txtMsg;
    AutoCompleteTextView txtWhoMeet;
    TextView txtDateTime, txtDateTime1;
    Bitmap bitmap = null;
    Toolbar toolbar;
    LinearLayout llHome, llHomeNewUser;
    String visitorId = "";
    ImageButton ibAddVisitor;

    String visitorIdPrint = "";

    String file_name_path = Environment.getExternalStorageDirectory().getPath() + "/Download/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout_cardview_home_page);
        toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        radioGroup = findViewById(R.id.radio);
        txtDateTime = findViewById(R.id.txtDateTime);
        txtDateTime1 = findViewById(R.id.txtDateTime1);
        llHome = findViewById(R.id.llHome);
        llHomeNewUser = findViewById(R.id.llHomeNewUser);
        ibAddVisitor = findViewById(R.id.ibAddVisitor);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(checkedId);
                String purpose = (String) radioButton.getText();
                txtMsg.setText(purpose);
            }

        });

        final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);
        lngEmployeeId = loginsession.getLong("userid", 1);

        //Status Bar Color
        StatusColor.SetStatusColor(getWindow(), ContextCompat.getColor(this, R.color.appColor));
        //final SharedPreferences loginsession = getApplicationContext().getSharedPreferences("SessionLogin", 0);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        navigationView.setItemIconTintList(null);

        btnVisitorOut = findViewById(R.id.btnVisitorOut);
        txtVisitorOut = findViewById(R.id.txtVisitorOut);
        btnTakePhoto = findViewById(R.id.buttonTake);
       // btnSave = findViewById(R.id.btnSave);
        btnPrint = findViewById(R.id.btnPrint);
        btnRefresh = findViewById(R.id.btnRefresh);
        imageView = findViewById(R.id.imageView);
        txtVisitorName = findViewById(R.id.txtVisitorName);
        txtMobile = findViewById(R.id.txtMobile);
        txtMobile1 = findViewById(R.id.txtMobile1);
        txtAddress = findViewById(R.id.txtAddress);
        txtMsg = findViewById(R.id.txtMsg);
        txtWhoMeet = findViewById(R.id.txtWhoMeet);


        EnableRuntimePermission();
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });
       /* btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        */
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataPrint();
                //printPass();
            }
        });
        btnVisitorOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveVisitorOutData();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refreshData();
            }
        });

        txtMobile1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                String enteredValue = s.toString();
                if (enteredValue.length() <= 9) {
                    btnVisitorOut.setVisibility(View.GONE);
                    txtVisitorOut.setVisibility(View.GONE);
                } else if (enteredValue.length() > 9 && enteredValue.length() < 11) {

                    MobileNoValidation();
                } else if (enteredValue.length() > 10) {
                    dataRequired("Please check the Mobile Number(It has More than 10 digits)");
                }

            }
        });
        txtWhoMeet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                String enteredValue = s.toString();
                if (enteredValue.length() > 2) {
                    ToWhomToMeet();

                }else{
                    ll.clear();
                    if(LPA != null){
                        LPA.notifyDataSetChanged();
                    }
                }

            }
        });
        txtWhoMeet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                // TODO Auto-generated method stub
                txtWhoMeet.showDropDown();
                txtWhoMeet.requestFocus();
                return false;
            }
        });

        txtWhoMeet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                String id = (new ArrayList<String>(toWhomMeet_data.keySet())).get(position);
                String name = (new ArrayList<String>(toWhomMeet_data.values())).get(position);
                try {
                    JSONObject jo = new JSONObject(name);
                    txtWhoMeet.setText(jo.getString("staffname") + " - " + jo.getString("department") + " - Code:" + jo.getString("staffcode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        dateTimeDisp();
        llHome.setVisibility(View.VISIBLE);
        llHomeNewUser.setVisibility(View.GONE);
        ibAddVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 refreshScreen();
                            }
        });
        txtMsg.setText("Meeting");

    }

    public void printPass(){
        if(visitorIdPrint.length() > 0) {
            String url = "http://erp.shasuncollege.edu.in/evarsityshasun/usermanager/loginManager/frmVisitorPrint.jsp?hdnVisitorId=" + visitorIdPrint;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    public void refreshScreen() {
        llHome.setVisibility(View.VISIBLE);
        llHomeNewUser.setVisibility(View.GONE);
        refreshData();
        ll.clear();
        if(LPA != null){
            LPA.notifyDataSetChanged();
        }
        txtMobile1.setText("");
        btnVisitorOut.setVisibility(View.GONE);
        txtVisitorOut.setVisibility(View.GONE);
        bitmap = null;
    }
    List ll = new ArrayList<String>();
    SpinnerListAdapter LPA;
    private void displayLeavePeriod() {
        if (toWhomMeet_data.size() == 0) {
            ll.clear();
            if(LPA != null){
                LPA.notifyDataSetChanged();
            }
            Toast.makeText(HomeActivity.this, "Response: No Data Found", Toast.LENGTH_LONG).show();
        } else {
            Collection<String> LeavePeriodcollection = toWhomMeet_data.values();
            String[] arrayLeavePeriod = LeavePeriodcollection.toArray(new String[LeavePeriodcollection.size()]);
            // ArrayAdapter<String> LPA = new ArrayAdapter<String>(this, R.layout.dropdownlistitem, arrayLeavePeriod);
            ll.clear();
            for (String item : arrayLeavePeriod) {
                ll.add(item);
            }
            LPA = new SpinnerListAdapter(this, R.layout.gridlayout_cardview_home_page, R.id.txtWhoMeet, ll);

            txtWhoMeet.setAdapter(LPA);
            txtWhoMeet.showDropDown();
            txtWhoMeet.requestFocus();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(getResources().getString(R.string.app_name_full));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        verifyStoragePermissions(this);
    }

    public void setCirularImage(byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCornerRadius(10);
        imageView.setImageDrawable(roundedBitmapDrawable);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //this.finish();
        finishAffinity();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (id == R.id.nav_home) {
            toolbar.setTitle(getResources().getString(R.string.app_name));
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Flag", 2);
            startActivity(intent);

        }
        if (id == R.id.nav_history) {
            Intent intent = new Intent(HomeActivity.this,ViewVisitorPassPdf.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Flag", 2);
            startActivity(intent);
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    public void refreshData() {
        txtVisitorName.setText("");
        txtAddress.setText("");
        txtWhoMeet.setText("");
        // txtMobile.setText("");
        txtMsg.setText("Meeting");
        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        imageView.setImageBitmap(null);
        ll.clear();
        if(LPA != null){
            LPA.notifyDataSetChanged();
        }

        bitmap = null;

    }

    public void saveData() {
        if (bitmap == null) {
            dataRequired("Please Take Photo");
        } else if (txtVisitorName.getText().toString().length() < 1) {
            dataRequired("Please Enter Visitor Name");
        } else if (txtMobile.getText().toString().length() < 1) {
            dataRequired("Please Enter Visitor Mobile Number");
        } else if (txtAddress.getText().toString().length() < 1) {
            dataRequired("Please Enter Visitor Address");
        } else if (txtWhoMeet.getText().toString().length() < 1) {
            dataRequired("Please Select to Who you want to meet");
        } else {
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
            String purpose = (String) radioButton.getText();
            String encodedImage = "";
            if (bitmap != null) {
                encodedImage = getBase64String(bitmap);
            }
            WebService.strParameters = new String[]{"String", "visitorname", txtVisitorName.getText().toString(), "String", "visitoraddress", txtAddress.getText().toString(), "String", "visitorcontactno", txtMobile.getText().toString(), "String", "purposeofvisit", purpose, "String", "towhommeet", "66", "String", "visitorphoto", encodedImage};
            WebService.METHOD_NAME = "saveVisitorJson";
            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        }

    }

    public void saveDataPrint() {
        if (bitmap == null) {
            dataRequired("Please Take Photo");
        } else if (txtVisitorName.getText().toString().length() < 1) {
            dataRequired("Please Enter Visitor Name");
        } else if (txtMobile.getText().toString().length() < 1) {
            dataRequired("Please Enter Visitor Mobile Number");
        } else if (txtAddress.getText().toString().length() < 1) {
            dataRequired("Please Enter Visitor Address");
        } else if (txtWhoMeet.getText().toString().length() < 1) {
            dataRequired("Please Select to Who you want to meet");
        } else {

           // replace single

            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
            String purpose = (String) radioButton.getText();
            String encodedImage = "";
            if (bitmap != null) {
                encodedImage = getBase64String(bitmap);
            }
            WebService.strParameters = new String[]{"String", "visitorname", txtVisitorName.getText().toString(), "String", "visitoraddress", txtAddress.getText().toString(), "String", "visitorcontactno", txtMobile.getText().toString(), "String", "purposeofvisit", purpose, "String", "towhommeet", "66", "String", "visitorphoto", encodedImage};
            WebService.METHOD_NAME = "saveVisitorJson";
            AsyncCallWSPrint task = new AsyncCallWSPrint();
            task.execute();
        }

    }

    private String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] imageBytes = baos.toByteArray();

        String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        return base64String;
    }

    public void saveVisitorOutData() {
        WebService.METHOD_NAME = "visitorEntryOutJson";
        WebService.strParameters = new String[]{"String", "mobileno", txtMobile1.getText().toString(), "Long", "visitorid", visitorId};
        AsyncCallWSEntryOut task = new AsyncCallWSEntryOut();
        task.execute();

    }

    public void MobileNoValidation() {
        WebService.METHOD_NAME = "checkMobileNumberJson";
        WebService.strParameters = new String[]{"String", "mobileno", txtMobile1.getText().toString()};
        AsyncCallWSMobile task = new AsyncCallWSMobile();
        task.execute();
    }

    public void ToWhomToMeet() {
        WebService.METHOD_NAME = "getEmployeeJson";

        WebService.strParameters = new String[]{"String", "employeename", txtWhoMeet.getText().toString()};
        if (!txtWhoMeet.getText().toString().contains("{\"staffcode\"") && !txtWhoMeet.getText().toString().contains("- Code:")) {
            AsyncCallWSEmployee task = new AsyncCallWSEmployee();
            task.execute();
        }

    }


    private class AsyncCallWSEntryOut extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(HomeActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.loading));
            //show dialog
            dialog.show();
            //Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Log.i(TAG, "doInBackground");
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            ResultString = WebService.invokeWS();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //nithin
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                JSONObject js = new JSONObject(ResultString);
                if (!js.getString("Status").equalsIgnoreCase("Error")) {
                    txtMobile1.setText("");
                    btnVisitorOut.setVisibility(View.GONE);
                    txtVisitorOut.setVisibility(View.GONE);
                } else {
                    Toast.makeText(HomeActivity.this, ResultString, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private class AsyncCallWSMobile extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(HomeActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.loading));
            //show dialog
            dialog.show();
            //Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Log.i(TAG, "doInBackground");
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            ResultString = WebService.invokeWS();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //Log.i(TAG, "onPostExecute");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (ResultString.length() > 0) {
                try {
                    JSONArray js = new JSONArray(ResultString);
                    if (js.length() > 0) {
                        JSONObject jo = new JSONObject(js.get(0).toString());
                        String id = jo.getString("count");
                        if (id.equalsIgnoreCase("0")) {
                            //show new user
                            llHome.setVisibility(View.GONE);
                            llHomeNewUser.setVisibility(View.VISIBLE);
                            txtMobile.setText(txtMobile1.getText());
                            //HideKeyboard
                            hideKeyboard(HomeActivity.this);

                        } else {
                            visitorId = jo.getString("visitorid");
                            //show entry out
                            btnVisitorOut.setVisibility(View.VISIBLE);
                            txtVisitorOut.setVisibility(View.VISIBLE);


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    public static void hideKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    private class AsyncCallWSEmployee extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(HomeActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.loading));
            //show dialog
            dialog.show();
            //Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Log.i(TAG, "doInBackground");
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            ResultString = WebService.invokeWS();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //Log.i(TAG, "onPostExecute");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dateTimeDisp();
            toWhomMeet_data.clear();
            JSONArray temp = null;
            try {
                temp = new JSONArray(ResultString);
                for (int i = 0; i < temp.length(); i++) {
                    JSONObject object = new JSONObject(temp.getJSONObject(i).toString());
                    toWhomMeet_data.put(i + "", object.toString());
                }


            } catch (JSONException e) {
                e.printStackTrace();
                ll.clear();
                if(LPA != null){
                    LPA.notifyDataSetChanged();
                }
            }
            displayLeavePeriod();
        }
    }

    public void dateTimeDisp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy h:mm aa");
        String formattedDate = df.format(c.getTime());
        txtDateTime.setText(formattedDate);
        txtDateTime1.setText(formattedDate);


        Clock clock = new Clock(HomeActivity.this);
        clock.AddClockTickListner(new OnClockTickListner() {

            @Override
            public void OnSecondTick(Time currentTime) {
                //Log.d("Tick Test per Second", DateFormat.format("h:mm:ss aa ", currentTime.toMillis(true)).toString());

            }

            @Override
            public void OnMinuteTick(Time currentTime) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy ");
                String formattedDate = df.format(c.getTime());
                txtDateTime.setText(formattedDate + DateFormat.format("h:mm aa", currentTime.toMillis(true)).toString());
                txtDateTime1.setText(formattedDate + DateFormat.format("h:mm aa", currentTime.toMillis(true)).toString());

                //  Log.d("Tick Test per Minute",DateFormat.format("h:mm aa", currentTime.toMillis(true)).toString());

            }
        });

    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(HomeActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.loading));
            //show dialog
            dialog.show();
            //Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Log.i(TAG, "doInBackground");
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            ResultString = WebService.invokeWS();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //Log.i(TAG, "onPostExecute");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
           // createpdf();
            try {
                JSONObject js = new JSONObject(ResultString);
                if (js.has("Status") && js.getString("Status").equalsIgnoreCase("Success")) {
                    Toast.makeText(HomeActivity.this, "Response: " + js.getString("Message"), Toast.LENGTH_LONG).show();
                    refreshScreen();
                    // createpdf();
                } else {
                    Toast.makeText(HomeActivity.this, "Response: " + js.getString("Message"), Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(HomeActivity.this, "Response: " + ResultString, Toast.LENGTH_LONG).show();

            }


        }
    }

    private class AsyncCallWSPrint extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(HomeActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.loading));
            //show dialog
            dialog.show();
            //Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Log.i(TAG, "doInBackground");
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            ResultString = WebService.invokeWS();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //Log.i(TAG, "onPostExecute");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            // createpdf();
            try {
               // ResultString ="{\"Status\":\"Success\",\"Message\":\"Visitors Entry In Saved Successfully\",\"Data\":[{\"visitorid\":\"56\"}]}";
                JSONObject js = new JSONObject(ResultString);
                if (js.has("Status") && js.getString("Status").equalsIgnoreCase("Success")) {
                    Toast.makeText(HomeActivity.this, "Response: " + js.getString("Message"), Toast.LENGTH_LONG).show();
                    JSONArray ja = new JSONArray(js.getString("Data"));
                    visitorIdPrint = new JSONObject(ja.get(0).toString()).getString("visitorid");
                   // Log.i("Test : ",visitorIdPrint);
                    createpdf();
                    refreshScreen();

                    //  printPass();
                } else {
                    Toast.makeText(HomeActivity.this, "Response: " + js.getString("Message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(HomeActivity.this, "Response: " + ResultString, Toast.LENGTH_LONG).show();

            }


        }
    }

    public void dataRequired(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return;
    }

    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(HomeActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS_STORAGE, RequestPermissionCode);
        }
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        super.onRequestPermissionsResult(requestCode, permissions, result);
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    //   Toast.makeText(HomePageGridViewLayout.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    public void createpdf() {
        Rect bounds = new Rect();
        int pageWidth = 400;
        int pageheight = 600;
        int pathHeight = 2;

        PdfDocument myPdfDocument = new PdfDocument();




        Paint paintHeader = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint paintBody = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        Path path = new Path();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageheight, 1).create();
        PdfDocument.Page documentPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = documentPage.getCanvas();



        int y = 55; // x = 10,
        //int x = (canvas.getWidth() / 2);
        int x = 10;

        int spSize = 12;//your sp size
        // Convert the sp to pixels
        float scaledTextSize = spSize * getResources().getDisplayMetrics().scaledDensity;

        paintHeader.setTextSize(scaledTextSize);
        paintHeader.setColor(getResources().getColor(R.color.appColor));

        int spSize1 = 8;//your sp size
        // Convert the sp to pixels
        float scaledTextSize1 = spSize1 * getResources().getDisplayMetrics().scaledDensity;

        paintBody.setTextSize(scaledTextSize1);
        paintBody.setColor(getResources().getColor(R.color.appColor));

        String appName = "Visitor Pass";
        paintHeader.getTextBounds(appName, 0, appName.length(), bounds);
        x = (canvas.getWidth() / 2) - (bounds.width() / 2);
        canvas.drawText(appName, x, y, paintHeader);

        y += 10;
        canvas.drawText("", x, y, paint);

        Bitmap b = (Bitmap.createScaledBitmap(bitmap, 120, 150, false));
        x = (canvas.getWidth() / 2) - (120 / 2);
        canvas.drawBitmap(b, x, y, paint);
        y += 150;
        paint.getTextBounds(txtDateTime.getText().toString(), 0, txtDateTime.getText().toString().length(), bounds);
        x = (canvas.getWidth() / 2) - (bounds.width() / 2);
        y += paint.descent() - paint.ascent();
        canvas.drawText(txtDateTime.getText().toString(), x, y, paint);


        TextPaint mTextPaint = new TextPaint();
        mTextPaint.setTextSize(scaledTextSize1);
        mTextPaint.setColor(getResources().getColor(R.color.appColor));


        StaticLayout mTextLayoutName = new StaticLayout("Name: " + txtVisitorName.getText().toString(), mTextPaint, canvas.getWidth() - 100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        x = 50;
        y += 20;
        canvas.translate(x, y);
        mTextLayoutName.draw(canvas);
        canvas.restore();

        StaticLayout mTextLayoutMobile = new StaticLayout("Mobile: " + txtMobile.getText().toString(), mTextPaint, canvas.getWidth() - 100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        x = 50;
        int hightspace = 10;
        y += mTextLayoutName.getHeight() + hightspace;
        canvas.translate(x, y);
        mTextLayoutMobile.draw(canvas);
        canvas.restore();

        StaticLayout mTextLayoutAddress = new StaticLayout("Address: " + txtAddress.getText().toString(), mTextPaint, canvas.getWidth() - 100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        x = 50;
        y += mTextLayoutMobile.getHeight() + hightspace;
        canvas.translate(x, y);
        mTextLayoutAddress.draw(canvas);
        canvas.restore();

        StaticLayout mTextLayoutPur = new StaticLayout("Purpose: " + txtMsg.getText().toString(), mTextPaint, canvas.getWidth() - 100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        x = 50;
        y += mTextLayoutAddress.getHeight() + hightspace;
        canvas.translate(x, y);
        mTextLayoutPur.draw(canvas);
        canvas.restore();

        StaticLayout mTextLayoutMeet = new StaticLayout("To Meet: " + txtWhoMeet.getText().toString(), mTextPaint, canvas.getWidth() - 100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        x = 50;
        y += mTextLayoutPur.getHeight() + hightspace;
        canvas.translate(x, y);
        mTextLayoutMeet.draw(canvas);
        canvas.restore();


        //blank space
        y += paint.descent() - paint.ascent();
        canvas.drawText("", x, y, paint);


        //blank space
        y += paint.descent() - paint.ascent();
        canvas.drawText("", x, y, paint);

        // Resources res = getResources();
        // Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.srmuh_logo);
        y += 50;
        Rect r = new Rect(10, 10, 390,  y);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(r, paint);

        myPdfDocument.finishPage(documentPage);
        SimpleDateFormat df = new SimpleDateFormat("dd_mm_yyyy_hh_mm_ss");
        file_name_path = file_name_path + "Visitor" + df.format(new Date()) + ".pdf";
        File file = new File(file_name_path);
        Log.e("TEST: ", file_name_path);
        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
            Log.e("TEST: ", file_name_path);
            myPdfDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewPdfFile(getApplicationContext());
    }

    public void viewPdfFile(Context context) {
        File pdfFile = new File(file_name_path);//File path
        if (pdfFile.exists()) //Checking if the file exists or not
        {
            Log.e("TESTFile 1: ", file_name_path);
            Uri uri;
            if (Build.VERSION.SDK_INT < 24) {
                uri = Uri.fromFile(pdfFile);
            } else {
                uri = Uri.parse(pdfFile.getPath());
            }
            Intent objIntent = new Intent(Intent.ACTION_VIEW);
            objIntent.setDataAndType(uri, "application/pdf");
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            objIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(objIntent);//Starting the pdf viewer
        } else {

            Toast.makeText(getApplicationContext(), "The file not exists! ", Toast.LENGTH_SHORT).show();

        }
    }
}


