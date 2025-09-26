package ui;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Closet;
import model.Outfit;

// outfits panel that displays all of the outfits
public class OutfitsPanel extends JPanel {
    private Closet closet;
    private JPanel outfitGrid;
    private static final int IMAGE_SIZE = 150;

    // EFFECTS: creates a outfit panel
    public OutfitsPanel(Closet closet) {
        this.closet = closet;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Your Outfits");
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);

        outfitGrid = new JPanel(new GridLayout(0, 3, 10, 10));

        JScrollPane scrollPane = new JScrollPane(outfitGrid);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);

        updateOutfitGrid();
    }

    // MODIFIES: this
    // EFFECTS: display the Top
    public void displayTop(Outfit outfit, JPanel outfitPanel) {
        try {
            BufferedImage myPicture = ImageIO.read(new File(outfit.getTop().getFilepath()));
            Image scaledImage = myPicture.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
            JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
            picLabel.setAlignmentX(CENTER_ALIGNMENT);
            outfitPanel.add(picLabel);
            JLabel topLabel = new JLabel("Top: " + outfit.getTop().getName());
            topLabel.setAlignmentX(CENTER_ALIGNMENT);
            outfitPanel.add(topLabel);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Top image not found");
            errorLabel.setAlignmentX(CENTER_ALIGNMENT);
            outfitPanel.add(errorLabel);
        }
    }

    // MODIFIES: this
    // EFFECTS: display the Bottom
    public void displayBottom(Outfit outfit, JPanel outfitPanel) {
        try {
            BufferedImage myPicture = ImageIO.read(new File(outfit.getBottom().getFilepath()));
            Image scaledImage = myPicture.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
            JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
            picLabel.setAlignmentX(CENTER_ALIGNMENT);
            outfitPanel.add(picLabel);
            JLabel topLabel = new JLabel("Bottom: " + outfit.getBottom().getName());
            topLabel.setAlignmentX(CENTER_ALIGNMENT);
            outfitPanel.add(topLabel);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Bottom image not found");
            errorLabel.setAlignmentX(CENTER_ALIGNMENT);
            outfitPanel.add(errorLabel);
        }
    }

    // MODIFIES: this
    // EFFECTS: display the Shoe
    public void displayShoe(Outfit outfit, JPanel outfitPanel) {
        try {
            BufferedImage myPicture = ImageIO.read(new File(outfit.getShoe().getFilepath()));
            Image scaledImage = myPicture.getScaledInstance(IMAGE_SIZE / 2, IMAGE_SIZE / 2, Image.SCALE_SMOOTH);
            JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
            picLabel.setAlignmentX(CENTER_ALIGNMENT);
            outfitPanel.add(picLabel);
            JLabel topLabel = new JLabel("Shoe: " + outfit.getShoe().getName());
            topLabel.setAlignmentX(CENTER_ALIGNMENT);
            outfitPanel.add(topLabel);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Shoe image not found");
            errorLabel.setAlignmentX(CENTER_ALIGNMENT);
            outfitPanel.add(errorLabel);
        }
    }

    // MODIFIES: this
    // EFFECTS: updates the display of all outfits in a grid
    public void updateOutfitGrid() {
        outfitGrid.removeAll();
        if (!closet.getOutfits().isEmpty()) {
            for (Outfit outfit : closet.getOutfits()) {
                JPanel outfitPanel = new JPanel();
                outfitPanel.setLayout(new BoxLayout(outfitPanel, BoxLayout.Y_AXIS));
                outfitPanel.setAlignmentX(CENTER_ALIGNMENT);

                displayTop(outfit, outfitPanel);
                displayBottom(outfit, outfitPanel);
                displayShoe(outfit, outfitPanel);

                outfitGrid.add(outfitPanel);
            }
        }

        outfitGrid.revalidate();
        outfitGrid.repaint();
    }
}
