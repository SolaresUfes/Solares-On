package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.Constants;

public class ResultadoActivity extends AppCompatActivity{

    public ConstraintLayout layoutResultBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        Intent intent = getIntent();
        final int idCidade = intent.getIntExtra(Constants.EXTRA_ID_CIDADE, 77);
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

        Button buttonDados = findViewById(R.id.button_dados);
        Button buttonInstalacao = findViewById(R.id.button_instalacao);
        Button buttonAnalise = findViewById(R.id.button_analise);
        Button buttonIndicesEconomicos = findViewById(R.id.button_indices_economicos);
        Button buttonFinalizar = findViewById(R.id.button_finalizar);
        Button buttonAjustarArea = findViewById(R.id.button_ajustar_area);
        layoutResultBackground = findViewById(R.id.layout_result_background);
        layoutResultBackground.setAlpha(1f);


        buttonDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirActivityDados(custoReais, consumokWh, horaSolar);
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
                AbrirActivityArea(idCidade, NomeCidade, custoReais, area);
            }
        });

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
        intentForward.putExtra(Constants.EXTRA_INDICE_LUCRATICVIDADE, indiceLucratividade);
        intentForward.putExtra(Constants.EXTRA_LCOE, LCOE);
        intentForward.putExtra(Constants.EXTRA_TEMPO_RETORNO, tempoRetorno);

        buttonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalizarCalculo(intentForward, layoutResultBackground);
            }
        });
    }

    public void AbrirActivityDados(double custoReais, double consumokWh, double horaSolar){
        Intent intent = new Intent(this, DadosActivity.class);
        intent.putExtra(Constants.EXTRA_HORA_SOLAR, horaSolar);
        intent.putExtra(Constants.EXTRA_CUSTO_REAIS, custoReais);
        intent.putExtra(Constants.EXTRA_CONSUMO, consumokWh);
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

    public void AbrirActivityArea(int idCidade, String NomeCidade, double custoReais, double area){
        Intent intent = new Intent(this, AreaActivity.class);
        intent.putExtra(Constants.EXTRA_ID_CIDADE, idCidade);
        intent.putExtra(Constants.EXTRA_CIDADE, NomeCidade);
        intent.putExtra(Constants.EXTRA_CUSTO_REAIS, custoReais);
        intent.putExtra(Constants.EXTRA_AREA, area);
        startActivity(intent);
    }

    public void FinalizarCalculo(Intent intent, ConstraintLayout layoutBackground){
        layoutBackground.setAlpha(0.4f);
        startActivity(intent);
    }

    protected void onResume () {
        super.onResume();
        layoutResultBackground.setAlpha(1f);
    }

}





