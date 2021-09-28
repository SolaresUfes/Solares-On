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

import java.util.Locale;

public class MostrarPainelContInvActivity extends AppCompatActivity {

    private CalculadoraOffGrid calculadora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_painel_cont_inv);

        Intent intent = getIntent();
        calculadora = (CalculadoraOffGrid) intent.getSerializableExtra(Constants.EXTRA_CALCULADORAOFF);
        final Global variavelGlobal = (Global)getApplicationContext();

        TextView textPlaca = findViewById(R.id.text_placa1);
        TextView textControlador = findViewById(R.id.text_controlador1);
        TextView textInversor = findViewById(R.id.text_inversor1);


        textPlaca.setText(String.format(Locale.ITALY, "%d %s de %.0f Wp",
                Integer.parseInt(variavelGlobal.getPlaca()[Constants.iPANEL_QTD]), "Placa", Double.parseDouble(variavelGlobal.getPlaca()[Constants.iPANEL_POTENCIA])));

        /*textControlador.setText(String.format(Locale.ITALY, "%d %s de %.0f Wp",
                Integer.parseInt(calculadora.getControlador()[Constants.iPANEL_QTD]), "Controlador", Double.parseDouble(calculadora.getControlador()[Constants.iPANEL_POTENCIA])));

        /*textInversor.setText(String.format(Locale.ITALY, "%d %s de %.2f kW",
                Integer.parseInt(calculadora.getInversor()[Constants.iINV_QTD]), "Inversor", Double.parseDouble(calculadora.getInversor()[Constants.iINV_POTENCIA])/1000));
*/
        //Botão voltar
        Button buttonVoltar = findViewById(R.id.button_voltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}