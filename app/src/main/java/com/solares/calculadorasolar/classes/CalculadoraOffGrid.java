package com.solares.calculadorasolar.classes;
import android.content.Context;
import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraOffGrid {
    // Variáveis Privadas da classe
    double HSP=0;
    String[] vetorEstado;
    String[] vetorCidade;
    String nomeCidade;
    String[] placaEscolhida;
    String[] inversorEscolhido;
    String[] controladorEscolhido;
    ArrayList<ArrayList<String>> matrizEquipamentosCA;
    ArrayList<ArrayList<String>> matrizEquipamentosCC;
    int[] vetorQntEquipamentosCA;
    int[] vetorQntEquipamentosCC;
    double potenciaUtilizadaDiariaCC=0;
    double potenciaUtilizadaDiariaCA=0;
    double potenciaUtilizadaDiariaTotal=0;
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

    //////////////////////////
    ////  Funções getters ////
    //////////////////////////
    public String[] getVetorCidade(){ return vetorCidade; }

    //////////////////////////
    ////  Funções setters ////
    //////////////////////////
    public void setVetorCidade(String[] vetorCidade){ this.vetorCidade = vetorCidade;}
    public void setVetorEstado(String[] vetorEstado){
        this.vetorEstado = vetorEstado;
    }
    public void setNomeCidade(String nomeCidade){
        this.nomeCidade = nomeCidade;
    }
    public void setMatrizEquipamentosCA(ArrayList<ArrayList<String>> matrizEquipamentos){
        this.matrizEquipamentosCA = new ArrayList();
        this.matrizEquipamentosCA = matrizEquipamentos;
    }
    public void setMatrizEquipamentosCC(ArrayList<ArrayList<String>> matrizEquipamentos){
        this.matrizEquipamentosCC = new ArrayList();
        this.matrizEquipamentosCC = matrizEquipamentos;
    }
    public void setVetorQntEquipamentosCA(int[] vetorQntEquipamentos){ this.vetorQntEquipamentosCA = vetorQntEquipamentos;}
    public void setVetorQntEquipamentosCC(int[] vetorQntEquipamentos){ this.vetorQntEquipamentosCC = vetorQntEquipamentos;}
    public void setAreaAlvo(float AreaAlvo) { this.areaAlvo = AreaAlvo;}
    public void setIdModuloEscolhido(int idModuloEscolhido){ this.idModuloEscolhido = idModuloEscolhido;}
    public void setIdInversorEscolhido(int idInversorEscolhido){ this.idInversorEscolhido = idInversorEscolhido;}
    public void setIdControladorEscolhido(int idControladorEscolhido){ this.idControladorEscolhido=idControladorEscolhido;}

    CalculadoraOffGrid(){
        setAreaAlvo(-1f);
        setIdModuloEscolhido(-1);
        setIdInversorEscolhido(-1);
        setIdControladorEscolhido(-1);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////        Função Principal       /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Calcular(Context MyContext){
        InputStream is=null;

        try {
            CalculadoraOnGrid calculadoraOnGrid = new CalculadoraOnGrid();  // talvez mudar isso para uma funcao principal, que essa irá herdar

            // Calcular a HSP
            HSP = calculadoraOnGrid.MeanSolarHour(vetorCidade);

            // Calcular a Demanda de Energia Total - isso vai mudar
            // - corrente contínua
            this.potenciaUtilizadaDiariaCC = demandaEnergeticaDiaria(matrizEquipamentosCC, vetorQntEquipamentosCC);
            // - corrente alternada
            this.potenciaUtilizadaDiariaCA = demandaEnergeticaDiaria(matrizEquipamentosCA, vetorQntEquipamentosCA);
            //this.potenciaUtilizadaDiariaTotal = demandaEnergeticaDiaria(matrizEquipamentos, vetorQntEquipamentos);
            this.potenciaUtilizadaDiariaTotal = this.potenciaUtilizadaDiariaCC + this.potenciaUtilizadaDiariaCA;

            // Calcular a Energia Ativa Necessária Diariamente
            this.energiaAtivaDia = energiaAtivaNecessariaDia(this.potenciaUtilizadaDiariaCA, this.potenciaUtilizadaDiariaCC);

            // Calcular Potência Mínima do Arranjo Fotovoltaico
            this.minPotencia = potenciaMinimaArranjoFotovoltaico(HSP,this.energiaAtivaDia);

            // Calculo da Potência Pico do Sistema

            // Calcular Tensão do Sistema
            Vsist = defTensaoSistema(this.energiaAtivaDia);

            // Definindo as Placas  ---- isso eh para nao esquecer - lembre de criar  uma funcao parecida em CSVRead
            is = MyContext.getResources().openRawResource(R.raw.banco_paineis);
            placaEscolhida = CSVRead.DefineSolarPanel(is, this.minPotencia, this.areaAlvo, this.idModuloEscolhido);
            area = calculadoraOnGrid.DefineArea(placaEscolhida);
            // Definindo a quantidade de placas em série e paralelo
            // Quantidade de placas em série e paralelo
            this.placaSerie = (int) Math.round((this.Vsist * 1.2) / this.tensaoMaxPowerTempMax); // Descobrir como ter a Tensão de Máxima Potência de Temp. Máx.
            this.placaParalelo = (int) Math.round(this.minPotencia / (this.placaSerie)); // Descobrir como ter a Corrente de Máxima Potência

            // Definindo o Banco de Baterias
            this.autonomia = numeroDiasAutonomia(vetorCidade); // deve ter um valor 2 <= n <= 4 dias
            this.CBI_C20 = (this.fatorSeguranca * this.energiaAtivaDia * this.autonomia) / (this.Pd * Vsist);
                /* --- Aqui definir qual bateria será utilizada - arqCSV --- */
            // Quantidade de baterias em série e em paralelo
            nBatSerie = (int)Math.round(Vsist/Vbat);
            nBatParalelo = (int)Math.round(this.CBI_C20/this.CBI_bat);
            // Quantidade total de baterias
            this.qntBat = this.nBatParalelo * this.nBatSerie;

            // Definindo o Controlador de Carga
            // aqui fazer o 'is' receber o banco de dados dos inversores off-grid
            controladorEscolhido = CSVRead.DefineChargeController(is, this.placaParalelo ,this.placaSerie, this.Vsist, idInversorEscolhido);

            // Definindo os Inversores
            if(this.potenciaUtilizadaDiariaCA != 0){
                this.potenciaAparente = this.potenciaUtilizadaDiariaCA / this.fatorPotencia;
                // aqui fazer o 'is' receber o banco de dados dos inversores off-grid
                inversorEscolhido = CSVRead.DefineInvertorOffGrid(is, this.placaEscolhida ,this.potenciaAparente, this.Vsist, idInversorEscolhido);
            }
            else inversorEscolhido=null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static double demandaEnergeticaDiaria(ArrayList<ArrayList<String>> matrizEquipamentos, int[] vetorQntEquipamentos){ // isso vai mudar
        double totalPower=0;
        double eachElementPower;
        double powerEquipment=0;
        double rendimentoBat=0.9;
        double rendimentoInv=0.9;

        for(int c=0; c < matrizEquipamentos.size(); c++){
            powerEquipment = Double.parseDouble(matrizEquipamentos.get(1).get(c)) * vetorQntEquipamentos[c];
            eachElementPower=1;
            for(int l=1; l < matrizEquipamentos.get(c).size(); l++){
                eachElementPower = Double.parseDouble(matrizEquipamentos.get(l).get(c)) * eachElementPower * powerEquipment / 7;
            }
            totalPower = eachElementPower + totalPower;
        }
        return totalPower / (rendimentoBat * rendimentoInv);
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
        double Red1 = 0.75;
        double Red2 = 0.9;
        for (int i=0; i<12; i++) {
            if (i == 1) Lmes = L * 28;
            else if (i % 2 == 0) Lmes = L * 31;
            else Lmes = L * 30;

            Pi = Lmes * (Red1 * Red2);

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
}
