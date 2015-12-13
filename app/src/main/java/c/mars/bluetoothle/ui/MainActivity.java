package c.mars.bluetoothle.ui;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import c.mars.bluetoothle.factory.BleHelperFactoryLollipop;
import c.mars.bluetoothle.R;
import c.mars.bluetoothle.helpers.BleHelper;
import c.mars.bluetoothle.helpers.BleHelperMarshmallow;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.scan)
    Button scan;
    @Bind(R.id.text)
    TextView text;

    private BleHelper bleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        bleHelper = BleHelperFactoryLollipop.getHelper(this, devices -> runOnUiThread(() -> {
                String s = String.format("Scan completed. Discovered %d devices\n", bleHelper.getDevices().keySet().size());
                Timber.d(s);
                text.setText(s);
                for (BluetoothDevice device : devices.keySet()) {
                    s = device.getAddress() + " (" + device.getType() + ")\n";
                    Timber.d(s);
                    text.setText(s);
                }
                scan.setText(getString(R.string.scan));
                enable = true;
            })
        );

        BleHelperMarshmallow.checkAndRequestPermissions(this);
    }

    boolean enable = true;

    @OnClick(R.id.scan)
    void scan() {
        bleHelper.scan(enable);

        enable = !enable;
        scan.setText(enable ? getString(R.string.scan) : getString(R.string.stop));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bleHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        BleHelperMarshmallow.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
