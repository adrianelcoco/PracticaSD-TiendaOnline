package com.TiendaOnline.TiendaOnline;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private List<Order> orders;

    private int countId=10000;

    public OrderService(){
        orders = new ArrayList<>();
    } //Constructor

    public List<Order> getOrders() {
        return this.orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public Order getOrderById(long id){ //Return an order by his Id
        Order order = null;
        for(Order o : this.orders){
            if(o.getId() == id){
                order = o;
                break;
            }
        }
        return order;
    }

    public void updateCountId(){
        countId=countId+10000;
    }

    public int getCountId(){return countId;}
}
