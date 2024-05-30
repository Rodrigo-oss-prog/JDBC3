import Data.ParttidaDAO;
import Data.TimeDAO;
import Model.Partida;
import Model.Time;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Jogos!");

        TimeDAO timeDAO = new TimeDAO();
        Time time1 = new Time(12, "Brasil");
        Time time2 = new Time(13, "Argentina");

        timeDAO.save(time1);
        timeDAO.save(time2);

        ParttidaDAO parttidaDAO = new ParttidaDAO();
        Partida p1 = new Partida(10, "20/07/2023", time1, time2, 1, 2);
        Partida p2 = new Partida(11, "21/07/2023", time1, time2, 2, 3);

        parttidaDAO.save(p1);
        parttidaDAO.save(p2);

        List<Time> times = timeDAO.findAll();
        for (Time time : times) {
            System.out.println(time);
        }

        List<Partida> partidas = parttidaDAO.findAll();
        for (Partida partida : partidas) {
            System.out.println(partida);
        }


    }
}