package factory;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Build;

import helpers.ble.base.BleInitializer;
import helpers.ble.base.BleInitializer.InitCompletionCallback;
import helpers.ble.base.BleScanner;
import helpers.ble.initializers.BleInitializerJ;
import helpers.ble.initializers.BleInitializerL;
import helpers.ble.initializers.BleInitializerM;
import helpers.ble.scanners.BleScannerJ;
import helpers.ble.scanners.BleScannerL;


/**
 * Created by Constantine Mars on 12/13/15.
 * Factory for creating BLE init helper according to OS version
 */
public class BleHelpersFactory {

    public static BleInitializer getInitializer(Activity activity, InitCompletionCallback initCompletionCallback) {
        final int osVersion=Build.VERSION.SDK_INT;

        if (osVersion >= Build.VERSION_CODES.M) {
            return new BleInitializerM(activity, initCompletionCallback);
        } else if (osVersion >= Build.VERSION_CODES.LOLLIPOP) {
            return new BleInitializerL(activity, initCompletionCallback);
        } else if (osVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return new BleInitializerJ(activity, initCompletionCallback);
        }

//        There is no BLE support in Android versions below Jelly Bean
        return null;
    }

    public static BleScanner getScanner(BleScanner.ScanCallback scanCallback, BluetoothAdapter bluetoothAdapter) {
        final int osVersion = Build.VERSION.SDK_INT;

        if (osVersion >= Build.VERSION_CODES.LOLLIPOP) {
            return new BleScannerL(scanCallback, bluetoothAdapter);
        } else if (osVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return new BleScannerJ(scanCallback, bluetoothAdapter);
        }

//        There is no BLE support in Android versions below Jelly Bean
        return null;
    }
}
