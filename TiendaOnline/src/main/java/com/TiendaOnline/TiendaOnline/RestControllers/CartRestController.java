package com.TiendaOnline.TiendaOnline.RestControllers;

import com.TiendaOnline.TiendaOnline.*;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @DeleteMapping("/deleted/{id}")
    public ResponseEntity<Product> deleteCartProduct(@PathVariable long id){
        Product product=productService.getProductById(id);
        if(product == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            cartService.removeProduct(product);
            stockService.increaseStock(product);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
