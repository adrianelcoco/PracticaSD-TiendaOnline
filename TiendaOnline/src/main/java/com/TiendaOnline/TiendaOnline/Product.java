package com.TiendaOnline.TiendaOnline;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_sequence")
    @SequenceGenerator(name = "my_sequence", sequenceName = "my_sequence_name", allocationSize = 1)
    private Long id;
    private String name;
    private double price;
    private int amountInCart;
    private int amountInOrder;

    private int stock;

    public Product(String name, double price,int stock){
        this.name=name;
        this.price=price;
        this.stock=stock;
        this.amountInCart=0;
    }
    public void increaseStock(int a){
        stock=stock+a;
    }
    public void decreaseStock(int a){
        stock=stock-a;
    }
    public void increaseAmountCart(int a){
        amountInCart=amountInCart+a;
    }
    public void decreaseAmountCart(int a){
        amountInCart=amountInCart-a;
    }
    public boolean stockGreaterThanZero(){
        if(stock>0){
            return true;
        }
        else{
            return false;
        }
    }
    public double pricePerProduct(){
        return Math.round((price * amountInCart) * 100.0) / 100.0;
    }
    public double pricePerProductInOrder(){
        return Math.round((price * amountInOrder) * 100.0) / 100.0;
    }
}
