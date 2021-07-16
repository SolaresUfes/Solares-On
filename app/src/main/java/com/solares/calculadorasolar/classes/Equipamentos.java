package com.solares.calculadorasolar.classes;

public class Equipamentos {
    String nome;
    int quantidade;
    double potencia;
    int horasPorDia;

    Equipamentos(){
        setNome(null);
        setQuantidade(0);
        setPotencia(0);
        setHorasPorDia(0);
    }

    public void setNome(String nome){ this.nome=nome;}
    public void setQuantidade(int quantidade){ this.quantidade=quantidade;}
    public void setPotencia(double potencia){ this.potencia=potencia;}
    public void setHorasPorDia(int horasPorDia){ this.horasPorDia=horasPorDia;}

    public String getNome(){ return this.nome;}
    public int getQuantidade(){ return this.quantidade;}
    public double getPotencia(){ return this.potencia;}
    public int getHorasPorDia(){ return this.horasPorDia;}
}
