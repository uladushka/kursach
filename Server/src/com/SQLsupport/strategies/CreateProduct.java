package com.SQLsupport.strategies;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.SQLsupport.Updatable;

import static com.SQLsupport.Constants.*;

public class CreateProduct implements Updatable {
    private String first_name;
    private float ave_grade, coefficient;
    int scholarship, iduser, idstudent;

    @Override
    public void getData(String data) {
        String[] dataFromClient = data.split("@@@");

        if (dataFromClient.length < 5) {
            System.out.println("Недостаточно данных: ожидается 5 значений.");
            return; // Выходим, если недостаточно данных
        }

        int count = 0;

        try {
            idstudent = Integer.parseInt(dataFromClient[count++]);
            iduser = Integer.parseInt(dataFromClient[count++]);
            first_name = dataFromClient[count++];

            // Обрабатываем ave_grade как float
            ave_grade = Float.parseFloat(dataFromClient[count++]);

            // Обрабатываем scholarship как int
            scholarship = Integer.parseInt(dataFromClient[count++]);

            // Обрабатываем coefficient как float
            coefficient = Float.parseFloat(dataFromClient[count++]);


        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка доступа к массиву: " + e.getMessage());
        }
    }

    @Override
    public boolean executeUpdate(Connection conn) {
        int count=1;
        try{
            String sql1 = "INSERT INTO " + DB_NAME + "." + SCHOLARSHIP_SCHEMA + " (" +
                    SCHOLARSHIP_SCHOLARSHIP + ","+
                    SCHOLARSHIP_COEFFICIENT + ")"+
                    " VALUES (?,?)";
            try(PreparedStatement prepStmt=conn.prepareStatement(sql1)){
                prepStmt.setInt(count++, scholarship);
                prepStmt.setFloat(count++, coefficient);
                prepStmt.executeUpdate();
            }
            count = 1;
            String sql2="INSERT INTO "+DB_NAME+"."+STUDENT_SCHEMA+" ("+
                    STUDENT_FIRSTNAME+","+
                    STUDENT_AVEGRADE+","+
                    STUDENT_MARK_ID+","+
                    STUDENT_SCHOLARSHIP_ID+","+
                    STUDENT_USER_ID+")"+
                    " VALUES (?,?,?,?,?,?)";
            try(PreparedStatement prepStmt=conn.prepareStatement(sql2)){
                prepStmt.setString(count++, first_name);
                prepStmt.setFloat(count++, ave_grade);
                prepStmt.setInt(count++, idstudent);
                prepStmt.setInt(count++, idstudent);
                prepStmt.setInt(count++, idstudent);
                return prepStmt.executeUpdate() > 0;
            }
        }catch(SQLException e){
        }
        return false;
    }
}
