package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CSVRead;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.fileWriter;

import java.io.InputStream;
import java.util.Locale;

public class AreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        Button buttonRecalcArea = findViewById(R.id.button_recalcular_area);
        Button buttonVoltar = findViewById(R.id.button_voltar);
        TextView textAreaAtual = findViewById(R.id.textView_area_atual);
        final EditText editArea = findViewById(R.id.editText_area);
        ConstraintLayout layout = findViewById(R.id.layout_area);

        Intent intent = getIntent();
        final double area = intent.getDoubleExtra(Constants.EXTRA_AREA, 0.0);
        final int idCidade = intent.getIntExtra(Constants.EXTRA_ID_CIDADE, 77);
        final String NomeCidade = intent.getStringExtra(Constants.EXTRA_CIDADE);
        final double custoReais = intent.getDoubleExtra(Constants.EXTRA_CUSTO_REAIS, 0.0);

        textAreaAtual.setText(String.format(Locale.ENGLISH, "Área Atual: %.2f m²", area));

        buttonRecalcArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 try {
                     float AreaAlvo = Float.parseFloat(editArea.getText().toString());

                     if (AreaAlvo <= 0f) {
                         Toast.makeText(AreaActivity.this, "Insira uma nova área!", Toast.LENGTH_LONG).show();
                     } else {
                         ReCalculate(AreaAlvo, idCidade, NomeCidade, custoReais);
                     }
                 } catch (Exception e){
                     Toast.makeText(AreaActivity.this, "Insira uma nova área!", Toast.LENGTH_LONG).show();
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



    public void ReCalculate(float AreaAlvo, int idCity, String cityName, double custoReais) {
        String city = cityName;

        try {
            InputStream is = getResources().openRawResource(R.raw.banco_irradiancia);
            String[] cityVec = CSVRead.getCity(idCity, is);
            double solarHour = CalculoActivity.MeanSolarHour(cityVec);

            CalculoActivity.costReais = CalculoActivity.ValueWithoutTaxes(custoReais);
            double energyConsumed = CalculoActivity.ConvertToKWh(CalculoActivity.costReais);

            double capacityWp = CalculoActivity.FindCapacity(energyConsumed, solarHour);
            if(capacityWp < 0.0){
                Toast.makeText(this, "O seu consumo de energia é muito baixo!", Toast.LENGTH_LONG).show();
            } else {
                //Definindo as placas
                is = getResources().openRawResource(R.raw.banco_paineis);
                String[] solarPanel = CSVRead.DefineSolarPanel(is, capacityWp, AreaAlvo);
                double area = CalculoActivity.DefineArea(solarPanel);

                //Definindo os inversores
                is = getResources().openRawResource(R.raw.banco_inversores);
                String[] invertor = CSVRead.DefineInvertor(is, solarPanel);
                String singPlur;
                if(invertor[Constants.iINV_QTD] == "1"){
                    singPlur = "Inversor";
                } else {
                    singPlur = "Inversores";
                }

                //Definindo os custos
                double[] costs = CalculoActivity.DefineCosts(solarPanel, invertor);
                //Calculo da energia produzida em um ano
                double anualGeneration = CalculoActivity.EstimateAnualGeneration(solarPanel, cityVec);

                CalculoActivity.GetEconomicInformation(anualGeneration, invertor, energyConsumed, costs[Constants.iCOSTS_TOTAL]);

                //Mudar para próxima activity
                Intent intent = new Intent(this, ResultadoActivity.class);
                //extras e guardar
                intent.putExtra(Constants.EXTRA_ID_CIDADE, idCity);
                intent.putExtra(Constants.EXTRA_CIDADE, city);
                intent.putExtra(Constants.EXTRA_CUSTO_REAIS, custoReais);
                intent.putExtra(Constants.EXTRA_CONSUMO, energyConsumed);
                intent.putExtra(Constants.EXTRA_POTENCIA, capacityWp);
                intent.putExtra(Constants.EXTRA_PLACAS, solarPanel);
                intent.putExtra(Constants.EXTRA_AREA, area);
                intent.putExtra(Constants.EXTRA_INVERSORES, invertor);
                intent.putExtra(Constants.EXTRA_CUSTO_PARCIAL, costs[Constants.iCOSTS_PARCIAL]);
                intent.putExtra(Constants.EXTRA_CUSTO_TOTAL, costs[Constants.iCOSTS_TOTAL]);
                intent.putExtra(Constants.EXTRA_GERACAO, anualGeneration);
                intent.putExtra(Constants.EXTRA_LUCRO, CalculoActivity.Ppayback);
                intent.putExtra(Constants.EXTRA_TAXA_DE_RETORNO, CalculoActivity.PInternalRateOfReturn);
                intent.putExtra(Constants.EXTRA_INDICE_LUCRATICVIDADE, CalculoActivity.PindiceLucratividade);
                intent.putExtra(Constants.EXTRA_LCOE, CalculoActivity.PLCOE);
                intent.putExtra(Constants.EXTRA_TEMPO_RETORNO, CalculoActivity.PTempoRetorno);
                intent.putExtra(Constants.EXTRA_HORA_SOLAR, solarHour);
                //Mudar
                startActivity(intent);
            }


        } catch (Exception e){
            Toast.makeText(this, R.string.informe_um_numero, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(CalculoActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
