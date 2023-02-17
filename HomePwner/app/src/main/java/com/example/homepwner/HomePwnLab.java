package com.example.homepwner;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomePwnLab {
    private static HomePwnLab sItemLab;
    private List<Home> mItems;
    private Context mContext;

    private HomePwnLab(Context context)
    {
        mContext = context.getApplicationContext();
        mItems = new ArrayList<Home>();
        Home newItem = new Home();
        newItem.setName("Rusty Bear");
        newItem.setSerial();
        newItem.setValue(94.0);
        mItems.add(newItem);
        newItem = new Home();
        newItem.setName("Fluffy Bear");
        newItem.setSerial();
        newItem.setValue(59.0);
        mItems.add(newItem);
        newItem = new Home();
        newItem.setName("Fluffy Spork");
        newItem.setSerial();
        newItem.setValue(66.0);
        mItems.add(newItem);
        newItem = new Home();
        newItem.setName("Fluffy Mac");
        newItem.setSerial();
        newItem.setValue(72.0);
        mItems.add(newItem);
        newItem = new Home();
        newItem.setName("Teddy Bear");
        newItem.setSerial();
        newItem.setValue(40.0);
        mItems.add(newItem);

    }

    public static HomePwnLab get(Context context) {
        if (sItemLab == null) {
            sItemLab = new HomePwnLab(context);
        }
        return sItemLab;
    }
    public void addItem(Home i)
    {
        mItems.add(i);
    }
    public List<Home> getmItems() {
        return mItems;
    }
    public Home getItem(UUID id) {
        for (Home item : mItems) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public File getPhotoFile(Home item) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, item.getPhotoFilename());
    }

}
