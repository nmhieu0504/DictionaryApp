package source;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class Main {
    JTextField searchField;
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
                JFrame frame = new JFrame("Dictionary");

                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                GridBagConstraints constraint = new GridBagConstraints();

                JPanel constrainPanel = new JPanel(new GridBagLayout());
                constraint.fill = GridBagConstraints.BOTH;
                constraint.weightx = 1;
                constraint.weighty = 1;
                constraint.gridx = 0;
                constraint.gridy = 0;
                constraint.insets = new Insets(5,5,5,5);
                JTextField searchField = new JTextField(40);
                constrainPanel.add(searchField, constraint);

                constraint.fill = GridBagConstraints.BOTH;
                constraint.weightx = 1;
                constraint.weighty = 1;
                constraint.gridx = 0;
                constraint.gridy = 1;
                constraint.insets = new Insets(5,5,5,5);
                JList<String> list = new JList<>();
                JScrollPane scrollPane = new JScrollPane(list);
                constrainPanel.add(scrollPane, constraint);

                JButton button = new JButton("Search");
                constraint.fill = GridBagConstraints.SOUTH;
                constraint.weightx = 1;
                constraint.weighty = 1;
                constraint.gridx = 0;
                constraint.gridy = 2;
                constraint.insets = new Insets(5,5,5,5);
                constrainPanel.add(button, constraint);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame searchFrame = new JFrame("Search");
                        JButton byDefinition = new JButton("Search by definition");
                        JButton byWord = new JButton("Search by word");
                        searchFrame.setLayout(new GridLayout());

                        byWord.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String keyword = searchField.getText();
                                ArrayList<String> arrayList = new ArrayList<>();
                                arrayList = mainObj.slangWord.findByWord(keyword);
                                String[] display;
                                if(arrayList == null) {
                                    display = new String[]{"Slang word not found!"};
                                }
                                else {
                                    display = new String[arrayList.size()];
                                    for (int i = 0; i < arrayList.size(); i++)
                                        display[i] = "- " + arrayList.get(i);
                                    try {
                                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("data/history.txt", true));
                                        bufferedWriter.write(keyword + "\n");
                                        bufferedWriter.close();
                                    }
                                    catch (IOException exception){
                                        System.out.println(exception.getMessage());
                                    }
                                }
                                list.setListData(display);
                                searchFrame.dispose();
                            }
                        });

                        byDefinition.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                HashMap<String, ArrayList<String>> result = new HashMap<>();
                                result = mainObj.slangWord.findByMeaning(searchField.getText());
                                String[] display;
                                String[] word;
                                if(result == null) {
                                    display = new String[]{"Definition not found!"};
                                }
                                else {
                                    display = new String[result.size()];
                                    word = new String[result.size()];
                                    Iterator<Map.Entry<String, ArrayList<String>>> iterator = result.entrySet().iterator();
                                    int idx = 0;
                                    while (iterator.hasNext()) {
                                        Map.Entry entry = (Map.Entry)iterator.next();
                                        word[idx] = (String) entry.getKey();
                                        display[idx++] = "Word: " + entry.getKey() + " - " + "Meaning: " + entry.getValue();
                                        iterator.remove();
                                    }
                                    try {
                                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("data/history.txt", true));
                                        for(String s : word)
                                            bufferedWriter.write(s + "\n");
                                        bufferedWriter.close();
                                    }
                                    catch (IOException exception){
                                        System.out.println(exception.getMessage());
                                    }
                                }
                                list.setListData(display);
                                searchFrame.dispose();
                            }
                        });

                        searchFrame.add(byWord);
                        searchFrame.add(byDefinition);
                        searchFrame.pack();
                        searchFrame.setVisible(true);
                    }
                });

                mainPanel.add(constrainPanel);
                frame.add(mainPanel);
                frame.pack();
                frame.setVisible(true);
            }
        });

        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("History");

                JPanel constrainPanel = new JPanel(new GridBagLayout());
                GridBagConstraints constraint = new GridBagConstraints();
                constraint.fill = GridBagConstraints.BOTH;
                constraint.weightx = 1;
                constraint.weighty = 1;
                constraint.gridx = 0;
                constraint.gridy = 1;
                constraint.insets = new Insets(5,5,5,5);
                JList<String> list = new JList<>();
                JScrollPane scrollPane = new JScrollPane(list);
                constrainPanel.add(scrollPane, constraint);

                Vector<String> display = new Vector<>();
                try {
                    FileReader fileReader = new FileReader("data/history.txt");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String lines;
                    while((lines = bufferedReader.readLine()) != null)
                        if(!lines.equals(""))
                            display.add(lines);
                    list.setListData(display);
                }
                catch (IOException exception){
                    System.out.println(exception.getMessage());
                }

                frame.add(constrainPanel);
                frame.pack();
                frame.setVisible(true);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        quizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
    }
}