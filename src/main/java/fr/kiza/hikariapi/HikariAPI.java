package fr.kiza.hikariapi;

import fr.kiza.hikariapi.database.DBHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HikariAPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(HikariAPI.class);

    private static DBHandler dbHandler;

    public static void connect(final String username, final String password, final String host, final int port, final String database){
        dbHandler = new DBHandler.Builder()
                .addAll(username, password, host, port, database)
                .build();
    }

    public static void disconnect(){
        if(dbHandler == null) LOGGER.error("DB_HANDLER cannot be null!");
        dbHandler.pool().close();
    }

    public static DBHandler getDbHandler() {
        return dbHandler;
    }
}
