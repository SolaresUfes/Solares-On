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
    ArrayList<ArrayList<String>> matrizEquipamentosCA;
    ArrayList<ArrayList<String>> matrizEquipamentosCC;
    int[] vetorQntEquipamentosCA;
    int[] vetorQntEquipamentosCC;
    double potenciaUtilizadaDiariaCC=0;
    double potenciaUtilizadaDiariaCA=0;
    double potenciaUtilizadaDiariaTotal=0;
    double horasDeSolPleno=0;
    double energiaNecessariaDia=0;
    double area=0;

    float areaAlvo;
    int idModuloEscolhido;
    int idInversorEscolhido;

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
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////        Função Principal       /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Calcular(Context MyContext){
        InputStream is=null;

        try {
            CalculadoraOnGrid calculadoraOnGrid = new CalculadoraOnGrid();  // talvez mudar isso para uma funcao principal, que essa irá herdar

            // Calcular a Hora de Sol Pleno do Local
            this.horasDeSolPleno = calculadoraOnGrid.MeanSolarHour(vetorCidade);

            // Calcular a Demanda de Energia Total
            // - corrente contínua
            this.potenciaUtilizadaDiariaCC = demandaEnergeticaDiaria(matrizEquipamentosCC, vetorQntEquipamentosCC);
            // - corrente alternada
            this.potenciaUtilizadaDiariaCA = demandaEnergeticaDiaria(matrizEquipamentosCA, vetorQntEquipamentosCA);
            //this.potenciaUtilizadaDiariaTotal = demandaEnergeticaDiaria(matrizEquipamentos, vetorQntEquipamentos); // talvez nao precise dos parametros
            this.potenciaUtilizadaDiariaTotal = this.potenciaUtilizadaDiariaCC + this.potenciaUtilizadaDiariaCA;

            // Calcular a Energia Ativa Necessária Diariamente
            //this.energiaNecessariaDia = 1;

            //Definindo as placas
            is = MyContext.getResources().openRawResource(R.raw.banco_paineis);
            placaEscolhida = CSVRead.DefineSolarPanel(is, potenciaUtilizadaDiariaTotal, this.areaAlvo, this.idModuloEscolhido);
            area = calculadoraOnGrid.DefineArea(placaEscolhida);
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static double demandaEnergeticaDiaria(ArrayList<ArrayList<String>> matrizEquipamentos, int[] vetorQntEquipamentos){
        double totalPower=0;
        double eachElementPower;
        double powerEquipment=0;

        for(int c=0; c < matrizEquipamentos.size(); c++){
            powerEquipment = Double.parseDouble(matrizEquipamentos.get(1).get(c)) * vetorQntEquipamentos[c];
            eachElementPower=1;
            for(int l=1; l < matrizEquipamentos.get(c).size(); l++){
                eachElementPower = Double.parseDouble(matrizEquipamentos.get(l).get(c)) * eachElementPower * powerEquipment / 7;
            }
            totalPower = eachElementPower + totalPower;
        }
        return totalPower / 0.9;
    }
}
