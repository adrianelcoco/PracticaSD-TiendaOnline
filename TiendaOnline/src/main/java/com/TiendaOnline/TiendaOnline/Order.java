package com.TiendaOnline.TiendaOnline;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private List<Product> products;

    private long id;

    private double totalPrice;

    private String name, street, payMethod;



}
