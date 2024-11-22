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

        System.out.println("Bienvenido al Juego de Fichas de Dominó y Tridominó");
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

                    if (ponerDeNuevo) {
                        jugadores[i].imprimeMano();
                        System.out.println("Ingresa el número de ficha a elegir o ingresa 0 para pasar turno:\n");
                    } else {
                        jugadores[i].imprimeMano();
                        System.out.println("Jugador " + jugadores[0].getNombre() + " Puntaje al momento: " + jugadores[0].getPuntos());
                        System.out.println("Jugador " + jugadores[1].getNombre() + " Puntaje al momento: " + jugadores[1].getPuntos());
                        System.out.println("Turno de: " + jugadores[i].getNombre() + "\n\nIngresa el número de ficha a elegir o ingrese 0 para tomar 2 fichas del pozo.");
                    }

                    int manoSize = jugadores[i].cantidadDeFichas();

                    do {
                        seleccion = scanner.nextInt();
                        scanner.nextLine();
                    } while (!(seleccion >= 0 && seleccion <= manoSize));

                    if (seleccion == 0) {
                        colocada = false;
                    } else {
                        seleccion -= 1;
                        FichaDomino fichaAInsertar = jugadores[i].getFicha(seleccion);
                        colocada = insertarAMesa(fichaAInsertar, i);
                    }

                    if (colocada) {
                        jugadores[i].eliminarPieza(seleccion);
                        jugadoresBloqueados = 0;
                        if (ponerDeNuevo) {
                            ponerDeNuevo = false;
                        }

                    } else {
                        if (!ponerDeNuevo) {

                            //pause();
                            if (pozo.estaVacio()) {
                                System.out.println("Sin fichas en pozo, ya no se puede tomar más fichas.\n");
                                jugadoresBloqueados++;
                            } else {
                                System.out.println("Se agregaron dos fichas a tu mano.");
                                jugadores[i].agregar2FichasAMano(pozo.get2fichas());
                                jugadores[i].imprimeMano();
                                System.out.println("Para seleccionar una ficha ingrese 1, para pasar turno ingrese 0:\n");

                                do {
                                    seleccion = scanner.nextInt();
                                } while (seleccion != 0 && seleccion != 1);
                                if (seleccion == 1) {
                                    ponerDeNuevo = true;
                                }
                            }
                        } else {
                            ponerDeNuevo = false;
                        }

                    }
                    //System.out.println("Jugador " + jugadores[i].getNombre() + "\nPuntaje al momento: " + jugadores[i].getPuntos());
                    if (jugadoresBloqueados == 2 || jugadores[0].cantidadDeFichas() == 0 || jugadores[1].cantidadDeFichas() == 0) {
                        finalDeRonda = true;
                    }
                } while (ponerDeNuevo);
            }
        } while (!finalDeRonda);
        System.out.println("Fin del juego.\n\n");
        //se determina el ganador
        ganadorFinal = encuentraGanador();
        System.out.println("Puntaje del jugador " + jugadores[0].getNombre() + ": " + jugadores[0].getPuntos());
        System.out.println("Puntaje del jugador " + jugadores[1].getNombre() + ": " + jugadores[1].getPuntos());
        System.out.println("El ganador del juego es : " + jugadores[ganadorFinal].getNombre());
        System.out.println("Puntaje final: " + jugadores[ganadorFinal].getPuntos());
    }

    /**
     * Reparte 10 fichas del pozo a cada jugador
     */
    private void reparteFichas() {
        for (int i = 0; i < 2; i++) {
            jugadores[i].setMano(pozo.get10fichas());
        }
    }

    /**
     * Genera las fichas y las mezcla para poder comenzar el juego
     */
    private void preparaMesa() {
        pozo.generaPiezas();
        pozo.mezclarPiezas();
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
        int posicionPrimerSeleccion;
        FichaDomino[] fichaTemp = new FichaDomino[3];
        FichaDomino ficha;

        do {
            for (int i = 0; i < jugadores.length; i++) {
                jugadores[i].imprimeMano();
                System.out.println("Jugador: " + jugadores[i].getNombre()
                        + "\nIngresa el número de ficha a elegir: ");
                int manoSize = jugadores[i].cantidadDeFichas();
                do {
                    primerSeleccion = scanner.nextInt();
                    if (!(primerSeleccion > 0 && primerSeleccion <= manoSize)) {
                        System.out.println("Elige un número de ficha correcta.");
                    }
                } while (!(primerSeleccion > 0 && primerSeleccion <= manoSize));
                posicion[i] = primerSeleccion - 1;
                ficha = jugadores[i].getFicha(posicion[i]);
                fichaTemp[i] = ficha;
                valorDeFicha[i] = ficha.sumaDePuntos();
            }
            if (valorDeFicha[0] == valorDeFicha[1]) {
                System.out.println("Vuelve a elegir fichas porque los jugadores tienen la misma suma de puntos.\n");
            }
        } while (valorDeFicha[0] == valorDeFicha[1]);
        System.out.println("Suma de puntos jugador " + jugadores[0].getNombre() + ": " + valorDeFicha[0]);
        System.out.println("Suma de puntos jugador " + jugadores[1].getNombre() + ": " + valorDeFicha[1]);
        if (valorDeFicha[0] > valorDeFicha[1]) {
            System.out.println("Jugador " + jugadores[0].getNombre() + " colocará la primera ficha");
            jugadores[0].sumaPuntos(valorDeFicha[0]);
        } else {
            System.out.println("Jugador " + jugadores[1].getNombre() + " colocará la primera ficha");
            jugadores[1].sumaPuntos(valorDeFicha[1]);
        }

        if (valorDeFicha[0] > valorDeFicha[1]) {
            System.out.println("Ingresa el número de cómo quieres posicionar la primer ficha: ");
            if (fichaTemp[0].isTridomino()) {
                for (int i = 0; i < 6; i++) {
                    fichaTemp[0].rotateRight();
                    System.out.println("Opcion " + (i + 1) + ":\n" + fichaTemp[0]);
                }
                do {
                    posicionPrimerSeleccion = scanner.nextInt();
                    if (posicionPrimerSeleccion < 1 || posicionPrimerSeleccion > 6) {
                        System.out.println("Elige un número de posición correcto.");
                    }
                } while (posicionPrimerSeleccion < 1 || posicionPrimerSeleccion > 6);
                for (int h = 0; h < posicionPrimerSeleccion; h++) {
                    fichaTemp[0].rotateRight();
                    if (posicionPrimerSeleccion == h + 1) {
                        fichaTemp[2] = fichaTemp[0];
                    }
                }
                mesa.add(fichaTemp[2]);
            } else {
                System.out.println("Opcion 1:\n" + fichaTemp[0]);
                fichaTemp[0].rotateRight();
                fichaTemp[0].rotateRight();
                System.out.println("Opcion 2:\n" + fichaTemp[0]);
                do {
                    posicionPrimerSeleccion = scanner.nextInt();
                    if (posicionPrimerSeleccion < 1 || posicionPrimerSeleccion > 2) {
                        System.out.println("Elige un número de posición correcto.");
                    }
                } while (posicionPrimerSeleccion < 1 || posicionPrimerSeleccion > 2);
                if (posicionPrimerSeleccion == 2) {
                    fichaTemp[2] = fichaTemp[0];
                } else {
                    fichaTemp[0].rotateRight();
                    fichaTemp[0].rotateRight();
                    fichaTemp[2] = fichaTemp[0];
                }
                mesa.add(fichaTemp[2]);
            }
            jugadores[0].eliminarPieza(posicion[0]);

            Jugador temporal = new Jugador(jugadores[0]);
            jugadores[0] = new Jugador(jugadores[1]);
            jugadores[1] = new Jugador(temporal);
            System.out.println("Jugador " + jugadores[1].getNombre() + "\nPuntaje al momento: " + jugadores[1].getPuntos());
        } else {
            System.out.println("Ingresa el número de cómo quieres posicionar la primer ficha: ");
            if (fichaTemp[1].isTridomino()) {
                for (int i = 0; i < 6; i++) {
                    fichaTemp[1].rotateRight();
                    System.out.println("Opcion " + (i + 1) + ":\n" + fichaTemp[1]);
                }
                do {
                    posicionPrimerSeleccion = scanner.nextInt();
                    if (posicionPrimerSeleccion < 1 || posicionPrimerSeleccion > 6) {
                        System.out.println("Elige un número de posición correcto.");
                    }
                } while (posicionPrimerSeleccion < 1 || posicionPrimerSeleccion > 6);
                for (int h = 0; h < posicionPrimerSeleccion; h++) {
                    fichaTemp[1].rotateRight();
                    if (posicionPrimerSeleccion == h + 1) {
                        fichaTemp[2] = fichaTemp[1];
                    }
                }
                mesa.add(fichaTemp[2]);
            } else {
                System.out.println("Opcion 1:\n" + fichaTemp[1]);
                fichaTemp[1].rotateRight();
                fichaTemp[1].rotateRight();
                System.out.println("Opcion 2:\n" + fichaTemp[1]);
                do {
                    posicionPrimerSeleccion = scanner.nextInt();
                    if (posicionPrimerSeleccion < 1 || posicionPrimerSeleccion > 2) {
                        System.out.println("Elige un número de posición correcto.");
                    }
                } while (posicionPrimerSeleccion < 1 || posicionPrimerSeleccion > 2);
                if (posicionPrimerSeleccion == 2) {
                    fichaTemp[2] = fichaTemp[1];
                } else {
                    fichaTemp[1].rotateRight();
                    fichaTemp[1].rotateRight();
                    fichaTemp[2] = fichaTemp[1];
                }
                mesa.add(fichaTemp[2]);
            }
            jugadores[1].eliminarPieza(posicion[1]);
            System.out.println("Jugador " + jugadores[1].getNombre() + "\nPuntaje al momento: " + jugadores[1].getPuntos());
        }
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
        Scanner scanner = new Scanner(System.in);
        boolean colocada = false;
        boolean insertable = false;
        int posicionFicha;
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
                        do {
                            if ((fichaAInsertar_tri.getValorIzquierdo() == fichaFinal_tri.getValorIzquierdo()
                                    && fichaAInsertar_tri.getValorDerecho() == fichaFinal_tri.getValorDerecho()
                                    && !fichaAInsertar_tri.isPointingUp())) {
                                insertable = true;
                            } else {
                                fichaAInsertar_tri.rotateLeft();
                                bucle = bucle + 1;
                            }
                        } while (!insertable && bucle < 10);
                        if (insertable) {
                            mesa.addLast(fichaAInsertar_tri);
                            jugadores[jugador].sumaPuntos(fichaAInsertar_tri.sumaDePuntos());
                            colocada = true;
                        } else {
                            System.out.println("Tu ficha no coincide");
                        }

                    } else {
                        System.out.println("Tu ficha no coincide");
                    }

                } else {
                    System.out.println("tu ficha debe ser trinomino");
                }

            } else {//si la ficha final está apuntando hacia abajo
                int contadorOpciones = 0;
                int validadorOpciones = 0;
                if (fichaAInsertar.isTridomino()) {
                    //INICIO
                    //agregar condicion que revise ambos casos, cuando a1== b1 o b2
                    //y rotar para preguntar como se quiere ingresar la ficha
                    //fichaAInsertar
                    FichaTridomino fichaAInsertar_tri = (FichaTridomino) fichaAInsertar;
                    FichaTridomino pruebas = (FichaTridomino) fichaAInsertar;//V2 (0 2 4)
                    for (int i = 0; i < 6; i++) {
                        pruebas.rotateRight();
                        if (i == 0 || i == 2 || i == 4) {
                            //System.out.println("Opcion " + (i + 1) + ":\n" + pruebas);
                            if (pruebas.getValorArriba() == fichaFinal_tri.getValorArriba()
                                    && pruebas.isPointingUp()) {
                                contadorOpciones++;
                            }
                        }
                    }
                    for (int i = 0; i < 6; i++) {
                        pruebas.rotateRight();
                        if (i == 0 || i == 2 || i == 4) {
                            if (validadorOpciones <= contadorOpciones && pruebas.getValorArriba() == fichaFinal_tri.getValorArriba()
                                    && pruebas.isPointingUp()) {
                                validadorOpciones++;
                                System.out.println("Opcion " + validadorOpciones + ":\n" + pruebas);
                            }
                        }
                    }
                    validadorOpciones = 0;
                    System.out.println("Veces :" + contadorOpciones);
                    if (contadorOpciones == 2) {
                        contadorOpciones = 0;
                        System.out.println("Ingresa el número de cómo quieres posicionar la ficha: ");
                        do {
                            posicionFicha = scanner.nextInt();
                            if (posicionFicha < 1 || posicionFicha > 2) {
                                System.out.println("Elige un número de posición correcto.");
                            }
                        } while (posicionFicha < 1 || posicionFicha > 2);

                        for (int i = 0; i < 6; i++) {
                            pruebas.rotateRight();
                            if (i == 0 || i == 2 || i == 4) {
                                if (validadorOpciones <= contadorOpciones && pruebas.getValorArriba() == fichaFinal_tri.getValorArriba()
                                        && pruebas.isPointingUp()) {
                                    validadorOpciones++;
                                    if (validadorOpciones == posicionFicha) {
                                        fichaAInsertar_tri = pruebas;
                                        System.out.println("ESTO SE GUARDA " + ":\n" + pruebas);
                                        System.out.println("ESTO SE queda en tri GUARDA " + ":\n" + pruebas);
                                        fichaAInsertar_tri.rotateRight();
                                    }
                                }
                            }
                        }
                    }
                    //FIN
                    if (fichaAInsertar_tri.tieneValor(fichaFinal_tri.getValorArriba())) {
                        do {
                            if (fichaAInsertar_tri.getValorArriba() == fichaFinal_tri.getValorArriba()
                                    && fichaAInsertar_tri.isPointingUp()) {
                                insertable = true;
                            } else {
                                fichaAInsertar.rotateLeft();
                            }
                        } while (!insertable);
                        mesa.addLast(fichaAInsertar_tri);
                        colocada = true;
                        jugadores[jugador].sumaPuntos(fichaAInsertar.sumaDePuntos());
                    } else {
                        System.out.println("la ficha no coincide");
                    }

                } else {//si la ficha a colocar es un domino
                    if (fichaAInsertar.tieneValor(fichaFinal_tri.getValorArriba())) {
                        if (fichaAInsertar.getValorIzquierdo() != fichaFinal_tri.getValorArriba()) {
                            do {
                                fichaAInsertar.rotateLeft();
                            } while (fichaAInsertar.getFacing() == Direccion.WEST || fichaAInsertar.getFacing() == Direccion.EAST);
                        }
                        mesa.addLast(fichaAInsertar);
                        colocada = true;
                        jugadores[jugador].sumaPuntos(fichaAInsertar.sumaDePuntos());
                    } else {
                        System.out.println("La ficha no coincide");
                    }

                }
            }
        } else { // si la ultima ficha de la mesa es Domino
            int contadorOpciones = 0;
            int validadorOpciones = 0;
            if (fichaAInsertar.isTridomino()) { //si la ficha a insertar es tridomino
                //agregar condicion que revise ambos casos, cuando a1== b1 o b2
                //y rotar para preguntar como se quiere ingresar la ficha
                //fichaAInsertar
                FichaTridomino fichaAInsertar_tri = (FichaTridomino) fichaAInsertar;
                FichaTridomino pruebas = (FichaTridomino) fichaAInsertar;//V2 (0 2 4)
                for (int i = 0; i < 6; i++) {
                    pruebas.rotateRight();
                    if (i == 0 || i == 2 || i == 4) {
                        //System.out.println("Opcion " + (i + 1) + ":\n" + pruebas);
                        if (pruebas.getValorArriba() == fichaFinal.getValorDerecho()
                                && pruebas.isPointingUp()) {
                            contadorOpciones++;
                        }
                    }
                }
                for (int i = 0; i < 6; i++) {
                    pruebas.rotateRight();
                    if (i == 0 || i == 2 || i == 4) {
                        if (validadorOpciones <= contadorOpciones && pruebas.getValorArriba() == fichaFinal.getValorDerecho()
                                && pruebas.isPointingUp()) {
                            validadorOpciones++;
                            System.out.println("Opcion " + validadorOpciones + ":\n" + pruebas);
                        }
                    }
                }
                validadorOpciones = 0;
                System.out.println("Veces :" + contadorOpciones);
                if (contadorOpciones == 2) {
                    contadorOpciones = 0;
                    System.out.println("Ingresa el número de cómo quieres posicionar la ficha: ");
                    do {
                        posicionFicha = scanner.nextInt();
                        if (posicionFicha < 1 || posicionFicha > 2) {
                            System.out.println("Elige un número de posición correcto.");
                        }
                    } while (posicionFicha < 1 || posicionFicha > 2);

                    for (int i = 0; i < 6; i++) {
                        pruebas.rotateRight();
                        if (i == 0 || i == 2 || i == 4) {
                            if (validadorOpciones <= contadorOpciones && pruebas.getValorArriba() == fichaFinal.getValorDerecho()
                                    && pruebas.isPointingUp()) {
                                validadorOpciones++;
                                if (validadorOpciones == posicionFicha) {
                                    fichaAInsertar_tri = pruebas;
                                    System.out.println("ESTO SE GUARDA " + ":\n" + pruebas);
                                    System.out.println("ESTO SE queda en tri GUARDA " + ":\n" + pruebas);
                                    fichaAInsertar_tri.rotateRight();
                                }
                            }
                        }
                    }
                }
                //Aqui termina codigo primero
                if (fichaAInsertar_tri.tieneValor(fichaFinal.getValorDerecho())) {
                    do {
                        if (fichaAInsertar_tri.getValorArriba() == fichaFinal.getValorDerecho()
                                && fichaAInsertar_tri.isPointingUp()) {
                            insertable = true;
                        } else {
                            fichaAInsertar_tri.rotateLeft();
                        }
                    } while (!insertable);
                    mesa.addLast(fichaAInsertar_tri);
                    jugadores[jugador].sumaPuntos(fichaAInsertar_tri.sumaDePuntos());
                    colocada = true;
                } else {
                    System.out.println("La ficha no coincide");
                }
            } else {//si la ficha a insertar es domino

                if (fichaAInsertar.tieneValor(fichaFinal.valorDerecho)) {
                    while (fichaAInsertar.getValorIzquierdo() != fichaFinal.valorDerecho) {
                        fichaAInsertar.rotateLeft();
                    }
                    mesa.addLast(fichaAInsertar);
                    jugadores[jugador].sumaPuntos(fichaAInsertar.sumaDePuntos());
                    colocada = true;
                } else {
                    System.out.println("No coincide la ficha");
                }
            }
        }

        if (colocada) {
            System.out.println("Ficha agregada!");
        }
        return colocada;
    }

    private void imprimeMesa() {

        System.out.println("Fichas en Mesa:");
        for (FichaDomino ficha : mesa) {
            System.out.println(ficha.toString());
        }
    }
}
