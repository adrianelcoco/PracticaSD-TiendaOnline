package com.TiendaOnline.TiendaOnline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {


    private List<Product> cart;

    public CartService(){
        cart=new ArrayList<>();
    }

    public List<Product> getProductsInCart() {
        return cart;
    }
    public void addProduct(Product p) {
        cart.add(p);
    }

    public void removeProduct(Product p) {
        cart.remove(p);
    }

    public void removeAllCart() {
        cart.clear();
    }


}


