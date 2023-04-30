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
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;


    @PostMapping("/newOrder") //Create a new order and show the order detail
    public String orderPlaced(Model model, @RequestParam("productsId") List<String> productsId,
                                @RequestParam("totalPrice") Double totalPrice,
                                @RequestParam("name") String name, @RequestParam("street") String street,
                                @RequestParam("pay-method") String payMethod){

        List<Product> products;
        Cart cart=cartRepository.findAll().get(0);
        products=cart.getProducts();
        List<Product> orderProducts=new ArrayList<>();
        List<Integer> amountProducts=new ArrayList<>();
        for(Product p : products){
            amountProducts.add(p.getAmountInCart());
            p.decreaseAmountCart(p.getAmountInCart());
            orderProducts.add(p);
            productRepository.save(p);
        }
        OrderEntity order = new OrderEntity(orderProducts,totalPrice,name,street,payMethod,amountProducts);
        orderRepository.save(order);
        cartRepository.delete(cart);
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("name",name);
        model.addAttribute("street",street);
        return "orderPlaced_template";
    }

    @PostMapping("/orderDetail") //Show the detail of a specific order
    public String getOrderDetail(Model model, @RequestParam long orderId){
        OrderEntity order= orderRepository.findById(orderId).get();
        List<Product> products=order.getProducts();
        List<Integer> amountProducts=order.getAmountProducts();
        int i=0;
        for(Product p: products){
            p.setAmountInOrder(amountProducts.get(i));
            i++;
        }
        model.addAttribute("order",order);
        model.addAttribute("products",order.getProducts());
        model.addAttribute("amountProducts",order.getAmountProducts());
        return "orderDetail_template";
    }

    @PostMapping("/modifyOrder")
    public String modifyOrder(Model model,@RequestParam long orderId){
        OrderEntity order= orderRepository.findById(orderId).get();
        model.addAttribute("order",order);
        return "modifyOrder_template";
    }

    @PostMapping("/modified")
    public String modifiedOrder(Model model,@RequestParam long orderId,@RequestParam String name, @RequestParam String street,@RequestParam String payMethod){
        OrderEntity order= orderRepository.findById(orderId).get();
        if(name.equals("")){
            if(street.equals("")){
                if(!payMethod.equals("")){
                    order.setPayMethod(payMethod);
                }
            }
            else{
                order.setStreet(street);
                if(!payMethod.equals("")){
                    order.setPayMethod(payMethod);
                }
            }
        }
        else{
            order.setName(name);
            if(street.equals("")){
                if(!payMethod.equals("")){
                    order.setPayMethod(payMethod);
                }
            }
            else{
                order.setStreet(street);
                if(!payMethod.equals("")){
                    order.setPayMethod(payMethod);
                }
            }
        }
        orderRepository.save(order);
        List<OrderEntity> orders= orderRepository.findAll();
        model.addAttribute("orders",orders);
        return "orders_template";
    }

    @PostMapping("/deleted")
    public String deleteOrder(Model model, @RequestParam long orderId){
        OrderEntity order= orderRepository.findById(orderId).get();
        orderRepository.delete(order);
        List<OrderEntity> orders= orderRepository.findAll();
        model.addAttribute("orders",orders);
        return "orders_template";
    }
    
}
