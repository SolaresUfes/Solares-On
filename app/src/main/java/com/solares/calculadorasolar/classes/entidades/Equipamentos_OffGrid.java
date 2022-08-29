package com.solares.calculadorasolar.classes.entidades;

import java.io.Serializable;

public class Equipamentos_OffGrid implements Serializable {
    String nome;
    double quantidade;
    double potencia;
    double horasPorDia;
    double diasUtilizados;
    boolean CC;

    public Equipamentos_OffGrid(){ }

    public void setNome(String nome){ this.nome=nome;}
    public void setQuantidade(double quantidade){ this.quantidade=quantidade;}
    public void setPotencia(double potencia){ this.potencia=potencia;}
    public void setHorasPorDia(double horasPorDia){ this.horasPorDia=horasPorDia;}
    public void setCC(boolean CC){ this.CC = CC;}
    public void setDiasUtilizados(double diasUtilizados){ this.diasUtilizados = diasUtilizados;}

    public String getNome(){ return this.nome;}
    public double getQuantidade(){ return this.quantidade;}
    public double getPotencia(){ return this.potencia;}
    public double getHorasPorDia(){ return this.horasPorDia;}
    public boolean getCC(){ return this.CC;}
    public double getDiasUtilizados(){ return this.diasUtilizados;}
}
