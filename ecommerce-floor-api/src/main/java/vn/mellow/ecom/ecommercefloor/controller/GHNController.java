package vn.mellow.ecom.ecommercefloor.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ClientException;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.model.geo.Geo;
import vn.mellow.ecom.ecommercefloor.client.GHNClient;
import vn.mellow.ecom.ecommercefloor.enums.ActiveStatus;
import vn.mellow.ecom.ecommercefloor.enums.CarrierType;
import vn.mellow.ecom.ecommercefloor.enums.GeoType;
import vn.mellow.ecom.ecommercefloor.manager.GeoManager;
import vn.mellow.ecom.ecommercefloor.manager.ShipmentManager;
import vn.mellow.ecom.ecommercefloor.model.shipment.Carrier;
import vn.mellow.ecom.ecommercefloor.model.shipment.ShippingService;
import vn.mellow.ecom.ecommercefloor.model.shipment.convert.*;
import vn.mellow.ecom.ecommercefloor.utils.NumberUtils;

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
    @Autowired
    private GeoManager geoManager;


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
        ResultGHN<ServicePack> resultPack = null;
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

    @ApiOperation(value = "create records geo")
    @PostMapping("shipment/address/create")
    public List<Geo> createGeoGHNs() throws ServiceException {
        List<Geo> geos = new ArrayList<Geo>();
        ResultGHN<ProvinceGHN> provinceGHNs = null;
        ResultGHN<DistrictGHN> districtGHNs = null;
        ResultGHN<WardGHN> wardGHNs = null;
        try {
            provinceGHNs = getGHNClient().getProvinceGHNs(token);

        } catch (ClientException e) {
            e.printStackTrace();
        }
        for (ProvinceGHN province : provinceGHNs.getData()) {
            Geo geoProvince = new Geo();
            geoProvince.setGhn_id(province.getProvinceID());
            geoProvince.setType(GeoType.PROVINCE);
            geoProvince.setName(province.getProvinceName());
            geoProvince.setCode(province.getCode());
            geoProvince = geoManager.createGeo(geoProvince);
            geos.add(geoProvince);


            try {
                districtGHNs = getGHNClient().getDistrictGHNs(token, geoProvince.getGhn_id());

            } catch (ClientException e) {
                throw new ServiceException(e.getErrorCode(),e.getErrorMessage(),e.getErrorDetail());


            }
            if (districtGHNs != null && districtGHNs.getData() != null && 0 != districtGHNs.getData().size()) {
                for (DistrictGHN district : districtGHNs.getData()) {
                    Geo geoDistrict = new Geo();
                    String isNUmber = String.valueOf(district.getDistrictID());
                    if (!NumberUtils.isNumeric(isNUmber)) {
                        continue;
                    }
                    geoDistrict.setGhn_id(district.getDistrictID());
                    geoDistrict.setParent_id(district.getProvinceID());
                    geoDistrict.setCode(district.getCode());
                    geoDistrict.setName(district.getDistrictName());
                    geoDistrict.setType(GeoType.DISTRICT);
                    geoDistrict = geoManager.createGeo(geoDistrict);
                    geos.add(geoDistrict);

                    try {
                            wardGHNs = getGHNClient().getWardGHNs(token, geoDistrict.getGhn_id());

                    } catch (ClientException e) {
                     e.printStackTrace();
                    }
                    if (wardGHNs != null && wardGHNs.getData() != null && 0 != wardGHNs.getData().size()) {
                        for (WardGHN ward : wardGHNs.getData()) {
                            Geo geoWard = new Geo();
                            geoWard.setGhn_id(ward.getWardCode());
                            geoWard.setCode(String.valueOf(ward.getWardCode()));
                            geoWard.setName(ward.getWardName());
                            geoWard.setParent_id(district.getDistrictID());
                            geoWard.setType(GeoType.WARD);
                            geoWard = geoManager.createGeo(geoWard);
                            geos.add(geoWard);

                        }
                    }

                }
            }

        }


        return geos;


    }
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllServiceException(ServiceException e) {
        LOGGER.error("ServiceException error.", e);
        return error(e.getErrorCode(), e.getErrorMessage(), e.getErrorDetail());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return error("internal_server_error", "Có lỗi trong quá trình xử lý", e.getMessage());
    }

}
