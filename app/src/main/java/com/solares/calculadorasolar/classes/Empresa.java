package com.solares.calculadorasolar.classes;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Empresa {
    public String telefone;
    public String site;
    public String nome;

    public Empresa(){} //// Default constructor required for calls to DataSnapshot.getValue(User.class)

    public Empresa(String nome){
        this.nome = nome;
    }

    public void CopyFrom(Empresa empresa){
        this.nome = empresa.getNome();
        this.site = empresa.getSite();
        this.telefone = empresa.getTelefone();
    }

    public String getTelefone(){ return this.telefone; }
    public String getSite(){ return this.site; }
    public String getNome(){ return this.nome; }
}
