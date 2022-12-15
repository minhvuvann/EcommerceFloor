package vn.mellow.ecom.ecommercefloor.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/order/1.0.0/")
public class OrderController extends BaseController {

}
