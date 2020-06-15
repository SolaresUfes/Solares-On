package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.Constants;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensionsAndSetTariff;

public class ResultadoActivity extends AppCompatActivity{

    public float porcent = 3f;

    //Layout usado para deixar o background do pop activity trasnparente
    public ConstraintLayout layoutResultBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        try {

            Intent intent = getIntent();
            final String[] cityVec = intent.getStringArrayExtra(Constants.EXTRA_VETOR_CIDADE);
            final String NomeCidade = intent.getStringExtra(Constants.EXTRA_CIDADE);
            final double custoReais = intent.getDoubleExtra(Constants.EXTRA_CUSTO_REAIS, 0.0);
            final double consumokWh = intent.getDoubleExtra(Constants.EXTRA_CONSUMO, 0.0);
            final double potenciaNecessaria = intent.getDoubleExtra(Constants.EXTRA_POTENCIA, 0.0);
            final String[] placaEscolhida = intent.getStringArrayExtra(Constants.EXTRA_PLACAS);
            final double area = intent.getDoubleExtra(Constants.EXTRA_AREA, 0.0);
            final String[] inversor = intent.getStringArrayExtra(Constants.EXTRA_INVERSORES);
            final double custoParcial = intent.getDoubleExtra(Constants.EXTRA_CUSTO_PARCIAL, 0.0);
            final double custoTotal = intent.getDoubleExtra(Constants.EXTRA_CUSTO_TOTAL, 0.0);
            final double geracaoAnual = intent.getDoubleExtra(Constants.EXTRA_GERACAO, 0.0);
            final double lucro = intent.getDoubleExtra(Constants.EXTRA_LUCRO, 0.0);
            final double taxaRetornoInvestimento = intent.getDoubleExtra(Constants.EXTRA_TAXA_DE_RETORNO, 0.0);
            final double economiaMensal = intent.getDoubleExtra(Constants.EXTRA_ECONOMIA_MENSAL, 0.0);
            final double LCOE = intent.getDoubleExtra(Constants.EXTRA_LCOE, 0.0);
            final int tempoRetorno = intent.getIntExtra(Constants.EXTRA_TEMPO_RETORNO, 0);
            final double horaSolar = intent.getDoubleExtra(Constants.EXTRA_HORA_SOLAR, 0.0);
            final double tarifaMensal = intent.getDoubleExtra(Constants.EXTRA_TARIFA, 0.0);

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

            //Passar as informações para a activity que os salvara em um .csv
            final Intent intentForward = new Intent(this, PopActivity.class);
            intentForward.putExtra(Constants.EXTRA_CIDADE, NomeCidade);
            intentForward.putExtra(Constants.EXTRA_CUSTO_REAIS, custoReais);
            intentForward.putExtra(Constants.EXTRA_CONSUMO, consumokWh);
            intentForward.putExtra(Constants.EXTRA_POTENCIA, potenciaNecessaria);
            intentForward.putExtra(Constants.EXTRA_PLACAS, placaEscolhida);
            intentForward.putExtra(Constants.EXTRA_AREA, area);
            intentForward.putExtra(Constants.EXTRA_INVERSORES, inversor);
            intentForward.putExtra(Constants.EXTRA_CUSTO_PARCIAL, custoParcial);
            intentForward.putExtra(Constants.EXTRA_CUSTO_TOTAL, custoTotal);
            intentForward.putExtra(Constants.EXTRA_GERACAO, geracaoAnual);
            intentForward.putExtra(Constants.EXTRA_LUCRO, lucro);
            intentForward.putExtra(Constants.EXTRA_TAXA_DE_RETORNO, taxaRetornoInvestimento);
            intentForward.putExtra(Constants.EXTRA_ECONOMIA_MENSAL, economiaMensal);
            intentForward.putExtra(Constants.EXTRA_LCOE, LCOE);
            intentForward.putExtra(Constants.EXTRA_TEMPO_RETORNO, tempoRetorno);

            //Criação do background para o pop activity (que ficará transparente)
            layoutResultBackground = findViewById(R.id.layout_result_background);
            layoutResultBackground.setAlpha(1f);

            //Listener do botão finalizar
            buttonFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IniciarPopActivity(intentForward, layoutResultBackground);
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

    protected void onResume () {
        super.onResume();
        layoutResultBackground.setAlpha(1f);
    }

    public void IniciarPopActivity(Intent intent, ConstraintLayout layoutBackground){
        layoutBackground.setAlpha(0.4f);
        startActivity(intent);
    }
}





