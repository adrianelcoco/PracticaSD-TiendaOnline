package com.TiendaOnline.TiendaOnline;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    private List<Stock> stocks;

    public StockService(){  //Constructor
        stocks= new ArrayList<>();
        stocks.add(new Stock(new Product(100L,"Lechuga",0.39),90));
        stocks.add(new Stock(new Product(200L,"Tomate",0.60),120));
        stocks.add(new Stock(new Product(300L,"Platano",1.15),75));
    }

    public List<Stock> getStocks(){
        return stocks;
    }


    public Stock getStock(Product product){  //Returns the stock of a product
        Stock stock= null;
        for (Stock s : this.stocks) {
            if (s.getProduct().equals(product)) {
                stock=s;
                break;
            }
        }
        return stock;
    }
    public Stock getStockById(long productId){ //Return the stock by the Id of a product
        Stock stock= null;
        for (Stock s : stocks) {
            if (s.getProduct().getId() == productId) {
                stock=s;
                break;
            }
        }
        return stock;
    }
    public void increaseStock(Product product){ //Increase the stock of a product by 1
        for (Stock s : this.stocks) {
            if (s.getProduct().equals(product)) {
                s.setAmount(s.getAmount()+1);
            }
        }
    }
    public void reduceStock(Product product){  //Reduce the stock of a product by 1

        for (Stock s : this.stocks) {
            if (s.getProduct().equals(product)) {
                s.setAmount(s.getAmount()-1);
            }
        }
    }
    public void updateProduct(Long id, Product newProduct){ //Update the stock of a product when this product is modified
        for (Stock s : this.stocks) {
            if (s.getProduct().getId().equals(id)) {
                s.getProduct().setName(newProduct.getName());
                s.getProduct().setPrice(newProduct.getPrice());
            }
        }
    }
    public boolean deleteProduct(Long id){ //Delete a stock when the product is deleted
        Stock deleteStock;
        boolean deleted=false;
        for (Stock s : stocks) {
            if (s.getProduct().getId().equals(id)) {
                deleteStock=s;
                stocks.remove(deleteStock);
                deleted=true;
                break;
            }
        }
        return deleted;
    }
}
