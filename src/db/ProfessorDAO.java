
package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import model.Professor;

public class ProfessorDAO {

	// Database connection
	private Connection connection;

	public ProfessorDAO() {
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

    public void add (Professor professor) throws SQLException {
        String sql = "insert into professor (idProf, nomeProf, idDepto) values (?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setLong(1, professor.getIdProf());

            // If attribute on model object is null, insert null into table field
            if (professor.getNomeProf() == null) {
                stmt.setNull(2, Types.VARCHAR);
            } else {
                stmt.setString(2, professor.getNomeProf());
            }

            if (professor.getIdDepto() == null) {
                stmt.setNull(3, Types.NUMERIC);
            } else {
                stmt.setLong(3, professor.getIdDepto());
            }

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void update (Professor professor) throws SQLException {
        String sql = "update professor set nomeProf=?, idDepto=? where idProf=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            if (professor.getNomeProf() == null) {
                stmt.setNull(1, Types.VARCHAR);
            } else {
                stmt.setString(1, professor.getNomeProf());
            }

            if (professor.getIdDepto() == null) {
                stmt.setNull(2, Types.NUMERIC);
            } else {
                stmt.setLong(2, professor.getIdDepto());
            }

            stmt.setLong(3, professor.getIdProf());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw e;
        }
    }

	public void remove(Professor professor) throws SQLException {
		try {
			PreparedStatement stmt = connection.prepareStatement("delete from professor where idProf=?");
			stmt.setLong(1, professor.getIdProf());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
            throw e;
		}
	}


    /**
     * Retrives a new object of class 'Professor' that contains information from table 'professor' from DB
     * @param props - Properties that can identify an entry in table 'professor' (value is a tring)
	 * @return Professor - A new object of type 'Professor'
     */
	public Professor find(Properties props) throws SQLException {
		return this.find(props.getProperty("idProf"));
    }
	public Professor find(String idProf) throws SQLException {

		if (idProf == null || idProf.equals("")) {
			return null;
		}

		try {
			return this.find(Long.parseLong(idProf));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public Professor find(Long idProf) throws SQLException {
		try {
			Professor professor = null;

			PreparedStatement stmt = connection.prepareStatement("select * from professor where idProf=?");
			stmt.setLong(1, idProf);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				professor = new Professor();

                professor.setIdProf(rs.getLong("idProf"));
                professor.setNomeProf(rs.getString("nomeProf"));

				Long idDepto = rs.getLong("idDepto");
				if (rs.wasNull()) {
					professor.setIdDepto(null);
				} else {
					professor.setIdDepto(idProf);
				}

			}
			rs.close();
			stmt.close();
			return professor;
		} catch (SQLException e) {
            throw e;
		}
	}
}
