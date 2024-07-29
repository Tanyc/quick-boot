package com.quick.online.config;

import apijson.framework.APIJSONSQLExecutor;
import apijson.orm.SQLConfig;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.quick.common.util.SpringBeanUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

public class OnlineSQLExecutor extends APIJSONSQLExecutor<String> {
    public static final String TAG = "OnlineSQLExecutor";

    // 适配连接池
    @Override
    public Connection getConnection(SQLConfig config) throws Exception {
        String key = config.getDatasource() + "-" + config.getDatabase();
        Connection c = connectionMap.get(key);
        if (c == null || c.isClosed()) {
            DataSource dataSource = SpringBeanUtils.getBean(DataSource.class);
            DynamicRoutingDataSource datasource = (DynamicRoutingDataSource) dataSource;

            Map<String, DataSource> dataSources = datasource.getDataSources();
            DataSource ds = dataSources.get(config.getDatasource());
            connectionMap.put(key, ds == null ? null : ds.getConnection());
        }
        return super.getConnection(config);
    }

    /**
     *  统一响应 蛇形命名转小驼峰命名
     */
//    @Override
//    protected String getKey(SQLConfig<String> config, ResultSet rs, ResultSetMetaData rsmd, int tablePosition, JSONObject table, int columnIndex, Map<String, JSONObject> childMap) throws Exception {
//        String key = super.getKey(config, rs, rsmd, tablePosition, table, columnIndex, childMap);
//        return StrUtil.toCamelCase(key);
//    }
}
