package gui;

import controller.Controller;
import model.Bacheca;
import model.ToDo;
import model.Utente;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.util.List;
import java.awt.*;
import java.util.Locale;
/**
 * Rappresenta la schermata che mostra con quali utenti è condiviso un todo, chi è l'autore e permette di inserire o eliminare nuove condivisioni all'autore del todo
 */
public class ShareView {

    private Controller controller;
    private Utente utenteCorrente;
    private ToDo todoCorrente;
    private Bacheca bachecaCorrente;

    private JPanel panel1;
    private JList<Utente> listaUtenti;
    private JButton rimuoviCondivisione;
    private JButton aggiungiCondivisione;
    private JLabel campoAutore;

    /**
     * Costruttore della classe ShareView
     *
     * @param controller il controller
     * @param utente l'utente corrente
     * @param toDo il todo corrente
     * @param bacheca la bacheca contenente il todo corrente
     */
    public ShareView(Controller controller, Utente utente, ToDo toDo, Bacheca bacheca) {

        $$$setupUI$$$();

        this.controller = controller;
        this.utenteCorrente = utente;
        this.todoCorrente = toDo;
        this.bachecaCorrente = bacheca;

        inizializzaInterfaccia();
        configuraCellRenderer();
        configuraPulsanti();
        impostaAutore();

    }

    /**
     * Inizializza l'interfaccia grafica
     */
    private void inizializzaInterfaccia() {
        panel1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // top, left, bottom, right
        listaUtenti.setBackground(UIManager.getColor("Panel.background"));
        aggiornaListaCondivisioni();
    }

    /**
     * Configura il cell renderer per la lista utenti
     */
    private void configuraCellRenderer() {
        listaUtenti.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Utente utente) {
                    setText(utente.getLogin());
                }
                return this;
            }
        });
    }

    /**
     * Configura i listener per i pulsanti
     */
    private void configuraPulsanti() {
        aggiungiCondivisione.addActionListener(e -> gestisciAggiungiCondivisione());
        rimuoviCondivisione.addActionListener(e -> gestisciRimuoviCondivisione());
    }

    /**
     * Imposta il testo dell'autore
     */
    private void impostaAutore() {
        String autore = controller.getAutoreToDo(todoCorrente.getId());
        campoAutore.setText(autore);
    }

    /**
     * Gestisce l'aggiunta di una condivisione
     */
    private void gestisciAggiungiCondivisione() {
        if (!controller.checkAutore(todoCorrente.getAutore(), utenteCorrente)) {
            JOptionPane.showMessageDialog(null, "Solo l'autore può modificare le condivisioni", "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            String username = JOptionPane.showInputDialog(null, "Inserisci il nome utente con cui vuoi condividere:", "Condividi Todo", JOptionPane.PLAIN_MESSAGE);
            if (username != null && !username.trim().isEmpty()) {
                aggiungiCondivisioneConUtente(username.trim());
            }
        }
    }

    /**
     * Aggiunge una condivisione con l'utente specificato
     * @param username l'username dell'utente
     */
    private void aggiungiCondivisioneConUtente(String username) {
        Utente utenteDestinatario = controller.getUtenteByUsername(username);

        if (utenteDestinatario != null) {
            eseguiAggiungiCondivisione(utenteDestinatario);
            aggiornaListaCondivisioni();
            JOptionPane.showMessageDialog(null, "Todo condiviso con " + username);
        } else {
            JOptionPane.showMessageDialog(null, "Utente \"" + username + "\" non trovato", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Esegue l'aggiunta della condivisione per tutte le bacheche dell'utente
     */
    private void eseguiAggiungiCondivisione(Utente utenteDestinatario) {
        if (bachecaCorrente.equals(utenteCorrente.getBacheca1())) {
            controller.aggiungiCondivisione(utenteDestinatario, todoCorrente);
        }

        if (bachecaCorrente.equals(utenteCorrente.getBacheca2())) {
            controller.aggiungiCondivisione(utenteDestinatario, todoCorrente);
        }

        if (bachecaCorrente.equals(utenteCorrente.getBacheca3())) {
            controller.aggiungiCondivisione(utenteDestinatario, todoCorrente);
        }
    }

    /**
     * Gestisce la rimozione di una condivisione
     */
    private void gestisciRimuoviCondivisione() {
        if (!controller.checkAutore(todoCorrente.getAutore(), utenteCorrente)) {
            JOptionPane.showMessageDialog(null, "Solo l'autore può modificare le condivisioni", "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            Utente selected = listaUtenti.getSelectedValue();

            if (selected == null) {
                JOptionPane.showMessageDialog(null, "Seleziona un utente dalla lista", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            eseguiRimuoviCondivisione(selected);
            aggiornaListaCondivisioni();
            JOptionPane.showMessageDialog(null, "Condivisione rimossa");
        }
    }

    /**
     * Esegue la rimozione della condivisione per la bacheca corrente
     */
    private void eseguiRimuoviCondivisione(Utente selected) {
        if (bachecaCorrente.equals(utenteCorrente.getBacheca1())) {
            controller.rimuoviCondivisione(selected, todoCorrente, 1);
        }

        if (bachecaCorrente.equals(utenteCorrente.getBacheca2())) {
            controller.rimuoviCondivisione(selected, todoCorrente, 2);
        }

        if (bachecaCorrente.equals(utenteCorrente.getBacheca3())) {
            controller.rimuoviCondivisione(selected, todoCorrente, 3);
        }
    }

    /**
     * Metodo per l'aggiornamento della lista dopo che viene effettuata un'operazione su di essa
     *
     */
    private void aggiornaListaCondivisioni() {
        List<Utente> condivisi = controller.getUtentiCondivisi(todoCorrente);
        DefaultListModel<Utente> model = new DefaultListModel<>();

        for (Utente u : condivisi) {
            model.addElement(u); // aggiungo l'oggetto intero
        }
        listaUtenti.setModel(model);
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.ITALIC, 12, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Creato da:");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        campoAutore = new JLabel();
        campoAutore.setText("Label");
        panel2.add(campoAutore, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.ITALIC, 12, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Condiviso con:");
        panel2.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        listaUtenti = new JList();
        panel2.add(listaUtenti, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        aggiungiCondivisione = new JButton();
        aggiungiCondivisione.setText("Aggiungi condivisione");
        panel1.add(aggiungiCondivisione, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rimuoviCondivisione = new JButton();
        rimuoviCondivisione.setText("Rimuovi condivisione");
        panel1.add(rimuoviCondivisione, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
