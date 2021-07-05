package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.Constants;

import java.util.Locale;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

public class ConsumoActivity extends AppCompatActivity {

    public static float porcent = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo);

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensions(this);

        //Pega o layout para poder colocar um listener nele (esconder o teclado)
        ConstraintLayout layout = findViewById(R.id.layout_consumo);

        //Pega o view do título e ajusta o tamanho da fonte
        TextView textTituloConsumo = findViewById(R.id.text_titulo_consumo);
        AutoSizeText.AutoSizeTextView(textTituloConsumo, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        //Pega o view da explicação e ajusta o tamanho da fonte
        TextView textExplicacaoConsumo = findViewById(R.id.ACRtext_explicacao_popup);
        AutoSizeText.AutoSizeTextView(textExplicacaoConsumo, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view da tarifa atual e ajusta o tamanho da fonte
        TextView textConsumoAtual = findViewById(R.id.text_consumo_atual);
        AutoSizeText.AutoSizeTextView(textConsumoAtual, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view da nova tarifa e ajusta o tamanho da fonte
        TextView textNovoConsumo = findViewById(R.id.text_novo_consumo);
        AutoSizeText.AutoSizeTextView(textNovoConsumo, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view do edit text para a nova tarifa e ajusta o tamanho da fonte
        final EditText editConsumo = findViewById(R.id.editText_consumo);
        AutoSizeText.AutoSizeEditText(editConsumo, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view da unidade e ajusta o tamanho da fonte
        TextView textUnidadeTarifa = findViewById(R.id.text_unidade_consumo);
        AutoSizeText.AutoSizeTextView(textUnidadeTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view do botão pra recalcular e ajusta o tamanho da fonte
        Button buttonRecalcConsumo = findViewById(R.id.button_recalcular_consumo);
        AutoSizeText.AutoSizeButton(buttonRecalcConsumo, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        /////////////////////////////////////

        //pegar os intents
        Intent intent = getIntent();
        final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);

        //atualizar o tarifa atual
        textConsumoAtual.setText(String.format(Locale.ITALY, "Consumo atual: %.2f kWh", calculadora.pegaConsumokWhs()));

        //Listener do botão de recalcular
        buttonRecalcConsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtualizarTarifa(calculadora, editConsumo);
            }
        });

        //Listener do fundo do layout, se o usuário clicar nele, esconde o teclado
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(editConsumo);
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

    public void AtualizarTarifa(final CalculadoraOnGrid calculadora, final EditText editConsumo){
        try {
            //Pega o consumo digitada no edit text
            double novoConsumo = Double.parseDouble(editConsumo.getText().toString());
            double novaTarifa, consumoSemImpostos;

            //Se o consumo for menor ou igual a CIP, pede pro usuário inserir novamente
            if (novoConsumo <= Constants.CIP) {
                try {
                    Toast.makeText(this, "O valor para o consumo está muito baixo!", Toast.LENGTH_LONG).show();
                } catch (Exception etoast){
                    etoast.printStackTrace();
                }
            } else {
                ///////Descobre a nova tarifa baseada no consumo em KWh
                //Tira os impostos do custo em reais
                consumoSemImpostos = CalculadoraOnGrid.ValueWithoutTaxes(calculadora.pegaCustoReais());
                //Descobre a tarifa
                novaTarifa = consumoSemImpostos/novoConsumo;
                //Atualiza a tarifa passada
                calculadora.setTarifaMensal(novaTarifa);
                //Refaz o cálculo com a nova tarifa/consumo e inicia a ResultadoActivity
                //Criar uma thread para fazer o cálculo pois é um processamento demorado
                Thread thread = new Thread(){
                    public void run(){
                        calculadora.Calcular(ConsumoActivity.this);
                    }
                };
                thread.start();

            }
        } catch (Exception e){
            try {
                Toast.makeText(this, "Insira um novo consumo, com um ponto separando a parte real da inteira", Toast.LENGTH_LONG).show();
            } catch (Exception etoast2){
                etoast2.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
