package source;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        SlangWord slangWord = new SlangWord("data/slang.txt");
        JFrame frame = new JFrame("Dictionary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        JTextField textField = new JTextField(40);
        constrainPanel.add(textField, constraint);

        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 1;
        constraint.weighty = 1;
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.insets = new Insets(5,5,5,5);
        JList<String> list = new JList<>();
        constrainPanel.add(list, constraint);

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
                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList = slangWord.findByWord(textField.getText());
                        String[] display;
                        if(arrayList == null) {
                            display = new String[]{"Slang word not found!"};
                        }
                        else {
                            display = new String[arrayList.size()];
                            for (int i = 0; i < arrayList.size(); i++)
                                display[i] = "- " + arrayList.get(i);
                        }
                        list.setListData(display);
                        searchFrame.dispose();
                    }
                });

                byDefinition.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList = slangWord.findByMeaning(textField.getText());
                        String[] display;
                        if(arrayList == null) {
                            display = new String[]{"Slang word not found!"};
                        }
                        else {
                            display = new String[arrayList.size()];
                            for (int i = 0; i < arrayList.size(); i++)
                                display[i] = "- " + arrayList.get(i);
                        }
                        list.setListData(display);
                        searchFrame.dispose();
                    }
                });

                searchFrame.add(byDefinition);
                searchFrame.add(byWord);
                searchFrame.pack();
                searchFrame.setVisible(true);
            }
        });

        mainPanel.add(constrainPanel);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
}