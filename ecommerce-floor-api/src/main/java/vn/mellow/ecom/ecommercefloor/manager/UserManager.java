package vn.mellow.ecom.ecommercefloor.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.filter.ResultList;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityUser;
import vn.mellow.ecom.ecommercefloor.base.manager.BaseManager;
import vn.mellow.ecom.ecommercefloor.enums.*;
import vn.mellow.ecom.ecommercefloor.model.geo.Address;
import vn.mellow.ecom.ecommercefloor.model.input.UpdateInfoUserInput;
import vn.mellow.ecom.ecommercefloor.model.input.UpdateStatusInput;
import vn.mellow.ecom.ecommercefloor.model.order.Order;
import vn.mellow.ecom.ecommercefloor.model.shop.Shop;
import vn.mellow.ecom.ecommercefloor.model.user.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.ReturnDocument.AFTER;

@Repository
public class UserManager extends BaseManager {
    public UserManager(MongoClient mongoClient) {
        super(mongoClient);
    }

    private MongoCollection<User> userCollection;

    public MongoCollection<User> getUserCollection() {
        if (null == userCollection) {
            userCollection = getCollection(User.class);
        }
        return userCollection;
    }

    private MongoCollection<KeyPassword> keyPasswordCollection;

    public MongoCollection<KeyPassword> getKeyPasswordCollection() {
        if (null == keyPasswordCollection) {
            keyPasswordCollection = getCollection(KeyPassword.class);
        }
        return keyPasswordCollection;
    }

    private MongoCollection<Role> roleCollection;

    public MongoCollection<Role> getRoleCollection() {
        if (null == roleCollection) {
            roleCollection = getCollection(Role.class);
        }
        return roleCollection;
    }

    public User createUser(User user, KeyPassword keyPassword, Role role) {
        //create new user
        user.setId(generateId());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(null);
        getUserCollection().insertOne(user);
        //create password for userCollection
        keyPassword.setCreatedAt(new Date());
        keyPassword.setUpdatedAt(null);
        keyPassword.setId(generateId());
        keyPassword.setUserId(user.getId());
        getKeyPasswordCollection().insertOne(keyPassword);

        //create role for user
        role.setId(generateId());
        role.setCreatedAt(new Date());
        role.setUpdatedAt(null);
        role.setUserId(user.getId());
        getRoleCollection().insertOne(role);
        ActivityUser activityUser = new ActivityUser();
        activityUser.setUserId(generateId());
        activityUser.setUserName("Nguyễn Thị Cẩm Tiên");
        activityUser.setEmail("nguyenthicamtien@gmail.com");
        activityUser.setPhone("0909499599");
        addActivityLog(
                activityUser, "Tạo tài khoản ", user.getId(), ActivityLogType.CREATE, User.class);

        return user;
    }

    public List<KeyPassword> getAllKeyPassword(String userId) {
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("userId", userId));
        return getKeyPasswordCollection().find(Filters.and(filter)).into(new ArrayList<>());
    }

    public List<Role> getAllRole(String userId) {
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("userId", userId));
        return getRoleCollection().find(Filters.and(filter)).into(new ArrayList<>());
    }

    public User getUser(String userId) {
        return getUserCollection().find(Filters.eq("_id", userId)).first();
    }

    public User getUserShop(int shopId) {
        return getUserCollection().find(Filters.eq("shop.shopId", shopId)).first();
    }

    public UserProfile getUserProfile(String userId) {
        UserProfile userProfile = new UserProfile();
        User user = getUser(userId);
        List<KeyPassword> keyPasswords = getAllKeyPassword(userId);
        List<Role> roles = getAllRole(userId);
        userProfile.setUser(user);
        userProfile.setKeyPassword(keyPasswords);
        userProfile.setRole(roles);
        return userProfile;

    }

    public User createShop(String userId, Shop shop) {
        Document updateDocument = new Document();
        updateDocument.put("updatedAt", new Date());
        updateDocument.put("shop", shop);
        Document newDocument = new Document();
        newDocument.append("$set", updateDocument);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("_id", userId));
        return getUserCollection().findOneAndUpdate(Filters.and(filters), newDocument, options);
    }

    public Shop getInfoShop(int shopId) {
        User user = getUserCollection().find(Filters.eq("shop.shopId", shopId)).first();
        return user.getShop();
    }

    public User updateInfoUser(String userId, UpdateInfoUserInput updateInfoUser) throws ServiceException {
        Document updateDocument = new Document();
        if (updateInfoUser == null) {
            throw new ServiceException("not_found", "Vui lòng nhập thông tin cần cập nhật của tài khoản", "update info is invalid_data");
        }
        updateDocument.put("updatedAt", new Date());
        if (updateInfoUser.getUsername() != null)
            updateDocument.put("username", updateInfoUser.getUsername());
        if (updateInfoUser.getBirthday() != null) {
            updateDocument.put("birthday", updateInfoUser.getBirthday());
        }
        if (updateInfoUser.getGender()!= null) {
            updateDocument.put("gender", updateInfoUser.getGender());
        }
        if (updateInfoUser.getImageUrl() != null) {
            updateDocument.put("imageUrl", updateInfoUser.getImageUrl());
        }
        if (updateInfoUser.getEmail() != null) {
            updateDocument.put("email", updateInfoUser.getEmail());
        }
        if (updateInfoUser.getTelephone() != null) {
            updateDocument.put("telephone", updateInfoUser.getTelephone());
        }
        if (updateInfoUser.getFullName() != null) {
            updateDocument.put("fullName", updateInfoUser.getFullName());
        }
        Document newDocument = new Document();
        newDocument.append("$set", updateDocument);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("_id", userId));

        return getUserCollection().findOneAndUpdate(Filters.and(filters), newDocument, options);


    }

    public KeyPassword updatePassword(String userId, String password) throws ServiceException {
        Document document = new Document();
        document.put("updatedAt", new Date());
        document.put("password", password);
        Document newDocument = new Document();
        newDocument.append("$set", document);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("passwordStatus", PasswordStatus.NEW.toString()));
        filters.add(Filters.eq("userId", userId));
        return getKeyPasswordCollection().findOneAndUpdate(Filters.and(filters), newDocument, options);
    }

    public User updateAddress(String userId, Address address) {
        Document updateDocument = new Document();
        updateDocument.put("updatedAt", new Date());
        if (address != null)
            updateDocument.put("address", address);

        Document newDocument = new Document();
        newDocument.append("$set", updateDocument);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("_id", userId));

        return getUserCollection().findOneAndUpdate(Filters.and(filters), newDocument, options);
    }

    public User updateUserStatus(String userId, UpdateStatusInput statusBody) throws ServiceException {
        User user = getUser(userId);
        if (null == user)
            throw new ServiceException("not_found", "Không tìm thấy thông tin tài khoản", "user not found");
        //validate status
        validateUpdateStatus(statusBody, user);
        if (null != user) {
            // update order status
            Document document = new Document();
            document.put("updatedAt", new Date());
            document.put("userStatus", statusBody.getStatus().toString());
            Document newDocument = new Document();
            newDocument.append("$set", document);

            List<Bson> bsonList = new ArrayList<>();
            bsonList.add(Filters.eq("_id", userId));
            FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
            user = getUserCollection().findOneAndUpdate(Filters.and(bsonList), newDocument, options);
            // add activity
            String description = "Cập nhật trạng thái" +
                    ": " + statusBody.getStatus();
            if (StringUtils.hasText(statusBody.getNote())) {
                description += ". " + statusBody.getNote();
            }
            // add log update status
            addActivityLog(statusBody.getByUser(), description, userId, ActivityLogType.UPDATE_STATUS, User.class);

            return user;
        }
        return null;
    }

    private void validateUpdateStatus(UpdateStatusInput statusBody, User user) throws ServiceException {
        if (null == statusBody) {
            throw new ServiceException("invalid_data", "Thông tin không hợp lệ", "update Status Body is required");
        }
        if (!UserStatus.isExist(statusBody.getStatus())) {
            throw new ServiceException("status_error", "Trang thái tài khoản không tồn tại", "status not exist");
        }
        if (UserStatus.CANCELLED.equals(user.getUserStatus())) {
            throw new ServiceException("status_updated", "Yêu cầu trạng thái công việc đã bị hủy", "Status is cancelled, can't update status");
        }
    }

    public ResultList<User> filterUser(UserFilter filterData) {
        List<Bson> filter = getFilters(filterData);
        appendFilter(filterData.getFullName(), "fullName", filter);
        if (null != filterData.getGender())
            appendFilter(filterData.getGender().toString(), "gender", filter);
        if (null != filterData.getUserStatus())
            appendFilter(filterData.getUserStatus().toString(), "userStatus", filter);
        if (null != filterData.getServiceType())
            appendFilter(filterData.getServiceType().toString(), "serviceType", filter);
        if (null != filterData.getEmail())
            appendFilter(filterData.getEmail(), "email", filter);
        if (null != filterData.getTelephone())
            appendFilter(filterData.getTelephone(), "telephone", filter);
        if (null != filterData.getUserId())
            appendFilter(filterData.getUserId(), "_id", filter);
        return getResultList(getUserCollection(), filter, filterData.getOffset(), filterData.getMaxResult());
    }

    public Role updateRole(String userId, RoleType type, ActivityUser activityUser) {
        Document document = new Document();
        document.put("updatedAt", new Date());
        document.put("type", type.toString());
        Document newDocument = new Document();
        newDocument.append("$set", document);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("userId", userId));
        Role user = getRoleCollection().findOneAndUpdate(Filters.and(filters), newDocument, options);
        addActivityLog(activityUser,
                "Cập nhật phân quyền " + type.getDescription() + " cho tài khoản :" + userId, userId, ActivityLogType.UPDATE_INFO, Role.class);
        return user;
    }
}
