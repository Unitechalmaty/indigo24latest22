package com.example.indigo24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class barcodeScanner extends AppCompatActivity  implements ZXingScannerView.ResultHandler  {


    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!checkPermission()) {
                requestPermissions();

            }
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(!cameraAccepted){
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("Для использования QR сканера вы должный разрешить использование камеры на вашем устройстве!", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i){
                                        requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        scannerView.stopCamera();
        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");
        String position = intent.getStringExtra("index");
        String scanResult = "none";
        intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("position", position);
        intent.putExtra("qrCode", scanResult);

        setResult(RESULT_OK, intent);
    }


    @Override
    public void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                    scannerView.setResultHandler(this);
                    scannerView.startCamera();


                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else{
                requestPermissions();
            }
        }
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(barcodeScanner.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Отмена", null)
                .create()
                .show();

    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(barcodeScanner.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    public boolean onSupportNavigateUp () {
        finish();
        return true;
    }



    @Override
    public void handleResult(Result result) {
        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");
        String position = intent.getStringExtra("index");
        String scanResult = result.getText();
        intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("position", position);
        intent.putExtra("qrCode", scanResult);

        setResult(RESULT_OK, intent);
        finish();
    }
}
