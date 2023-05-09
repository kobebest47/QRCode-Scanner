package com.example.myapplication;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
    String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    String DB_URL = "jdbc:mysql://192.168.0.160/warehouse?useSSL=false&allowPublicKeyRetrieval=false";
    String USER = "root";
    String PASS = "root" ;

    public void run() {
        try {
            Class.forName(JDBC_DRIVER);
            Log.v("DB", "加載驅動成功");
        } catch (ClassNotFoundException e) {
            Log.e("DB", "加載驅動失敗");
            return;
        }
    }
    public String getData() {
        String data = "";
        try {
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            String sql = "SELECT * FROM ware";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                String id = rs.getString("id");
                String name = rs.getString("store_name");
                data += id + ", " + name + "\n";
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    public void insertData(String data) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

            String sql = "INSERT INTO `ware` (`store_name`,`store_id`,`year`,`date`,`col`) VALUES (substring_index(('" + data + "'), '.',1)," +
                    "SUBSTRING_INDEX(SUBSTRING_INDEX('" + data + "','.',2),'.',-1),SUBSTRING_INDEX(SUBSTRING_INDEX('" + data + "','.',3),'.',-1)," +
                    "SUBSTRING_INDEX(SUBSTRING_INDEX('" + data + "','.',4),'.',-1),SUBSTRING_INDEX(SUBSTRING_INDEX('" + data + "','.',5),'.',-1))" ;

            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            st.close();
            Log.v("DB", "寫入資料完成：" + data);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("DB", "寫入資料失敗");
            Log.e("DB", e.toString());
        }
    }
    }

