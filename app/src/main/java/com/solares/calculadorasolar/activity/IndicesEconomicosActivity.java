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

public class IndicesEconomicosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indices_economicos);

        Intent intent = getIntent();
        final double lucro = intent.getDoubleExtra(Constants.EXTRA_LUCRO, 0.0);
        final double taxaRetornoInvestimento = intent.getDoubleExtra(Constants.EXTRA_TAXA_DE_RETORNO, 0.0);
        final double indiceLucratividade = intent.getDoubleExtra(Constants.EXTRA_INDICE_LUCRATICVIDADE, 0.0);
        final double LCOE = intent.getDoubleExtra(Constants.EXTRA_LCOE, 0.0);
        final int tempoRetorno = intent.getIntExtra(Constants.EXTRA_TEMPO_RETORNO, 0);

        TextView textLucro = findViewById(R.id.text_lucro);
        textLucro.setText(String.format(Locale.ITALY,"R$ %.2f", lucro));

        TextView textTaxaRetorno = findViewById(R.id.text_taxa_retorno);
        textTaxaRetorno.setText(String.format(Locale.ENGLISH, "Taxa de Retorno do Investimento: %.2f%%", taxaRetornoInvestimento));

        TextView textIndiceLucratividade = findViewById(R.id.text_indice_lucratividade);
        textIndiceLucratividade.setText(String.format(Locale.ITALY, "Índice de Lucratividade: R$ %.2f", indiceLucratividade));

        TextView textLCOE = findViewById(R.id.text_LCOE);
        textLCOE.setText(String.format(Locale.ITALY, "Custo da energia solar (LCOE): R$ %.2f", LCOE));

        TextView textTempo = findViewById(R.id.text_tempo_retorno);
        if(tempoRetorno>25){
            textTempo.setText("Tempo para Retorno do Investimento: Nunca");
        } else {
            textTempo.setText(String.format(Locale.ENGLISH, "Tempo para Retorno do Investimento: %d anos", tempoRetorno));
        }

        Button buttonVoltar = findViewById(R.id.button_voltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
