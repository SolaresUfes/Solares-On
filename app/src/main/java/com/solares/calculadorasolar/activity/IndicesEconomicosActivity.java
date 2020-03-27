package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.Constants;

import java.util.Locale;

public class IndicesEconomicosActivity extends AppCompatActivity {

    public float porcent = 3f;
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

        TextView textTituloIndices = findViewById(R.id.text_titulo_indices);
        AutoSizeText.AutoSizeTextView(textTituloIndices, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

        TextView textEstaticoLucro = findViewById(R.id.text_lucro_1);
        AutoSizeText.AutoSizeTextView(textEstaticoLucro, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        TextView textLucro = findViewById(R.id.text_lucro);
        textLucro.setText(String.format(Locale.ITALY,"R$ %.2f", lucro));
        AutoSizeText.AutoSizeTextView(textLucro, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        TextView textEstaticoTaxaRetorno = findViewById(R.id.text_taxa_retorno_1);
        AutoSizeText.AutoSizeTextView(textEstaticoTaxaRetorno, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        TextView textTaxaRetorno = findViewById(R.id.text_taxa_retorno);
        textTaxaRetorno.setText(String.format(Locale.ITALY, "%.2f%%", taxaRetornoInvestimento));
        AutoSizeText.AutoSizeTextView(textTaxaRetorno, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        TextView textEstaticoIndiceLucratividade = findViewById(R.id.text_indice_lucratividade_1);
        AutoSizeText.AutoSizeTextView(textEstaticoIndiceLucratividade, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        TextView textIndiceLucratividade = findViewById(R.id.text_indice_lucratividade);
        textIndiceLucratividade.setText(String.format(Locale.ITALY, "R$ %.2f", indiceLucratividade));
        AutoSizeText.AutoSizeTextView(textIndiceLucratividade, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        TextView textEstaticoLCOE = findViewById(R.id.text_LCOE_1);
        AutoSizeText.AutoSizeTextView(textEstaticoLCOE, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        TextView textLCOE = findViewById(R.id.text_LCOE);
        textLCOE.setText(String.format(Locale.ITALY, "R$ %.2f", LCOE));
        AutoSizeText.AutoSizeTextView(textLCOE, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        TextView textEstaticoTempo = findViewById(R.id.text_tempo_retorno_1);
        AutoSizeText.AutoSizeTextView(textEstaticoTempo, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        TextView textTempo = findViewById(R.id.text_tempo_retorno);
        AutoSizeText.AutoSizeTextView(textTempo, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        if(tempoRetorno>25){
            textTempo.setText("Nunca");
        } else {
            textTempo.setText(String.format(Locale.ITALY, "%d anos", tempoRetorno));
        }

        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
