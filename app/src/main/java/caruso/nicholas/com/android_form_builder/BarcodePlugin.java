package caruso.nicholas.com.android_form_builder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.HashMap;

import corps.technology.diversified.workorder.BarcodeCaptureActivity;
import corps.technology.diversified.workorder.BarcodeScannerActivity;
import corps.technology.diversified.workorder.MyApplication;
import corps.technology.diversified.workorder.R;

/**
 * Nick:3/1/2018
 * WorkOrder.
 */

public class BarcodePlugin {
    public static final int RC_BARCODE_CAPTURE = 91;
    public static final int RC_BARCODE_SCAN = 92;
    public static final int RC_BARCODE_TOOL_CHANGE = 93;

    public Fragment fragment;
    private Context context;
    public Listener listener;
    private HashMap<String, Integer> barcode_map;
    private SharedPreferences sharedPreferences;
    private int current_barcode_tool = RC_BARCODE_CAPTURE;
    public static final String BARCODE_TOOL_PREFERENCE = "barcode_tool_preference";
    public static final String CURRENT_BARCODE_TOOL_PREF = "current_barcode_tool_preference";


    public interface Listener {
        void barcodeResult(BarcodeObject data);

        HashMap<String, Integer> setBarcodeMap();
    }

    public static boolean isBarcodeRequest(int requestCode) {
        return requestCode == RC_BARCODE_CAPTURE || requestCode == RC_BARCODE_SCAN || requestCode == RC_BARCODE_TOOL_CHANGE;
    }

    public static BarcodePlugin create(@NonNull Fragment fragment) {
        Listener listener;
        try {
            listener = (Listener) fragment;
        } catch (ClassCastException e) {
            listener = new Listener() {
                @Override
                public void barcodeResult(BarcodeObject data) {

                }

                @Override
                public HashMap<String, Integer> setBarcodeMap() {
                    return null;
                }
            };
        }
        return new BarcodePlugin(fragment, listener);

    }


    private BarcodePlugin(Fragment fragment, @NonNull Listener listener) {
        this.fragment = fragment;
        this.context = fragment.getContext();
        this.listener = listener;
        this.barcode_map = listener.setBarcodeMap();
        sharedPreferences = context.getSharedPreferences(BARCODE_TOOL_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(CURRENT_BARCODE_TOOL_PREF)) {
            current_barcode_tool = sharedPreferences.getInt(CURRENT_BARCODE_TOOL_PREF, current_barcode_tool);
        }
    }


    public void makeBarcodeButton(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBarcodeActivity();
            }
        });
    }

    private void startBarcodeActivity() {
        if (current_barcode_tool == RC_BARCODE_CAPTURE) {
            Intent intent = new Intent(fragment.getContext(), BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.RADIO_EXTRA, barcode_map);
            fragment.startActivityForResult(intent, RC_BARCODE_CAPTURE);
        } else {
            Intent intent = new Intent(fragment.getContext(), BarcodeScannerActivity.class);
            intent.putExtra(BarcodeCaptureActivity.RADIO_EXTRA, barcode_map);
            fragment.startActivityForResult(intent, RC_BARCODE_SCAN);
        }
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CommonStatusCodes.SUCCESS) {
            if (requestCode == RC_BARCODE_SCAN) {
                resultScanner(data);
            }
            if (requestCode == RC_BARCODE_CAPTURE) {
                resultCapture(data);
            }
        } else if (resultCode == CommonStatusCodes.INTERRUPTED) {
            switchBarcodeTool();
            startBarcodeActivity();
        } else {
            barcodeErrorDialog(resultCode).show();
        }
    }

    private void resultCapture(Intent data) {
        if (data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
            int radio_id = data.getIntExtra(BarcodeCaptureActivity.RadioChecked, -1);
            listener.barcodeResult(new BarcodeObject(barcode, radio_id));
        }
    }

    private void resultScanner(Intent data) {

        if (data != null) {
            String barcode = data.getStringExtra(BarcodeScannerActivity.Barcode_String);
            int radio_id = data.getIntExtra(BarcodeCaptureActivity.RadioChecked, -1);
            listener.barcodeResult(new BarcodeObject(barcode, radio_id));
        }
    }


    private AlertDialog barcodeErrorDialog(int resultCode) {
        String result = String.format(fragment.getString(R.string.barcode_error),
                CommonStatusCodes.getStatusCodeString(resultCode));
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
        builder.setIcon(R.drawable.ic_alert);
        builder.setTitle("Barcode Error");
        builder.setMessage(result);
        builder.setNeutralButton(R.string.okey, (dialogInterface, i) -> {

        });
        return MyApplication.colorDialog(builder.create(), fragment.getResources());

    }

    public void switchBarcodeTool() {

        if (current_barcode_tool == RC_BARCODE_CAPTURE) {
            current_barcode_tool = RC_BARCODE_SCAN;
        } else {
            current_barcode_tool = RC_BARCODE_CAPTURE;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CURRENT_BARCODE_TOOL_PREF);
        editor.putInt(CURRENT_BARCODE_TOOL_PREF, current_barcode_tool);
        editor.apply();
    }

    public final class BarcodeObject {
        public String barcode;
        public int radio;

        BarcodeObject(Barcode barcode, int rad) {
            this.barcode = barcode.displayValue;
            radio = rad;
        }

        BarcodeObject(String barcodeString, int rad) {
            this.barcode = barcodeString;
            radio = rad;

        }
    }
}
