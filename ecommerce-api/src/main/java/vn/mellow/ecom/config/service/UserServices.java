package vn.mellow.ecom.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.mellow.ecom.model.enums.PasswordStatus;
import vn.mellow.ecom.manager.UserManager;
import vn.mellow.ecom.model.user.KeyPassword;
import vn.mellow.ecom.model.user.User;
import vn.mellow.ecom.model.user.UserFilter;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServices implements UserDetailsService {
    @Autowired
    private UserManager userManager;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserFilter userFilter = new UserFilter();
        userFilter.setEmail(email);
        User user = userManager.filterUser(userFilter).getResultList().get(0);
        List<KeyPassword> keyPasswords = userManager.getAllKeyPassword(user.getId());
        String pwd = "";
        for (KeyPassword key : keyPasswords) {
            if (PasswordStatus.NEW.equals(key.getPasswordStatus())){
                pwd= key.getPassword();
                break;
            }

        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), pwd, new ArrayList<>());
    }
}
