package com.solares.calculadorasolar.classes.entidades;

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
