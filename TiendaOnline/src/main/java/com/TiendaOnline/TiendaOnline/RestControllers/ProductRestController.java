package com.TiendaOnline.TiendaOnline.RestControllers;

import com.TiendaOnline.TiendaOnline.*;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductRestController {
    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @Autowired
    StockService stockService;

    @Autowired
    OrderService orderService;

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable long id){
        return productService.getProductById(id);
    }
    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.getProducts();
    }
    @PostMapping("/createProduct")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestParam String name, @RequestParam double price,@RequestParam int amount){
        productService.updateCountId();
        long id= productService.getCountId();
        Product product= new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        productService.addProduct(product);
        stockService.getStocks().add(new Stock(product,amount));
        return product;
    }
    @PutMapping("/modify/{id}")
    public ResponseEntity<Product> modifyProduct(@PathVariable long id,@RequestParam String name, @RequestParam String price){
        Product product=productService.getProductById(id);
        if(product == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            String oldName=product.getName();
            Double oldPrice=product.getPrice();
            Product newProduct;
            if(name.equals("")){
                product.setName(oldName);
                product.setPrice(Double.parseDouble(price));
                newProduct=new Product(id,oldName,Double.parseDouble(price));
            }
            else if(price.equals("")){
                product.setName(name);
                product.setPrice(oldPrice);
                newProduct=new Product(id,name,oldPrice);
            }
            else{
                product.setName(name);
                product.setPrice(Double.parseDouble(price));
                newProduct=new Product(id,name,Double.parseDouble(price));

            }
            stockService.updateProduct(id,newProduct);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable long id){

        if(productService.removeProduct(productService.getProductById(id))){
            if(stockService.deleteProduct(id)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/cart")
    public Cart getCart(){
        return cartService.getCart();
    }

    @PutMapping("/added/{id}")
    public ResponseEntity<Cart> addToCart(@PathVariable long id){
        Product product = productService.getProductById(id);
        if(product == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            cartService.addProduct(product);
            stockService.reduceStock(product);
            if(stockService.getStockById(id).getAmount()==0){
                productService.removeProduct(product);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
    @GetMapping("/stock")
    public List<Stock> getStocks(){
        return stockService.getStocks();
    }

    @GetMapping("/order")
    public List<Order> getOrders(){
        return orderService.getOrders();
    }

}
