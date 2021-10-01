package com.solares.calculadorasolar.classes.entidades;

import java.io.Serializable;

public class Inversor implements Serializable {
    private String Codigo;
    private String Marca;
    private float Potencia;
    private float Preco;
    private float RendimentoMaximo;
    private int Qtd;
    private float PrecoTotal;

    public Inversor(){/* Construtor Vazio */}

    public float getRendimentoMaximo() {
        return RendimentoMaximo;
    }

    public void setRendimentoMaximo(float rendimentoMaximo) {
        RendimentoMaximo = rendimentoMaximo;
    }

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

    public String getMarca() {
        return Marca;
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

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }
}
