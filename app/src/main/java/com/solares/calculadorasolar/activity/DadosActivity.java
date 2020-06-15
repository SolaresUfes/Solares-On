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
        final String[] cityVec = intent.getStringArrayExtra(Constants.EXTRA_VETOR_CIDADE);
        final String cityName = intent.getStringExtra(Constants.EXTRA_CIDADE);

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensionsAndSetTariff(this, tarifaMensal);

        //Configurar o título
        TextView textTituloDados = findViewById(R.id.text_titulo_dados);
        AutoSizeText.AutoSizeTextView(textTituloDados, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        ////////////Configurar o textView de consumo em reais////////////////
        //Pega a view que não muda (diz qual é a informação) e ajusta o tamanho da fonte
        TextView textEstaticoCustoReais = findViewById(R.id.text_consumo_reais_1);
        AutoSizeText.AutoSizeTextView(textEstaticoCustoReais, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        //Pega a view que será usada para escrever a informação, escreve a informação e ajusta o tamanho da fonte
        TextView textCustoReais = findViewById(R.id.text_consumo_reais);
        textCustoReais.setText(String.format(Locale.ITALY,"R$ %.2f", custoReais));
        AutoSizeText.AutoSizeTextView(textCustoReais, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        ////////////Configurar o textView de consumo em kWh////////////////
        //Pega a view que não muda (diz qual é a informação) e ajusta o tamanho da fonte
        TextView textEstaticoConsumoEnergia = findViewById(R.id.text_consumo_energia_1);
        AutoSizeText.AutoSizeTextView(textEstaticoConsumoEnergia, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        //Pega a view que será usada para escrever a informação, escreve a informação e ajusta o tamanho da fonte
        TextView textConsumoEnergia = findViewById(R.id.text_consumo_energia);
        textConsumoEnergia.setText(String.format(Locale.ITALY, "%.2f kWh", consumokWh));
        AutoSizeText.AutoSizeTextView(textConsumoEnergia, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        ////////////Configurar o textView de horas de sol pleno////////////////
        //Pega a view que não muda (diz qual é a informação) e ajusta o tamanho da fonte
        TextView textEstaticoHoraSolar = findViewById(R.id.text_hora_solar_1);
        AutoSizeText.AutoSizeTextView(textEstaticoHoraSolar, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        //Pega a view que será usada para escrever a informação, escreve a informação e ajusta o tamanho da fonte
        TextView textHoraSolar = findViewById(R.id.text_hora_solar);
        textHoraSolar.setText(String.format(Locale.ITALY, "%.2f kWh/m²dia", horaSolar));
        AutoSizeText.AutoSizeTextView(textHoraSolar, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        ////////////Configurar o textView da tarifa de energia////////////////
        //Pega a view que não muda (diz qual é a informação) e ajusta o tamanho da fonte
        TextView textEstaticoTarifa = findViewById(R.id.text_tarifa_1);
        AutoSizeText.AutoSizeTextView(textEstaticoTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        //Pega a view que será usada para escrever a informação, escreve a informação e ajusta o tamanho da fonte
        TextView textTarifa = findViewById(R.id.text_tarifa);
        textTarifa.setText(String.format(Locale.ITALY, "R$ %.2f / kWh", tarifaMensal));
        AutoSizeText.AutoSizeTextView(textTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        ////////////Configurar o botão de modificar a tarifa////////////////
        Button buttonTarifa = findViewById(R.id.button_modificar_tarifa);
        AutoSizeText.AutoSizeButton(buttonTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent - 0.5f);

        ////////////Configurar o botão de modificar o consumo////////////////
        Button buttonConsumo = findViewById(R.id.button_modificar_consumo);
        AutoSizeText.AutoSizeButton(buttonConsumo, MainActivity.alturaTela, MainActivity.larguraTela, porcent - 0.5f);


        ////////////Configurar o botão de voltar////////////////
        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        //Listener do botão modificar a tarifa, se ele for clicado, abre uma activity
        buttonTarifa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirActivityTarifa(cityVec, cityName, custoReais, tarifaMensal);
            }
        });

        //Listener do botão para modificar o consumo, se ele for clicado, abre uma activity
        buttonConsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirActivityConsumo(cityVec, cityName, custoReais, consumokWh);
            }
        });

        //Listener do botão voltar, se ele for clicado, realiza a ação de voltar pra última activity
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void AbrirActivityTarifa(String[] cityVec, String cityName, double custoReais, double tarifaMensal){
        Intent intent = new Intent(this, TarifaActivity.class);
        intent.putExtra(Constants.EXTRA_VETOR_CIDADE, cityVec);
        intent.putExtra(Constants.EXTRA_CIDADE, cityName);
        intent.putExtra(Constants.EXTRA_CUSTO_REAIS, custoReais);
        intent.putExtra(Constants.EXTRA_TARIFA, tarifaMensal);
        startActivity(intent);
    }

    public void AbrirActivityConsumo(String[] cityVec, String cityName, double custoReais, double consumokWh){
        Intent intent = new Intent(this, ConsumoActivity.class);
        intent.putExtra(Constants.EXTRA_VETOR_CIDADE, cityVec);
        intent.putExtra(Constants.EXTRA_CIDADE, cityName);
        intent.putExtra(Constants.EXTRA_CUSTO_REAIS, custoReais);
        intent.putExtra(Constants.EXTRA_CONSUMO, consumokWh);
        startActivity(intent);
    }
}
