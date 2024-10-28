package fr.kiza.kikariapi;

import fr.kiza.hikariapi.HikariAPI;
import fr.kiza.hikariapi.database.query.DBQuery;

import java.sql.SQLException;

public class HikariTest {

    public static void main(String[] args) {
        try {
            HikariAPI.connect("root", "", "localhost", 3306, "teeworlds");

            if(hasAccount(10)){
                System.out.println("YES !");
            }else{
                System.out.println("NO !");
            }
        } finally {
            HikariAPI.disconnect();
        }
    }

    private static boolean hasAccount(final int id){
        return (boolean) new DBQuery(HikariAPI.getDbHandler().pool().getDataSource()).query((resultSet -> {
            try {
                if(resultSet.next()){
                    return true;
                }
            }catch (final SQLException e) {
                throw new RuntimeException(e);
            }
            return false;
        }), "SELECT id FROM users WHERE id='" + id + "'");
    }
}
