package vn.mellow.ecom.ecommercefloor.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.client.GHNClient;
import vn.mellow.ecom.ecommercefloor.enums.ActiveStatus;
import vn.mellow.ecom.ecommercefloor.enums.CarrierType;
import vn.mellow.ecom.ecommercefloor.manager.ShipmentManager;
import vn.mellow.ecom.ecommercefloor.model.bank.Bank;
import vn.mellow.ecom.ecommercefloor.model.shipment.Carrier;
import vn.mellow.ecom.ecommercefloor.model.shipment.ShippingService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/shipment/1.0.0/")
public class GHNController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(GHNController.class);

    @Value("${GHN.token}")
    private String token;
    @Value("${GHN.url}")
    private String url;
    @Autowired
    private ShipmentManager shipmentManager;


    public void createCarrier() {
        Carrier carrier = shipmentManager.getCarrierType(CarrierType.GHN);
        if (null == carrier) {
            carrier = new Carrier();
            carrier.setName(CarrierType.GHN.getDescription());
            carrier.setType(CarrierType.GHN);
            carrier.setStatus(ActiveStatus.ACTIVE);
            carrier.setDescription(CarrierType.GHN.getDescription());
            shipmentManager.createCarrier(carrier);
        }
    }

    private GHNClient getGHNClient() {
        return new GHNClient(url);
    }
//    @ApiOperation(value = "Get list bank")
//    @GetMapping("shipment/shipping-services")
//    public List<ShippingService> getShippingServices() throws ServiceException {
//
//    }


}
