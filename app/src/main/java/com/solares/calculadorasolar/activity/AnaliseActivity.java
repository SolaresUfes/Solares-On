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

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensionsAndSetTariff;
import static com.solares.calculadorasolar.activity.MainActivity.PtarifaPassada;

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

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensionsAndSetTariff(this, PtarifaPassada);

        TextView textResultado = findViewById(R.id.text_resultado_analise);
        AutoSizeText.AutoSizeTextView(textResultado, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        // Essas variáveis são as referências para os textos que aparecem no layout.
        TextView textEstaticoCustoParcial = findViewById(R.id.text_custo_parcial_1);
        TextView textCustoParcial = findViewById(R.id.text_custo_parcial);
        textCustoParcial.setText(String.format(Locale.ITALY,"R$ %.2f", custoParcial));
        AutoSizeText.AutoSizeTextView(textEstaticoCustoParcial, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        AutoSizeText.AutoSizeTextView(textCustoParcial, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textEstaticoCustoTotal = findViewById(R.id.text_custo_total_1);
        TextView textCustoTotal = findViewById(R.id.text_custo_total);
        textCustoTotal.setText(String.format(Locale.ITALY,"R$ %.2f", custoTotal));
        AutoSizeText.AutoSizeTextView(textEstaticoCustoTotal, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        AutoSizeText.AutoSizeTextView(textCustoTotal, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textEstaticoGeracao = findViewById(R.id.text_geracao_anual_1);
        AutoSizeText.AutoSizeTextView(textEstaticoGeracao, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textGeracao = findViewById(R.id.text_geracao_anual);
        textGeracao.setText(String.format(Locale.ITALY,"%.2f kWh", geracaoAnual));
        AutoSizeText.AutoSizeTextView(textGeracao, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
