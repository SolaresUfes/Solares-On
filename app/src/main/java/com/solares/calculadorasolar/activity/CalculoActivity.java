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
import android.widget.AdapterView;
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

        //Pegando informações sobre o dispositivo, para regular o tamanho da letra (fonte)
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        larguraTela = size.x;
        alturaTela = size.y;

        //Identificando os componentes do layout
        this.mViewHolder.textSimulacao = findViewById(R.id.text_simulacao);
        AutoSizeText.AutoSizeTextView(this.mViewHolder.textSimulacao, alturaTela, larguraTela, 4f);
        this.mViewHolder.buttonCalc = findViewById(R.id.button_calc);
        AutoSizeText.AutoSizeButton(this.mViewHolder.buttonCalc, alturaTela, larguraTela, porcent);
        this.mViewHolder.editCostMonth = findViewById(R.id.edit_cost);
        AutoSizeText.AutoSizeEditText(this.mViewHolder.editCostMonth, alturaTela, larguraTela, porcent-2);

        //Criando spinners (dos estados e das cidades)
        this.mViewHolder.spinnerStates = findViewById(R.id.spinner_states);
        //Aqui, coloca o vetor de strings que será exibido no spinner
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this, R.array.Estados, R.layout.spinner_item);
        this.mViewHolder.spinnerStates.setAdapter(adapterS);
        this.mViewHolder.spinnerStates.setSelection(7);

        this.mViewHolder.spinnerCities = findViewById(R.id.spinner_cities);
        ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this, R.array.ES, R.layout.spinner_item);
        this.mViewHolder.spinnerCities.setAdapter(adapterC);
        this.mViewHolder.spinnerCities.setSelection(33);

        this.mViewHolder.layout = findViewById(R.id.layout_calculo);

        //Isso indica para não se preocupar com a área no primeiro cálculo
        final float AreaAlvo = -1f;

        //Se o spinner de estado for selecionado, muda o spinner de cidades de acordo
        this.mViewHolder.spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Muda o spinner de cidades para o estado correspondente
                ChangeSpinner(position);
            }

            //O ide reclama se eu tirar esse metodo, então deixei ele aí, mas sem nada
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Se o usuário clicar no fundo do app, o teclado se fecha
        this.mViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mViewHolder.editCostMonth);
            }
        });

        //Se o usuário clicar no botão calcular, o cálculo é feito e muda-se para a próxima activity
        this.mViewHolder.buttonCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculate(AreaAlvo);
            }
        });

    }


    /*
     * Esse método faz todos os cálculos, passando para a proxima activity os resultados
     */
    public void Calculate(float AreaAlvo) {
        int idCity;
        String city;

        try {
            //Fecha teclado
            mViewHolder.editCostMonth.onEditorAction(EditorInfo.IME_ACTION_DONE);
            //Identifica a cidade escolhida e pega suas informações
            String stateName = this.mViewHolder.spinnerStates.getSelectedItem().toString();
            idCity = this.mViewHolder.spinnerCities.getSelectedItemPosition();
            city = this.mViewHolder.spinnerCities.getItemAtPosition(idCity).toString();
            InputStream is = getResources().openRawResource(R.raw.banco_irradiancia);
            String[] cityVec = CSVRead.getCity(idCity, stateName, is);

            //Calcula a média anual da hora solar da cidade escolhida
            double solarHour = MeanSolarHour(cityVec);
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
                //Pegando as informações do estado
                is = getResources().openRawResource(R.raw.banco_estados);
                String[] stateVec;
                if(cityVec != null){
                    stateVec = CSVRead.getState(cityVec, is);
                } else {
                    throw new Exception("Cidade não foi encontrada");
                }
                //Calculo da energia produzida em um ano
                double anualGeneration = EstimateAnualGeneration(solarPanel, stateVec, cityVec);

                GetEconomicInformation(anualGeneration, invertor, energyConsumed, costs[Constants.iCOSTS_TOTAL]);

                //Preparação para mudar para próxima activity
                Intent intent = new Intent(this, ResultadoActivity.class);
                //Esses extras são usados para se enviar informações entre activities
                intent.putExtra(Constants.EXTRA_VETOR_CIDADE, cityVec);
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
                //Mudar de activity
                startActivity(intent);
            }
        } catch (Exception e){
            //Se algum erro ocorrer, pede para o usuário informar um número real
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

    public static double EstimateAnualGeneration(String[] solarPanel, String[] stateVec, String[] cityVec){
        //Calculos retirados do manual de engenharia FV p. 149 a 153
        double anualGeneration=0.0, dailyGen=0.0, monthlyGen=0.0;
        int month;
        double tempAboveLimit;
        double correctionTemp;
        double efficiency, irradiance = 1000.0, ambientTemp;

        for(month=1; month<=12; month++){
            //Pega a temperatura média do estado no mês
            ambientTemp = Double.parseDouble(stateVec[month]);
            //Temperatura estimada do módulo acima de 25°C (se a temperatura do módulo for 50°C, tempAboveLimit será 25)
            tempAboveLimit = ambientTemp + ((Integer.parseInt(solarPanel[Constants.PANEL_NOCT]) - 20)*0.00125*irradiance) - 25;
            //Esse valor será negativo devido ao coeficiente de temperatura
            correctionTemp = (tempAboveLimit * Double.parseDouble(solarPanel[Constants.iPANEL_COEFTEMP]) *
                                Double.parseDouble(solarPanel[Constants.iPANEL_POTENCIA])) / 100;
            //Eficiencia do sistema (qual a porcentagem de energia captada em 1m²)
            efficiency = (Double.parseDouble(solarPanel[Constants.iPANEL_POTENCIA]) + correctionTemp)/
                        (Double.parseDouble(solarPanel[Constants.iPANEL_AREA])*1000);

            //Horas de sol pico do mês * eficiencia * area total
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
        for(year = 0; year <= 25; year++) { //Até 25 anos, que é a estimativa da vida útil dos paineis
            tariff = Constants.TARIFF*Math.pow(1 + Constants.TARIFF_CHANGE/100.0, year);
            LCOEDivisor = Math.pow(1 + Constants.SELIC, year);

            //Depreciação do painel a cada ano (diminuição de rendimento)
            geracaoComDepreciacao = anualGeneration * (1 - (Constants.DEPREC_PANELS) * year);
            //Perdas com o inversor
            geracaoComAsPerdas = geracaoComDepreciacao * Double.parseDouble(invertor[Constants.iINV_RENDIMENTO_MAXIMO]);
            //Somando o total de energia produzida para calcular o custo de cada KWh
            LCOEGeneration = geracaoComAsPerdas/LCOEDivisor;
            LCOESumGeneration += LCOEGeneration;
            //Encontra quantos KWh o usuário ganhou de créditos esse ano (créditos têm o sinal negativo) ou se ele gastou mais do que produziu
            if (consumoMedioAnual < geracaoComAsPerdas) {
                //Se ele produziu mais do que gastou, aumenta os créditos
                saldoDoConsumoAnual[year] = consumoMedioAnual - geracaoComAsPerdas;
                consumoAcimaDaGeracao = 0.0;
            } else {
                //Se ele consumiu mais do que produziu, não ganha créditos e tem que pagar a diferença
                saldoDoConsumoAnual[year] = 0.0;
                consumoAcimaDaGeracao = consumoMedioAnual - geracaoComAsPerdas;
            }

            //Os créditos duram apenas por cinco anos, então, se não foram usados, descarta o mais antigo
            saldoDoConsumoCincoAnos += saldoDoConsumoAnual[year];
            if (year > 4) {
                saldoDoConsumoCincoAnos -= saldoDoConsumoAnual[year - 5];
            }

            //Se o consumo que ele tem que pagar for maior do que os créditos que ele tem...
            if (consumoAcimaDaGeracao + saldoDoConsumoCincoAnos > 0.0) {
                //Verifica de fato quanto ele deve pagar, descontando os créditos ainda restantes
                ConsumoAPagarNoAno = consumoAcimaDaGeracao + saldoDoConsumoCincoAnos;
                //Zera os créditos
                saldoDoConsumoCincoAnos = 0;
            } else {
                //Se os créditos suprirem a demanda, ele não paga "nada"
                ConsumoAPagarNoAno = 0;
                saldoDoConsumoCincoAnos += consumoAcimaDaGeracao;
            }

            //Se o que ele deve pagar for menor do que o custo de disponibilidade, ele paga o custo de disponibilidade
            if (ConsumoAPagarNoAno < 12.0 * Constants.COST_DISP) {
                ConsumoAPagarNoAno = 12.0 * Constants.COST_DISP;
            }
            //Acha o valor em reais que ele pagará para a concessionária no ano
            valorAPagar = ConsumoAPagarNoAno * tariff;
            //Coloca os roubos... Quero dizer, impostos
            custoComImposto = valorAPagar / (1 - (Constants.PIS + Constants.COFINS + Constants.ICMS)) + Constants.CIP;
            //Quanto ele pagaria sem o sistema fotovoltaico, também com roubo (imposto)
            custoAtualComImposto = (12.0 * energyConsumedMonthly)*tariff / (1 - (Constants.PIS + Constants.COFINS + Constants.ICMS)) + Constants.CIP;
            //Acha a diferença que o sistema causou no custo (isso será o lucro ou a economia do ano)
            diferençaDeCusto = custoAtualComImposto - custoComImposto;

            //Verifica se nesse ano deve-se trocar o inversor (de 10 em 10 anos)
            if (year % Constants.INVERTOR_TIME == 0 && year > 0) {
                custoInversor = Double.parseDouble(invertor[Constants.iINV_PRECO]) *
                                    Math.pow(1 + Constants.IPCA, year) * Double.parseDouble(invertor[Constants.iINV_QTD]);
            } else {
                custoInversor = 0;
            }

            //Considerando que os custos de manutenção são relativos ao investimento inicial, que esses custos sobem em 2 vezes a inflação
            // e que o custo de instalação do inversor é um décimo de seu preço
            custoManutEInstal = (custoTotalInstal * Constants.MAINTENANCE_COST) * Math.pow(1 + (2 * Constants.IPCA), year) + custoInversor * 0.1;
            gastosTotais = custoManutEInstal + custoInversor;
            //Esses são os custos coniderados no LCOE, os custos de energia (da concessionária) não entram no cálculo
            LCOEcost = gastosTotais/LCOEDivisor;
            LCOESumCost += LCOEcost;


            if (year == 0) { //Se for o primeiro ano, considera os custos de instalação
                //A economia (ou lucro) do sistema no ano foi:
                cashFlow[year] = -custoTotalInstal + diferençaDeCusto - gastosTotais;
            } else {
                //Lembrando que a diferença de custo é o quanto o usuário economiza com a concessionária depois de instalar o sistema
                cashFlow[year] = diferençaDeCusto - gastosTotais;
            }

            //Diminui o valor economizado, trazendo para valores atuais
            cashFlowCurrentCurrency = cashFlow[year] / Math.pow(1 + Constants.SELIC, year);
            //O payback, que inicialmente é zero, armazena isso
            payback+=cashFlowCurrentCurrency;

            //Se ainda não foi definido tempo de retorno, e o payback for maior que zero, quer dizer que o investimento foi pago nesse ano
            if(PTempoRetorno == 30 && payback>=0){
                PTempoRetorno = year;
            }
        }
        //Colocando os valores encontrados em variáveis públicas
        Ppayback = payback;

        PindiceLucratividade = (payback+custoTotalInstal)/custoTotalInstal;

        //Usei uma função que achei na internet para calcular o irr. Não tenho ideia de como funciona, mas funciona
        double internalRateOfReturn = IRR.getIRR(cashFlow);
        //Se o payback for negativo, o irr dá um valor extremamente alto, então eu boto zero
        if(internalRateOfReturn > 1000000.0){
            PInternalRateOfReturn = 0.0;
        } else {
            PInternalRateOfReturn = internalRateOfReturn;
        }


        LCOEcrf = (Constants.COST_OF_CAPITAL * Math.pow(1 + Constants.COST_OF_CAPITAL, Constants.PANEL_LIFE)/
                (Math.pow(1 + Constants.COST_OF_CAPITAL, Constants.PANEL_LIFE) - 1))*custoTotalInstal;
        //Divide os todos os custos por toda energia produzida
        LCOE = (LCOESumCost + LCOEcrf)/LCOESumGeneration;
        PLCOE = LCOE;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(CalculoActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void ChangeSpinner(int statePos){
        int[] statesPositions = {R.array.AC, R.array.AL, R.array.AP, R.array.AM, R.array.BA, R.array.CE, R.array.DF,
                R.array.ES, R.array.GO, R.array.MA, R.array.MT, R.array.MS, R.array.MG, R.array.PA, R.array.PB,
                R.array.PR, R.array.PE, R.array.PI, R.array.RJ, R.array.RN, R.array.RS, R.array.RO, R.array.RR,
                R.array.SC, R.array.SP, R.array.SE, R.array.TO};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, statesPositions[statePos], R.layout.spinner_item);
        this.mViewHolder.spinnerCities.setAdapter(adapter);
    }

    public static class ViewHolder{
        TextView textSimulacao;
        EditText editCostMonth;
        Button buttonCalc;
        Spinner spinnerCities;
        Spinner spinnerStates;
        ConstraintLayout layout;
    }
}
