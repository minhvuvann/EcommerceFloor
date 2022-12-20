package vn.mellow.ecom.ecommercefloor.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import vn.mellow.ecom.ecommercefloor.base.manager.BaseManager;
import vn.mellow.ecom.ecommercefloor.enums.ScoreType;
import vn.mellow.ecom.ecommercefloor.model.geo.Geo;
import vn.mellow.ecom.ecommercefloor.model.user.Score;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ScoreManager extends BaseManager {
    public ScoreManager(MongoClient mongoClient) {
        super(mongoClient);
    }


    private MongoCollection<Score> scoreMongoCollection;

    public MongoCollection<Score> getScoreMongoCollection() {
        if (null == scoreMongoCollection) {
            scoreMongoCollection = getCollection(Score.class);
        }
        return scoreMongoCollection;
    }
    public Score createScore(Score score){
        score.setId(generateId());
        score.setCreatedAt(new Date());
        getScoreMongoCollection().insertOne(score);
        return score;
    }
    public Score getScore(String id){
        return getScoreMongoCollection().find(Filters.eq("_id",id)).first();

    }
    public Score getScoreCustomer(String customerId){
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("userId",customerId));
        filter.add(Filters.eq("type", ScoreType.BUY));
        return getScoreMongoCollection().find(Filters.and(filter)).first();
    }
    public Score getScoreShop(String shopId){
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("userId",shopId));
        filter.add(Filters.eq("type", ScoreType.SELL));
        return getScoreMongoCollection().find(Filters.and(filter)).first();
    }

}
