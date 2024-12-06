package com.SQLsupport.strategies;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.SQLsupport.Updatable;

import static com.SQLsupport.Constants.*;
import static java.lang.Integer.parseInt;

public class AddManufacturer implements Updatable {
    int programming,  unity;
    int course_work;
    String idmark,maths;


    @Override
    public void getData(String data) {
        String[] dataFromClient = data.split("@@@");

        // Проверяем, что массив содержит достаточное количество элементов
        if (dataFromClient.length < 5) {
            System.out.println("Недостаточно данных: ожидается минимум 5 значений.");
            return; // Выход, если недостаточно данных
        }

        int i = 0;

        try {
             idmark = String.format(dataFromClient[i++]);
             maths = String.format(dataFromClient[i++]);
            programming = parseInt(dataFromClient[i++]);
            unity = parseInt(dataFromClient[i++]);
            course_work = parseInt(dataFromClient[i++]);
        }  catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка доступа к массиву: " + e.getMessage());
        }
    }

    @Override
    public boolean executeUpdate(Connection conn) {
        int count=1;
        try{
            String sql1="INSERT INTO "+DB_NAME+"."+MARK_SCHEMA+" ("+
                    MARK_ID + ","+
                    MARK_MATHS+","+
                    MARK_PROGRAMMING+","+
                    MARK_UNITY+","+
                    MARK_COURSEWORK+")"+
                    " VALUES (?,?,?,?,?);";
            try(PreparedStatement prepStmt=conn.prepareStatement(sql1)){
                prepStmt.setString(count++, idmark);
                prepStmt.setString(count++, maths);
                prepStmt.setInt(count++, programming);
                prepStmt.setInt(count++, unity);
                prepStmt.setInt(count++, course_work);
                return prepStmt.executeUpdate() > 0;
            }
        }catch(SQLException e){

        }
        return false;
    }
}
