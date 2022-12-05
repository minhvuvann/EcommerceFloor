package vn.mellow.ecom.ecommercefloor.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.user.UserProfile;

@Component
public class UserProfileController {
    @Autowired
    private UserManager userManager;

    public UserProfile getUserProfile(String userId) throws ServiceException {
        if (null == userId || userId.length() == 0 || "null".equalsIgnoreCase(userId)) {
            throw new ServiceException("invalid_data", "Vui lòng nhập id user cần tìm", "user id is null");

        }
        if (null == userManager.getUser(userId)) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin user", "user is null");

        }
        return userManager.getUserProfile(userId);
    }
}
