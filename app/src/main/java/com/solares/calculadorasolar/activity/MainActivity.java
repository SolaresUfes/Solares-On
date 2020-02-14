package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.solares.calculadorasolar.R;


public class MainActivity extends AppCompatActivity {

    public boolean foiInicializado = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, CalculoActivity.class);
        foiInicializado = true;
        startActivity(intent);

    }

    protected void onResume () {
        super.onResume();
        if(foiInicializado){
            finish();
        }
    }
}
