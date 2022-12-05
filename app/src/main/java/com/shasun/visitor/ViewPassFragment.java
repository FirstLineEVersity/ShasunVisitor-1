package com.shasun.visitor;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPassFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
String visitorPassId = "";
    public ViewPassFragment(String visitorPassId) {
        // Required empty public constructor
        this.visitorPassId = visitorPassId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPassFragment newInstance(String param1, String param2) {
        ViewPassFragment fragment = new ViewPassFragment(null);
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
    private WebView mMainWebview;
    private WebView print_webview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_pass, container, false);
        mMainWebview = new WebView(getActivity());
        FrameLayout webViewpar = (FrameLayout) view.findViewById(R.id.webViewParent);
        webViewpar.addView(mMainWebview);


        print_webview = (WebView) view.findViewById(R.id.print_view); //view on which you want to take a printout
        mMainWebview.addJavascriptInterface(new WebViewJavaScriptInterface(getContext()), "androidapp"); //
        // "androidapp is used to call methods exposed from javascript interface, in this example case print
        // method can be called by androidapp.print(String)"
        // load your data from the URL in web view
        if (savedInstanceState != null) {
            mMainWebview.restoreState(savedInstanceState);
        } else {
            // mMainWebview.loadUrl("your website URL");
            mMainWebview.loadUrl("http://erp.shasuncollege.edu.in/evarsityshasun/general/transaction/VenueBookingforMobile.jsp?EmployeeName=GOMATHI.B&EmployeeId=66");

        }

        mMainWebview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                createWebPagePrint(mMainWebview);
            }
        });
        return view;
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

            getActivity().runOnUiThread(new Runnable() {

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
                createWebPagePrint(view);
                Log.e("RADHA TEST","RADHA TEST LOADING COMPLETED");
                super.onPageFinished(view, url);

            }
        });
        // Generate an HTML document on the fly:
        print_webview.loadDataWithBaseURL(null, ss, "text/html", "UTF-8", null);
    }

    public void createWebPagePrint(WebView webView) {

        PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        String jobName = getString(R.string.app_name) + " Document";
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5);
        PrintJob printJob = printManager.print(jobName, printAdapter, builder.build());

        if (printJob.isCompleted()) {
            Toast.makeText(getContext(), R.string.print_complete, Toast.LENGTH_LONG).show();
        } else if (printJob.isFailed()) {
            Toast.makeText(getContext(), R.string.print_failed, Toast.LENGTH_LONG).show();
        }
    }
}