package com.solares.calculadorasolar.classes;

import android.content.Context;

import com.solares.calculadorasolar.classes.entidades.Bateria_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Controlador_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Equipamentos_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Inversor_OffGrid;
import com.solares.calculadorasolar.classes.entidades.Painel_OffGrid;

import java.io.Serializable;
import java.util.LinkedList;

public class CalculadoraOffGrid implements Serializable {
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
    String nomeCidade;
    Painel_OffGrid placaEscolhida;
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
    public LinkedList<Inversor_OffGrid> pegaListaInversoresOffGrid() { return listaInversores_off_grid; }
    public String[] pegaNomesInversoresOffGrid() { return nomesInversores_off_grid; }
    public LinkedList<Controlador_OffGrid> pegaListaControladoresOffGrid() { return listaControladores_off_grid; }
    public String[] pegaNomesControladoresOffGrid() { return nomesControladores_off_grid; }
    public LinkedList<Bateria_OffGrid> pegaListaBateriasOffGrid() { return listaBaterias_off_grid; }
    public String[] pegaNomesBateriasOffGrid() { return nomesBaterias_off_grid; }
    public LinkedList<Equipamentos_OffGrid> pegaListaEquipamentosOffGrid() { return listaEquipamentos_off_grid; }
    public String pegaNomeCidade(){ return nomeCidade; }
    public double pegaCustoReais(){ return custoReais; }
    public double pegaConsumokWhs(){ return consumokWh; }
    public double pegaPotenciaNecessaria(){ return potenciaNecessaria; }
    public double pegaPotenciaInstalada(){ return potenciaInstalada; }
    public double pegaArea(){ return area; }
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
    public void setListaBateriasOffGrid(LinkedList<Bateria_OffGrid> listaBateriasOffGrid) { this.listaBaterias_off_grid = listaBaterias_off_grid; }
    public void setListaEquipamentosOffGrid(LinkedList<Equipamentos_OffGrid> listaEquipamentosOffGrid) { this.listaEquipamentos_off_grid = listaEquipamentosOffGrid; }
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

    }
}
