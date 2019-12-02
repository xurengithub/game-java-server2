package game.user;

import java.util.ArrayList;

public class UserPool {
    private ArrayList<User> users = new ArrayList<User>(100);
    private static UserPool pool;
    private UserPool(){

    }

    public static UserPool getInstance(){
        if(pool == null){
            synchronized(UserPool.class){
                if(pool == null)
                    pool = new UserPool();
            }
        }
        return pool;
    }

    public User getUser(){

        for (User poolUser:users){
            if(!poolUser.isUsed()){
                return poolUser;
            }
        }
        User user = new User();
        users.add(user);
        return user;
    }

}
