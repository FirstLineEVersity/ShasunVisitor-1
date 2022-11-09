package com.shasun.visitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class HistoryActivity extends AppCompatActivity {
    private static String ResultString = "";
    AutoCompleteTextView txtFromDate, txtToDate;
    ImageButton ibSearch;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    private final ArrayList<String> leavestatus_list = new ArrayList<String>(200);
    Button button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ibSearch = findViewById(R.id.ibSearch);
        txtFromDate = findViewById(R.id.txtFromDate);

        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtFromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard(HistoryActivity.this, txtFromDate);
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    final DatePickerDialog mDatePicker = new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                final DatePickerDialog mDatePicker = new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        txtToDate = findViewById(R.id.txtToDate);
        txtToDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    final DatePickerDialog mDatePicker = new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                final DatePickerDialog mDatePicker = new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(txtFromDate.getTag().toString());
            date2 = format.parse(txtToDate.getTag().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date1.compareTo(date2) > 0) {
            Toast.makeText(HistoryActivity.this, "From date must be less than To Date", Toast.LENGTH_LONG).show();

        } else {
            WebService.strParameters = new String[]{"String", "fromdate", txtFromDate.getTag().toString(), "String", "todate", txtToDate.getTag().toString()};
            WebService.METHOD_NAME = "getVisitorDetails";
            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        }
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = format.parse(txtFromDate.getTag().toString());
                    date2 = format.parse(txtToDate.getTag().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date1.compareTo(date2) > 0) {
                    Toast.makeText(HistoryActivity.this, "From date must be less than To Date", Toast.LENGTH_LONG).show();
                } else {
                    WebService.strParameters = new String[]{"String", "fromdate", txtFromDate.getTag().toString(), "String", "todate", txtToDate.getTag().toString()};
                    WebService.METHOD_NAME = "getVisitorDetails";
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                }
            }
        });
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

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
                        object.getString("visitorphoto"));
            }
            if (leavestatus_list.size() == 0) {
                Toast.makeText(HistoryActivity.this, "Response: No Data Found", Toast.LENGTH_LONG).show();
            } else {
                mRecyclerView = findViewById(R.id.rvLeaveStatus); // Assigning the RecyclerView Object to the xml View
                mRecyclerView.setHasFixedSize(true);
                // Letting the system know that the list objects are of fixed size
                LeaveStatusLVAdapter TVA = new LeaveStatusLVAdapter(leavestatus_list, R.layout.leavestatuslistitem);
                mRecyclerView.setAdapter(TVA);
                mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
                mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(HistoryActivity.this, "Response: " + ResultString, Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(HistoryActivity.this);

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
            runOnUiThread(new Runnable() {
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

}