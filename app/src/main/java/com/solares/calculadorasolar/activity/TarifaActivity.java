package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.auxiliares.ExplicacaoInfos;
import com.solares.calculadorasolar.classes.auxiliares.FirebaseManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

public class TarifaActivity extends AppCompatActivity {

    public static float porcent = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifa);

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensions(this);

        //Pega o layout para poder colocar um listener nele (esconder o teclado)
        ConstraintLayout layout = findViewById(R.id.layout_tarifa);


        //Pega o view da explicação e ajusta o tamanho da fonte
        TextView textExplicacaoTarifa = findViewById(R.id.text_explicacao_tarifa);
        AutoSizeText.AutoSizeTextView(textExplicacaoTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view do título e ajusta o tamanho da fonte
        TextView textTituloTarifa = findViewById(R.id.text_titulo_tarifa);
        AutoSizeText.AutoSizeTextView(textTituloTarifa, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

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


        //pegar os intents
        Intent intent = getIntent();
        final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);

        //MOstrar a tarifa atual como padrão
        editTarifa.setText(String.format(Locale.ENGLISH, "%.2f", calculadora.pegaTarifaMensal()));


        //Tutorial sobre as informações
        findViewById(R.id.inst_button_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExplicacaoInfos.ShowPopUpInfo(TarifaActivity.this, findViewById(R.id.blackener), "Dúvidas",
                        "Altere o valor da tarifa de energia usado nos cálculos. O valor da tarifa deve ser inserido sem impostos e seu valor pode ser encontrado na sua conta de luz ou no site da distribuidora de energia que atende ao seu imóvel.");
            }
        });

        //Listener do botão de recalcular
        buttonRecalcTarifa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtualizarTarifa(calculadora, editTarifa);
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

    public void AtualizarTarifa(final CalculadoraOnGrid calculadora, final EditText editTarifa){
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
            } else {
                //Atualiza a tarifa passada
                calculadora.setTarifaMensal(NovaTarifa);
                //Refaz o cálculo com a nova tarifa e inicia a ResultadoActivity
                //Criar uma thread para fazer o cálculo pois é um processamento demorado
                Thread thread = new Thread(){
                    public void run(){
                        calculadora.Calcular(TarifaActivity.this);
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
