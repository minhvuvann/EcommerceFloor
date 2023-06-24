package vn.mellow.ecom.restcontroller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.base.controller.BaseController;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.model.enums.GeoType;
import vn.mellow.ecom.manager.GeoManager;
import vn.mellow.ecom.model.geo.Geo;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/geo/1.0.0/")
public class GeoRestController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(GeoRestController.class);
    @Autowired
    private GeoManager geoManager;
    @ApiOperation(value = "get list province")
    @PostMapping("/province-list")
    public List<Geo> getProvicnes() {
        return geoManager.getGeoType(GeoType.PROVINCE);
    }
    @ApiOperation(value = "get list district by province id")
    @PostMapping("/district-list")
    public List<Geo> getDistrict(@RequestParam(value = "province-id") int provinceId) {
        return geoManager.getGeoParent(GeoType.DISTRICT,provinceId);
    }
    @ApiOperation(value = "get list ward by district id")
    @PostMapping("/ward-list")
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
