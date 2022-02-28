package com.chs.mt.xf_dap.util;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.chs.mt.xf_dap.datastruct.MacCfg;

public class TestUtil  implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        System.out.println("BUG 进入了好吧");
        MacCfg.addressName=new LocationUtils().showLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
