package vn.mellow.ecom.base.client;

import vn.mellow.ecom.base.exception.ClientException;
import vn.mellow.ecom.model.input.FeeShippingGHNDTO;
import vn.mellow.ecom.model.input.ShopGHNDTO;
import vn.mellow.ecom.model.shipment.convert.*;

public class GHNClient extends BaseClient {
    public GHNClient(String service) {
        super(service);
    }

    public ResultGHN<ServicePack> getInfoServicePack(String token, String shopId, int fromDistrict, int toDistrict) throws ClientException {
        return getResponsePackList(
                "v2/shipping-order/available-services?shop_id=" + shopId + "&from_district=" + fromDistrict + "&to_district=" + toDistrict
                , ServicePack.class, token);

    }

    public ResultGHN<ProvinceGHN> getProvinceGHNs(String token) throws ClientException {
        return getResponsePackList("master-data/province", ProvinceGHN.class, token);
    }

    public ResultGHN<DistrictGHN> getDistrictGHNs(String token, int province_id) throws ClientException {
        return getResponsePackList("master-data/district?province_id=" + province_id, DistrictGHN.class, token);
    }

    public ResultGHN<WardGHN> getWardGHNs(String token, int district_id) throws ClientException {
        return getResponsePackList("master-data/ward?district_id=" + district_id, WardGHN.class, token);
    }

    public ResultObjectGHN createShop(String token, ShopGHNDTO shopGHNDTO) throws ClientException {
        return post("v2/shop/register", shopGHNDTO, ResultObjectGHN.class, token);
    }

    public ResultObjectGHN getFeeShippingService(String token, String shopId, FeeShippingGHNDTO feeShippingGHNDTO) throws ClientException {
        shopId = shopId.replaceFirst("^3", "4");
        return post("v2/shipping-order/fee", feeShippingGHNDTO, ResultObjectGHN.class, token, shopId);
    }
}
