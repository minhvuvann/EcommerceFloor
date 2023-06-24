package vn.mellow.ecom.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import vn.mellow.ecom.base.filter.ResultList;
import vn.mellow.ecom.base.manager.BaseManager;
import vn.mellow.ecom.model.websocket.Message;
import vn.mellow.ecom.model.websocket.MessageFilter;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 06/05/2023, Thứ Bảy
 **/
@Repository
public class MessageManager extends BaseManager {
    public MessageManager(MongoClient mongoClient) {
        super(mongoClient);
    }

    private MongoCollection<Message> messageMongoCollection;

    public MongoCollection<Message> getMessageMongoCollection() {
        if (null == messageMongoCollection) {
            messageMongoCollection = getCollection(Message.class);
        }
        return messageMongoCollection;
    }

    public Message createMessage(Message mess) {
        mess.setFromAt(new Date());
        mess.setId(generateId());
        getMessageMongoCollection().insertOne(mess);
        return mess;
    }

    public boolean isUserDataChanged() {
        try (var changeStreamCursor = getMessageMongoCollection().watch().iterator()) {
            if (changeStreamCursor.hasNext()) {
                ChangeStreamDocument<Message> changeStreamDocument = changeStreamCursor.next();
                return true;
            }
        }
        return false;
    }

    public List<Message> getMessages(String userId) {
        return getMessageMongoCollection().find(Filters.eq("userId", userId)).into(new ArrayList<>());
    }

    public List<Message> getMessages(String userId, String senderId) {
        Bson filter = Filters.or(
                Filters.and(Filters.eq("userId", userId), Filters.eq("senderId", senderId)),
                Filters.and(Filters.eq("userId", senderId), Filters.eq("senderId", userId))
        );

        return getMessageMongoCollection()
                .find(filter)
                .sort(Sorts.ascending("fromAt")) // Sắp xếp theo fromAt tăng dần
                .into(new ArrayList<>());
    }

    public ResultList<Message> filterMessage(MessageFilter filterData) {
        List<Bson> filter = getFilters(filterData);
        // add filter
        if (filterData.getUserId() != null) {
            appendFilter(filterData.getUserId(), "userId", filter);
        }
        if (filterData.getSenderId() != null) {
            appendFilter(filterData.getSenderId(), "senderId", filter);
        }
        return getResultList(getMessageMongoCollection(), filter, filterData.getOffset(), filterData.getMaxResult());
    }
}
