package com.example.alex.safesharing;

import android.app.Service;
import android.content.Intent;
import android.os.FileObserver;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by alexHash on 10.06.2017.
 */

public class ServiceMonitor extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        String mainFolder = intent.getStringExtra("mainFolder");

        Toast.makeText(getApplicationContext(), "Service start! Main folder is: " + mainFolder, Toast.LENGTH_LONG).show();
        FileObserver observer=new FileObserver(mainFolder) {
            @Override
            public void onEvent(int event, String path) {
                if(path==null){
                    return;
                }
                if((FileObserver.CREATE & event)!=0){
                    Toast.makeText(getApplicationContext(), "A fost creat fisierul: "+ path,Toast.LENGTH_LONG).show();

                }
            }
        };

        observer.startWatching();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Service stopped!",Toast.LENGTH_LONG).show();
    }
}
