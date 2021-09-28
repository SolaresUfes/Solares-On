package com.solares.calculadorasolar.classes.entidades;

import java.io.Serializable;

public class Painel implements Serializable {
    //Atributos
    private float Area;
    private String Codigo;
    private float CoefTempPot;
    private String Marca;
    private float NOCT;
    private float Potencia;
    private float Preco;
    private int Qtd;
    private float CustoTotal;


    public Painel(){ /* Construtor Vazio */}

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
}
