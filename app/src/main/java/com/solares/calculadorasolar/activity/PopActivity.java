package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.AutoSizeText;

public class PopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Definindo o tamanho da activity de acordo com o tamnho da tela
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)(dm.widthPixels * .7f), (int)(dm.heightPixels*.25));

        //Identificando os componentes do layout e ajustando seu tamanho
        TextView textPerguntaEmail = findViewById(R.id.text_pergunta_email);
        AutoSizeText.AutoSizeTextView(textPerguntaEmail, MainActivity.alturaTela, MainActivity.larguraTela, 3f);
        Button buttonSim = findViewById(R.id.button_sim);
        AutoSizeText.AutoSizeButton(buttonSim, MainActivity.alturaTela, MainActivity.larguraTela, 3f);
        Button buttonNao = findViewById(R.id.button_nao);
        AutoSizeText.AutoSizeButton(buttonNao, MainActivity.alturaTela, MainActivity.larguraTela, 3f);

        Intent intentReceive = getIntent();
        final String NomeCidade = intentReceive.getStringExtra(Constants.EXTRA_CIDADE);
        final double custoReais = intentReceive.getDoubleExtra(Constants.EXTRA_CUSTO_REAIS, 0.0);
        final double consumokWh = intentReceive.getDoubleExtra(Constants.EXTRA_CONSUMO, 0.0);
        final double potenciaNecessaria = intentReceive.getDoubleExtra(Constants.EXTRA_POTENCIA, 0.0);
        final String[] placaEscolhida = intentReceive.getStringArrayExtra(Constants.EXTRA_PLACAS);
        final double area = intentReceive.getDoubleExtra(Constants.EXTRA_AREA, 0.0);
        final String[] inversor = intentReceive.getStringArrayExtra(Constants.EXTRA_INVERSORES);
        final double custoParcial = intentReceive.getDoubleExtra(Constants.EXTRA_CUSTO_PARCIAL, 0.0);
        final double custoTotal = intentReceive.getDoubleExtra(Constants.EXTRA_CUSTO_TOTAL, 0.0);
        final double geracaoAnual = intentReceive.getDoubleExtra(Constants.EXTRA_GERACAO, 0.0);
        final double lucro = intentReceive.getDoubleExtra(Constants.EXTRA_LUCRO, 0.0);
        final double taxaRetornoInvestimento = intentReceive.getDoubleExtra(Constants.EXTRA_TAXA_DE_RETORNO, 0.0);
        final double indiceLucratividade = intentReceive.getDoubleExtra(Constants.EXTRA_INDICE_LUCRATICVIDADE, 0.0);
        final double LCOE = intentReceive.getDoubleExtra(Constants.EXTRA_LCOE, 0.0);
        final int tempoRetorno = intentReceive.getIntExtra(Constants.EXTRA_TEMPO_RETORNO, 0);

        final Intent intentForward = new Intent(this, FinalActivity.class);
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

        buttonNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        buttonSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentForward);
            }
        });
    }
}
