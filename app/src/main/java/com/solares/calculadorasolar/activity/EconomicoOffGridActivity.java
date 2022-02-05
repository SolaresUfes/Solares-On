package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.Global;

import java.util.Locale;

public class EconomicoOffGridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economico_off_grid);

        //Pegar informações da última activity (informações que serão exibidas)
        Intent intent = getIntent();
        final CalculadoraOffGrid calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);

        final Global variavelGlobal = (Global)getApplicationContext();

        TextView textNomePlaca = findViewById(R.id.text_nome_placa);
        TextView textNomeControlador = findViewById(R.id.text_nome_controlador);
        TextView textNomeInversor = findViewById(R.id.text_nome_inversor);
        TextView textPrecoPlaca = findViewById(R.id.text_preco_placa);
        TextView textPrecoControlador = findViewById(R.id.text_preco_controlador);
        TextView textPrecoInversor = findViewById(R.id.text_preco_inversor);

        try {
            System.out.println("Nome da placa que chegou: "+calculadora.getPlacaEscolhida()[Constants.iPANEL_CUSTO_TOTAL]);
            System.out.println("Nome do inversor que chegou: "+calculadora.getInversor()[Constants.iINVOFF_NOME]);
            System.out.println("Nome do controlador que chegou: "+calculadora.getControlador()[Constants.iCON_NOME]);

            //Placa
            textNomePlaca.setText(String.format(Locale.ITALY, "%s",
                    calculadora.getPlacaEscolhida()[Constants.iPANEL_NOME]));
            textPrecoPlaca.setText(String.format(Locale.ITALY, "R$ %.2f",
                    Double.parseDouble(calculadora.getPlacaEscolhida()[Constants.iPANEL_CUSTO_TOTAL])));

            //Controlador
            textNomeControlador.setText(String.format(Locale.ITALY, "%s",
                    calculadora.getControlador()[Constants.iCON_NOME]));
            textPrecoControlador.setText(String.format(Locale.ITALY, "R$ %.2f",
                    Double.parseDouble(calculadora.getControlador()[Constants.iCON_PRECO_TOTAL])));

            //Inversor
            if(calculadora.getInversor() != null) {
                textNomeInversor.setText(String.format(Locale.ITALY, "%s",
                        calculadora.getInversor()[Constants.iINVOFF_NOME]));
                textPrecoInversor.setText(String.format(Locale.ITALY, "R$ %.2f",
                        Double.parseDouble(calculadora.getInversor()[Constants.iINVOFF_PRECO_TOTAL])));
            }

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