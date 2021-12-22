package source;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class Main {
    SlangWord slangWord;
    public Main(){}

    public static void main(String[] args) {
        Main myObj = new Main();
        myObj.slangWord = new SlangWord("data/slang.txt");
        Menu(myObj);
    }

    public static void Menu(Main mainObj){
        JFrame menuFrame = new JFrame("Slang Word");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 3, 30 , 30));
        mainPanel.setBorder(new EmptyBorder(new Insets(30, 30, 30,30)));

        JButton searchButton = new JButton("SEARCH");
        JButton historyButton = new JButton("SEARCH HISTORY");
        JButton addButton = new JButton("ADD A SLANG WORD");
        JButton editButton = new JButton("EDIT A SLANG WORD");
        JButton deleteButton = new JButton("DELETE A SLANG WORD");
        JButton resetButton = new JButton("RESET SLANG WORDS LIST");
        JButton randomButton = new JButton("RANDOM A SLANG WORD");
        JButton quizButton = new JButton("QUIZ");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search(mainObj);
            }
        });
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHistory(mainObj);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSlangWord(mainObj);
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchAndEdit(mainObj);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSlangWord(mainObj);
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetToOrigin(mainObj);
            }
        });
        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomSlangWord(mainObj);
            }
        });
        quizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateQuiz(mainObj);
            }
        });

        mainPanel.add(searchButton);
        mainPanel.add(historyButton);
        mainPanel.add(addButton);
        mainPanel.add(editButton);
        mainPanel.add(deleteButton);
        mainPanel.add(resetButton);
        mainPanel.add(randomButton);
        mainPanel.add(quizButton);

        menuFrame.add(mainPanel);
        menuFrame.pack();
        menuFrame.setVisible(true);
        menuFrame.setLocationRelativeTo(null);
    }

    public static void search(Main mainObj){
        JFrame frame = new JFrame("Dictionary");
        String[] title = {"Slang Word", "Meaning"};
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        GridBagConstraints constraint = new GridBagConstraints();
        JPanel constrainPanel = new JPanel(new GridBagLayout());

        constraint.fill = GridBagConstraints.WEST;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.insets = new Insets(5,5,5,5);
        JLabel searchLabel = new JLabel("Slang word:");
        constrainPanel.add(searchLabel, constraint);

        constraint.fill = GridBagConstraints.BOTH;
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.insets = new Insets(5,5,5,5);
        JTextField searchField = new JTextField(40);
        constrainPanel.add(searchField, constraint);

        JButton button = new JButton("Search");
        constraint.fill = GridBagConstraints.EAST;
        constraint.gridx = 2;
        constraint.gridy = 0;
        constraint.insets = new Insets(5,5,5,5);
        constrainPanel.add(button, constraint);

        constraint.fill = GridBagConstraints.BOTH;
        constraint.gridwidth = 3;
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.insets = new Insets(5,5,5,5);
        DefaultTableModel model = new DefaultTableModel(new String[0][], title);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        constrainPanel.add(scrollPane, constraint);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.setRowHeight(30);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame searchFrame = new JFrame("Search");
                JButton byDefinition = new JButton("Search by definition");
                JButton byWord = new JButton("Search by word");

                byWord.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        searchFrame.dispose();
                        for(int i = model.getRowCount() - 1; i >= 0; i--)
                            model.removeRow(i);
                        String keyword = searchField.getText();
                        ArrayList<String> arrayList = mainObj.slangWord.findByWord(keyword);
                        if(arrayList == null) {
                            JOptionPane.showMessageDialog(null, "Slang word not found!", "Search failed!", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            for (String s : arrayList)
                                model.addRow(new String[]{searchField.getText(), s});

                            try {
                                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("data/history.txt", true));
                                bufferedWriter.write(keyword + "\n");
                                bufferedWriter.close();
                            }
                            catch (IOException exception){
                                System.out.println(exception.getMessage());
                            }
                        }
                    }
                });

                byDefinition.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        searchFrame.dispose();
                        for(int i = model.getRowCount() - 1; i >= 0; i--)
                            model.removeRow(i);
                        HashMap<String, ArrayList<String>> result = mainObj.slangWord.findByMeaning(searchField.getText());
                        if(result.size() == 0) {
                            JOptionPane.showMessageDialog(null, "Definition not found!", "Search failed!", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            Iterator<Map.Entry<String, ArrayList<String>>> iterator = result.entrySet().iterator();
                            try {
                                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("data/history.txt", true));
                                while (iterator.hasNext()) {
                                    Map.Entry<String, ArrayList<String>> entry = iterator.next();
                                    bufferedWriter.write(entry.getKey() + "\n");
                                    ArrayList<String> tmpArr = entry.getValue();
                                    for(String s : tmpArr)
                                        model.addRow(new String[]{entry.getKey(), s});
                                    iterator.remove();
                                }
                                bufferedWriter.close();
                            }
                            catch (IOException exception){
                                System.out.println(exception.getMessage());
                            }
                        }
                    }
                });
                JPanel panel = new JPanel(new GridLayout(1,2,5,5));
                panel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
                panel.add(byWord);
                panel.add(byDefinition);
                searchFrame.add(panel);
                searchFrame.pack();
                searchFrame.setVisible(true);
                searchFrame.setLocationRelativeTo(null);
            }
        });

        mainPanel.add(constrainPanel);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public static void viewHistory(Main mainObj){
        JFrame frame = new JFrame("History");
        String[] title = {"Slang Word", "Meaning"};

        JPanel constrainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill = GridBagConstraints.BOTH;
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.insets = new Insets(5,5,5,5);
        DefaultTableModel model = new DefaultTableModel(new String[0][], title);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        constrainPanel.add(scrollPane, constraint);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.setRowHeight(30);

        try {
            FileReader fileReader = new FileReader("data/history.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String lines;
            while((lines = bufferedReader.readLine()) != null)
                if(!lines.equals("")) {
                    ArrayList<String> arr = mainObj.slangWord.findByWord(lines);
                    if(arr != null)
                        for(String s : arr)
                            model.addRow(new String[]{lines, s});
                    else
                        model.addRow(new String[]{lines, "#------This slang word is deleted------#"});
                }
            bufferedReader.close();
        }
        catch (IOException exception){
            System.out.println(exception.getMessage());
        }

        frame.add(constrainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public static void addSlangWord(Main mainObj){
        String[] labelName = {"Slang word: ", "Meaning: "};
        JTextField[] textFieldArray = new JTextField[2];
        JFrame addSlangWordFrame = new JFrame("Add A Slang Word");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel constraintPanel = new JPanel();
        constraintPanel.setLayout(new GridBagLayout());
        for (int i = 0; i < 2; ++i) {
            GridBagConstraints constraint = new GridBagConstraints();
            constraint.anchor = GridBagConstraints.EAST;
            constraint.gridx = 0;
            constraint.gridy = i;
            constraint.ipadx = 5;
            JLabel label = new JLabel(labelName[i]);
            constraintPanel.add(label, constraint);
            constraint.fill = GridBagConstraints.BOTH;
            constraint.gridx = 1;
            constraint.gridy = i;
            constraint.insets = new Insets(5,5,5,5);
            textFieldArray[i] = new JTextField(40);
            constraintPanel.add(textFieldArray[i], constraint);
        }
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20,0)));
        buttonPanel.add(cancelButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mainObj.slangWord.findByWord(textFieldArray[0].getText()) == null) {
                    mainObj.slangWord.addSlangWord(textFieldArray[0].getText(), textFieldArray[1].getText());
                    JOptionPane.showMessageDialog(addSlangWordFrame, "Slang word added!", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    String[] options = {"Duplicate", "Overwrite" };
                    int choice = JOptionPane.showOptionDialog(null, "'" + textFieldArray[0].getText() + "' have already existed in the slang words list", "Slang word existed", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);

                    if(choice == 0){
                        mainObj.slangWord.duplicateSlangWord(textFieldArray[0].getText(), textFieldArray[1].getText());
                        JOptionPane.showMessageDialog(null, "Slang word duplicated!", "Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else if(choice == 1){
                        mainObj.slangWord.overwriteSlangWord(textFieldArray[0].getText(), textFieldArray[1].getText());
                        JOptionPane.showMessageDialog(null, "Slang word overwritten!", "Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSlangWordFrame.dispose();
            }
        });
        mainPanel.add(constraintPanel);
        mainPanel.add(buttonPanel);

        addSlangWordFrame.add(mainPanel);
        addSlangWordFrame.pack();
        addSlangWordFrame.setVisible(true);
        addSlangWordFrame.setLocationRelativeTo(null);
    }
    public static void searchAndEdit(Main mainObj){
        JFrame frame = new JFrame("Search For Edit");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        GridBagConstraints constraint = new GridBagConstraints();
        JPanel constrainPanel = new JPanel(new GridBagLayout());

        constraint.fill = GridBagConstraints.BOTH;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.insets = new Insets(5,5,5,5);
        JLabel label = new JLabel("Search:");
        constrainPanel.add(label, constraint);

        constraint.fill = GridBagConstraints.BOTH;
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.insets = new Insets(5,5,5,5);
        JTextField searchField = new JTextField(40);
        constrainPanel.add(searchField, constraint);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Search");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20,0)));
        buttonPanel.add(cancelButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mainObj.slangWord.findByWord(searchField.getText()) != null) {
                    editSlangWord(mainObj, searchField.getText());
                    frame.dispose();
                }
                else
                    JOptionPane.showMessageDialog(frame, "Slang word not found!", "Message", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        mainPanel.add(constrainPanel);
        mainPanel.add(buttonPanel);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public static void editSlangWord(Main mainObj, String word){
        final String originWord = word;
        ArrayList<String> meaning = mainObj.slangWord.findByWord(word);
        JFrame editSlangWordFrame = new JFrame("Edit A Slang Word");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel constraintPanel = new JPanel();
        constraintPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.EAST;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.ipadx = 5;
        JLabel label = new JLabel("Slang Word:");
        constraintPanel.add(label, constraint);

        constraint.fill = GridBagConstraints.BOTH;
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.insets = new Insets(5,5,5,5);
        JTextField slangWord = new JTextField(40);
        slangWord.setText(word);
        constraintPanel.add(slangWord, constraint);

        JTextField[] textFieldArray = new JTextField[meaning.size()];
        for (int i = 0; i < meaning.size(); i++) {
            constraint.anchor = GridBagConstraints.EAST;
            constraint.gridx = 0;
            constraint.gridy = i + 2;
            constraint.ipadx = 5;
            if(meaning.size() == 1)
                constraintPanel.add(new JLabel("Meaning:"), constraint);
            else
                constraintPanel.add(new JLabel("Meaning " + (i + 1) + ":"), constraint);
            constraint.fill = GridBagConstraints.BOTH;
            constraint.gridx = 1;
            constraint.gridy = i + 2;
            constraint.insets = new Insets(5,5,5,5);
            textFieldArray[i] = new JTextField(40);
            textFieldArray[i].setText(meaning.get(i));
            constraintPanel.add(textFieldArray[i], constraint);
        }
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20,0)));
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = slangWord.getText();
                ArrayList<String>meaning = new ArrayList<>();
                for (JTextField jTextField : textFieldArray) {
                    if(!Objects.equals(jTextField.getText(), ""))
                        meaning.add(jTextField.getText());
                }
                if(meaning.size() > 0) {
                    if(!Objects.equals(originWord, word))
                        if(mainObj.slangWord.findByWord(word) != null) {
                            JOptionPane.showMessageDialog(null, "Slang word existed!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    mainObj.slangWord.editSlangWord(originWord, word, meaning);
                    JOptionPane.showMessageDialog(editSlangWordFrame, "Slang word saved!", "Message", JOptionPane.INFORMATION_MESSAGE);
                    editSlangWordFrame.dispose();
                }
                else
                    JOptionPane.showMessageDialog(null, "Slang word must have at least 1 meaning!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSlangWordFrame.dispose();
            }
        });

        mainPanel.add(constraintPanel);
        mainPanel.add(buttonPanel);
        editSlangWordFrame.add(mainPanel);
        editSlangWordFrame.pack();
        editSlangWordFrame.setVisible(true);
        editSlangWordFrame.setLocationRelativeTo(null);
    }
    public static void deleteSlangWord(Main mainObj){
        JFrame frame = new JFrame("Delete A Slang Word");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        GridBagConstraints constraint = new GridBagConstraints();
        JPanel constrainPanel = new JPanel(new GridBagLayout());

        constraint.fill = GridBagConstraints.BOTH;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.insets = new Insets(5,5,5,5);
        JLabel label = new JLabel("Slang word: ");
        constrainPanel.add(label, constraint);

        constraint.fill = GridBagConstraints.BOTH;
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.insets = new Insets(5,5,5,5);
        JTextField searchField = new JTextField(40);
        constrainPanel.add(searchField, constraint);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20,0)));
        buttonPanel.add(cancelButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mainObj.slangWord.findByWord(searchField.getText()) != null) {
                    int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure to delete this slang word", "Delete confirm", JOptionPane.YES_NO_OPTION);
                    if(confirm == 0) {
                        mainObj.slangWord.deleteSlangWord(searchField.getText());
                        frame.dispose();
                    }
                }
                else
                    JOptionPane.showMessageDialog(frame, "Slang word not found!", "Message", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        mainPanel.add(constrainPanel);
        mainPanel.add(buttonPanel);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public static void resetToOrigin(Main mainObj){
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to reset the slang words list", "Reset confirm", JOptionPane.YES_NO_OPTION);
        if(confirm == 0) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader("data/slang(origin).txt"));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("data/slang.txt"));
                String lines;
                while ((lines = bufferedReader.readLine()) != null)
                    bufferedWriter.write(lines + "\n");
                bufferedReader.close();
                bufferedWriter.close();
                mainObj.slangWord = new SlangWord("data/slang.txt");
                JOptionPane.showMessageDialog(null, "Slang words list reset!", "Reset origin slang words list", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
    public static void randomSlangWord(Main mainObj){
        String[] title = {"Slang Word", "Meaning"};
        JFrame randomSlangWordFrame = new JFrame("On This Day Slang Word");

        JPanel constraintPanel = new JPanel();
        constraintPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridwidth = 3;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.ipadx = 5;
        constraint.insets = new Insets(10,5,5,5);
        JButton button = new JButton("Click to random a slang word");
        constraintPanel.add(button, constraint);

        constraint.fill = GridBagConstraints.BOTH;
        constraint.gridwidth = 3;
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.insets = new Insets(15,5,5,5);
        DefaultTableModel model = new DefaultTableModel(new String[0][], title);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        constraintPanel.add(scrollPane, constraint);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.setRowHeight(30);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = model.getRowCount() - 1; i >= 0; i--)
                    model.removeRow(i);
                String randomWord = mainObj.slangWord.randomSlangWord();
                ArrayList<String> meaning = mainObj.slangWord.findByWord(randomWord);
                for(String s : meaning)
                    model.addRow(new String[]{randomWord, s});
            }
        });

        randomSlangWordFrame.add(constraintPanel);
        randomSlangWordFrame.pack();
        randomSlangWordFrame.setVisible(true);
        randomSlangWordFrame.setLocationRelativeTo(null);
    }
    public static void generateQuiz(Main mainObj){
        JFrame frame = new JFrame("Quiz");
        JPanel panel = new JPanel();

        JButton buttonWord = new JButton("Quiz with Word");
        JButton buttonDefinition = new JButton("Quiz with Definition");

        buttonWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quizWithWord(mainObj);
            }
        });

        buttonDefinition.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quizWithDefinition(mainObj);
            }
        });

        panel.setLayout(new GridLayout(1,2,5,5));
        panel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        panel.add(buttonWord);
        panel.add(buttonDefinition);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public static void quizWithWord(Main mainObj){
        ArrayList<String> choices = new ArrayList<>();
        for(int i = 0; i < 4; i++)
            choices.add(mainObj.slangWord.randomSlangWord());
        int randomNumber = (int) Math.floor(Math.random() * 3);
        JFrame frame = new JFrame("Quiz With Word");
        JLabel question = new JLabel("What does '" + choices.get(randomNumber) + "' means?");
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,20));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(new Insets(0,15,15,15)));

        JButton buttonA = new JButton(mainObj.slangWord.getMeaning(choices.get(0)));
        JButton buttonB = new JButton(mainObj.slangWord.getMeaning(choices.get(1)));
        JButton buttonC = new JButton(mainObj.slangWord.getMeaning(choices.get(2)));
        JButton buttonD = new JButton(mainObj.slangWord.getMeaning(choices.get(3)));

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isWrong = false;
                if(e.getSource() == buttonA){
                    if(randomNumber == 0)
                        buttonA.setBackground(Color.GREEN);
                    else {
                        buttonA.setBackground(Color.RED);
                        isWrong = true;
                    }
                }
                else if(e.getSource() == buttonB){
                    if(randomNumber == 1)
                        buttonB.setBackground(Color.GREEN);
                    else{
                        buttonB.setBackground(Color.RED);
                        isWrong = true;
                    }
                }
                else if(e.getSource() == buttonC){
                    if(randomNumber == 2)
                        buttonC.setBackground(Color.GREEN);
                    else{
                        buttonC.setBackground(Color.RED);
                        isWrong = true;
                    }
                }
                else {
                    if(randomNumber == 3)
                        buttonD.setBackground(Color.GREEN);
                    else{
                        buttonD.setBackground(Color.RED);
                        isWrong = true;
                    }
                }
                if(isWrong){
                    if(randomNumber == 0)
                        buttonA.setBackground(Color.GREEN);
                    else if(randomNumber == 1)
                        buttonB.setBackground(Color.GREEN);
                    else if(randomNumber == 2)
                        buttonC.setBackground(Color.GREEN);
                    else
                        buttonD.setBackground(Color.GREEN);
                }

                buttonA.setEnabled(false);
                buttonB.setEnabled(false);
                buttonC.setEnabled(false);
                buttonD.setEnabled(false);
            }
        };

        buttonA.addActionListener(actionListener);
        buttonB.addActionListener(actionListener);
        buttonC.addActionListener(actionListener);
        buttonD.addActionListener(actionListener);

        buttonPanel.add(buttonA);
        buttonPanel.add(buttonB);
        buttonPanel.add(buttonC);
        buttonPanel.add(buttonD);
        labelPanel.add(question);
        mainPanel.add(labelPanel);
        mainPanel.add(buttonPanel);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public static void quizWithDefinition(Main mainObj){
        ArrayList<String> choices = new ArrayList<>();
        for(int i = 0; i < 4; i++)
            choices.add(mainObj.slangWord.randomSlangWord());
        int randomNumber = (int) Math.floor(Math.random() * 3);
        JFrame frame = new JFrame("Quiz With Definition");
        JLabel question = new JLabel("Which slang word below has the meaning '" + mainObj.slangWord.getMeaning(choices.get(randomNumber)) + "'?");
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,20));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(new Insets(0,15,15,15)));

        JButton buttonA = new JButton(choices.get(0));
        JButton buttonB = new JButton(choices.get(1));
        JButton buttonC = new JButton(choices.get(2));
        JButton buttonD = new JButton(choices.get(3));

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isWrong = false;
                if(e.getSource() == buttonA){
                    if(randomNumber == 0)
                        buttonA.setBackground(Color.GREEN);
                    else {
                        buttonA.setBackground(Color.RED);
                        isWrong = true;
                    }
                }
                else if(e.getSource() == buttonB){
                    if(randomNumber == 1)
                        buttonB.setBackground(Color.GREEN);
                    else{
                        buttonB.setBackground(Color.RED);
                        isWrong = true;
                    }
                }
                else if(e.getSource() == buttonC){
                    if(randomNumber == 2)
                        buttonC.setBackground(Color.GREEN);
                    else{
                        buttonC.setBackground(Color.RED);
                        isWrong = true;
                    }
                }
                else {
                    if(randomNumber == 3)
                        buttonD.setBackground(Color.GREEN);
                    else{
                        buttonD.setBackground(Color.RED);
                        isWrong = true;
                    }
                }
                if(isWrong){
                    if(randomNumber == 0)
                        buttonA.setBackground(Color.GREEN);
                    else if(randomNumber == 1)
                        buttonB.setBackground(Color.GREEN);
                    else if(randomNumber == 2)
                        buttonC.setBackground(Color.GREEN);
                    else
                        buttonD.setBackground(Color.GREEN);
                }

                buttonA.setEnabled(false);
                buttonB.setEnabled(false);
                buttonC.setEnabled(false);
                buttonD.setEnabled(false);
            }
        };

        buttonA.addActionListener(actionListener);
        buttonB.addActionListener(actionListener);
        buttonC.addActionListener(actionListener);
        buttonD.addActionListener(actionListener);

        buttonPanel.add(buttonA);
        buttonPanel.add(buttonB);
        buttonPanel.add(buttonC);
        buttonPanel.add(buttonD);
        labelPanel.add(question);
        mainPanel.add(labelPanel);
        mainPanel.add(buttonPanel);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}