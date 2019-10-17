package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.Constants;

import java.util.Locale;

public class InstalacaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instalacao);

        Intent intent = getIntent();
        final double potenciaNecessaria = intent.getDoubleExtra(Constants.EXTRA_POTENCIA, 0.0);
        final String[] placaEscolhida = intent.getStringArrayExtra(Constants.EXTRA_PLACAS);
        final double area = intent.getDoubleExtra(Constants.EXTRA_AREA, 0.0);
        final String[] inversor = intent.getStringArrayExtra(Constants.EXTRA_INVERSORES);

        TextView textPotencia = findViewById(R.id.text_potencia);
        textPotencia.setText(String.format(Locale.ENGLISH,"Potência Necessária: %.2f Wp", potenciaNecessaria));

        TextView textPlaca = findViewById(R.id.text_placa);
        String singplur;
        if(Integer.parseInt(placaEscolhida[Constants.iPANEL_QTD]) > 1){
            singplur = "Placas";
        } else {
            singplur = "Placa";
        }
        textPlaca.setText(String.format(Locale.ENGLISH, "%d %s de %.0f W",
                Integer.parseInt(placaEscolhida[Constants.iPANEL_QTD]), singplur, Double.parseDouble(placaEscolhida[Constants.iPANEL_POTENCIA])));

        TextView textArea = findViewById(R.id.text_area);
        textArea.setText(String.format(Locale.ENGLISH, "%.2f m²", area));

        TextView textInversor = findViewById(R.id.text_inversor);
        if(Integer.parseInt(inversor[Constants.iINV_QTD]) > 1){
            singplur = "Inversores";
        } else {
            singplur = "Inversor";
        }
        textInversor.setText(String.format(Locale.ENGLISH, "%d %s de %.0f W",
                Integer.parseInt(inversor[Constants.iINV_QTD]), singplur, Double.parseDouble(inversor[Constants.iINV_POTENCIA])));

        Button buttonVoltar = findViewById(R.id.button_voltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
