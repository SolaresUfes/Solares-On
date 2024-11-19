package com.solares.calculadorasolar.classes.auxiliares;

public class Constants {

    // Constantes para cálculo
    public static double ICMS = 0.25; //É o valor usado pela maioria dos estados
    public static double PIS = 0.0120;
    public static double COFINS = 0.0553;
    public static double CIPMAX = 50; // 50 reais
    public static double CIPMIN = 8; //8 reais
    //public static double TARIFF = 0.56228;
    public static double TD = 0.8;
    public static double COST_DISP_MONOFASICO = 30.0;
    public static double COST_DISP_BIFASICO = 50.0;
    public static double COST_DISP_TRIFASICO = 100.0;
    public static double PERCENTUAL_COST_INSTALATION = 0.35;
    public static double LOSS_DIRT = 0.01; //Perda de geração devido a sujeira
    public static double DEPREC_PANELS = 0.007; //Perda de eficiência a cada ano
    public static double INVERTOR_TIME = 10;
    public static double IPCA = 0.03;
    public static double MAINTENANCE_COST = 0.01;
    public static double COST_OF_CAPITAL = 0.065; //Taxa Selic * Retorno Esperado
    public static double SELIC = 0.065;
    public static double TARIFF_CHANGE = 6;
    public static double PANEL_LIFE = 25;

    // Constantes para o calculo das baterias - off grid
    public static double fatorSeguranca = 1.25;
    public static double Pd = 0.8;

    // Strings para passagem de parâmetros para outras activities
    public static final String EXTRA_CALCULADORAON = "com.solares.com.solares.calculadorasolar.EXTRA_CALCULADORAON";
    public static final String EXTRA_CALCULADORAOFF = "com.solares.com.solares.calculadorasolar.EXTRA_CALCULADORAOFF";
    public static final String EXTRA_EQUIPAMENTO = "com.solares.calculadorasolar.classes.entidades.Equipamentos_OffGrid";
    public static final String EXTRA_CUSTO_REAIS = "com.solares.com.solares.calculadorasolar.EXTRA_CUSTO_REAIS";
    public static final String EXTRA_CONSUMO = "com.solares.com.solares.calculadorasolar.EXTRA_CONSUMO";
    public static final String EXTRA_POTENCIA = "com.solares.com.solares.calculadorasolar.EXTRA_POTENCIA";
    public static final String EXTRA_PLACAS = "com.solares.com.solares.calculadorasolar.EXTRA_PLACAS";
    public static final String EXTRA_AREA = "com.solares.com.solares.calculadorasolar.EXTRA_AREA";
    public static final String EXTRA_INVERSORES = "com.solares.com.solares.calculadorasolar.EXTRA_INVERSORES";
    public static final String EXTRA_CUSTO_PARCIAL = "com.solares.com.solares.calculadorasolar.EXTRA_CUSTO_PARCIAL";
    public static final String EXTRA_CUSTO_TOTAL = "com.solares.com.solares.calculadorasolar.EXTRA_CUSTO_TOTAL";
    public static final String EXTRA_GERACAO = "com.solares.com.solares.calculadorasolar.EXTRA_GERACAO";
    public static final String EXTRA_LUCRO = "com.solares.com.solares.calculadorasolar.EXTRA_LUCRO";
    public static final String EXTRA_TAXA_DE_RETORNO = "com.solares.com.solares.calculadorasolar.EXTRA_TAXA_DE_RETORNO";
    public static final String EXTRA_INDICE_LUCRATICVIDADE = "com.solares.com.solares.calculadorasolar.EXTRA_INDICE_LUCRATICVIDADE";
    public static final String EXTRA_LCOE = "com.solares.com.solares.calculadorasolar.EXTRA_LCOE";
    public static final String EXTRA_TEMPO_RETORNO = "com.solares.com.solares.calculadorasolar.EXTRA_TEMPO_RETORNO";
    public static final String EXTRA_HORA_SOLAR = "com.solares.com.solares.calculadorasolar.EXTRA_HORA_SOLAR";
    public static final String EXTRA_ID_CIDADE = "com.solares.com.solares.calculadorasolar.EXTRA_ID_CIDADE";
    public static final String EXTRA_CIDADE = "com.solares.com.solares.calculadorasolar.EXTRA_CIDADE";
    public static final String EXTRA_VETOR_CIDADE = "com.solares.com.solares.calculadorasolar.EXTRA_VETOR_CIDADE";
    public static final String EXTRA_TARIFA = "com.solares.com.solares.calculadorasolar.EXTRA_TARIFA";
    public static final String EXTRA_ECONOMIA_MENSAL = "com.solares.com.solares.calculadorasolar.EXTRA_ECONOMIA_MENSAL";

    //Indices Paineis
    public static final int iPANEL_CODIGO = 1;
    public static final int iPANEL_MARCA = 2;
    public static final int iPANEL_POTENCIA = 3;
    public static final int iPANEL_PRECO = 4;
    public static final int iPANEL_AREA = 5;
    public static final int iPANEL_COEFTEMP = 6;
    public static final int iPANEL_NOCT = 7;


    //Indices Inversores
    public static final int iINV_CODIGO = 1;
    public static final int iINV_MARCA = 2;
    public static final int iINV_POTENCIA = 3;
    public static final int iINV_PRECO = 4;
    public static final int iINV_RENDIMENTO_MAXIMO = 5;


    //Indices Equipamentos
    public static final int iEQUI_NOME = 2;
    public static final int iEQUI_POT = 0;
    public static final int iEQUI_CC = 1;
    public static final int iEQUI_ID = 3;


    //Indices Cidades
    public static final int iCID_ESTADO = 0;
    //Indices Estados
    public static final int iEST_SIGLA = 0;
    public static final int iEST_TARIFA = 13;

    //Indices costs[]
    public static final int iCOSTS_PARCIAL = 0;
    public static final int iCOSTS_TOTAL = 1;

}
