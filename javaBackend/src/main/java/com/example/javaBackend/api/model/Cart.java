package com.example.javaBackend.api.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<String> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void addItem(String item) {
        this.items.add(item);
    }

    public void clearCart() {
        this.items.clear();
    }
}