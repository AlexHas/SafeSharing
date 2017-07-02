package com.example.alex.safesharing;

import android.os.FileObserver;
import android.widget.Toast;

import static android.R.attr.path;

/**
 * Created by alexHash on 10.06.2017.
 */

public class FolderObserver extends FileObserver {
    private String folderPath;
    public FolderObserver(String path){
        super(path, FileObserver.ALL_EVENTS);
        folderPath=path;
    }
    @Override
    public void onEvent(int i, String s) {
        if(s==null){
            return;
        }
        if ((FileObserver.CREATE & i)!=0) {



        }
    }
}
