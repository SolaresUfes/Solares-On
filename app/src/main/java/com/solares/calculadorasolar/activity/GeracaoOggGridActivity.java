package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.Constants;

import java.util.Locale;

public class GeracaoOggGridActivity extends AppCompatActivity {

    float porcent = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geracao_ogg_grid);

        //Pegar informações da última activity (informações que serão exibidas)
        Intent intent = getIntent();
        final CalculadoraOffGrid calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);

        TextView textTituloGeracao = findViewById(R.id.text_geracao);
        AutoSizeText.AutoSizeTextView(textTituloGeracao, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        try{

            // ## Estimativa da energia diária gerada ##
            TextView textEnergiaDiaria = findViewById(R.id.text_energia_diaria);
            AutoSizeText.AutoSizeTextView(textEnergiaDiaria, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            TextView textEnergiaDiariaValor = findViewById(R.id.text_energia_diaria_valor);
            textEnergiaDiariaValor.setText(String.format(Locale.ITALY,"%.2f kWh", calculadora.pegaGeracaoDiario()));
            AutoSizeText.AutoSizeTextView(textEnergiaDiariaValor, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            // ## Autonomia média ##
            TextView textautonomia = findViewById(R.id.text_autonomia);
            AutoSizeText.AutoSizeTextView(textautonomia, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            TextView textautonomiaValor = findViewById(R.id.text_autonomia_valor);
            textautonomiaValor.setText(String.format(Locale.ITALY,"%d dias", calculadora.pegaAutonomia()));
            AutoSizeText.AutoSizeTextView(textautonomiaValor, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            Button button_voltar = findViewById(R.id.button_voltar);
            AutoSizeText.AutoSizeButton(button_voltar, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            button_voltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}