package com.solares.calculadorasolar.classes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.activity.ResultadoActivity;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.auxiliares.IRR;
import com.solares.calculadorasolar.classes.entidades.Bateria_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Controlador_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Empresa;
import com.solares.calculadorasolar.classes.entidades.Inversor;
import com.solares.calculadorasolar.classes.entidades.Inversor_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Painel;
import com.solares.calculadorasolar.classes.entidades.Painel_OffGrid;

import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Locale;

public class CalculadoraOnGrid implements Serializable {
    //Variáveis da Classe
    LinkedList<Empresa> listaEmpresas;
    String[] vetorCidade;
    String[] vetorEstado;
    LinkedList<Painel> listaPaineis;
    String[] nomesPaineis;
    LinkedList<Inversor> listaInversores;
    String[] nomesInversores;
    String nomeCidade;
    Painel placaEscolhida;
    double custoReais;
    double consumokWh;
    double potenciaNecessaria;
    double potenciaInstalada; //nPaineis * potenciaPainel
    double area;
    Inversor inversor;
    double custoParcial;
    double custoTotal;
    double geracaoAnual;
    double lucro;
    double taxaInternaRetorno;
    double economiaMensal;
    double LCOE;
    int tempoRetorno;
    double horasDeSolPleno;
    double tarifaMensal;
    int numeroDeFases;
    float areaAlvo;
    int idModuloEscolhido;
    int idInversorEscolhido;
    double custoDisponibilidade;

    double consumo;
    boolean modoCalculoPorDinheiro;


    /* Descrição: Construtor do Objeto CalculadoraOnGrid
     * Parâmetros de Entrada: -;
     * Saída: -;
     * Pré Condições: -;
     * Pós Condições: O objeto foi construido com a área alvo como -1f e sem nenhum módulo ou inversor definido;
     */
    public CalculadoraOnGrid(){
        this.areaAlvo = -1f;
        this.idModuloEscolhido = -1;
        this.idInversorEscolhido = -1;
    }

    //////////////////////////
    ////  Funções getters ////
    //////////////////////////
    public String[] pegaVetorCidade(){ return vetorCidade; }
    public String[] pegaVetorEstado(){ return vetorEstado; }
    public LinkedList<Empresa> pegaListaEmpresa() { return listaEmpresas; }
    public LinkedList<Painel> pegaListaPaineis() { return listaPaineis; }
    public String[] pegaNomesPaineis() { return nomesPaineis; }
    public LinkedList<Inversor> pegaListaInversores() { return listaInversores; }
    public String[] pegaNomesInversores() { return nomesInversores; }
    public String pegaNomeCidade(){ return nomeCidade; }
    public Painel pegaPlacaEscolhida(){ return placaEscolhida; }
    public double pegaCustoReais(){ return custoReais; }
    public double pegaConsumokWhs(){ return consumokWh; }
    public double pegaPotenciaNecessaria(){ return potenciaNecessaria; }
    public double pegaPotenciaInstalada(){ return potenciaInstalada; }
    public double pegaArea(){ return area; }
    public Inversor pegaInversor(){ return inversor; }
    public double pegaCustoParcial(){ return custoParcial; }
    public double pegaCustoTotal(){ return custoTotal; }
    public double pegaGeracaoAnual(){ return geracaoAnual; }
    public double pegaLucro(){ return lucro; }
    public double pegaTaxaInternaRetorno(){ return taxaInternaRetorno; }
    public double pegaEconomiaMensal(){ return economiaMensal; }
    public double pegaLCOE(){ return LCOE; }
    public int pegaTempoRetorno(){ return tempoRetorno; }
    public double pegaHorasDeSolPleno(){ return horasDeSolPleno; }
    public double pegaTarifaMensal(){ return tarifaMensal; }
    public int pegaNumeroDeFases(){ return numeroDeFases; }
    public double pegaCustoDisponibilidade(){ return custoDisponibilidade; }

    //////////////////////////
    ////  Funções setters ////
    //////////////////////////
    public void setVetorCidade(String[] vetorCidade){
        this.vetorCidade = vetorCidade;
    }
    public void setVetorEstado(String[] vetorEstado){
        this.vetorEstado = vetorEstado;
    }
    public void setListaPaineis(LinkedList<Painel> listaPaineis) { this.listaPaineis = listaPaineis; }
    public void setListaInversores(LinkedList<Inversor> listaInversores) { this.listaInversores = listaInversores; }
    public void setListaEmpresas(LinkedList<Empresa> listaEmpresas) { this.listaEmpresas = listaEmpresas; }
    public void setTarifaMensal(double tarifa){
        this.tarifaMensal = tarifa;
    }
    public void setNomeCidade(String nomeCidade){
        this.nomeCidade = nomeCidade;
    }
    public void setConsumo(double consumo) { this.consumo = consumo; }
    public void setModoCalculoPorDinheiro(boolean modoCalculoPorDinheiro) { this.modoCalculoPorDinheiro = modoCalculoPorDinheiro; }
    public void setCustoReais(double custoReais){
        this.custoReais = custoReais;
    }
    public void setConsumokWh(double consumokWh) { this.consumokWh = consumokWh; }
    public void setAreaAlvo(float novaAreaAlvo) { this.areaAlvo = novaAreaAlvo; }
    public void setNumeroDeFases(int novoNumeroDeFases) {
        this.numeroDeFases = novoNumeroDeFases;
        switch (novoNumeroDeFases){
            case 0:
                this.custoDisponibilidade = Constants.COST_DISP_MONOFASICO;
                break;
            case 1:
                this.custoDisponibilidade = Constants.COST_DISP_BIFASICO;
                break;
            case 2:
                this.custoDisponibilidade = Constants.COST_DISP_TRIFASICO;
                break;
            default:
                this.custoDisponibilidade = Constants.COST_DISP_BIFASICO;
        }
    }

    //idModuloEscolhido - Inteiro que representa um modelo de módulo escolhido pelo usuário. Se for -1, escolhe o melhor
    public void setIdModuloEscolhido(int novoIdModuloEscolhido) { this.idModuloEscolhido = novoIdModuloEscolhido; }
    //idInversoreEscolhido - Inteiro que representa um modelo de inversor escolhido pelo usuário. Se for -1, escolhe o melhor
    public void setIdInversorEscolhido(int novoIdInversorEscolhido) { this.idInversorEscolhido = novoIdInversorEscolhido; }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////        Funções Principais       ////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* Descrição: Realiza todos os cálculos relazionados ao On Grid e iniciam o ResultadoActivity. Deve ser chamado com um objeto (calculadora.Calcular())
     * Parâmetros de Entrada: MyContext - Contexto da activity chamando o calcular.
     * Saída: -;
     * Pré Condições: O objeto deve ter seus campos de vetorCidade, vetorEstado, tarifaMensal, nomeCidade e custoReais preenchidos;
     * Pós Condições: O objeto tem seus valores alterados, com os resultados do cálculo e passa o intent Serializable e inicia a ResultadoActivity;
     */
    public void Calcular(Context MyContext) {
        InputStream is=null;

        if(this.pegaListaInversores().size() <= 0){
            Log.i("Calculate", "Falha na busca por inversores");
        }
        if(this.pegaListaPaineis().size() <= 0){
            Log.i("Calculate", "Falha na busca por paineis");
        }

        try {
            //Calcula os consumos em kWh e em reais
            if(modoCalculoPorDinheiro){
                //Calcula o consumo mensal em KWh
                double custoSemImpostos = CalculadoraOnGrid.ValueWithoutTaxes(consumo);
                this.setConsumokWh(CalculadoraOnGrid.ConvertToKWh(custoSemImpostos, this.pegaTarifaMensal()));
                this.setCustoReais(consumo);
            } else {
                //Calcula o consumo mensal em reais
                this.setConsumokWh(consumo);
                this.setCustoReais(ValueWithTaxes(consumo*this.pegaTarifaMensal()));
            }


            //Calcula a média anual da hora solar da cidade escolhida
            horasDeSolPleno = MeanSolarHour(vetorCidade);

            //Pega os nomes dos inversores
            int cont=0;
            nomesInversores = new String[this.pegaListaInversores().size()];
            for (Inversor inversorAtual : this.pegaListaInversores()) {
                nomesInversores[cont] = inversorAtual.getMarca() + " " + inversorAtual.getCodigo() + " - " + String.format(Locale.ITALY, "%.1f", inversorAtual.getPotencia()/1000f) + "kW";
                cont++;
            }

            //Acha a potêcia necessária
            potenciaNecessaria = FindTargetCapacity(consumokWh, horasDeSolPleno);
            int idMelhorModulo=-1;
            cont=0;
            double melhorLucro=0.0;
            nomesPaineis = new String[this.pegaListaPaineis().size()];
            if(this.idModuloEscolhido == -1){
                for (Painel painelAtual : this.pegaListaPaineis()) {
                    nomesPaineis[cont] = painelAtual.getMarca() + " " + painelAtual.getCodigo() + " " +  String.format(Locale.ITALY, "%.0f", painelAtual.getPotencia())+ "W";
                    calculaResultadosPlaca(MyContext, painelAtual);
                    if (cont == 0) {
                        idMelhorModulo = cont;
                        melhorLucro = this.pegaLucro();
                    }
                    if (this.pegaLucro() > melhorLucro) { // Definir melhor placa em relação à anterior
                        idMelhorModulo = cont;
                        melhorLucro = this.pegaLucro();
                    }

                    cont++;
                }

                calculaResultadosPlaca(MyContext, listaPaineis.get(idMelhorModulo));
            }else{
                for (Painel painelAtual : this.pegaListaPaineis()) {
                    nomesPaineis[cont] = painelAtual.getMarca() + " " + painelAtual.getCodigo() + " " +painelAtual.getPotencia() + "W";
                    cont++;
                }
                calculaResultadosPlaca(MyContext, listaPaineis.get(this.idModuloEscolhido));
            }

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
        } catch (Exception e){
            e.printStackTrace();
            Log.i("Calculate", "Erro no Cálculo");
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
     * LCOE (Antigo): Incorporating Performance-based Global Sensitivity and Uncertainty Analysis into LCOE Calculations for Emerging Renewable Energy Technologies -
Thomas T.D. Tran, Amanda D. Smith
     * Custos de mão de obra e afins: https://www.greener.com.br/estudo/estudo-estrategico-mercado-fotovoltaico-de-geracao-distribuida-2-semestre-de-2020/
     */
    public void GetEconomicInformation(){
        int year;
        double geracaoComDepreciacao, consumoMedioAnual=consumokWh*12, geracaoTotalVidaUtil=0.0;
        double creditoCincoAnos=0, consumoAcimaDaGeracao=0, valorAPagar=0;
        double gastosTotais, ConsumoAPagarNoAno; //Quanto tem que pagar no ano
        double custoComImposto, custoAtualComImposto, diferencaDeCusto, custoInversor=0.0, custoManutEInstal=0.0;
        double[] creditoAnual = new double[30], cashFlow=new double[25];
        double cashFlowCurrentCurrency=0, payback = 0.0, tariff;
        double LCOEcost, LCOEGeneration, LCOEDivisor;
        double LCOESumCost=0.0, LCOESumGeneration=0.0;
        tempoRetorno = 30;
        for(year = 0; year < 25; year++) { //Até 25 anos, que é a estimativa da vida útil dos paineis
            //Atualiza a tarifa com uma estimativa anual de incremento de preço (inflação tarifária)
            tariff = tarifaMensal*Math.pow(1 + Constants.TARIFF_CHANGE/100.0, year);

            //Um ajuste do Custo nivelado de energia
            //LCOEDivisor = Math.pow(1 + Constants.SELIC, year);

            //Depreciação do painel a cada ano (diminuição de rendimento)
            geracaoComDepreciacao = this.geracaoAnual * (1 - (Constants.DEPREC_PANELS) * year);
            //Somando o total de energia produzida para calcular o custo de cada KWh
            //LCOEGeneration = geracaoComDepreciacao/LCOEDivisor; //CÁLCULO DO OUTRO LCOE
            LCOEGeneration = geracaoComDepreciacao; //Cálculo mais simples do custo da energia
            LCOESumGeneration += LCOEGeneration;
            geracaoTotalVidaUtil += geracaoComDepreciacao;
            //Encontra quantos KWh o usuário ganhou de créditos esse ano (créditos têm o sinal negativo) ou se ele gastou mais do que produziu
            if (consumoMedioAnual < geracaoComDepreciacao) {
                //Se ele produziu mais do que gastou, aumenta os créditos
                creditoAnual[year] = consumoMedioAnual - geracaoComDepreciacao;
                consumoAcimaDaGeracao = 0.0;
            } else {
                //Se ele consumiu mais do que produziu, não ganha créditos e tem que pagar a diferença
                creditoAnual[year] = 0.0;
                consumoAcimaDaGeracao = consumoMedioAnual - geracaoComDepreciacao;
            }

            //Os créditos duram apenas por cinco anos, então, se não foram usados, descarta o mais antigo
            creditoCincoAnos = 0;
            for (int i = year; (i > year - 5) && (i >= 0); i--){
                creditoCincoAnos += creditoAnual[i];
            }

            //Se o consumo que ele tem que pagar for maior do que os créditos que ele tem...
            if (consumoAcimaDaGeracao + creditoCincoAnos > 0.0) {
                //Verifica de fato quanto ele deve pagar, descontando os créditos ainda restantes
                ConsumoAPagarNoAno = consumoAcimaDaGeracao + creditoCincoAnos;
                //Zera os créditos dos últimos cinco anos
                for (int i = year; i > year - 5 && (i >= 0); i--){
                    creditoAnual[i] = 0.0;
                }
            } else {
                //Se os créditos suprirem a demanda, ele não paga "nada"
                ConsumoAPagarNoAno = 0;
                //Consome os créditos utilizados
                for (int i = Math.max(year - 4, 0); i < year; i++){
                    consumoAcimaDaGeracao = creditoAnual[i] + consumoAcimaDaGeracao;
                    if(consumoAcimaDaGeracao <= 0.0){ //Se os créditos já quitaram todo o consumo
                        creditoAnual[i] = consumoAcimaDaGeracao;
                        break;
                    } else {
                        creditoAnual[i] = 0.0;
                    }
                }
            }

            //Se o que ele deve pagar for menor do que o custo de disponibilidade, ele paga o custo de disponibilidade
            if (ConsumoAPagarNoAno < 12.0 * this.pegaCustoDisponibilidade()) {
                ConsumoAPagarNoAno = 12.0 * this.pegaCustoDisponibilidade();
            }
            //Acha o valor em reais que ele pagará para a concessionária no ano
            valorAPagar = ConsumoAPagarNoAno * tariff;
            //Coloca os roubos... Quero dizer, impostos
            custoComImposto = ValueWithTaxes(valorAPagar);
            //Quanto ele pagaria sem o sistema fotovoltaico, também com roubo (imposto)
            custoAtualComImposto = ValueWithTaxes((12.0 * consumokWh)*tariff);
            //Acha a diferença que o sistema causou no custo (isso será o lucro ou a economia do ano)
            diferencaDeCusto = custoAtualComImposto - custoComImposto;
            if(year == 0){
                economiaMensal = diferencaDeCusto/12;
            }

            //Verifica se nesse ano deve-se trocar o inversor (de 10 em 10 anos)
            if (year % Constants.INVERTOR_TIME == 0 && year > 0) {
                custoInversor = inversor.getPrecoTotal() * Math.pow(1 + Constants.IPCA, year);
            } else {
                custoInversor = 0;
            }

            //Considerando que os custos de manutenção são relativos ao investimento inicial, que esses custos sobem em 2 vezes a inflação
            // e que o custo de instalação do inversor é um décimo de seu preço
            custoManutEInstal = (custoTotal * Constants.MAINTENANCE_COST) * Math.pow(1 + (2 * Constants.IPCA), year) + custoInversor * 0.1;
            gastosTotais = custoManutEInstal + custoInversor;
            //Esses são os custos coniderados no LCOE, os custos de energia (da concessionária) não entram no cálculo
            //LCOEcost = gastosTotais/LCOEDivisor;
            LCOESumCost += gastosTotais;


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
            this.taxaInternaRetorno = 0.0;
        } else {
            this.taxaInternaRetorno = internalRateOfReturn;
        }

        /* CÁLCULO DO LCOE NO MÉTODO MAIS COMPLEXO (NÃO USADO PELO MERCADO DE ENERGIA SOLAR)
        /////////////Bora calcular o LCOE!!
        //Achar o capacity factor da usina (porcentagem de energia real gerada em relação à produção nominal da usina)
        //Geração real dividido pela geração máxima (capacidade * horas em um dia * dias em um ano * anos de operação)
        double capacityFactor = geracaoTotalVidaUtil * 1000 /(potenciaInstalada * 24 * 365 * Constants.PANEL_LIFE);
        //Encontrar o Overnight Capital Cost (R$/kW) o investimento inicial por kW
        double overnightCapitalCost = this.custoTotal * 1000 / (potenciaInstalada); //Multiplicando por 1000 para encontrar por kW, em vez de por W
        //Encontrar o CRF (Capital Recovery Factor)
        double numerador = (Constants.COST_OF_CAPITAL * Math.pow(1 + Constants.COST_OF_CAPITAL, Constants.PANEL_LIFE));
        double denominador = (Math.pow(1 + Constants.COST_OF_CAPITAL, Constants.PANEL_LIFE) - 1);
        double LCOEcrf = numerador / denominador;

        //Encontrar o custo de manutenção por kW
        double fixedOnM = LCOESumCost * 1000 / (potenciaInstalada * Constants.PANEL_LIFE); //Multiplicando por 1000 para encontrar por kW-ano, em vez de por W-ano

        //Faz o cálculo do LCOE de acordo com a referência (Documentação deste método)
        LCOE = (overnightCapitalCost*LCOEcrf + fixedOnM) /
                (24*365*capacityFactor);
         */

        //Cálculo do LCOE feito no mercado atualmente
        LCOE = (LCOESumCost + this.custoTotal) / LCOESumGeneration;
    }




    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////        Funções Auxiliares       ////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* Descrição: Calcula os resultados do dimensionamento para o modelo de módulo fotovoltaico escolhido por idModuloEscolhido,
     *              retornando true se a placa não existir no banco de dados
     * Parâmetros de Entrada: MyContext - Contexto de execução da função para criar os inputStreams //
     *                        placaEscolhida - painel para se calcular os resultados econômicos
     * Saída: -;
     * Pré Condições: MyContext é válido // placaEscolhida é diferente de NULL
     * Pós Condições: Altera as informações econômicas e do sistema no objeto this
     */
    public void calculaResultadosPlaca(Context MyContext, Painel placaEscolhida){
        //Definindo oas placas
        if(this.areaAlvo < 0){
            placaEscolhida.setQtd((int) Math.floor(this.potenciaNecessaria / placaEscolhida.getPotencia())); // arredondar para baixo
        } else {
            placaEscolhida.setQtd((int)Math.floor(this.areaAlvo / placaEscolhida.getArea())); // arredondar para baixo
        }
        placaEscolhida.setCustoTotal(placaEscolhida.getQtd() * placaEscolhida.getPreco());


        this.placaEscolhida = placaEscolhida;

        area = DefineArea(placaEscolhida);

        //Encontrando a potenciaInstalada
        this.potenciaInstalada = placaEscolhida.getPotencia() * placaEscolhida.getQtd();

        //Definindo os inversores
        inversor = DefineInvertor();

        //Definindo os custos
        double[] custos = this.DefineCosts(placaEscolhida, inversor);
        custoParcial = custos[Constants.iCOSTS_PARCIAL];
        custoTotal = custos[Constants.iCOSTS_TOTAL];

        //Calculo da energia produzida em um ano
        geracaoAnual = EstimateAnualGeneration();

        GetEconomicInformation();
    }

    /* Descrição: Recebe a conta de energia em reais e retorna o valor, também em reais, retirando os impostos
     * Parâmetros de Entrada: costReais - Valor em reais da conta de luz média mensal, com impostos
     * Saída: Valor sem impostos - costReais retirando o CIP, ICMS, PIS e COFINS
     * Pré Condições: costReais é não nulo e contém os impostos
     * Pós Condições: Retorna o valor sem impostos
     */
    @org.jetbrains.annotations.Contract(pure = true)
    public static double ValueWithoutTaxes(double costReais){
        //Considera o menor entre 5% do valor total da conta de luz e um valor máximo de 50 reais
        double contribuicaoIlumPublica = Math.min(costReais * 0.05, Constants.CIPMAX);
        return (costReais - contribuicaoIlumPublica)*(1 - Constants.ICMS)*(1 - Constants.PIS - Constants.COFINS);
    }

    /* Descrição: Recebe a o consumo vezes a tarifa, em reais, e retorna o valor total que deveria ser pago
     * Parâmetros de Entrada: valorSemImpostos - Valor em reais que se pagaria pela energia sem impostos e taxas
     * Saída: Valor sem impostos - costReais retirando o CIP, ICMS, PIS e COFINS
     * Pré Condições: costReais é não nulo e contém os impostos
     * Pós Condições: Retorna o valor sem impostos
     */
    @org.jetbrains.annotations.Contract(pure = true)
    public static double ValueWithTaxes(double valorSemImpostos){
        double valorSemCIP = (valorSemImpostos / ((1 - Constants.ICMS)*(1 - Constants.PIS - Constants.COFINS)));
        //Considera o menor entre 5% do valor total da conta de luz e um valor máximo de 50 reais
        double contribuicaoIlumPublica = Math.min(valorSemCIP * 0.05 / 0.95, Constants.CIPMAX);
        return  valorSemCIP + contribuicaoIlumPublica;
    }


    /* Descrição: Converte a conta de energia de reais para kWh
     * Parâmetros de Entrada: costReais - Valor double em reais da conta de luz média mensal, sem impostos;
tarifaMensal - valor da tarifa de energia
     * Saída: Valor de energia em kWh mensal - costReais dividido pela tarifa de energia
     * Pré Condições: costReais é não nulo e não contém os impostos; stateVec - não é nulo e contém as informações do estado
     * Pós Condições: Retorna o valor do consumo de energia mensal
     */
    public static double ConvertToKWh(double costReais, double tarifaMensal){
        return costReais/tarifaMensal;
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

    /* Descrição: Escolhe o arranjo de inversores mais barato com base no arranjo de paineis escolhido ou o inversor com índice idInversorEscolhido
     * Parâmetros de Entrada: -;
     * Saída: Arranjo do inversor de menor custo total, ou modelo escolhido
     * Pré Condições: O arranjo de paineis já foi escolhido. idInversorEsoclhido pode ser -1 para pegar o mais barato ou o índice do modelo
     * Pós Condições: Os inversores têm seus valores de quantidade e preço total atualizados (A não ser que idInversorEscolhido seja diferente de -1);
     */
    public Inversor DefineInvertor(){
        Inversor inversorBarato = null;

        if(idInversorEscolhido != -1){
            inversorBarato = listaInversores.get(idInversorEscolhido);
            inversorBarato.setQtd((int)Math.ceil((0.8*this.pegaPlacaEscolhida().getQtd()*this.pegaPlacaEscolhida().getPotencia())/
                    inversorBarato.getPotencia()));
            inversorBarato.setPrecoTotal(inversorBarato.getQtd() * inversorBarato.getPreco());
            return inversorBarato;
        }

        for (Inversor inversor : listaInversores){
            inversor.setQtd((int)Math.ceil((0.8*this.pegaPlacaEscolhida().getQtd()*this.pegaPlacaEscolhida().getPotencia())/
                    inversor.getPotencia()));
            inversor.setPrecoTotal(inversor.getQtd() * inversor.getPreco());
            if(inversorBarato == null){
                inversorBarato = inversor;
            } else {
                if(inversor.getPrecoTotal() < inversorBarato.getPrecoTotal()){
                    inversorBarato = inversor;
                }
            }
        }

        return inversorBarato;
    }


    /* Descrição: Encontra a potência necessária do sistema de módulos fotovoltaicos. Essa potência será o alvo para o dimensionamento dos módulos.
     * Parâmetros de Entrada: energyConsumed - O consumo mensal de energia elétrica (kWh); solarHour - Horas de sol pleno média da cidade escolhida (horas ou kWh/m²dia)
     * Saída: capacidade de geração alvo que o sistema deve ter (Wp), ou 0 se o energyConsumed for menor que o custo de disponibilidade
     * Pré Condições: energyConsumed - é não nulo e positivo. Pode ser menor que o custo de disponibilidade (retorno 0);
solarHour - Está em horas (maior que zero)
     * Pós Condições: Retorna o valor da potência necessária em Wp
     */
    public double FindTargetCapacity(double energyConsumed, double solarHour){
        //energyConsumed -= this.pegaCustoDisponibilidade(); //O Custo de disponibilidade é o mínimo que alguém pode pagar
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
    public static double DefineArea(Painel solarPanel){
        return solarPanel.getQtd() * solarPanel.getArea();
    }


    /* Descrição: Retorna os custos (vetor de double) do sistema dimensionado, tanto o custo parcial quanto o custo total.
     * Parâmetros de Entrada: solarPanel - Vetor de Strings com as informações do sistema de módulos dimensionado.
invertor - Inversor com as informações do sistema de inversores dimensionado
     * Saída: Vetor de double de duas posições com os custos (Índice Custo parcial: 0, Índice Custo total: 1).
O custo parcial é apenas o custos dos módulos e dos inversores. O custo total leva em conta custos com projeto, mão de obra, proteção, fiação, estrutura, entre outros.
     * Pré Condições: solarPanel e invertor - Vetores não nulos com as informações do sistema
     * Pós Condições: Retorna o vetor de custos
     */
    public double[] DefineCosts(Painel solarPanel, Inversor invertor){
        double[] costs = {0.0, 0.0};
        double porcentagemCustosIntegrador;

        //Definido custo parcial
        costs[Constants.iCOSTS_PARCIAL] = invertor.getPrecoTotal() +
                solarPanel.getCustoTotal();
        ////Definindo o custo total
        //Porcentagem do custo parcial para custos com mão de obra, outros componentes etc... Segundo Estudo estratégico da Greener de 2020
        //Nesse estudo, chegamos na equação y = 1.65-0.032x para descrever a porcentagem do custo do kit que representa o custo final para o consumidor
        //Isso desde 1kWp até 8kWp, além desses limites, usamos o valor do limite
        if(this.pegaPotenciaInstalada() < 1000){
            porcentagemCustosIntegrador = 1.65 - 0.032*1;
        } else if(this.pegaPotenciaInstalada() > 8000) {
            porcentagemCustosIntegrador = 1.65 - 0.032 * 8;
        } else {
            porcentagemCustosIntegrador = 1.65 - 0.032*this.pegaPotenciaInstalada()/1000; // /1000 para ter a potencia em kWp
        }


        costs[Constants.iCOSTS_TOTAL] = costs[Constants.iCOSTS_PARCIAL] * porcentagemCustosIntegrador;

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
            tempAboveLimit = ambientTemp + ((placaEscolhida.getNOCT() - 20)*0.00125*irradiance) - 25;
            //Esse valor será negativo devido ao coeficiente de temperatura
            correctionTemp = (tempAboveLimit * placaEscolhida.getCoefTempPot() *
                    placaEscolhida.getPotencia()) / 100;
            //Eficiencia do sistema (qual a porcentagem de energia captada em 1m²)
            efficiency = (placaEscolhida.getPotencia() + correctionTemp)/
                    (placaEscolhida.getArea()*1000); //O 1000 é a quantidade de W/m² que estamos considerando

            //Horas de sol pico do mês * eficiencia * area total
            dailyGen = (Double.parseDouble(vetorCidade[month])/1000.0) * efficiency *
                    placaEscolhida.getArea() * placaEscolhida.getQtd();
            monthlyGen = dailyGen * GetNumberOfDays(month);
            anualGeneration += monthlyGen;
        }
        //Considera perdas por sujeira
        anualGeneration = anualGeneration * (1 - Constants.LOSS_DIRT);
        //Considera perdas com o inversor
        anualGeneration = anualGeneration * inversor.getRendimentoMaximo();

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
