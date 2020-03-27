package com.solares.calculadorasolar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        context = getApplicationContext();

        TextView textTituloArea = findViewById(R.id.text_titulo_area);
        AutoSizeText.AutoSizeTextView(textTituloArea, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

        Button buttonRecalcArea = findViewById(R.id.button_recalcular_area);
        AutoSizeText.AutoSizeButton(buttonRecalcArea, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, CalculoActivity.alturaTela, CalculoActivity.larguraTela, 4f);

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

        textAreaAtual.setText(String.format(Locale.ITALY, "Área Atual: %.2f m²", area));

        buttonRecalcArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 try {
                     //Pega a área digitada no edit text
                     float AreaAlvo = Float.parseFloat(editArea.getText().toString());

                     //Se a área for menor ou igual a zero, pede pro usuário inserir novamente
                     if (AreaAlvo <= 0f) {
                         Toast.makeText(AreaActivity.this, "Insira uma nova área!", Toast.LENGTH_LONG).show();
                     } else {
                         //Cria os input streams, de onde vai-se ler os arquivos .csv e pegar as informações necessárias
                         InputStream isEstado, isPaineis, isInversores;
                         isEstado = getResources().openRawResource(R.raw.banco_estados);
                         isPaineis = getResources().openRawResource(R.raw.banco_paineis);
                         isInversores = getResources().openRawResource(R.raw.banco_inversores);
                         //Refaz o cálculo com a nova área e inicia a ResultadoActivity
                         Intent intent = ReCalculate(AreaAlvo, cityVec, NomeCidade, custoReais, isEstado, isPaineis, isInversores, getApplicationContext());
                         startActivity(intent);
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



    public static Intent ReCalculate(float AreaAlvo, String[] cityVec, String cityName, double custoReais,
                                     InputStream isEstado, InputStream isPaineis, InputStream isInversores,
                                     Context MyContext) {
        try {
            double solarHour = CalculoActivity.MeanSolarHour(cityVec);

            //Pega informações do estado
            String[] stateVec;
            if(cityVec != null){
                stateVec = CSVRead.getState(cityVec, isEstado);
            } else {
                throw new Exception("Cidade não foi encontrada");
            }

            //Calcula o consumo mensal em KWh
            CalculoActivity.costReais = CalculoActivity.ValueWithoutTaxes(custoReais);
            double energyConsumed;
            if(stateVec != null){
                energyConsumed = CalculoActivity.ConvertToKWh(CalculoActivity.costReais, stateVec);
            } else {
                throw new Exception("Estado não foi encontrado");
            }

            double capacityWp = CalculoActivity.FindCapacity(energyConsumed, solarHour);
            if(capacityWp < 0.0){
                Toast.makeText(MyContext, "O seu consumo de energia é muito baixo!", Toast.LENGTH_LONG).show();
            } else {
                //Definindo as placas
                String[] solarPanel = CSVRead.DefineSolarPanel(isPaineis, capacityWp, AreaAlvo);
                double area = CalculoActivity.DefineArea(solarPanel);

                //Definindo os inversores
                String[] invertor = CSVRead.DefineInvertor(isInversores, solarPanel);

                //Definindo os custos
                double[] costs = CalculoActivity.DefineCosts(solarPanel, invertor);

                //Calculo da energia produzida em um ano
                double anualGeneration = CalculoActivity.EstimateAnualGeneration(solarPanel, stateVec, cityVec);

                CalculoActivity.GetEconomicInformation(anualGeneration, invertor, energyConsumed, costs[Constants.iCOSTS_TOTAL], stateVec);

                //Fechar as inputStreams //É necessário criar 3 try-catch porque se houver uma falha em um is, os outros ainda são fechados normalmente
                try {
                    isPaineis.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    isEstado.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    isInversores.close();
                } catch (Exception e){
                    e.printStackTrace();
                }

                //Cria o intent para mudar para ResultadoActivity
                Intent intent = new Intent(MyContext, ResultadoActivity.class);
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
                //Se o usuário modificou a tarifa, ela será passada assim pra próxima activity
                if(CalculoActivity.PtarifaPassada != 0.0){
                    intent.putExtra(Constants.EXTRA_TARIFA, CalculoActivity.PtarifaPassada);
                } else {
                    intent.putExtra(Constants.EXTRA_TARIFA, Double.parseDouble(stateVec[Constants.iEST_TARIFA]));
                }
                //Retorna a intent
                return intent;
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //Se ocorreu algum erro, fechar os inputStreams
            //Fechar as inputStreams //É necessário criar 3 try-catch porque se houver uma falha em um is, os outros ainda são fechados normalmente
            try {
                if(isPaineis!=null){
                    isPaineis.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                if(isPaineis!=null){
                    isEstado.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                if(isPaineis!=null){
                    isInversores.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(CalculoActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
