package com.solares.calculadorasolar.classes.entidades;

import java.io.Serializable;

public class Inversor_OffGrid implements Serializable {
    private String Codigo;
    private String Marca;
    private String Nome;
    private float Potencia;
    private float Preco;
    private float RendimentoMaximo;
    private float TensaoEntrada;
    private float TensaoSaida;
    private int Qtd;
    private float PrecoTotal;

    public Inversor_OffGrid(){/* Construtor Vazio */}

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

    public float getTensaoEntrada(){return TensaoEntrada;}

    public void setTensaoEntrada(float TensaoEntrada){this.TensaoEntrada = TensaoEntrada;}

    public float getTensaoSaida(){return TensaoSaida;}

    public void setTensaoSaida(float TensaoSaida){this.TensaoSaida = TensaoSaida;}

    public String getMarca() {
        if(Marca == null){
            return "";
        }
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
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
        if(Codigo == null){
            return "";
        }
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }
}
