package com.solares.calculadorasolar.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.AutoSizeText;
import com.solares.calculadorasolar.classes.CSVRead;
import com.solares.calculadorasolar.classes.Constants;
import com.solares.calculadorasolar.classes.IRR;

import java.io.InputStream;



public class CalculoActivity extends AppCompatActivity {

    public ViewHolder mViewHolder = new ViewHolder();
    public static double costReais;

    /////
    public static double PLCOE;
    public static double PInternalRateOfReturn;
    public static double Ppayback;
    public static double PindiceLucratividade;
    public static int PTempoRetorno;
    /////
    public static int larguraTela;
    public static int alturaTela;
    public float porcent = 5f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        larguraTela = size.x;
        alturaTela = size.y;

        this.mViewHolder.textSimulacao = findViewById(R.id.text_simulacao);
        AutoSizeText.AutoSizeTextView(this.mViewHolder.textSimulacao, alturaTela, larguraTela, 4f);
        this.mViewHolder.buttonCalc = findViewById(R.id.button_calc);
        AutoSizeText.AutoSizeButton(this.mViewHolder.buttonCalc, alturaTela, larguraTela, porcent);
        this.mViewHolder.editCostMonth = findViewById(R.id.edit_cost);
        AutoSizeText.AutoSizeEditText(this.mViewHolder.editCostMonth, alturaTela, larguraTela, porcent-2);

        this.mViewHolder.spinnerCities = findViewById(R.id.spinner_cities);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cidades, R.layout.spinner_item);
        this.mViewHolder.spinnerCities.setAdapter(adapter);
        this.mViewHolder.spinnerCities.setSelection(77);

        this.mViewHolder.layout = findViewById(R.id.layout_calculo);

        final float AreaAlvo = -1f;

        this.mViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mViewHolder.editCostMonth);
            }
        });

        this.mViewHolder.buttonCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculate(AreaAlvo);
            }
        });

    }

    public void Calculate(float AreaAlvo) {
        int idCity;
        String city;

        try {
            mViewHolder.editCostMonth.onEditorAction(EditorInfo.IME_ACTION_DONE);
            idCity = this.mViewHolder.spinnerCities.getSelectedItemPosition();
            city = this.mViewHolder.spinnerCities.getItemAtPosition(idCity).toString();
            InputStream is = getResources().openRawResource(R.raw.banco_irradiancia);
            String[] cityVec = CSVRead.getCity(idCity, is);
            double solarHour = MeanSolarHour(cityVec);//Double.parseDouble(cityVec[13])/1000.0;
            double custoReais;

            String cost = this.mViewHolder.editCostMonth.getText().toString();
            custoReais = Double.valueOf(cost);
            costReais = ValueWithoutTaxes(custoReais);
            double energyConsumed = ConvertToKWh(costReais);

            double capacityWp = FindCapacity(energyConsumed, solarHour);
            if(capacityWp < 0.0){
                Toast.makeText(this, "O seu consumo de energia é muito baixo!", Toast.LENGTH_LONG).show();
            } else {
                //Definindo as placas
                is = getResources().openRawResource(R.raw.banco_paineis);
                String[] solarPanel = CSVRead.DefineSolarPanel(is, capacityWp, AreaAlvo);
                double area = DefineArea(solarPanel);

                //Definindo os inversores
                is = getResources().openRawResource(R.raw.banco_inversores);
                String[] invertor = CSVRead.DefineInvertor(is, solarPanel);

                //Definindo os custos
                double[] costs = DefineCosts(solarPanel, invertor);
                //Calculo da energia produzida em um ano
                double anualGeneration = EstimateAnualGeneration(solarPanel, cityVec);

                GetEconomicInformation(anualGeneration, invertor, energyConsumed, costs[Constants.iCOSTS_TOTAL]);

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
                intent.putExtra(Constants.EXTRA_LUCRO, Ppayback);
                intent.putExtra(Constants.EXTRA_TAXA_DE_RETORNO, PInternalRateOfReturn);
                intent.putExtra(Constants.EXTRA_INDICE_LUCRATICVIDADE, PindiceLucratividade);
                intent.putExtra(Constants.EXTRA_LCOE, PLCOE);
                intent.putExtra(Constants.EXTRA_TEMPO_RETORNO, PTempoRetorno);
                intent.putExtra(Constants.EXTRA_HORA_SOLAR, solarHour);
                //Mudar
                startActivity(intent);


            }


        } catch (Exception e){
            Toast.makeText(this, R.string.informe_um_numero, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }



    @org.jetbrains.annotations.Contract(pure = true)
    public static double ValueWithoutTaxes(double costReais){
        return (costReais*(1-(Constants.ICMS + Constants.PIS + Constants.COFINS)));
    }

    public static double ConvertToKWh(double costReais){
        return costReais/Constants.TARIFF;
    }

    public static double MeanSolarHour(String[] cityVec){
        int i;
        double solarHour = 0.0;
        for(i=1; i<=12; i++){
            solarHour += Double.parseDouble(cityVec[i])/1000.0;
        }
        solarHour = solarHour/12.0;


        return solarHour;
    }

    public static double FindCapacity(double energyConsumed, double solarHour){
        energyConsumed -= Constants.COST_DISP; //O Custo de disponibilidade é o mínimo que alguém pode pagar
        // Ou seja, se a pessoa consumir 400KWh, e produzir 375KWh, ela irá pagar 50KWh à concessionária, se esse for o custo de disponibilidade.
        energyConsumed = (energyConsumed*1000)/30.0; //Energia consumida por dia
        energyConsumed = energyConsumed / (Constants.TD * solarHour); //TD é a constante de segurança
        return energyConsumed;
    }

    public static double DefineArea(String[] solarPanel){
        double area = Integer.parseInt(solarPanel[Constants.iPANEL_QTD]) * Double.parseDouble(solarPanel[Constants.iPANEL_AREA]);
        return area;
    }

    public static double[] DefineCosts(String[] solarPanel, String[] invertor){
        double[] costs = {0.0, 0.0};
        //Definido custo parcial
        costs[Constants.iCOSTS_PARCIAL] = Double.parseDouble(invertor[Constants.iINV_PRECO_TOTAL]) +
                                        Double.parseDouble(solarPanel[Constants.iPANEL_CUSTO_TOTAL]);
        //Definindo o custo total
        costs[Constants.iCOSTS_TOTAL] = costs[Constants.iCOSTS_PARCIAL]*(1 + Constants.PERCENTUAL_COST_INSTALATION);

        return costs;
    }

    public static double EstimateAnualGeneration(String[] solarPanel, String[] cityVec){
        //Consultar o manual de engenharia FV p. 149 a 153
        double anualGeneration=0.0, dailyGen=0.0, monthlyGen=0.0;
        int month;
        double tempAboveLimit;
        double correctionTemp;
        double efficiency, irradiance = 1000.0, ambientTemp;

        for(month=1; month<=12; month++){
            ambientTemp = Integer.parseInt(cityVec[13+month]);
            tempAboveLimit = ambientTemp + ((Integer.parseInt(solarPanel[Constants.PANEL_NOCT]) - 20)*0.00125*irradiance) - 25;
            correctionTemp = (tempAboveLimit * Double.parseDouble(solarPanel[Constants.iPANEL_COEFTEMP]) *
                                Double.parseDouble(solarPanel[Constants.iPANEL_POTENCIA])) / 100;
            efficiency = (Double.parseDouble(solarPanel[Constants.iPANEL_POTENCIA]) + correctionTemp)/(Double.parseDouble(solarPanel[Constants.iPANEL_AREA])*1000);

            dailyGen = (Double.parseDouble(cityVec[month])/1000.0) * efficiency *
                    Double.parseDouble(solarPanel[Constants.iPANEL_AREA]) * Double.parseDouble(solarPanel[Constants.iPANEL_QTD]);
            monthlyGen = dailyGen * GetNumberOfDays(month);
            anualGeneration += monthlyGen;
        }

        return anualGeneration;
    }

    public static int GetNumberOfDays(int month){
        switch (month){
            case 1:
                return 31;
            case 2:
                return 28;
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 12:
                return 31;
        }
        return 0;
    }

    public static void GetEconomicInformation(double anualGeneration, String[] invertor,
                                                  double energyConsumedMonthly, double custoTotalInstal){
        int year;
        anualGeneration = anualGeneration * (1 - Constants.LOSS_DIRT);
        double geracaoComDepreciacao, geracaoComAsPerdas, consumoMedioAnual=energyConsumedMonthly*12;
        double saldoDoConsumoCincoAnos=0, consumoAcimaDaGeracao=0, valorAPagar=0;
        double gastosTotais, ConsumoAPagarNoAno; //Quanto tem que pagar no ano
        double custoComImposto, custoAtualComImposto, diferençaDeCusto, custoInversor=0.0, custoManutEInstal=0.0;
        double[] saldoDoConsumoAnual = new double[30], cashFlow=new double[26];
        double cashFlowCurrentCurrency=0, payback = 0.0, tariff;
        double LCOEcost, LCOEGeneration, LCOEcrf=0.0, LCOE, LCOEDivisor;
        double LCOESumCost=0.0, LCOESumGeneration=0.0;
        PTempoRetorno = 30;
        for(year = 0; year < 26; year++) {
            tariff = Constants.TARIFF*Math.pow(1 + Constants.TARIFF_CHANGE/100.0, year);
            LCOEDivisor = Math.pow(1 + Constants.SELIC, year);

            geracaoComDepreciacao = anualGeneration * (1 - (Constants.DEPREC_PANELS) * year);
            geracaoComAsPerdas = geracaoComDepreciacao * Double.parseDouble(invertor[Constants.iINV_RENDIMENTO_MAXIMO]);
            LCOEGeneration = geracaoComAsPerdas/LCOEDivisor;
            LCOESumGeneration += LCOEGeneration;
            if (consumoMedioAnual < geracaoComAsPerdas) {
                saldoDoConsumoAnual[year] = 0.0;
            } else {
                saldoDoConsumoAnual[year] = consumoMedioAnual - geracaoComAsPerdas;
            }
            saldoDoConsumoCincoAnos += saldoDoConsumoAnual[year];
            if (year > 4) {
                saldoDoConsumoCincoAnos -= saldoDoConsumoAnual[year - 5];
            }
            if (consumoMedioAnual > geracaoComAsPerdas) {
                consumoAcimaDaGeracao = consumoMedioAnual - geracaoComAsPerdas;
            } else {
                consumoAcimaDaGeracao = 0.0;
            }

            if (consumoAcimaDaGeracao + saldoDoConsumoCincoAnos > 0.0) {
                ConsumoAPagarNoAno = consumoAcimaDaGeracao + saldoDoConsumoCincoAnos;
                saldoDoConsumoCincoAnos = 0;
            } else {
                ConsumoAPagarNoAno = 0;
                saldoDoConsumoCincoAnos += consumoAcimaDaGeracao;
            }

            if (ConsumoAPagarNoAno < 12.0 * Constants.COST_DISP) {
                ConsumoAPagarNoAno = 12.0 * Constants.COST_DISP;
            }
            valorAPagar = ConsumoAPagarNoAno * tariff;
            custoComImposto = valorAPagar / (1 - (Constants.PIS + Constants.COFINS + Constants.ICMS)) + Constants.CIP;
            custoAtualComImposto = (12.0 * energyConsumedMonthly)*tariff / (1 - (Constants.PIS + Constants.COFINS + Constants.ICMS)) + Constants.CIP;
            diferençaDeCusto = custoAtualComImposto - custoComImposto;

            if (year % Constants.INVERTOR_TIME == 0 && year > 0) {
                custoInversor = Double.parseDouble(invertor[Constants.iINV_PRECO]) *
                                    Math.pow(1 + Constants.IPCA, year) * Double.parseDouble(invertor[Constants.iINV_QTD]);
            } else {
                custoInversor = 0;
            }

            custoManutEInstal = (custoTotalInstal * Constants.MAINTENANCE_COST) * Math.pow(1 + (2 * Constants.IPCA), year) + custoInversor * 0.1;
            gastosTotais = custoManutEInstal + custoInversor;
            LCOEcost = gastosTotais/LCOEDivisor;
            LCOESumCost += LCOEcost;


            if (year == 0) {
                cashFlow[year] = -custoTotalInstal + diferençaDeCusto - gastosTotais;
            } else {
                cashFlow[year] = diferençaDeCusto - gastosTotais;
            }

            cashFlowCurrentCurrency = cashFlow[year] / Math.pow(1 + Constants.SELIC, year);
            payback+=cashFlowCurrentCurrency;
            if(PTempoRetorno == 30 && payback>=0){
                PTempoRetorno = year;
            }
        }
        Ppayback = payback;

        double indiceLucratividade = (payback+custoTotalInstal)/custoTotalInstal;
        PindiceLucratividade = indiceLucratividade;

        double internalRateOfReturn = IRR.getIRR(cashFlow);
        if(internalRateOfReturn > 1000000.0){
            PInternalRateOfReturn = 0.0;
        } else {
            PInternalRateOfReturn = internalRateOfReturn;
        }


        LCOEcrf = (Constants.COST_OF_CAPITAL * Math.pow(1 + Constants.COST_OF_CAPITAL, Constants.PANEL_LIFE)/
                (Math.pow(1 + Constants.COST_OF_CAPITAL, Constants.PANEL_LIFE) - 1))*custoTotalInstal;
        LCOE = (LCOESumCost + LCOEcrf)/LCOESumGeneration;
        PLCOE = LCOE;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(CalculoActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static class ViewHolder{
        TextView textSimulacao;
        EditText editCostMonth;
        Button buttonCalc;
        Spinner spinnerCities;
        ConstraintLayout layout;
    }
}
