package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.auxiliares.FirebaseManager;
import com.solares.calculadorasolar.classes.entidades.Painel;

import java.util.Locale;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

public class DetalhesActivity extends AppCompatActivity {

    public static float porcent = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensions(this);

        //Pega o layout para poder colocar um listener nele (esconder o teclado)
        ConstraintLayout layout = findViewById(R.id.ADE_layout_tarifa);


        //Pega o view da explicação e ajusta o tamanho da fonte
        TextView textExplicacaoTarifa = findViewById(R.id.ADE_text_explicacao_tarifa);
        AutoSizeText.AutoSizeTextView(textExplicacaoTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view do título e ajusta o tamanho da fonte
        TextView textTituloTarifa = findViewById(R.id.ADE_text_titulo_tarifa);
        AutoSizeText.AutoSizeTextView(textTituloTarifa, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        //Pega o view da nova tarifa e ajusta o tamanho da fonte
        TextView textNovaTarifa = findViewById(R.id.ADE_text_nova_tarifa);
        AutoSizeText.AutoSizeTextView(textNovaTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view do edit text para a nova tarifa e ajusta o tamanho da fonte
        final EditText editTarifa = findViewById(R.id.ADE_editText_tarifa);
        AutoSizeText.AutoSizeEditText(editTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view da unidade e ajusta o tamanho da fonte
        TextView textUnidadeTarifa = findViewById(R.id.ADE_text_unidade_tarifa);
        AutoSizeText.AutoSizeTextView(textUnidadeTarifa, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Pega o view do número de fases e ajusta o tamanho da fonte
        TextView textNumeroFases = findViewById(R.id.ADE_text_numero_fases);
        AutoSizeText.AutoSizeTextView(textNumeroFases, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        //Criando spinner do número de fases
        final Spinner spinnerFases = findViewById(R.id.ADE_spinner_numero_fases);
        //Aqui, coloca o vetor de strings que será exibido no spinner
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this, R.array.Fases, R.layout.spinner_item);
        spinnerFases.setAdapter(adapterS);
        spinnerFases.setSelection(1);

        //Pega o view do botão pra recalcular e ajusta o tamanho da fonte
        Button buttonConfirm = findViewById(R.id.ADE_button_recalcular_tarifa);
        AutoSizeText.AutoSizeButton(buttonConfirm, MainActivity.alturaTela, MainActivity.larguraTela, 4f);


        //pegar os intents
        Intent intent = getIntent();
        final CalculadoraOnGrid calculadora = (CalculadoraOnGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAON);


        //MOstrar a tarifa atual como padrão
        editTarifa.setText(String.format(Locale.ENGLISH, "%.2f", calculadora.pegaTarifaMensal()));

        //Listener do botão de confirmar
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("firebase", "Placas:");
                for (Painel painel : calculadora.pegaListaPaineis() ) {
                    Log.d("firebase", painel.getMarca() + " " + painel.getCodigo() + " " + painel.getPotencia() + " " + painel.getArea() + " " + painel.getNOCT() + " " + painel.getCoefTempPot() + " " + painel.getPreco());
                }
                RealizaCalculos(calculadora, editTarifa, spinnerFases);
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

    public void RealizaCalculos(final CalculadoraOnGrid calculadora, final EditText editTarifa, final Spinner spinnerFases){
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

                //Seleciona o número de fases
                calculadora.setNumeroDeFases((int)spinnerFases.getSelectedItemPosition());

                calculadora.Calcular(DetalhesActivity.this);

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
