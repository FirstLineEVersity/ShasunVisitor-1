package com.shasun.visitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PrintWebViewActivity extends AppCompatActivity {
    private WebView mMainWebview;
    private WebView print_webview;
    FloatingActionButton fab;
    boolean dataCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainWebview = new WebView(PrintWebViewActivity.this);
        setContentView(R.layout.activity_print_web_view);// mention layout of you activity here
        FrameLayout webViewpar = (FrameLayout) findViewById(R.id.webViewParent);
        webViewpar.addView(mMainWebview);
        fab = findViewById(R.id.fab);

        print_webview = (WebView) findViewById(R.id.print_view); //view on which you want to take a printout
        mMainWebview.addJavascriptInterface(new PrintWebViewActivity.WebViewJavaScriptInterface(this), "androidapp"); //
        // "androidapp is used to call methods exposed from javascript interface, in this example case print
        // method can be called by androidapp.print(String)"
        // load your data from the URL in web view
        if (savedInstanceState != null) {
            mMainWebview.restoreState(savedInstanceState);
        } else {
           // mMainWebview.loadUrl("your website URL");
            mMainWebview.loadUrl("http://erp.shasuncollege.edu.in/evarsityshasun/general/transaction/VenueBookingforMobile.jsp?EmployeeName=GOMATHI.B&EmployeeId=66");

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWebPagePrint(mMainWebview);
            }
        });
        mMainWebview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                createWebPagePrint(mMainWebview);
                dataCalled = true;
                //finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dataCalled){
            onBackPressed();
        }
    }

    public class WebViewJavaScriptInterface {

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(Context context) {

        }

        /**
         * method defined by developer, which will be called by web page , from web developer
         * this you can call when want to take a print, either on click of a button or directly
         * Convert HTML data, to be printed,  in form of a string and pass to this method.
         *
         * @param data
         */
        @JavascriptInterface
        public void print(final String data) {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    doWebViewPrint(data);

                }
            });
        }
    }

    private void doWebViewPrint(String ss) {

        print_webview.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                fab.setVisibility(View.VISIBLE);


                super.onPageFinished(view, url);
            }
        });
        // Generate an HTML document on the fly:
        print_webview.loadDataWithBaseURL(null, ss, "text/html", "UTF-8", null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("TEST","BACK CALLED");
    }

    public void createWebPagePrint(WebView webView) {

        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        String jobName = getString(R.string.app_name) + " Document";
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5);
        PrintJob printJob = printManager.print(jobName, printAdapter, builder.build());

        if (printJob.isCompleted()) {
            Toast.makeText(getApplicationContext(), R.string.print_complete, Toast.LENGTH_LONG).show();
        } else if (printJob.isFailed()) {
            Toast.makeText(getApplicationContext(), R.string.print_failed, Toast.LENGTH_LONG).show();
        }
    }
}