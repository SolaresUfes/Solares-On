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
    ArrayList<ArrayList<String>> matrizEquipamentos;
    int[] vetorQntEquipamentos;
    double potenciaUtilizadaDiaria=0;
    double horasDeSolPleno=0;

    //////////////////////////
    ////  Funções setters ////
    //////////////////////////
    public void setVetorCidade(String[] vetorCidade){ this.vetorCidade = vetorCidade;}
    public void setMatrizEquipamentos(ArrayList<ArrayList<String>> matrizEquipamentos){
        this.matrizEquipamentos = new ArrayList();
        this.matrizEquipamentos = matrizEquipamentos;
    }
    public void setVetorQntEquipamentos(int[] vetorQntEquipamentos){this.vetorQntEquipamentos = vetorQntEquipamentos;}

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////        Função Principal       /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Calcular(Context MyContext){
        InputStream is=null;
        try {
            CalculadoraOnGrid calculadoraOnGrid = new CalculadoraOnGrid();  // talvez mudar isso para uma funcao principal, que essa irá herdar

            // calcular a Hora de Sol Pleno do local
            horasDeSolPleno = calculadoraOnGrid.MeanSolarHour(vetorCidade);

            // calcular a Demanda de Energia Total
            potenciaUtilizadaDiaria = demandaEnergetica(matrizEquipamentos, vetorQntEquipamentos); // talvez nao precise dos parametros

            // calcular o
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static double demandaEnergetica(ArrayList<ArrayList<String>> matrizEquipamentos, int[] vetorQntEquipamentos){
        double totalPower=0;
        double eachElementPower=1;
        double powerEquipament=0;

        for(int c=0; c < matrizEquipamentos.size(); c++){
            powerEquipament = Double.parseDouble(matrizEquipamentos.get(1).get(c)) * vetorQntEquipamentos[c];
            for(int l=1; l < matrizEquipamentos.get(c).size(); l++){
                eachElementPower = Double.parseDouble(matrizEquipamentos.get(l).get(c)) * eachElementPower * powerEquipament / 7;
            }
            totalPower = eachElementPower + totalPower;
        }
        return totalPower;
    }
}
