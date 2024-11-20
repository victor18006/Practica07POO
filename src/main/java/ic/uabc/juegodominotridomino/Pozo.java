/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ic.uabc.juegodominotridomino;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author VOM
 */

public class Pozo {
    ArrayList<FichaDomino> piezas;

    /**
     * Constructor de la clase
      */
    public Pozo() {
        piezas = new ArrayList<>();
    }

    /**
     * Mezcla las fichas creadas
     */
    public void mezclarPiezas(){
        Collections.shuffle(piezas);
    }

    /**
     * Limpia y vuelve a generar el set
     */
    public void reiniciaSet(){
        piezas.clear();
        generaPiezas();
    }

    /**
     * Crea las fichas del set
     */
    public void generaPiezas(){
        generarDomino();
        generarTridomino();
    }

    /**
     * Crea las fichas de Domino
      */
    private void generarDomino(){
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6 ; j++) {
                FichaDomino pieza = new FichaDomino(i,j);
                piezas.add(pieza);
            }
        }
    }

    /**
     * Crea las fichas de tridomino
     */
    private void generarTridomino(){
        for (int i = 0; i <= 5; i++){
            for (int j = i; j <= 5; j++){
                for (int k = j; k <= 5; k++){
                    FichaTridomino ficha = new FichaTridomino(i,j,k);
                    piezas.add(ficha);
                }
            }
        }
    }

    public ArrayList<FichaDomino> get10fichas() {
        ArrayList<FichaDomino> mano = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mano.add(piezas.getFirst());
            piezas.removeFirst();
        }
        return mano;
    }
    public ArrayList<FichaDomino> get2fichas() {
        ArrayList<FichaDomino> mano = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mano.add(piezas.getFirst());
            piezas.removeFirst();
        }
        return mano;
    }
    public FichaDomino getFichaDomino() {
        FichaDomino ficha= piezas.getFirst();
        piezas.removeFirst();
        return ficha;
    }
    public boolean estaVacio(){
        boolean resp=false;
        if(piezas.isEmpty()){
                 resp= true;
        }
        return resp;
    }
}