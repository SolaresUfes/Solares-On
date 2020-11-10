package com.solares.calculadorasolar.classes;

public class CalculadoraOnGrid {
    //Variáveis da Classe
    String[] cityVec;
    String NomeCidade;
    String[] placaEscolhida;
    double custoReais;
    double consumokWh;
    double potenciaNecessaria;
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
    double horaSolar;
    double tarifaMensal;


    

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
    public static double EstimateAnualGeneration(String[] solarPanel, String[] stateVec, String[] cityVec){
        //Calculos retirados do manual de engenharia FV p. 149 a 153
        double anualGeneration=0.0, dailyGen, monthlyGen;
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
                    (Double.parseDouble(solarPanel[Constants.iPANEL_AREA])*1000); //O 1000 é a quantidade de W/m² que estamos considerando

            //Horas de sol pico do mês * eficiencia * area total
            dailyGen = (Double.parseDouble(cityVec[month])/1000.0) * efficiency *
                    Double.parseDouble(solarPanel[Constants.iPANEL_AREA]) * Double.parseDouble(solarPanel[Constants.iPANEL_QTD]);
            monthlyGen = dailyGen * GetNumberOfDays(month);
            anualGeneration += monthlyGen;
        }

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
