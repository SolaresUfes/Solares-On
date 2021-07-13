package com.solares.calculadorasolar.classes;
import android.content.Context;
import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.CalculadoraOnGrid;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraOffGrid {
    // Variáveis Privadas da classe
    String[] vetorCidade;
    String[] placaEscolhida;
    String[] inversor;
    ArrayList<ArrayList<String>> matrizEquipamentosCA;
    ArrayList<ArrayList<String>> matrizEquipamentosCC;
    int[] vetorQntEquipamentosCA;
    int[] vetorQntEquipamentosCC;
    double potenciaUtilizadaDiariaCC=0;
    double potenciaUtilizadaDiariaCA=0;
    double potenciaUtilizadaDiariaTotal=0;
    double horasDeSolPleno=0;
    double energiaAtivaDia=0;
    double minPotencia=0;
    double area=0;
    int autonomia=2;
    int Vsis=0;
    int Vbat=0;
    double CBI_C20=0;
    double Pd=0.3;
    double fatorSeguranca=1.25;
    int nBatSerie=0;
    int nBatParalelo=0;
    double CBI_bat=0;
    int qntBat=0;

    float areaAlvo=-1f;
    int idModuloEscolhido=-1;
    int idInversorEscolhido=-1;

    //////////////////////////
    ////  Funções setters ////
    //////////////////////////
    public void setVetorCidade(String[] vetorCidade){ this.vetorCidade = vetorCidade;}
    public void setMatrizEquipamentosCA(ArrayList<ArrayList<String>> matrizEquipamentos){
        this.matrizEquipamentosCA = new ArrayList();
        this.matrizEquipamentosCA = matrizEquipamentos;
    }
    public void setMatrizEquipamentosCC(ArrayList<ArrayList<String>> matrizEquipamentos){
        this.matrizEquipamentosCC = new ArrayList();
        this.matrizEquipamentosCC = matrizEquipamentos;
    }
    public void setVetorQntEquipamentosCA(int[] vetorQntEquipamentos){this.vetorQntEquipamentosCA = vetorQntEquipamentos;}
    public void setVetorQntEquipamentosCC(int[] vetorQntEquipamentos){this.vetorQntEquipamentosCC = vetorQntEquipamentos;}
    public void setAreaAlvo(float AreaAlvo) { this.areaAlvo = AreaAlvo; }
    public void setIdModuloEscolhido(int idModuloEscolhido){this.idModuloEscolhido = idModuloEscolhido;}


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////        Função Principal       /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Calcular(Context MyContext){
        InputStream is=null;

        try {
            CalculadoraOnGrid calculadoraOnGrid = new CalculadoraOnGrid();  // talvez mudar isso para uma funcao principal, que essa irá herdar

            // Calcular a Demanda de Energia Total
            // - corrente contínua
            this.potenciaUtilizadaDiariaCC = demandaEnergeticaDiaria(matrizEquipamentosCC, vetorQntEquipamentosCC);
            // - corrente alternada
            this.potenciaUtilizadaDiariaCA = demandaEnergeticaDiaria(matrizEquipamentosCA, vetorQntEquipamentosCA);
            //this.potenciaUtilizadaDiariaTotal = demandaEnergeticaDiaria(matrizEquipamentos, vetorQntEquipamentos);
            this.potenciaUtilizadaDiariaTotal = this.potenciaUtilizadaDiariaCC + this.potenciaUtilizadaDiariaCA;

            // Calcular a Energia Ativa Necessária Diariamente
            this.energiaAtivaDia = energiaAtivaNecessariaDia(this.potenciaUtilizadaDiariaCA, this.potenciaUtilizadaDiariaCC);

            // Calcular Potência Mínima do Arranjo Fotovoltaico
            this.minPotencia = potenciaMinimaArranjoFotovoltaico(vetorCidade,this.energiaAtivaDia);

            // Definindo as Placas  ---- isso eh para nao esquecer - lembre de criar  uma funcao parecida em CSVRead
            is = MyContext.getResources().openRawResource(R.raw.banco_paineis);
            placaEscolhida = CSVRead.DefineSolarPanel(is, this.minPotencia, this.areaAlvo, this.idModuloEscolhido);
            area = calculadoraOnGrid.DefineArea(placaEscolhida);

            // Definindo o Banco de Baterias
            this.autonomia = numeroDiasAutonomia(vetorCidade); // deve ter um valor 2 <= n <= 4 dias
            Vsis = defTensaoSistema(this.energiaAtivaDia);
            this.CBI_C20 = (this.fatorSeguranca * this.energiaAtivaDia * this.autonomia) / (this.Pd * Vsis);
                /* --- Aqui definir qual bateria será utilizada --- */
            // Quantidade de baterias em série e em paralelo
            nBatSerie = (int)Math.round(Vsis/Vbat);
            nBatParalelo = (int)Math.round(this.CBI_C20/this.CBI_bat);
            // Quantidade total de baterias
            this.qntBat = this.nBatParalelo * this.nBatSerie;

            // Definindo o Controlador de Carga
                // sem mppt

            // Definindo os Inversores

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static double demandaEnergeticaDiaria(ArrayList<ArrayList<String>> matrizEquipamentos, int[] vetorQntEquipamentos){
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

    public static double potenciaMinimaArranjoFotovoltaico(String[] vetorCidade, double L){
        double Lmes=0;
        double HSPi=0;
        double Pi=0;
        double Pmax=0;
        double Red1 = 0.75;
        double Red2 = 0.9;
        for (int i=0; i<12; i++){
            if(i==1) Lmes = L * 28;
            else if(i%2==0) Lmes = L*31;
            else Lmes = L * 30;

            HSPi = horaSolarMes(vetorCidade[i]);
            Pi = Lmes * (HSPi * Red1 * Red2);

            if(Pi > Pmax) Pmax = Pi;
        }
        return Pmax;
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
