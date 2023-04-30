package com.TiendaOnline.TiendaOnline;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    private List<Product> products;
    @Column(name = "`empty`")
    private boolean empty;
    public Cart (boolean empty){
        products= new ArrayList<>();
        this.empty=empty;
    }

    public boolean getEmpty(){
        return empty;
    }
    public void setEmpty(boolean e){
        empty=e;
    }

    public Product getProductById(long id){
        Product product=null;
        for(Product p: this.products){
            if(p.getId().equals(id)){
                product=p;
                break;
            }
        }
        return product;
    }
    public double getTotalPrice(){
        double totalPrice=0;
        for(Product p: this.products){
            totalPrice=totalPrice+p.pricePerProduct();
        }
        return Math.round(totalPrice * 100.0) / 100.0;
    }

}
