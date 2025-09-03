package com.todo;

import controller.Controller;
import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe principale che avvia l'esecuzione del programma
 */
public class Main
{
    /**
     * Punto di inizio dell'applicazione
     */
    public static void main(String[] args)
    {
        List<Utente> utenti = new ArrayList<>();
        Controller controller = new Controller(utenti);

        controller.startTodo();
    }
}
