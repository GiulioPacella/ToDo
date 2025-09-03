package gui;

import controller.Controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import model.*;


/**
 * Rappresenta la schermata principale dell'applicazione, dove vengono mostrate le 3 bacheche contenenti i todo e da dove è possibile accedere alle altre schermate
 */
public class HomeView {



    final Utente utenteCorrente;
    private JPanel panel1;
    LocalDate oggi = LocalDate.now();
    final Controller controller;
    String desc = "Descrizione:";

    private JList<ToDo> list1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JList<ToDo> list2;
    private JList<ToDo> list3;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JButton createUniButton;
    private JButton createLavoroButton;
    private JButton createTempoLiberoButton;
    private JButton svuotaTLButton;
    private JButton svuotaLavoroButton;
    private JButton svuotaUniButton;
    private JButton modificaTLButton;
    private JButton modificaLavoroButton;
    private JButton modificaUniButton;
    private JPanel panel5;
    private JButton cercaPerNome;
    private JButton logoutButton;
    private JButton cercaPerScadenzaButton;
    private JSeparator separator1;
    private JSeparator separator2;


    /**
     * Costruttore della classe HomeView
     *
     * @param controller il controller
     * @param utente     l'utente corrente
     */
    public HomeView(Controller controller, Utente utente) {
        this.controller = controller;
        this.utenteCorrente = utente;

        $$$setupUI$$$();

        inizializzaListModel();
        configuraPanelPrincipale();
        configuraTitoliBacheche();
        configuraBottoniRicerca();
        configuraBottoneLogout();
        configuraListe();
        popolaBacheche();
        configuraListeners();
        configuraRicerca();
        setupKeyBindings();
    }

    /**
     * Inizializza i modelli di lista
     *
     */
    private void inizializzaListModel() {
        DefaultListModel<ToDo> listModel1 = new DefaultListModel<>();
        DefaultListModel<ToDo> listModel2 = new DefaultListModel<>();
        DefaultListModel<ToDo> listModel3 = new DefaultListModel<>();

        list1.setModel(listModel1);
        list2.setModel(listModel2);
        list3.setModel(listModel3);
    }

    /**
     * Regola i pannelli e imposta la struttura a griglia che ospita le 3 bacheche
     *
     */
    private void configuraPanelPrincipale() {
        panel2.setPreferredSize(new Dimension(300, 100));
        panel3.setPreferredSize(new Dimension(300, 100));
        panel4.setPreferredSize(new Dimension(300, 100));
        panel5.setPreferredSize(new Dimension(120, 100));

        panel1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0;
        c.weightx = 1.0;
        panel2.setPreferredSize(new Dimension(280, 100));
        panel2.setMinimumSize(new Dimension(280, 100));
        panel1.add(panel2, c);

        c.gridx = 1;
        c.weightx = 1.0;
        panel3.setPreferredSize(new Dimension(280, 100));
        panel3.setMinimumSize(new Dimension(280, 100));
        panel1.add(panel3, c);

        c.gridx = 2;
        c.weightx = 1.0;
        panel4.setPreferredSize(new Dimension(280, 100));
        panel4.setMinimumSize(new Dimension(280, 100));
        panel1.add(panel4, c);

        c.gridx = 3;
        c.weightx = 0.0;
        panel5.setPreferredSize(new Dimension(150, 100));
        panel5.setMinimumSize(new Dimension(150, 100));
        panel1.add(panel5, c);

        panel2.setOpaque(true);
    }

    /**
     * Imposta i titoli delle bacheche
     *
     */
    private void configuraTitoliBacheche() {
        label1.setText(controller.getBachecaCorrente(1).getTitolo());
        label2.setText(controller.getBachecaCorrente(2).getTitolo());
        label3.setText(controller.getBachecaCorrente(3).getTitolo());
    }


    /**
     * Configura i bottoni che permettono di cercare un todo in base al titolo o in base ala data di scadenza
     */
    private void configuraBottoniRicerca() {
        cercaPerNome.setBorderPainted(false);
        cercaPerNome.setContentAreaFilled(false);
        cercaPerNome.setFocusPainted(false);
        cercaPerNome.setOpaque(false);
        cercaPerNome.setForeground(Color.GRAY);
        cercaPerNome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cercaPerNome.setFont(new Font("SansSerif", Font.PLAIN, 12));

        cercaPerScadenzaButton.setBorderPainted(false);
        cercaPerScadenzaButton.setContentAreaFilled(false);
        cercaPerScadenzaButton.setFocusPainted(false);
        cercaPerScadenzaButton.setOpaque(false);
        cercaPerScadenzaButton.setForeground(Color.GRAY);
        cercaPerScadenzaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cercaPerScadenzaButton.setFont(new Font("SansSerif", Font.PLAIN, 12));

        separator1.setForeground(Color.LIGHT_GRAY);
        separator2.setForeground(Color.LIGHT_GRAY);
    }

    /**
     * Configura il bottone per effettuare il logout
     *
     */
    private void configuraBottoneLogout() {
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setOpaque(false);
        logoutButton.setForeground(Color.GRAY);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        logoutButton.addActionListener(_ -> controller.startTodo());
    }

    /**
     * Configura le 3 liste e il loro layout
     *
     */
    private void configuraListe() {
        configuraSingolaLista(list1);
        configuraSingolaLista(list2);
        configuraSingolaLista(list3);

        Border roundedBorder = createRoundedBorder();
        list1.setBorder(roundedBorder);
        list2.setBorder(roundedBorder);
        list3.setBorder(roundedBorder);

        list1.setCellRenderer(createCustomCellRenderer());
        list2.setCellRenderer(createCustomCellRenderer());
        list3.setCellRenderer(createCustomCellRenderer());

        list1.repaint();
        list2.repaint();
        list3.repaint();
    }

    /**
     * Configura una lista e il suo aspetto estetico
     *
     * @param list la lista da configurare
     */
    private void configuraSingolaLista(JList<ToDo> list) {
        list.setBackground(Color.WHITE);
        list.setSelectionBackground(new Color(0, 0, 0, 0));
        list.setSelectionForeground(Color.LIGHT_GRAY);
        list.setOpaque(false);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Configura i bordi stondati per le bacheche e per i todo
     */
    private Border createRoundedBorder() {
        return new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(200, 200, 200));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x, y, width - 1, height - 1, 20, 20);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(10, 10, 10, 10);
            }
        };
    }

    /**
     * Popola le 3 bacheche
     */
    private void popolaBacheche() {
        popolaBacheca1();
        popolaBacheca2();
        popolaBacheca3();
    }

    /**
     * Popola la bacheca numero 1
     */
    private void popolaBacheca1() {
        DefaultListModel<ToDo> listModel1 = (DefaultListModel<ToDo>) list1.getModel();
        for (ToDo todo : utenteCorrente.getBacheca1().getTodos()) {
            listModel1.addElement(todo);
        }
    }

    /**
     * Popola la bacheca numero 2
     */
    private void popolaBacheca2() {
        DefaultListModel<ToDo> listModel2 = (DefaultListModel<ToDo>) list2.getModel();
        for (ToDo todo : utenteCorrente.getBacheca2().getTodos()) {
            listModel2.addElement(todo);
        }
    }

    /**
     * Popola la bacheca numero 3
     */
    private void popolaBacheca3() {
        DefaultListModel<ToDo> listModel3 = (DefaultListModel<ToDo>) list3.getModel();
        for (ToDo todo : utenteCorrente.getBacheca3().getTodos()) {
            listModel3.addElement(todo);
        }
    }

    /**
     * Configura i listener per le 3 bacheche
     */
    private void configuraListeners() {
        configuraListenersBacheca1();
        configuraListenersBacheca2();
        configuraListenersBacheca3();
        configuraListenersSelezioneMutua();
    }

    /**
     * Configura i listener per la bacheca numero 1
     */
    private void configuraListenersBacheca1() {
        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list1.locationToIndex(e.getPoint());
                if (index != -1) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        list1.setSelectedIndex(index);
                    } else if (SwingUtilities.isRightMouseButton(e) && list1.isSelectedIndex(index)) {
                        list1.removeSelectionInterval(index, index);
                    }
                }
            }
        });

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = list1.locationToIndex(e.getPoint());
                    if (index != -1) {
                        ToDo todoSelezionato = list1.getModel().getElementAt(index);
                        controller.showModify(utenteCorrente, utenteCorrente.getBacheca1(), todoSelezionato);
                    }
                }
            }
        });

        createUniButton.addActionListener(_ -> controller.showCreation(utenteCorrente, utenteCorrente.getBacheca1()));
        svuotaUniButton.addActionListener(_ -> {
            controller.eliminaBacheca(utenteCorrente.getBacheca1());
            controller.showHome(utenteCorrente);
        });

        modificaUniButton.addActionListener(_ -> {
            Bacheca b = controller.getBachecaCorrente(1);
            mostraDialogoModificaBacheca(b, utenteCorrente.getBacheca1());
        });
    }

    /**
     * Configura i listener per la bacheca numero 2
     */
    private void configuraListenersBacheca2() {
        list2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list2.locationToIndex(e.getPoint());
                if (index != -1) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        list2.setSelectedIndex(index);
                    } else if (SwingUtilities.isRightMouseButton(e) && list2.isSelectedIndex(index)) {
                        list2.removeSelectionInterval(index, index);
                    }
                }
            }
        });

        list2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = list2.locationToIndex(e.getPoint());
                    if (index != -1) {
                        ToDo todoSelezionato = list2.getModel().getElementAt(index);
                        controller.showModify(utenteCorrente, utenteCorrente.getBacheca2(), todoSelezionato);
                    }
                }
            }
        });

        createLavoroButton.addActionListener(_ -> controller.showCreation(utenteCorrente, utenteCorrente.getBacheca2()));
        svuotaLavoroButton.addActionListener(_ -> {
            controller.eliminaBacheca(utenteCorrente.getBacheca2());
            controller.showHome(utenteCorrente);
        });

        modificaLavoroButton.addActionListener(_ -> {
            Bacheca b = controller.getBachecaCorrente(2);
            mostraDialogoModificaBacheca(b, utenteCorrente.getBacheca2());
        });
    }

    /**
     * Configura i listener per la bacheca numero 3
     */
    private void configuraListenersBacheca3() {
        list3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list3.locationToIndex(e.getPoint());
                if (index != -1) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        list3.setSelectedIndex(index);
                    } else if (SwingUtilities.isRightMouseButton(e) && list3.isSelectedIndex(index)) {
                        list3.removeSelectionInterval(index, index);
                    }
                }
            }
        });

        list3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = list3.locationToIndex(e.getPoint());
                    if (index != -1) {
                        ToDo todoSelezionato = list3.getModel().getElementAt(index);
                        controller.showModify(utenteCorrente, utenteCorrente.getBacheca3(), todoSelezionato);
                    }
                }
            }
        });

        createTempoLiberoButton.addActionListener(_ -> controller.showCreation(utenteCorrente, utenteCorrente.getBacheca3()));
        svuotaTLButton.addActionListener(_ -> {
            controller.eliminaBacheca(utenteCorrente.getBacheca3());
            controller.showHome(utenteCorrente);
        });

        modificaTLButton.addActionListener(_ -> {
            Bacheca b = controller.getBachecaCorrente(3);
            mostraDialogoModificaBacheca(b, utenteCorrente.getBacheca3());
        });
    }

    /**
     * Configura e apre la finestra per la modifica della bacheca
     */
    private void mostraDialogoModificaBacheca(Bacheca b, Bacheca bachecaDaAggiornare) {
        JTextField nomeField = new JTextField(b.getTitolo());
        JTextArea descrizioneArea = new JTextArea(b.getDescrizioneBacheca(), 5, 20);
        descrizioneArea.setLineWrap(true);
        descrizioneArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descrizioneArea);

        Object[] message = {
                "Nome bacheca:", nomeField,
                desc, scrollPane
        };

        int option = JOptionPane.showConfirmDialog(
                null,
                message,
                "Gestione bacheca",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            controller.aggiornaBacheca(bachecaDaAggiornare, nomeField.getText(), descrizioneArea.getText());
            controller.showHome(utenteCorrente);
        }
    }

    /**
     * Configura i listener per la corretta selezione dei todo
     */
    private void configuraListenersSelezioneMutua() {
        list1.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && list1.getSelectedIndex() != -1) {
                list2.clearSelection();
                list3.clearSelection();
            }
        });

        list2.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && list2.getSelectedIndex() != -1) {
                list1.clearSelection();
                list3.clearSelection();
            }
        });

        list3.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && list3.getSelectedIndex() != -1) {
                list1.clearSelection();
                list2.clearSelection();
            }
        });
    }

    /**
     * Configura la ricerca per titolo e per scadenza dei todo
     */
    private void configuraRicerca() {
        cercaPerNome.addActionListener(_ -> {
            JTextField titoloField = new JTextField();

            Object[] message = {
                    "Inserisci il titolo:", titoloField,
            };

            int option = JOptionPane.showConfirmDialog(
                    null,
                    message,
                    "Ricerca ToDo",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (option == JOptionPane.OK_OPTION) {
                controller.searchToDo(titoloField.getText());
            }
        });

        cercaPerScadenzaButton.addActionListener(_ -> {
            SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
            JSpinner dateSpinner = new JSpinner(dateModel);
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
            dateSpinner.setEditor(dateEditor);

            int result = JOptionPane.showConfirmDialog(
                    null,
                    dateSpinner,
                    "Seleziona la data di scadenza",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                Date selectedDate = (Date) dateSpinner.getValue();
                LocalDate dataInserita = selectedDate.toInstant()
                        .atZone(java.util.TimeZone.getDefault().toZoneId())
                        .toLocalDate();

                List<ToDo> scadenze = controller.getToDoEntroData(utenteCorrente, dataInserita);
                controller.showExpiring(scadenze, dataInserita);
            }
        });
    }


    /**
     * SetUp dei KeyBindings per poter spostare un To Do da una bacheca all'altra con le freccette
     *
     */
    private void setupKeyBindings() {
        setupArrowKey(list1, "RIGHT", () -> spostaToDo(list1, utenteCorrente.getBacheca1(), utenteCorrente.getBacheca2()));
        setupArrowKey(list2, "RIGHT", () -> spostaToDo(list2, utenteCorrente.getBacheca2(), utenteCorrente.getBacheca3()));
        setupArrowKey(list2, "LEFT", () -> spostaToDo(list2, utenteCorrente.getBacheca2(), utenteCorrente.getBacheca1()));
        setupArrowKey(list3, "LEFT", () -> spostaToDo(list3, utenteCorrente.getBacheca3(), utenteCorrente.getBacheca2()));
    }

    /**
     * SetUp delle funzioni per poter spostare un To Do da una bacheca all'altra con le freccette
     *
     * @param list la lista di todo
     * @param key il tasto da premere
     * @param  action l'azione che viene effettuata (lo spostamento dei todo)
     */
    private void setupArrowKey(JList<ToDo> list, String key, Runnable action) {
        InputMap im = list.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = list.getActionMap();

        im.put(KeyStroke.getKeyStroke(key), key);
        am.put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    /**
     * SetUp dei KeyBindings e delle funzioni per poter spostare un To Do da una bacheca all'altra con le freccette
     *
     */
    private void spostaToDo(JList<ToDo> sourceList, Bacheca sourceBacheca, Bacheca targetBacheca) {
        ToDo selected = sourceList.getSelectedValue();
        if (selected != null && controller.checkAutorePerSpostamento(selected, utenteCorrente)) {
            sourceBacheca.getTodos().remove(selected);
            targetBacheca.getTodos().add(selected);

            controller.salvaPosizione(selected, targetBacheca);
            controller.showHome(utenteCorrente);

        }
    }

    /**
     * Restituisce il Root Panel
     *
     * @return il root panel
     */
    public JPanel getRootPanel() {
        return panel1;
    }


    // Renderer personalizzate per le liste (per una funzione prettamente estetica)

    private DefaultListCellRenderer createCustomCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                if (value instanceof ToDo todo) {

                    JPanel contentPanel = new JPanel(new BorderLayout());
                    contentPanel.setOpaque(false);

                    JLabel titleLabel = new JLabel(truncate(todo.getTitoloTodo(), 24));

                    JLabel dateLabel = new JLabel(todo.getScadenza().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    dateLabel.setFont(dateLabel.getFont().deriveFont(Font.ITALIC, 11f));
                    dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

                    Icon colorIcon = new Icon() {
                        @Override
                        public void paintIcon(Component c, Graphics g, int x, int y) {
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                            g2.setColor(new Color(0, 0, 0, 30));
                            g2.fillRoundRect(x + 1, y + 1, getIconWidth(), getIconHeight(), 6, 6);

                            g2.setColor(todo.getColore());
                            g2.fillRoundRect(x, y, getIconWidth(), getIconHeight(), 6, 6);

                            g2.setColor(new Color(255, 255, 255, 60));
                            g2.fillRoundRect(x + 1, y + 1, getIconWidth() - 6, getIconHeight() - 6, 4, 4);

                            g2.setColor(new Color(0, 0, 0, 100));
                            g2.setStroke(new BasicStroke(1.2f));
                            g2.drawRoundRect(x, y, getIconWidth() - 1, getIconHeight() - 1, 6, 6);
                        }

                        @Override
                        public int getIconWidth() {
                            return 12;
                        }

                        @Override
                        public int getIconHeight() {
                            return 12;
                        }
                    };

                    titleLabel.setIcon(colorIcon);
                    titleLabel.setIconTextGap(10);

                    if (todo.getCompletato() == Boolean.TRUE) {
                        dateLabel.setForeground(new Color(34, 139, 34));
                    } else if (todo.getScadenza().isBefore(oggi)) {
                        dateLabel.setForeground(Color.RED);
                    } else {
                        titleLabel.setForeground(new Color(50, 50, 50));
                    }

                    contentPanel.add(titleLabel, BorderLayout.WEST);
                    contentPanel.add(dateLabel, BorderLayout.EAST);

                    JPanel outerPanel = new JPanel(new BorderLayout());
                    outerPanel.setOpaque(true);
                    outerPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 8, 0));

                    JPanel innerPanel = new JPanel(new BorderLayout()) {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                            g2.setColor(new Color(0, 0, 0, 15));
                            g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 16, 16);

                            Color bgColor = isSelected ? new Color(240, 248, 255) : new Color(250, 250, 250);
                            g2.setColor(bgColor);
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                            g2.setColor(new Color(220, 220, 220));
                            g2.setStroke(new BasicStroke(1.0f));
                            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);

                            if (isSelected) {
                                g2.setColor(new Color(100, 149, 237, 100));
                                g2.setStroke(new BasicStroke(2.0f));
                                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
                            }
                        }
                    };

                    innerPanel.setOpaque(false);
                    innerPanel.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
                    innerPanel.add(contentPanel, BorderLayout.CENTER);

                    outerPanel.setEnabled(false);
                    innerPanel.setEnabled(false);
                    titleLabel.setEnabled(true);
                    dateLabel.setEnabled(true);

                    outerPanel.add(innerPanel, BorderLayout.CENTER);
                    return outerPanel;
                }

                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        };


    }

    /**
     * Tronca un testo
     *
     * @param text      il testo
     * @param maxLength la lunghezza massima che può avere
     * @return il testo troncato
     */
    public String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        } else {
            return text.substring(0, maxLength) + "...";  // aggiunge i "..." per indicare il troncamento
        }
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
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        list2 = new JList();
        panel3.add(list2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.ITALIC, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Label");
        panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        createLavoroButton = new JButton();
        createLavoroButton.setText("+");
        panel6.add(createLavoroButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel6.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        svuotaLavoroButton = new JButton();
        Font svuotaLavoroButtonFont = this.$$$getFont$$$(null, -1, -1, svuotaLavoroButton.getFont());
        if (svuotaLavoroButtonFont != null) svuotaLavoroButton.setFont(svuotaLavoroButtonFont);
        svuotaLavoroButton.setText("Svuota");
        panel3.add(svuotaLavoroButton, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificaLavoroButton = new JButton();
        Font modificaLavoroButtonFont = this.$$$getFont$$$("Segoe UI Emoji", Font.ITALIC, 12, modificaLavoroButton.getFont());
        if (modificaLavoroButtonFont != null) modificaLavoroButton.setFont(modificaLavoroButtonFont);
        modificaLavoroButton.setText("\uD83D\uDCCC");
        panel3.add(modificaLavoroButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel7, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        list3 = new JList();
        panel4.add(list3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.ITALIC, 14, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Label");
        panel4.add(label3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel4.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel8, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        createTempoLiberoButton = new JButton();
        createTempoLiberoButton.setText("+");
        panel8.add(createTempoLiberoButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel8.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        svuotaTLButton = new JButton();
        svuotaTLButton.setText("Svuota");
        panel4.add(svuotaTLButton, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificaTLButton = new JButton();
        Font modificaTLButtonFont = this.$$$getFont$$$("Segoe UI Emoji", Font.ITALIC, 12, modificaTLButton.getFont());
        if (modificaTLButtonFont != null) modificaTLButton.setFont(modificaTLButtonFont);
        modificaTLButton.setText("\uD83D\uDCCC");
        panel4.add(modificaTLButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel9, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        list1 = new JList();
        panel2.add(list1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.ITALIC, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Label");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel10, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        createUniButton = new JButton();
        createUniButton.setHorizontalAlignment(0);
        createUniButton.setText("+");
        panel10.add(createUniButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        panel10.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        svuotaUniButton = new JButton();
        svuotaUniButton.setText("Svuota");
        panel2.add(svuotaUniButton, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificaUniButton = new JButton();
        Font modificaUniButtonFont = this.$$$getFont$$$("Segoe UI Emoji", Font.ITALIC, 12, modificaUniButton.getFont());
        if (modificaUniButtonFont != null) modificaUniButton.setFont(modificaUniButtonFont);
        modificaUniButton.setText("\uD83D\uDCCC");
        panel2.add(modificaUniButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel11, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer7 = new com.intellij.uiDesigner.core.Spacer();
        panel5.add(spacer7, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel12, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        separator2 = new JSeparator();
        panel12.add(separator2, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cercaPerScadenzaButton = new JButton();
        cercaPerScadenzaButton.setText("Cerca per scadenza");
        panel12.add(cercaPerScadenzaButton, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cercaPerNome = new JButton();
        cercaPerNome.setText("Cerca per nome");
        panel12.add(cercaPerNome, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        separator1 = new JSeparator();
        panel12.add(separator1, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer8 = new com.intellij.uiDesigner.core.Spacer();
        panel12.add(spacer8, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel12.add(panel13, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText(" ");
        panel12.add(label6, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText(" ");
        panel12.add(label7, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel14, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer9 = new com.intellij.uiDesigner.core.Spacer();
        panel14.add(spacer9, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel15, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        logoutButton = new JButton();
        logoutButton.setText("Logout");
        panel15.add(logoutButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer10 = new com.intellij.uiDesigner.core.Spacer();
        panel5.add(spacer10, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
     * $$$ get root component $$$ j component.
     *
     * @return the j component
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
