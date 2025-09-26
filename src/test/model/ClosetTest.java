package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Bottom.BottomCategory;
import model.Top.TopCategory;
import model.Shoe.ShoeCategory;

public class ClosetTest{
    Closet closet;
    Outfit outfit;
    Outfit duplicate;
    Top top;
    Top top2;
    Bottom bottom;
    Bottom bottom2;
    Shoe shoe;
    Shoe shoe2;
    
    @BeforeEach
    void runBefore() {
        closet = new Closet("testname");

        top = new Top("Old Navy Hoodie", "Blue", TopCategory.Hoodie, "data/top.png");
        bottom = new Bottom("Nike Sweats", "Gray", BottomCategory.Sweatpants, "data/bottom.png");
        shoe = new Shoe("Air Forces", "White", ShoeCategory.Sneakers, "data/shoe.png");

        top2 = new Top("Gap Shirt", "Black", TopCategory.Shirt, "data/top1.png");
        bottom2 = new Bottom("HM Jeans", "Blue", BottomCategory.Jeans, "data/bottom1.png");
        shoe2 = new Shoe("Adidas Slides", "Gray", ShoeCategory.Slides, "data/shoe1.png");

        outfit = new Outfit(top, bottom, shoe);
    }

    @Test
    void constructorTest() {
        List<String> empty = new ArrayList<>();
        assertEquals(empty, closet.getClothes());
        assertEquals(empty, closet.getOutfits());
        assertEquals("testname", closet.getName());
    }

    @Test
    void addClothingTest() {
        closet.addClothing(top,"Top");
        closet.addClothing(bottom, "Bottom");
        closet.addClothing(shoe, "Shoe");

        List<ClothingItem> clothes = closet.getClothes();
        assertEquals(3, clothes.size());
        assertTrue(clothes.contains(top));
        assertTrue(clothes.contains(bottom));
        assertTrue(clothes.contains(shoe));
    }

    @Test
    void removeClothingTest() {
        closet.addClothing(top,"Top");
        closet.addClothing(top2, "Top");

        closet.removeClothing(top, "Top");
        assertEquals(1, closet.getClothingType("Top").size());
        assertFalse(closet.getClothingType("Top").contains(top));
        closet.removeClothing(top2, "Top");
        assertTrue(closet.getClothingType("Top").isEmpty());
        assertFalse(closet.getClothingType("Top").contains(top));
    }

    @Test
    void addOufitTest() {
        duplicate = new Outfit(top, bottom, shoe);
        closet.addOutfit(outfit);

        List<Outfit> outfits = closet.getOutfits();
        assertEquals(1, outfits.size());
        assertTrue(outfits.contains(outfit));

        closet.addOutfit(duplicate);
        outfits = closet.getOutfits();
        assertEquals(1, outfits.size());
        assertFalse(outfits.contains(duplicate));
    }

    @Test
    void getClothingTypeTest() {
        closet.addClothing(top,"Top");
        closet.addClothing(bottom, "Bottom");
        closet.addClothing(shoe, "Shoe");
        closet.addClothing(bottom2, "Bottom");

        List<ClothingItem> tops = closet.getClothingType("Top");
        List<ClothingItem> bottoms = closet.getClothingType("Bottom");
        List<ClothingItem> shoes = closet.getClothingType("Shoe");

        assertEquals(1, tops.size());
        assertEquals(2, bottoms.size());
        assertEquals(1, shoes.size());
        assertTrue(tops.contains(top));
        assertTrue(bottoms.contains(bottom));
        assertTrue(bottoms.contains(bottom2));
        assertTrue(shoes.contains(shoe));
    }

    @Test
    void filterClothingTypeTest() {
        closet.addClothing(top,"Top");
        closet.addClothing(bottom, "Bottom");
        closet.addClothing(shoe, "Shoe");
        closet.addClothing(top2, "Top");

        List<ClothingItem> tops = closet.filterClothingType("Top");
        List<ClothingItem> bottoms = closet.filterClothingType("Bottom");
        List<ClothingItem> shoes = closet.filterClothingType("Shoe");

        assertEquals(2, tops.size());
        assertEquals(1, bottoms.size());
        assertEquals(1, shoes.size());
        assertTrue(tops.contains(top));
        assertTrue(tops.contains(top));
        assertTrue(bottoms.contains(bottom));
        assertTrue(shoes.contains(shoe));
    }
    @Test
    void generateNullRandomOutfitTest() {
        assertNull(closet.generateRandomOutfit());
        closet.addClothing(top,"Top");
        assertNull(closet.generateRandomOutfit());
        closet.addClothing(bottom, "Bottom");
        assertNull(closet.generateRandomOutfit());
        closet.addClothing(shoe, "Shoe");
        assertNotNull(closet.generateRandomOutfit());
    }

    @Test
    void generateRandomOutfitTest() {
        closet.addClothing(top,"Top");
        closet.addClothing(bottom, "Bottom");
        closet.addClothing(shoe, "Shoe");
        closet.addClothing(top2, "Top");
        closet.addClothing(bottom2, "Bottom");
        closet.addClothing(shoe2, "Shoe");

        List<ClothingItem> tops = closet.getClothingType("Top");
        List<ClothingItem> bottoms = closet.getClothingType("Bottom");
        List<ClothingItem> shoes = closet.getClothingType("Shoe");

        Outfit randomOutfit = closet.generateRandomOutfit();
        assertTrue(tops.contains(randomOutfit.getTop()));
        assertTrue(bottoms.contains(randomOutfit.getBottom()));
        assertTrue(shoes.contains(randomOutfit.getShoe()));
    }

    @Test
    void toJsonTest() {
        Closet closet = new Closet("Rudra");
        closet.addClothing(top, "Top");
        closet.addClothing(top2, "Top");
        closet.addClothing(bottom, "Bottom");
        closet.addClothing(shoe, "Shoe");
        closet.addOutfit(outfit);

        JSONObject json = closet.toJson();
        assertTrue(json.has("Rudra"));

        JSONObject closetData = json.getJSONObject("Rudra");

        JSONObject clothingData = closetData.getJSONObject("clothes");
        JSONArray topArray = clothingData.getJSONArray("Top");
        JSONArray bottomArray = clothingData.getJSONArray("Bottom");
        JSONArray shoeArray = clothingData.getJSONArray("Shoe");

        JSONArray outfitArray = closetData.getJSONArray("outfits");

        assertEquals(2, topArray.length());
        assertEquals("Old Navy Hoodie", topArray.getJSONObject(0).getString("name"));
        
        assertEquals(1, bottomArray.length());
        assertEquals("Nike Sweats", bottomArray.getJSONObject(0).getString("name"));
        
        assertEquals(1, shoeArray.length());
        assertEquals("Air Forces", shoeArray.getJSONObject(0).getString("name"));

        assertEquals(1, outfitArray.length());
    }
}
