/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ic.uabc.juegodominotridomino;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author VOM
 */

public class Juego {

    Jugador[] jugadores;
    Pozo pozo;
    ArrayList<FichaDomino> mesa;
    int ganadorFinal;

    /**
     * Constructor
     */
    public Juego() {
        jugadores = new Jugador[2];
        pozo = new Pozo();
        mesa = new ArrayList<>();
        ganadorFinal = 0;
    }

    public void comenzarJuego() {
        boolean finalDeRonda = false;
        int jugadoresBloqueados = 0;
        Scanner scanner = new Scanner(System.in);
        int seleccion;
        boolean colocada;

        //se agregan los jugadores
        agregarJugadores();
        //reparte fichas
        preparaMesa();
        reparteFichas();
        //se determina cuál jugador comienza la ronda
        determinaQuienComienza();

        //comienza la ronda
        do {
            for (int i = 0; i < jugadores.length; i++) {
                boolean ponerDeNuevo = false;
                do {
                    finalDeRonda = DeterminarRondaTerminada(jugadoresBloqueados);
                    imprimeMesa();

                    if(ponerDeNuevo) System.out.println("Ingresa la posicion de la nueva ficha que deseas colocar, si no puedes hacelo presiona 0");
                    else {
                        jugadores[i].imprimeMano();
                        System.out.println(jugadores[i].getNombre() + " escoja una ficha para insertar o presiona 0 para tomar 2 fichas del pozo");
                    }

                    int manoSize = jugadores[i].cantidadDeFichas();

                    do {
                        seleccion = scanner.nextInt();
                        scanner.nextLine();
                    } while (!(seleccion >= 0 && seleccion <= manoSize));

                    if (seleccion == 0) colocada = false;
                    else {
                        seleccion -= 1;
                        FichaDomino fichaAInsertar = jugadores[i].getFicha(seleccion);
                        colocada = insertarAMesa(fichaAInsertar, i);
                    }

                    if (colocada) {
                        jugadores[i].eliminarPieza(seleccion);
                        jugadoresBloqueados = 0;
                        if (ponerDeNuevo) ponerDeNuevo=false;

                    } else {
                        if(!ponerDeNuevo) {
                            System.out.println("Se añaden dos fichas a tu mano.");
                            pause();
                            if (pozo.estaVacio()) {
                                System.out.println("pozo vacío, no puedes tomar fichas...");
                                jugadoresBloqueados++;
                            } else {
                                jugadores[i].agregar2FichasAMano(pozo.get2fichas());
                                jugadores[i].imprimeMano();
                                System.out.println("Deseas colocar una de tus fichas nuevas? [Si==1] [No==0]");

                                do {
                                    seleccion = scanner.nextInt();
                                }while (seleccion!=0&&seleccion!=1);
                                if(seleccion==1) ponerDeNuevo = true;
                            }
                        }else ponerDeNuevo=false;

                    }
                    if (jugadoresBloqueados == 2 || jugadores[0].cantidadDeFichas() == 0 || jugadores[1].cantidadDeFichas() == 0) {
                        finalDeRonda = true;
                    }
                }while(ponerDeNuevo);

            }
        } while (!finalDeRonda);
        System.out.println("Juego terminado\n\n");
        //se determina el ganador
        ganadorFinal = encuentraGanador();
        System.out.println("Felicidades " + jugadores[ganadorFinal].getNombre() + " has ganado el juego!");
        System.out.println(" Puntos: "+jugadores[ganadorFinal].getPuntos());
        int posPerdedor=1-ganadorFinal;
        System.out.println(" Puntos "+ jugadores[posPerdedor].getNombre()+": "+jugadores[posPerdedor].getPuntos());

    }

    /**
     * Reparte 10 fichas del pozo a cada jugador
     */
    private void reparteFichas() {

        System.out.println("...Repartiendo Fichas...");
        pause();
        for (int i = 0; i < 2; i++) {
            jugadores[i].setMano(pozo.get10fichas());
        }
    }

    /**
     * Genera las fichas y las mezcla para poder comenzar el juego
     */
    private void preparaMesa() {
        pozo.reiniciaSet();
        pozo.generaPiezas();
        pozo.mezclarPiezas();
        System.out.println("...Mezclando fichas...");
        pause();
    }

    /**
     * Crea los jugadores según el nombre dado.
     */
    private void agregarJugadores() {
        Scanner scanner = new Scanner(System.in);
        String nombre;
        for (int i = 0; i < jugadores.length; i++) {
            System.out.println("Ingresa el nombre del jugador " + (i + 1) + ": ");
            nombre = scanner.next();
            jugadores[i] = new Jugador(nombre);
        }
    }

    /**
     * Encuentra el ganador del jugo segun la cantidad de puntos
     *
     * @return posicion del ganador
     */
    private int encuentraGanador() {
        int ganador = 0;
        int puntosMaximos = 0;
        for (int i = 0; i < jugadores.length; i++) {
            if (jugadores[i].getPuntos() > puntosMaximos) {
                ganador = i;
                puntosMaximos = jugadores[i].getPuntos();
            }
        }
        return ganador;
    }

    /**
     * Realiza la selección del jugador que comienza la ronda según los puntos
     * de una ficha al azar de la mano de cada jugador
     */
    private void determinaQuienComienza() {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        int[] valorDeFicha = new int[2], posicion = new int[2];
        int primerSeleccion;
        FichaDomino[] fichaTemp = new FichaDomino[2];
        FichaDomino ficha;

        for (int i = 0; i < jugadores.length; i++) {
            jugadores[i].imprimeMano();
            /*System.out.println("Jugador: " + jugadores[i].getNombre() +
                    "\n Presiona 1 para seleccionar una ficha al azar");*/
            System.out.println("Jugador: " + jugadores[i].getNombre() +
                    "\n Ingresa el número de ficha a elegir: ");

            /*do {
                usuario = scanner.nextInt();
            } while (usuario != 1);*/
            int manoSize = jugadores[i].cantidadDeFichas();
            do {
                primerSeleccion = scanner.nextInt();
                if(!(primerSeleccion > 0 && primerSeleccion <= manoSize)){
                    System.out.println("Elige un número de ficha correcta.");
                }
            } while (!(primerSeleccion > 0 && primerSeleccion <= manoSize));
            //posicion[i] = random.nextInt(0, 9);
            posicion[i] = primerSeleccion-1;
            ficha = jugadores[i].getFicha(posicion[i]);
            fichaTemp[i] = ficha;
            valorDeFicha[i] = ficha.sumaDePuntos();
            pause();
            System.out.println(ficha+"\nPuntos: " + valorDeFicha[i]);
        }

        if (valorDeFicha[0] > valorDeFicha[1]) {
            mesa.add(fichaTemp[0]);
            jugadores[0].eliminarPieza(posicion[0]);

            Jugador temporal = new Jugador(jugadores[0]);
            jugadores[0] = new Jugador(jugadores[1]);
            jugadores[1] = new Jugador(temporal);

        } else {
            mesa.add(fichaTemp[1]);
            jugadores[1].eliminarPieza(posicion[1]);
        }
        System.out.println(jugadores[1].getNombre() + " colocará la primera ficha");
        pause();
    }

    /**
     * Busca si las se cumplen las condiciones para terminar el juego
     */
    private boolean DeterminarRondaTerminada(int jugadoresBloqueados) {
        boolean rondaCompleta = false;
        if (jugadoresBloqueados == jugadores.length) {
            rondaCompleta = true;
        } else {
            for (Jugador jugador : jugadores) {
                if (jugador.getMano().isEmpty()) {
                    rondaCompleta = true;
                    break;
                }
            }
        }
        return rondaCompleta;
    }

    private boolean insertarAMesa(FichaDomino fichaAInsertar, int jugador) {
        FichaDomino fichaFinal = mesa.getLast();
        boolean colocada=false;
        boolean insertable = false;
        //si la  ultima ficha de la mesa ficha es tridomino
        if (fichaFinal.isTridomino()) {
            FichaTridomino fichaFinal_tri = (FichaTridomino) fichaFinal;
            if (fichaFinal_tri.isPointingUp()) { //si la ficha de la mesa está apuntando arriba
                //solo se pueden poner tridomino abajo de ella
                if (fichaAInsertar.isTridomino()) {
                    FichaTridomino fichaAInsertar_tri = (FichaTridomino) fichaAInsertar;
                    if (fichaAInsertar_tri.tieneValor(fichaFinal_tri.getValorDerecho())
                            && fichaAInsertar_tri.tieneValor(fichaFinal_tri.getValorIzquierdo())) {
                        int bucle = 0;
                        do{
                            if ((fichaAInsertar_tri.getValorIzquierdo() == fichaFinal_tri.getValorIzquierdo()
                                && fichaAInsertar_tri.getValorDerecho() == fichaFinal_tri.getValorDerecho()
                                && !fichaAInsertar_tri.isPointingUp()))
                            {
                                insertable = true;
                            } else {
                                fichaAInsertar_tri.rotateLeft();
                                bucle = bucle+1;
                            }
                        } while(!insertable && bucle < 10);
                        if(insertable) {
                            mesa.addLast(fichaAInsertar_tri);
                            jugadores[jugador].sumaPuntos(fichaAInsertar_tri.sumaDePuntos());
                            colocada = true;
                        }else {
                            System.out.println("Tu ficha no coincide");
                        }

                    } else System.out.println("Tu ficha no coincide");

                } else System.out.println("tu ficha debe ser trinomino");


            } else {//si la ficha final está apuntando hacia abajo
                if (fichaAInsertar.isTridomino()) {
                    FichaTridomino fichaAInsertar_tri = (FichaTridomino) fichaAInsertar;
                    if (fichaAInsertar_tri.tieneValor(fichaFinal_tri.getValorArriba())) {
                        do {
                            if (fichaAInsertar_tri.getValorArriba() == fichaFinal_tri.getValorArriba()
                                    && fichaAInsertar_tri.isPointingUp()) {
                                insertable = true;
                            } else {
                                fichaAInsertar.rotateLeft();
                            }
                        } while(!insertable);
                        mesa.addLast(fichaAInsertar_tri);
                        colocada = true;
                        jugadores[jugador].sumaPuntos(fichaAInsertar.sumaDePuntos());
                    }else System.out.println("la ficha no coincide");

                } else {//si la ficha a colocar es un domino
                    if (fichaAInsertar.tieneValor(fichaFinal_tri.getValorArriba())){
                        if(fichaAInsertar.getValorIzquierdo()!=fichaFinal_tri.getValorArriba()){
                            do {
                                fichaAInsertar.rotateLeft();
                            } while(fichaAInsertar.getFacing() == Direccion.WEST || fichaAInsertar.getFacing() == Direccion.EAST );
                        }
                        mesa.addLast(fichaAInsertar);
                        colocada = true;
                        jugadores[jugador].sumaPuntos(fichaAInsertar.sumaDePuntos());
                    }else System.out.println("La ficha no coincide");

                }
            }
        }else{ // si la ultima ficha de la mesa es Domino
            if (fichaAInsertar.isTridomino()){ //si la ficha a insertar es tridomino
                FichaTridomino fichaAInsertar_tri = (FichaTridomino) fichaAInsertar;
                if(fichaAInsertar_tri.tieneValor(fichaFinal.getValorDerecho())){
                    do{
                        if (fichaAInsertar_tri.getValorArriba()==fichaFinal.getValorDerecho()
                                && fichaAInsertar_tri.isPointingUp()){
                            insertable = true;
                        } else{
                            fichaAInsertar_tri.rotateLeft();
                        }
                    } while(!insertable);
                    mesa.addLast(fichaAInsertar_tri);
                    jugadores[jugador].sumaPuntos(fichaAInsertar_tri.sumaDePuntos());
                    colocada = true;
                }else System.out.println("La ficha no coincide");
            }else{//si la ficha a insertar es domino

                if(fichaAInsertar.tieneValor(fichaFinal.valorDerecho)){
                    while(fichaAInsertar.getValorIzquierdo()!=fichaFinal.valorDerecho){
                        fichaAInsertar.rotateLeft();
                    }
                    mesa.addLast(fichaAInsertar);
                    jugadores[jugador].sumaPuntos(fichaAInsertar.sumaDePuntos());
                    colocada = true;
                } else System.out.println("Esta ficha no coincide");
            }
        }

        if (colocada) {
            System.out.println("...agregando ficha...");
        }
        pause();
        return colocada;
    }


    private void imprimeMesa(){

        System.out.println("    .:MESA:.      ");
        for (FichaDomino ficha : mesa) {
            System.out.println(ficha.toString()+"\n");
        }
    }

    /**
     * Hace pausas elegantes
     */
    private void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.format("InterruptedException : %s%n", e);
        }
    }
}