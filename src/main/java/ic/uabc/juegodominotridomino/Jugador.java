/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ic.uabc.juegodominotridomino;

import java.util.ArrayList;

/**
 *
 * @author VOM
 */

public class Jugador {
    private String nombre;
    private ArrayList<FichaDomino> mano;
    private int puntos;

    /**
     * Constructor para jugador nuevo
      * @param nombre
     */
    public Jugador(String nombre) {
        this.nombre = nombre;
        puntos=0;
    }

    /**
     * Constructor para copia de jugador
     */
    public Jugador(Jugador copia){
        this.nombre = copia.nombre;
        this.puntos = copia.puntos;
        this.mano=copia.mano;
    }

    public String getNombre() {
        return nombre;
    }
    public ArrayList<FichaDomino> getMano() {
        return mano;
    }
    public int cantidadDeFichas(){
        return mano.size();
    }
    public void setMano(ArrayList<FichaDomino> mano) {
        this.mano = mano;
    }
    public FichaDomino getFicha(int posicion){
        return mano.get(posicion);
    }
    public void agregar2FichasAMano(ArrayList<FichaDomino> fichas){
        mano.add(fichas.getFirst());
        mano.add(fichas.getLast());
    }
    public void eliminarPieza(int posicion){
        mano.remove(posicion);
    }
    public void imprimeFicha(int posicion){
        System.out.println(mano.get(posicion));
    }

    public void imprimeMano(){
        // Buscar los índices de los saltos de línea en cada cadena
        int[] indices = new int[mano.size()];
        for (int i = 0; i < mano.size(); i++) {
            indices[i] = mano.get(i).toString().indexOf('\n');
        }

        // Construir la nueva cadena combinando las partes de las cadenas
        StringBuilder combinedTile = new StringBuilder();
        
        combinedTile.append("\n");
        
        for (int i = 0; i < mano.size(); i++) {
            if(i>9) combinedTile.append(" ");
            combinedTile.append(mano.get(i).toString().substring(0, indices[i]));
            combinedTile.append(" ");
        }
        combinedTile.append("\n");

        for (int i = 0; i < mano.size(); i++) {
            if(i>9) combinedTile.append(" ");
            combinedTile.append(mano.get(i).toString().substring(indices[i] + 1));
            combinedTile.append(" ");
        }
        
        combinedTile.append("\n");
        
        for (int i = 0; i < mano.size(); i++){
            String finalDeCadena;
            if(i<10) finalDeCadena="-  ";
            else finalDeCadena="-  ";
           if(mano.get(i).isTridomino()) combinedTile.append("-").append(i+1).append(finalDeCadena);
           else combinedTile.append("-").append(i+1).append("- ");
        }
        combinedTile.append("\n");
        
        System.out.println(combinedTile.toString());
    }
    
    public int getPuntos() {
        return puntos;
    }
    
    public void sumaPuntos(int puntos) {
        this.puntos += puntos;
    }

    public int valorDeFicha(int posicionDeFicha){
        return mano.get(posicionDeFicha).sumaDePuntos();
    }
}