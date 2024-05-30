package Data;

import Model.Partida;
import Model.Time;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TimeDAO implements DAO<Time>{


    @Override
    public void save(Time type) {
        String sql = "INSERT INTO time (idTime, pais) VALUES (?,?)";
        try(PreparedStatement stmt = ConnectionFactory.createStatement(sql)){
            stmt.setInt(1, type.getIdTime());
            stmt.setString(2, type.getPais());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Time type) {
        String sql = "UPDATE time SET pais =? WHERE idTime =?";
        try(PreparedStatement stmt = ConnectionFactory.createStatement(sql)){

            stmt.setString(1, type.getPais());
            stmt.setInt(2, type.getIdTime());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Time type) {
        String sql = "DELETE FROM time WHERE idTime =?";
        try(PreparedStatement stmt = ConnectionFactory.createStatement(sql)){

            stmt.setInt(1, type.getIdTime());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Time findById(int id) {
        String sql = "SELECT * FROM time WHERE idTime = ?";
        Time t = null;
        try(PreparedStatement stmt = ConnectionFactory.createStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                t = new Time(rs.getInt("idTime"),
                rs.getString("pais"));
            }


        }catch(Exception e){
            e.printStackTrace();
        }


        return t;
    }

    @Override
    public List<Time> findAll() {
        String sql = "SELECT * FROM time WHERE idTime = ?";
        List<Time> times = new ArrayList<>();
        try(PreparedStatement stmt = ConnectionFactory.createStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
               Time t = new Time(rs.getInt("idTime"),
                        rs.getString("pais"));
               times.add(t);


            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return times;
    }
}
