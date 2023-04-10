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
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StockService stockService;

    @PostMapping ("/deleted") //Remove a product from the cart
    public String deleteCartProduct(Model model,@RequestParam("productId") long id){
        Product product = productService.getProductById(id);
        cartService.removeProduct(product);
        stockService.increaseStock(product); //Increase the stock of the removed product by one
        if(cartService.getProductsInCart().size()==0){
            cartService.setEmpty(true);
        }
        List<Product> products=cartService.getProductsInCart();
        model.addAttribute("products", products);
        return "cart_template";
    }
    @GetMapping ("/") //Return to main page
    public String returnMain(Model model){
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }
    @PostMapping ("/order") //Show the page that allows the user to make the order
    public String goOrder(Model model){
        List<Product> products= cartService.getProductsInCart();
        model.addAttribute("products", products);
        model.addAttribute("totalPrice",cartService.getTotalPrice());
        return "order_template";
    }
}
