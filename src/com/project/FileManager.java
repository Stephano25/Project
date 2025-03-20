package com.project;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class FileManager {
    public static void openFile(JFrame parent, JTextArea textArea) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                textArea.setText(new String(Files.readAllBytes(Paths.get(file.getPath()))));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "Erreur lors de la lecture du fichier", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void saveFile(JFrame parent, JTextArea textArea) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.exists()) {
                int confirm = JOptionPane.showConfirmDialog(parent, "Le fichier existe déjà. Voulez-vous l'écraser ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textArea.getText());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "Erreur lors de l'enregistrement", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
