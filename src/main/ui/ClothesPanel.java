package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import model.Closet;
import model.ClothingItem;
import model.Top;
import model.Bottom;
import model.Shoe;
import model.Top.TopCategory;
import model.Bottom.BottomCategory;
import model.Shoe.ShoeCategory;

// clothes panel that displays all of the clothes with the option to add more
public class ClothesPanel extends JPanel {
    private Closet closet;
    private List<ClothingItem> clothes;
    private JPanel clothesGrid;
    private JPanel buttonPanel;
    private JPanel buttonsPanel;
    private static final int GRID_COLUMNS = 3;
    private static final int IMAGE_SIZE = 150; 

    // EFFECTS: creates a clothes panel
    public ClothesPanel(Closet closet) {
        this.closet = closet;
        setLayout(new BorderLayout());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel label = new JLabel("Your Clothes");
        JButton addClothes = new JButton("Add Clothes");
        JButton filterClothes = new JButton("Filter Clothes");
        label.setAlignmentX(CENTER_ALIGNMENT);
        buttonsPanel = new JPanel();
        buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);

        buttonPanel.add(label);
        buttonsPanel.add(addClothes);
        buttonsPanel.add(filterClothes);
        buttonPanel.add(buttonsPanel);

        clothesGrid = new JPanel(new GridLayout(0, GRID_COLUMNS, 10, 10));

        JScrollPane scrollPane = new JScrollPane(clothesGrid, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 31);
        
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addClothes.addActionListener(e -> addClothingItem());
        filterClothes.addActionListener(e -> filterClothingItem());

        clothes = closet.getClothes();

        updateClothingGrid();
        
    }

    // MODIFIES: this
    // EFFECTS: gets user's choices and filters clothing items 
    private void filterClothingItem() {
        String[] types = {"All", "Top", "Bottom", "Shoe"};
        String type = (String) JOptionPane.showInputDialog(this, "Select type of clothing:", "Add Clothing",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
        if (type == null) {
            return;
        }
            
        switch (type) {
            case "All":
                clothes = closet.getClothes();
                break;
            case "Top":
                clothes = closet.filterClothingType("Top");
                break;
            case "Bottom":
                clothes = closet.filterClothingType("Bottom");
                break;
            case "Shoe":
                clothes = closet.filterClothingType("Shoe");
                break;
        }
        updateClothingGrid();

    }

    // MODIFIES: closet
    // EFFECTS: gets user's choices and adds clothing item to closet
    private void addClothingItem() {
        String[] types = {"Top", "Bottom", "Shoe"};
        String type = (String) JOptionPane.showInputDialog(this, "Select type of clothing:", "Add Clothing",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
        if (type == null) {
            return;
        }
        String name = JOptionPane.showInputDialog(this, "Enter clothing name:");
        if (name == null) {
            return;
        }
        String colour = JOptionPane.showInputDialog(this, "Enter color:");
        if (colour == null) {
            return;
        }
        
        String filepath = putFile();
        if (filepath == "") {
            return;
        }
        getClothingSubType(type, name, colour, filepath);
    }

    // EFFECTS: gets file from user, stores it, then returns in local file path
    private String putFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Clothing Image");
        int result = fileChooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) { 
            return ""; 
        }
        File selectedFile = fileChooser.getSelectedFile();
        String fileName = selectedFile.getName();
        String userImageDir = "data/imgs/" + closet.getName();
        String filepath = userImageDir + "/" + fileName;

        try {
            Files.createDirectories(Paths.get(userImageDir));
            Files.copy(selectedFile.toPath(), Paths.get(filepath), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        return filepath;
    }

    // EFFECTS: adds clothing item and updates closet depending on subtype
    private void getClothingSubType(String type, String name, String colour, String filepath) {
        ClothingItem newItem = null;
        switch (type) {                
            case "Top":
                String[] topCategories = {"Hoodie", "Jacket", "Shirt"};
                String topCategory = (String) JOptionPane.showInputDialog(this, "Select category:", "Top Category",
                        JOptionPane.QUESTION_MESSAGE, null, topCategories, topCategories[0]);
                newItem = new Top(name, colour, TopCategory.valueOf(topCategory), filepath);
                break;
            case "Bottom":
                String[] bottomCategori = {"Sweatpants", "Jeans", "Shorts"};
                String bottomCategory = (String) JOptionPane.showInputDialog(this, "Select category:", 
                            "Bottom Category", JOptionPane.QUESTION_MESSAGE, null, bottomCategori, bottomCategori[0]);
                newItem = new Bottom(name, colour, BottomCategory.valueOf(bottomCategory), filepath);
                break;
            case "Shoe":
                String[] shoeCategories = {"Sneakers", "Slides", "Boots"};
                String shoeCategory = (String) JOptionPane.showInputDialog(this, "Select category:", "Shoe Category",
                        JOptionPane.QUESTION_MESSAGE, null, shoeCategories, shoeCategories[0]);
                newItem = new Shoe(name, colour, ShoeCategory.valueOf(shoeCategory), filepath);
                break;
        }
        addClothingToCloset(newItem, type);
    }

    // MODIFIES: this
    // EFFECTS: adds clothing item to closet and updates grid
    private void addClothingToCloset(ClothingItem newItem, String type) {
        closet.addClothing(newItem, type);
        clothes = closet.getClothes();
        updateClothingGrid();
    }

    // MODIFIES: itemPanel
    // EFFECTS: adds clothing label and button
    private void showClothingLabel(ClothingItem item, JPanel itemPanel) {
        JLabel nameLabel = new JLabel(item.getName());
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            String type;
            if (item instanceof Top) {
                type = "Top";
            } else if (item instanceof Bottom) {
                type = "Bottom";
            } else {
                type = "Shoe";
            }
            closet.removeClothing(item, type);
            clothes = closet.getClothes();
            updateClothingGrid();
        });
        itemPanel.add(nameLabel);
        itemPanel.add(removeButton);
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        removeButton.setAlignmentX(CENTER_ALIGNMENT);
        clothesGrid.add(itemPanel);
    }

    // MODIFIES: this
    // EFFECTS: updates clothing grid to display all clothes
    private void updateClothingGrid() {
        clothesGrid.removeAll();
        List<ClothingItem> all = clothes;
        for (ClothingItem item : all) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            
            try {           
                BufferedImage myPicture = ImageIO.read(new File(item.getFilepath()));
                Image scaledImage = myPicture.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
                JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
                picLabel.setAlignmentX(CENTER_ALIGNMENT);
                itemPanel.add(picLabel);
            } catch (Exception e) {
                JLabel errorLabel = new JLabel("Image not found");
                itemPanel.add(errorLabel);
            }
            showClothingLabel(item, itemPanel);
        }
        
        clothesGrid.revalidate();
        clothesGrid.repaint();
    }
}