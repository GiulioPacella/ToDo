package model;

/**
 * Rappresenta un Utente del sistema.
 * Ogni utente ha un username, una password e 3 bacheche personali nelle quali può creare, modificare ed eliminare i todo
 */
public class Utente {
    private final String login;
    private String password;
    private Bacheca bacheca1;
    private Bacheca bacheca2;
    private Bacheca bacheca3;

    /**
     * Istanzia un nuovo utente nel model.
     *
     * @param login    l'username
     * @param password la password
     */
    public Utente(String login, String password)
    {
        this.login = login;
        this.password = password;
        this.bacheca1 = new Bacheca("Università", "Bacheca dedicata all'Università");
        this.bacheca2 = new Bacheca("Lavoro", "Bacheca dedicata al Lavoro");
        this.bacheca3 = new Bacheca("Tempo Libero", "Bacheca dedicata al Tempo Libero");
    }

    /**
     * Restituisce l'username.
     *
     * @return l'username
     */
    public String getLogin() {
        return login;
    }


    /**
     * Restituisce la password
     *
     * @return la password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Restituisce la bacheca numero 1
     *
     * @return la bacheca 1
     */
    public Bacheca getBacheca1()
    {
        return bacheca1;
    }

    /**
     * Restituisce la bacheca numero 2
     *
     * @return la bacheca 2
     */
    public Bacheca getBacheca2()
    {return bacheca2; }

    /**
     * Restituisce la bacheca numero 3
     *
     * @return la bacheca 3
     */
    public Bacheca getBacheca3()
    { return bacheca3; }


    /**
     * Imposta la password
     *
     * @param password la nuova password
     *
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Imposta la bacheca numero 1
     *
     * @param bacheca1 la nuova bacheca numero 1
     *
     */
    public void setBacheca1(Bacheca bacheca1) {
        this.bacheca1 = bacheca1;
    }

    /**
     * Imposta la bacheca numero 2
     *
     * @param bacheca2 la nuova bacheca numero 2
     *
     */
    public void setBacheca2(Bacheca bacheca2) {
        this.bacheca2 = bacheca2;
    }

    /**
     * Imposta la bacheca numero 3
     *
     * @param bacheca3 la nuova bacheca numero 3
     *
     */
    public void setBacheca3(Bacheca bacheca3) {
        this.bacheca3 = bacheca3;
    }


    /**
     * Restituisce la bacheca conoscendo il suo id
     *
     * @param id l'id della bacheca
     * @return la bacheca
     *
     */
    public Bacheca getBachecaById(int id) {
        if (bacheca1 != null && bacheca1.getId() == id) return bacheca1;
        if (bacheca2 != null && bacheca2.getId() == id) return bacheca2;
        if (bacheca3 != null && bacheca3.getId() == id) return bacheca3;
        return null;
    }

}

