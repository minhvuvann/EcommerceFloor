package vn.mellow.ecom.ecommercefloor.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.ecommercefloor.manager.OrderManager;

@Component
public class OrderCreateController {
    @Autowired
    private OrderManager orderManager;

}
