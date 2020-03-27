package com.solares.calculadorasolar.classes;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class CSVRead {

    private static String divider = ",";

    public static String[] getCity(int idCity, String stateName, InputStream is){
        String[] values = new String[0];
        BufferedReader bufferedReader=null;
        int currentLine=0;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            bufferedReader.readLine(); //Ignora a primeira linha (cabeçário)
            while ((line = bufferedReader.readLine()) != null) {
                values = line.split(divider);
                if (stateName.equals(values[0])){ //Se essa linha for a primeira linha do estado correto
                    currentLine = 0;
                    if (idCity == currentLine) { //Se for igual a zero (primeira cidade)
                        //Fechar o bufferedReader
                        try {
                            bufferedReader.close();
                        } catch (IOException ioex){
                            ioex.printStackTrace();
                        }

                        return values;
                    }
                    currentLine++;
                    while ((line = bufferedReader.readLine()) != null) {
                        values = line.split(divider);
                        if (idCity == currentLine) {
                            //Fechar o bufferedReader
                            try {
                                bufferedReader.close();
                            } catch (IOException ioex){
                                ioex.printStackTrace();
                            }

                            return values;
                        }
                        currentLine++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader
            try {
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
            } catch (IOException ioex){
                ioex.printStackTrace();
            }
        }

        return null;
    }

    public static String[] DefineSolarPanel(InputStream is, double WpNeeded, float AreaAlvo){
        String[] cheaperPanel;
        String[] currentPanel;
        String line;
        double currentCost, cheaperCost, precoTotal;
        BufferedReader bufferedReader=null;

        try{
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            bufferedReader.readLine();
            line = bufferedReader.readLine();
            cheaperPanel = line.split(divider);
            //O custo aqui é definido por preçoTotal/potênciaTotal (R$/Wp)
            cheaperCost = FindPanelCost(WpNeeded, cheaperPanel, AreaAlvo);

            if(AreaAlvo == -1f){
                cheaperPanel[Constants.iPANEL_QTD] = String.valueOf((int)Math.floor(WpNeeded/Double.parseDouble(cheaperPanel[Constants.iPANEL_POTENCIA])));
            } else {
                cheaperPanel[Constants.iPANEL_QTD] = String.valueOf((int)Math.floor(AreaAlvo / Double.parseDouble(cheaperPanel[Constants.iPANEL_AREA])));
            }

            //O custo aqui é em dinheiro mesmo (quantidade de dinheiro "bruta")
            precoTotal = Double.parseDouble(cheaperPanel[Constants.iPANEL_QTD]) * Double.parseDouble(cheaperPanel[Constants.iPANEL_PRECO]);
            cheaperPanel[Constants.iPANEL_CUSTO_TOTAL] = String.valueOf(precoTotal);

            while ((line = bufferedReader.readLine()) != null){
                currentPanel = line.split(divider);
                //O custo aqui é definido por preçoTotal/potênciaTotal
                currentCost = FindPanelCost(WpNeeded, currentPanel, AreaAlvo);

                if(AreaAlvo == -1f){
                    currentPanel[Constants.iPANEL_QTD] = String.valueOf((int)Math.floor(WpNeeded/Double.parseDouble(currentPanel[Constants.iPANEL_POTENCIA])));
                } else {
                    currentPanel[Constants.iPANEL_QTD] = String.valueOf((int)Math.floor(AreaAlvo / Double.parseDouble(currentPanel[Constants.iPANEL_AREA])));
                }

                //O custo aqui é em dinheiro mesmo
                precoTotal = Double.parseDouble(currentPanel[Constants.iPANEL_QTD]) * Double.parseDouble(currentPanel[Constants.iPANEL_PRECO]);
                currentPanel[Constants.iPANEL_CUSTO_TOTAL] = String.valueOf(precoTotal);
                if(currentCost<cheaperCost){
                    cheaperCost = currentCost;
                    cheaperPanel = currentPanel;
                }
            }
            //Fechar o bufferedReader
            try {
                bufferedReader.close();
            } catch (IOException ioex){
                ioex.printStackTrace();
            }

            return cheaperPanel;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader se ocorreu algum erro
            try {
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
            } catch (IOException ioex){
                ioex.printStackTrace();
            }
        }

        //Se der algum erro, retorna uma string vazia
        String[] error = {""};
        return error;

    }

    private static double FindPanelCost(double WpNeeded, @NotNull String[] dataPanel, float AreaAlvo){
        int qtd;
        double precoTotal, potenciaTotal;
        if(AreaAlvo == -1f){
            qtd = (int)Math.floor(WpNeeded/Double.parseDouble(dataPanel[Constants.iPANEL_POTENCIA]));
            precoTotal = qtd*Double.parseDouble(dataPanel[Constants.iPANEL_PRECO]);
            potenciaTotal = qtd*Double.parseDouble(dataPanel[Constants.iPANEL_POTENCIA]);

            return precoTotal/potenciaTotal;
        } else {
            qtd = (int)Math.floor(AreaAlvo / Double.parseDouble(dataPanel[Constants.iPANEL_AREA]));
            precoTotal = qtd*Double.parseDouble(dataPanel[Constants.iPANEL_PRECO]);
            potenciaTotal = qtd*Double.parseDouble(dataPanel[Constants.iPANEL_POTENCIA]);

            return precoTotal/potenciaTotal;
        }
    }

    public static String[] getState(String[] cityVec, InputStream is){
        String stateName = cityVec[Constants.iCID_ESTADO];
        String[] values;
        String line;
        BufferedReader bufferedReader=null;

        try{
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            //Passar por todas as linhas do arquivo de estados
            while ((line = bufferedReader.readLine()) != null) {
                values = line.split(divider);
                if (stateName.equals(values[0])){ //Se essa linha for a do estado correto

                    //Fechar o bufferedReader
                    try {
                        bufferedReader.close();
                    } catch (IOException ioex){
                        ioex.printStackTrace();
                    }

                    //Retorna as informações do estado em um array de Strings
                    return values;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader, se houve algum erro
            try {
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
            } catch (IOException ioex){
                ioex.printStackTrace();
            }
        }

        return null;
    }

    public static String[] DefineInvertor(InputStream is, String[] solarPanel){
        String[] cheaperInvertor, currentInvertor;
        String line;
        double currentCost, cheaperCost, WpGenerated;
        int numberInvertors;
        BufferedReader bufferedReader = null;
        WpGenerated = Double.parseDouble(solarPanel[Constants.iPANEL_QTD]) * Double.parseDouble(solarPanel[Constants.iPANEL_POTENCIA]);
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            bufferedReader.readLine(); //Joga fora a primeira
            line = bufferedReader.readLine();
            cheaperInvertor = line.split(divider);
            numberInvertors = (int)Math.ceil((1.15*WpGenerated)/Double.parseDouble(cheaperInvertor[Constants.iINV_POTENCIA]));//Qtd de inversores
            cheaperCost = numberInvertors * Double.parseDouble(cheaperInvertor[Constants.iINV_PRECO]);
            cheaperInvertor[Constants.iINV_QTD] = String.valueOf(numberInvertors);
            cheaperInvertor[Constants.iINV_PRECO_TOTAL] = String.valueOf(cheaperCost);

            while ((line = bufferedReader.readLine()) != null){
                currentInvertor = line.split(divider);
                numberInvertors = (int)Math.ceil((1.15*WpGenerated)/Double.parseDouble(currentInvertor[Constants.iINV_POTENCIA]));//Qtd de inversores
                currentCost = numberInvertors * Double.parseDouble(currentInvertor[Constants.iINV_PRECO]);
                currentInvertor[Constants.iINV_QTD] = String.valueOf(numberInvertors);
                currentInvertor[Constants.iINV_PRECO_TOTAL] = String.valueOf(currentCost);

                if(currentCost<cheaperCost){
                    cheaperCost = currentCost;
                    cheaperInvertor = currentInvertor;
                }
            }

            //Fechar o bufferedReader
            try {
                bufferedReader.close();
            } catch (IOException ioex){
                ioex.printStackTrace();
            }

            return cheaperInvertor;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader, se houve algum erro
            try {
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
            } catch (IOException ioex){
                ioex.printStackTrace();
            }
        }

        String[] error = {""};
        return error;
    }

}