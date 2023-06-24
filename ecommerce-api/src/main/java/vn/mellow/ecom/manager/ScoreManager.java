package vn.mellow.ecom.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import vn.mellow.ecom.base.manager.BaseManager;
import vn.mellow.ecom.model.enums.ScoreType;
import vn.mellow.ecom.model.user.Score;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.ReturnDocument.AFTER;

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

    public Score createScore(Score score) {
        score.setId(generateId());
        score.setCreatedAt(new Date());
        getScoreMongoCollection().insertOne(score);
        return score;
    }

    public Score getScore(String id) {
        return getScoreMongoCollection().find(Filters.eq("_id", id)).first();

    }

    public Score updateScore(String userId, long count) {
        Document document = new Document();
        document.put("updatedAt", new Date());
        Document adjustmentScore = new Document();

        adjustmentScore.put("score", count);
        Document newDocument = new Document();
        newDocument.append("$set", document);
        newDocument.append("$inc", adjustmentScore);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("userId", userId));
        return getScoreMongoCollection().findOneAndUpdate(Filters.and(filters), newDocument, options);
    }

    public Score updateScoreType(String userId, ScoreType type) {
        Document document = new Document();
        document.put("updatedAt", new Date());
        document.put("type", type.toString());
        Document newDocument = new Document();
        newDocument.append("$set", document);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("userId", userId));
        return getScoreMongoCollection().findOneAndUpdate(Filters.and(filters), newDocument, options);
    }
}
