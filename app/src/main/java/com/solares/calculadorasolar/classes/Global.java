package com.solares.calculadorasolar.classes;

import java.util.ArrayList;
import android.app.Application;
import android.text.BoringLayout;

public class Global extends Application{
    private ArrayList<Equipamentos> meusEquipamentos = new ArrayList<>();
    private ArrayList<Integer> posSpinnerCategoria = new ArrayList<>();
    private String nomeEstado;
    private int idCity;
    private String nomeCidade;
    private boolean removerTodasViews=false;

    /* ----------------------- */
    /* --- Funcoes setters --- */
    public void adicionarElemento(Equipamentos equipamentos){ meusEquipamentos.add(equipamentos); }
    public void setMeusEquipamentos(ArrayList<Equipamentos> meusEquipamentos){ this.meusEquipamentos = meusEquipamentos; }
    public void alterarEquipamento(int posicao, Equipamentos equipamentos){ this.meusEquipamentos.set(posicao, equipamentos); }
    public void setNomeEstado(String nomeEstado){ this.nomeEstado=nomeEstado; }
    public void setIdCity(int IdCity){ this.idCity=IdCity; }
    public void setNomeCidade(String nomeCidade){ this.nomeCidade=nomeCidade; }
    public void setRemoverTodasViews(Boolean removerTodasViews){ this.removerTodasViews = removerTodasViews; }

    /* ----------------------- */
    /* --- Funcoes getters --- */
    public void printarNomeElemento(int a) { System.out.println(meusEquipamentos.get(a).getNome()); }
    public int getSizeEquipamento() { return meusEquipamentos.size();}
    public ArrayList<Equipamentos> getEquipamentos() { return meusEquipamentos;}
    public double getQuantidadeElemento(int elemento){ return meusEquipamentos.get(elemento).getQuantidade(); }
    public double getPotenciaElemento(int elemento){ return meusEquipamentos.get(elemento).getPotencia();}
    public String getNomeEstado(){ return this.nomeEstado; }
    public int getIdCity(){ return this.idCity; }
    public String getNomeCidade(){ return this.nomeCidade; }
    public boolean getRemoverTodasViews(){return this.removerTodasViews; }


    private static Global instance;

    public static Global getInstance() {
        if (instance == null)
            instance = new Global();
        return instance;
    }
}
