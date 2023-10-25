package com.Inventory.inventorytracker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import android.Manifest;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;

    private Context context;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        previewView = getActivity().findViewById(R.id.cameraPreview);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            init();
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 101);
        }

    }

    private void init(){
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(context);
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
        }, ContextCompat.getMainExecutor(context));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            init();
        }
        else{
            Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindImageAnalysis(ProcessCameraProvider processCameraProvider){
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720)).
                setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), new ImageAnalysis.Analyzer() {
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
                                }catch(Exception e){
                                    e.printStackTrace();
                                }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

}