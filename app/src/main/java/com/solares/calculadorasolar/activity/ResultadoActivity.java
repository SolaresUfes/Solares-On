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

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensionsAndSetTariff;

public class ResultadoActivity extends AppCompatActivity{

    public float porcent = 3f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        try {

            Intent intent = getIntent();
            CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);

            //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
            //Essa função pega as dimensões e as coloca em váriaveis globais
            GetPhoneDimensionsAndSetTariff(this, tarifaMensal);

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
                    AbrirActivityDados(custoReais, consumokWh, horaSolar, tarifaMensal, cityVec, NomeCidade);
                }
            });

            buttonInstalacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityInstalacao(potenciaNecessaria, placaEscolhida, area, inversor);
                }
            });

            buttonAnalise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityAnalise(custoParcial, custoTotal, geracaoAnual);
                }
            });

            buttonIndicesEconomicos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityIndices(lucro, taxaRetornoInvestimento, economiaMensal, LCOE, tempoRetorno);
                }
            });

            buttonAjustarArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbrirActivityArea(cityVec, NomeCidade, custoReais, area);
                }
            });

            buttonFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FinalizarCalculo();
                }
            });

        } catch (Exception e){
            Log.i("ResultadoActivity", "Erro na captura de intents");
        }
    }

    public void AbrirActivityDados(double custoReais, double consumokWh, double horaSolar, double tarifaMensal, String[] cityVec, String NomeCidade){
        Intent intent = new Intent(this, DadosActivity.class);
        intent.putExtra(Constants.EXTRA_HORA_SOLAR, horaSolar);
        intent.putExtra(Constants.EXTRA_CUSTO_REAIS, custoReais);
        intent.putExtra(Constants.EXTRA_CONSUMO, consumokWh);
        intent.putExtra(Constants.EXTRA_TARIFA, tarifaMensal);
        intent.putExtra(Constants.EXTRA_VETOR_CIDADE, cityVec);
        intent.putExtra(Constants.EXTRA_CIDADE, NomeCidade);
        startActivity(intent);
    }

    public void AbrirActivityInstalacao(double potenciaNecessaria, String[] placaEscolhida, double area, String[] inversor){
        Intent intent = new Intent(this, InstalacaoActivity.class);
        intent.putExtra(Constants.EXTRA_POTENCIA, potenciaNecessaria);
        intent.putExtra(Constants.EXTRA_PLACAS, placaEscolhida);
        intent.putExtra(Constants.EXTRA_AREA, area);
        intent.putExtra(Constants.EXTRA_INVERSORES, inversor);
        startActivity(intent);
    }

    public void AbrirActivityAnalise(double custoParcial, double custoTotal, double geracaoAnual){
        Intent intent = new Intent(this, AnaliseActivity.class);
        intent.putExtra(Constants.EXTRA_CUSTO_PARCIAL, custoParcial);
        intent.putExtra(Constants.EXTRA_CUSTO_TOTAL, custoTotal);
        intent.putExtra(Constants.EXTRA_GERACAO, geracaoAnual);
        startActivity(intent);
    }

    public void AbrirActivityIndices(double lucro, double taxaRetornoInvestimento, double economiaMensal, double LCOE, int tempoRetorno){
        Intent intent = new Intent(this, IndicesEconomicosActivity.class);
        intent.putExtra(Constants.EXTRA_LUCRO, lucro);
        intent.putExtra(Constants.EXTRA_TAXA_DE_RETORNO, taxaRetornoInvestimento);
        intent.putExtra(Constants.EXTRA_ECONOMIA_MENSAL, economiaMensal);
        intent.putExtra(Constants.EXTRA_LCOE, LCOE);
        intent.putExtra(Constants.EXTRA_TEMPO_RETORNO, tempoRetorno);
        startActivity(intent);
    }

    public void AbrirActivityArea(String[] cityVec, String NomeCidade, double custoReais, double area){
        Intent intent = new Intent(this, AreaActivity.class);
        intent.putExtra(Constants.EXTRA_VETOR_CIDADE, cityVec);
        intent.putExtra(Constants.EXTRA_CIDADE, NomeCidade);
        intent.putExtra(Constants.EXTRA_CUSTO_REAIS, custoReais);
        intent.putExtra(Constants.EXTRA_AREA, area);
        startActivity(intent);
    }

    public void FinalizarCalculo(){
        //Limpa tarifa inserida
        MainActivity.PtarifaPassada = 0.0;

        Intent intent = new Intent(this, CreditoActivity.class);
        startActivity(intent);
    }
}





