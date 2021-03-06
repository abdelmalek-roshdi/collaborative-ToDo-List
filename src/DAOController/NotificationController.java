/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOController;

import Connection.DataBaseConnection;
import DAOs.BaseDAO;
import Entities.BaseEntity;
import Entities.NotificationEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abd-Elmalek
 */
public class NotificationController<NotificationDAO> implements BaseDAO<NotificationEntity> {

    private Connection connection = DataBaseConnection.getInstance();
    private PreparedStatement preparedStatement;
    private NotificationEntity notificationEntity;

    /*EMAN KAMAL */
    @Override
    public ArrayList<NotificationEntity> findAll() {
        int id = 0;
        Date time = null;
        String msg = "";
        int receiverUserId = 0;
        int senderUserId = 0;
        ArrayList<NotificationEntity> notification_list = new ArrayList<NotificationEntity>();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM [todoDB].[dbo].[notification] ");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
                time = rs.getDate(2);
                receiverUserId = rs.getInt(3);
                senderUserId = rs.getInt(4);
                msg = rs.getString(5);
                notification_list.add(new NotificationEntity(id, time, msg, receiverUserId, senderUserId));

            }
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notification_list;
    }

    @Override
    public NotificationEntity findById(int id) {
        Date time = null;
        String msg = "";
        int receiverUserId = 0;
        int senderUserId = 0;
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM [todoDB].[dbo].[notification] WHERE id = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
                time = rs.getDate(2);
                receiverUserId = rs.getInt(3);
                senderUserId = rs.getInt(4);
                msg = rs.getString(5);
                notificationEntity = new NotificationEntity(id, time, msg, receiverUserId, senderUserId);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notificationEntity;
    }

    @Override
    public boolean insert(NotificationEntity entity) {
        int rows_affected = 0;
        PreparedStatement pst=null;
        try {
            pst = connection.prepareStatement("INSERT INTO [todoDB].[dbo].[notification] (time,receiverUserId,senderUserId,message) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            pst.setDate(1, entity.getTime());
            pst.setInt(2, entity.getReceivedUserId());
            pst.setInt(3, entity.getSentUserId());
            pst.setString(4, entity.getMsg());
            rows_affected = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rows_affected > 0) {
            try {
                ResultSet rs2 = pst.getGeneratedKeys();
                if (rs2.next()) {
                    int notifiId = rs2.getInt(1);
                    entity.setId(notifiId);
                    System.out.println(notifiId);
                }
            } catch (SQLException ex) {
                Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean update(NotificationEntity entity) {
        int rows_affected = 0;
        try {
            PreparedStatement pst
                    = connection.prepareStatement("UPDATE [todoDB].[dbo].[notification] SET time = ?, receiverUserId = ?,senderUserId=?, message=? WHERE id = ?");
            pst.setDate(1, entity.getTime());
            pst.setInt(2, entity.getReceivedUserId());
            pst.setInt(3, entity.getSentUserId());
            pst.setString(4, entity.getMsg());
            pst.setInt(5, entity.getId());
            rows_affected = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (rows_affected > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(NotificationEntity entity) {
        int rows_affected = 0;
        try {
            PreparedStatement pst = connection.prepareStatement("DELETE FROM [todoDB].[dbo].[notification] WHERE id = ?");
            pst.setInt(1, entity.getId());
            rows_affected = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rows_affected > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*EMAN KAMAL */
    public ArrayList<NotificationEntity> findByReceiverId(int receiverId) {
        ArrayList<NotificationEntity> notifications = new ArrayList<NotificationEntity>();

        try {
            String query = "SELECT *\n"
                    + "FROM [todoDB].[dbo].[notification]\n"
                    + "WHERE receiverUserId = ?;";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, receiverId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                notifications.add(new NotificationEntity(resultSet.getInt("id"), resultSet.getDate("time"), resultSet.getString("message"), resultSet.getInt("receiverUserId"), resultSet.getInt("senderUserId")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return notifications;
    }

}
