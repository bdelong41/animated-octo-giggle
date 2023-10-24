package com.Inventory.inventorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Size;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.widget.Toast;

import com.Inventory.inventorytracker.DataBase.DBHandler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;



import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //drawer code
    private DrawerLayout drawerLayout;

    private EditText qrCodeTxt;
    private EditText boxName;
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private DBHandler dbHandler;
    private Button addBox;
    private Button clearData;
    private Button UpdateBox;
    private Button GetBox;
    private Integer boxID;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //drawer code
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        setContentView(R.layout.activity_main);

        qrCodeTxt = findViewById(R.id.qrCideTxt);
        boxName = findViewById(R.id.boxName);
        addBox = findViewById(R.id.addBox);
        UpdateBox = findViewById(R.id.UpdateBox);
        GetBox = findViewById(R.id.GetBox);
        clearData = findViewById(R.id.clearDatabase);
        previewView = findViewById(R.id.cameraPreview);
        dbHandler = new DBHandler(MainActivity.this);
        dbHandler.createBox(30);
        //adding click listener to button
        addBox.setOnClickListener((view) ->{
            if(boxID != null) {
                createBox(boxID);
                updateBox(boxID, String.valueOf(boxName.getText()));
            }

        });
        clearData.setOnClickListener((view) ->{
            //dbHandler.deleteTable();
            dbHandler.seedTable();
        });
        UpdateBox.setOnClickListener((view) ->{
            if(boxID != null) updateBox(boxID, String.valueOf(boxName.getText()));
        });
        GetBox.setOnClickListener((view) ->{
            boxID = Integer.parseInt(String.valueOf(qrCodeTxt.getText()));
            getBox(boxID);
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

    //drawer code
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        item.getItemId();
        int itemId = item.getItemId();
        if(itemId == R.id.nav_home){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
        else if(itemId == R.id.nav_settings){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        }
        else if(itemId == R.id.nav_share){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
        }
        else if(itemId == R.id.nav_about){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        }
        else if(itemId == R.id.nav_logout){
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}