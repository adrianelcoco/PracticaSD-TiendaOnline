package com.TiendaOnline.TiendaOnline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private Product product;
    private int amount;

}
