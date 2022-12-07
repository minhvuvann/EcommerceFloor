package vn.mellow.ecom.ecommercefloor.manager;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vn.mellow.ecom.ecommercefloor.base.manager.BaseManager;
import vn.mellow.ecom.ecommercefloor.enums.CarrierType;
import vn.mellow.ecom.ecommercefloor.model.shipment.Carrier;
import vn.mellow.ecom.ecommercefloor.model.shipment.ShippingService;
import vn.mellow.ecom.ecommercefloor.model.user.KeyPassword;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ShipmentManager extends BaseManager {

    public ShipmentManager(MongoClient mongoClient) {
        super(mongoClient);
    }

    private MongoCollection<Carrier> carrierMongoCollection;

    public MongoCollection<Carrier> getCarrierCollection() {
        if (null == carrierMongoCollection) {
            carrierMongoCollection = getCollection(Carrier.class);
        }
        return carrierMongoCollection;
    }

    private MongoCollection<ShippingService> shippingServiceMongoCollection;

    public MongoCollection<ShippingService> getShippingServiceCollection() {
        if (null == shippingServiceMongoCollection) {
            shippingServiceMongoCollection = getCollection(ShippingService.class);
        }
        return shippingServiceMongoCollection;
    }

    public Carrier createCarrier(Carrier carrier) {
        carrier.setCreatedAt(new Date());
        carrier.setId(generateId());
        getCarrierCollection().insertOne(carrier);
        return carrier;
    }

    public Carrier getCarrierType(CarrierType type) {

       Carrier carrier = getCarrierCollection().find(Filters.eq("type", type.toString())).first();
        if (null != carrier) {
            return carrier;
        }
        return null;
    }

    public ShippingService createShippingService(ShippingService shippingService) {
        shippingService.setCreatedAt(new Date());
        shippingService.setId(generateId());
        getShippingServiceCollection().insertOne(shippingService);
        return shippingService;
    }
    public List<ShippingService> getShippingServices(String carrierId) {
        return getShippingServiceCollection().find(Filters.eq("carrierId", carrierId)).into(new ArrayList<>());
    }

}
