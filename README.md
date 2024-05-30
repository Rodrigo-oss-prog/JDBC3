# Data Access Object (DAO)

O padrão de projeto DAO separa a lógica de negócios da lógica de persistência de dados. Ele encapsula os mecanismos de acesso aos dados e fornece apenas interfaces de comunicação. Desta forma, é possível alterar ou acrescentar fontes de dados sem alterar a camada de lógica de negócios.

* SEM uso do DAO:

public static void main(String[] args) {

         Curso curso = new Curso(100,"Java");

         curso.salvar();

}


COM uso do DAO:

public static void main(String[] args) {

         Curso curso = new Curso(100,"Java");

         CursoDAO cursoDAO = new CursoDAO();

         cursoDAO.salvar(curso);

}

# ConnectionFactory

## exemplos

O padrão de projeto Factory encapsula a criação  (fabricação) de objetos.

Para acessar um BD, é útil criarmos uma fábrica de conexão (ConnectionFactory). Esta “fábrica” irá nos retornar uma instância do objeto de conexão com o banco de dados, de maneira a evitar codificação repetida em diversos locais do código.  

Mais ainda: se precisar alterar a string de conexão, nome do BD ou o próprio BD, é só alterar a ConnectionFactory.
````
package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionFactory implements AutoCloseable {
    private static PreparedStatement stmt = null;
    private static Connection conn  = null;

    public  static Connection criaConexao() {
        try {
            if(conn == null)
                conn = DriverManager.getConnection("jdbc:sqlite:aula1.db");
        }catch (SQLException e){
         e.printStackTrace();
        }
        return conn;
    }

    public static PreparedStatement criaStatement(String sql){
        try {stmt = criaConexao().prepareStatement(sql);}
        catch (SQLException e)
        { e.printStackTrace();}
        return stmt;
    }

    @Override
    public void close() throws Exception {
        if(conn != null)
            conn.close();
        if(stmt != null)
            stmt.close();
    }

}
````
# Domínio Curso

````
Classe Curso

package model;

public class Curso {
    private int idCurso;
    private String nome;

    public void mostrarDados(){
        System.out.println("\n Id: " + getIdCurso() +
                "\n Nome: " + getNome());
    }
    public Curso(int idCurso, String nome) {
        this.idCurso = idCurso;
        this.nome = nome;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
````

# Interface CursoDAO
````
package data;

import model.Curso;

import java.util.List;

public interface CursoDAO {
     void  salvar(Curso c);

     void atualizar (Curso c);
     void excluir (Curso c);
     Curso buscar (int codCur);
     List<Curso> buscarTodos();
}
````
# Classe CursoSQLiteDAO
````
package data;

import model.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public  class CursoSQLiteDAO implements CursoDAO{

    @Override
    public   void salvar(Curso c) {
        String sql = "INSERT INTO curso values (?,?)";
        try(PreparedStatement stmt=ConnectionFactory.criaStatement(sql)){
            stmt.setInt(1,c.getIdCurso());
            stmt.setString(2,c.getNome());
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Curso c) {
        String sql = "UPDATE curso SET nome=? WHERE idCurso=?";
        try(PreparedStatement stmt=ConnectionFactory.criaStatement(sql)){
            stmt.setString(1,c.getNome());
            stmt.setInt(2,c.getIdCurso());
            stmt.executeUpdate();
        }
         catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excluir(Curso c) {
        String sql = "DELETE FROM curso WHERE idCurso=?";

        try(PreparedStatement stmt = ConnectionFactory.criaStatement(sql)){
            stmt.setInt(1,c.getIdCurso());
            stmt.executeUpdate();
        }
       catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Curso buscar(int codCur) {
        String sql = "SELECT * FROM curso WHERE idCurso=?";
        Curso c=null;
       try (PreparedStatement stmt = ConnectionFactory.criaStatement(sql)){
           stmt.setInt(1,codCur);
           ResultSet rs = stmt.executeQuery();
           while (rs.next())
               c = new Curso(rs.getInt("idCurso"), rs.getString("nome"));

       } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public List<Curso> buscarTodos() {
        String sql = "SELECT * FROM curso";
        List<Curso> listaCursos =new ArrayList<>();
        try (PreparedStatement stmt = ConnectionFactory.criaStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Curso c = new Curso(rs.getInt("idCurso"), rs.getString("nome"));
                listaCursos.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCursos;
    }
}

````

# Domínio Cidade
````
package model;


public class Cidade {
    private int idCidade;
    private String nome;


    public void mostrarDados(){
        System.out.println("\n Id: " + getIdCidade() +
                "\n Nome: " + getNome());
    }

    public Cidade(int idCidade, String nome) {
        this.idCidade = idCidade;
        this.nome = nome;
    }

    public int getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(int idCidade) {
        this.idCidade = idCidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}


package data;

import model.Cidade;

import java.util.List;

public interface CidadeDAO {
    void  salvar(Cidade c);
    void atualizar (Cidade c);
    void excluir (Cidade c);
    Cidade buscar (int codCid);
    List<Cidade> buscarTodos();
}

 package data;

import model.Cidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CidadeSQLiteDAO implements CidadeDAO{
    @Override
    public void salvar(Cidade c) {
        String sql = "INSERT INTO cidade values (?,?)";
        try(PreparedStatement stmt=ConnectionFactory.criaStatement(sql)) {
            stmt.setInt(1, c.getIdCidade());
            stmt.setString(2, c.getNome());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void atualizar(Cidade c) {

        String sql = "UPDATE cidade SET nome=? WHERE idCidade=?";
        try(PreparedStatement stmt=ConnectionFactory.criaStatement(sql)){

            stmt.setString(1,c.getNome());
            stmt.setInt(2,c.getIdCidade());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excluir(Cidade c) {
        String sql = "DELETE FROM cidade WHERE idCidade=?";
        try(PreparedStatement stmt=ConnectionFactory.criaStatement(sql)){
            stmt.setInt(1,c.getIdCidade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cidade buscar(int codCid) {
        Cidade c=null;
        String sql = "SELECT * FROM cidade WHERE idCidade=?";
        try(PreparedStatement stmt = ConnectionFactory.criaStatement(sql)){
            stmt.setInt(1,codCid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                c = new Cidade(rs.getInt("idCidade"), rs.getString("nome"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public List<Cidade> buscarTodos() {
        String sql = "SELECT * FROM cidade";
        List<Cidade> listaCursos =new ArrayList<>();
        try(PreparedStatement stmt = ConnectionFactory.criaStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cidade c = new Cidade(rs.getInt("idCidade"), rs.getString("nome"));
                listaCursos.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCursos;
    }
}
````

# Domínio Aluno

````
package model;

public class Aluno   {
    private int idAluno;
    private String prontuario;
    private String nome;
    private Curso curso;
    private Cidade cidade;



    public void mostrarDados(){
        System.out.println("\n Id: " + getIdAluno() +
                "\n Nome: " + this.getNome() +
                "\n Prontuario: " + this.getProntuario()+
                "\n model.Curso: " + getCurso().getNome() +
                "\n model.Cidade: " + getCidade().getNome());
    }
    public Aluno(int idAluno, String prontuario, String nome, Curso curso, Cidade cidade) {
        this.idAluno = idAluno;
        this.prontuario = prontuario;
        this.nome = nome;
        this.curso = curso;
        this.cidade = cidade;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public String getProntuario() {
        return prontuario;
    }

    public void setProntuario(String prontuario) {
        this.prontuario = prontuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
}


package data;

import model.Aluno;
import java.util.List;

public interface AlunoDAO {
    void  salvar(Aluno a);
    void atualizar (Aluno a);
    void apagar (Aluno a);
    Aluno buscar (String prontuario);
    List<Aluno> buscarTodos();
}


package data;

import model.Aluno;
import model.Cidade;
import model.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoSQLiteDAO implements AlunoDAO{
    @Override
    public void salvar(Aluno a) {
        String sql = "INSERT INTO aluno values (?,?,?,?,?)";

        try(PreparedStatement stmt = ConnectionFactory.criaStatement(sql)){
            stmt.setInt(1,a.getIdAluno());
            stmt.setString(2,a.getProntuario());
            stmt.setString(3,a.getNome());
            stmt.setInt(4,a.getCurso().getIdCurso());
            stmt.setInt(5, a.getCidade().getIdCidade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(Aluno a) {

        String sql = "UPDATE aluno SET prontuario=?, nome=?, curso=?, cidade=? WHERE idAluno=?";
        try(PreparedStatement stmt = ConnectionFactory.criaStatement(sql)){
            stmt.setString(1,a.getProntuario());
            stmt.setString(2,a.getNome());
            stmt.setInt(3,a.getCurso().getIdCurso());
            stmt.setInt(4, a.getCidade().getIdCidade());
            stmt.setInt(5,a.getIdAluno());
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apagar(Aluno a) {
        String sql = "DELETE FROM aluno WHERE idAluno=?";

        try(PreparedStatement stmt = ConnectionFactory.criaStatement(sql)){
            stmt.setInt(1,a.getIdAluno());
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Aluno buscar(String prontuario) {
        String sql = "SELECT * FROM aluno WHERE prontuario=?";
        Aluno a=null;
        try(PreparedStatement stmt = ConnectionFactory.criaStatement(sql)){
            stmt.setString(1,prontuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Curso curso = new CursoSQLiteDAO().buscar(rs.getInt("curso"));
                Cidade cidade = new CidadeSQLiteDAO().buscar(rs.getInt("cidade"));
                a = new Aluno(rs.getInt("idAluno"),
                        rs.getString("prontuario"),
                        rs.getString("nome"),
                        curso,cidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public List<Aluno> buscarTodos() {
        String sql = "SELECT * FROM aluno";
        List<Aluno> listaAlunos=new ArrayList<>();
        try(PreparedStatement stmt = ConnectionFactory.criaStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Curso curso = new CursoSQLiteDAO().buscar(rs.getInt("curso"));
                Cidade cidade = new CidadeSQLiteDAO().buscar(rs.getInt("cidade"));
                Aluno a = new Aluno(rs.getInt("idAluno"),
                        rs.getString("prontuario"),
                        rs.getString("nome"),
                        curso,cidade);
                listaAlunos.add(a);
            }
        }
         catch (SQLException e) {
            e.printStackTrace();
        }
        return listaAlunos;
    }
}
````

# Classe Principal
````
import data.AlunoSQLiteDAO;
import data.CidadeSQLiteDAO;
import data.CursoSQLiteDAO;
import model.Aluno;
import model.Cidade;
import model.Curso;

import java.util.List;

public class Principal {
    public static void main(String[] args) {
         Curso curso1 = new Curso(100,"Java");
         Curso curso2 = new Curso(200,"POO");
         CursoSQLiteDAO cursoDAO = new CursoSQLiteDAO();
         cursoDAO.salvar(curso1);
         cursoDAO.salvar(curso2);

        Cidade cidade1 = new Cidade(10, "São Carlos");
        Cidade cidade2 = new Cidade(20, "Ribeirão Preto");
        CidadeSQLiteDAO cidadeDAO = new CidadeSQLiteDAO();
        cidadeDAO.salvar(cidade1);
        cidadeDAO.salvar(cidade2);

        Aluno a1 = new Aluno(1,"1010","Maria",curso1,cidade1);
        Aluno a2 = new Aluno(2,"2020","João",curso2,cidade2);
        AlunoSQLiteDAO alunoDAO = new AlunoSQLiteDAO();
        alunoDAO.salvar(a1);
        alunoDAO.salvar(a2);

        List<Curso> listaCursos=cursoDAO.buscarTodos();
        for (Curso c : listaCursos )
            c.mostrarDados();

        List<Cidade> listaCidades=cidadeDAO.buscarTodos();
        for (Cidade c : listaCidades )
            c.mostrarDados();

        List<Aluno> listaAlunos=alunoDAO.buscarTodos();
        for (Aluno a : listaAlunos )
            a.mostrarDados();

    }
}
