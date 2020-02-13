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

public class AnaliseActivity extends AppCompatActivity {

    public final float porcent = 3f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analise);

        Intent intent = getIntent();
        final double custoParcial = intent.getDoubleExtra(Constants.EXTRA_CUSTO_PARCIAL, 0.0);
        final double custoTotal = intent.getDoubleExtra(Constants.EXTRA_CUSTO_TOTAL, 0.0);
        final double geracaoAnual = intent.getDoubleExtra(Constants.EXTRA_GERACAO, 0.0);

        TextView textResultado = findViewById(R.id.text_resultado_analise);
        AutoSizeText.AutoSizeTextView(textResultado, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

        TextView textEstaticoCustoParcial = findViewById(R.id.text_custo_parcial_1);
        TextView textCustoParcial = findViewById(R.id.text_custo_parcial);
        textCustoParcial.setText(String.format(Locale.ITALY,"R$ %.2f", custoParcial));
        AutoSizeText.AutoSizeTextView(textEstaticoCustoParcial, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        AutoSizeText.AutoSizeTextView(textCustoParcial, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        TextView textEstaticoCustoTotal = findViewById(R.id.text_custo_total_1);
        TextView textCustoTotal = findViewById(R.id.text_custo_total);
        textCustoTotal.setText(String.format(Locale.ITALY,"R$ %.2f", custoTotal));
        AutoSizeText.AutoSizeTextView(textEstaticoCustoTotal, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        AutoSizeText.AutoSizeTextView(textCustoTotal, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        TextView textEstaticoGeracao = findViewById(R.id.text_geracao_anual_1);
        AutoSizeText.AutoSizeTextView(textEstaticoGeracao, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        TextView textGeracao = findViewById(R.id.text_geracao_anual);
        textGeracao.setText(String.format(Locale.ENGLISH,"%.2f kWh", geracaoAnual));
        AutoSizeText.AutoSizeTextView(textGeracao, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

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
