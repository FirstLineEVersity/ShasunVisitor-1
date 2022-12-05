package com.shasun.visitor;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryPassCheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryPassCheckFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextInputEditText  txtMobile;
    TextView txtDateTime;
    ImageButton btnVisitorOut;
    TextView txtVisitorOut;
    private static String ResultString = "";
    String visitorId = "";


    public EntryPassCheckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntryPassCheckFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntryPassCheckFragment newInstance(String param1, String param2) {
        EntryPassCheckFragment fragment = new EntryPassCheckFragment();
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
            view = inflater.inflate(R.layout.fragment_entry_pass_check_tab, container, false);
        }else{
            view = inflater.inflate(R.layout.fragment_entry_pass_check_mobile, container, false);

        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        txtMobile = view.findViewById(R.id.txtMobile1);
        txtMobile.setText("");
        txtDateTime = view.findViewById(R.id.txtDateTime1);
        btnVisitorOut = view.findViewById(R.id.btnVisitorOut);
        txtVisitorOut = view.findViewById(R.id.txtVisitorOut);
        btnVisitorOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveVisitorOutData();
            }
        });
        txtMobile.addTextChangedListener(new TextWatcher() {
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
        dateTimeDisp();
        txtMobile.setText("");
        btnVisitorOut.setVisibility(View.GONE);
        txtVisitorOut.setVisibility(View.GONE);
        return view;
    }
    public void saveVisitorOutData() {
        if (!CheckNetwork.isInternetAvailable(getContext())) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();

        } else {
            WebService.METHOD_NAME = "visitorEntryOutJson";
            WebService.strParameters = new String[]{"String", "mobileno", txtMobile.getText().toString(), "Long", "visitorid", visitorId};
            AsyncCallWSEntryOut task = new AsyncCallWSEntryOut();
            task.execute();
        }

    }
    public void dataRequired(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        return;
    }
    private class AsyncCallWSEntryOut extends AsyncTask<Void, Void, Void> {
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
            //nithin
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                JSONObject js = new JSONObject(ResultString);
                if (!js.getString("Status").equalsIgnoreCase("Error")) {
                    txtMobile.setText("");
                    btnVisitorOut.setVisibility(View.GONE);
                    txtVisitorOut.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), ResultString, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void MobileNoValidation() {
        if (!CheckNetwork.isInternetAvailable(getContext())) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();

        } else {
            WebService.METHOD_NAME = "checkMobileNumberJson";
            WebService.strParameters = new String[]{"String", "mobileno", txtMobile.getText().toString()};
            AsyncCallWSMobile task = new AsyncCallWSMobile();
            task.execute();
        }
    }

    private class AsyncCallWSMobile extends AsyncTask<Void, Void, Void> {
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

            if (ResultString.length() > 0) {
                try {
                    JSONArray js = new JSONArray(ResultString);
                    if (js.length() > 0) {
                        JSONObject jo = new JSONObject(js.get(0).toString());
                        String id = jo.getString("count");
                        if (id.equalsIgnoreCase("0")) {
                            //show new user
                            //txtMobile.setText(txtMobile1.getText());
                            //HideKeyboard
                            //FragmentTwo CCall
                            setFragment();


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

    protected void setFragment() {

        EntryPassNewUserFragment fragment2 = new EntryPassNewUserFragment(txtMobile.getText().toString());
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main_activity_navigation, fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void dateTimeDisp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy h:mm aa");
        String formattedDate = df.format(c.getTime());
        txtDateTime.setText(formattedDate);


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
                txtDateTime.setText(formattedDate + DateFormat.format("h:mm aa", currentTime.toMillis(true)).toString());

                //  Log.d("Tick Test per Minute",DateFormat.format("h:mm aa", currentTime.toMillis(true)).toString());

            }
        });

    }
}