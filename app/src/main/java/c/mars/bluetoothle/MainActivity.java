package c.mars.bluetoothle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import factory.BleHelpersFactory;
import helpers.ble.base.BleInitializer;
import helpers.ble.base.BleScanner;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

//    public static final int LOCATION_REQUEST_CODE = 1001;

    @Bind(R.id.scan)
    Button scan;
    @Bind(R.id.text)
    TextView text;
    boolean enable = true;
    private BleInitializer bleInitializer;
    private BleScanner bleScanner;
    private boolean initialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        bleInitializer = BleHelpersFactory.getInitializer(this, this::startScan);
    }

    private void startScan(BluetoothAdapter bluetoothAdapter) {
        scan.setText(enable ? getString(R.string.scan) : getString(R.string.stop));

        bleScanner = BleHelpersFactory.getScanner(devices -> {

            runOnUiThread(() -> {
                String s = String.format(Locale.getDefault(), getString(R.string.scan_completed), devices.keySet().size());
                Timber.d(s);
                text.setText(s);
                for (BluetoothDevice device : devices.keySet()) {
                    s = s + "\n" + device.getAddress() + " (" + device.getType() + ")\n";
                    Timber.d(s);
                    text.setText(s);
                }
                scan.setText(getString(R.string.scan));
            });

        }, bluetoothAdapter);

        bleScanner.scan(enable);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bleInitializer.onStart();
    }

    @Override
    protected void onStop() {
        bleInitializer.onStop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bleInitializer.onResume();
    }

    @OnClick(R.id.scan)
    void scan() {
        if (initialized) {
            bleScanner.scan(enable);
            enable = !enable;
            scan.setText(enable ? getString(R.string.scan) : getString(R.string.stop));
        } else {
            bleInitializer.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bleInitializer.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        bleInitializer.onRequestPermissionsResult(requestCode, permissions, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
