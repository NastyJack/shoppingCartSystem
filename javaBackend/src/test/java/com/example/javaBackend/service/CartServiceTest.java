package com.example.javaBackend.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    public void setup() {
        cartService = new CartService(); // assuming no-arg constructor for test
    }

    @Test
    public void testAddSingleItem() {
        cartService.addItem("Apple");
        assertEquals(1, cartService.viewCart().size());
        assertTrue(cartService.viewCart().contains("Apple"));
    }

    @Test
    public void testTotalPriceForApples() {
        cartService.addItem("Apple");
        cartService.addItem("Apple");
        int total = cartService.calculateTotalRawPrice();
        assertEquals(70, total); // 2 * 35
    }

    @Test
    public void testBOGOOfferForMelons() {
        cartService.addItem("Melon");
        cartService.addItem("Melon");
        cartService.addItem("Melon"); // 3 melons
        int total = cartService.calculateTotalRawPrice();
        assertEquals(100, total); // 2 charged out of 3
    }

    @Test
    public void testThreeForTwoLimes() {
        cartService.addItem("Lime");
        cartService.addItem("Lime");
        cartService.addItem("Lime");
        cartService.addItem("Lime");
        int total = cartService.calculateTotalRawPrice();
        assertEquals(45, total); // 3 for 2 = 30 + 1 = 15 => 45
    }

    @Test
    public void testInvalidItemDoesNotAffectCart() {
        cartService.addItem("Pineapple");
        assertEquals(0, cartService.viewCart().size());
    }

    @Test
    public void testClearCart() {
        cartService.addItem("Apple");
        cartService.clearCart();
        assertEquals(0, cartService.viewCart().size());
    }

    @Test
    public void testViewCart() {
        // Add some items to the cart
        cartService.addItem("Apple");
        cartService.addItem("Banana");
        cartService.addItem("Lime");
        cartService.addItem("Melon");

        // Get the items in the cart
        List<String> cartItems = (List<String>) cartService.viewCart();

        // Verify the items in the cart
        assertEquals(4, cartItems.size());  // Expecting 4 items in the cart
        assertTrue(cartItems.contains("Apple"));  // Check if Apple is in the cart
        assertTrue(cartItems.contains("Banana"));  // Check if Banana is in the cart
        assertTrue(cartItems.contains("Lime"));  // Check if Lime is in the cart
        assertTrue(cartItems.contains("Melon"));  // Check if Melon is in the cart
    }


    // test multiple large cart combinations ===========================================================
    @Test
    public void testMultipleApplesAndBananas() {
        cartService.addItem("Apple");   // 35p
        cartService.addItem("Apple");   // 35p
        cartService.addItem("Banana");  // 20p
        cartService.addItem("Banana");  // 20p
        cartService.addItem("Banana");  // 20p

        int total = cartService.calculateTotalRawPrice();

        // Expected:
        // Apples: 35 + 35 = 70
        // Bananas: 20 + 20 + 20 = 60
        assertEquals(130, total);
    }

    @Test
    public void testBuyOneGetOneFreeMelons() {
        cartService.addItem("Melon");  // 50p
        cartService.addItem("Melon");  // 50p

        int total = cartService.calculateTotalRawPrice();

        // Expected:
        // 2 Melons: 50p (BOGO - 1 for free)
        assertEquals(50, total); // 1 melon paid
    }

    @Test
    public void testThreeForTwoLimesExtended() {
        cartService.addItem("Lime");   // 15p
        cartService.addItem("Lime");   // 15p
        cartService.addItem("Lime");   // 15p
        cartService.addItem("Lime");   // 15p
        cartService.addItem("Lime");   // 15p

        int total = cartService.calculateTotalRawPrice();

        // Expected:
        // 5 Limes: 2 for the price of 1 set (3 limes) and 2 more at 15p
        // 2 sets of 3 Limes (2 paid) and 2 more individual
        assertEquals(60, total); // 2 * (30) + 2 * 15 = 60
    }

    @Test
    public void testSingleLimeWithOffer() {
        cartService.addItem("Lime");   // 15p

        int total = cartService.calculateTotalRawPrice();

        // Expected:
        // 1 Lime = 15p (no offer applied)
        assertEquals(15, total);
    }


    @Test
    public void testAllOffersWithMultipleItems() {
        // Adding items with different pricing logic
        cartService.addItem("Apple");  // 35p
        cartService.addItem("Apple");  // 35p
        cartService.addItem("Banana"); // 20p
        cartService.addItem("Banana"); // 20p
        cartService.addItem("Melon"); // 50p
        cartService.addItem("Melon"); // 50p
        cartService.addItem("Lime");  // 15p
        cartService.addItem("Lime");  // 15p
        cartService.addItem("Lime");  // 15p

        int total = cartService.calculateTotalRawPrice();

        // Expected:
        // Apples: 35 + 35 = 70
        // Bananas: 20 + 20 = 40
        // Melons (BOGO): 50
        // Limes (3 for 2): 30
        assertEquals(190, total); // 70 + 40 + 50 + 30 = 190
    }





}
