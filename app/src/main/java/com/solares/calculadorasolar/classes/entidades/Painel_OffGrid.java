package com.solares.calculadorasolar.classes.entidades;

import java.io.Serializable;

public class Painel_OffGrid implements Serializable {
    //Atributos
    private float Area;
    private String Codigo;
    private float CoefTempPot;
    private float Icc;
    private String Marca;
    private float NOCT;
    private String nome;
    private float Potencia;
    private float Preco;
    private float Vca;
    private int Qtd;
    private float CustoTotal;


    public Painel_OffGrid(){ /* Construtor Vazio */}

    public float getPreco() {
        return Preco;
    }

    public void setPreco(float preco) {
        Preco = preco;
    }

    public float getPotencia() {
        return Potencia;
    }

    public void setPotencia(float potencia) {
        Potencia = potencia;
    }

    public float getNOCT() {
        return NOCT;
    }

    public void setNOCT(float NOCT) {
        this.NOCT = NOCT;
    }

    public String getMarca() {
        if(Marca == null){
            return "";
        }
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public float getCoefTempPot() {
        return CoefTempPot;
    }

    public void setCoefTempPot(float coefTempPot) {
        CoefTempPot = coefTempPot;
    }

    public String getCodigo() {
        if(Codigo == null){
            return "";
        }
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public float getArea() {
        return Area;
    }

    public void setArea(float area) {
        Area = area;
    }

    public int getQtd() { return Qtd; }

    public void setQtd(int qtd) { Qtd = qtd; }

    public float getCustoTotal() { return CustoTotal; }

    public void setCustoTotal(float custoTotal) { CustoTotal = custoTotal; }

    public float getIcc() { return Icc;}

    public void setIcc(float Icc){ this.Icc = Icc; }

    public float getVca() { return Vca;}

    public void getVca(float Vca){ this.Vca = Vca; }

    public String getNome() { return nome;}

    public void setNome(String nome){ this.nome = nome; }
}
