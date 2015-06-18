package nl.nos.lab.twocents;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Browser;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;


public class WebviewActivity extends Activity {

    private static final String ARTICLE_FILE = "Article_hybrid.html";
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl("file:///android_asset/local_site/Article.html");

        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // If we do not know any special action, we just perform default URL handling.
                return defaultUrlHandling(view, url);
            }

            /**
             * This is the default handling if no web-client is set: open a dialog so the user can
             * choose what application can handle the url.
             *
             * @param view The webview.
             * @param url  The url to open. This is not a url that can be opened by the NOS app.
             * @return True if the url can be opened by an application, false otherwise.
             */
            private boolean defaultUrlHandling(WebView view, String url) {

                if (url.equals("file:///open_video")) {
                    startActivity(new Intent(WebviewActivity.this, CaptureActivity.class));
                    return true;
                }

                // Default intent handling from NullWebviewClient
                Intent intent;
                // Perform generic parsing of the URI to turn it into an Intent.
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                } catch (URISyntaxException ex) {
                    return false;
                }
                // Sanitize the Intent, ensuring web pages can not bypass browser
                // security (only access to BROWSABLE activities).
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setComponent(null);
                // Pass the package name as application ID so that the intent from the
                // same application can be opened in the same tab.
                intent.putExtra(Browser.EXTRA_APPLICATION_ID,
                        view.getContext().getPackageName());
                try {
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    return false;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (TwoCentsApplication.getInstance().getTipObject() != null) {
            webview.loadUrl("file:///android_asset/local_site/Article_after.html");
        }
    }
}