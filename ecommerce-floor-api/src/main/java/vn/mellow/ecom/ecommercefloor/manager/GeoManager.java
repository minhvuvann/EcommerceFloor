package vn.mellow.ecom.ecommercefloor.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import vn.mellow.ecom.ecommercefloor.base.manager.BaseManager;
import vn.mellow.ecom.ecommercefloor.base.model.Geo;
import vn.mellow.ecom.ecommercefloor.enums.CarrierType;
import vn.mellow.ecom.ecommercefloor.enums.GeoType;
import vn.mellow.ecom.ecommercefloor.model.shipment.Carrier;
import vn.mellow.ecom.ecommercefloor.model.shipment.ShippingService;
import vn.mellow.ecom.ecommercefloor.model.user.KeyPassword;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
public class GeoManager extends BaseManager {

    public GeoManager(MongoClient mongoClient) {
        super(mongoClient);
    }

    private MongoCollection<Geo> geoCollection;

    public MongoCollection<Geo> getGeoCollection() {
        if (null == geoCollection) {
            geoCollection = getCollection(Geo.class);
        }
        return geoCollection;
    }

    public Geo createGeo(Geo geo) {
        geo.setCreatedAt(new Date());
        geo.setId(generateId());
        getGeoCollection().insertOne(geo);
        return geo;
    }

    public Geo getGeoType(GeoType type) {
        Geo geo = getGeoCollection().find(Filters.eq("type", type.toString())).first();
        if (null != geo) {
            return geo;
        }
        return null;
    }

    public List<Geo> getGeoParent(GeoType type, String parent_id) {
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("parent_id", parent_id));
        filter.add(Filters.eq("type", type.toString()));
        return getGeoCollection().find(Filters.and(filter)).into(new ArrayList<>());

    }

}
