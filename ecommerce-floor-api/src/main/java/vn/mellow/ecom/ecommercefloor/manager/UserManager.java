package vn.mellow.ecom.ecommercefloor.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.springframework.stereotype.Repository;
import vn.mellow.ecom.ecommercefloor.base.manager.BaseManager;
import vn.mellow.ecom.ecommercefloor.enums.*;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.user.KeyPassword;
import vn.mellow.ecom.ecommercefloor.model.user.Role;
import vn.mellow.ecom.ecommercefloor.model.user.SocialConnect;
import vn.mellow.ecom.ecommercefloor.model.user.User;

import java.util.Date;

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

    private MongoCollection<SocialConnect> socialCollection;

    public MongoCollection<SocialConnect> getSocialCollection() {
        if (null == socialCollection) {
            socialCollection = getCollection(SocialConnect.class);
        }
        return socialCollection;
    }

    private MongoCollection<Role> roleCollection;

    public MongoCollection<Role> getRoleCollection() {
        if (null == roleCollection) {
            roleCollection = getCollection(Role.class);
        }
        return roleCollection;
    }

    public User createUser(User user, KeyPassword keyPassword, SocialConnect socialConnect, Role role) {
        //create new user


        user.setId(generateId());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(null);
        getUserCollection().insertOne(user);
        //create password for userCollection
        keyPassword.setCreatedAt(new Date());
        keyPassword.setId(generateId());
        keyPassword.setUserId(user.getId());
        getKeyPasswordCollection().insertOne(keyPassword);

        //create role for user
        role.setCreatedAt(new Date());
        role.setUpdatedAt(null);
        role.setUserId(user.getId());
        getRoleCollection().insertOne(role);
        // check that the service user not normally
        if (null != socialConnect && !ServiceType.NORMALLY.equals(user.getServiceType())) {
            socialConnect.setId(generateId());
            socialConnect.setUserId(user.getId());
            socialConnect.setCreatedAt(new Date());
            socialConnect.setServiceStatus(ServiceStatus.IN_PROGRESS_CONNECT);
            socialConnect.setUpdatedAt(null);
            getSocialCollection().insertOne(socialConnect);
        }
        return user;
    }
}
