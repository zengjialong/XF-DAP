package com.chs.mt.xf_dap.bean;


import android.bluetooth.BluetoothDevice;

public class BTinfo {

	public boolean sel = false;
	public BluetoothDevice device = null;
    public Boolean BoolStartCnt = false;
    public int time = 5;

    public BTinfo(boolean ssel,BluetoothDevice sdevice) {
        super();
        this.sel = ssel;
        this.device = sdevice;
    }
    public BTinfo() {
        super();
        this.sel = false;
        this.device = null;
    }
}
