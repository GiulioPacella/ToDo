package implementazioni.postgres.dao;

import dao.BachecaDAO;
import database.ConnessioneDatabase;
import model.Bacheca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Implementazione DAO per la gestione delle Bacheche in PostgreSQL.
 * Fornisce metodi per aggiornare, modificare, svuotare e recuperare le Bacheche
 * dal database
 */
public class BachecaImplementazionePostgreDAO implements BachecaDAO {

    /**
     * Metodo per l'Update della bacheca quando vengono modificati il suo titolo o la sua descrizione
     *
     * @param bacheca la bacheca
     * @param idBacheca l'id della bacheca
     */
    public void updateBacheca(Bacheca bacheca, int idBacheca) {
        String sql = "UPDATE bacheca SET titolo = ?, descrizione = ? WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bacheca.getTitolo());
            stmt.setString(2, bacheca.getDescrizioneBacheca());
            stmt.setInt(3, idBacheca);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Restituisce le bacheche di un utente dal suo username
     *
     * @param login l'username dell'utente
     * @return le bacheche in ordine di id
     *
     */
    public List<Bacheca> getBachecheByUtente(String login) {
        String sql = "SELECT id, titolo, descrizione, proprietario " +
                "FROM bacheca " +
                "WHERE proprietario = ? " +
                "ORDER BY id";

        List<Bacheca> bacheche = new ArrayList<>();

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Bacheca b = new Bacheca(
                        rs.getString("titolo"),
                        rs.getString("descrizione")
                );
                b.setId(rs.getInt("id")); // assegni subito l'id corretto
                bacheche.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bacheche;
    }


}
