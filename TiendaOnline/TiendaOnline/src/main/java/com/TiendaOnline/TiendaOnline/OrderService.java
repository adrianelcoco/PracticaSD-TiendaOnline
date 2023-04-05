package com.TiendaOnline.TiendaOnline;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private List<Order> orders;

    public OrderService(){
        orders = new ArrayList<>();
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public Order getOrderById(long id){
        Order order = null;
        for(Order o : this.orders){
            if(o.getId() == id){
                order = o;
                break;
            }
        }
        return order;
    }
    public void removeOrder(Order order) {
        this.orders.remove(order);
    }


}
