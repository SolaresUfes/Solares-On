package com.solares.calculadorasolar.classes;

import static com.solares.calculadorasolar.classes.CalculadoraOnGrid.DefineArea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.activity.AdicionarEquipamentosActivity;
import com.solares.calculadorasolar.activity.PedirConsumoEnergeticoActivity;
import com.solares.calculadorasolar.activity.ResultadoOffGridActivity;
import com.solares.calculadorasolar.classes.Global;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class CalculadoraOffGrid implements Serializable{
    // Variáveis Privadas da classe
    double HSP=0;
    String[] vetorEstado;
    String[] vetorCidade;
    String nomeCidade;
    String[] placaEscolhida;
    String[] inversorEscolhido;
    String[] controladorEscolhido;
    String[] bateriaEscolhida;
    double potenciaUtilizadaDiariaCC=0;
    double potenciaUtilizadaDiariaCA=0;
    double fatorPotencia=0.92;
    double energiaAtivaDia=0;
    double minPotencia=0;
    double potenciaInstalada=0;
    double temperaturaMedia=0;
    double area=0;
    double custoParcial=0;
    double custoTotal=0;
    double lucro=0;
    int autonomia=2;
    int Vsist=0;
    int Vbat=0;
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

    float areaAlvo;
    int idModuloEscolhido;
    int idInversorEscolhido;
    int idControladorEscolhido;
    int idBateriaEscolhida;

    //////////////////////////
    ////  Funções getters ////
    //////////////////////////
    public String[] getVetorCidade(){ return this.vetorCidade; }
    public String[] getPlacaEscolhida(){ return this.placaEscolhida; }
    public double getMinPotencia(){ return this.minPotencia; }
    public String[] getControlador(){ return this.controladorEscolhido; }
    public String[] getInversor(){ return this.inversorEscolhido; }
    public String[] getBateria(){ return this.bateriaEscolhida; }
    public double pegaLucro(){return this.lucro; }

    //////////////////////////
    ////  Funções setters ////
    //////////////////////////
    public void setVetorCidade(String[] vetorCidade){ this.vetorCidade = vetorCidade;}
    public void setVetorEstado(String[] vetorEstado){
        this.vetorEstado = vetorEstado;
    }
    public void setNomeCidade(String nomeCidade){ this.nomeCidade = nomeCidade; }
    public void setAreaAlvo(float AreaAlvo) { this.areaAlvo = AreaAlvo;}
    public void setIdModuloEscolhido(int idModuloEscolhido){ this.idModuloEscolhido = idModuloEscolhido;}
    public void setIdInversorEscolhido(int idInversorEscolhido){ this.idInversorEscolhido = idInversorEscolhido;}
    public void setIdControladorEscolhido(int idControladorEscolhido){ this.idControladorEscolhido=idControladorEscolhido;}
    public void setIdBateriaEscolhida(int idBateriaEscolhida){ this.idBateriaEscolhida = idBateriaEscolhida;}
    public void setPotenciaUtilizadaDiariaCC(double potenciaUtilizadaDiariaCC){ this.potenciaUtilizadaDiariaCC = potenciaUtilizadaDiariaCC;}
    public void setPotenciaUtilizadaDiariaCA(double potenciaUtilizadaDiariaCA){ this.potenciaUtilizadaDiariaCA = potenciaUtilizadaDiariaCA;}

    public CalculadoraOffGrid(){
        setAreaAlvo(-1f);
        setIdModuloEscolhido(-1);
        setIdInversorEscolhido(-1);
        setIdControladorEscolhido(-1);
        setIdBateriaEscolhida(-1);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////        Função Principal       /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Calcular(Context MyContext){
        InputStream is=null;

        try {
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

            /*// Definindo as Placas  ---- isso eh para nao esquecer - lembre de criar  uma funcao parecida em CSVRead
            is = MyContext.getResources().openRawResource(R.raw.banco_paineis_offgrid);
            placaEscolhida = CSVRead.DefineSolarPanel(is, this.minPotencia, this.areaAlvo, this.idModuloEscolhido); // this.minPotencia
            area = calculadoraOnGrid.DefineArea(placaEscolhida);
            System.out.println("------ Módulo: "+placaEscolhida[Constants.iPANEL_NOME]);

            // Calcular Voc_corrigida
            double voc = Double.parseDouble(placaEscolhida[Constants.iPANEL_Voc]);
            placaEscolhida[Constants.iPANEL_Voc] = Double.toString(voc * (1 + ((temperaturaMedia - 25) * coeficienteVariacao)/100));

            // Definindo o Controlador de Carga is,this.Vsist, 1, Integer.parseInt(placaEscolhida[Constants.iPANELOFF_QTD]), this.minPotencia, Integer.parseInt(placaEscolhida[Constants.iPANELOFF_POTENCIA]), this.idControladorEscolhido
            is = MyContext.getResources().openRawResource(R.raw.banco_controladores);
            controladorEscolhido = CSVRead.DefineChargeController(is, this.Vsist, this.placaEscolhida, this.minPotencia, this.idControladorEscolhido); // P_pv talvez seja a potencia da placa multiplicado pela quantidade de placas
            System.out.println("------ Controlador: "+controladorEscolhido[Constants.iCON_NOME]);
            // Definindo Quantidade de Placas em Série e Paralelo
           // this.placaSerie = numModulosSerie(Integer.parseInt(controladorEscolhido[Constants.iCON_V_MAX_SISTEMA]), 1);// Descobrir como ter a Tensão de Máxima Potência de Temp. Máx.
           // this.placaParalelo = numModulosParalelo(this.minPotencia, this.placaSerie,Integer.parseInt(this.placaEscolhida[Constants.iPANELOFF_POTENCIA])); // Descobrir como ter a Corrente de Máxima Potência


            // Definindo o Banco de Baterias
            is = MyContext.getResources().openRawResource(R.raw.banco_baterias);
            bateriaEscolhida = CSVRead.DefineBattery(is, this.CBI_C20, this.Vsist, this.idBateriaEscolhida, this.autonomia);


            // Definindo os Inversores
            if(this.potenciaUtilizadaDiariaCA != 0){
                this.potenciaAparente = this.potenciaUtilizadaDiariaCA / this.fatorPotencia;
                is = MyContext.getResources().openRawResource(R.raw.banco_inversores_off);
                inversorEscolhido = CSVRead.DefineInvertorOffGrid(is, this.placaEscolhida ,this.potenciaAparente, this.Vsist, idInversorEscolhido);
                System.out.println("-------- Inversor: "+inversorEscolhido[Constants.iINVOFF_NOME]);
            }
            else inversorEscolhido= new String[]{"0"};*/

            int cont=0, idMelhorModulo=-1;
            double menorCusto=0.0;
            if(this.idModuloEscolhido == -1){
                idMelhorModulo = cont;
                menorCusto = this.pegaLucro();
                while (!calculaResultadosPlaca(MyContext, cont)) {
                    if (custoTotal < menorCusto) { // Definir melhor placa em relação à anterior
                        idMelhorModulo = cont;
                        menorCusto = custoTotal;
                    }

                    cont++;
                }
                calculaResultadosPlaca(MyContext, idMelhorModulo);
            }else{
                calculaResultadosPlaca(MyContext, this.idModuloEscolhido);
            }
            System.out.println("IDDD: "+idMelhorModulo);



            // Por algum motivo nao esta funcionando            variavelGlobal.setPlaca(placaEscolhida);
            //Preparação para mudar para próxima activity
            Intent intent = new Intent(MyContext, ResultadoOffGridActivity.class);

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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////        Funções Auxiliares       ////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    public int numModulosSerie(int Vcontrolador, double Voc_corrigida){ ;
        int mSerie = (int)Math.round((Vcontrolador * 1.2) / Voc_corrigida);
        return mSerie;
    }

    public int numModulosParalelo(double P_pv, int qntPlacasSerie, int P_mod){
        return (int) Math.round(P_pv / (qntPlacasSerie * P_mod));
    }

    public boolean calculaResultadosPlaca(Context MyContext, int idModuloEscolhido){
        InputStream is;
        //Definindo as placas
        is = MyContext.getResources().openRawResource(R.raw.banco_paineis_offgrid);
        placaEscolhida = CSVRead.DefineSolarPanel(is, minPotencia, this.areaAlvo, idModuloEscolhido);

        if(placaEscolhida==null) return true; // Quando terminar de varrer as linhas deverá sair do while

        area = DefineArea(placaEscolhida);

        //Encontrando a potenciaInstalada
        this.potenciaInstalada = Double.parseDouble(placaEscolhida[Constants.iPANEL_POTENCIA]) * Double.parseDouble(placaEscolhida[Constants.iPANEL_QTD]);

        // Calcular Voc_corrigida
        double voc = Double.parseDouble(placaEscolhida[Constants.iPANEL_Voc]);
        placaEscolhida[Constants.iPANEL_Voc] = Double.toString(voc * (1 + ((temperaturaMedia - 25) * coeficienteVariacao)/100));

        // Definindo o Controlador de Carga
        is = MyContext.getResources().openRawResource(R.raw.banco_controladores);
        controladorEscolhido = CSVRead.DefineChargeController(is, this.Vsist, this.placaEscolhida, this.minPotencia, this.idControladorEscolhido); // P_pv talvez seja a potencia da placa multiplicado pela quantidade de placas
        System.out.println("------ Controlador: "+controladorEscolhido[Constants.iCON_NOME]);

        // Definindo o Banco de Baterias
        is = MyContext.getResources().openRawResource(R.raw.banco_baterias);
        bateriaEscolhida = CSVRead.DefineBattery(is, this.CBI_C20, this.Vsist, this.idBateriaEscolhida, this.autonomia);
        System.out.println("-------- Bateria: "+bateriaEscolhida[Constants.iBAT_NOME]);

        // Definindo os Inversores
        System.out.println(potenciaUtilizadaDiariaCA);
        if(this.potenciaUtilizadaDiariaCA != 0){
            this.potenciaAparente = this.potenciaUtilizadaDiariaCA / this.fatorPotencia;
            is = MyContext.getResources().openRawResource(R.raw.banco_inversores_off);
            inversorEscolhido = CSVRead.DefineInvertorOffGrid(is, this.placaEscolhida ,this.potenciaAparente, this.Vsist, idInversorEscolhido);
            System.out.println("-------- Inversor: "+inversorEscolhido[Constants.iINVOFF_NOME]);
        }
        else inversorEscolhido= new String[]{"0","0", "0", "0", "0", "0", "0", "0", "0","0"};

        //Definindo os custos
        double[] custos = this.DefineCosts(placaEscolhida, inversorEscolhido, controladorEscolhido, bateriaEscolhida);
        custoParcial = custos[Constants.iCOSTS_PARCIAL];
        custoTotal = custos[Constants.iCOSTS_TOTAL];

        //Calculo da energia produzida em um ano
        //geracaoAnual = EstimateAnualGeneration();

        System.out.println("Preco Modulo: "+placaEscolhida[Constants.iPANEL_CUSTO_TOTAL]);
        System.out.println("Preco inv: "+inversorEscolhido[Constants.iINVOFF_PRECO_TOTAL]);
        System.out.println("Preco c: "+controladorEscolhido[Constants.iCON_PRECO_TOTAL]);
        System.out.println("Preco b: "+bateriaEscolhida[Constants.iBAT_PRECO_TOTAL]);

        return false;
    }

    public double[] DefineCosts(String[] solarPanel, String[] invertor, String[] controlador, String[] bateria){
        double[] costs = {0.0, 0.0};
        double porcentagemCustosIntegrador;

        //Definido custo parcial
        costs[Constants.iCOSTS_PARCIAL] = Double.parseDouble(invertor[Constants.iINVOFF_PRECO_TOTAL]) +
                Double.parseDouble(solarPanel[Constants.iPANEL_CUSTO_TOTAL]) + Double.parseDouble(controlador[Constants.iCON_PRECO_TOTAL]) +
                Double.parseDouble(bateria[Constants.iBAT_PRECO_TOTAL]);
        ////Definindo o custo total
        //Porcentagem do custo parcial para custos com mão de obra, outros componentes etc... Segundo Estudo estratégico da Greener de 2020
        //Nesse estudo, chegamos na equação y = 1.65-0.032x para descrever a porcentagem do custo do kit que representa o custo final para o consumidor
        //Isso desde 1kWp até 8kWp, além desses limites, usamos o valor do limite
        if(this.potenciaInstalada < 1000){
            porcentagemCustosIntegrador = 1.65 - 0.032*1;
        } else if(this.potenciaInstalada > 8000) {
            porcentagemCustosIntegrador = 1.65 - 0.032 * 8;
        } else {
            porcentagemCustosIntegrador = 1.65 - 0.032*this.potenciaInstalada/1000; // /1000 para ter a potencia em kWp
        }


        costs[Constants.iCOSTS_TOTAL] = costs[Constants.iCOSTS_PARCIAL]*porcentagemCustosIntegrador;

        return costs;
    }
}
