package com.example.javaBackend.api.controller;

import com.example.javaBackend.api.dto.ItemRequest;
import com.example.javaBackend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping("/cart/view")
    public ResponseEntity<ApiResponse> getCart() {
        return this.cartService.getCart();
    }

    @PostMapping("/cart/addItem")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody ItemRequest request) {
        return this.cartService.addItem(request.getName());
    }

    @DeleteMapping("/cart/clear")
    public ResponseEntity<ApiResponse> clearCart(){
        return this.cartService.clearCart();
    }

    @PostMapping("/cart/calculatePrice")
    public ResponseEntity<ApiResponse> calculatePrice(){
        return this.cartService.calculatePrice();
    }

}
