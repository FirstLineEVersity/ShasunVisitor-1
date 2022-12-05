package com.shasun.visitor;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryPassNewUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryPassNewUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageButton btnTakePhoto;
    ImageButton btnRefresh, btnPrint;
    private final LinkedHashMap<String, String> toWhomMeet_data = new LinkedHashMap<String, String>();
    RadioGroup radioGroup;
    ImageView imageView;
    private static String ResultString = "";
    TextInputEditText txtVisitorName, txtMobile1, txtAddress;
    AutoCompleteTextView txtWhoMeet;
    TextView txtDateTime1;
    EditText txtMsg;
    Bitmap bitmap = null;

    String visitorIdPrint = "";
    String mobileNo;
    String encodedImage = "";


    public EntryPassNewUserFragment(String mobileNo) {
        // Required empty public constructor
        this.mobileNo = mobileNo;
        visitorIdPrint = "";
        encodedImage = "";
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntryPassNewUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntryPassNewUserFragment newInstance(String param1, String param2) {
        EntryPassNewUserFragment fragment = new EntryPassNewUserFragment(null);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(WebService.isTab) {
            view = inflater.inflate(R.layout.fragment_entry_pass_new_user_tab, container, false);
        }else{
            view = inflater.inflate(R.layout.fragment_entry_pass_new_user_mobile, container, false);

        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        radioGroup = view.findViewById(R.id.radio);
        txtDateTime1 = view.findViewById(R.id.txtDateTime);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(checkedId);
                String purpose = (String) radioButton.getText();
                txtMsg.setText(purpose);
            }

        });

        final SharedPreferences loginsession = view.getContext().getSharedPreferences("SessionLogin", 0);
        btnTakePhoto = view.findViewById(R.id.buttonTake);
        // btnSave = findViewById(R.id.btnSave);
        btnPrint = view.findViewById(R.id.btnPrint);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        imageView = view.findViewById(R.id.imageView);
        txtVisitorName = view.findViewById(R.id.txtVisitorName);
        txtMobile1 = view.findViewById(R.id.txtMobile);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtMsg = view.findViewById(R.id.txtMsg);
        txtWhoMeet = view.findViewById(R.id.txtWhoMeet);
        txtMobile1.setText(mobileNo);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataPrint();
                //printPass();
            }
        });


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refreshData();
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

                } else {
                    ll.clear();
                    if (LPA != null) {
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
                    //txtWhoMeet.setText(jo.getString("staffname") + " - " + jo.getString("department"));
                    txtWhoMeet.setText(jo.getString("staffname") + " - " + jo.getString("department") + " - Code:" + jo.getString("staffcode"));
                    txtWhoMeet.setTag(jo.getString("staffid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        dateTimeDisp();

        txtMsg.setText("Meeting");
        refreshData();
        return view;
    }

    public void dateTimeDisp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy h:mm aa");
        String formattedDate = df.format(c.getTime());
        txtDateTime1.setText(formattedDate);


        Clock clock = new Clock(getContext());
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
                txtDateTime1.setText(formattedDate + DateFormat.format("h:mm aa", currentTime.toMillis(true)).toString());

                //  Log.d("Tick Test per Minute",DateFormat.format("h:mm aa", currentTime.toMillis(true)).toString());

            }
        });

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
        if (LPA != null) {
            LPA.notifyDataSetChanged();
        }
        bitmap = null;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    public void dataRequired(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        return;
    }

    public void saveDataPrint() {
        if (bitmap == null) {
            dataRequired("Please Take Photo");
        } else if (txtVisitorName.getText().toString().length() < 1) {
            dataRequired("Please Enter Visitor Name");
        } else if (txtMobile1.getText().toString().length() < 1) {
            dataRequired("Please Enter Visitor Mobile Number");
        } else if (txtAddress.getText().toString().length() < 1) {
            dataRequired("Please Enter Visitor Address");
        } else if (txtWhoMeet.getText().toString().length() < 1) {
            dataRequired("Please Select to Whom you want to meet");
        } else if (txtMsg.getText().toString().length() < 1) {
            dataRequired("Please enter purpose");
        } else if (txtVisitorName.getText().toString().contains("'")) {
            dataRequired("Please remove 'single code' in Visitor Name");
        } else if (txtMobile1.getText().toString().contains("'")) {
            dataRequired("Please remove 'single code' in Mobile Number");
        } else if (txtAddress.getText().toString().contains("'")) {
            dataRequired("Please remove 'single code' in Address");
        } else if (txtWhoMeet.getText().toString().contains("'")) {
            dataRequired("Please remove 'single code' in Whom you want to meet");
        } else if (txtMsg.getText().toString().contains("'")) {
            dataRequired("Please remove 'single code' in purpose");
        } else {


            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
            String purpose = (String) radioButton.getText();

            if (bitmap != null) {
                encodedImage = getBase64String(bitmap);
            }
            if (!CheckNetwork.isInternetAvailable(getContext())) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();

            } else {
                WebService.strParameters = new String[]{"String", "visitorname", txtVisitorName.getText().toString(), "String", "visitoraddress", txtAddress.getText().toString(), "String", "visitorcontactno", txtMobile1.getText().toString(), "String", "purposeofvisit", txtMsg.getText().toString(), "String", "towhommeet", txtWhoMeet.getTag().toString(), "String", "visitorphoto", encodedImage};
                WebService.METHOD_NAME = "saveVisitorJson";
                AsyncCallWSPrint task = new AsyncCallWSPrint();
                task.execute();
            }
        }

    }

    private String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] imageBytes = baos.toByteArray();

        String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        return base64String;
    }

    private class AsyncCallWSPrint extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(getContext());

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
            if (ResultString.length() > 0) {
                try {
                    // ResultString ="{\"Status\":\"Success\",\"Message\":\"Visitors Entry In Saved Successfully\",\"Data\":[{\"visitorid\":\"56\"}]}";
                    JSONObject js = new JSONObject(ResultString);
                    if (js.has("Status") && js.getString("Status").equalsIgnoreCase("Success")) {
                        Toast.makeText(getContext(), "Response: " + js.getString("Message"), Toast.LENGTH_LONG).show();
                        JSONArray ja = new JSONArray(js.getString("Data"));
                        visitorIdPrint = new JSONObject(ja.get(0).toString()).getString("visitorid");

                        printPass();


                    } else {
                        Toast.makeText(getContext(), "Response: " + js.getString("Message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Response: " + ResultString, Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(getContext(), "Response: " + ResultString, Toast.LENGTH_LONG).show();

            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TEST", "RESUME CALLED");
        if (visitorIdPrint.length() > 0) {
            setFragment();
        }
    }

    protected void setFragment() {

        EntryPassCheckFragment fragment2 = new EntryPassCheckFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main_activity_navigation, fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void printPass() {
        if (visitorIdPrint.length() > 0) {
            if (!CheckNetwork.isInternetAvailable(getContext())) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
                return;
            } else {
                WebService.strParameters = new String[]{"String", "visitorname", txtVisitorName.getText().toString(),
                        "String", "visitoraddress", txtAddress.getText().toString(), "String", "visitorcontactno", txtMobile1.getText().toString(), "String", "purposeofvisit", txtMsg.getText().toString(), "String", "towhommeet", txtWhoMeet.getTag().toString(), "String", "visitorphoto", encodedImage};

                String dataS = txtVisitorName.getText().toString() + "##" +
                        txtAddress.getText().toString() + "##" + txtMsg.getText().toString() + "##" +
                        txtWhoMeet.getText().toString() + "##" + txtMobile1.getText().toString() + "##" +
                        txtDateTime1.getText().toString() + "##" + txtDateTime1.getText().toString() + "##" +
                        encodedImage + "##" + visitorIdPrint;
                Context context = getContext();
                Intent intent;
                intent = new Intent(context, TempActivity.class);
                intent.putExtra("visitorData", dataS);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);

                 /*

                ViewPassFragment fragment = new ViewPassFragment(visitorIdPrint);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_main_activity_navigation, fragment).addToBackStack(null).commit();


                  */
            }

        }
    }

    public void ToWhomToMeet() {
        if (!CheckNetwork.isInternetAvailable(getContext())) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();

        } else {
            WebService.METHOD_NAME = "getEmployeeJson";

            WebService.strParameters = new String[]{"String", "employeename", txtWhoMeet.getText().toString()};
            if (!txtWhoMeet.getText().toString().contains("{\"staffcode\"") && !txtWhoMeet.getText().toString().contains("- Code:")) {
                AsyncCallWSEmployee task = new AsyncCallWSEmployee();
                task.execute();
            }
        }

    }

    private class AsyncCallWSEmployee extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(getContext());

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
                if (LPA != null) {
                    LPA.notifyDataSetChanged();
                }
            }
            displayLeavePeriod();
        }
    }

    List ll = new ArrayList<String>();
    SpinnerListAdapter LPA;

    private void displayLeavePeriod() {
        if (toWhomMeet_data.size() == 0) {
            ll.clear();
            if (LPA != null) {
                LPA.notifyDataSetChanged();
            }
            Toast.makeText(getContext(), "Response: No Data Found", Toast.LENGTH_LONG).show();
        } else {
            Collection<String> LeavePeriodcollection = toWhomMeet_data.values();
            String[] arrayLeavePeriod = LeavePeriodcollection.toArray(new String[LeavePeriodcollection.size()]);
            // ArrayAdapter<String> LPA = new ArrayAdapter<String>(this, R.layout.dropdownlistitem, arrayLeavePeriod);
            ll.clear();
            for (String item : arrayLeavePeriod) {
                ll.add(item);
            }
            LPA = new SpinnerListAdapter(getContext(), R.layout.gridlayout_cardview_home_page, R.id.txtWhoMeet, ll);

            txtWhoMeet.setAdapter(LPA);
            txtWhoMeet.showDropDown();
            txtWhoMeet.requestFocus();
        }
    }
}