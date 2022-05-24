package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.Global;

import java.util.Locale;

public class BateriaOffGridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bateria_off_grid);

        //Pegar informações da última activity (informações que serão exibidas)
        Intent intent = getIntent();
        final CalculadoraOffGrid calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);

        final Global variavelGlobal = (Global)getApplicationContext();

        TextView textBateria = findViewById(R.id.text_bateria);
        TextView textNomeBateria = findViewById(R.id.text_nome_bateria);
        TextView textValorBateria = findViewById(R.id.text_preco_bateria);
        TextView textSerie = findViewById(R.id.text_serie);
        TextView textValorQntSerie = findViewById(R.id.text_qnt_serie);
        TextView textParal = findViewById(R.id.text_paral);
        TextView textValorQntParal = findViewById(R.id.text_qnt_paral);

        try {
            System.out.println("Nome da bateria que chegou: "+calculadora.getBateria()[Constants.iPANEL_CUSTO_TOTAL]);

            textNomeBateria.setText(String.format(Locale.ITALY, "%s",
                    calculadora.getBateria()[Constants.iBAT_NOME]));
            textValorBateria.setText(String.format(Locale.ITALY, "R$ %.2f",
                    Double.parseDouble(calculadora.getBateria()[Constants.iBAT_PRECO_TOTAL])));

            textSerie.setText(String.format(Locale.ITALY, "Quantidade em Série"));
            textValorQntSerie.setText(String.format(Locale.ITALY, "%d",
                    Integer.parseInt(calculadora.getBateria()[Constants.iBAT_QTD_SERIE])));

            textParal.setText(String.format(Locale.ITALY, "Quantidade em Paralelo"));;
            textValorQntParal.setText(String.format(Locale.ITALY, "%d",
                    Integer.parseInt(calculadora.getBateria()[Constants.iBAT_QTD_PARAL])));

            //Botão voltar
            Button buttonVoltar = findViewById(R.id.button_voltar);
            buttonVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}