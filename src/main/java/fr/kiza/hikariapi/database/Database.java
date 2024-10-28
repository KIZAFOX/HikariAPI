package fr.kiza.hikariapi.database;

import fr.kiza.hikariapi.database.pool.HikariPool;

public abstract class Database {

    abstract public String username();

    abstract public String password();

    abstract public String host();

    abstract public int port();

    abstract public String database();

    abstract public HikariPool pool();

}