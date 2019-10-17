package com.solares.calculadorasolar.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.fileWriter;

import java.io.File;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public static File file;
    public boolean foiInicializado = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "Espaço de armazenamento não encontrado!", Toast.LENGTH_LONG).show();
        } else {
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileDir = baseDir + "/SolaresCalc";
            File dir = new File(fileDir);
            dir.mkdirs();
            file = new File(fileDir + "/dadosEmails.txt");
        }


        Intent intent = new Intent(this, CalculoActivity.class);
        foiInicializado = true;
        startActivity(intent);

    }

    protected void onResume () {
        super.onResume();
        if(foiInicializado){
            finish();;
        }
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    Constants.PERMISSIONS_STORAGE,
                    Constants.REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    public boolean isExternalStorageWritable () {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
