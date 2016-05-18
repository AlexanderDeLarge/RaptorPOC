package com.cleverlance.raptorpoc.common;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBUtils {
    public static boolean rcAlreadyExists(String rc) throws SQLException {
        OracleDataSource ods;
        ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@//cpsdb:1521/RAP23");
        ods.setUser("CPSWRITE");
        ods.setPassword("Cpswrite2015");
        Connection con = ods.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select rc.pin from rgs_client rc where rc.pin = " + rc);
        try{
            rs.next();
            return true;
        } catch (Exception e){
            return false;
        }

    }


}
