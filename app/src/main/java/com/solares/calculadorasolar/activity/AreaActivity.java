package com.solares.calculadorasolar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
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

import static com.solares.calculadorasolar.activity.MainActivity.GetPhoneDimensionsAndSetTariff;
import static com.solares.calculadorasolar.activity.MainActivity.PtarifaPassada;

public class AreaActivity extends AppCompatActivity {

    public static float porcent = 3f;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        context = getApplicationContext();

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        //Essa função pega as dimensões e as coloca em váriaveis globais
        GetPhoneDimensionsAndSetTariff(this, PtarifaPassada);

        TextView textTituloArea = findViewById(R.id.text_titulo_area);
        AutoSizeText.AutoSizeTextView(textTituloArea, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        Button buttonRecalcArea = findViewById(R.id.button_recalcular_area);
        AutoSizeText.AutoSizeButton(buttonRecalcArea, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        Button buttonVoltar = findViewById(R.id.button_voltar);
        AutoSizeText.AutoSizeButton(buttonVoltar, MainActivity.alturaTela, MainActivity.larguraTela, 4f);

        TextView textAreaAtual = findViewById(R.id.text_area_atual);
        AutoSizeText.AutoSizeTextView(textAreaAtual, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textNovaArea = findViewById(R.id.text_nova_area);
        AutoSizeText.AutoSizeTextView(textNovaArea, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        final EditText editArea = findViewById(R.id.editText_area);
        AutoSizeText.AutoSizeEditText(editArea, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

        TextView textUnidadeArea = findViewById(R.id.text_unidade);
        AutoSizeText.AutoSizeTextView(textUnidadeArea, MainActivity.alturaTela, MainActivity.larguraTela, porcent);

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
                     final float AreaAlvo = Float.parseFloat(editArea.getText().toString());

                     //Se a área for menor ou igual a zero, pede pro usuário inserir novamente
                     if (AreaAlvo <= 0f) {
                         Toast.makeText(AreaActivity.this, "Insira uma nova área!", Toast.LENGTH_LONG).show();
                     } else {
                         //Cria os input streams, de onde vai-se ler os arquivos .csv e pegar as informações necessárias
                         final InputStream isEstado = getResources().openRawResource(R.raw.banco_estados);
                         final InputStream isPaineis = getResources().openRawResource(R.raw.banco_paineis);
                         final InputStream isInversores = getResources().openRawResource(R.raw.banco_inversores);
                         //Refaz o cálculo com a nova área e inicia a ResultadoActivity
                         //Criar uma thread para fazer o cálculo pois é um processamento demorado
                         Thread thread = new Thread(){
                             public void run(){
                                 Intent intent;
                                 intent = ReCalculate(AreaAlvo, cityVec, NomeCidade, custoReais, isEstado, isPaineis, isInversores, getApplicationContext());
                                 startActivity(intent);
                             }
                         };
                         thread.start();
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
            double solarHour = MainActivity.MeanSolarHour(cityVec);

            //Pega informações do estado
            String[] stateVec;
            if(cityVec != null){
                stateVec = CSVRead.getState(cityVec, isEstado);
                //Verifica se foi passada alguma tarifa pelo usuário:
                if(PtarifaPassada != 0.0){
                    if(stateVec != null) {
                        //Se foi passada uma tarifa, ela é colocada no lugar da do estado
                        stateVec[Constants.iEST_TARIFA] = Double.toString(PtarifaPassada);
                    }
                }
            } else {
                throw new Exception("Cidade não foi encontrada");
            }

            //Calcula o consumo mensal em KWh
            MainActivity.costReais = MainActivity.ValueWithoutTaxes(custoReais);
            double energyConsumed;
            if(stateVec != null){
                energyConsumed = MainActivity.ConvertToKWh(MainActivity.costReais, stateVec);
            } else {
                throw new Exception("Estado não foi encontrado");
            }

            double capacityWp = MainActivity.FindCapacity(energyConsumed, solarHour);
            if(capacityWp < 0.0){
                Toast.makeText(MyContext, "O seu consumo de energia é muito baixo!", Toast.LENGTH_LONG).show();
            } else {
                //Definindo as placas
                String[] solarPanel = CSVRead.DefineSolarPanel(isPaineis, capacityWp, AreaAlvo);
                double area = MainActivity.DefineArea(solarPanel);

                //Definindo os inversores
                String[] invertor = CSVRead.DefineInvertor(isInversores, solarPanel);

                //Definindo os custos
                double[] costs = MainActivity.DefineCosts(solarPanel, invertor);

                //Calculo da energia produzida em um ano
                double anualGeneration = MainActivity.EstimateAnualGeneration(solarPanel, stateVec, cityVec);

                MainActivity.GetEconomicInformation(anualGeneration, invertor, energyConsumed, costs[Constants.iCOSTS_TOTAL], stateVec);

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
                intent.putExtra(Constants.EXTRA_LUCRO, MainActivity.Ppayback);
                intent.putExtra(Constants.EXTRA_TAXA_DE_RETORNO, MainActivity.PInternalRateOfReturn);
                intent.putExtra(Constants.EXTRA_INDICE_LUCRATICVIDADE, MainActivity.PindiceLucratividade);
                intent.putExtra(Constants.EXTRA_LCOE, MainActivity.PLCOE);
                intent.putExtra(Constants.EXTRA_TEMPO_RETORNO, MainActivity.PTempoRetorno);
                intent.putExtra(Constants.EXTRA_HORA_SOLAR, solarHour);
                //Se o usuário modificou a tarifa, ela será passada assim pra próxima activity
                if(MainActivity.PtarifaPassada != 0.0){
                    intent.putExtra(Constants.EXTRA_TARIFA, MainActivity.PtarifaPassada);
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
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Log.i("AreaActivity", "Error while hiding keyboard");
        }
    }
}
