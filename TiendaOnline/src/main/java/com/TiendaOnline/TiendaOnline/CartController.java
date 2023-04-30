package com.TiendaOnline.TiendaOnline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;


    @PostMapping ("/deleted") //Remove a product from the cart
    public String deleteCartProduct(Model model,@RequestParam("productId") long id){
        Product product = productRepository.findById(id).get();
        Cart cart=cartRepository.findAll().get(0);
        if(product.getAmountInCart()>1){
            product.decreaseAmountCart(1);
            product.increaseStock(1);
        }
        else{
            product.decreaseAmountCart(1);
            product.increaseStock(1);
            cart.getProducts().remove(product);
        }
        if(cart.getProducts().isEmpty()){
            cart.setEmpty(true);
        }
        productRepository.save(product);
        cartRepository.save(cart);
        List<Product> products=cart.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("empty",cart.getEmpty());
        return "cart_template";
    }
    @PostMapping("/deletedCart")
    public String deleteCart(Model model){
        Cart cart=cartRepository.findAll().get(0);
        List<Product> productsInCart=cart.getProducts();
        for(Product p : productsInCart){
            p.increaseStock(p.getAmountInCart());
            p.decreaseAmountCart(p.getAmountInCart());
            productRepository.save(p);
        }
        cartRepository.delete(cart);
        List<Product> products= productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("cart",cartRepository.findAll());
        return "products_template";
    }
    @GetMapping ("/") //Return to main page
    public String returnMain(Model model){
        List<Product> products= productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("cart",cartRepository.findAll());
        return "products_template";
    }
    @PostMapping ("/order") //Show the page that allows the user to make the order
    public String goOrder(Model model){
        Cart cart=cartRepository.findAll().get(0);
        List<Product> products= cart.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("totalPrice",cart.getTotalPrice());
        return "order_template";
    }

}
