package Data;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {

    void save(T type);
    void update(T type);
    void delete(T type);
     T findById(int id);
     List<T> findAll();

}
