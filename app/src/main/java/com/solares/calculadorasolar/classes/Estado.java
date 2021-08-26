package com.solares.calculadorasolar.classes;

import java.util.ArrayList;
import java.util.HashMap;

public class Estado {
    public String nome;
    public String todo;
    public ArrayList<String> cidades;

    public Estado(){} //// Default constructor required for calls to DataSnapshot.getValue(User.class)

    public Estado(String nome, String todo){
        this.nome = nome;
        this.todo = todo;
    }


    public String getNome() {
        return nome;
    }

    public String getTodo() {
        return todo;
    }

    public ArrayList<String> getCidades() {
        return cidades;
    }
}
