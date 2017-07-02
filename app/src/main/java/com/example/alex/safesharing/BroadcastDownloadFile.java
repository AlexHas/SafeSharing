package com.example.alex.safesharing;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

/**
 * Created by alexHash on 02.06.2017.
 */

public class BroadcastDownloadFile extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager.Query query = null;
        Cursor c = null;
        DownloadManager downloadManager = null;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        query = new DownloadManager.Query();
        if(query!=null) {
            query.setFilterByStatus(DownloadManager.STATUS_FAILED|DownloadManager.STATUS_PAUSED|DownloadManager.STATUS_SUCCESSFUL|
                    DownloadManager.STATUS_RUNNING|DownloadManager.STATUS_PENDING);
        } else {
            return;
        }
        c = downloadManager.query(query);
        if(c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch(status) {
                case DownloadManager.STATUS_PAUSED:
                    break;
                case DownloadManager.STATUS_PENDING:
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    break;
            }
        }
    }

}
