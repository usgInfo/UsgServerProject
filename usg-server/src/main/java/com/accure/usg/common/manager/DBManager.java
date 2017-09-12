package com.accure.usg.common.manager;

import com.accure.db.in.DAO;
import com.accure.db.in.DAOFactory;
import com.mongodb.MongoClient;
import static com.accure.usg.server.utils.Common.getConfig;
import com.mongodb.DB;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;

import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author accure
 */
public class DBManager {

    public static DAO getDbConnection() throws Exception {
        PropertiesConfiguration config = getConfig();
        DAO dao = DAOFactory.getDAO((String) config.getProperty("db-name"),
                (String) config.getProperty("db-url"),
                (String) config.getProperty("db-port"),
                (String) config.getProperty("db-schema"),
                (String) config.getProperty("db-un"),
                (String) config.getProperty("db-pwd"));
        return dao;
    }

    public static MongoClient getMongoClient() throws Exception {
        PropertiesConfiguration config = getConfig();
        MongoCredential credential = MongoCredential.createMongoCRCredential((String) config.getProperty("db-un"), (String) config.getProperty("db-schema"), ((String) config.getProperty("db-pwd")).toCharArray());
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.cursorFinalizerEnabled(false);
        builder.connectionsPerHost(500);
        MongoClient mngClient = new MongoClient(new ServerAddress((String) config.getProperty("db-url"), Integer.parseInt((String) config.getProperty("db-port"))), Arrays.asList(credential), builder.build());
        return mngClient;
    }

    public static DB getDB() throws Exception {
        DB db = DBManager.getDbConnection().getDB();
        return db;
    }

    public static MongoDatabase getMongoDatabase() throws Exception {
        PropertiesConfiguration config = getConfig();
        MongoDatabase db = DBManager.getMongoClient().getDatabase((String) config.getProperty("db-schema"));
        return db;
    }
}
