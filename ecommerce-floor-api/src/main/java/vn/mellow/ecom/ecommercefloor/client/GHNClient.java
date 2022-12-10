package vn.mellow.ecom.ecommercefloor.client;

import vn.mellow.ecom.ecommercefloor.base.client.BaseClient;
import vn.mellow.ecom.ecommercefloor.base.exception.ClientException;
import vn.mellow.ecom.ecommercefloor.model.shipment.convert.*;

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

    public ResultGHN<ShopGHN> createShop(String token, int district_id,
                                         String ward_code, String name, String phone, String address) throws ClientException {
        return getResponsePackList("v2/shop/register?district_id=" + district_id +
                "&ward_code=" + ward_code + "&name=" + name + "&phone=" + phone +
                "&address=" + address, ShopGHN.class, token);
    }

}
