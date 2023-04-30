package com.TiendaOnline.TiendaOnline.RestControllers;

import com.TiendaOnline.TiendaOnline.*;
import com.TiendaOnline.TiendaOnline.OrderEntity;
import com.TiendaOnline.TiendaOnline.OrderService;
import com.TiendaOnline.TiendaOnline.ProductService;
import jakarta.persistence.criteria.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/newOrder")
    public ResponseEntity<OrderEntity> createOrder(@RequestParam String name, @RequestParam String street,
                                                   @RequestParam String payMethod){
        Cart cart= cartRepository.findAll().get(0);
        if(cart.getEmpty() || cart==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            List<Product> orderProducts=new ArrayList<>();
            List<Integer> amountProducts=new ArrayList<>();
            double totalPrice= cart.getTotalPrice();
            for(Product p : cart.getProducts()){
                amountProducts.add(p.getAmountInCart());
                p.decreaseAmountCart(p.getAmountInCart());
                orderProducts.add(p);
                productRepository.save(p);
            }
            if((payMethod.equals("Tarjeta")  || payMethod.equals("Efectivo")) && !name.equals("") && !street.equals("")){
                OrderEntity order=new OrderEntity(orderProducts,totalPrice,name,street,payMethod,amountProducts);
                orderRepository.save(order);
                cartRepository.delete(cart);
                return new ResponseEntity<>(order,HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable long id){
        Optional<OrderEntity> order= orderRepository.findById(id);
        if(order.isPresent()){
            List<Product> products=order.get().getProducts();
            List<Integer> amountProducts=order.get().getAmountProducts();
            int i=0;
            for(Product p: products){
                p.setAmountInOrder(amountProducts.get(i));
                i++;
            }
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<OrderEntity> modifyOrder(@PathVariable long id,@RequestParam String name, @RequestParam String street,@RequestParam String payMethod) {
        OrderEntity order = orderRepository.findById(id).get();
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if (name.equals("")) {
                if (street.equals("")) {
                    if (!payMethod.equals("")) {
                        order.setPayMethod(payMethod);
                    }
                } else {
                    order.setStreet(street);
                    if (!payMethod.equals("")) {
                        order.setPayMethod(payMethod);
                    }
                }
            } else {
                order.setName(name);
                if (street.equals("")) {
                    if (!payMethod.equals("")) {
                        order.setPayMethod(payMethod);
                    }
                } else {
                    order.setStreet(street);
                    if (!payMethod.equals("")) {
                        order.setPayMethod(payMethod);
                    }
                }
            }
            orderRepository.save(order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        }

    }
    @DeleteMapping("/deleted/{id}")
    public ResponseEntity<OrderEntity> deleteOrder(@PathVariable long id){
        OrderEntity order=orderRepository.findById(id).get();
        if(order==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            orderRepository.delete(order);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/{orderId}/productsByPrice")
    public List<Product> getProductsByOrderIdAndPriceRange(@PathVariable Long orderId,
                                                           @RequestParam("minPrice") Double minPrice,
                                                           @RequestParam("maxPrice") Double maxPrice) {
        return orderRepository.findProductsByOrderIdAndPriceRange(orderId, minPrice, maxPrice);
    }

    @GetMapping("/getOrderByName")
    public List<OrderEntity> getByName(@RequestParam String name) {
        return orderRepository.findByName(name);
    }

    @GetMapping("/getOrderById/{orderId}")
    public List<OrderEntity> getProductsByOrderId(@PathVariable Long orderId) {
        return orderRepository.findProductsByOrderId(orderId);
    }

    @GetMapping("/getByProduct/{name}")
    public List<OrderEntity> getByProduct(@PathVariable String name) {
        return orderRepository.findByProductName(name);
    }

    @GetMapping("/orders/orderBy/nameAsc")
    public List<OrderEntity> getAllOrdersOrderByNameAsc() {
        return orderRepository.OrderByNameAsc();
    }

    @GetMapping("/orders/orderBy/nameDesc")
    public List<OrderEntity> getAllOrdersOrderByNameDesc() {
        return orderRepository.OrderByNameDesc();
    }

    @GetMapping("/orders/orderBy/totalPriceAsc")
    public List<OrderEntity> getAllOrdersOrderByTotalPriceAsc() {
        return orderRepository.OrderByTotalPriceAsc();
    }

    @GetMapping("/orders/orderBy/totalPriceDesc")
    public List<OrderEntity> getAllOrdersOrderByTotalPriceDesc() {
        return orderRepository.OrderByTotalPriceDesc();
    }

    @GetMapping("/orders/orderBy/streetAsc")
    public List<OrderEntity> getAllOrdersOrderByStreetAsc() {
        return orderRepository.OrderByStreetAsc();
    }

    @GetMapping("/orders/orderBy/streetDesc")
    public List<OrderEntity> getAllOrdersOrderByStreetDesc() {
        return orderRepository.OrderByStreetDesc();
    }

    @GetMapping("/orders/orderBy/payMethodAsc")
    public List<OrderEntity> getAllOrdersOrderByPayMethodAsc() {
        return orderRepository.OrderByPayMethodAsc();
    }

    @GetMapping("/orders/orderBy/payMethodDesc")
    public List<OrderEntity> getAllOrdersOrderByPayMethodDesc() {
        return orderRepository.OrderByPayMethodDesc();
    }
    @GetMapping("/order/payMethod/{payMethod}")
    public List<OrderEntity> getOrderByPayMethod(@PathVariable String payMethod) {
       return orderRepository.findByPayMethod(payMethod);
    }
    @GetMapping("/order/street/{street}")
    public ResponseEntity<OrderEntity> getOrderByStreet(@PathVariable String street) {
        OrderEntity order = orderRepository.findByStreet(street);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/order/totalPrice/{totalPrice}")
    public ResponseEntity<OrderEntity> getOrderByTotalPrice(@PathVariable double totalPrice) {
        OrderEntity order = orderRepository.findByTotalPrice(totalPrice);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
