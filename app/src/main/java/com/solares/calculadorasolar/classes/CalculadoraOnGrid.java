package com.solares.calculadorasolar.classes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.activity.ResultadoActivity;

import java.io.InputStream;
import java.io.Serializable;

public class CalculadoraOnGrid implements Serializable {
    //Variáveis da Classe
    String[] vetorCidade;
    String[] vetorEstado;
    String nomeCidade;
    String[] placaEscolhida;
    double custoReais;
    double consumokWh;
    double potenciaNecessaria;
    double potenciaInstalada; //nPaineis * potenciaPainel
    double area;
    String[] inversor;
    double custoParcial;
    double custoTotal;
    double geracaoAnual;
    double lucro;
    double taxaRetornoInvestimento;
    double economiaMensal;
    double LCOE;
    int tempoRetorno;
    double horasDeSolPleno;
    double tarifaMensal;

    float areaAlvo;


    /* Descrição: Construtor do Objeto CalculadoraOnGrid
     * Parâmetros de Entrada: -;
     * Saída: -;
     * Pré Condições: -;
     * Pós Condições: O objeto foi construido com a área alvo como -1f;
     */
    public CalculadoraOnGrid(){
        this.areaAlvo = -1f;
    }

    //////////////////////////
    ////  Funções getters ////
    //////////////////////////
    public String[] pegaVetorCidade(){ return vetorCidade; }
    public String[] pegaVetorEstado(){ return vetorEstado; }
    public String pegaNomeCidade(){ return nomeCidade; }
    public String[] pegaPlacaEscolhida(){ return placaEscolhida; }
    public double pegaCustoReais(){ return custoReais; }
    public double pegaConsumokWhs(){ return consumokWh; }
    public double pegaPotenciaNecessaria(){ return potenciaNecessaria; }
    public double pegaArea(){ return area; }
    public String[] pegaInversor(){ return inversor; }
    public double pegaCustoParcial(){ return custoParcial; }
    public double pegaCustoTotal(){ return custoTotal; }
    public double pegaGeracaoAnual(){ return geracaoAnual; }
    public double pegaLucro(){ return lucro; }
    public double pegaTaxaRetornoInvestimento(){ return taxaRetornoInvestimento; }
    public double pegaEconomiaMensal(){ return economiaMensal; }
    public double pegaLCOE(){ return LCOE; }
    public int pegaTempoRetorno(){ return tempoRetorno; }
    public double pegaHorasDeSolPleno(){ return horasDeSolPleno; }
    public double pegaTarifaMensal(){ return tarifaMensal; }

    //////////////////////////
    ////  Funções setters ////
    //////////////////////////
    public void setVetorCidade(String[] vetorCidade){
        this.vetorCidade = vetorCidade;
    }
    public void setVetorEstado(String[] vetorEstado){
        this.vetorEstado = vetorEstado;
    }
    public void setTarifaMensal(double tarifa){
        this.tarifaMensal = tarifa;
        this.vetorEstado[Constants.iEST_TARIFA] = String.valueOf(tarifa);
    }
    public void setNomeCidade(String nomeCidade){
        this.nomeCidade = nomeCidade;
    }
    public void setCustoReais(double custoReais){
        this.custoReais = custoReais;
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////        Funções Principais       ////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* Descrição: Realiza todos os cálculos relazionados ao On Grid e iniciam o ResultadoActivity. Deve ser chamado com um objeto (calculadora.Calcular())
     * Parâmetros de Entrada: AreaAlvo - A área que terá o sistema final. Se AreaAlvo for -1, calcula a área ideal para a pessoa.
     * MyContext - Contexto da activity chamando o calcular.
     * Saída: -;
     * Pré Condições: O objeto deve ter seus campos de vetorCidade, vetorEstado, tarifaMensal, nomeCidade e custoReais preenchidos;
     * Pós Condições: O objeto tem seus valores altlerados, com os resultados do cálculo e passa o intent Serializable e inicia a ResultadoActivity;
     */
    public void Calcular(float AreaAlvo, Context MyContext) {
        InputStream is=null;

        if (AreaAlvo != -1){
            this.areaAlvo = AreaAlvo;
        }

        try {
            //Calcula a média anual da hora solar da cidade escolhida
            horasDeSolPleno = MeanSolarHour(vetorCidade);

            /////////////////double custoReais;

            //Calcula o consumo mensal em KWh
            double custoSemImpostos = ValueWithoutTaxes(custoReais);
            if(vetorEstado != null){
                consumokWh = ConvertToKWh(custoSemImpostos, vetorEstado);
            } else {
                throw new Exception("Estado não foi encontrado");
            }

            //Acha a potêcia necessária
            potenciaNecessaria = FindTargetCapacity(consumokWh, horasDeSolPleno);
            if(potenciaNecessaria < 0.0){
                try{
                    Toast.makeText(MyContext, "O seu consumo de energia é muito baixo!", Toast.LENGTH_LONG).show();
                } catch (Exception etoast){
                    etoast.printStackTrace();
                }
            } else {
                //Definindo as placas
                is = MyContext.getResources().openRawResource(R.raw.banco_paineis);
                placaEscolhida = CSVRead.DefineSolarPanel(is, potenciaNecessaria, this.areaAlvo);
                area = DefineArea(placaEscolhida);

                //Encontrando a potenciaInstalada
                this.potenciaInstalada = Double.parseDouble(placaEscolhida[Constants.iPANEL_POTENCIA]) * Double.parseDouble(placaEscolhida[Constants.iPANEL_QTD]);

                //Definindo os inversores
                is = MyContext.getResources().openRawResource(R.raw.banco_inversores);
                inversor = CSVRead.DefineInvertor(is, placaEscolhida);

                //Definindo os custos
                double[] custos = DefineCosts(placaEscolhida, inversor);
                custoParcial = custos[Constants.iCOSTS_PARCIAL];
                custoTotal = custos[Constants.iCOSTS_TOTAL];

                //Calculo da energia produzida em um ano
                geracaoAnual = EstimateAnualGeneration();

                GetEconomicInformation();

                //Preparação para mudar para próxima activity
                Intent intent = new Intent(MyContext, ResultadoActivity.class);

                //Fechar o InputStream
                try {
                    is.close();
                } catch (Exception e){
                    e.printStackTrace();
                }

                //Passar o objeto com as informações calculadas para a próxima Activity (Resultado)
                intent.putExtra(Constants.EXTRA_CALCULADORAON, this);

                //Mudar de activity
                MyContext.startActivity(intent);
            }
        } catch (Exception e){
            Log.i("Calculate", "Erro no Cálculo");
            //Se algum erro ocorrer, pede para o usuário informar um número real
            try {
                Toast.makeText(MyContext, R.string.informe_um_numero, Toast.LENGTH_LONG).show();
            } catch (Exception ee){
                ee.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            //Fechar o InputStream, se ocorreu algum erro
            try {
                if(is!=null){
                    is.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /* Descrição: Realiza os cálculos relacionados aos índices econômicos e altera as variáveis do objeto
     * Parâmetros de Entrada: -;
     * Saída: -;
     * Pré Condições: O objeto deve ter passado pela função Calcular, não é recomendado chamar essa função sozinha;
     * Pós Condições: O objeto tem seus valores altlerados, com os resultados do cálculo;
     * Fontes:
     * LCOE: Incorporating Performance-based Global Sensitivity and Uncertainty Analysis into LCOE Calculations for Emerging Renewable Energy Technologies -
Thomas T.D. Tran, Amanda D. Smith
     */
    public void GetEconomicInformation(){
        int year;
        double geracaoComDepreciacao, consumoMedioAnual=consumokWh*12, geracaoTotalVidaUtil=0.0;
        double saldoDoConsumoCincoAnos=0, consumoAcimaDaGeracao=0, valorAPagar=0;
        double gastosTotais, ConsumoAPagarNoAno; //Quanto tem que pagar no ano
        double custoComImposto, custoAtualComImposto, diferencaDeCusto, custoInversor=0.0, custoManutEInstal=0.0;
        double[] saldoDoConsumoAnual = new double[30], cashFlow=new double[25];
        double cashFlowCurrentCurrency=0, payback = 0.0, tariff;
        double LCOEcost, LCOEGeneration, LCOEDivisor;
        double LCOESumCost=0.0, LCOESumGeneration=0.0;
        tempoRetorno = 30;
        for(year = 0; year < 25; year++) { //Até 25 anos, que é a estimativa da vida útil dos paineis
            //Atualiza a tarifa com uma estimativa anual de incremento de preço (inflação tarifária)
            tariff = tarifaMensal*Math.pow(1 + Constants.TARIFF_CHANGE/100.0, year);

            //Um ajuste do Custo nivelado de energia
            LCOEDivisor = Math.pow(1 + Constants.SELIC, year);

            //Depreciação do painel a cada ano (diminuição de rendimento)
            geracaoComDepreciacao = this.geracaoAnual * (1 - (Constants.DEPREC_PANELS) * year);
            //Somando o total de energia produzida para calcular o custo de cada KWh
            LCOEGeneration = geracaoComDepreciacao/LCOEDivisor;
            LCOESumGeneration += LCOEGeneration;
            geracaoTotalVidaUtil += geracaoComDepreciacao;
            //Encontra quantos KWh o usuário ganhou de créditos esse ano (créditos têm o sinal negativo) ou se ele gastou mais do que produziu
            if (consumoMedioAnual < geracaoComDepreciacao) {
                //Se ele produziu mais do que gastou, aumenta os créditos
                saldoDoConsumoAnual[year] = consumoMedioAnual - geracaoComDepreciacao;
                consumoAcimaDaGeracao = 0.0;
            } else {
                //Se ele consumiu mais do que produziu, não ganha créditos e tem que pagar a diferença
                saldoDoConsumoAnual[year] = 0.0;
                consumoAcimaDaGeracao = consumoMedioAnual - geracaoComDepreciacao;
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
            custoComImposto = (valorAPagar / (1 - (Constants.PIS + Constants.COFINS + Constants.ICMS))) + Constants.CIP;
            //Quanto ele pagaria sem o sistema fotovoltaico, também com roubo (imposto)
            custoAtualComImposto = ((12.0 * consumokWh)*tariff / (1 - (Constants.PIS + Constants.COFINS + Constants.ICMS))) + Constants.CIP;
            //Acha a diferença que o sistema causou no custo (isso será o lucro ou a economia do ano)
            diferencaDeCusto = custoAtualComImposto - custoComImposto;
            if(year == 0){
                economiaMensal = diferencaDeCusto/12;
            }

            //Verifica se nesse ano deve-se trocar o inversor (de 10 em 10 anos)
            if (year % Constants.INVERTOR_TIME == 0 && year > 0) {
                custoInversor = Double.parseDouble(inversor[Constants.iINV_PRECO]) *
                        Math.pow(1 + Constants.IPCA, year) * Double.parseDouble(inversor[Constants.iINV_QTD]);
            } else {
                custoInversor = 0;
            }

            //Considerando que os custos de manutenção são relativos ao investimento inicial, que esses custos sobem em 2 vezes a inflação
            // e que o custo de instalação do inversor é um décimo de seu preço
            custoManutEInstal = (custoTotal * Constants.MAINTENANCE_COST) * Math.pow(1 + (2 * Constants.IPCA), year) + custoInversor * 0.1;
            gastosTotais = custoManutEInstal + custoInversor;
            //Esses são os custos coniderados no LCOE, os custos de energia (da concessionária) não entram no cálculo
            LCOEcost = gastosTotais/LCOEDivisor;
            LCOESumCost += LCOEcost;


            if (year == 0) { //Se for o primeiro ano, considera os custos de instalação
                //A economia (ou lucro) do sistema no ano foi:
                cashFlow[year] = -custoTotal + diferencaDeCusto - gastosTotais;
            } else {
                //Lembrando que a diferença de custo é o quanto o usuário economiza com a concessionária depois de instalar o sistema
                cashFlow[year] = diferencaDeCusto - gastosTotais;
            }

            //Diminui o valor economizado, trazendo para valores atuais
            cashFlowCurrentCurrency = cashFlow[year] / Math.pow(1 + Constants.SELIC, year);
            //O payback, que inicialmente é zero, armazena isso
            payback+=cashFlowCurrentCurrency;

            //Se ainda não foi definido tempo de retorno, e o payback for maior que zero, quer dizer que o investimento foi pago nesse ano
            if(tempoRetorno == 30 && payback>=0){
                //Como year é um índice de um vetor, temos que adicionar 1 para se ter o valor correto
                tempoRetorno = year+1;
            }
        }
        //Colocando os valores encontrados em variáveis públicas
        this.lucro = payback;

        //Se quiser o indice de lucratividade
        //double indiceDeLucratividade = (payback+custoTotalInstal)/custoTotalInstal;

        //Usei uma função que achei na internet para calcular o irr. Não tenho ideia de como funciona, mas funciona
        double internalRateOfReturn = IRR.getIRR(cashFlow);
        //Se o payback for negativo, o irr dá um valor extremamente alto, então eu boto zero
        if(internalRateOfReturn > 1000000.0){
            this.taxaRetornoInvestimento = 0.0;
        } else {
            this.taxaRetornoInvestimento = internalRateOfReturn;
        }

        /////////////Bora calcular o LCOE!!
        //Achar o capacity factor da usina (porcentagem de energia real gerada em relação à produção nominal da usina)
        //Geração real dividido pela geração máxima (capacidade * horas em um dia * dias em um ano * anos de operação)
        double capacityFactor = geracaoTotalVidaUtil * 1000 /(potenciaInstalada * 24 * 365 * Constants.PANEL_LIFE);
        //Encontrar o Overnight Capital Cost (R$/kW) o investimento inicial por kW
        double overnightCapitalCost = this.custoTotal * 1000 / (potenciaInstalada); //Multiplicando por 1000 para encontrar por kW, em vez de por W
        //Encontrar o CRF (Capital Recovery Factor)
        double LCOEcrf = (Constants.COST_OF_CAPITAL * Math.pow(1 + Constants.COST_OF_CAPITAL, Constants.PANEL_LIFE)/
                (Math.pow(1 + Constants.COST_OF_CAPITAL, Constants.PANEL_LIFE) - 1));
        //Encontrar o custo de manutenção por kW
        double fizxedOnM = LCOESumCost * 1000 / (potenciaInstalada * 365); //Multiplicando por 1000 para encontrar por kW-ano, em vez de por W-ano

        //Faz o cálculo do LCOE de acordo com a referência (Documentação deste método)
        LCOE = (overnightCapitalCost*LCOEcrf + fizxedOnM) /
                (24*365*capacityFactor);

        double LCOE2 = (custoTotal + LCOESumCost)/LCOESumGeneration;
        double LCOE3 = (custoTotal + LCOESumCost)/geracaoTotalVidaUtil;
        int i = 1;
    }




    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////        Funções Auxiliares       ////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /* Descrição: Recebe a conta de energia em reais e retorna o valor, também em reais, retirando os impostos
     * Parâmetros de Entrada: costReais - Valor em reais da conta de luz média mensal, com impostos
     * Saída: Valor sem impostos - costReais retirando o CIP, ICMS, PIS e COFINS
     * Pré Condições: costReais é não nulo e contém os impostos
     * Pós Condições: Retorna o valor sem impostos
     */
    @org.jetbrains.annotations.Contract(pure = true)
    public static double ValueWithoutTaxes(double costReais){
        return (costReais - (Constants.CIP))*(1 - Constants.ICMS - Constants.PIS - Constants.COFINS);
    }


    /* Descrição: Converte a conta de energia de reais para kWh
     * Parâmetros de Entrada: costReais - Valor double em reais da conta de luz média mensal, sem impostos;
stateVec - Vetor de Strings com as informações do estado escolhido
     * Saída: Valor de energia em kWh mensal - costReais dividido pela tarifa de energia
     * Pré Condições: costReais é não nulo e não contém os impostos; stateVec - não é nulo e contém as informações do estado
     * Pós Condições: Retorna o valor do consumo de energia mensal
     */
    public static double ConvertToKWh(double costReais, String[] stateVec){
        return costReais/Double.parseDouble(stateVec[Constants.iEST_TARIFA]);
    }


    /* Descrição: Retorna as Horas de Sol Pleno por dia média no ano
     * Parâmetros de Entrada: cityVec - Vetor de Strings com as informações da cidade
     * Saída: Valor médio do HSP diário. Considera as médias mensais para realizar a média anual
     * Pré Condições: cityVec - não é nulo e contém as informações de irradiação da cidade (Wh/m²dia)
     * Pós Condições: Retorna o valor do HSP médio anual (horas ou kWh/m²dia)
     */
    public static double MeanSolarHour(String[] cityVec){
        int i;
        double solarHour = 0.0;
        for(i=1; i<=12; i++){
            solarHour += Double.parseDouble(cityVec[i])/1000.0;
        }
        solarHour = solarHour/12.0;

        return solarHour;
    }


    /* Descrição: Encontra a potência necessária do sistema de módulos fotovoltaicos. Essa potência será o alvo para o dimensionamento dos módulos.
     * Parâmetros de Entrada: energyConsumed - O consumo mensal de energia elétrica (kWh); solarHour - Horas de sol pleno média da cidade escolhida (horas ou kWh/m²dia)
     * Saída: capacidade de geração alvo que o sistema deve ter (Wp), ou 0 se o energyConsumed for menor que o custo de disponibilidade
     * Pré Condições: energyConsumed - é não nulo e positivo. Pode ser menor que o custo de disponibilidade (retorno 0);
solarHour - Está em horas (maior que zero)
     * Pós Condições: Retorna o valor da potência necessária em Wp
     */
    public static double FindTargetCapacity(double energyConsumed, double solarHour){
        energyConsumed -= Constants.COST_DISP; //O Custo de disponibilidade é o mínimo que alguém pode pagar
        // Ou seja, se a pessoa consumir 400KWh, e produzir 375KWh, ela irá pagar 50KWh à concessionária, se esse for o custo de disponibilidade.
        if(energyConsumed < 0.0){ //Verifica se o consumo é menor que o custo de disponibilidade
            return 0;
        }
        energyConsumed = (energyConsumed*1000)/30.0; //Energia consumida por dia
        energyConsumed = energyConsumed / (Constants.TD * solarHour); //TD é a constante de segurança
        return energyConsumed;
    }


    /* Descrição: Retorna a área do sistema de módulos que foi dimensionado
     * Parâmetros de Entrada: solarPanel - Vetor de Strings com as informações do sistema de módulos dimensionado.
     * Saída: Área total do sistema de módulos (m²)
     * Pré Condições: solarPanel - Vetor não nulo com as informações do sistema
     * Pós Condições: Retorna o valor da área
     */
    public static double DefineArea(String[] solarPanel){
        return Integer.parseInt(solarPanel[Constants.iPANEL_QTD]) * Double.parseDouble(solarPanel[Constants.iPANEL_AREA]);
    }


    /* Descrição: Retorna os custos (vetor de double) do sistema dimensionado, tanto o custo parcial quanto o custo total.
     * Parâmetros de Entrada: solarPanel - Vetor de Strings com as informações do sistema de módulos dimensionado.
invertor - Vetor de Strings com as informações do sistema de inversores dimensionado
     * Saída: Vetor de double de duas posições com os custos (Índice Custo parcial: 0, Índice Custo total: 1).
O custo parcial é apenas o custos dos módulos e dos inversores. O custo total leva em conta custos com projeto, mão de obra, proteção, fiação, estrutura, entre outros.
     * Pré Condições: solarPanel e invertor - Vetores não nulos com as informações do sistema
     * Pós Condições: Retorna o vetor de custos
     */
    public static double[] DefineCosts(String[] solarPanel, String[] invertor){
        double[] costs = {0.0, 0.0};
        //Definido custo parcial
        costs[Constants.iCOSTS_PARCIAL] = Double.parseDouble(invertor[Constants.iINV_PRECO_TOTAL]) +
                Double.parseDouble(solarPanel[Constants.iPANEL_CUSTO_TOTAL]);
        //Definindo o custo total
        costs[Constants.iCOSTS_TOTAL] = costs[Constants.iCOSTS_PARCIAL]*(1 + Constants.PERCENTUAL_COST_INSTALATION);

        return costs;
    }


    /* Descrição: Faz o cálculo da geração anual de energia com o sistema definido.
     * Parâmetros de Entrada: solarPanel - Vetor de Strings com as informações do sistema de módulos dimensionado;
stateVec - Vetor de Strings com as informações do Estado (temperatura); cityVec - Vetor de strings com as informações da cidade (Horas de Sol Pleno)
     * Saída: Estimativa da energia gerada em um ano (kWh)
     * Pré Condições: solarPanel, stateVec e cityVec - Vetores não nulos com as informações do sistema e do local
     * Pós Condições: Retorna o valor da energia gerada
     */
    public double EstimateAnualGeneration(){
        //Calculos retirados do manual de engenharia FV p. 149 a 153
        double anualGeneration=0.0, dailyGen, monthlyGen;
        int month;
        double tempAboveLimit;
        double correctionTemp;
        double efficiency, irradiance = 1000.0, ambientTemp;

        for(month=1; month<=12; month++){
            //Pega a temperatura média do estado no mês
            ambientTemp = Double.parseDouble(vetorEstado[month]);
            //Temperatura estimada do módulo acima de 25°C (se a temperatura do módulo for 50°C, tempAboveLimit será 25)
            tempAboveLimit = ambientTemp + ((Integer.parseInt(placaEscolhida[Constants.PANEL_NOCT]) - 20)*0.00125*irradiance) - 25;
            //Esse valor será negativo devido ao coeficiente de temperatura
            correctionTemp = (tempAboveLimit * Double.parseDouble(placaEscolhida[Constants.iPANEL_COEFTEMP]) *
                    Double.parseDouble(placaEscolhida[Constants.iPANEL_POTENCIA])) / 100;
            //Eficiencia do sistema (qual a porcentagem de energia captada em 1m²)
            efficiency = (Double.parseDouble(placaEscolhida[Constants.iPANEL_POTENCIA]) + correctionTemp)/
                    (Double.parseDouble(placaEscolhida[Constants.iPANEL_AREA])*1000); //O 1000 é a quantidade de W/m² que estamos considerando

            //Horas de sol pico do mês * eficiencia * area total
            dailyGen = (Double.parseDouble(vetorCidade[month])/1000.0) * efficiency *
                    Double.parseDouble(placaEscolhida[Constants.iPANEL_AREA]) * Double.parseDouble(placaEscolhida[Constants.iPANEL_QTD]);
            monthlyGen = dailyGen * GetNumberOfDays(month);
            anualGeneration += monthlyGen;
        }
        //Considera perdas por sujeira
        anualGeneration = anualGeneration * (1 - Constants.LOSS_DIRT);
        //Considera perdas com o inversor
        anualGeneration = anualGeneration * Double.parseDouble(inversor[Constants.iINV_RENDIMENTO_MAXIMO]);

        return anualGeneration;
    }


    /* Descrição: Retorna o número de dias do mês escolhido
     * Parâmetros de Entrada: month - Inteiro que representa o mês (1 - janeiro, ..., 12 - dezembro)
     * Saída: Número de dias do mês. Se month não for nenhum dos meses, retorna 0. Fevereiro tem 28 dias.
     * Pré Condições: month - inteiro entre 1 e 12
     * Pós Condições: Retorna o número de dias no mês
     */
    public static int GetNumberOfDays(int month){
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                return 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
        }
        return 0;
    }
}
