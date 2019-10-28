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
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.CSVRead;
import com.solares.calculadorasolar.classes.Constants;

import java.io.InputStream;
import java.util.Locale;

public class AreaActivity extends AppCompatActivity {

    public static float porcent = 3f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        TextView textTituloArea = findViewById(R.id.text_titulo_area);
        AutoSizeText.AutoSizeTextView(textTituloArea, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 5f);

        Button buttonRecalcArea = findViewById(R.id.button_recalcular_area);
        AutoSizeText.AutoSizeButton(buttonRecalcArea, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 5f);

        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 5f);

        TextView textAreaAtual = findViewById(R.id.text_area_atual);
        AutoSizeText.AutoSizeTextView(textAreaAtual, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        TextView textNovaArea = findViewById(R.id.text_nova_area);
        AutoSizeText.AutoSizeTextView(textNovaArea, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        final EditText editArea = findViewById(R.id.editText_area);
        AutoSizeText.AutoSizeEditText(editArea, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);

        TextView textUnidadeArea = findViewById(R.id.text_unidade);
        AutoSizeText.AutoSizeTextView(textUnidadeArea, CalculoActivity.alturaTela, CalculoActivity.larguraTela, porcent);
        ConstraintLayout layout = findViewById(R.id.layout_area);

        Intent intent = getIntent();
        final double area = intent.getDoubleExtra(Constants.EXTRA_AREA, 0.0);
        final String[] cityVec = intent.getStringArrayExtra(Constants.EXTRA_VETOR_CIDADE);
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
                         ReCalculate(AreaAlvo, cityVec, NomeCidade, custoReais);
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



    public void ReCalculate(float AreaAlvo, String[] cityVec, String cityName, double custoReais) {
        try {
            InputStream is;
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

                //Definindo os custos
                double[] costs = CalculoActivity.DefineCosts(solarPanel, invertor);
                //Pega o estado
                is = getResources().openRawResource(R.raw.banco_estados);
                String[] stateVec;
                if(cityVec != null){
                    stateVec = CSVRead.getState(cityVec, is);
                } else {
                    throw new Exception("Cidade não foi encontrada");
                }
                //Calculo da energia produzida em um ano
                double anualGeneration = CalculoActivity.EstimateAnualGeneration(solarPanel, stateVec, cityVec);

                CalculoActivity.GetEconomicInformation(anualGeneration, invertor, energyConsumed, costs[Constants.iCOSTS_TOTAL]);

                //Mudar para próxima activity
                Intent intent = new Intent(this, ResultadoActivity.class);
                //extras e guardar
                intent.putExtra(Constants.EXTRA_VETOR_CIDADE, cityVec);
                intent.putExtra(Constants.EXTRA_CIDADE, cityName);
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
