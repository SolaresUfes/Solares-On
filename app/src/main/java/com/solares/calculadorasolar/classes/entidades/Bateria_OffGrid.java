package com.solares.calculadorasolar.classes.entidades;

import java.io.Serializable;

public class Bateria_OffGrid implements Serializable {
    private float Area;
    private float C20;
    private float Capacidade;
    private String Marca;
    private String Nome;
    private float Preco;
    private float Vnom;
    private int Qtd;
    private float PrecoTotal;
    private int nSerie;
    private int nParalel;

    public Bateria_OffGrid(){/* Construtor Vazio */}

    public float getArea() {
        return Area;
    }

    public void setArea(float Area) { this.Area = Area;}

    public float getPreco() {
        return Preco;
    }

    public void setPreco(float preco) {
        Preco = preco;
    }

    public float getC20() {
        return C20;
    }

    public void setC20(float C20) {
        this.C20 = C20;
    }

    public float getCapacidade() {
        return Capacidade;
    }

    public void setCapacidade(float Capacidade) {
        this.Capacidade = Capacidade;
    }

    public float getVnom() {return Vnom;}

    public void setVnom(float Vnom) {this.Vnom = Vnom; }

    public String getMarca() {
        if(Marca == null){
            return "";
        }
        return Marca;
    }

    public String getNome() {
        if(Nome == null){
            return "";
        }
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public int getQtd() {
        return Qtd;
    }

    public void setQtd(int qtd) {
        Qtd = qtd;
    }

    public float getPrecoTotal() {
        return PrecoTotal;
    }

    public void setPrecoTotal(float precoTotal) {
        PrecoTotal = precoTotal;
    }

    public int getnSerie(){ return nSerie; }

    public void setnSerie(int nserie){ nSerie = nserie;}

    public int getnParalel(){ return nParalel; }

    public void setnParalel(int nparalel){ nParalel = nparalel;}
}
