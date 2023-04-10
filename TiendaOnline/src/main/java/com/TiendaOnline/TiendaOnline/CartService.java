package com.TiendaOnline.TiendaOnline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {


    private Cart cart;

    public CartService(){
        cart= new Cart();
    } //Constructor

    public Cart getCart(){
        return this.cart;
    }
    public List<Product> getProductsInCart() {
        return cart.getProducts();
    } //Returns the products in the cart
    public void addProduct(Product p) { //Add a product to the cart
        cart.getProducts().add(p);
        setEmpty(false);
    }

    public void removeProduct(Product p) {
        cart.getProducts().remove(p);
    } //Remove a product from the cart

    public void removeAllCart() { //Remove all the products from the cart
        cart.getProducts().clear();
        setEmpty(true);
    }
    public boolean getEmpty(){
        return cart.getEmpty();
    } //Return if the cart is empty or not
    public void setEmpty(boolean empty){
        cart.setEmpty(empty);
    }

    public double getTotalPrice(){ //Calculate the total price of the products in the cart
        double totalPrice=0;
        for(Product p: cart.getProducts()){
            totalPrice=totalPrice+p.getPrice();
        }
        totalPrice=Math.round(totalPrice * 100.0) / 100.0;
        return totalPrice;
    }

}


