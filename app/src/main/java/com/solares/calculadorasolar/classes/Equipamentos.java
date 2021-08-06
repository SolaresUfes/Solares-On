package com.solares.calculadorasolar.classes;

public class Equipamentos {
    String nome;
    int quantidade;
    double potencia;
    int horasPorDia;
    int diasUtilizados;
    boolean CC;

    public Equipamentos(){
        setNome(null);
        setQuantidade(0);
        setPotencia(0);
        setHorasPorDia(0);
        setDiasUtilizados(0);
        setCC(false);
    }

    Equipamentos(String nome, int quantidade, int horasPorDia, int diasUtilizados, double potencia, boolean CC) {
        setNome(nome);
        setQuantidade(quantidade);
        setHorasPorDia(horasPorDia);
        setDiasUtilizados(diasUtilizados);
        setPotencia(potencia);
        setCC(CC);
    }

    public void setNome(String nome){ this.nome=nome;}
    public void setQuantidade(int quantidade){ this.quantidade=quantidade;}
    public void setPotencia(double potencia){ this.potencia=potencia;}
    public void setHorasPorDia(int horasPorDia){ this.horasPorDia=horasPorDia;}
    public void setCC(boolean CC){ this.CC = CC;}
    public void setDiasUtilizados(int diasUtilizados){ this.diasUtilizados = diasUtilizados;}

    public String getNome(){ return this.nome;}
    public int getQuantidade(){ return this.quantidade;}
    public double getPotencia(){ return this.potencia;}
    public int getHorasPorDia(){ return this.horasPorDia;}
    public boolean getCC(){ return this.CC;}
    public int getDiasUtilizados(){ return this.diasUtilizados;}
}
