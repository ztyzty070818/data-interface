package io.sugo.http.resource;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import io.sugo.http.database.OracleDbManager;
import io.sugo.http.resource.Cache.DataCache;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Path("/")
public class WxjResource {
    private static final Logger LOG = Logger.getLogger(WxjResource.class);
    private static DataCache dataCache = DataCache.getDataCacheInstance();
    private static Properties properties = new Properties();
    public static String salesRe;
    public static String receiptRe;
    public static String branchRe;
    public static boolean demo;

    public WxjResource() throws IOException {
        properties.load(new InputStreamReader(new FileInputStream("config.properties"), "UTF-8"));
        demo = properties.getProperty("demo").equals("true");
        salesRe = properties.getProperty("sales.result");
        receiptRe = properties.getProperty("receipt.result");
        branchRe = properties.getProperty("branch.result");
    }


    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final Statement stmt = OracleDbManager.getStmt();

    @POST
    @Path("/sales")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sales()  {

        String returnStr;
        if(demo == true) {
            returnStr = salesRe;
        } else {
            try {
                returnStr = getResultFromCache("sales");
            } catch (Exception e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ImmutableMap.<String, Object>of("error", e.getMessage()))
                        .build();
            }
        }
        return Response.ok(returnStr).build();
    }

    @POST
    @Path("/receipt")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receipt() throws Exception {

        String returnStr;
        if(demo == true) {
            returnStr = receiptRe;
        } else {
            returnStr = getResultFromCache("receipt");
        }
        return Response.ok(returnStr).build();
    }


    public static String createSql(String name) {
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append(properties.getProperty(name+".areas"));
        sb.append(" from ");
        sb.append(properties.getProperty(name+".table"));
        sb.append(" "+properties.getProperty(name+".where"));
        return sb.toString();
    }

    public static String getResultFromCache(String name) throws Exception {
        String sql;
        String returnStr;
        sql = getSQLFromCache(name);
        if(name.equals("sales")) {
            String branchSql = getSQLFromCache("branch");
            returnStr = getReceiptResult(sql,branchSql);
        } else {
            returnStr = getResult(sql,name);
        }

        return returnStr;
    }

    public static String getSQLFromCache(String name) {
        String sql = dataCache.getData(name+"_sql");
        if(sql == null) {
            sql = createSql(name);
            dataCache.postData(name+"_sql",sql);
        }
        return sql;
    }


    public static String getResult(String sql,String name) throws Exception {
        LOG.info("sql = "+sql);

        ResultSet resultSet = stmt.executeQuery(sql);
        String[] areas = properties.getProperty(name+".areas").split(",");
        String[] types = properties.getProperty(name+".type").split(",");
        String[] areas_EN = properties.getProperty(name+".english").split(",");

        Map<String,Object> map;
        List<String> list = new ArrayList<>();

        int count = 0;
        while(resultSet.next()) {

            map = new HashMap();

            for(int i=0; i<areas.length; i++) {
                if(types[i].trim().equals("int")) {
                    map.put(areas_EN[i],resultSet.getInt(areas[i]));
                } else if(types[i].trim().equals("double")) {
                    map.put(areas_EN[i],resultSet.getDouble(areas[i]));
                } else if(types[i].trim().equals("string")) {
                    String str = resultSet.getString(areas[i]);
                    if(str!=null && str.trim().length()>0) {
                        map.put(areas_EN[i],resultSet.getString(areas[i]));
                    } else {
                        map.put(areas_EN[i],"");
                    }
                } else if(types[i].trim().equals("date")) {
                    map.put(areas_EN[i],resultSet.getDate(areas[i]));
                } else if(types[i].trim().equals("float")) {
                    map.put(areas_EN[i],resultSet.getFloat(areas[i]));
                } else if(types[i].trim().equals("long")) {
                    map.put(areas_EN[i],resultSet.getLong(areas[i]));
                } else if(types[i].trim().equals("boolean")) {
                    map.put(areas_EN[i],resultSet.getBoolean(areas[i]));
                } else if(types[i].trim().equals("byte")) {
                    map.put(areas_EN[i],resultSet.getByte(areas[i]));
                } else {
                    throw new Exception("字段类型错误");
                }
            }

            count++;

            list.add(jsonMapper.writeValueAsString(map));
        }
        LOG.info("record count : " + count);
        String returnString = "{\"result\" : "+list.toString()+"}";
        return returnString;
    }

    public static String getReceiptResult(String sql,String BranchSql) throws Exception {

        Map<String,Map<String,Object>> receiptMap = getResultMap(sql,"sales");
        Map<String,Map<String,Object>> branchMap = getResultMap(BranchSql,"branch");
        List<String> list = new ArrayList<>();

        for(Map.Entry<String,Map<String,Object>> entry:receiptMap.entrySet()){
            Map<String,Object> bMap = branchMap.get(entry.getKey());
            String[] areas_EN = properties.getProperty("branch.english").split(",");
            entry.getValue().put(areas_EN[2], bMap.get(areas_EN[2]));
            entry.getValue().put(areas_EN[3], bMap.get(areas_EN[3]));
            entry.getValue().put(areas_EN[4], bMap.get(areas_EN[4]));
            list.add(jsonMapper.writeValueAsString(entry.getValue()));
        }





        String returnString = "{\"result\" : "+list.toString()+"}";
        return returnString;
    }

    public static Map<String,Map<String,Object>> getResultMap(String sql,String name) throws Exception {
        LOG.info("sql = "+sql);

        ResultSet resultSet = stmt.executeQuery(sql);
        String[] areas = properties.getProperty(name+".areas").split(",");
        String[] types = properties.getProperty(name+".type").split(",");
        String[] areas_EN = properties.getProperty(name+".english").split(",");

        Map<String,Object> map;
        Map<String,Map<String,Object>> rm = new HashMap<>();

        int count = 0;
        while(resultSet.next()) {

            map = new HashMap();

            for(int i=0; i<areas.length; i++) {
                if(types[i].trim().equals("int")) {
                    map.put(areas_EN[i],resultSet.getInt(areas[i]));
                } else if(types[i].trim().equals("double")) {
                    map.put(areas_EN[i],resultSet.getDouble(areas[i]));
                } else if(types[i].trim().equals("string")) {
                    String str = resultSet.getString(areas[i]);
                    if(str!=null && str.trim().length()>0) {
                        map.put(areas_EN[i],resultSet.getString(areas[i]));
                    } else {
                        map.put(areas_EN[i],"");
                    }
                } else if(types[i].trim().equals("date")) {
                    map.put(areas_EN[i],resultSet.getDate(areas[i]));
                } else if(types[i].trim().equals("float")) {
                    map.put(areas_EN[i],resultSet.getFloat(areas[i]));
                } else if(types[i].trim().equals("long")) {
                    map.put(areas_EN[i],resultSet.getLong(areas[i]));
                } else if(types[i].trim().equals("boolean")) {
                    map.put(areas_EN[i],resultSet.getBoolean(areas[i]));
                } else if(types[i].trim().equals("byte")) {
                    map.put(areas_EN[i],resultSet.getByte(areas[i]));
                } else {
                    throw new Exception("数据库字段类型对应不上");
                }
            }

            count++;
            String keyStr = map.get("code")+"";
            rm.put(keyStr,map);
        }
        LOG.info("record count : " + count);
        return rm;
    }

    public static String createErrorReturn(String errorStr) {
        String returnStr = "{\"error\" : \""+errorStr + "\"}";
        return returnStr;
    }

}
