package com.TiendaOnline.TiendaOnline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private List<Product> products= new ArrayList<>();

    private boolean empty=true;

    public boolean getEmpty(){
        return empty;
    }
    public void setEmpty(boolean e){
        empty=e;
    }
}
