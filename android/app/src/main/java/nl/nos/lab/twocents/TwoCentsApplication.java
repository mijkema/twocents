package nl.nos.lab.twocents;

import android.app.Application;

/**
 * @author Matthijs IJkema (18-06-15).
 */
public class TwoCentsApplication extends Application {

    private static TwoCentsApplication instance;
    private TipObject tipObject;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static TwoCentsApplication getInstance() {
        return instance;
    }

    public void set(TipObject tipObject) {
        this.tipObject = tipObject;
    }

    public TipObject getTipObject() {
        return tipObject;
    }
}
