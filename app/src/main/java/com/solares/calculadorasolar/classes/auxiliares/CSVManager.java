package com.solares.calculadorasolar.classes.auxiliares;

import android.content.Context;

import com.solares.calculadorasolar.R;
import com.solares.calculadorasolar.classes.entidades.Inversor;
import com.solares.calculadorasolar.classes.entidades.Painel;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CSVManager {

    private static final String divider = ",";

    public static String[] getCity(int idCity, String stateName, InputStream is) {
        String[] values = new String[0];
        BufferedReader bufferedReader = null;
        int currentLine = 0;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            bufferedReader.readLine(); //Ignora a primeira linha (cabeçário)
            while ((line = bufferedReader.readLine()) != null) {
                values = line.split(divider);
                if (stateName.equals(values[0])) { //Se essa linha for a primeira linha do estado correto
                    currentLine = 0;
                    if (idCity == currentLine) { //Se for igual a zero (primeira cidade)
                        //Fechar o bufferedReader
                        try {
                            bufferedReader.close();
                        } catch (IOException ioex) {
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
                            } catch (IOException ioex) {
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
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        return null;
    }


    public static String[] getState(String[] cityVec, InputStream is) {
        String stateName = cityVec[Constants.iCID_ESTADO];
        String[] values;
        String line;
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            //Passar por todas as linhas do arquivo de estados
            while ((line = bufferedReader.readLine()) != null) {
                values = line.split(divider);
                if (stateName.equals(values[0])) { //Se essa linha for a do estado correto

                    //Fechar o bufferedReader
                    try {
                        bufferedReader.close();
                    } catch (IOException ioex) {
                        ioex.printStackTrace();
                    }

                    //Retorna as informações do estado em um array de Strings
                    return values;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader, se houve algum erro
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        return null;
    }


    public static LinkedList<Painel> getPaineisCSV(Context MyContext) {
        InputStream is = MyContext.getResources().openRawResource(R.raw.banco_paineis);
        String[] moduloString;
        String line = "";
        BufferedReader bufferedReader = null;
        LinkedList<Painel> listaPaineis = new LinkedList<>();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            bufferedReader.readLine();

            line = bufferedReader.readLine();
            while (line != null) {
                moduloString = line.split(divider);
                Painel painel = new Painel();
                painel.setCodigo(moduloString[Constants.iPANEL_CODIGO]);
                painel.setMarca(moduloString[Constants.iPANEL_MARCA]);
                painel.setPotencia(Float.parseFloat(moduloString[Constants.iPANEL_POTENCIA]));
                painel.setPreco(Float.parseFloat(moduloString[Constants.iPANEL_PRECO]));
                painel.setArea(Float.parseFloat(moduloString[Constants.iPANEL_AREA]));
                painel.setCoefTempPot(Float.parseFloat(moduloString[Constants.iPANEL_COEFTEMP]));
                painel.setNOCT(Float.parseFloat(moduloString[Constants.iPANEL_NOCT]));

                listaPaineis.add(painel);

                line = bufferedReader.readLine();
            }

            //Fechar o bufferedReader
            try {
                bufferedReader.close();
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }

            return listaPaineis;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader se ocorreu algum erro
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        return null;
    }


    public static LinkedList<Inversor> getInversoresCSV(Context MyContext) {
        InputStream is = MyContext.getResources().openRawResource(R.raw.banco_inversores);
        String[] inversorString;
        String line = "";
        BufferedReader bufferedReader = null;
        LinkedList<Inversor> listaInversores = new LinkedList<>();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            bufferedReader.readLine();

            line = bufferedReader.readLine();
            while (line != null) {
                inversorString = line.split(divider);
                Inversor inversor = new Inversor();
                inversor.setCodigo(inversorString[Constants.iINV_CODIGO]);
                inversor.setMarca(inversorString[Constants.iINV_MARCA]);
                inversor.setPotencia(Float.parseFloat(inversorString[Constants.iINV_POTENCIA]));
                inversor.setPreco(Float.parseFloat(inversorString[Constants.iINV_PRECO]));
                inversor.setRendimentoMaximo(Float.parseFloat(inversorString[Constants.iINV_RENDIMENTO_MAXIMO]));

                listaInversores.add(inversor);

                line = bufferedReader.readLine();
            }

            //Fechar o bufferedReader
            try {
                bufferedReader.close();
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }

            return listaInversores;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Fechar o bufferedReader se ocorreu algum erro
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        return null;
    }


    public String convertToCSV(String[] data) {
        StringBuilder line = new StringBuilder();
        for (String string : data) {
            line.append(string).append(",");
        }
        return line.toString();
    }
}
