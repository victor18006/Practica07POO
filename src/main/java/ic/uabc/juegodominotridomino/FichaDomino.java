/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ic.uabc.juegodominotridomino;

/**
 *
 * @author VOM
 */

public class FichaDomino implements Movible {
    protected int valorDerecho;
    protected int valorIzquierdo;
    protected boolean volteada; // true -> se ven los puntitos

    private Direccion facing = Direccion.NORTH;

    /**
     * Constructor no parametrizado
     */
    public FichaDomino() {
        valorDerecho = -1;
        valorIzquierdo = -1;
        volteada = true;
    }


    /**
     * Constructor parametrizado
     * @param valorDerecho int
     * @param valorIzquierdo int
     */
    public FichaDomino(int valorDerecho, int valorIzquierdo) {
        this.valorDerecho = valorDerecho;
        this.valorIzquierdo = valorIzquierdo;
        volteada = true;
    }

    public int sumaDePuntos(){
        return valorDerecho+valorIzquierdo;
    }

    /**
     * Getter de valorDerecho
     * @return int valorDerecho
     */
    public int getValorDerecho() {
        return valorDerecho;
    }

    /**
     * Setter de valorDerecho
     * @param valorDerecho int
     */
    public void setValorDerecho(int valorDerecho) {
        this.valorDerecho = valorDerecho;
    }

    /**
     * Getter de valorIzquierdo
     * @return valorIzquierdo
     */
    public int getValorIzquierdo() {
        return valorIzquierdo;
    }

    /**
     * Setter de valorIzquierdo
     * @param valorIzquierdo int
     */
    public void setValorIzquierdo(int valorIzquierdo) {
        this.valorIzquierdo = valorIzquierdo;
    }

    /**
     * Getter del atributo que indica el estado de visibilidad de la ficha
     * @return boolean
     */
    public boolean isVolteada() {
        return volteada;
    }

    public boolean isTridomino(){ return false; }

    /**
     * Setter del estado de visibilidad de la ficha
      * @param volteada  boolean
     */
    public void setVolteada(boolean volteada) {
        this.volteada = volteada;
    }
    /**
     * Devuelve la dirección en la que se encuentra apuntando la ficha
     * según el valor derecho asignado a la hora de la creación de la ficha
     * @return dir
     */
    public Direccion getFacing() {
        return facing;
    }

    /**
     * Se asigna la dirección de la ficha
     * @param facing dir
     */
    public void setFacing(Direccion facing) {
        this.facing = facing;
    }

    /**
     * Rota la ficha en una posición hacia la derecha
     */
    public void rotateRight() {
        if (facing == Direccion.WEST) {
            facing = Direccion.NORTH;
        } else {
            facing = facing.values()[facing.ordinal()+1];
        }
        switch (facing) {
            case WEST:
            case EAST:
                girar();
        }
        // change facing to next direction
        //facing = facing.values()[facing.ordinal()+1];
    }

    /**
     * Rota la ficha en una posición hacia la izquierda
     */
    public void rotateLeft() {
        // change facing to next direction
        if (facing == Direccion.NORTH) {
            facing = Direccion.WEST;
        } else {
            facing = facing.values()[facing.ordinal()-1];
        }
        switch (facing) {
            case SOUTH:
            case NORTH:
                girar();
        }

    }

    /**
     * Intercambia el sentido de los valores en la ficha
     */
    private void girar() {
        int temp;
        temp = valorDerecho;
        valorDerecho = valorIzquierdo;
        valorIzquierdo = temp;
    }

    /**
     * Busca un valor dado en los lados de la ficha
     * @param valor m
     * @return m
     */
    public boolean tieneValor(int valor) {
        return (valor==valorDerecho || valor==valorIzquierdo);
    }

    /**
     * Pone el lado del valor indicado en el lado derecho
     * @param valor ma
     */
    public void colocarValorEnLadoIzquierdo(int valor) {
        if (valorDerecho == valor) {
            girar();
        }
    }

    /**
     * Indica si la ficha es mula
     * @return m
     */
    public boolean esMula() {
        return valorDerecho == valorIzquierdo;
    }

    /**
     * Crea una cadena que contenga la representación de la ficha
     * @return m
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (volteada) {
            switch (facing) {
                case EAST:
                case WEST:
                    sb.append("[").append(valorDerecho);
                    sb.append("|").append(valorIzquierdo);
                    sb.append(']');
                    break;
                case NORTH:
                case SOUTH:
                    sb.append("[").append(valorIzquierdo).append("]\n");
                    sb.append("[").append(valorDerecho).append("]");
                    break;
            }
        } else {
            // no se ven los puntos
            sb.append("[   ]\n");
        }
        return sb.toString();
    }
}