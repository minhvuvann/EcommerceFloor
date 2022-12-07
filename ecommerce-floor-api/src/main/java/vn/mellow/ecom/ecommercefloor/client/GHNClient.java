package vn.mellow.ecom.ecommercefloor.client;

import vn.mellow.ecom.ecommercefloor.base.client.BaseClient;
import vn.mellow.ecom.ecommercefloor.base.exception.ClientException;
import vn.mellow.ecom.ecommercefloor.model.shipment.convert.ResultPack;
import vn.mellow.ecom.ecommercefloor.model.shipment.convert.ServicePack;

public class GHNClient extends BaseClient {
    public GHNClient(String service) {
        super(service);
    }

    public ResultPack<ServicePack> getInfoServicePack(String token, String shopId, int fromDistrict, int toDistrict) throws ClientException {
        return getResponsePackList(token,
                "/available-services?shop_id="+shopId+"&from_district="+fromDistrict+"&to_district="+toDistrict,ServicePack.class);

    }
}
