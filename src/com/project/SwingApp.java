package com.project;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SwingApp extends JFrame {
    private JTextField textField;
    private JLabel statusLabel;
    private JCheckBox enableButtons;
    private JPanel mainPanel;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JProgressBar progressBar;
    private int totalTasks = 0;
    private int completedTasks = 0;

    // Liste pour stocker les fichiers ouverts
    private List<File> filesList = new ArrayList<>();

    public SwingApp() {
        setTitle("Java Swing Application");
        setSize(900, 600); // Agrandir la taille de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout());

        // Panel supérieur
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        statusLabel = new JLabel("Bienvenue dans l'application");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(statusLabel);

        // Panneau central
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Formulaire utilisateur à gauche
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5)); // Ajuster pour ajouter plus d'éléments
        JTextField nameField = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Homme", "Femme"});
        JRadioButton option1 = new JRadioButton("Option 1");
        JRadioButton option2 = new JRadioButton("Option 2");
        ButtonGroup group = new ButtonGroup();
        group.add(option1);
        group.add(option2);

        // Définir la taille uniforme des boutons
        Dimension buttonSize = new Dimension(150, 40); // Taille uniforme plus petite pour tous les boutons

        // Réduire la taille du bouton envoyer
        JButton submitButton = new JButton("Envoyer");
        submitButton.setPreferredSize(buttonSize); // Reduire la taille du bouton
        submitButton.setMaximumSize(buttonSize); // Empêcher l'agrandissement du bouton
        JTextArea resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false); // Empêcher la modification du texte
        submitButton.addActionListener(e -> {
            if (nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nom.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                resultArea.setText("Nom: " + nameField.getText() + "\nSexe: " + genderBox.getSelectedItem());
                // Afficher les informations collectées après l'envoi
                JOptionPane.showMessageDialog(this, resultArea.getText(), "Informations collectées", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Sexe:"));
        formPanel.add(genderBox);
        formPanel.add(option1);
        formPanel.add(option2);
        formPanel.add(submitButton);  // Le bouton envoyer avec taille réduite
        formPanel.add(resultArea);   // Zone de texte pour afficher les résultats

        // Gestion des tâches à droite
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        progressBar = new JProgressBar(0, 100);
        JButton addTaskButton = new JButton("Ajouter tâche");
        addTaskButton.setPreferredSize(buttonSize); // Définir la taille pour le bouton Ajouter tâche
        addTaskButton.setMaximumSize(buttonSize);

        JButton removeTaskButton = new JButton("Supprimer tâche");
        removeTaskButton.setPreferredSize(buttonSize); // Définir la taille pour le bouton Supprimer tâche
        removeTaskButton.setMaximumSize(buttonSize);

        JButton completeTaskButton = new JButton("Terminer tâche");
        completeTaskButton.setPreferredSize(buttonSize); // Définir la taille pour le bouton Terminer tâche
        completeTaskButton.setMaximumSize(buttonSize);

        addTaskButton.addActionListener(e -> {
            String task = JOptionPane.showInputDialog(this, "Entrer la tâche :");
            if (task != null && !task.isEmpty()) {
                taskListModel.addElement(task);
                totalTasks++;
                updateProgress();
            }
        });
        removeTaskButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cette tâche ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    taskListModel.remove(selectedIndex);
                    totalTasks--;
                    updateProgress();
                }
            }
        });
        completeTaskButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                String task = taskListModel.get(selectedIndex);
                if (!task.contains("(Terminé)")) {
                    taskListModel.set(selectedIndex, task + " (Terminé)");
                    completedTasks++;
                    updateProgress();
                } else {
                    JOptionPane.showMessageDialog(this, "La tâche est déjà terminée.");
                }
            }
        });

        JPanel taskButtonPanel = new JPanel();
        taskButtonPanel.add(addTaskButton);
        taskButtonPanel.add(removeTaskButton);
        taskButtonPanel.add(completeTaskButton);
        taskPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        taskPanel.add(taskButtonPanel, BorderLayout.SOUTH);
        taskPanel.add(progressBar, BorderLayout.NORTH);

        middlePanel.add(formPanel);
        middlePanel.add(taskPanel);

        // Panneau inférieur
        JPanel bottomPanel = new JPanel(new GridLayout(2, 4, 10, 10));  // GridLayout pour organiser les boutons en lignes

        // Champs et boutons
        textField = new JTextField(20);
        JButton showMessageButton = new JButton("Afficher Message");
        showMessageButton.setPreferredSize(buttonSize);
        showMessageButton.setMaximumSize(buttonSize);
        showMessageButton.addActionListener(e -> JOptionPane.showMessageDialog(this, textField.getText()));

        enableButtons = new JCheckBox("Activer les boutons");
        enableButtons.setSelected(true);
        enableButtons.addActionListener(e -> showMessageButton.setEnabled(enableButtons.isSelected()));

        JButton closeButton = new JButton("Fermer");
        closeButton.setPreferredSize(buttonSize);
        closeButton.setMaximumSize(buttonSize);
        closeButton.addActionListener(e -> System.exit(0));

        JButton colorButton = new JButton("Changer couleur");
        colorButton.setPreferredSize(buttonSize);
        colorButton.setMaximumSize(buttonSize);
        colorButton.addActionListener(e -> {
            if (mainPanel.getBackground() == Color.LIGHT_GRAY) {
                mainPanel.setBackground(Color.WHITE);
                topPanel.setBackground(Color.WHITE);
                middlePanel.setBackground(Color.WHITE);
                bottomPanel.setBackground(Color.WHITE);
            } else {
                mainPanel.setBackground(Color.LIGHT_GRAY);
                topPanel.setBackground(Color.LIGHT_GRAY);
                middlePanel.setBackground(Color.LIGHT_GRAY);
                bottomPanel.setBackground(Color.LIGHT_GRAY);

            }
        });

        // Boutons Ouvrir et Enregistrer
        JButton openButton = new JButton("Ouvrir");
        openButton.setPreferredSize(buttonSize);
        openButton.setMaximumSize(buttonSize);
        openButton.addActionListener(e -> openFile());

        JButton saveButton = new JButton("Enregistrer");
        saveButton.setPreferredSize(buttonSize);
        saveButton.setMaximumSize(buttonSize);
        saveButton.addActionListener(e -> saveFile());

        // Ajouter les boutons au panneau inférieur
        bottomPanel.add(textField);
        bottomPanel.add(showMessageButton);
        bottomPanel.add(enableButtons);
        bottomPanel.add(closeButton);
        bottomPanel.add(colorButton);
        bottomPanel.add(openButton); // Bouton Ouvrir
        bottomPanel.add(saveButton); // Bouton Enregistrer

        // Ajouter les panneaux à l'interface
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(middlePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem quitItem = new JMenuItem("Quitter");
        quitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous quitter ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        fileMenu.add(quitItem);

        JMenu themeMenu = new JMenu("Thème");
        JMenuItem lightTheme = new JMenuItem("Clair");
        JMenuItem darkTheme = new JMenuItem("Sombre");
        lightTheme.addActionListener(e -> {
            mainPanel.setBackground(Color.WHITE);
            topPanel.setBackground(Color.WHITE);
            middlePanel.setBackground(Color.WHITE);
            bottomPanel.setBackground(Color.WHITE);
        });
        darkTheme.addActionListener(e -> {
            mainPanel.setBackground(Color.DARK_GRAY);
            topPanel.setBackground(Color.DARK_GRAY);
            middlePanel.setBackground(Color.DARK_GRAY);
            bottomPanel.setBackground(Color.DARK_GRAY);
        });
        themeMenu.add(lightTheme);
        themeMenu.add(darkTheme);

        menuBar.add(fileMenu);
        menuBar.add(themeMenu);
        setJMenuBar(menuBar);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void updateProgress() {
        if (totalTasks > 0) {
            int progress = (completedTasks * 100) / totalTasks;
            progressBar.setValue(progress);
        }
    }

    // Méthode pour ouvrir un fichier
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            filesList.add(file); // Ajouter le fichier à la liste
            JOptionPane.showMessageDialog(this, "Fichier ouvert: " + file.getName());
        }
    }

    // Méthode pour enregistrer dans un fichier
    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            filesList.add(file); // Ajouter le fichier à la liste
            JOptionPane.showMessageDialog(this, "Fichier enregistré: " + file.getName());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingApp().setVisible(true));
    }
}
