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
    }
    public List<Product> getProductsInCart() {
        return cart.getProducts();
    }
    public void addProduct(Product p) {
        cart.getProducts().add(p);
    }

    public void removeProduct(Product p) {
        cart.getProducts().remove(p);
    }

    public void removeAllCart() {
        cart.getProducts().clear();
        setEmpty(true);
    }
    public boolean getEmpty(){
        return cart.getEmpty();
    }
    public void setEmpty(boolean empty){
        cart.setEmpty(empty);
    }

    public double getTotalPrice(){
        double totalPrice=0;
        for(Product p: cart.getProducts()){
            totalPrice=totalPrice+p.getPrice();
        }
        totalPrice=Math.round(totalPrice * 100.0) / 100.0;
        return totalPrice;
    }

}


