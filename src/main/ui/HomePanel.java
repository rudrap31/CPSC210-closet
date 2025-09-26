package ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import model.Closet;
import model.Outfit;
import model.ClothingItem;
import model.Top;
import model.Bottom;
import model.Shoe;

// home panel that displays an outfits with options to customize and save
public class HomePanel extends JPanel {
    private Closet closet;
    private Outfit current;
    private JPanel displayPanel;
    private OutfitsPanel outfitsPanel;
    private static final int IMAGE_SIZE = 150;

    // EFFECTS: creates a home panel displying an outfit
    public HomePanel(Closet closet, OutfitsPanel outfitsPanel) {
        this.closet = closet;
        this.outfitsPanel =  outfitsPanel;
        current = null;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Welcome to Your Closet!");
        JButton rerollButton = new JButton("Reroll Outfit");
        JButton saveButton = new JButton("Save Outfit");

        add(label);
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(rerollButton);
        buttons.add(saveButton);
        label.setAlignmentX(CENTER_ALIGNMENT);
        buttons.setAlignmentX(CENTER_ALIGNMENT);
        add(buttons);
        displayPanel = new JPanel(new GridLayout(0, 1));
        add(displayPanel);

        rerollButton.addActionListener(e -> {
            current = closet.generateRandomOutfit();
            updateOutfitDisplay();
        });
        
        saveButton.addActionListener(e -> saveOutfit());
        updateOutfitDisplay();
    }

    // EFFECTS: displayed the top outfit values
    public JPanel displayTop() { 
        JPanel topPanel = new JPanel();
        topPanel.setAlignmentX(CENTER_ALIGNMENT);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        try {           
            BufferedImage myPicture = ImageIO.read(new File(current.getTop().getFilepath()));
            Image scaledImage = myPicture.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
            JButton topButton = new JButton(new ImageIcon(scaledImage));
            topButton.setAlignmentX(CENTER_ALIGNMENT);
            topButton.addActionListener(e -> selectNewTop());
            topPanel.add(topButton);
            JLabel topLabel = new JLabel(current.getTop().getName());
            topLabel.setAlignmentX(CENTER_ALIGNMENT);
            topPanel.add(topLabel);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Top image not found");
            topPanel.add(errorLabel);
        }
        return topPanel;
    }

    // EFFECTS: displayed the bottom outfit values
    public JPanel displayBottom() { 
        JPanel bottomPanel = new JPanel();
        bottomPanel.setAlignmentX(CENTER_ALIGNMENT);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        try {           
            BufferedImage myPicture = ImageIO.read(new File(current.getBottom().getFilepath()));
            Image scaledImage = myPicture.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
            JButton bottomButton = new JButton(new ImageIcon(scaledImage));
            bottomButton.setAlignmentX(CENTER_ALIGNMENT);
            bottomButton.addActionListener(e -> selectNewBottom());
            bottomPanel.add(bottomButton);
            JLabel bottomLabel = new JLabel(current.getBottom().getName());
            bottomLabel.setAlignmentX(CENTER_ALIGNMENT);
            bottomPanel.add(bottomLabel);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Bottom image not found");
            bottomPanel.add(errorLabel);
        }
        return bottomPanel;
    }

    // EFFECTS: displayed the shoe outfit values
    public JPanel displayShoe() { 
        JPanel shoePanel = new JPanel();
        shoePanel.setAlignmentX(CENTER_ALIGNMENT);
        shoePanel.setLayout(new BoxLayout(shoePanel, BoxLayout.Y_AXIS));
        try {           
            BufferedImage myPicture = ImageIO.read(new File(current.getShoe().getFilepath()));
            Image scaledImage = myPicture.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
            JButton shoeButton = new JButton(new ImageIcon(scaledImage));
            shoeButton.setAlignmentX(CENTER_ALIGNMENT);
            shoeButton.addActionListener(e -> selectNewShoe());
            shoePanel.add(shoeButton);
            JLabel shoeLabel = new JLabel(current.getShoe().getName());
            shoeLabel.setAlignmentX(CENTER_ALIGNMENT);
            shoePanel.add(shoeLabel);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Shoe image not found");
            shoePanel.add(errorLabel);
        }
        return shoePanel;
    }

    // MODIFIES: this
    // EFFECTS: displayed the current outfit values
    // if no current outfit is selected displays random one.
    // else if there are no Tops or no Bottoms or No Shoe displays message
    public void updateOutfitDisplay() {
        displayPanel.removeAll();
        if (closet.getClothingType("Top").size() == 0 
                    || closet.getClothingType("Bottom").size() == 0 
                    || closet.getClothingType("Shoe").size() == 0) {
            return;
        }
        if (current == null) {
            current = new Outfit((Top) closet.getClothingType("Top").get(0), 
            (Bottom) closet.getClothingType("Bottom").get(0), 
            (Shoe) closet.getClothingType("Shoe").get(0));
        }

        JPanel topPanel = displayTop();

        JPanel bottomPanel = displayBottom();

        JPanel shoePanel = displayShoe();
        
        displayPanel.add(topPanel);
        displayPanel.add(bottomPanel);
        displayPanel.add(shoePanel);
        displayPanel.revalidate();
        displayPanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: allows user to select a new top
    private void selectNewTop() {
        List<ClothingItem> tops = closet.getClothingType("Top");
        if (tops.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tops available!");
            return;
        }

        ArrayList<String> optionsList = new ArrayList<>();
        for (ClothingItem top : tops) {
            optionsList.add(top.getName() + " (" + top.getColour() + ")");
        }
        String[] options = optionsList.toArray(new String[0]);

        String selected = (String) JOptionPane.showInputDialog(this,
                "Select a new top:", "Choose Top",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (selected != null) {
            String name = selected.substring(0, selected.indexOf(" ("));
            for (ClothingItem top : tops) {
                if (top.getName().equals(name)) {
                    current = new Outfit((Top) top, current.getBottom(), current.getShoe());
                    updateOutfitDisplay();
                    break;
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: allows user to select a new bottom
    private void selectNewBottom() {
        List<ClothingItem> bottoms = closet.getClothingType("Bottom");
        if (bottoms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No bottoms available!");
            return;
        }
        ArrayList<String> optionsList = new ArrayList<>();
        for (ClothingItem bottom : bottoms) {
            optionsList.add(bottom.getName() + " (" + bottom.getColour() + ")");
        }
        String[] options = optionsList.toArray(new String[0]);
        String selected = (String) JOptionPane.showInputDialog(this,
                "Select a new bottom:",  "Choose Bottom",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (selected != null) {
            String name = selected.substring(0, selected.indexOf(" ("));
            for (ClothingItem bottom : bottoms) {
                if (bottom.getName().equals(name)) {
                    current = new Outfit(current.getTop(), (Bottom) bottom, current.getShoe());
                    updateOutfitDisplay();
                    break;
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: allows user to select new shoes
    private void selectNewShoe() {
        List<ClothingItem> shoes = closet.getClothingType("Shoe");
        if (shoes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No shoes available!");
            return;
        }

        ArrayList<String> optionsList = new ArrayList<>();
        for (ClothingItem shoe : shoes) {
            optionsList.add(shoe.getName() + " (" + shoe.getColour() + ")");
        }
        String[] options = optionsList.toArray(new String[0]);
        String selected = (String) JOptionPane.showInputDialog(this,
                "Select new shoes:", "Choose Shoes",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (selected != null) {
            String name = selected.substring(0, selected.indexOf(" ("));
            for (ClothingItem shoe : shoes) {
                if (shoe.getName().equals(name)) {
                    current = new Outfit(current.getTop(), current.getBottom(), (Shoe) shoe);
                    updateOutfitDisplay();
                    break;
                }
            }
        }
    }

    // MODIFIES: closet
    // EFFECTS: save the current outfit displayed on screen
    public void saveOutfit() {
        if (current != null) {
            closet.addOutfit(current);
            outfitsPanel.updateOutfitGrid();
        }
    }
}