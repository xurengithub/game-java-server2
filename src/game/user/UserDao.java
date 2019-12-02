package game.user;

import core.DB.Template.AbstractDao;
import game.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserDao extends AbstractDao<UserEntity> {

    public static String table = "user";

    /**
     * 通过id查找查找UserEntity  null
     * @param id
     * @return
     */
    public UserEntity findById(long id){
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id",id);
        ArrayList<UserEntity> list = super.find(table, map);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 通过用户名查找查找UserEntity null
     * @param userName
     * @return
     */
    public UserEntity findByName(String userName){
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("userName",userName);
        ArrayList<UserEntity> list = super.find(table, map);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 更新UserEntity
     * @param userEntity
     * @return
     */
    public int update(UserEntity userEntity){
        Map<Object,Object> setMap = new HashMap<Object, Object>();
        setMap.put("userName",userEntity.getUserName());
        setMap.put("password",userEntity.getPassword());

        Map<Object,Object> whereMap = new HashMap<Object, Object>();
        whereMap.put("id",userEntity.getId());
        return super.update(table,setMap,whereMap);
    }

    /**
     * 保存UserEntity
     * @param userEntity
     * @return
     */
    public int save(UserEntity userEntity){
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id",userEntity.getId());
        map.put("userName",userEntity.getUserName());
        map.put("password",userEntity.getPassword());
        return super.insert(table, map);
    }

    /**
     * 通过id删除用户账号信息
     * @param id
     * @return
     */
    public int deleteById(long id){
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id",id);
        return super.delete(table, map);
    }

    /**
     * 通过用户名删除账号信息
     * @param userName
     * @return
     */
    public int deleteByName(String userName){
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("userName",userName);
        return super.delete(table, map);
    }

    /**
     * 实现模板类，包装一ResultSet数据 返回UserEntity
     * @param rs
     * @return
     * @throws SQLException
     */
    @Override
    protected UserEntity rowMapper(ResultSet rs) throws SQLException {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(rs.getLong("id"));
        userEntity.setUserName(rs.getString("userName"));
        userEntity.setPassword(rs.getString("password"));
        userEntity.setCtime(rs.getTimestamp("ctime"));
        userEntity.setMtime(rs.getTimestamp("mtime"));
        return userEntity;
    }
}
