package ui;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import model.Closet;
import model.ClothingItem;
import model.EventLog;
import model.Event;
import model.Outfit;
import model.Shoe;
import model.Top;
import model.Bottom;
import model.Bottom.BottomCategory;
import model.Shoe.ShoeCategory;
import model.Top.TopCategory;
import persistance.JsonReader;
import persistance.JsonWriter;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// runs closetApp with GUI
public class ClosetApp extends JFrame {
    private Closet closet;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String PATH = "./data/closetData.json";
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HomePanel hp;
    private ClothesPanel cp;
    private OutfitsPanel op;

    // EFFECTS: Creates a new closet application
    public ClosetApp() {
        jsonWriter = new JsonWriter(PATH);
       
        setSize(500, 700);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitPrompt();
            }
        });
        giveStartingOptions();
        setTitle(closet.getName() + "'s Closet");

        cp = new ClothesPanel(closet);
        op = new OutfitsPanel(closet);
        hp = new HomePanel(closet, op);

        addNavBar(new JPanel(new GridLayout(1, 3, 0, 5)));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(hp, "Home");
        mainPanel.add(cp, "Clothes");
        mainPanel.add(op, "Outfits");
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }


    // MODIFIES: navPanel
    // EFFECTS: Creates JPanel of a navigation bar
    public void addNavBar(JPanel navPanel) {
        navPanel.setPreferredSize(new Dimension(0, 50));

        JButton homeButton = new JButton("Home");
        JButton outfitsButton = new JButton("Outfits");
        JButton clothesButton = new JButton("Clothes");

        homeButton.setBackground(new Color(139, 184, 223));
        homeButton.setOpaque(true);
        homeButton.setBorderPainted(false);
        homeButton.setForeground(Color.BLACK);

        outfitsButton.setBackground(new Color(139, 184, 223));
        outfitsButton.setOpaque(true);
        outfitsButton.setBorderPainted(false);
        outfitsButton.setForeground(Color.BLACK);

        clothesButton.setBackground(new Color(139, 184, 223));
        clothesButton.setOpaque(true);
        clothesButton.setBorderPainted(false);
        clothesButton.setForeground(Color.BLACK);

        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        outfitsButton.addActionListener(e -> cardLayout.show(mainPanel, "Outfits"));
        clothesButton.addActionListener(e -> cardLayout.show(mainPanel, "Clothes"));

        navPanel.add(homeButton);
        navPanel.add(outfitsButton);
        navPanel.add(clothesButton);
        add(navPanel, BorderLayout.SOUTH);
    }

    // EFFECTS: Asks user if they would like to load or create a new closet
    public void giveStartingOptions() {
        Object[] options = { "Load", "Create New" };
        int choice = JOptionPane.showOptionDialog(null, "Do you want to load or create a new closet?", "Load",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);

        if (choice == 0) {
            closet = loadCloset(askName());
        } else if (choice == 1) {
            closet = new Closet(askName());
        }
    }

    //EFFECTS: prompts the user if they want to save their closet before quitting
    public void quitPrompt() {
        Object[] options = { "Save and Exit", "Exit" };
        int choice = JOptionPane.showOptionDialog(null, "Do you want to save your closet before exiting?", "Save",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);

        if (choice == 0) {
            saveCloset();
        }
        for (Event e: EventLog.getInstance()) {
            System.out.println(e.getDate());
            System.out.println(e.getDescription());
        }
        EventLog.getInstance().clear();
        System.exit(0);
    }

    //EFFECTS: asks the user their name and returns it
    public String askName() {
        String name = "";
        while (name == null || name.trim().isEmpty()) {
            name = JOptionPane.showInputDialog(null, "Enter your name (no spaces):", 
                "User Name", JOptionPane.QUESTION_MESSAGE);
            if (name == null) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0); // Close the application
                }
            }
        }
        return name.trim();
    }

    // EFFECTS: saves the user's closet to file
    private void saveCloset() {
        try {
            jsonWriter.open();
            jsonWriter.write(closet);
            jsonWriter.close();
            System.out.println("Saved " + closet.getName());
        } catch (IOException e) {
            System.out.println("Unable to write to file: " + PATH);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the user's closet from the file and returns it, or creates a new one if not found
    private Closet loadCloset(String name) {
        jsonReader = new JsonReader(PATH, name);
        try {
            closet = jsonReader.read();
            if (closet == null) {
                JOptionPane.showMessageDialog(null, "Data not found. Creating a new closet for " + name, 
                                            "Closet Not Found", JOptionPane.INFORMATION_MESSAGE);
                return new Closet(name);
            }
            JOptionPane.showMessageDialog(null, "Loaded closet for: " + closet.getName(), 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            return closet;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + PATH + ". Creating a new closet for" + name,
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return new Closet(name);
        }
    }
}
