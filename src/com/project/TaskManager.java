package com.project;

import javax.swing.*;

class TaskManager {
    private DefaultListModel<String> taskModel;
    private JList<String> taskList;
    private JProgressBar progressBar;

    public TaskManager() {
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);
        progressBar = new JProgressBar(0, 100);
    }

    public JList<String> getTaskList() { return taskList; }
    public JProgressBar getProgressBar() { return progressBar; }
    public JPanel getTaskPanel() {
        JPanel panel = new JPanel();
        JButton addTaskButton = new JButton("Ajouter Tâche");
        JButton removeTaskButton = new JButton("Supprimer Tâche");

        addTaskButton.addActionListener(e -> addTask());
        removeTaskButton.addActionListener(e -> removeTask());

        panel.add(new JScrollPane(taskList));
        panel.add(addTaskButton);
        panel.add(removeTaskButton);
        panel.add(progressBar);
        return panel;
    }

    private void addTask() {
        String task = JOptionPane.showInputDialog("Nouvelle tâche:");
        if (task != null && !task.isEmpty()) {
            taskModel.addElement(task);
            updateProgressBar();
        }
    }

    private void removeTask() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            taskModel.remove(index);
            updateProgressBar();
        }
    }

    private void updateProgressBar() {
        int totalTasks = taskModel.getSize();
        int completedTasks = (int) taskModel.elements().asIterator().next().chars().filter(c -> c == '✓').count();
        progressBar.setValue(totalTasks == 0 ? 0 : (completedTasks * 100) / totalTasks);
    }
}