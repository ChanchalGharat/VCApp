package com.example.majorproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.content.Context;
import android.net.wifi.WifiManager;


import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    private static final int REQUEST_SPEECH_RECOGNITION = 1001;
    private static final String[] RECORD_AUDIO_PERMISSION = {Manifest.permission.RECORD_AUDIO};
    private static final String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CHANGE_WIFI_STATE};

    private SpeechRecognizer speechRecognizer;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        // Request microphone and wifi control permissions
        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_RECORD_AUDIO_PERMISSION);

        // Get WifiManager instance
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Set up button click listener
        Button recordButton = findViewById(R.id.button);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request permission before starting speech recognition
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, RECORD_AUDIO_PERMISSION, REQUEST_RECORD_AUDIO_PERMISSION);
                    return; // Don't proceed if permission not granted
                }
                startSpeechRecognition();
            }
        });
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, REQUEST_SPEECH_RECOGNITION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with speech recognition
                // i do this startSpeechRecognition();
            } else {
                Toast.makeText(this, "Microphone permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH_RECOGNITION) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                // Process the speech recognition result here
                if (!result.isEmpty()) {
                    String recognizedSpeech = result.get(0);
                    Toast.makeText(this, "Recognized speech: " + recognizedSpeech,  Toast.LENGTH_SHORT).show();
                    processVoiceCommand(recognizedSpeech);
                } else {
                    Toast.makeText(this, "No speech recognized", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Speech recognition was not successful or was canceled
                Toast.makeText(this, "Speech recognition failed or canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void processVoiceCommand(String spokenText) {
        spokenText = spokenText.toLowerCase();

        if (spokenText.contains("open camera")) openCamera();
        else if (spokenText.contains("turn wifi on") || spokenText.contains("enable wifi") || spokenText.contains("connect to wifi")) {
            turnWifiOn();
        } else if (spokenText.contains("turn wifi off") || spokenText.contains("disable wifi")) {
            turnWifiOff();
        } else if (spokenText.contains("close cemera")) {
            closeCemera();
        }else if (spokenText.contains("open website") || spokenText.contains("can you open the website") || spokenText.contains("website open")) {
            openWebsite();
        }else if (spokenText.contains("send email") || spokenText.contains("enable email") || spokenText.contains("i want to send email")) {
            sendEmail();
        } else if (spokenText.contains("show map") || spokenText.contains("enable map") || spokenText.contains("connect to map")) {
            showMap();
        } else if (spokenText.contains("open Calculator") || spokenText.contains("launch calculator")) {
            openCalculator();

        } else {
            Toast.makeText(this, "Unknown command", Toast.LENGTH_SHORT).show();
        }
    }

    private void turnWifiOn() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(true);
            Toast.makeText(this, "WiFi turned on", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to turn on WiFi", Toast.LENGTH_SHORT).show();
        }
    }


    private void turnWifiOff() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(false);
            Toast.makeText(this, "WiFi turned off", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to turn off WiFi", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera() {
        // Check if camera app exists (optional)
        if (isCameraAppAvailable()) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(cameraIntent);
        } else {
            Toast.makeText(this, "Camera app not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeCemera() {
        // Check if camera app exists (optional)
        if (isCameraAppAvailable()) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(cameraIntent);
        } else {
            Toast.makeText(this, "Camera app not found", Toast.LENGTH_SHORT).show();
        }
    }
    private void openWebsite() {
        String url = "https://gecraipur.ac.in/"; // Replace with your website URL
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:example@example.com")); // Replace with recipient email
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject"); // Replace with email subject (optional)
        intent.putExtra(Intent.EXTRA_TEXT, "Message body"); // Replace with email body (optional)
        startActivity(intent);
    }


    private void showMap() {
        String location = "geo:0,0?q=latitude,longitude(label)"; // Replace latitude, longitude, and label
        Uri mapUri = Uri.parse(location);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void openCalculator() {
        // Create an intent to launch the calculator app
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_CALCULATOR);

        // Check if there's any activity that can handle this intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Calculator app not found", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean isCameraAppAvailable() {
        final PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

}


