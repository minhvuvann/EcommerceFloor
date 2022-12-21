package vn.mellow.ecom.ecommercefloor.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.enums.UserStatus;
import vn.mellow.ecom.ecommercefloor.manager.ScoreManager;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.user.Score;
import vn.mellow.ecom.ecommercefloor.model.user.User;
import vn.mellow.ecom.ecommercefloor.model.user.UserProfile;

@Component
public class UserProfileController {
    @Autowired
    private UserManager userManager;

    @Autowired
    private ScoreManager scoreManager;

    public UserProfile getUserProfile(String userId) throws ServiceException {
        if (null == userId || userId.length() == 0 || "null".equalsIgnoreCase(userId)) {
            throw new ServiceException("invalid_data", "Vui lòng nhập id user cần tìm", "user id is null");

        }

        User data = userManager.getUser(userId);
        if (null == data || !data.getUserStatus().equals(UserStatus.ACTIVE)) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin user", "Not found data user by id: " + userId);
        }
        UserProfile result = userManager.getUserProfile(userId);

        Score score = scoreManager.getScore(userId);
        if (null != score) {
            result.setScore(score);
        }
        return result;
    }
}
