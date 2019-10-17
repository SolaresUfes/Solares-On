package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.Constants;

import java.util.Locale;

public class AnaliseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analise);

        Intent intent = getIntent();
        final double custoParcial = intent.getDoubleExtra(Constants.EXTRA_CUSTO_PARCIAL, 0.0);
        final double custoTotal = intent.getDoubleExtra(Constants.EXTRA_CUSTO_TOTAL, 0.0);
        final double geracaoAnual = intent.getDoubleExtra(Constants.EXTRA_GERACAO, 0.0);

        TextView textCustoParcial = findViewById(R.id.text_custo_parcial);
        textCustoParcial.setText(String.format(Locale.ITALY,"R$ %.2f", custoParcial));

        TextView textCustoTotal = findViewById(R.id.text_custo_total);
        textCustoTotal.setText(String.format(Locale.ITALY,"R$ %.2f", custoTotal));

        TextView textGeracao = findViewById(R.id.text_geracao_anual);
        textGeracao.setText(String.format(Locale.ENGLISH,"Estimativa da geração anual de energia: %.2f kWh", geracaoAnual));

        Button buttonVoltar = findViewById(R.id.button_voltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
