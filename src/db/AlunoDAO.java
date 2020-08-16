
package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import model.Aluno;

public class AlunoDAO {

	// Database connection
	private Connection connection;

	public AlunoDAO() {
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


    public void add (Aluno aluno) throws SQLException {
        String sql = "insert into aluno (nroAluno, nomeAluno, formacao, nivel, idade) values (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setLong(1, aluno.getNroAluno());

			// If attribute on model object is null, insert null into table field
			if (aluno.getNomeAluno() == null){
				stmt.setNull(2, Types.VARCHAR);
			} else {
				stmt.setString(2, aluno.getNomeAluno());
			}

			if (aluno.getFormacao() == null) {
				stmt.setNull(3, Types.VARCHAR);
			} else {
				stmt.setString(3, aluno.getFormacao());
			}

			if (aluno.getNivel() == null) {
				stmt.setNull(4, Types.VARCHAR);
			} else {
				stmt.setString(4, aluno.getNivel());
			}

			if (aluno.getIdade() == null) {
				stmt.setNull(5, Types.NUMERIC);
			} else {
				stmt.setInt(5, aluno.getIdade());
			}


            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
			throw e;
        }

    }

    public void update (Aluno aluno) throws SQLException {
        String sql = "update aluno set nomeAluno=?, formacao=?, nivel=?, idade=? where nroAluno=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

			if (aluno.getNomeAluno() == null){
				stmt.setNull(1, Types.VARCHAR);
			} else {
				stmt.setString(1, aluno.getNomeAluno());
			}

			if (aluno.getFormacao() == null) {
				stmt.setNull(2, Types.VARCHAR);
			} else {
				stmt.setString(2, aluno.getFormacao());
			}

			if (aluno.getNivel() == null) {
				stmt.setNull(3, Types.VARCHAR);
			} else {
				stmt.setString(3, aluno.getNivel());
			}

			if (aluno.getIdade() == null) {
				stmt.setNull(4, Types.NUMERIC);
			} else {
				stmt.setInt(4, aluno.getIdade());
			}

			stmt.setLong(5, aluno.getNroAluno());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
			throw e;
        }
    }

	public void remove(Aluno aluno) throws SQLException {
		try {
			PreparedStatement stmt = connection.prepareStatement("delete from aluno where nroAluno=?");
			stmt.setLong(1, aluno.getNroAluno());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw e;
		}
	}

    /**
     * Retrives a new object of class 'Aluno' that contains information from table 'aluno' from DB
     * @param props - Properties that can identify an entry in table 'aluno' (value is a tring)
	 * @return Aluno - A new object of type 'Aluno'
     */
	public Aluno find(Properties props) throws SQLException {
		return this.find(props.getProperty("nroAluno"));
	}
	public Aluno find(String nroAluno) throws SQLException {

		// Treats cases where no entry can be found
		if (nroAluno == null || nroAluno.equals("")) {
			return null;
		}

		try {
			return this.find(Long.parseLong(nroAluno));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public Aluno find(Long nroAluno) throws SQLException {
		try {
			Aluno aluno = null;

			PreparedStatement stmt = connection.prepareStatement("select * from aluno where nroAluno=?");
			stmt.setLong(1, nroAluno);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				aluno = new Aluno();

				aluno.setNroAluno(rs.getLong("nroAluno"));
        		aluno.setNomeAluno(rs.getString("nomeAluno"));
        		aluno.setFormacao(rs.getString("formacao"));
        		aluno.setNivel(rs.getString("nivel"));

				// By default, getting null value from column in Db as a number returns 0.
				// Preventing this to happen (setting null instead of 0)
				Integer idade = rs.getInt("idade");
				if (rs.wasNull()) {
					aluno.setIdade(null);
				} else {
					aluno.setIdade(idade);
				}


			}
			rs.close();
			stmt.close();
			return aluno;
		} catch (SQLException e) {
			throw e;
		}
	}




	public List<Aluno> getAll() throws SQLException {
		try {
			List<Aluno> alunos = new ArrayList<Aluno>();
			PreparedStatement stmt = this.connection
					.prepareStatement("select * from aluno");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Aluno aluno = new Aluno();
				aluno.setNroAluno(rs.getLong("nroAluno"));
				aluno.setNomeAluno(rs.getString("nomeAluno"));
				aluno.setFormacao(rs.getString("formacao"));
                aluno.setNivel(rs.getString("nivel"));
                aluno.setIdade(rs.getInt("idade"));

				alunos.add(aluno);
			}
			rs.close();
			stmt.close();
			return alunos;
		} catch (SQLException e) {
			throw e;
		}
	}

}
