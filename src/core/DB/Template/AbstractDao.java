package core.DB.Template;

import core.DB.DBConnectionPool;
import core.utils.ArrayUtils;
import core.utils.DBUtils;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractDao<T> {
    private Logger logger = LoggerFactory.getLogger(AbstractDao.class);
    /**
     * 通过preSql和值数组查询对象
     * @param sql
     * @param params
     * @return
     */
    protected ArrayList<T> find(String sql, Object[] params){
//        this.logger.debug(sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<T> objList = new ArrayList<T>();

        DBConnectionPool pool = null;

        try {
             pool = DBConnectionPool.getInstance();
             conn = pool.getConnection();
             ps = conn.prepareStatement(sql);
             for(int i = 0; i<params.length; i++){
                ps.setObject(i+1, params[i]);
             }
             rs = ps.executeQuery();
             while(rs.next()){
                 objList.add(rowMapper(rs));
             }
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
//                rs.close();
//                ps.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return objList;
    }

    /**
     * 通过表名称和条件map查询对象
     * @param tableName
     * @param whereMap
     * @return
     */
    protected ArrayList<T> find(String tableName, Map<Object,Object> whereMap){
        String sql = DBUtils.preSelectSql(tableName, whereMap.keySet().toArray(new String[whereMap.keySet().size()]));
        return find(sql, whereMap.values().toArray());
    }

    /**
     * 通过表名称和keyvaluesMap插入对象
     * @param tableName
     * @param kvMap
     * @return
     */
    protected int insert(String tableName, Map<Object,Object> kvMap){
        String sql = DBUtils.preInsertSql2(tableName, kvMap.keySet().toArray(new String[kvMap.keySet().size()]));
        return update(sql, kvMap.values().toArray());
    }

    protected int update(String tableName, Map<Object,Object> setMap, Map<Object,Object> whereMap){
        String[] setKeys = setMap.keySet().toArray(new String[setMap.keySet().size()]);
        Object[] setValues = setMap.values().toArray();

        String[] whereKeys = whereMap.keySet().toArray(new String[whereMap.keySet().size()]);
        Object[] whereValues = whereMap.values().toArray();

        Object[] values = ArrayUtils.Connect(setValues,whereValues);
        String sql = DBUtils.preUpdateSql(tableName, setKeys, whereKeys);
        return update(sql, values);
    }

    protected int delete(String tableName, Map<Object,Object> whereMap){
        String sql = DBUtils.preDeleteSql(tableName, whereMap.keySet().toArray(new String[whereMap.keySet().size()]));
        return update(sql, whereMap.values().toArray());
    }

    protected int update(String sql, Object[] params){
//        this.logger.debug(sql);
        Connection conn = null;
        PreparedStatement ps = null;
        int resnum = 0;

        DBConnectionPool pool = null;

        try {
            pool = DBConnectionPool.getInstance();
            conn = pool.getConnection();
            ps = conn.prepareStatement(sql);
            for(int i = 0; i<params.length; i++){
                ps.setObject(i+1, params[i]);
            }
            resnum = ps.executeUpdate();

        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
//                ps.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resnum;
    }

    /**
     * 需要实现来封装对应得实体类
     * @param rs
     * @return
     * @throws SQLException
     */
    protected abstract T rowMapper(ResultSet rs) throws SQLException;


}
