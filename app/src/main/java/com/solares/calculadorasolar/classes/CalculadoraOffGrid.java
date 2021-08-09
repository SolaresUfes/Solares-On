package com.solares.calculadorasolar.classes;
import android.content.Context;
import android.content.Intent;

import com.solares.calculadorasolar.activity.AdicionarEquipamentosActivity;
import com.solares.calculadorasolar.activity.PedirConsumoEnergeticoActivity;

import java.io.InputStream;
import java.util.ArrayList;

public class CalculadoraOffGrid {
    // Variáveis Privadas da classe
    double HSP=0;
    String[] vetorEstado;
    String[] vetorCidade;
    String nomeCidade;
    String[] placaEscolhida;
    String[] inversorEscolhido;
    String[] controladorEscolhido;
    String[] bateriaEscolhida;
    ArrayList<Equipamentos> vetorEquipamentos;
    int[] vetorQntEquipamentosCA;
    int[] vetorQntEquipamentosCC;
    double potenciaUtilizadaDiariaCC=0;
    double potenciaUtilizadaDiariaCA=0;
    double potenciaAparente=0;
    double fatorPotencia=0.92;
    double energiaAtivaDia=0;
    double minPotencia=0;
    double tempMax=0;
    double area=0;
    int autonomia=2;
    int Vsist=0;
    int Vbat=0;
    double CBI_C20=0;
    double Pd=0.3;
    double fatorSeguranca=1.25;
    int nBatSerie=0;
    int nBatParalelo=0;
    double CBI_bat=0;
    int qntBat=0;
    int placaParalelo=0;
    int placaSerie=0;
    double correntePainel=0;
    double correnteMaxPower=0;
    double tensaoMaxPowerTempMax=0;

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

    //////////////////////////
    ////  Funções setters ////
    //////////////////////////
    public void setVetorCidade(String[] vetorCidade){ this.vetorCidade = vetorCidade;}
    public void setVetorEstado(String[] vetorEstado){
        this.vetorEstado = vetorEstado;
    }
    public void setNomeCidade(String nomeCidade){ this.nomeCidade = nomeCidade; }
    public void setVetorEquipamentos(ArrayList<Equipamentos> vetorEquipamentos){ this.vetorEquipamentos = vetorEquipamentos; }
    public void setVetorQntEquipamentosCA(int[] vetorQntEquipamentos){ this.vetorQntEquipamentosCA = vetorQntEquipamentos;}
    public void setVetorQntEquipamentosCC(int[] vetorQntEquipamentos){ this.vetorQntEquipamentosCC = vetorQntEquipamentos;}
    public void setAreaAlvo(float AreaAlvo) { this.areaAlvo = AreaAlvo;}
    public void setIdModuloEscolhido(int idModuloEscolhido){ this.idModuloEscolhido = idModuloEscolhido;}
    public void setIdInversorEscolhido(int idInversorEscolhido){ this.idInversorEscolhido = idInversorEscolhido;}
    public void setIdControladorEscolhido(int idControladorEscolhido){ this.idControladorEscolhido=idControladorEscolhido;}
    public void setIdBateriaEscolhida(int idBateriaEscolhida){ this.idBateriaEscolhida = idBateriaEscolhida;}

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
            CalculadoraOnGrid calculadoraOnGrid = new CalculadoraOnGrid();  // talvez mudar isso para uma funcao principal, que essa irá herdar

          /*  // Calcular a HSP
            HSP = calculadoraOnGrid.MeanSolarHour(vetorCidade);

            // Calcular a Demanda de Energia Total - isso vai mudar
            this.potenciaUtilizadaDiariaCC = demandaEnergiaDiaria(vetorEquipamentos, true);
            this.potenciaUtilizadaDiariaCA = demandaEnergiaDiaria(vetorEquipamentos, false);

            // Calcular a Energia Ativa Necessária Diariamente
            this.energiaAtivaDia = energiaAtivaNecessariaDia(this.potenciaUtilizadaDiariaCA, this.potenciaUtilizadaDiariaCC);

            // Calcular Potência Mínima do Arranjo Fotovoltaico
            this.minPotencia = potenciaMinimaArranjoFotovoltaico(HSP,this.energiaAtivaDia);

            // Calcular Tensão do Sistema
            Vsist = defTensaoSistema(this.energiaAtivaDia);*/

            /*// Definindo as Placas  ---- isso eh para nao esquecer - lembre de criar  uma funcao parecida em CSVRead
            is = MyContext.getResources().openRawResource(R.raw.banco_paineis);
            placaEscolhida = CSVRead.DefineSolarPanel(is, 400, this.areaAlvo, this.idModuloEscolhido); // this.minPotencia
            area = calculadoraOnGrid.DefineArea(placaEscolhida);
            System.out.println("------------------------"+placaEscolhida[0]);*/

/*
            // Definindo o Banco de Baterias
            this.autonomia = numeroDiasAutonomia(vetorCidade); // deve ter um valor 2 <= n <= 4 dias
            this.CBI_C20 = (this.fatorSeguranca * this.energiaAtivaDia * this.autonomia) / (this.Pd * Vsist);
            //is = MyContext.getResources().openRawResource(R.raw.banco_baterias);
            bateriaEscolhida = CSVRead.DefineBattery(is, this.CBI_C20, this.Vsist, this.idBateriaEscolhida);
*/
            // Definindo o Controlador de Carga is,this.Vsist, 1, Integer.parseInt(placaEscolhida[Constants.iPANEL_QTD]), this.minPotencia, Integer.parseInt(placaEscolhida[Constants.iPANEL_POTENCIA]), this.idControladorEscolhido
    /*        is = MyContext.getResources().openRawResource(R.raw.banco_controladores);
            System.out.println("IS de fora: "+is);
            controladorEscolhido = CSVRead.DefineChargeController(is,24, 1, Integer.parseInt(placaEscolhida[Constants.iPANEL_QTD]), 400, Integer.parseInt(placaEscolhida[Constants.iPANEL_POTENCIA]), 2);//this.idControladorEscolhido
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("------Controlador--------- "+controladorEscolhido[Constants.iCON_ID]);
            System.out.println(" ");
            System.out.println(" ");
            // Definindo Quantidade de Placas em Série e Paralelo
            this.placaSerie = numModulosSerie(Integer.parseInt(controladorEscolhido[Constants.iCON_V_MAX_SISTEMA]), 1);// Descobrir como ter a Tensão de Máxima Potência de Temp. Máx.
            this.placaParalelo = numModulosParalelo(this.minPotencia, this.placaSerie,Integer.parseInt(this.placaEscolhida[Constants.iPANEL_POTENCIA])); // Descobrir como ter a Corrente de Máxima Potência
*/
            // Definindo os Inversores
            /*if(this.potenciaUtilizadaDiariaCA != 0){
                this.potenciaAparente = this.potenciaUtilizadaDiariaCA / this.fatorPotencia;
                // aqui fazer o 'is' receber o banco de dados dos inversores off-grid
                inversorEscolhido = CSVRead.DefineInvertorOffGrid(is, this.placaEscolhida ,this.potenciaAparente, this.Vsist, idInversorEscolhido);
            }
            else inversorEscolhido=null;*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////        Funções Auxiliares       ////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static double demandaEnergiaDiaria(ArrayList<Equipamentos> vetorEquipamentos, boolean Icontinua){
        double potenciaTotal=0;
        double rendimentoBat=0.9, rendimentoInv=0.9;

        if(Icontinua){
            for(int i=0; i<vetorEquipamentos.size(); i++){
                if(vetorEquipamentos.get(i).getCC()){
                    potenciaTotal = potenciaTotal + vetorEquipamentos.get(i).getPotencia()*vetorEquipamentos.get(i).getHorasPorDia()*vetorEquipamentos.get(i).getDiasUtilizados()/7;
                }
            }
            return potenciaTotal / (rendimentoBat * rendimentoInv);
        }

        for(int i=0; i<vetorEquipamentos.size(); i++){
            if(!vetorEquipamentos.get(i).getCC()){
                potenciaTotal = potenciaTotal + vetorEquipamentos.get(i).getPotencia()*vetorEquipamentos.get(i).getHorasPorDia()*vetorEquipamentos.get(i).getDiasUtilizados()/7;
            }
        }
        return potenciaTotal / (rendimentoBat * rendimentoInv);
    }

    public static double energiaAtivaNecessariaDia(double Lcc, double Lca){
        double L=0;
        double rendimentoBat=0.9;
        double rendimentoInv=0.9;
        L = Lcc/rendimentoBat + Lca/(rendimentoBat * rendimentoInv);
        return L;
    }

    public static double potenciaMinimaArranjoFotovoltaico(double HSP, double L){
        double Lmes=0;
        double Pi=0;
        double Pmax=0;
        double Red1Red2 = 0.9;

        for (int i=0; i<12; i++) {
            if (i == 1) Lmes = L * 28;
            else if (i % 2 == 0) Lmes = L * 31;
            else Lmes = L * 30;

            Pi = Lmes * (Red1Red2);

            if (Pi > Pmax) Pmax = Pi;
        }
        return Pmax * HSP;
    }

    public static double potenciaPico(double HSP, double minPotencia){
        double insolacao=0;
        double potenciaPico=0;

        insolacao = 1000 * HSP;
        potenciaPico = minPotencia / insolacao;

        return potenciaPico;
    }

    public static double horaSolarMes(String cityVec){
        double solarHour = 0.0;
        solarHour = Double.parseDouble(cityVec)/1000.0;
        return solarHour;
    }

    public static int numeroDiasAutonomia(String[] vetorCidade){
        double HSPmin=-1;
        double HSPmes=-1;
        int dias=2;
        for (int i=0; i<12; i++){
            HSPmes = horaSolarMes(vetorCidade[i]);
            if(HSPmin > HSPmes) HSPmin = HSPmes;
        }
        dias = (int) Math.round((-0.48 * HSPmin) + 4.58);
        if (dias < 2) return 2;
        else if(dias > 4) return 4;
        return dias;
    }

    public static int defTensaoSistema(double L){
        if(L <= 1000) return 12;
        if(L <= 4000) return 24;
        return 48;
    }

    public int numModulosSerie(int Vcontrolador, double Voc_corrigida){
        System.out.println("Entrou na funcao mudo serie");
        System.out.println("Vcontroller: "+Vcontrolador);
        System.out.println("Voc_corriegida: "+Voc_corrigida);
        int mSerie = (int)Math.round((Vcontrolador * 1.2) / Voc_corrigida);
        return mSerie;
    }

    public int numModulosParalelo(double P_pv, int qntPlacasSerie, int P_mod){
        return (int) Math.round(P_pv / (qntPlacasSerie * P_mod));
    }
}
