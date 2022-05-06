package com.solares.calculadorasolar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.CalculadoraOffGrid;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.Global;

import java.util.ArrayList;
import java.util.Locale;

public class MostrarPainelContInvActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_painel_cont_inv);



        final Global variavelGlobal = (Global)getApplicationContext();

        TextView textPlaca = findViewById(R.id.text_placa1);
        TextView textControlador = findViewById(R.id.text_controlador1);
        TextView textInversor = findViewById(R.id.text_inversor1);

        try{
            //Pegar informações da última activity (informações que serão exibidas)
            Intent intent = getIntent();
            final CalculadoraOffGrid calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);
            System.out.println("Nome da placa que chegou: "+calculadora.getPlacaEscolhida()[Constants.iPANEL_NOME]);
            System.out.println("Nome do inversor que chegou: "+calculadora.getInversor()[Constants.iINVOFF_NOME]);
            //System.out.println("Nome do controlador que chegou: "+calculadora.getControlador()[Constants.iCON_NOME]);

            // for(int i=0; i< calculadora.getPlacaEscolhida().length; i++){
            //    System.out.println(calculadora.getPlacaEscolhida()[i]);
            // }

            textPlaca.setText(String.format(Locale.ITALY, "%d %s de %.0f Wp",
                    Integer.parseInt(calculadora.getPlacaEscolhida()[Constants.iPANEL_QTD]), "Placa", Double.parseDouble(calculadora.getPlacaEscolhida()[Constants.iPANEL_POTENCIA])));

            textControlador.setText(String.format(Locale.ITALY, "%d %s de %.0f Wp",
                    Integer.parseInt(calculadora.getControlador()[Constants.iPANEL_QTD]), "Controlador", Double.parseDouble(calculadora.getControlador()[Constants.iPANEL_POTENCIA])));

            if(calculadora.getInversor().equals("0")) {
                textInversor.setText(String.format(Locale.ITALY, "%d %s de %.2f kW",
                        Integer.parseInt(calculadora.getInversor()[Constants.iINVOFF_QTD]), "Inversor", (Double.parseDouble(calculadora.getInversor()[Constants.iINVOFF_POTENCIAAPARENTE]) / 1000) * 0.8));
            }
            else{
                textInversor.setText(String.format(Locale.ITALY,  "Não há necessidade de inversor!"));
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