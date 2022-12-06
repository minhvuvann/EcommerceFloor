package vn.mellow.ecom.ecommercefloor.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import vn.mellow.ecom.ecommercefloor.base.filter.ResultList;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityUser;
import vn.mellow.ecom.ecommercefloor.base.manager.BaseManager;
import vn.mellow.ecom.ecommercefloor.enums.*;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.user.*;
import vn.mellow.ecom.ecommercefloor.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                activityUser, "Tạo đơn Order Fulfillment",user.getId(), ActivityLogType.CREATE, User.class);

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

    public ResultList<User> filterUser(UserFilter filterData) {
        List<Bson> filter = getFilters(filterData);
        appendFilter(filterData.getFullName(), "fullName", filter);
        if (null != filterData.getGender())
            appendFilter(filterData.getGender().toString(), "gender", filter);
        if (null != filterData.getUserStatus())
            appendFilter(filterData.getUserStatus().toString(), "userStatus", filter);
        if (null != filterData.getServiceType())
            appendFilter(filterData.getServiceType().toString(), "serviceType", filter);
        if (null!=filterData.getEmail())
            appendFilter(filterData.getEmail(),"email",filter);
        if (null!=filterData.getTelephone())
            appendFilter(filterData.getTelephone(),"telephone",filter);
        if (null != filterData.getUserId())
            appendFilter(filterData.getUserId(), "_id", filter);
        return getResultList(getUserCollection(), filter, filterData.getOffset(), filterData.getMaxResult());
    }
}
