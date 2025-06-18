import controller.Controller;
import gui.*;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        // Creo una lista di utenti di prova

        List<Utente> utenti = new ArrayList<Utente>();
        Utente utente = new Utente("", "");
        utenti.add(utente);

        Utente utente2 = new Utente("user", "user");
        utenti.add(utente2);


        new ToDo("Fissare ricevimento", LocalDate.of(2025, 4,23), "https://www.docenti.unina.it/#!/search", "Alle ore 10 il mercoledì con il professor Pippo", null , Color.CYAN , null , utente.getUniversita());
        ToDo todo2 = new ToDo("Fissare ricevimento", LocalDate.of(2020, 2, 23), "https://www.docenti.unina.it/#!/search", "Alle ore 10 il mercoledì con il professor Pippo", null , Color.YELLOW , null, utente.getLavoro());




        Controller controller = new Controller(utenti);
        controller.shareTodo(todo2, "user", Bacheca.titoloBacheca.Lavoro);

        controller.startTodo();

    }


}
