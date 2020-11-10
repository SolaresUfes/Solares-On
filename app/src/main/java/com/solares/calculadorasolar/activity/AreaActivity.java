package com.solares.calculadorasolar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.CSVRead;
import com.solares.calculadorasolar.classes.Constants;

import java.io.InputStream;
import java.util.Locale;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensionsAndSetTariff;
import static com.solares.calculadorasolar.activity.MainActivity.PtarifaPassada;

public class AreaActivity extends AppCompatActivity {

    public static float porcent = 3f;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        context = getApplicationContext();

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensionsAndSetTariff(this, PtarifaPassada);

        TextView textTituloArea = findViewById(R.id.text_titulo_area);
        AutoSizeText.AutoSizeTextView(textTituloArea, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        Button buttonRecalcArea = findViewById(R.id.button_recalcular_area);
        AutoSizeText.AutoSizeButton(buttonRecalcArea, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        TextView textAreaAtual = findViewById(R.id.text_area_atual);
        AutoSizeText.AutoSizeTextView(textAreaAtual, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textNovaArea = findViewById(R.id.text_nova_area);
        AutoSizeText.AutoSizeTextView(textNovaArea, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        final EditText editArea = findViewById(R.id.editText_area);
        AutoSizeText.AutoSizeEditText(editArea, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textUnidadeArea = findViewById(R.id.text_unidade);
        AutoSizeText.AutoSizeTextView(textUnidadeArea, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        ConstraintLayout layout = findViewById(R.id.layout_area);

        Intent intent = getIntent();
        final double area = intent.getDoubleExtra(Constants.EXTRA_AREA, 0.0);
        final String[] cityVec = intent.getStringArrayExtra(Constants.EXTRA_VETOR_CIDADE);
        final String NomeCidade = intent.getStringExtra(Constants.EXTRA_CIDADE);
        final double custoReais = intent.getDoubleExtra(Constants.EXTRA_CUSTO_REAIS, 0.0);

        textAreaAtual.setText(String.format(Locale.ITALY, "Área Atual: %.2f m²", area));

        buttonRecalcArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 try {
                     //Pega a área digitada no edit text
                     final float AreaAlvo = Float.parseFloat(editArea.getText().toString());

                     //Se a área for menor ou igual a zero, pede pro usuário inserir novamente
                     if (AreaAlvo <= 0f) {
                         try {
                             Toast.makeText(AreaActivity.this, "Insira uma nova área!", Toast.LENGTH_LONG).show();
                         } catch (Exception etoast1){
                             etoast1.printStackTrace();
                         }
                     } else {
                         //Refaz o cálculo com a nova área e inicia a ResultadoActivity
                         //Criar uma thread para fazer o cálculo pois é um processamento demorado
                         Thread thread = new Thread(){
                             public void run(){
                                 Intent intent;
                                 //MainActivity.Calculate(AreaAlvo, cityVec, NomeCidade, -1, custoReais, null, AreaActivity.this);
                             }
                         };
                         thread.start();
                     }
                 } catch (Exception e){
                     try {
                         Toast.makeText(AreaActivity.this, "Insira uma nova área!", Toast.LENGTH_LONG).show();
                     } catch (Exception etoast2){
                         etoast2.printStackTrace();
                     }
                     e.printStackTrace();
                 }
            }
        });

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(editArea);
            }
        });

    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Log.i("AreaActivity", "Error while hiding keyboard");
        }
    }
}
