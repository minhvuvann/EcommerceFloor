package vn.mellow.ecom.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import vn.mellow.ecom.base.manager.BaseManager;
import vn.mellow.ecom.model.enums.GeoType;
import vn.mellow.ecom.model.geo.Geo;

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

    public List<Geo> getGeoType(GeoType type) {
      return getGeoCollection().find(Filters.eq("type", type.toString())).into(new ArrayList<>());
    }

    public Geo getGeoGHN_ID(GeoType type, int ghn_id) {
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("ghn_id", ghn_id));
        filter.add(Filters.eq("type", type.toString()));
        return getGeoCollection().find(Filters.and(filter)).first();

    }

    public List<Geo> getGeoParent(GeoType type, int parent_id) {
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("parent_id", parent_id));
        filter.add(Filters.eq("type", type.toString()));
        return getGeoCollection().find(Filters.and(filter)).into(new ArrayList<>());

    }

}
