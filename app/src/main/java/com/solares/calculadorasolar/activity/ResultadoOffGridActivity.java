package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.AutoSizeText;
import com.solares.calculadorasolar.classes.auxiliares.Constants;

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensions;

public class ResultadoOffGridActivity extends AppCompatActivity {

    float porcent = 3f;

    public ViewHolder mViewHolder = new ViewHolder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_off_grid);

        Intent intent = getIntent();
        final CalculadoraOffGrid calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);
        System.out.println("Calculadora Placa: "+calculadora.pegaListaControladoresOffGrid());

        GetPhoneDimensions(this);

        try {
            this.mViewHolder.buttonEquipamento = findViewById(R.id.button_equipamentos);
            AutoSizeText.AutoSizeButton(this.mViewHolder.buttonEquipamento, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.buttonEconomico = findViewById(R.id.button_economico);
            AutoSizeText.AutoSizeButton(this.mViewHolder.buttonEconomico, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.buttonBateria = findViewById(R.id.button_bateria);
            AutoSizeText.AutoSizeButton(this.mViewHolder.buttonBateria, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.buttonGeracao = findViewById(R.id.button_geracao);
            AutoSizeText.AutoSizeButton(this.mViewHolder.buttonGeracao, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.buttonFinalizar = findViewById(R.id.button_finalizar);
            AutoSizeText.AutoSizeButton(this.mViewHolder.buttonFinalizar, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            TextView textResult = findViewById(R.id.text_titulo_resultado);
            AutoSizeText.AutoSizeTextView(textResult, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

            this.mViewHolder.buttonEquipamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ResultadoOffGridActivity.this, EquipamentosOffGridActivity.class);
                    intent.putExtra(Constants.EXTRA_CALCULADORAOFF, calculadora);
                    startActivity(intent);
                }
            });

            this.mViewHolder.buttonEconomico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ResultadoOffGridActivity.this, EconomicoOffGridActivity.class);
                    intent.putExtra(Constants.EXTRA_CALCULADORAOFF, calculadora);
                    startActivity(intent);
                }
            });

            /*this.mViewHolder.buttonBateria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ResultadoOffGridActivity.this, BateriaOffGridActivity.class);
                    intent.putExtra(Constants.EXTRA_CALCULADORAOFF, calculadora);
                    startActivity(intent);

                }
            });*/

            this.mViewHolder.buttonGeracao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ResultadoOffGridActivity.this, GeracaoOggGridActivity.class);
                    intent.putExtra(Constants.EXTRA_CALCULADORAOFF, calculadora);
                    startActivity(intent);
                }
            });

            this.mViewHolder.buttonFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ResultadoOffGridActivity.this, CreditoActivity.class);
                    startActivity(intent);
                }
            });

        }catch (Exception e){
            Log.i("ResultadoActivity", "Erro na captura de intents");
        }
    }

    /*public void AbrirActivityEquipamentos(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, MostrarPainelContInvActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAOFF, calculadora);

        startActivity(intent);
    }*/

    public static class ViewHolder{
        Button buttonEquipamento;
        Button buttonEconomico;
        Button buttonBateria;
        Button buttonGeracao;
        Button buttonFinalizar;
    }
}