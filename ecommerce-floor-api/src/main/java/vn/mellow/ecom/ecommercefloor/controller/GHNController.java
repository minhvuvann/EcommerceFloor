package vn.mellow.ecom.ecommercefloor.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ClientException;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.client.GHNClient;
import vn.mellow.ecom.ecommercefloor.enums.ActiveStatus;
import vn.mellow.ecom.ecommercefloor.enums.CarrierType;
import vn.mellow.ecom.ecommercefloor.manager.ShipmentManager;
import vn.mellow.ecom.ecommercefloor.model.shipment.Carrier;
import vn.mellow.ecom.ecommercefloor.model.shipment.ShippingService;
import vn.mellow.ecom.ecommercefloor.model.shipment.convert.ResultPack;
import vn.mellow.ecom.ecommercefloor.model.shipment.convert.ServicePack;

import java.util.ArrayList;
import java.util.HashMap;
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


    public Carrier createCarrier() {
        Carrier carrier = shipmentManager.getCarrierType(CarrierType.GHN);
        if (null == carrier) {
            carrier = new Carrier();
            carrier.setName(CarrierType.GHN.getDescription());
            carrier.setType(CarrierType.GHN);
            carrier.setStatus(ActiveStatus.ACTIVE);
            carrier.setDescription(CarrierType.GHN.getDescription());
            carrier = shipmentManager.createCarrier(carrier);
        }
        return carrier;
    }

    private GHNClient getGHNClient() {
        return new GHNClient(url);
    }

    @ApiOperation(value = "Get list shipping service")
    @GetMapping("shipment/shipping-services")
    public List<ShippingService> getShippingServices(
            @RequestParam("shop-id") String shopId, @RequestParam("from-district") int fromDistrict,
            @RequestParam("to-district") int toDistrict) throws ServiceException {
        Carrier carrier = createCarrier();
        List<ShippingService> shippingServices = shipmentManager.getShippingServices(carrier.getId());
        HashMap<String, ShippingService> serviceHashMap = new HashMap<>();
        if (null != shippingServices && shippingServices.size() != 0) {
            for (ShippingService shippingService : shippingServices) {
                serviceHashMap.put(shippingService.getServiceId(), shippingService);
            }

        }
        ResultPack<ServicePack> resultPack = null;
        try {
            resultPack = getGHNClient().getInfoServicePack(token, shopId, fromDistrict, toDistrict);

        } catch (ClientException e) {
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e.getErrorDetail());
        }
        if (null == resultPack)
            return shippingServices;
        else {
            List<ServicePack> servicePackList = resultPack.getData();
            for (ServicePack servicePack : servicePackList) {

                if (null == serviceHashMap.get(servicePack.getService_id())) {
                    ShippingService shippingService = new ShippingService();
                    shippingService.setName(servicePack.getShort_name());
                    shippingService.setServiceId(servicePack.getService_id());
                    shippingService.setCarrierId(carrier.getId());
                    shippingService.setStatus(ActiveStatus.ACTIVE);
                    shippingService.setServiceCodeId(servicePack.getService_type_id());
                    shipmentManager.createShippingService(shippingService);
                    shippingServices.add(shippingService);
                }

            }


        }
        return shippingServices;

    }


}
