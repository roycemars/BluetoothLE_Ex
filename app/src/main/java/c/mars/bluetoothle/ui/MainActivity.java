package c.mars.bluetoothle.ui;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import c.mars.bluetoothle.R;
import c.mars.bluetoothle.helpers.BleHelperMarshmallow;
import c.mars.bluetoothle.helpers.LocationHelper;
import c.mars.bluetoothle.helpers.PermissionsHelper;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final int LOCATION_REQUEST_CODE = 1001;
    @Bind(R.id.scan)
    Button scan;
    @Bind(R.id.text)
    TextView text;
    boolean enable = true;
    private BleHelperMarshmallow bleHelper;
    private LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        bleHelper = new BleHelperMarshmallow(this, devices -> runOnUiThread(() -> {
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

        locationHelper = new LocationHelper(() -> {
            doScan();
        }, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationHelper.start();
    }

    @Override
    protected void onStop() {
        locationHelper.stop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.resume();
    }

    @OnClick(R.id.scan)
    void scan() {

        locationHelper.checkLocationEnabled();
    }

    private void doScan() {
        bleHelper.scan(enable);

        enable = !enable;
        scan.setText(enable ? getString(R.string.scan) : getString(R.string.stop));
    }

    @OnClick(R.id.bt)
    void bt() {
        PermissionsHelper.checkPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, "Need Location permission");
    }

    @OnClick(R.id.bt_admin)
    void btAdmin() {
        PermissionsHelper.checkPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, "Need Bluetooth Admin permission");
    }

    @OnClick(R.id.location)
    void location() {
        Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(enableLocationIntent, LOCATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationHelper.onActivityResult(requestCode, resultCode, data);
        bleHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        bleHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
