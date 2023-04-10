package com.TiendaOnline.TiendaOnline.RestControllers;

import com.TiendaOnline.TiendaOnline.*;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockRestController {
    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @PutMapping("/modify/{id}")
    private ResponseEntity<Stock> updateStock(@PathVariable long id, @RequestParam int newStock, @RequestParam String mode){
            Stock stock = stockService.getStockById(id);
            if(stock==null){
                return new ResponseEntity<>(stock,HttpStatus.NOT_FOUND);
            }
            else{
                if(mode.equals("aumentar")){
                    newStock = newStock + stock.getAmount();
                    if (stock.getAmount() <= 0) {
                        productService.addProduct(stock.getProduct());
                    }
                    stock.setAmount(newStock);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else if (mode.equals("reducir")) {
                    newStock = stock.getAmount() - newStock;
                    if (newStock <= 0) {
                        productService.removeProduct(stock.getProduct());
                    }
                    stock.setAmount(newStock);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }

        }
    }

