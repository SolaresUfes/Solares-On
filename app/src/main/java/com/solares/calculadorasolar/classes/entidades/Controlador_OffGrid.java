package com.solares.calculadorasolar.classes.entidades;

import java.io.Serializable;

public class Controlador_OffGrid implements Serializable {
    private float Area;
    private float Corrente_carga;
    private String Marca;
    private String Nome;
    private float Preco;
    private float Tensao_bateria;
    private float Tensao_max_sistema;
    private int Qtd;
    private float PrecoTotal;

    public Controlador_OffGrid(){/* Construtor Vazio */}

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

    public float getCorrente_carga() {
        return Corrente_carga;
    }

    public void setCorrente_carga(float Corrente_carga) {
        this.Corrente_carga = Corrente_carga;
    }

    public float getTensao_bateria(){return Tensao_bateria;}

    public void setTensao_bateria(float Tensao_bateria){this.Tensao_bateria = Tensao_bateria;}

    public float getTensao_max_sistema(){return Tensao_max_sistema;}

    public void setTensao_max_sistema(float Tensao_max_sistema){this.Tensao_max_sistema = Tensao_max_sistema;}

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
}
