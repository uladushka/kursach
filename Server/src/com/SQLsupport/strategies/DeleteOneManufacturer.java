package com.SQLsupport.strategies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.SQLsupport.Updatable;

import static com.SQLsupport.Constants.*;

public class DeleteOneManufacturer implements Updatable {
    private int id;
    @Override
    public void getData(String data) {
        id=Integer.parseInt(data);
    }

    @Override
    public boolean executeUpdate(Connection conn) {
        int count=1;
        try{
            String sql1="DELETE FROM "+DB_NAME+"."+MARK_SCHEMA+
                    " WHERE "+MARK_ID+" =?";
            try(PreparedStatement prepStmt=conn.prepareStatement(sql1)){
                prepStmt.setInt(count++, id);
                return prepStmt.executeUpdate() > 0;
            }
        }catch(SQLException e){

        }
        return false;
    }
}
