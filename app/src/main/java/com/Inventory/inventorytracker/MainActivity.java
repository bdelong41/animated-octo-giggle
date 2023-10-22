package com.Inventory.inventorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.widget.Toast;

import com.Inventory.inventorytracker.DataBase.DBHandler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private EditText qrCodeTxt;
    private EditText boxName;
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private DBHandler dbHandler;
    private Button addBox;
    private Integer boxID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        qrCodeTxt = findViewById(R.id.qrCideTxt);
        boxName = findViewById(R.id.boxName);
        addBox = findViewById(R.id.addBox);
        previewView = findViewById(R.id.cameraPreview);
        dbHandler = new DBHandler(MainActivity.this);
        dbHandler.createBox(30);
        //adding click listener to button
        addBox.setOnClickListener((view) ->{
            if(boxID != null) createBox(boxID);
        });
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            init();
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    private void init(){
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(MainActivity.this);
        cameraProviderListenableFuture.addListener(new Runnable(){
            @Override
            public void run(){
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                    bindImageAnalysis(cameraProvider);
                }
                catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(MainActivity.this));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            init();
        }
        else{
            Toast.makeText(MainActivity.this, "Permissions Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindImageAnalysis(ProcessCameraProvider processCameraProvider){
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720)).
                setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(MainActivity.this), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {

                Image mediaImage = image.getImage();

                if(mediaImage != null){
                    InputImage image2 = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());

                    BarcodeScanner scanner = BarcodeScanning.getClient();

                    Task<List<Barcode>> results = scanner.process(image2);

                    results.addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {

                            for(Barcode barcode: barcodes){
                                final String getValue = barcode.getRawValue();

                                try{
                                    //try casting barcode data to integer
                                    boxID = Integer.parseInt(getValue);
                                    boxName.setText(dbHandler.getData(boxID));
                                }catch(Exception e){
                                    qrCodeTxt.setText("Failed to parse barcode as integer");
                                    e.printStackTrace();
                                }

                                qrCodeTxt.setText(getValue);
                            }

                            image.close();
                            mediaImage.close();
                        }
                    });
                }
            }
        });

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider((previewView.getSurfaceProvider()));
        processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }


    public void createBox(Integer boxID){
        dbHandler.createBox(boxID);
        updateBox(boxID, "Sample data,");

    }
    public void updateBox(Integer boxID, String data){
        dbHandler.updateData(boxID, data, "Owner");
//        boxName.setText(Integer.toString(dbHandler.updateData(boxID, data, "Owner")));
        getBox(boxID);
//        getBox(boxID);

    }
    public void getBox(Integer boxID){
        boxName.setText(dbHandler.getData(boxID));
    }
}