package model;

/**
 * The type model.Utente.
 */
public class Utente {
    private final String login;
    private String password;
    private Bacheca Universita;
    private Bacheca Lavoro;
    private Bacheca TempoLibero;

    /**
     * Instantiates a new model.Utente.
     *
     * @param login    the login
     * @param password the password
     */
    public Utente(String login, String password)
    {
        this.login = login;
        this.password = password;
        this.Universita = new Bacheca(Bacheca.titoloBacheca.Universita, "Bacheca dedicata all'Università");
        this.Lavoro = new Bacheca(Bacheca.titoloBacheca.Lavoro, "Bacheca dedicata al Lavoro");
        this.TempoLibero = new Bacheca(Bacheca.titoloBacheca.TempoLibero, "Bacheca dedicata al Tempo Libero");
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Bacheca getUniversita()
    {
        return Universita;
    }

    public Bacheca getLavoro()
    {return Lavoro; }

    public Bacheca getTempoLibero()
    { return TempoLibero; }
}

