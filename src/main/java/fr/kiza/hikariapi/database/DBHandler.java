package fr.kiza.hikariapi.database;

import com.zaxxer.hikari.HikariConfig;
import fr.kiza.hikariapi.HikariAPI;
import fr.kiza.hikariapi.database.pool.HikariPool;

import java.time.Duration;
import java.time.Instant;

public class DBHandler extends Database{

    private String username, password, host;
    private int port;
    private String database;

    private HikariPool pool;

    public static class Builder{
        private String username, password, host;
        private int port;
        private String database;

        public Builder addUsername(final String username){
            this.username = username;
            return this;
        }

        public Builder addPassword(final String password){
            this.password = password;
            return this;
        }

        public Builder addHost(final String host){
            this.host = host;
            return this;
        }

        public Builder addPort(final int port){
            this.port = port;
            return this;
        }

        public Builder addDatabase(final String database){
            this.database = database;
            return this;
        }

        public Builder addAll(final String username, final String password, final String host, final int port, final String database){
            this.username = username;
            this.password = password;
            this.host = host;
            this.port = port;
            this.database = database;
            return this;
        }

        public DBHandler build(){
            final Instant start = Instant.ofEpochMilli(Instant.now().toEpochMilli());

            final DBHandler data = new DBHandler();
            data.username = this.username;
            data.password = this.password;
            data.host = this.host;
            data.port = this.port;
            data.database = this.database;

            final HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database);
            config.setUsername(this.username);
            config.setPassword(this.password);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            data.pool = new HikariPool(config);

            HikariAPI.LOGGER.info("Connection done in: {} ms!", Duration.between(start, Instant.ofEpochMilli(Instant.now().toEpochMilli())).toMillis());

            return data;
        }
    }

    @Override
    public String username() {
        return this.username;
    }

    @Override
    public String password() {
        return this.password;
    }

    @Override
    public String host() {
        return this.host;
    }

    @Override
    public int port() {
        return this.port;
    }

    @Override
    public String database() {
        return this.database;
    }

    @Override
    public HikariPool pool() {
        return this.pool;
    }

    @Override
    public String toString() {
        return "DBHandler{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", database='" + database + '\'' +
                ", pool=" + pool.toString() +
                '}';
    }
}
