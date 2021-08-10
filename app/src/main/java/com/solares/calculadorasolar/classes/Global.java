package com.solares.calculadorasolar.classes;

import java.util.ArrayList;
import android.app.Application;

public class Global extends Application{
    private ArrayList<Equipamentos> meusEquipamentos = new ArrayList<>();
    private String nomeEstado;
    private int idCity;
    private String nomeCidade;

    /* ----------------------- */
    /* --- Funcoes setters --- */
    public void adicionarElemento(Equipamentos equipamentos){ meusEquipamentos.add(equipamentos); }
    public void setNomeEstado(String nomeEstado){ this.nomeEstado=nomeEstado; }
    public void setIdCity(int IdCity){ this.idCity=IdCity; }
    public void setNomeCidade(String nomeCidade){ this.nomeCidade=nomeCidade; }

    /* ----------------------- */
    /* --- Funcoes getters --- */
    public void printarNomeElemento(int a) { System.out.println(meusEquipamentos.get(a).getNome()); }
    public double getQuantidadeElemento(int elemento){ return meusEquipamentos.get(elemento).getQuantidade(); }
    public double getPotenciaElemento(int elemento){ return meusEquipamentos.get(elemento).getPotencia();}
    public String getNomeEstado(){ return this.nomeEstado; }
    public int getIdCity(){ return this.idCity; }
    public String getNomeCidade(){ return this.nomeCidade; }


    private static Global instance;

    public static Global getInstance() {
        if (instance == null)
            instance = new Global();
        return instance;
    }
}
