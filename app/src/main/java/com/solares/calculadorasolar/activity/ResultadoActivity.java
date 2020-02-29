package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.Constants;

public class ResultadoActivity extends AppCompatActivity{

    public float porcent = 3f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

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
        final double indiceLucratividade = intent.getDoubleExtra(Constants.EXTRA_INDICE_LUCRATICVIDADE, 0.0);
        final double LCOE = intent.getDoubleExtra(Constants.EXTRA_LCOE, 0.0);
        final int tempoRetorno = intent.getIntExtra(Constants.EXTRA_TEMPO_RETORNO, 0);
        final double horaSolar = intent.getDoubleExtra(Constants.EXTRA_HORA_SOLAR, 0.0);
        final double tarifaMensal = intent.getDoubleExtra(Constants.EXTRA_TARIFA, 0.0);

        TextView textTituloResultado = findViewById(R.id.text_titulo_resultado);
        AutoSizeText.AutoSizeTextView(textTituloResultado, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

        Button buttonDados = findViewById(R.id.button_dados);
        AutoSizeText.AutoSizeButton(buttonDados, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        Button buttonInstalacao = findViewById(R.id.button_instalacao);
        AutoSizeText.AutoSizeButton(buttonInstalacao, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        Button buttonAnalise = findViewById(R.id.button_analise);
        AutoSizeText.AutoSizeButton(buttonAnalise, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        Button buttonIndicesEconomicos = findViewById(R.id.button_indices_economicos);
        AutoSizeText.AutoSizeButton(buttonIndicesEconomicos, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        Button buttonFinalizar = findViewById(R.id.button_finalizar);
        AutoSizeText.AutoSizeButton(buttonFinalizar, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);
        Button buttonAjustarArea = findViewById(R.id.button_ajustar_area);
        AutoSizeText.AutoSizeButton(buttonAjustarArea, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);


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
                AbrirActivityIndices(lucro, taxaRetornoInvestimento, indiceLucratividade, LCOE, tempoRetorno);
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

    public void AbrirActivityIndices(double lucro, double taxaRetornoInvestimento, double indiceLucratividade, double LCOE, int tempoRetorno){
        Intent intent = new Intent(this, IndicesEconomicosActivity.class);
        intent.putExtra(Constants.EXTRA_LUCRO, lucro);
        intent.putExtra(Constants.EXTRA_TAXA_DE_RETORNO, taxaRetornoInvestimento);
        intent.putExtra(Constants.EXTRA_INDICE_LUCRATICVIDADE, indiceLucratividade);
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
        CalculoActivity.PtarifaPassada = 0.0;

        Intent intent = new Intent(this, CreditoActivity.class);
        startActivity(intent);
    }
}





