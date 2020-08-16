
package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import model.Matriculado;
import db.CursoInfo;

public class MatriculadoDAO {

	// Database connection
	private Connection connection;

	public MatriculadoDAO() {
		this.connection = ConnectionFactory.getInstance().getConnection();
    }

    public void endConnection() throws SQLException {
        try {
            if (!this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void add (Matriculado matriculado) throws SQLException {
        String sql = "insert into matriculado (nroAluno, nomeCurso) values (?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setLong(1, matriculado.getNroAluno());
            stmt.setString(2, matriculado.getNomeCurso());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw e;
        }
    }

	public void remove(Matriculado matriculado) throws SQLException {
		try {
			PreparedStatement stmt = connection.prepareStatement("delete from matriculado where nroAluno=? and nomeCurso=?");
			stmt.setLong(1, matriculado.getNroAluno());
            stmt.setString(2, matriculado.getNomeCurso());

            stmt.execute();
			stmt.close();
		} catch (SQLException e) {
            throw e;
		}
	}
}
