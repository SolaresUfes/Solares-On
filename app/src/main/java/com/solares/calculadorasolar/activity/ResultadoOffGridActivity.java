package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.auxiliares.Constants;

public class ResultadoOffGridActivity extends AppCompatActivity {

    public ViewHolder mViewHolder = new ViewHolder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_off_grid);

        Intent intent = getIntent();
        final CalculadoraOffGrid calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);
        System.out.println("Calculadora Placa: "+calculadora.pegaListaControladoresOffGrid());

        try {
            this.mViewHolder.buttonEquipamento = findViewById(R.id.button_equipamentos);
            this.mViewHolder.buttonEconomico = findViewById(R.id.button_economico);
            this.mViewHolder.buttonBateria = findViewById(R.id.button_bateria);
            this.mViewHolder.buttonGeracao = findViewById(R.id.button_geracao);
            this.mViewHolder.buttonFinalizar = findViewById(R.id.button_finalizar);

            this.mViewHolder.buttonEquipamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ResultadoOffGridActivity.this, MostrarPainelContInvActivity.class);
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

            this.mViewHolder.buttonBateria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ResultadoOffGridActivity.this, BateriaOffGridActivity.class);
                    intent.putExtra(Constants.EXTRA_CALCULADORAOFF, calculadora);
                    startActivity(intent);

                }
            });

            this.mViewHolder.buttonGeracao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            this.mViewHolder.buttonFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }catch (Exception e){
            Log.i("ResultadoActivity", "Erro na captura de intents");
        }
    }

    public void AbrirActivityEquipamentos(CalculadoraOnGrid calculadora){
        Intent intent = new Intent(this, MostrarPainelContInvActivity.class);
        intent.putExtra(Constants.EXTRA_CALCULADORAOFF, calculadora);

        startActivity(intent);
    }

    public static class ViewHolder{
        Button buttonEquipamento;
        Button buttonEconomico;
        Button buttonBateria;
        Button buttonGeracao;
        Button buttonFinalizar;
    }
}