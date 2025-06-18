package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Locale;

import model.*;

import static java.awt.AWTEventMulticaster.add;

public class HomeView {


    private Utente utenteCorrente;
    private JPanel panel1;
    LocalDate oggi = LocalDate.now();

    private DefaultListModel<ToDo> todoListModel;

    private Controller controller;

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
    private JLabel label5;
    private JLabel DescrizioneBachecaTL;
    private JLabel DescrizioneBachecaUni;
    private JLabel DescrizioneLavoro;
    private JButton SpostaUniR;
    private JButton SpostaUniL;
    private JButton SpostaLavoroR;
    private JButton SpostaLavoroL;
    private JButton SpostaTemR;
    private JButton SpostaTemL;
    private JButton button1;
    private JButton button2;
    private ToDo selectedTodo;


    private String Bacheca1Name = "Università";
    private String Bacheca2Name = "Lavoro";
    private String Bacheca3Name = "Tempo Libero";

    public HomeView(Controller controller, Utente utente) {

        this.controller = controller;
        this.utenteCorrente = utente;

        DefaultListModel<ToDo> listModel1 = new DefaultListModel<>();
        $$$setupUI$$$();

        panel1.setLayout(new GridLayout(1, 3, 10, 15)); // 1 riga, 3 colonne uguali
        panel1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel1.add(panel2);
        panel1.add(panel3);
        panel1.add(panel4);


        // Lista Università

        for (ToDo todo : utente.getUniversita().getTodos()) {
            listModel1.addElement(todo);
        }
        label1.setText(Bacheca1Name);
        list1.setModel(listModel1);
        DescrizioneBachecaUni.setText(utente.getUniversita().getDescrizioneBacheca());

        list1.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ToDo) {
                    ToDo todo = (ToDo) value;
                    label.setText(todo.getTitoloTodo());
                    if (todo.getScadenza().isBefore(oggi) && todo.getCompletato() == Boolean.FALSE) {
                        label.setForeground(Color.RED);
                    } else {
                        label.setForeground(Color.BLACK);
                        label.setBorder(null);
                    }

                    if (!isSelected) {
                        label.setBackground(todo.getColore());
                        label.setOpaque(true);
                    }
                }
                return label;
            }
        });

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list1.locationToIndex(e.getPoint());
                if (index != -1) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        list1.setSelectedIndex(index); // Seleziona con il sinistro
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        if (list1.isSelectedIndex(index)) {
                            list1.removeSelectionInterval(index, index); // Deseleziona con il destro
                        }
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
                        controller.showModify(utenteCorrente, utenteCorrente.getUniversita(), todoSelezionato);
                    }
                }
            }
        });

        createUniButton.addActionListener(e -> {
            controller.showCreation(utente, utente.getUniversita());
        });
        svuotaUniButton.addActionListener
                (e ->
                        {
                            utente.getUniversita().getTodos().clear();
                            controller.showHome(utenteCorrente);
                        }
                );
        modificaUniButton.addActionListener(e -> {
            String nuovaDesc = JOptionPane.showInputDialog(
                    null,
                    "Inserisci la descrizione:",
                    DescrizioneBachecaUni.getText()
            );

            if (nuovaDesc != null && !nuovaDesc.trim().isEmpty()) {
                DescrizioneBachecaUni.setText(nuovaDesc.trim());
                utente.getUniversita().setDescrizioneBacheca(DescrizioneBachecaUni.getText());
            } else if (nuovaDesc != null) {
                JOptionPane.showMessageDialog(null, "La descrizione non può essere vuota");
            }
        });

        SpostaUniR.addActionListener(e -> {
            selectedTodo = list1.getSelectedValue();
            if (selectedTodo != null) {
                utente.getUniversita().getTodos().remove(selectedTodo);
                utente.getLavoro().getTodos().add(selectedTodo);
                controller.showHome(utenteCorrente);
            }

        });

        // Lista Lavoro //

        DefaultListModel<ToDo> listModel2 = new DefaultListModel<>();
        for (ToDo todo : utente.getLavoro().getTodos()) {
            listModel2.addElement(todo);
        }
        label2.setText(Bacheca2Name);
        list2.setModel(listModel2);
        DescrizioneLavoro.setText(utente.getLavoro().getDescrizioneBacheca());

        list2.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ToDo) {
                    ToDo todo = (ToDo) value;
                    label.setText(todo.getTitoloTodo());
                    if (todo.getScadenza().isBefore(oggi) && todo.getCompletato() == Boolean.FALSE) {
                        label.setForeground(Color.RED);
                    } else {
                        label.setForeground(Color.BLACK);
                        label.setBorder(null);
                    }

                    if (!isSelected) {
                        label.setBackground(todo.getColore());
                        label.setOpaque(true);
                    }
                }
                return label;
            }
        });

        list2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list2.locationToIndex(e.getPoint());
                if (index != -1) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        list2.setSelectedIndex(index); // Seleziona con il sinistro
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        if (list2.isSelectedIndex(index)) {
                            list2.removeSelectionInterval(index, index); // Deseleziona con il destro
                        }
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
                        controller.showModify(utenteCorrente, utenteCorrente.getLavoro(), todoSelezionato);
                    }
                }
            }
        });

        createLavoroButton.addActionListener(e -> {
            controller.showCreation(utente, utente.getLavoro());
        });

        svuotaLavoroButton.addActionListener
                (e ->
                        {
                            utente.getLavoro().getTodos().clear();
                            controller.showHome(utenteCorrente);
                        }
                );

        modificaLavoroButton.addActionListener(e -> {
            String nuovaDesc = JOptionPane.showInputDialog(
                    null,
                    "Inserisci la descrizione:",
                    DescrizioneLavoro.getText()
            );

            if (nuovaDesc != null && !nuovaDesc.trim().isEmpty()) {
                DescrizioneLavoro.setText(nuovaDesc.trim());
                utente.getLavoro().setDescrizioneBacheca(DescrizioneLavoro.getText());
            } else if (nuovaDesc != null) {
                JOptionPane.showMessageDialog(null, "La descrizione non può essere vuota");
            }
        });

        SpostaLavoroL.addActionListener(e -> {
            selectedTodo = list2.getSelectedValue();
            if (selectedTodo != null) {
                utente.getLavoro().getTodos().remove(selectedTodo);
                utente.getUniversita().getTodos().add(selectedTodo);
                controller.showHome(utenteCorrente);
            }

        });

        SpostaLavoroR.addActionListener(e -> {
            selectedTodo = list2.getSelectedValue();
            if (selectedTodo != null) {
                utente.getLavoro().getTodos().remove(selectedTodo);
                utente.getTempoLibero().getTodos().add(selectedTodo);
                controller.showHome(utenteCorrente);
            }

        });


        // Lista Tempo Libero

        DefaultListModel<ToDo> listModel3 = new DefaultListModel<>();
        for (ToDo todo : utente.getTempoLibero().getTodos()) {
            listModel3.addElement(todo);
        }
        label3.setText(Bacheca3Name);
        list3.setModel(listModel3);
        DescrizioneBachecaTL.setText(utente.getTempoLibero().getDescrizioneBacheca());

        list3.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ToDo) {
                    ToDo todo = (ToDo) value;
                    label.setText(todo.getTitoloTodo());
                    if (todo.getScadenza().isBefore(oggi) && todo.getCompletato() == Boolean.FALSE) {
                        label.setForeground(Color.RED);
                    } else {
                        label.setForeground(Color.BLACK);
                        label.setBorder(null);
                    }

                    if (!isSelected) {
                        label.setBackground(todo.getColore());
                        label.setOpaque(true);
                    }
                }
                return label;
            }
        });

        list3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list3.locationToIndex(e.getPoint());
                if (index != -1) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        list3.setSelectedIndex(index); // Seleziona con il sinistro
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        if (list3.isSelectedIndex(index)) {
                            list3.removeSelectionInterval(index, index); // Deseleziona con il destro
                        }
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
                        controller.showModify(utenteCorrente, utenteCorrente.getLavoro(), todoSelezionato);
                    }
                }
            }
        });

        createTempoLiberoButton.addActionListener(e -> {
            controller.showCreation(utente, utente.getTempoLibero());
        });

        svuotaTLButton.addActionListener
                (e ->
                        {
                            utente.getTempoLibero().getTodos().clear();
                            controller.showHome(utenteCorrente);
                        }
                );

        modificaTLButton.addActionListener(e -> {
            String nuovaDesc = JOptionPane.showInputDialog(
                    null,
                    "Inserisci la descrizione:",
                    DescrizioneBachecaTL.getText()
            );

            if (nuovaDesc != null && !nuovaDesc.trim().isEmpty()) {
                DescrizioneBachecaTL.setText(nuovaDesc.trim());
                utente.getTempoLibero().setDescrizioneBacheca(DescrizioneBachecaTL.getText());
            } else if (nuovaDesc != null) {
                JOptionPane.showMessageDialog(null, "La descrizione non può essere vuota");
            }
        });

        SpostaTemL.addActionListener(e -> {
            selectedTodo = list3.getSelectedValue();
            if (selectedTodo != null) {
                utente.getTempoLibero().getTodos().remove(selectedTodo);
                utente.getLavoro().getTodos().add(selectedTodo);
                controller.showHome(utenteCorrente);
            }

        });


        // Listener per evitare la selezione di molteplici ToDoS in liste diverse

        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

    public JPanel getRootPanel() {
        return panel1;
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
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        list2 = new JList();
        panel3.add(list2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 20, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Label");
        panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        DescrizioneLavoro = new JLabel();
        DescrizioneLavoro.setText("Label");
        panel5.add(DescrizioneLavoro, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        createLavoroButton = new JButton();
        createLavoroButton.setText("+");
        panel6.add(createLavoroButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel6.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        SpostaLavoroR = new JButton();
        SpostaLavoroR.setText(">");
        panel6.add(SpostaLavoroR, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        SpostaLavoroL = new JButton();
        SpostaLavoroL.setText("<");
        panel6.add(SpostaLavoroL, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        svuotaLavoroButton = new JButton();
        Font svuotaLavoroButtonFont = this.$$$getFont$$$(null, -1, -1, svuotaLavoroButton.getFont());
        if (svuotaLavoroButtonFont != null) svuotaLavoroButton.setFont(svuotaLavoroButtonFont);
        svuotaLavoroButton.setText("Svuota");
        panel3.add(svuotaLavoroButton, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificaLavoroButton = new JButton();
        Font modificaLavoroButtonFont = this.$$$getFont$$$(null, -1, -1, modificaLavoroButton.getFont());
        if (modificaLavoroButtonFont != null) modificaLavoroButton.setFont(modificaLavoroButtonFont);
        modificaLavoroButton.setText("Modifica");
        panel3.add(modificaLavoroButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel7, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        list3 = new JList();
        panel4.add(list3, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, 20, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Label");
        panel4.add(label3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel4.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel8, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        DescrizioneBachecaTL = new JLabel();
        DescrizioneBachecaTL.setText("Label");
        panel8.add(DescrizioneBachecaTL, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel9, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        createTempoLiberoButton = new JButton();
        createTempoLiberoButton.setText("+");
        panel9.add(createTempoLiberoButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel9.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        SpostaTemR = new JButton();
        SpostaTemR.setText(">");
        panel9.add(SpostaTemR, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        SpostaTemL = new JButton();
        SpostaTemL.setText("<");
        panel9.add(SpostaTemL, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        svuotaTLButton = new JButton();
        svuotaTLButton.setText("Svuota");
        panel4.add(svuotaTLButton, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificaTLButton = new JButton();
        Font modificaTLButtonFont = this.$$$getFont$$$(null, -1, -1, modificaTLButton.getFont());
        if (modificaTLButtonFont != null) modificaTLButton.setFont(modificaTLButtonFont);
        modificaTLButton.setText("Modifica");
        panel4.add(modificaTLButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel10, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        list1 = new JList();
        panel2.add(list1, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 20, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Label");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel11, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        DescrizioneBachecaUni = new JLabel();
        DescrizioneBachecaUni.setText("Label");
        panel11.add(DescrizioneBachecaUni, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel12, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        createUniButton = new JButton();
        createUniButton.setHorizontalAlignment(0);
        createUniButton.setText("+");
        panel12.add(createUniButton, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        panel12.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        SpostaUniR = new JButton();
        SpostaUniR.setText(">");
        panel12.add(SpostaUniR, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        SpostaUniL = new JButton();
        SpostaUniL.setText("<");
        panel12.add(SpostaUniL, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button1 = new JButton();
        button1.setText("Button");
        panel12.add(button1, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button2 = new JButton();
        button2.setText("Button");
        panel12.add(button2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        svuotaUniButton = new JButton();
        svuotaUniButton.setText("Svuota");
        panel2.add(svuotaUniButton, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificaUniButton = new JButton();
        Font modificaUniButtonFont = this.$$$getFont$$$(null, -1, -1, modificaUniButton.getFont());
        if (modificaUniButtonFont != null) modificaUniButton.setFont(modificaUniButtonFont);
        modificaUniButton.setText("Modifica");
        panel2.add(modificaUniButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel13, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
