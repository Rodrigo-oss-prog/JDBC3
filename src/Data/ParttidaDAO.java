package Data;

import Model.Partida;
import Model.Time;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParttidaDAO implements DAO<Partida>{

    @Override
    public void save(Partida p) {
        String sql = "INSERT INTO partida (idPartida, dataJogo, time1, " +
                "time2, placarTime1, placartime2) VALUES (?, ?, ?, ?, ?, ? )";
        try(PreparedStatement stmt = ConnectionFactory.createStatement(sql)){
            stmt.setInt(1, p.getIdPartida());
            stmt.setString(2, p.getDataJogo());
            stmt.setInt(3, p.getTime1().getIdTime());
            stmt.setInt(4, p.getTime2().getIdTime());
            stmt.setInt(5, p.getPlacarTime1());
            stmt.setInt(6, p.getPlacarTime2());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Partida p) {
        String sql = "UPDATE partida SET dataJogo =?, time1 =?, " +
                "time2 =?, placarTime1 =?, placarTime2 =? WHERE idPartida =?";
        try(PreparedStatement stmt = ConnectionFactory.createStatement(sql)){
            stmt.setString(1, p.getDataJogo());
            stmt.setInt(2, p.getTime1().getIdTime());
            stmt.setInt(3, p.getTime2().getIdTime());
            stmt.setInt(4, p.getPlacarTime1());
            stmt.setInt(5, p.getPlacarTime2());
            stmt.setInt(6, p.getIdPartida());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(Partida p) {
        String sql = "DELETE FROM partida WHERE idPartida =?";
        try(PreparedStatement stmt = ConnectionFactory.createStatement(sql)){
            stmt.setInt(1, p.getIdPartida());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Partida findById(int id) {
        String sql = "SELECT * FROM partida WHERE idPartida = ?";
        Partida p = null;
        try(PreparedStatement stmt = ConnectionFactory.createStatement(sql)){

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Time time1 = new TimeDAO().findById(rs.getInt("time1"));
                Time time2 = new TimeDAO().findById(rs.getInt("time2"));
                p = new Partida( rs.getInt("idPartida"),
                        rs.getString("dataJogo"),
                        time1,
                        time2,
                        rs.getInt("placarTime1"),
                        rs.getInt("placarTime2"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    @Override
    public List<Partida> findAll() {
        String sql = "SELECT * FROM partida";
        List<Partida> partidas = new ArrayList<>();
        try(PreparedStatement stmt = ConnectionFactory.createStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Time time1 = new TimeDAO().findById(rs.getInt("time1"));
                Time time2 = new TimeDAO().findById(rs.getInt("time2"));
                Partida p = new Partida(rs.getInt("idPartida"),
                        rs.getString("dataJogo"), time1,
                        time2, rs.getInt("placarTime1"),
                        rs.getInt("placarTime2"));
                partidas.add(p);


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partidas;

    }

}
