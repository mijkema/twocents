package nl.nos.lab.twocents;

import android.app.Application;

/**
 * @author Matthijs IJkema (18-06-15).
 */
public class TwoCentsApplication extends Application {

    private static TwoCentsApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static TwoCentsApplication getInstance() {
        return instance;
    }
}
