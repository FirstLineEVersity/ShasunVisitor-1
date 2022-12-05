package com.shasun.visitor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    private static String ResultString = "";
    AutoCompleteTextView txtFromDate, txtToDate;
    TextView noData;
    ImageButton ibSearch;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    private final ArrayList<String> leavestatus_list = new ArrayList<String>(200);
    Button button_back;
    View view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(WebService.isTab) {
            view = inflater.inflate(R.layout.fragment_history_tab, container, false);
        }else{
            view = inflater.inflate(R.layout.fragment_history_mobile, container, false);

        }
        ibSearch = view.findViewById(R.id.ibSearch);
        txtFromDate = view.findViewById(R.id.txtFromDate);
        noData = view.findViewById(R.id.noData);


        txtFromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    final DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                            // TODO Auto-generated method stub
                            /*      Your code   to get date and time    */

                            String year1 = String.valueOf(selectedYear);
                            String month1 = String.valueOf(selectedMonth + 1);
                            String day1 = String.valueOf(selectedDay);
                            txtFromDate.setText(day1 + "/" + month1 + "/" + year1);
                            txtFromDate.setTag(year1 + "-" + month1 + "-" + day1);

                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.setTitle("Select From Date");
                    mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                    mDatePicker.show();

                }
            }
        });
        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */

                        String year1 = String.valueOf(selectedYear);
                        String month1 = String.valueOf(selectedMonth + 1);
                        String day1 = String.valueOf(selectedDay);
                        txtFromDate.setText(day1 + "/" + month1 + "/" + year1);
                        txtFromDate.setTag(year1 + "-" + month1 + "-" + day1);

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select From Date");
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
        txtToDate = view.findViewById(R.id.txtToDate);
        txtToDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    final DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                            // TODO Auto-generated method stub
                            /*      Your code   to get date and time    */

                            String year1 = String.valueOf(selectedYear);
                            String month1 = String.valueOf(selectedMonth + 1);
                            String day1 = String.valueOf(selectedDay);
                            txtToDate.setText(day1 + "/" + month1 + "/" + year1);
                            txtToDate.setTag(year1 + "-" + month1 + "-" + day1);

                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.setTitle("Select From Date");
                    mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                    mDatePicker.show();
                }
            }
        });
        txtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */

                        String year1 = String.valueOf(selectedYear);
                        String month1 = String.valueOf(selectedMonth + 1);
                        String day1 = String.valueOf(selectedDay);
                        txtToDate.setText(day1 + "/" + month1 + "/" + year1);
                        txtToDate.setTag(year1 + "-" + month1 + "-" + day1);

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select From Date");
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });


        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        mMonth = mMonth + 1;
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        txtFromDate.setText(mDay + "/" + mMonth + "/" + mYear);
        txtFromDate.setTag(mYear + "-" + mMonth + "-" + mDay);
        txtToDate.setText(mDay + "/" + mMonth + "/" + mYear);
        txtToDate.setTag(mYear + "-" + mMonth + "-" + mDay);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(txtFromDate.getTag().toString());
            date2 = format.parse(txtToDate.getTag().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date1.compareTo(date2) > 0) {
            Toast.makeText(getContext(), "From date must be less than To Date", Toast.LENGTH_LONG).show();

        } else {
            if (!CheckNetwork.isInternetAvailable(getContext())) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();

            } else {
                WebService.strParameters = new String[]{"String", "fromdate", txtFromDate.getTag().toString(), "String", "todate", txtToDate.getTag().toString()};
                WebService.METHOD_NAME = "getVisitorDetails";
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        }
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int diff = getDateDiffFromNow(txtFromDate.getTag().toString(), txtToDate.getTag().toString());
                if (diff > 0) {
                    Toast.makeText(getContext(), "From date must be less than To Date", Toast.LENGTH_LONG).show();
                    if (TVA != null) {
                        leavestatus_list.clear();
                        TVA.notifyDataSetChanged();
                    }
                    displayLeaveStatus();
                } else {
                    WebService.strParameters = new String[]{"String", "fromdate", txtFromDate.getTag().toString(), "String", "todate", txtToDate.getTag().toString()};
                    WebService.METHOD_NAME = "getVisitorDetails";
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                }
            }
        });
        return view;
    }

    public int getDateDiffFromNow(String fromDate, String toDate) {
        int days = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            long diff = sdf.parse(fromDate).getTime() - sdf.parse(toDate).getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            days = ((int) (long) hours / 24);
         //   Log.i(TAG, "Date " + fromDate + " Difference From Now :" + days + " days");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    displayLeaveStatus();
                }
            });

            // Toast.makeText(HistoryActivity.this, "Response: " + ResultString, Toast.LENGTH_LONG).show();


        }
    }

    HistoryLVAdapter TVA;

    private void displayLeaveStatus() {
        leavestatus_list.clear();
        try {
            JSONArray temp = new JSONArray(ResultString);
            // Log.i("TEST: ",ResultString.toString());
            for (int i = 0; i <= temp.length() - 1; i++) {
                JSONObject object = new JSONObject(temp.getJSONObject(i).toString());
                String outTime = "-";
                if (object.has("visitorouttime")) {
                    outTime = object.getString("visitorouttime");
                }
                leavestatus_list.add(object.getString("visitorname") + "##" +
                        object.getString("visitoraddress") + "##" + object.getString("purpose") + "##" +
                        object.getString("towhommeet") + "##" + object.getString("contactno") + "##" +
                        object.getString("visitorintime") + "##" + outTime + "##" +
                        object.getString("visitorphoto") + "##" + object.getString("visitorid"));
            }
            if (leavestatus_list.size() == 0) {
                Toast.makeText(getContext(), "Response: No Data Found", Toast.LENGTH_LONG).show();
                if (TVA != null) {
                    TVA.notifyDataSetChanged();
                }
                noData.setVisibility(View.VISIBLE);
            } else {
                noData.setVisibility(View.GONE);
                mRecyclerView = view.findViewById(R.id.rvLeaveStatus); // Assigning the RecyclerView Object to the xml View
                mRecyclerView.setHasFixedSize(true);
                // Letting the system know that the list objects are of fixed size
                if(WebService.isTab) {
                    TVA = new HistoryLVAdapter(leavestatus_list, R.layout.historylistitem_tab);
                }else{
                    TVA = new HistoryLVAdapter(leavestatus_list, R.layout.historylistitem_mobile);
                }
               mRecyclerView.setAdapter(TVA);
                mLayoutManager = new LinearLayoutManager(getContext());                 // Creating a layout Manager
                mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(getContext(), "Response: " + ResultString, Toast.LENGTH_LONG).show();
        }


    }

}