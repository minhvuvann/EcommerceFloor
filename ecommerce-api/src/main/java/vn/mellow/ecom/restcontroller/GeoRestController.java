package vn.mellow.ecom.restcontroller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.base.client.GHNClient;
import vn.mellow.ecom.base.controller.BaseController;
import vn.mellow.ecom.base.exception.ClientException;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.model.enums.CountryCode;
import vn.mellow.ecom.model.enums.GeoType;
import vn.mellow.ecom.manager.GeoManager;
import vn.mellow.ecom.model.geo.Geo;
import vn.mellow.ecom.model.shipment.convert.DistrictGHN;
import vn.mellow.ecom.model.shipment.convert.ProvinceGHN;
import vn.mellow.ecom.model.shipment.convert.ResultGHN;
import vn.mellow.ecom.model.shipment.convert.WardGHN;
import vn.mellow.ecom.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/geo/1.0.0/")
public class GeoRestController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(GeoRestController.class);
    @Autowired
    private GeoManager geoManager;
    @Value("${GHN.url}")
    private String areaVN;
    @Value("${GHN.token}")
    private String token;
    @ApiOperation(value = "generate geo")
    @GetMapping("generate-geo")
    public List<Geo> generateGeo() throws ServiceException {
        GHNClient client = new GHNClient(areaVN);
        List<Geo> geos = new ArrayList<Geo>();
        ResultGHN<ProvinceGHN> provinceGHNs = null;
        ResultGHN<DistrictGHN> districtGHNs = null;
        ResultGHN<WardGHN> wardGHNs = null;
        try {
            provinceGHNs = client.getProvinceGHNs(token);

        } catch (ClientException e) {
            e.printStackTrace();
        }
        for (ProvinceGHN province : provinceGHNs.getData()) {
            Geo geoProvince = new Geo();
            geoProvince.setCountryCode(CountryCode.VN);
            geoProvince.setGhn_id(province.getProvinceID());
            geoProvince.setType(GeoType.PROVINCE);
            geoProvince.setName(province.getProvinceName());
            geoProvince.setCode(province.getCode());
            Geo geoProvinceData = geoManager.getGeoGHN_ID(GeoType.PROVINCE, province.getProvinceID());
            if (null == geoProvinceData)
                geoProvince = geoManager.createGeo(geoProvince);
            geos.add(geoProvince);


            try {
                districtGHNs = client.getDistrictGHNs(token, geoProvince.getGhn_id());

            } catch (ClientException e) {
                throw new ServiceException(e.getErrorCode(), e.getErrorMessage());


            }
            if (districtGHNs != null && districtGHNs.getData() != null && !districtGHNs.getData().isEmpty()) {
                for (DistrictGHN district : districtGHNs.getData()) {
                    Geo geoDistrict = new Geo();
                    String isNUmber = String.valueOf(district.getDistrictID());
                    if (!NumberUtils.isNumeric(isNUmber)) {
                        continue;
                    }
                    geoDistrict.setCountryCode(CountryCode.VN);
                    geoDistrict.setGhn_id(district.getDistrictID());
                    geoDistrict.setParent_id(district.getProvinceID());
                    geoDistrict.setCode(district.getCode());
                    geoDistrict.setName(district.getDistrictName());
                    geoDistrict.setType(GeoType.DISTRICT);
                    Geo geoDistrictData = geoManager.getGeoGHN_ID(GeoType.DISTRICT, district.getDistrictID());
                    if (null == geoDistrictData)
                        geoDistrict = geoManager.createGeo(geoDistrict);
                    geos.add(geoDistrict);

                    try {
                        wardGHNs =client.getWardGHNs(token, geoDistrict.getGhn_id());

                    } catch (ClientException e) {
                        continue;
                    }
                    if (null != wardGHNs &&
                            wardGHNs.getData() != null
                            && !wardGHNs.getData().isEmpty()) {
                        for (WardGHN ward : wardGHNs.getData()) {
                            Geo geoWard = new Geo();
                            geoWard.setCountryCode(CountryCode.VN);
                            geoWard.setGhn_id(ward.getWardCode());
                            geoWard.setCode(String.valueOf(ward.getWardCode()));
                            geoWard.setName(ward.getWardName());
                            geoWard.setParent_id(district.getDistrictID());
                            geoWard.setType(GeoType.WARD);
                            Geo geoWardData = geoManager.getGeoGHN_ID(GeoType.WARD, ward.getWardCode());
                            if (null == geoWardData)
                                geoWard = geoManager.createGeo(geoWard);
                            geos.add(geoWard);

                        }
                    }

                }
            }

        }


        return geos;
    }
    @ApiOperation(value = "get list province")
    @PostMapping("/province-list")
    @Caching(
            put = {@CachePut(value = "ecommerce_floor", condition = "#clearCache==@environment.getProperty('app.cache.clearKey')")},
            cacheable = {@Cacheable(value = "ecommerce_floor", condition = "#clearCache!=@environment.getProperty('app.cache.clearKey')")})
    public List<Geo> getProvicnes() {
        return geoManager.getGeoType(GeoType.PROVINCE);
    }
    @ApiOperation(value = "get list district by province id")
    @PostMapping("/district-list")
    @Caching(
            put = {@CachePut(value = "ecommerce_floor", condition = "#clearCache==@environment.getProperty('app.cache.clearKey')")},
            cacheable = {@Cacheable(value = "ecommerce_floor", condition = "#clearCache!=@environment.getProperty('app.cache.clearKey')")})
    public List<Geo> getDistrict(@RequestParam(value = "province-id") int provinceId) {
        return geoManager.getGeoParent(GeoType.DISTRICT,provinceId);
    }
    @ApiOperation(value = "get list ward by district id")
    @PostMapping("/ward-list")
    @Caching(
            put = {@CachePut(value = "ecommerce_floor", condition = "#clearCache==@environment.getProperty('app.cache.clearKey')")},
            cacheable = {@Cacheable(value = "ecommerce_floor", condition = "#clearCache!=@environment.getProperty('app.cache.clearKey')")})
    public List<Geo> getWards(@RequestParam(value = "district-id") int districtId) {
        return geoManager.getGeoParent(GeoType.WARD, districtId);
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
