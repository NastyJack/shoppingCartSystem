package com.example.javaBackend.service;


import com.example.javaBackend.api.controller.ApiResponse;
import com.example.javaBackend.api.model.Cart;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    private Cart cart;

    public CartService(){
        this.cart = new Cart();
    }

    public ResponseEntity<ApiResponse> addItem(String itemName) {
        int price = PRICE_MAP.getOrDefault(itemName, -1); // -1 indicates price not found

        if (price == -1) {
            ApiResponse errorResponse = ApiResponse.error("Invalid item. Item not added to cart.", 400);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        cart.addItem(itemName); // Add the item name to the list
        ApiResponse successResponse = ApiResponse.success("Item " + itemName + " added to cart.", cart.getItems());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    // Price mapping for cart items
    private static final Map<String, Integer> PRICE_MAP = Map.of(
            "Apple", 35,
            "Banana", 20,
            "Melon", 50,
            "Lime", 15
    );




    public ResponseEntity<ApiResponse> clearCart(){
        cart.clearCart();
        ApiResponse successResponse = ApiResponse.success("Cart has been cleared",cart.getItems());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> getCart(){

    if(cart.getItems().isEmpty())
    { ApiResponse errorResponse = ApiResponse.success("Cart is empty.",cart.getItems());
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);}

    ApiResponse successResponse = ApiResponse.success("Items available in cart.",cart.getItems());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);

    }

    public ResponseEntity<ApiResponse> calculatePrice() {

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            ApiResponse successResponse = ApiResponse.success("Cart is empty", 0);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }

        Map<String, Integer> itemCounts = new HashMap<>();
        for (String item : cart.getItems()) {
            itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
        }

        int total = 0;
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            String name = entry.getKey();
            int quantity = entry.getValue();


            // return error if cart contains invalid items
            if (!PRICE_MAP.containsKey(name)) {
                ApiResponse errorResponse = ApiResponse.error("Invalid item '" + name + "' in cart. Cannot perform total.",400);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // get price for each item
            int price = PRICE_MAP.get(name);

            switch (name) {
                case "Melon":
                    // quantity / 2 is price of pairs
                    // quantity % 2 is price for any leftover melons
                    total += (quantity / 2 + quantity % 2) * price;
                    break;

                case "Lime":
                    // quantity / 3 * 2 is sum for a set of 3 limes
                    // quantity % 3 is price for any leftover limes
                    total += ((quantity / 3) * 2 + quantity % 3) * price;
                    break;

                default:
                    total += quantity * price;
                    break;
            }
        }

        ApiResponse successResponse = ApiResponse.success("Total price calculated", total);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    // helpers for for unit testing ==========================================================
    public int calculateTotalRawPrice() {
        ResponseEntity<ApiResponse> response = calculatePrice();

        if (response == null || response.getBody() == null || response.getBody().getData() == null) {
            return -1; // Or handle the error as needed
        }

        Object data = response.getBody().getData();

        if (data instanceof Integer) {
            return (int) data;
        } else {
            return -1; // Data is not an integer
        }
    }


    public Collection<String> viewCart() {
        return cart.getItems();
    }

}
