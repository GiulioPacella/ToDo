//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
{
    public static void main(String[] args)
    {
    //Testing delle classi e dei metodi

    Utente utente1 = new Utente("Pippo", "password1");
    Utente utente2 = new Utente("Pluto", "password2");

    Bacheca Universita = new Bacheca(Bacheca.titoloBacheca.Universita, "Questa bacheca è dedicata all'università");
    Bacheca Lavoro = new Bacheca(Bacheca.titoloBacheca.Lavoro, "Questa bacheca è dedicata al lavoro");
    Bacheca TempoLibero = new Bacheca(Bacheca.titoloBacheca.TempoLibero, "Questa bacheca è dedicata al tempo libero");

    ToDo todo1 = new ToDo("Completare il progetto", "24/04/25", "https://www.jetbrains.com/", "Completa il testing e riordina il codice", "C:\\Users\\Mario\\Documents\\OO\\diagrammaUML.jpg", "Blu" ,"", Universita);
    ToDo todo2 = new ToDo("Fissare ricevimento", "30/04/25", "https://www.docenti.unina.it/#!/search", "Alle ore 10 il mercoledì con il professor Pippo", "C:\\Users\\Mario\\Documents\\Orario2semestre.jpg", "Rosso" , "", Universita);
    ToDo todo3 = new ToDo("Ripetizioni di matematica", "26/04/25", "https://www.geogebra.org/?lang=it", "Ripetizioni sulle funzioni a Mario Rossi", "C:\\Users\\Mario\\Documents\\Matematica\\funzioni.png", "Verde", "", Lavoro);
    ToDo todo4 = new ToDo("Partita di calcio", "1/05/25", "", "Ore 10 al campo 4", "", "Verde", "", TempoLibero);


    Universita.stampaBacheca();
    Lavoro.stampaBacheca();
    TempoLibero.stampaBacheca();

    System.out.println("-------------------------------------------------------------------------");

    todo1.completa(); // segno come completato il to do "Completare il progetto"
    todo1.condividiTodo(utente1); // condivido il to do "Completare il progetto" con Pippo
    todo2.cambiaBacheca(TempoLibero); // sposto il to do "Fissare ricevimento" nella bacheca Tempo libero

        Universita.stampaBacheca();
        Lavoro.stampaBacheca();
        TempoLibero.stampaBacheca();

    }
}