package c.mars.bluetoothle;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import timber.log.Timber;

/**
 * Created by Constantine Mars on 12/6/15.
 */
@Data @RequiredArgsConstructor
public class BluetoothLeHelper {
    private final Activity activity;
    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 3001;

    private static BluetoothLeHelper instance;
    public static BluetoothLeHelper getInstance(Activity activity) {
        instance=new BluetoothLeHelper(activity);
        instance.init();
        return instance;
    }

    public void init(){
        final BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        checkBluetoothState();
    }

    private boolean checkBluetoothState() {
        if (bluetoothAdapter == null) {
            Timber.i("Bluetooth is not supported");
            return false;
        }
        if(!bluetoothAdapter.isEnabled()) {
            Timber.i("Bluetooth is disabled. Enabling...");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }
        Timber.i("Bluetooth enabled");
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                checkBluetoothState();
                break;
        }
    }
}
