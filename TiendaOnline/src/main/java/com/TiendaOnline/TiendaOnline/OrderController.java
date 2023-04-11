package com.TiendaOnline.TiendaOnline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;


    @PostMapping("/newOrder") //Create a new order and show the order detail
    public String orderPlaced(Model model, @RequestParam("productsId") List<String> productsId,
                                @RequestParam("totalPrice") Double totalPrice,
                                @RequestParam("name") String name, @RequestParam("street") String street,
                                @RequestParam("pay-method") String payMethod){
        long id=orderService.getCountId();
        orderService.updateCountId();
        List<Product> products=new ArrayList<>();
        for(int i=0;i< productsId.size();i++){ //Add the products from the cart to a new products arrayList
            products.add(productService.getProductById(Long.parseLong(productsId.get(i))));
        }
        Order order = new Order(products,id,totalPrice,name,street,payMethod);
        orderService.addOrder(order);
        cartService.removeAllCart(); //Remove all products from the cart
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("name",name);
        model.addAttribute("street",street);
        return "orderPlaced_template";
    }

    @PostMapping("/orderDetail") //Show the detail of a specific order
    public String getOrderDetail(Model model, @RequestParam long orderId){
        Order order= orderService.getOrderById(orderId);
        model.addAttribute("order",order);
        model.addAttribute("products",order.getProducts());
        return "orderDetail_template";
    }


}
