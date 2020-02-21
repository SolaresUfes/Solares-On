package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.solares.calculadorasolar.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreditoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credito);

        Button buttonIntagram = findViewById(R.id.button_instagram);


        buttonIntagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/projeto.solares/"));
                startActivity(intent);
            }
        });
    }
}
