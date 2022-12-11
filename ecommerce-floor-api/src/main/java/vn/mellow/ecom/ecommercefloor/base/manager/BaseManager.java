package vn.mellow.ecom.ecommercefloor.base.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import vn.mellow.ecom.ecommercefloor.base.filter.BaseFilter;
import vn.mellow.ecom.ecommercefloor.base.filter.ResultList;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityLog;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityUser;
import vn.mellow.ecom.ecommercefloor.base.mongo.BaseSpringConfiguration;
import vn.mellow.ecom.ecommercefloor.enums.ActivityLogType;
import vn.mellow.ecom.ecommercefloor.utils.FulltextIndex;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseManager {

    protected final MongoClient client;

    protected Logger logger;

    public Logger getLogger() {
        if (null == logger) {
            logger = Logger.getLogger(getClass().getSimpleName());
        }
        return logger;
    }

    public BaseManager(MongoClient mongoClient) {
        this.client = mongoClient;
    }

    protected <TDocument> MongoCollection<TDocument> getCollection(Class<TDocument> var2) {
        String collectionName = var2.getSimpleName();
        return getCollection(var2, collectionName);
    }

    protected <TDocument> MongoCollection<TDocument> getCollection(Class<TDocument> var2, String collectionName) {
        MongoCollection<TDocument> collection = client.getDatabase(BaseSpringConfiguration.MONGO_DB_NAME).getCollection(collectionName, var2);
        // check index
        try {
            Field[] fields = var2.getDeclaredFields();
            List<String> fulltextList = new ArrayList<>();
            for (Field field : fields) {
                Annotation annotation = field.getAnnotation(FulltextIndex.class);
                if (null != annotation) {
                    fulltextList.add(field.getName());
                }
            }
            if (fulltextList.size() > 0) {
                indexFullText(collection, fulltextList);
            }
        } catch (Exception e) {
            getLogger().log(Level.INFO, "Error create fulltext for collection: " + collectionName, e);
        }

        return collection;
    }

    private MongoCollection<Document> sequenceItemCollection;

    public MongoCollection<Document> getSequenceItemCollection() {
        if (null == sequenceItemCollection) {
            sequenceItemCollection = getCollection(Document.class, "DataSequence");
        }
        return sequenceItemCollection;
    }

    private MongoCollection<ActivityLog> activityLogCollection;

    protected MongoCollection<ActivityLog> getActivityLogCollection() {
        if (null == activityLogCollection) {
            activityLogCollection = getCollection(ActivityLog.class);
        }
        return activityLogCollection;
    }

    protected List<ActivityLog> getActivityLog(String requestId) {
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("requestId", requestId));
        return getActivityLogCollection().find(Filters.and(filter)).sort(new BasicDBObject("createdAt", -1)).into(new ArrayList<>());
    }

    protected ActivityLog addActivityLog(ActivityUser user, String description, String requestId, ActivityLogType type, Class requestEntity) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setCreatedAt(new Date());
        activityLog.setUser(user);
        activityLog.setDescription(description);
        activityLog.setRequestId(requestId);
        activityLog.setRequestType(requestEntity.getSimpleName());
        activityLog.setType(type);
        activityLog.setId(generateId());
        getActivityLogCollection().insertOne(activityLog);
        return activityLog;
    }

    protected void appendFilter(Object value, String key, List<Bson> filter) {
        if (null != value) {
            if (String.valueOf(value).trim().length() > 0) {
                filter.add(Filters.eq(key, value));
            }
        }
    }

    protected void betweenFilter(double priceFrom, double priceTo, List<Bson> filter) {
        filter.add(Filters.and(Filters.gte("price", priceFrom), Filters.lte("price", priceTo)));
    }

    long idCounter = System.currentTimeMillis();

    private String getRandomNumber() {
        int number = new Random().nextInt(999);
        String id = number + "";
        if (number < 10) {
            id = "00" + number;
        } else if (number < 100) {
            id = "0" + number;
        }
        return id;
    }

    public String generateId() {
        idCounter++;
        return idCounter + getRandomNumber();
    }

    public String generateCode(Class entity) {
        String entityName = entity.getSimpleName();
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("entityName", entityName));
        Bson filters = Filters.and(filter);
        Document updateUpdateDate = new Document();
        updateUpdateDate.put("updatedAt", new Date());

        Document updateCreateDate = new Document();
        updateCreateDate.put("createdAt", new Date());

        Document updateLevel = new Document();
        updateLevel.put("sequenceId", 1l);

        Document newDocument = new Document();
        newDocument.append("$set", updateUpdateDate);
        newDocument.append("$inc", updateLevel);
        newDocument.append("$setOnInsert", updateCreateDate);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().upsert(true);
        options.returnDocument(ReturnDocument.AFTER);
        Document result = getSequenceItemCollection().findOneAndUpdate(filters, newDocument, options);
        return String.valueOf(result.get("sequenceId"));
    }

    protected ResultList getResultList(MongoCollection collection, List<Bson> filter, int offset, int maxResult) {
        Bson filters = Filters.and(filter);
        long count = collection.countDocuments();
        FindIterable itr = collection.find();
        if (filter.size() > 0) {
            itr = collection.find(filters);
            count = collection.countDocuments(filters);
        }
        ResultList resultList = new ResultList();
        resultList.setResultList(new ArrayList());
        if (count > 0) {

            boolean hasFilterText = false;
            for (Bson bson : filter) {
                String f = bson.toString();
                if (null != f && f.startsWith("Text Filter")) {
                    hasFilterText = true;
                }
            }
            if (maxResult > 0) {
                itr.limit(maxResult);
                itr.skip(offset);
            }
            if (hasFilterText) {
                itr = itr.projection(Projections.metaTextScore("score"))
                        .sort(Sorts.metaTextScore("score"));
            } else {
                itr = itr.sort(Sorts.descending("createdAt"));
            }
            List list = (List) itr.into(new ArrayList<>());
            resultList.setResultList(list);
            resultList.setMaxResult(maxResult);
            resultList.setIndex(offset);
            resultList.setTotal(count);
        }
        return resultList;
    }

    protected List<Bson> getUpdateFilter() {
        List<Bson> filterUpdatedAt = new ArrayList<>();
        filterUpdatedAt.add(Filters.eq("updatedAt", null));
        filterUpdatedAt.add(Filters.lte("updatedAt", new Date(new Date().getTime() - 1000l)));
        List<Bson> filterRequest = new ArrayList<>();
        filterRequest.add(Filters.or(filterUpdatedAt));
        return filterRequest;
    }

    protected void indexFullText(MongoCollection collection, String... fieldIds) {
        if (null != fieldIds && fieldIds.length > 0) {
            Document fullTextIndex = new Document();
            for (String fieldId : fieldIds) {
                fullTextIndex.put(fieldId, "text");
            }
            collection.createIndex(fullTextIndex);
        }
    }

    private void indexFullText(MongoCollection collection, List<String> fieldIds) {
        if (null != fieldIds && fieldIds.size() > 0) {
            Document fullTextIndex = new Document();
            for (String fieldId : fieldIds) {
                fullTextIndex.put(fieldId, "text");
            }
            collection.createIndex(fullTextIndex);
        }
    }

    protected List<Bson> getFilters() {
        List<Bson> filter = new ArrayList<>();

        return filter;
    }

    protected List<Bson> getFilters(BaseFilter baseFilter) {
        return baseFilter.getFilterList();
    }
}
