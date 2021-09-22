package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.Constants;

import java.util.Locale;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

public class IndicesEconomicosActivity extends AppCompatActivity {

    public float porcent = 3f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indices_economicos);

        Intent intent = getIntent();
        final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);


        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensions(this);

        TextView textTituloIndices = findViewById(R.id.text_titulo_indices);
        AutoSizeText.AutoSizeTextView(textTituloIndices, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        TextView textEstaticoLucro = findViewById(R.id.text_lucro_1);
        AutoSizeText.AutoSizeTextView(textEstaticoLucro, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textLucro = findViewById(R.id.text_lucro);
        textLucro.setText(String.format(Locale.ITALY,"R$ %.2f", calculadora.pegaLucro()));
        AutoSizeText.AutoSizeTextView(textLucro, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textEstaticoTaxaRetorno = findViewById(R.id.text_taxa_retorno_1);
        AutoSizeText.AutoSizeTextView(textEstaticoTaxaRetorno, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textTaxaRetorno = findViewById(R.id.text_taxa_retorno);
        textTaxaRetorno.setText(String.format(Locale.ITALY, "%.2f%%", calculadora.pegaTaxaInternaRetorno()));
        AutoSizeText.AutoSizeTextView(textTaxaRetorno, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textEstaticoEconomiaMensal = findViewById(R.id.text_economia_mensal_1);
        AutoSizeText.AutoSizeTextView(textEstaticoEconomiaMensal, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textEconomiaMensal = findViewById(R.id.text_economia_mensal);
        textEconomiaMensal.setText(String.format(Locale.ITALY, "R$ %.2f", calculadora.pegaEconomiaMensal()));
        AutoSizeText.AutoSizeTextView(textEconomiaMensal, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textEstaticoLCOE = findViewById(R.id.text_LCOE_1);
        AutoSizeText.AutoSizeTextView(textEstaticoLCOE, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textLCOE = findViewById(R.id.text_LCOE);
        textLCOE.setText(String.format(Locale.ITALY, "R$ %.2f / kWh", calculadora.pegaLCOE()));
        AutoSizeText.AutoSizeTextView(textLCOE, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textEstaticoTempo = findViewById(R.id.text_tempo_retorno_1);
        AutoSizeText.AutoSizeTextView(textEstaticoTempo, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        TextView textTempo = findViewById(R.id.text_tempo_retorno);
        AutoSizeText.AutoSizeTextView(textTempo, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
        if(calculadora.pegaTempoRetorno()>25){
            textTempo.setText("Nunca");
        } else {
            textTempo.setText(String.format(Locale.ITALY, "%d anos", calculadora.pegaTempoRetorno()));
        }

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
