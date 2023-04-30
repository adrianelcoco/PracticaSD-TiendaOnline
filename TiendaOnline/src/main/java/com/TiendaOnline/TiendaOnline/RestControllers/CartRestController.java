package com.TiendaOnline.TiendaOnline.RestControllers;

import com.TiendaOnline.TiendaOnline.*;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;


    @DeleteMapping("/deleted/{id}")
    public ResponseEntity<Product> deleteCartProduct(@PathVariable long id) {
        Optional<Product> product = productRepository.findById(id);
        Cart cart = cartRepository.findAll().get(0);
        if (!product.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<Product> productsInCart = cart.getProducts();
            if (productsInCart.contains(product.get())) {
                if(product.get().getAmountInCart()>1){
                    product.get().decreaseAmountCart(1);
                    product.get().increaseStock(1);
                }
                else{
                    product.get().decreaseAmountCart(1);
                    product.get().increaseStock(1);
                    cart.getProducts().remove(product.get());
                }
                if(cart.getProducts().isEmpty()){
                    cart.setEmpty(true);
                }
                productRepository.save(product.get());
                cartRepository.save(cart);
                return new ResponseEntity<>(product.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }
    }

    @DeleteMapping("/deleted")
    public ResponseEntity<Cart> deleteCart() {
        Cart cart = cartRepository.findAll().get(0);
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            cartRepository.delete(cart);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/getByProduct/{name}")
    public Cart getCartByProduct(@PathVariable String name) {
        return cartRepository.findByProductName(name);
    }

    @GetMapping("/PriceGreaterThan/{price}")
    public List<Product> getCartByPrice(@PathVariable double price) {
        return cartRepository.findByPriceGreaterThan(price);
    }
    @GetMapping("/getCartOrderByPriceAsc")
    public List<Product> getCartOrderByPriceAsc() {
        return cartRepository.findAllOrderByPriceAsc();
    }
    @GetMapping("/getCartOrderByPriceDesc")
    public List<Product> getCartOrderByPriceDesc() {
        return cartRepository.findAllOrderByPriceDesc();
    }
    @GetMapping("/getCartOrderByAmountInCartAsc")
    public List<Product> getCartOrderByAmountInCartAsc() {
        return cartRepository.findAllOrderByAmountAsc();
    }
    @GetMapping("/getCartOrderByAmountInCartDesc")
    public List<Product> getCartOrderByAmountInCartDesc() {
        return cartRepository.findAllOrderByAmountDesc();
    }
}
