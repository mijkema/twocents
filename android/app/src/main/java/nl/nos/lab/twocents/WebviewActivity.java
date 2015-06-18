package nl.nos.lab.twocents;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;


public class WebviewActivity extends ActionBarActivity {

    private static final String ARTICLE_FILE = "Article_hybrid.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        WebView webview = (WebView) findViewById(R.id.webview);
        try {
            String articleHtml = readAsString(ARTICLE_FILE);
            webview.loadDataWithBaseURL("http://nos.nl", articleHtml, null, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

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

                if (url.equals("http://nos.nl/open_video")) {
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

    /**
     * Read a file in the assets folder associated with ctx as a string.
     *
     * @param assetFile The file to read.
     * @return The string contents of the requested file.
     * @throws java.io.IOException If the file does not exist.
     */
    public String readAsString(String assetFile) throws IOException {
        // Open the file.
        InputStream stream = getAssets().open(assetFile);

        // Read the file from the stream.
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}