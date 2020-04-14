package com.idea.church.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class DBNetworkService extends Service {
    Intent sendInfo;

    public DBNetworkService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendInfo = new Intent();
        sendInfo.setAction("MEDIA_PLAYER_INFO");

        return super.onStartCommand(intent, flags, startId);
    }

    // Binder given to clients
    private final IBinder iBinder = new DBNetworkService.LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class LocalBinder extends Binder {
        public DBNetworkService getService() {
            return DBNetworkService.this;
        }
    }
}
