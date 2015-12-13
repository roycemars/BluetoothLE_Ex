package c.mars.bluetoothle.factory;

import android.app.Activity;
import android.os.Build;

import c.mars.bluetoothle.helpers.BleHelperJellyBean;
import c.mars.bluetoothle.helpers.BleHelperLollipop;
import c.mars.bluetoothle.helpers.BleHelper;

/**
 * Created by Constantine Mars on 12/13/15.
 */
public class BleHelperFactoryLollipop {
    public static BleHelper getHelper(Activity activity, BleHelper.ScanListener listener) {
        final int osVersion=Build.VERSION.SDK_INT;
        if(osVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2 && osVersion < Build.VERSION_CODES.LOLLIPOP) {
            return new BleHelperJellyBean(activity, listener);
        } else if(osVersion > Build.VERSION_CODES.LOLLIPOP) {
            return new BleHelperLollipop(activity, listener);
        }

        return null;
    }
}
