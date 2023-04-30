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
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToMany
    private List<Product> products;

    private double totalPrice;

    private String name, street, payMethod;
    @ElementCollection
    private List<Integer> amountProducts;


    public OrderEntity(List<Product> orderProducts, Double totalPrice, String name, String street, String payMethod, List<Integer> amountProducts) {
        products=orderProducts;
        this.totalPrice=totalPrice;
        this.name=name;
        this.street=street;
        this.payMethod=payMethod;
        this.amountProducts=amountProducts;
    }

}
