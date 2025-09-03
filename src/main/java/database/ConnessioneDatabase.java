package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe di utilità per la gestione della connessione al database.
 * Fornisce un metodo per ottenere una connessione al DB
 */
public class ConnessioneDatabase {

    private Connection connection = null;
    private String nome = "postgres";
    private String password = "2525";
    private String url = "jdbc:postgresql://localhost:5432/ToDo";
    private String driver = "org.postgresql.Driver";


    /**
     * Costruttore della classe ConnessioneDatabase
     */
    private ConnessioneDatabase() throws SQLException {

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Restituisce la connessione al database
     *
     * @return la connessione al database
     */
    public static Connection getConnection() throws SQLException
    {
       ConnessioneDatabase connessioneDatabase = new ConnessioneDatabase();
       return connessioneDatabase.connection;
    }


}
