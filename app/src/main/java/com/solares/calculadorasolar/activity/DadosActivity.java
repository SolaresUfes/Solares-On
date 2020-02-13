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

public class DadosActivity extends AppCompatActivity {

    public float porcent = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados);

        Intent intent = getIntent();
        final double horaSolar = intent.getDoubleExtra(Constants.EXTRA_HORA_SOLAR, 0.0);
        final double custoReais = intent.getDoubleExtra(Constants.EXTRA_CUSTO_REAIS, 0.0);
        final double consumokWh = intent.getDoubleExtra(Constants.EXTRA_CONSUMO, 0.0);

        TextView textTituloDados = findViewById(R.id.text_titulo_dados);
        AutoSizeText.AutoSizeTextView(textTituloDados, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

        TextView textEstaticoCustoReais = findViewById(R.id.text_consumo_reais_1);
        AutoSizeText.AutoSizeTextView(textEstaticoCustoReais, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        TextView textCustoReais = findViewById(R.id.text_consumo_reais);
        textCustoReais.setText(String.format(Locale.ITALY,"R$ %.2f", custoReais));
        AutoSizeText.AutoSizeTextView(textCustoReais, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        TextView textEstaticoConsumoEnergia = findViewById(R.id.text_consumo_energia_1);
        AutoSizeText.AutoSizeTextView(textEstaticoConsumoEnergia, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        TextView textConsumoEnergia = findViewById(R.id.text_consumo_energia);
        textConsumoEnergia.setText(String.format(Locale.ENGLISH, "%.2f kWh", consumokWh));
        AutoSizeText.AutoSizeTextView(textConsumoEnergia, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        TextView textEstaticoHoraSolar = findViewById(R.id.text_hora_solar_1);
        AutoSizeText.AutoSizeTextView(textEstaticoHoraSolar, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        TextView textHoraSolar = findViewById(R.id.text_hora_solar);
        textHoraSolar.setText(String.format(Locale.ENGLISH, "%.2f kWh/m²dia", horaSolar));
        AutoSizeText.AutoSizeTextView(textHoraSolar, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

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
