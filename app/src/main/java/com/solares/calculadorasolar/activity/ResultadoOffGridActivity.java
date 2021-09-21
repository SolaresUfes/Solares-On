package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.solares.calculadorasolar.R;

public class ResultadoOffGridActivity extends AppCompatActivity {

    public ViewHolder mViewHolder = new ViewHolder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_off_grid);

        try {
            this.mViewHolder.buttonEquipamento = findViewById(R.id.button_equipamentos);
            this.mViewHolder.buttonEconomico = findViewById(R.id.button_economico);
            this.mViewHolder.buttonBateria = findViewById(R.id.button_bateria);
            this.mViewHolder.buttonGeracao = findViewById(R.id.button_geracao);
            this.mViewHolder.buttonFinalizar = findViewById(R.id.button_finalizar);

            this.mViewHolder.buttonEquipamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            this.mViewHolder.buttonEconomico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            this.mViewHolder.buttonBateria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
            e.printStackTrace();
        }
    }

    public static class ViewHolder{
        Button buttonEquipamento;
        Button buttonEconomico;
        Button buttonBateria;
        Button buttonGeracao;
        Button buttonFinalizar;
    }
}