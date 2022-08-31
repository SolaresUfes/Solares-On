package com.solares.calculadorasolar.classes;

import static com.solares.calculadorasolar.classes.CalculadoraOnGrid.GetNumberOfDays;
import static com.solares.calculadorasolar.classes.CalculadoraOnGrid.ValueWithTaxes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.solares.calculadorasolar.activity.ResultadoOffGridActivity;
import com.solares.calculadorasolar.classes.auxiliares.Constants;
import com.solares.calculadorasolar.classes.auxiliares.IRR;
import com.solares.calculadorasolar.classes.entidades.Bateria_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Controlador_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Equipamentos_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Inversor_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Painel_OffGrid;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

public class CalculadoraOffGrid implements Serializable {
    double HSP=0;
    String[] vetorCidade;
    String[] vetorEstado;
    LinkedList<Painel_OffGrid> listaPaineis_off_grid;
    String[] nomesPaineis_off_grid;
    LinkedList<Inversor_OffGrid> listaInversores_off_grid;
    String[] nomesInversores_off_grid;
    LinkedList<Controlador_OffGrid> listaControladores_off_grid;
    String[] nomesControladores_off_grid;
    LinkedList<Bateria_OffGrid> listaBaterias_off_grid;
    String[] nomesBaterias_off_grid;
    LinkedList<Equipamentos_OffGrid> listaEquipamentos_off_grid;
    String[] nomesEquipamentos_off_grid;
    ArrayList<Equipamentos_OffGrid> vetorEquipamentos;
    ArrayList<Equipamentos_OffGrid> EquipamentosSelecionados;
    String nomeCidade;
    Painel_OffGrid painelEscolhido;
    Inversor_OffGrid inversorEscolhido;
    Bateria_OffGrid bateriaEscolhida;
    Controlador_OffGrid controladorEscolhido;
    double custoReais;
    double consumokWh;
    double potenciaNecessaria;
    double potenciaInstalada; //nPaineis * potenciaPainel
    double area;
    double custoParcial;
    double custoTotal;
    double geracaoDiario;
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
    int idControladorEscolhido;
    int idBateriaEscolhida;
    double custoDisponibilidade;

    double consumo;
    boolean modoCalculoPorDinheiro;

    double potenciaUtilizadaDiariaCC=0;
    double potenciaUtilizadaDiariaCA=0;
    double fatorPotencia=0.92;
    double energiaAtivaDia=0;
    double minPotencia=0;
    double Vsist=0;
    double Vbat=0;
    double CBI_C20=0;
    int nBatSerie=0;
    int nBatParalelo=0;
    double CBI_bat=0;
    int qntBat=0;
    int placaParalelo=0;
    int placaSerie=0;
    double correntePainel=0;
    double correnteMaxPower=0;
    double tensaoMaxPowerTempMax=0;
    double potenciaAparente;
    double Voc_corrigida;
    double coeficienteVariacao = -0.29;
    double temperaturaMedia=0;
    int autonomia=2;

    /* Descrição: Construtor do Objeto CalculadoraOnGrid
     * Parâmetros de Entrada: -;
     * Saída: -;
     * Pré Condições: -;
     * Pós Condições: O objeto foi construido com a área alvo como -1f e sem nenhum módulo ou inversor definido;
     */
    public CalculadoraOffGrid(){
        this.areaAlvo = -1f;
        this.idModuloEscolhido = -1;
        this.idInversorEscolhido = -1;
        this.idControladorEscolhido = -1;
        this.idBateriaEscolhida = -1;
    }

    //////////////////////////
    ////  Funções getters ////
    //////////////////////////
    public String[] pegaVetorCidade(){ return vetorCidade; }
    public String[] pegaVetorEstado(){ return vetorEstado; }
    public LinkedList<Painel_OffGrid> pegaListaPaineisOffGrid() { return listaPaineis_off_grid; }
    public String[] pegaNomesPaineisOffGrid() { return nomesPaineis_off_grid; }
    public Painel_OffGrid pegaPlacaEscolhidaOffGrid(){ return painelEscolhido;}
    public LinkedList<Inversor_OffGrid> pegaListaInversoresOffGrid() { return listaInversores_off_grid; }
    public String[] pegaNomesInversoresOffGrid() { return nomesInversores_off_grid; }
    public Inversor_OffGrid pegaInversorEscolhidaOffGrid(){ return inversorEscolhido;}
    public LinkedList<Controlador_OffGrid> pegaListaControladoresOffGrid() { return listaControladores_off_grid; }
    public String[] pegaNomesControladoresOffGrid() { return nomesControladores_off_grid; }
    public Controlador_OffGrid pegaControladorEscolhidaOffGrid(){ return controladorEscolhido;}
    public LinkedList<Bateria_OffGrid> pegaListaBateriasOffGrid() { return listaBaterias_off_grid; }
    public String[] pegaNomesBateriasOffGrid() { return nomesBaterias_off_grid; }
    public Bateria_OffGrid pegaBateriaEscolhidaOffGrid(){ return bateriaEscolhida;}
    public LinkedList<Equipamentos_OffGrid> pegaListaEquipamentosOffGrid() { return listaEquipamentos_off_grid; }
    public String[] pegaNomesEquipamentosOffGrid() { return nomesEquipamentos_off_grid; }
    public ArrayList<Equipamentos_OffGrid> pegaVetorEquipamentos(){ return vetorEquipamentos; }
    public int pegaAutonomia() { return this.autonomia; }
    public String pegaNomeCidade(){ return nomeCidade; }
    public double pegaCustoReais(){ return custoReais; }
    public double pegaConsumokWhs(){ return consumokWh; }
    public double pegaPotenciaNecessaria(){ return potenciaNecessaria; }
    public double pegaPotenciaInstalada(){ return potenciaInstalada; }
    public double pegaArea(){ return area; }
    public double pegaCustoParcial(){ return custoParcial; }
    public double pegaCustoTotal(){ return custoTotal; }
    public double pegaGeracaoDiario(){ return geracaoDiario; }
    public double pegaLucro(){ return lucro; }
    public double pegaTaxaInternaRetorno(){ return taxaInternaRetorno; }
    public double pegaEconomiaMensal(){ return economiaMensal; }
    public double pegaLCOE(){ return LCOE; }
    public int pegaTempoRetorno(){ return tempoRetorno; }
    public double pegaHorasDeSolPleno(){ return horasDeSolPleno; }
    public double pegaTarifaMensal(){ return tarifaMensal; }
    public int pegaNumeroDeFases(){ return numeroDeFases; }
    public double pegaCustoDisponibilidade(){ return custoDisponibilidade; }
    public double getMinPotencia(){ return this.minPotencia; }

    //////////////////////////
    ////  Funções setters ////
    //////////////////////////
    public void setVetorCidade(String[] vetorCidade){
        this.vetorCidade = vetorCidade;
    }
    public void setVetorEstado(String[] vetorEstado){
        this.vetorEstado = vetorEstado;
    }
    public void setListaPaineisOffGrid(LinkedList<Painel_OffGrid> listaPaineis_off_grid) { this.listaPaineis_off_grid = listaPaineis_off_grid; }
    public void setListaInversoresOffGrid(LinkedList<Inversor_OffGrid> listaInversores_off_grid) { this.listaInversores_off_grid = listaInversores_off_grid; }
    public void setListaControladoresOffGrid(LinkedList<Controlador_OffGrid> listaControladores_off_grid) { this.listaControladores_off_grid = listaControladores_off_grid; }
    public void setListaBateriasOffGrid(LinkedList<Bateria_OffGrid> listaBaterias_off_grid) { this.listaBaterias_off_grid = listaBaterias_off_grid; }
    public void setListaEquipamentosOffGrid(LinkedList<Equipamentos_OffGrid> listaEquipamentosOffGrid) { this.listaEquipamentos_off_grid = listaEquipamentosOffGrid; }
    public void setEquipamentosSelecionados(Equipamentos_OffGrid equipamento){        this.EquipamentosSelecionados.add(equipamento); }
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
    public void setPotenciaUtilizadaDiariaCC(double potenciaUtilizadaDiariaCC){ this.potenciaUtilizadaDiariaCC = potenciaUtilizadaDiariaCC;}
    public void setPotenciaUtilizadaDiariaCA(double potenciaUtilizadaDiariaCA){ this.potenciaUtilizadaDiariaCA = potenciaUtilizadaDiariaCA;}

    //idModuloEscolhido - Inteiro que representa um modelo de módulo escolhido pelo usuário. Se for -1, escolhe o melhor
    public void setIdModuloEscolhido(int novoIdModuloEscolhido) { this.idModuloEscolhido = novoIdModuloEscolhido; }
    //idInversoreEscolhido - Inteiro que representa um modelo de inversor escolhido pelo usuário. Se for -1, escolhe o melhor
    public void setIdInversorEscolhido(int novoIdInversorEscolhido) { this.idInversorEscolhido = novoIdInversorEscolhido; }
    //idModuloEscolhido - Inteiro que representa um modelo de módulo escolhido pelo usuário. Se for -1, escolhe o melhor
    public void setIdControladorEscolhido(int novoIdControladorEscolhido) { this.idControladorEscolhido = novoIdControladorEscolhido; }
    //idInversoreEscolhido - Inteiro que representa um modelo de inversor escolhido pelo usuário. Se for -1, escolhe o melhor
    public void setIdBateriaEscolhido(int novoIdBateriaEscolhida) { this.idBateriaEscolhida = novoIdBateriaEscolhida; }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////        Função Principal       /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Calcular(Context MyContext){
        InputStream is=null;

        try{
            Log.d("Activity", "Entrou na Calculadora Off Grid");

            CalculadoraOnGrid calculadoraOnGrid = new CalculadoraOnGrid();

            // Calcular a HSP do estado em questão
            this.HSP = calculadoraOnGrid.MeanSolarHour(vetorCidade);

            // Calcular a temperatura média do estado em questão
            this.temperaturaMedia = Meantemperature(vetorEstado);

            // Calcular a Energia Ativa Necessária Diariamente
            this.energiaAtivaDia = energiaAtivaNecessariaDia(this.potenciaUtilizadaDiariaCC, this.potenciaUtilizadaDiariaCA);
            System.out.println("L: "+ this.energiaAtivaDia);

            // Calcular Potência Mínima do Arranjo Fotovoltaico
            this.minPotencia = potenciaMinimaArranjoFotovoltaico(HSP, this.energiaAtivaDia);
            System.out.println("Essa é a minima potencia: " + this.minPotencia);

            // Calcular Tensão do Sistema
            this.Vsist = defTensaoSistema(this.energiaAtivaDia);
            System.out.println("Tensao do Sistema: "+this.Vsist);

            // Calcular Voc_corrigida
            Voc_corrigida = Vsist * (1 + ((temperaturaMedia - 25) * coeficienteVariacao)/100);
            System.out.println("Tensao Corrigida: "+this.Voc_corrigida);

            // Número de Dias de Autonomia
            this.autonomia = numeroDiasAutonomia(vetorCidade);

            // Calculo do CBI_C20
            this.CBI_C20 = (Constants.fatorSeguranca * this.energiaAtivaDia * this.autonomia) / (Constants.Pd * this.Vsist);

            int idMelhorModulo=-1;
            int cont=0;
            double melhorLucro=0.0;
            this.nomesPaineis_off_grid = new String[this.pegaListaPaineisOffGrid().size()];
            if(this.idModuloEscolhido == -1){
                for (Painel_OffGrid painelAtual : listaPaineis_off_grid) {
                    System.out.println(melhorLucro);
                    calculaResultadosPlaca(MyContext, painelAtual);
                    this.nomesPaineis_off_grid[cont] = painelAtual.getMarca() + " " + painelAtual.getCodigo() + " " +  String.format(Locale.ITALY, "%.0f", painelAtual.getPotencia())+ "W";
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

                calculaResultadosPlaca(MyContext, listaPaineis_off_grid.get(idMelhorModulo));
            }else{
                iniciarNomesDosModulos();
                calculaResultadosPlaca(MyContext, listaPaineis_off_grid.get(this.idModuloEscolhido));
            }

            Intent intent = new Intent(MyContext, ResultadoOffGridActivity.class);


            //Log.d("placa escolhida", "Plcaca: "+painelEscolhido.getNome());

            //Fechar o InputStream
            try {
                is.close();
            } catch (Exception e){
                e.printStackTrace();
            }

            //Passar o objeto com as informações calculadas para a próxima Activity (ResultadoOff)
            intent.putExtra(Constants.EXTRA_CALCULADORAOFF, this);
            //Mudar de activity
            MyContext.startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void GetEconomicInformation(){
        int year;
        double geracaoComDepreciacao, consumoMedioAnual=this.minPotencia*12, geracaoTotalVidaUtil=0.0;
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
            tariff = 0.61*Math.pow(1 + Constants.TARIFF_CHANGE/100.0, year); //0.61 tarifa da energia - tornar possiver ajustar essa tarifa

            //Um ajuste do Custo nivelado de energia
            //LCOEDivisor = Math.pow(1 + Constants.SELIC, year);

            //Depreciação do painel a cada ano (diminuição de rendimento)
            geracaoComDepreciacao = (this.geracaoAnual) * (1 - (Constants.DEPREC_PANELS) * (year+1) * (1 - inversorEscolhido.getRendimentoMaximo()) );
            //Somando o total de energia produzida para calcular o custo de cada KWh
            //LCOEGeneration = geracaoComDepreciacao/LCOEDivisor; //CÁLCULO DO OUTRO LCOE
            LCOEGeneration = geracaoComDepreciacao; //Cálculo mais simples do custo da energia
            LCOESumGeneration += LCOEGeneration;
            geracaoTotalVidaUtil += geracaoComDepreciacao;


            //Acha o valor em reais que ele deixará de pagar por ano
            valorAPagar = geracaoComDepreciacao * tariff;
            //Coloca os roubos... Quero dizer, impostos
            custoComImposto = ValueWithTaxes(valorAPagar);
            //Quanto ele pagaria sem o sistema fotovoltaico, também com roubo (imposto)
            custoAtualComImposto = ValueWithTaxes((12*this.minPotencia)*tariff);
            //Acha a diferença que o sistema causou no custo (isso será o lucro ou a economia do ano)
            diferencaDeCusto = custoAtualComImposto - custoComImposto;
            System.out.println("Diferenca: "+diferencaDeCusto);
            if(year == 0){
                economiaMensal = diferencaDeCusto/12;
            }

            //Verifica se nesse ano deve-se trocar o inversor (de 10 em 10 anos)
            if (year % Constants.INVERTOR_TIME == 0 && year > 0) {
                custoInversor = inversorEscolhido.getPrecoTotal() * Math.pow(1 + Constants.IPCA, year);
            } else {
                custoInversor = 0;
            }

            //Verifica se nesse ano deve-se trocar o controlador

            //Verifica se nesse ano deve-se trocar a bateria

            //Considerando que os custos de manutenção são relativos ao investimento inicial, que esses custos sobem em 2 vezes a inflação
            // e que o custo de instalação do inversor é um décimo de seu preço
            custoManutEInstal = (custoTotal * Constants.MAINTENANCE_COST) * Math.pow(1 + (2 * 0.1), year) + custoInversor * 0.1;// ipca = 10%
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


    public void calculaResultadosPlaca(Context MyContext, Painel_OffGrid placaEscolhida){
        //Definindo oas placas
        if(this.areaAlvo < 0){
            placaEscolhida.setQtd((int) Math.floor(this.minPotencia / placaEscolhida.getPotencia())); // arredondar para baixo
        } else {
            placaEscolhida.setQtd((int)Math.floor(this.areaAlvo / placaEscolhida.getArea())); // arredondar para baixo
        }
        placaEscolhida.setCustoTotal(placaEscolhida.getQtd() * placaEscolhida.getPreco());


        this.painelEscolhido = placaEscolhida;

        area = DefineArea(placaEscolhida);

        //Encontrando a potenciaInstalada
        this.potenciaInstalada = placaEscolhida.getPotencia() * placaEscolhida.getQtd();

        //Definindo a bateria
        bateriaEscolhida = DefineBattery();

        //Definindo o inversor
        inversorEscolhido = DefineInvertor();

        //Definido o controlador
        controladorEscolhido = DefineController(this.painelEscolhido);

        //Definindo os custos
        double[] custos = this.DefineCosts(placaEscolhida, inversorEscolhido, controladorEscolhido, bateriaEscolhida);
        custoParcial = custos[Constants.iCOSTS_PARCIAL];
        custoTotal = custos[Constants.iCOSTS_TOTAL];

        //Calculo da energia produzida em um ano
        geracaoDiario = EstimateDailyGeneration();

        GetEconomicInformation();
    }

    public Inversor_OffGrid DefineInvertor(){
        Inversor_OffGrid inversorBarato = null;

        if(idInversorEscolhido != -1){
            inversorBarato = listaInversores_off_grid.get(idInversorEscolhido);
            inversorBarato.setQtd((int)Math.ceil((0.8*this.pegaPlacaEscolhidaOffGrid().getQtd()*this.pegaPlacaEscolhidaOffGrid().getPotencia())/
                    inversorBarato.getPotencia()));
            inversorBarato.setPrecoTotal(inversorBarato.getQtd() * inversorBarato.getPreco());
            return inversorBarato;
        }

        for (Inversor_OffGrid inversor : listaInversores_off_grid){
            inversor.setQtd((int)Math.ceil((0.8*this.pegaPlacaEscolhidaOffGrid().getQtd()*this.pegaPlacaEscolhidaOffGrid().getPotencia())/
                    inversor.getPotencia()));
            inversor.setPrecoTotal(inversor.getQtd() * inversor.getPreco());
            if(inversorBarato == null){
                inversorBarato = inversor;
            } else {
                if(inversor.getPrecoTotal() < inversorBarato.getPrecoTotal() && inversor.getTensaoEntrada() >= bateriaEscolhida.getVnom()){
                    inversorBarato = inversor;
                }
            }
        }
        return inversorBarato;
    }

    public Controlador_OffGrid DefineController(Painel_OffGrid painelEscolhido){
        Controlador_OffGrid controladorBarato = null;
        float Ic;
        int modParal, modSerie;

        int qtdController;
        float totalCostController;

        if(idControladorEscolhido != -1){
            controladorBarato = listaControladores_off_grid.get(idControladorEscolhido);

            modSerie = numModulosSerie(controladorBarato.getTensao_max_sistema(), painelEscolhido.getVca());
            modParal = numModulosParalelo(this.minPotencia, modSerie, painelEscolhido.getPotencia());

            Ic = (float) (1.25 * modParal * painelEscolhido.getIcc());

            qtdController = (int)Math.ceil(Ic/controladorBarato.getCorrente_carga());
            totalCostController = qtdController * controladorBarato.getPreco();

            controladorBarato.setQtd(qtdController);
            controladorBarato.setPrecoTotal(totalCostController);
            return controladorBarato;

        }

        if (listaControladores_off_grid.size()!=0){
            for (Controlador_OffGrid controlador : listaControladores_off_grid){
                modSerie = numModulosSerie(controlador.getTensao_max_sistema(), painelEscolhido.getVca());
                modParal = numModulosParalelo(this.minPotencia, modSerie, painelEscolhido.getPotencia());

                Ic = (float) (1.25 * modParal * painelEscolhido.getIcc());

                qtdController = (int)Math.ceil(Ic/controlador.getCorrente_carga());
                totalCostController = qtdController * controlador.getPreco();

                controlador.setQtd(qtdController);
                controlador.setPrecoTotal(totalCostController);

                if(controladorBarato == null){
                    controladorBarato=controlador;
                }
                else{
                    if(controlador.getTensao_bateria() >= this.Vsist && controladorBarato.getPrecoTotal() > totalCostController) {//  &&  currentPowerSistController > Voc*modSerie --> devo colocar, entretanto está sempre retornando Falso
                        controladorBarato = controlador;
                    }
                }

            }
        }

       return controladorBarato;
   }

    public Bateria_OffGrid DefineBattery(){
        Bateria_OffGrid bateriaBarato = null;

        if (idBateriaEscolhida != -1){
            bateriaBarato = listaBaterias_off_grid.get(idBateriaEscolhida);

            nBatSerie = (int)Math.ceil(Vsist/bateriaBarato.getVnom());
            nBatParalelo = (int)Math.ceil(CBI_C20/bateriaBarato.getC20());
            qntBat = nBatParalelo * nBatSerie;

            bateriaBarato.setPrecoTotal(bateriaBarato.getPreco()*qntBat);
            bateriaBarato.setnSerie(nBatSerie);
            bateriaBarato.setnParalel(nBatParalelo);
            bateriaBarato.setQtd(qntBat);

            return bateriaBarato;
        }


        if (listaBaterias_off_grid.size()!=0){
            for (Bateria_OffGrid bateria : listaBaterias_off_grid){
                nBatSerie = (int)Math.ceil(Vsist/bateria.getVnom());
                nBatParalelo = (int)Math.ceil(CBI_C20/bateria.getC20());
                qntBat = nBatParalelo * nBatSerie;

                bateria.setPrecoTotal(bateria.getPreco()*qntBat);
                bateria.setnSerie(nBatSerie);
                bateria.setnParalel(nBatParalelo);
                bateria.setQtd(qntBat);

                if(bateriaBarato == null) bateriaBarato=bateria;
                else{
                    if (bateria.getPrecoTotal() < bateriaBarato.getPrecoTotal()){
                        bateriaBarato = bateria;
                    }
                }
            }
        }

        return bateriaBarato;
    }

    public static double DefineArea(Painel_OffGrid solarPanel){
        return solarPanel.getQtd() * solarPanel.getArea();
    }

    public double EstimateDailyGeneration(){
        //Calculos retirados do manual de engenharia FV p. 149 a 153
        double monthlyGen, dailyGen=0, cdailyGen;
        int month;
        double ambientTemp;

        double Kt;
        double Tmod;
        double Eficiencia;
        double ProdMensal;

        geracaoAnual = 0;

        for(month=1; month<=12; month++){
            //Pega a temperatura média do estado no mês
            ambientTemp = Double.parseDouble(vetorEstado[month]);

            //Cálculo do coeficiente térmico para o módulo
            Kt = (painelEscolhido.getNOCT() - 20)/800;
            //Cálculo da temperatura de operação estimada do módulo
            Tmod = ambientTemp + Kt*1000;
            //Cálculo da eficiência dos módulos, relativa à sua potência nominal
            Eficiencia = (Tmod - 25) * (painelEscolhido.getCoefTempPot()/100);
            //Cáculo da produção de energia para cada mês
            ProdMensal = (1+Eficiencia) * painelEscolhido.getPotencia() * painelEscolhido.getQtd() * HSP * 30 / 1000;

            geracaoAnual +=ProdMensal;

            cdailyGen = ProdMensal/GetNumberOfDays(month);
            if ((cdailyGen > dailyGen)) {
                dailyGen = cdailyGen;
            }

        }
        //Considera perdas por sujeira
        geracaoAnual = geracaoAnual * (1 - Constants.LOSS_DIRT);
        //Considera perdas com o inversor
        geracaoAnual = geracaoAnual * inversorEscolhido.getRendimentoMaximo();

        monthlyGen = geracaoAnual/12;

        return dailyGen;
    }

    public double[] DefineCosts(Painel_OffGrid solarPanel, Inversor_OffGrid invertor, Controlador_OffGrid controlador, Bateria_OffGrid bateria){
        double[] costs = {0.0, 0.0};
        double porcentagemCustosIntegrador;

        //Definido custo parcial
        costs[Constants.iCOSTS_PARCIAL] = bateria.getPrecoTotal()+ controlador.getPrecoTotal() + invertor.getPrecoTotal() + solarPanel.getCustoTotal();
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

    public double Meantemperature(String[] vetorEstado){
        int i;
        double temperatura = 0.0;
        for(i=1; i<=12; i++){
            temperatura += Double.parseDouble(vetorEstado[i]);
        }
        temperatura = temperatura/12.0;

        return temperatura;
    }

    public static double energiaAtivaNecessariaDia(double Lcc, double Lca){
        double L=0;
        double rendimentoBat=0.9;
        double rendimentoInv=0.9;
        L = Lcc/rendimentoBat + Lca/(rendimentoBat * rendimentoInv);
        return L;
    }

    // #### Fórmula ####
    // Pmax = max_i=1 ^12 (L_i / (HSP * Red1 * Red2))
    public static double potenciaMinimaArranjoFotovoltaico(double HSP, double L){
        double Lmes=0;
        double Pi;
        double Pmax=0;
        double Red1Red2 = 0.9;

        for (int i=0; i<12; i++) {
            Pi=0;
            Lmes = L * numDiasMes(i);
            Pi = HSP * (Red1Red2);
            Pi = Lmes / Pi;
            if (Pi > Pmax) Pmax = Pi;
        }
        return Pmax;
    }

    static int numDiasMes(int i){
        if(i==3 || i==5 || i==8 || i==10) return 30; // abril, junho, setembro, novembro
        else if(i==2) return 2;
        return 31;
    }

    public static double potenciaPico(double HSP, double minPotencia){
        double insolacao=0;
        double potenciaPico=0;

        insolacao = 1000 * HSP;
        potenciaPico = minPotencia / insolacao;

        return potenciaPico;
    }

    public static int numeroDiasAutonomia(String[] vetorCidade){
        double HSPmin=-1;
        double HSPmes=-1;
        int dias=2;

        HSPmin = Double.parseDouble(vetorCidade[1])/1000.0;
        for (int i=2; i<=12; i++){
            HSPmes = Double.parseDouble(vetorCidade[i])/1000.0;
            if(HSPmin > HSPmes) HSPmin = HSPmes;
        }

        dias = (int) Math.ceil((-0.48 * HSPmin) + 4.58);

        if(dias < 2) return 2;
        else if(dias > 4) return 4;
        return dias;
    }

    public static int defTensaoSistema(double L){
        if(L <= 1000) return 12;
        if(L <= 4000) return 24;
        return 48;
    }

    public int numModulosSerie(float Vcontrolador, double Voc_corrigida){ ;
        int mSerie = (int)Math.round((Vcontrolador * 1.2) / Voc_corrigida);
        return mSerie;
    }

    public int numModulosParalelo(double P_pv, int qntPlacasSerie, float P_mod){
        return (int) Math.round(P_pv / (qntPlacasSerie * P_mod));
    }

    public void iniciarNomesDosElementos(){
        //Pega os nomes dos equipamentos eletronicos
        System.out.println("Iniciou elementos eletronicos");
        int cont=0;
        nomesEquipamentos_off_grid = new String[this.pegaListaEquipamentosOffGrid().size()];
        EquipamentosSelecionados = new ArrayList<Equipamentos_OffGrid>();
        vetorEquipamentos = new ArrayList<Equipamentos_OffGrid>();
        for (Equipamentos_OffGrid equipamentoAtual : this.pegaListaEquipamentosOffGrid()) {
            Log.d("calculadora_off", "Equipamentos encontrado: " + equipamentoAtual.getNome());
            vetorEquipamentos.add(equipamentoAtual);
            nomesEquipamentos_off_grid[cont] = equipamentoAtual.getNome();
            cont++;
        }
    }

    public void iniciarNomesDosModulos(){
        //Pega os nomes dos equipamentos eletronicos
        System.out.println("Iniciou Modulos");
        int cont=0;
        nomesPaineis_off_grid = new String[this.pegaListaPaineisOffGrid().size()];
        for (Painel_OffGrid painelAtual : this.listaPaineis_off_grid) {
            nomesPaineis_off_grid[cont] = painelAtual.getMarca() + " " + painelAtual.getCodigo() + " " +painelAtual.getPotencia() + "W";
            listaPaineis_off_grid.get(cont).setNome(nomesPaineis_off_grid[cont]);
            cont++;
        }
    }

    public void iniciarNomesDosControladores(){
        //Pega os nomes dos equipamentos eletronicos
        System.out.println("Iniciou Controladores");
        int cont=0;
        nomesControladores_off_grid = new String[this.pegaListaControladoresOffGrid().size()];
        for (Controlador_OffGrid controlador : this.pegaListaControladoresOffGrid()) {
            Log.d("calculadora_off", "Controlador encontrado: " + controlador.getNome());
            nomesControladores_off_grid[cont] = controlador.getNome();
            cont++;
        }
    }

    public void iniciarNomesDosInversores(){
        //Pega os nomes dos equipamentos eletronicos
        System.out.println("Iniciou Inversores");
        int cont=0;
        nomesInversores_off_grid = new String[this.pegaListaInversoresOffGrid().size()];
        for (Inversor_OffGrid inversor_offGrid : this.pegaListaInversoresOffGrid()) {
            Log.d("calculadora_off", "inversor_offGrid encontrado: " + inversor_offGrid.getNome());
            nomesInversores_off_grid[cont] = inversor_offGrid.getNome();
            cont++;
        }
    }

    public void iniciarNomesDasBaterias(){
        //Pega os nomes dos equipamentos eletronicos
        System.out.println("Iniciou Baterias");
        int cont=0;
        nomesBaterias_off_grid = new String[this.pegaListaBateriasOffGrid().size()];
        for (Bateria_OffGrid bateria_offGrid : this.pegaListaBateriasOffGrid()) {
            Log.d("calculadora_off", "bateria_offGrid encontrado: " + bateria_offGrid.getNome());
            nomesBaterias_off_grid[cont] = bateria_offGrid.getNome();
            cont++;
        }
    }
}
