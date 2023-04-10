package com.TiendaOnline.TiendaOnline.RestControllers;

import com.TiendaOnline.TiendaOnline.*;
import com.TiendaOnline.TiendaOnline.Order;
import com.TiendaOnline.TiendaOnline.OrderService;
import com.TiendaOnline.TiendaOnline.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @PostMapping("/newOrder")
    public ResponseEntity<Order> createOrder(@RequestParam String name, @RequestParam String street,
                                      @RequestParam String payMethod){
        Cart cart= cartService.getCart();
        if(cart.getEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
        else{
            long id=orderService.getCountId();
            orderService.updateCountId();
            List<Product> products=new ArrayList<>();
            for(Product p : cart.getProducts()){
                products.add(p);
            }
            double totalPrice= cartService.getTotalPrice();
            if(payMethod.equals("Tarjeta")  || payMethod.equals("Efectivo")){
                Order order=new Order(products,id,totalPrice,name,street,payMethod);
                orderService.addOrder(order);
                cartService.removeAllCart();
                return new ResponseEntity<>(order,HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }
    }
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable long id){
        Order order= orderService.getOrderById(id);
        return order;
    }
}
