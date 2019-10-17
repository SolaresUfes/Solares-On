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

public class DadosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados);

        Intent intent = getIntent();
        final double horaSolar = intent.getDoubleExtra(Constants.EXTRA_HORA_SOLAR, 0.0);
        final double custoReais = intent.getDoubleExtra(Constants.EXTRA_CUSTO_REAIS, 0.0);
        final double consumokWh = intent.getDoubleExtra(Constants.EXTRA_CONSUMO, 0.0);

        TextView textCustoReais = findViewById(R.id.text_consumo_reais);
        textCustoReais.setText(String.format(Locale.ITALY,"Consumo mensal de energia (em reais): R$ %.2f", custoReais));

        TextView textConsumoEnergia = findViewById(R.id.text_consumo_energia);
        textConsumoEnergia.setText(String.format(Locale.ENGLISH, "Consumo mensal de energia: %.2f kWh", consumokWh));

        TextView textHoraSolar = findViewById(R.id.text_hora_solar);
        textHoraSolar.setText(String.format(Locale.ENGLISH, "Horas de Sol Pleno da Cidade: %.2f kWh/m²dia", horaSolar));

        Button buttonVoltar = findViewById(R.id.button_voltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
