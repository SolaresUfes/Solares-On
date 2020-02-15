package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.Constants;

import java.util.Locale;

public class InstalacaoActivity extends AppCompatActivity {

    public float percent = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instalacao);

        Intent intent = getIntent();
        final double potenciaNecessaria = intent.getDoubleExtra(Constants.EXTRA_POTENCIA, 0.0);
        final String[] placaEscolhida = intent.getStringArrayExtra(Constants.EXTRA_PLACAS);
        final double area = intent.getDoubleExtra(Constants.EXTRA_AREA, 0.0);
        final String[] inversor = intent.getStringArrayExtra(Constants.EXTRA_INVERSORES);

        TextView textTituloInstal = findViewById(R.id.text_titulo_intalacao);
        AutoSizeText.AutoSizeTextView(textTituloInstal, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

        TextView textEstaticoPotencia = findViewById(R.id.text_potencia_1);
        AutoSizeText.AutoSizeTextView(textEstaticoPotencia, CalculoActivity.alturaTela, CalculoActivity.larguraTela, percent);
        TextView textPotencia = findViewById(R.id.text_potencia);
        textPotencia.setText(String.format(Locale.ITALY,"%.2f Wp", potenciaNecessaria));
        AutoSizeText.AutoSizeTextView(textPotencia, CalculoActivity.alturaTela, CalculoActivity.larguraTela, percent);

        TextView textEstaticoPlaca = findViewById(R.id.text_placa_1);
        AutoSizeText.AutoSizeTextView(textEstaticoPlaca, CalculoActivity.alturaTela, CalculoActivity.larguraTela, percent);
        TextView textPlaca = findViewById(R.id.text_placa);
        AutoSizeText.AutoSizeTextView(textPlaca, CalculoActivity.alturaTela, CalculoActivity.larguraTela, percent);
        String singplur;
        if(Integer.parseInt(placaEscolhida[Constants.iPANEL_QTD]) > 1){
            singplur = "Placas";
        } else {
            singplur = "Placa";
        }
        textPlaca.setText(String.format(Locale.ITALY, "%d %s de %.0f W",
                Integer.parseInt(placaEscolhida[Constants.iPANEL_QTD]), singplur, Double.parseDouble(placaEscolhida[Constants.iPANEL_POTENCIA])));

        TextView textEstaticoArea = findViewById(R.id.text_area_1);
        AutoSizeText.AutoSizeTextView(textEstaticoArea, CalculoActivity.alturaTela, CalculoActivity.larguraTela, percent);
        TextView textArea = findViewById(R.id.text_area);
        textArea.setText(String.format(Locale.ITALY, "%.2f m²", area));
        AutoSizeText.AutoSizeTextView(textArea, CalculoActivity.alturaTela, CalculoActivity.larguraTela, percent);

        TextView textEstaticoInversor = findViewById(R.id.text_inversor_1);
        AutoSizeText.AutoSizeTextView(textEstaticoInversor, CalculoActivity.alturaTela, CalculoActivity.larguraTela, percent);
        TextView textInversor = findViewById(R.id.text_inversor);
        AutoSizeText.AutoSizeTextView(textInversor, CalculoActivity.alturaTela, CalculoActivity.larguraTela, percent);
        if(Integer.parseInt(inversor[Constants.iINV_QTD]) > 1){
            singplur = "Inversores";
        } else {
            singplur = "Inversor";
        }
        textInversor.setText(String.format(Locale.ITALY, "%d %s de %.0f W",
                Integer.parseInt(inversor[Constants.iINV_QTD]), singplur, Double.parseDouble(inversor[Constants.iINV_POTENCIA])));


        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
