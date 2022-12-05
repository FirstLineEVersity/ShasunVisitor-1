package com.shasun.visitor;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewVisitorPassPdf extends AppCompatActivity {

    private int visitorIdPrint=0;
    private String strPdfUrl="";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras()!=null){
            visitorIdPrint=Integer.parseInt(getIntent().getExtras().getString("visitorIdPrint"));
        }
        strPdfUrl = "https://erp.shasuncollege.edu.in/evarsityshasun/usermanager/loginManager/frmVisitorPrint.jsp?hdnVisitorId=" + visitorIdPrint;
   if(CheckNetwork.isInternetAvailable(ViewVisitorPassPdf.this)){
            MyPaySlipTask task = new MyPaySlipTask();
            task.execute(strPdfUrl);
        }
        else {
            Toast.makeText(ViewVisitorPassPdf.this,getResources().getString(R.string.loginNoInterNet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private class MyPaySlipTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(params[0]).openConnection();
                con.setRequestMethod("HEAD");
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            boolean bResponse = result;
            if (bResponse==true){
                try {
                    File file = new File(strPdfUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri;
                    if (Build.VERSION.SDK_INT < 24) {
                        uri = Uri.fromFile(file);
                    } else {
                        uri = Uri.parse(file.getPath());
                    }
                    intent.setDataAndType(uri,"application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                } catch (ActivityNotFoundException anfe) {
                    Toast.makeText(ViewVisitorPassPdf.this, "No activity found to open this attachment.", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(ViewVisitorPassPdf.this, "File does not exist!: Payroll not approved / PDF File not generated in ERP", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
