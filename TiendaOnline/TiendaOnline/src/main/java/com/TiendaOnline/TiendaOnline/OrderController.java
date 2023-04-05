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
    private int countId=0;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;


    @PostMapping("/newOrder")
    public String orderPlaced(Model model, @RequestParam("productsId") List<String> productsId,
                                @RequestParam("totalPrice") Double totalPrice,
                                @RequestParam("name") String name, @RequestParam("street") String street,
                                @RequestParam("pay-method") String payMethod){
        long id=countId+100000;
        countId=countId+100;
        List<Product> products=new ArrayList<>();
        for(int i=0;i< productsId.size();i++){
            products.add(productService.getProductById(Long.parseLong(productsId.get(i))));
        }
        Order order = new Order(products,id,totalPrice,name,street,payMethod);
        orderService.addOrder(order);
        cartService.removeAllCart();
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("name",name);
        model.addAttribute("street",street);
        return "orderPlaced_template";
    }

    @GetMapping("/detail")
    public String getOrderDetail(Model model, @RequestParam("orderId") long orderId){
        Order order= orderService.getOrderById(orderId);
        model.addAttribute("order",order);
        model.addAttribute("products",order.getProducts());
        return "orderDetail_template";
    }


}
