package com.bank.database.automationdatabaseservices.config;

import com.bank.database.automationdatabaseservices.model.ADBDevicesModel;
import com.bank.database.automationdatabaseservices.model.ResponseModel;
import com.bank.database.automationdatabaseservices.model.auth.UserModel;
import com.google.gson.Gson;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;

import java.util.*;

public class Config {
    public static String DBHOST = "automation_tools_mariadb_1";
    public static String DBNAME = "automation";
    public static String DBUSERNAME = "root";
    public static String DBPASSWORD = "M1r34cl3@";
    public static int DBPORT = 3306;
    public static String DBSERVICEHOST = "localhost";
    public static int DBSERVICEPORT = 9002;
    public static int REDISPORT = 6379;
    public static String REDIS = "automation-redis";
    public static String PUBLISHER = "automation-producer";
    public static int PUBLISHERPORT = 9003;
    public static String SUBSCRIBER = "services3-consumer";
    public static int SUBSCRIBEPORT = 9004;
    public static String KAFKAHOST = "automation_tools_kafka_1";
    public static int KAFKAPORT = 9092;
    public static String APPIUMSERVER = "172.18.0.1";
    public static int APPIUMPORT = 4723;
    public static String DEVICESMONITORINGSERVER = "172.18.0.1";
    public static int DEVICESMONITORINGPORT = 8081;
    public static boolean success = false;
    public static String CRAWLAPIURL = "192.168.1.236";
    public static int CRAWLAPIURLPORT = 8182;
    public static int CRAWLWATERFALLPORT = 5001;

    public static List<String> userToken = new ArrayList<>();
    public static String firebaseUrl = "https://fcm.googleapis.com/fcm/send";
    public static String firebaseTokenAuth = "AAAAttwOSB0:APA91bHTmbDIjUng5BaOGx-5EfK2YMgalhdpmbp3xfKtP-GE9oUHSEPHmU6DTKqsyo2CguREkASW9xvaUCgSyy-h6ePrOJO9oK6Tes_rWedqtLuiAOikUrutgA5NBKVt1_wQfbRRcmJj";

    public static List<ADBDevicesModel> LISTDEVICES = new ArrayList<ADBDevicesModel>();

    public static UserModel GET(String key){
        try{
            RedisConnection<String, String> connection = REDISCONF();
            String result = connection.get(key);
            Gson gson = new Gson();
            return gson.fromJson(result, UserModel.class);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static RedisConnection<String, String> REDISCONF(){
        RedisClient redisClient = new RedisClient(RedisURI.create("redis://"+Config.REDIS+":"+Config.REDISPORT));
        RedisConnection<String, String> connectin = redisClient.connect();
        return connectin;
    }

    public static Map<String, String> show(String conf) {
        Map<String, String> data = new HashMap<>();

        if (success){
            data.put("dbhost", Config.DBHOST + " ->" + conf);
            data.put("dbname", Config.DBNAME + " ->" + conf);
            data.put("dbusername", Config.DBUSERNAME + " ->" + conf);
            data.put("dbpassword", Config.DBPASSWORD + " ->" + conf);
            data.put("dbport", Config.DBPORT + " ->" + conf);
            data.put("redisport", Config.REDISPORT + " ->" + conf);
            data.put("redis", Config.REDIS + " ->" + conf);
            data.put("publisher", Config.PUBLISHER + " ->" + conf);
            data.put("publisherport", Config.PUBLISHERPORT + " ->" + conf);
            data.put("subscriber", Config.SUBSCRIBER + " ->" + conf);
            data.put("subscriberport", Config.SUBSCRIBEPORT + " ->" + conf);
            data.put("kafkahost", Config.KAFKAHOST + " ->" + conf);
            data.put("kafkaport", Config.KAFKAPORT + " ->" + conf);
            data.put("appiumserver", Config.APPIUMSERVER + " ->" + conf);
            data.put("appiumport", Config.APPIUMPORT + " ->" + conf);
            data.put("devicesmonitoringserver", Config.DEVICESMONITORINGSERVER + " ->" + conf);
            data.put("devicesmonitoringport", Config.DEVICESMONITORINGPORT + " ->" + conf);
            data.put("crawlurl", Config.CRAWLAPIURL + " -> " + conf);
            data.put("crawlurlport", Config.CRAWLAPIURLPORT + " -> " + conf);
            data.put("crawlwaterfallport", Config.CRAWLWATERFALLPORT + " -> " + conf);
        }else{
            data.put("Error", "Error On call another API");
        }

        return data;
    }

    public static ResponseModel RESPONSECODE(int code, String message, String id){
        ResponseModel responseModel = new ResponseModel();
        Date date = new Date();
        responseModel.setCode(code);
        responseModel.setUniqueid(id);
        responseModel.setMessage(message);
        responseModel.setTime(date);
        return responseModel;
    }

    public static ResponseModel mResponseCode(int code, List<Map<String, String>> list, String message){
        ResponseModel responseModel = new ResponseModel();
        Date date = new Date();
        responseModel.setCode(code);
        responseModel.setMessage(message);
        responseModel.setData(list);
        responseModel.setTime(date);
        return responseModel;
    }
}
