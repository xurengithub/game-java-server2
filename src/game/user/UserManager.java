package game.user;

import commons.events.ApplicationListener;
import commons.events.BaseUserEvent;
import commons.events.LoginEvent;
import core.utils.DecodeUtils;
import game.user.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserManager implements ApplicationListener {

    @Autowired
    private UserDao userDao;
    private Logger logger = LoggerFactory.getLogger(UserManager.class);

    /**
     * 登录 返回true or false
     * @param name
     * @param password
     * @return
     */
    public long login(String name, String password){

        UserEntity user = userDao.findByName(name);
        this.logger.debug("userEntity name {},password {}",user.getUserName(),user.getPassword());
        password = DecodeUtils.md5(password);
        this.logger.debug("user enter password {}",password);
        if(password.equals(user.getPassword())){
            return user.getId();
        }
        return -1;
    }


    /**
     * 注册 返回true or false
     * @param name
     * @param password
     * @return
     */
    public boolean register(String name, String password){
        UserEntity user = userDao.findByName(name);
        if(user != null){
            return false;
        }

        user = new UserEntity();
        user.setUserName(name);
        password = DecodeUtils.md5(password);
        user.setPassword(password);

        int num = userDao.save(user);
        if(num <= 0){
            return false;
        }
        return true;

    }


    @Override
    public long onApplicationEvent(BaseUserEvent event) {
        if(event instanceof LoginEvent){
            return this.login(((LoginEvent) event).userName, ((LoginEvent) event).password);
        }
        return -1;
    }
}
