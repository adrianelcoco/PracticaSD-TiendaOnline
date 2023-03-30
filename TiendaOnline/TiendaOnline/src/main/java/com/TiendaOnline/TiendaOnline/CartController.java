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

    @PostMapping ("/deleted")
    public String deleteCartProduct(Model model,@RequestParam("productId") long id){
        Product product = productService.getProductById(id);
        cartService.removeProduct(product);
        List<Product> products=cartService.getProductsInCart();
        model.addAttribute("products", products);
        return "cart_template";
    }
}
