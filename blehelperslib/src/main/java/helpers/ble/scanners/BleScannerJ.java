package helpers.ble.scanners;

import android.bluetooth.BluetoothAdapter;

import java.util.concurrent.TimeUnit;

import helpers.ble.base.BleScanner;

/**
 * Created by Constantine Mars on 6/13/16.
 * Scanner for Android Jelly Bean
 */

public class BleScannerJ extends BleScanner {

    private BluetoothAdapter.LeScanCallback callback = (device, rssi, scanRecord) -> addDevice(device, rssi);

    public BleScannerJ(ScanCallback listener, BluetoothAdapter bluetoothAdapter) {
        super(listener, bluetoothAdapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void scan(boolean enable) {
        if (bluetoothAdapter != null) {
            if (enable) {
                scanning = true;
                bluetoothAdapter.startLeScan(callback);

                rx.Observable.timer(SCAN_PERIOD, TimeUnit.SECONDS).forEach(aLong -> {
                    scanning = false;
                    bluetoothAdapter.stopLeScan(callback);
                    listener.onCompleted(devices);
                });
            } else {
                scanning = false;
                bluetoothAdapter.stopLeScan(callback);
                listener.onCompleted(devices);
            }
        }
    }
}
