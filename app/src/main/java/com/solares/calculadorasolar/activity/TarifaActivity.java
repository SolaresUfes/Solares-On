package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Locale;

public class TarifaActivity extends AppCompatActivity {

    public static float porcent = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifa);

        //Pega o layout para poder colocar um listener nele (esconder o teclado)
        ConstraintLayout layout = findViewById(R.id.layout_tarifa);

        //Pega o view do título e ajusta o tamanho da fonte
        TextView textTituloTarifa = findViewById(R.id.text_titulo_tarifa);
        AutoSizeText.AutoSizeTextView(textTituloTarifa, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

        //Pega o view da tarifa atual e ajusta o tamanho da fonte
        TextView textTarifaAtual = findViewById(R.id.text_tarifa_atual);
        AutoSizeText.AutoSizeTextView(textTarifaAtual, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        //Pega o view da nova tarifa e ajusta o tamanho da fonte
        TextView textNovaTarifa = findViewById(R.id.text_nova_tarifa);
        AutoSizeText.AutoSizeTextView(textNovaTarifa, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        //Pega o view do edit text para a nova tarifa e ajusta o tamanho da fonte
        final EditText editTarifa = findViewById(R.id.editText_tarifa);
        AutoSizeText.AutoSizeEditText(editTarifa, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        //Pega o view da unidade e ajusta o tamanho da fonte
        TextView textUnidadeTarifa = findViewById(R.id.text_unidade_tarifa);
        AutoSizeText.AutoSizeTextView(textUnidadeTarifa, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        //Pega o view do botão pra recalcular e ajusta o tamanho da fonte
        Button buttonRecalcTarifa = findViewById(R.id.button_recalcular_tarifa);
        AutoSizeText.AutoSizeButton(buttonRecalcTarifa, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

        //Pega o view do botão para voltar e ajusta o tamanho da fonte
        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);


        //pegar os intents
        Intent intent = getIntent();
        final double custoReais = intent.getDoubleExtra(Constants.EXTRA_CUSTO_REAIS, 0.0);
        final String[] cityVec = intent.getStringArrayExtra(Constants.EXTRA_VETOR_CIDADE);
        final String cityName = intent.getStringExtra(Constants.EXTRA_CIDADE);
        final double tarifaMensal = intent.getDoubleExtra(Constants.EXTRA_TARIFA, 0.0);

        //atualizar o tarifa atual
        textTarifaAtual.setText(String.format(Locale.ITALY, "Tarifa Atual: R$ %.2f / kWh", tarifaMensal));

        //Listener do botão de recalcular
        buttonRecalcTarifa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtualizarTarifa(custoReais, cityVec, cityName, editTarifa);
            }
        });

        //Listeners do botão de voltar
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //Listener do fundo do layout, se o usuário clicar nele, esconde o teclado
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(editTarifa);
            }
        });
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(CalculoActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void AtualizarTarifa(double custoReais, String[] cityVec, String cityName, EditText editTarifa){
        try {
            //Pega a tarifa digitada no edit text
            double NovaTarifa = Double.parseDouble(editTarifa.getText().toString());

            //Se a tarifa for menor ou igual a zero, pede pro usuário inserir novamente
            if (NovaTarifa <= 0.0) {
                Toast.makeText(this, "Insira uma nova tarifa!", Toast.LENGTH_LONG).show();
            } else {
                //Atualiza a tarifa passada
                CalculoActivity.PtarifaPassada = NovaTarifa;
                //Cria os input streams, de onde vai-se ler os arquivos .csv e pegar as informações necessárias
                InputStream isEstado, isPaineis, isInversores;
                isEstado = getResources().openRawResource(R.raw.banco_estados);
                isPaineis = getResources().openRawResource(R.raw.banco_paineis);
                isInversores = getResources().openRawResource(R.raw.banco_inversores);
                //Refaz o cálculo com a nova área e inicia a ResultadoActivity
                Intent intent = AreaActivity.ReCalculate(-1, cityVec, cityName, custoReais, isEstado, isPaineis, isInversores, getApplicationContext());
                startActivity(intent);
            }
        } catch (Exception e){
            Toast.makeText(this, "Insira uma nova tarifa, com um ponto separando a parte real da inteira", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
