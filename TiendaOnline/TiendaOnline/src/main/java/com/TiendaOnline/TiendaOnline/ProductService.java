package com.TiendaOnline.TiendaOnline;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private List<Product> products;

    public ProductService() {
        this.products = new ArrayList<>();
        products.add(new Product(100L,"Lechuga",0.39));
        products.add(new Product(200L,"Tomate",0.60));
        products.add(new Product(300L,"Platano",1.15));
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }

    public Product getProductById(long id) {
        for (Product p : this.products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }


}