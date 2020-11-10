package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Locale;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensionsAndSetTariff;
import static com.solares.calculadorasolar.activity.MainActivity.PtarifaPassada;

public class TarifaActivity extends AppCompatActivity {

    public static float porcent = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifa);

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensionsAndSetTariff(this, PtarifaPassada);

        //Pega o layout para poder colocar um listener nele (esconder o teclado)
        ConstraintLayout layout = findViewById(R.id.layout_tarifa);

        //Pega o view do título e ajusta o tamanho da fonte
        TextView textTituloTarifa = findViewById(R.id.text_titulo_tarifa);
        AutoSizeText.AutoSizeTextView(textTituloTarifa, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        //Pega o view da tarifa atual e ajusta o tamanho da fonte
        TextView textTarifaAtual = findViewById(R.id.text_tarifa_atual);
        AutoSizeText.AutoSizeTextView(textTarifaAtual, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view da nova tarifa e ajusta o tamanho da fonte
        TextView textNovaTarifa = findViewById(R.id.text_nova_tarifa);
        AutoSizeText.AutoSizeTextView(textNovaTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view do edit text para a nova tarifa e ajusta o tamanho da fonte
        final EditText editTarifa = findViewById(R.id.editText_tarifa);
        AutoSizeText.AutoSizeEditText(editTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view da unidade e ajusta o tamanho da fonte
        TextView textUnidadeTarifa = findViewById(R.id.text_unidade_tarifa);
        AutoSizeText.AutoSizeTextView(textUnidadeTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view do botão pra recalcular e ajusta o tamanho da fonte
        Button buttonRecalcTarifa = findViewById(R.id.button_recalcular_tarifa);
        AutoSizeText.AutoSizeButton(buttonRecalcTarifa, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        //Pega o view do botão para voltar e ajusta o tamanho da fonte
        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, MainActivity.alturaTela, MainActivity.larguraTela, 4f);


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
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Log.i("TarifaActivity", "Error while hiding keyboard");
        }
    }

    public void AtualizarTarifa(final double custoReais, final String[] cityVec, final String cityName, final EditText editTarifa){
        try {
            //Pega a tarifa digitada no edit text
            double NovaTarifa = Double.parseDouble(editTarifa.getText().toString());

            //Se a tarifa for menor ou igual a zero, pede pro usuário inserir novamente
            if (NovaTarifa <= 0.0) {
                try {
                    Toast.makeText(this, "Insira uma nova tarifa! Valor menor ou igual a zero!", Toast.LENGTH_LONG).show();
                } catch (Exception etoast1){
                    etoast1.printStackTrace();
                }
            } else if (NovaTarifa > custoReais/Constants.COST_DISP){  //Se a tarifa for muito grande
                try {
                    Toast.makeText(this, "Insira uma nova tarifa! Valor muito alto!", Toast.LENGTH_LONG).show();
                } catch (Exception etoast2){
                    etoast2.printStackTrace();
                }
            } else {
                //Atualiza a tarifa passada
                MainActivity.PtarifaPassada = NovaTarifa;
                //Refaz o cálculo com a nova área e inicia a ResultadoActivity
                //Criar uma thread para fazer o cálculo pois é um processamento demorado
                Thread thread = new Thread(){
                    public void run(){
                        //MainActivity.Calculate(-1, cityVec, cityName, -1, custoReais, null, TarifaActivity.this);
                    }
                };
                thread.start();

            }
        } catch (Exception e){
            try {
                Toast.makeText(this, "Insira uma nova tarifa, com um ponto separando a parte real da inteira", Toast.LENGTH_LONG).show();
            } catch (Exception etoast3){
                etoast3.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
