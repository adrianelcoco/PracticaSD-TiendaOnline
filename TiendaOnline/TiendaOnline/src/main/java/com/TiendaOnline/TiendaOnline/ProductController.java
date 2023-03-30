package com.TiendaOnline.TiendaOnline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String showProducts(Model model) {
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }
    @GetMapping("/createproduct")
    public String goCreateProduct(Model model) {
        return "newproduct_template";
    }
    @PostMapping("/")
    public String createProduct(Model model,@RequestParam("name") String name, @RequestParam("price") String price ) {
        double realPrice= Double.parseDouble(price);
        int size= productService.getProducts().size();
        long newId= (size+1)* 100L;
        Product newProduct= new Product(newId,name,realPrice);
        productService.addProduct(newProduct);
        List<Product> products= productService.getProducts();
        model.addAttribute("products", products);
        return "products_template";
    }


    @GetMapping("/cart")
    public String goCart(Model model){
        List<Product> products= cartService.getProductsInCart();
        model.addAttribute("products", products);
        return "cart_template";
    }

    @PostMapping ("/added")
    public String addToCart(Model model, @RequestParam("productId") long id){
        Product product= productService.getProductById(id);
        cartService.addProduct(product);
        List<Product> products= productService.getProducts();


        model.addAttribute("products", products);
        return "products_template";

    }



}