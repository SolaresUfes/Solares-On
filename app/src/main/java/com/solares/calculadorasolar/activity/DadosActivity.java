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

        //Pegar informações da última activity (informações que serão exibidas)
        Intent intent = getIntent();
        final double horaSolar = intent.getDoubleExtra(Constants.EXTRA_HORA_SOLAR, 0.0);
        final double custoReais = intent.getDoubleExtra(Constants.EXTRA_CUSTO_REAIS, 0.0);
        final double consumokWh = intent.getDoubleExtra(Constants.EXTRA_CONSUMO, 0.0);
        final double tarifaMensal = intent.getDoubleExtra(Constants.EXTRA_TARIFA, 0.0);

        //Configurar o título
        TextView textTituloDados = findViewById(R.id.text_titulo_dados);
        AutoSizeText.AutoSizeTextView(textTituloDados, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

        ////////////Configurar o textView de consumo em reais////////////////
        //Pega a view que não muda (diz qual é a informação) e ajusta o tamanho da fonte
        TextView textEstaticoCustoReais = findViewById(R.id.text_consumo_reais_1);
        AutoSizeText.AutoSizeTextView(textEstaticoCustoReais, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        //Pega a view que será usada para escrever a informação, escreve a informação e ajusta o tamanho da fonte
        TextView textCustoReais = findViewById(R.id.text_consumo_reais);
        textCustoReais.setText(String.format(Locale.ITALY,"R$ %.2f", custoReais));
        AutoSizeText.AutoSizeTextView(textCustoReais, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        ////////////Configurar o textView de consumo em kWh////////////////
        //Pega a view que não muda (diz qual é a informação) e ajusta o tamanho da fonte
        TextView textEstaticoConsumoEnergia = findViewById(R.id.text_consumo_energia_1);
        AutoSizeText.AutoSizeTextView(textEstaticoConsumoEnergia, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        //Pega a view que será usada para escrever a informação, escreve a informação e ajusta o tamanho da fonte
        TextView textConsumoEnergia = findViewById(R.id.text_consumo_energia);
        textConsumoEnergia.setText(String.format(Locale.ENGLISH, "%.2f kWh", consumokWh));
        AutoSizeText.AutoSizeTextView(textConsumoEnergia, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        ////////////Configurar o textView de horas de sol pleno////////////////
        //Pega a view que não muda (diz qual é a informação) e ajusta o tamanho da fonte
        TextView textEstaticoHoraSolar = findViewById(R.id.text_hora_solar_1);
        AutoSizeText.AutoSizeTextView(textEstaticoHoraSolar, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        //Pega a view que será usada para escrever a informação, escreve a informação e ajusta o tamanho da fonte
        TextView textHoraSolar = findViewById(R.id.text_hora_solar);
        textHoraSolar.setText(String.format(Locale.ENGLISH, "%.2f kWh/m²dia", horaSolar));
        AutoSizeText.AutoSizeTextView(textHoraSolar, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        ////////////Configurar o textView da tarifa de energia////////////////
        //Pega a view que não muda (diz qual é a informação) e ajusta o tamanho da fonte
        TextView textEstaticoTarifa = findViewById(R.id.text_tarifa_1);
        AutoSizeText.AutoSizeTextView(textEstaticoTarifa, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        //Pega a view que será usada para escrever a informação, escreve a informação e ajusta o tamanho da fonte
        TextView textTarifa = findViewById(R.id.text_tarifa);
        textTarifa.setText(String.format(Locale.ITALY, "R$%.2f", tarifaMensal));
        AutoSizeText.AutoSizeTextView(textTarifa, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);


        ////////////Configurar o botão de voltar////////////////
        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

        //Listener do botão, se ele for clicado, realiza a ação de voltar pra última activity
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
