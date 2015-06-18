package nl.nos.lab.twocents;

import android.graphics.Bitmap;

import java.util.List;

/**
 * @author Matthijs IJkema (18-06-15).
 */
public class TipObject {
    private final List<Bitmap> list;
    private final String name;
    private final String title;

    public TipObject(List<Bitmap> createdBitmapList, String name, String title) {
        list = createdBitmapList;
        this.name = name;
        this.title = title;
    }

    public List<Bitmap> getList() {
        return list;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }
}
