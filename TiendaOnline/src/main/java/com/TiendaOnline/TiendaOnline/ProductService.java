package com.TiendaOnline.TiendaOnline;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private List<Product> products;
    private int countId=300;
    public ProductService() {       //Constructor

    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public boolean removeProduct(Product product) {
        boolean deleted=this.products.remove(product);
        if(deleted){
            return true;
        }
        else{
            return false;
        }
    }


    public Product getProductById(long id) {  //Returns a product by his Id
        for (Product p : this.products) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void updateCountId(){
        countId=countId+100;
    }
    public long getCountId(){
        return countId;
    }
}