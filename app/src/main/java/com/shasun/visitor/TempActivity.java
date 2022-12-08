package com.shasun.visitor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;

public class TempActivity extends Activity {
    private WebView mMainWebview;
    private WebView wvVenueBooking;
    FloatingActionButton fab;
    String visitorData;
    String visitorName;
    String visitorMobileNo;
    String visitorAddress;
    String visitorPurpose;
    String visitorDateTime;
    String visitorWhomToMeet;
    String visitorImage;
    String shasunLogo;

    boolean dataCalled = false;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        //initialize webview
        wvVenueBooking = findViewById(R.id.myWebView);
        fab = findViewById(R.id.fab);
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            visitorData = b.getString("visitorData", "");
            String[] strColumns = visitorData.split("##");
            visitorName = strColumns[0];
            visitorMobileNo = strColumns[4];
            visitorAddress = strColumns[1];
            visitorPurpose = strColumns[2];
            visitorDateTime = strColumns[5];
            visitorWhomToMeet = strColumns[3];
            visitorImage = strColumns[7];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slogovisitor);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        shasunLogo = Base64.encodeToString(imageBytes, Base64.DEFAULT);


        //add webview client to handle event of loading
        wvVenueBooking.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                //if page loaded successfully then show print button
                printPDF(wvVenueBooking);
                dataCalled = true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printPDF(wvVenueBooking);
            }
        });

        //prepare your html content which will be show in webview
        String htmlDocument1 = "<html>\n" +
                "<body>\n" +
                "    <div  id=\"divDetail\" >\n" +
                "        <div  class=\"rTableRow\">\n" +
                "            <tlb:process resultSet=\"<%=objDao.getVisitorDetailsPrint(longVisitorId)%>\">\n" +
                "                <table width=\"40%\"  style=\"border: thin solid black\" CELLSPACING=\"10\" CELLPADDING=\"5\" id=\"example2\" align=\"left\" >\n" +
                "                    <tr>\n" +
                "                        <td colspan=\"2\" align=\"center\"><img src=\"data:image/png;base64," + shasunLogo + "\" style=\"height:110px;width:350px;vertical-align:top;\"><td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td><b>VISITOR PASS</b></td>\n" +
                "                        <td><img style=\"height:120px;width:90px;\" src=\"data:image/png;base64," + visitorImage + "\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>Date & Time</td>\n" +
                "                        <td nowrap>" + visitorDateTime + "</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>Visitor Name</td>\n" +
                "                        <td>" + visitorName + "</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>Address</td>\n" +
                "                        <td>" + visitorAddress + "</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>Contact No.</td>\n" +
                "                        <td>" + visitorMobileNo + "</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>Whom Meet</td>\n" +
                "                        <td>" + visitorWhomToMeet + "</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>Purpose</td>\n" +
                "                        <td>" + visitorPurpose + "</td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </tlb:process>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        wvVenueBooking.loadData(htmlDocument1, "text/HTML", "UTF-8");

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(dataCalled){
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("TEST","BACK CALLED");
    }

    //create a function to create the print job
    private void createWebPrintJob(WebView webView) {

        //create object of print manager in your device
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        //create object of print adapter
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        //provide name to your newly generated pdf file
        String jobName = getString(R.string.app_name) + " Print Test";

        //open print dialog
        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }

    //perform click pdf creation operation on click of print button click
    public void printPDF(View view) {
        createWebPrintJob(wvVenueBooking);
    }
}