package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.Constants;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

public class ResultadoActivity extends AppCompatActivity{

    public float porcent = 3f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        try {

            Intent intent = getIntent();
            final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);

            //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
            //Essa função pega as dimensões e as coloca em váriaveis globais
            GetPhoneDimensions(this);

            TextView textTituloResultado = findViewById(R.id.text_titulo_resultado);
            AutoSizeText.AutoSizeTextView(textTituloResultado, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
            Button buttonDados = findViewById(R.id.button_dados);
            AutoSizeText.AutoSizeButton(buttonDados, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            Button buttonInstalacao = findViewById(R.id.button_instalacao);
            AutoSizeText.AutoSizeButton(buttonInstalacao, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            Button buttonAnalise = findViewById(R.id.button_analise);
            AutoSizeText.AutoSizeButton(buttonAnalise, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            Button buttonIndicesEconomicos = findViewById(R.id.button_indices_economicos);
            AutoSizeText.AutoSizeButton(buttonIndicesEconomicos, MainActivity.alturaTela, MainActivity.larguraTela, porcent);
            Button buttonFinalizar = findViewById(R.id.button_finalizar);
            AutoSizeText.AutoSizeButton(buttonFinalizar, MainActivity.alturaTela, MainActivity.larguraTela, 4f);
            Button buttonAjustarArea = findViewById(R.id.button_ajustar_area);
            AutoSizeText.AutoSizeButton(buttonAjustarArea, MainActivity.alturaTela, MainActivity.larguraTela, porcent);


            buttonDados.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityDados(calculadora);
                }
            });

            buttonInstalacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityInstalacao(calculadora);
                }
            });

            buttonAnalise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityAnalise(calculadora);
                }
            });

            buttonIndicesEconomicos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityIndices(calculadora);
                }
            });

            buttonAjustarArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityArea(calculadora);
                }
            });

            buttonFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FinalizarCalculo(calculadora);
                }
            });

        } catch (Exception e){
            Log.i("ResultadoActivity", "Erro na captura de intents");
        }
    }

    public void AbrirActivityDados(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, DadosActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);

        startActivity(intent);
    }

    public void AbrirActivityInstalacao(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, InstalacaoActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }

    public void AbrirActivityAnalise(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, AnaliseActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }

    public void AbrirActivityIndices(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, IndicesEconomicosActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }

    public void AbrirActivityArea(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, AreaActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }

    public void FinalizarCalculo(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, CreditoActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAON, calculadora);
        startActivity(intent);
    }
}





