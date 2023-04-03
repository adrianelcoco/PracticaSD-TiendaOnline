package com.TiendaOnline.TiendaOnline;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    private List<Stock> stocks;

    public StockService(){
        stocks= new ArrayList<>();
        stocks.add(new Stock(new Product(100L,"Lechuga",0.39),90));
        stocks.add(new Stock(new Product(200L,"Tomate",0.60),120));
        stocks.add(new Stock(new Product(300L,"Platano",1.15),75));
    }

    public List<Stock> getStocks(){
        return stocks;
    }

    public void updateProduct(Long id, Product newProduct){
        for (Stock s : this.stocks) {
            if (s.getProduct().getId().equals(id)) {
                s.getProduct().setName(newProduct.getName());
                s.getProduct().setPrice(newProduct.getPrice());
            }
        }
    }
    public void deleteProduct(Long id){
        Stock deleteStock= new Stock();
        for (Stock s : stocks) {
            if (s.getProduct().getId().equals(id)) {
                deleteStock=s;
                stocks.remove(deleteStock);
                break;
            }
        }

    }
}
