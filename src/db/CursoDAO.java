
package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import model.*;
import db.CursoInfo;

public class CursoDAO {

	// Database connection
	private Connection connection;

	public CursoDAO() {
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

    public void add (Curso curso) throws SQLException {
        String sql = "insert into curso (nome, horario, sala, idProf) values (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, curso.getNome());

			// If attribute on model object is null, insert null into table field
			if (curso.getHorario() == null){
				stmt.setNull(2, Types.VARCHAR);
			} else {
				stmt.setString(2, curso.getHorario());
			}

			if (curso.getSala() == null){
				stmt.setNull(3, Types.VARCHAR);
			} else {
				stmt.setString(3, curso.getSala());
			}

			if (curso.getIdProf() == null){
				stmt.setNull(4, Types.NUMERIC);
			} else {
				stmt.setLong(4, curso.getIdProf());
			}

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void update (Curso curso) throws SQLException {
        String sql = "update curso set horario=?, sala=?, idProf=? where nome=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

			if (curso.getHorario() == null){
				stmt.setNull(1, Types.VARCHAR);
			} else {
				stmt.setString(1, curso.getHorario());
			}

			if (curso.getSala() == null){
				stmt.setNull(2, Types.VARCHAR);
			} else {
				stmt.setString(2, curso.getSala());
			}

			if (curso.getIdProf() == null){
				stmt.setNull(3, Types.NUMERIC);
			} else {
				stmt.setLong(3, curso.getIdProf());
			}

            stmt.setString(4, curso.getNome());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw e;
        }
    }

	public void remove(Curso curso) throws SQLException {
		try {
			PreparedStatement stmt = connection.prepareStatement("delete from curso where nome=?");
			stmt.setString(1, curso.getNome());

            stmt.execute();
			stmt.close();
		} catch (SQLException e) {
            throw e;
		}
	}

    /**
     * Retrives a new object of class 'Curso' that contains information from table 'curso' from DB
     * @param props - Properties that can identify an entry in table 'curso' (value is a tring)
	 * @return Curso - A new object of type 'Curso'
     */
	public Curso find(Properties props) throws SQLException {
		return this.find(props.getProperty("nome"));
    }

	public Curso find(String nome) throws SQLException {
		try {
			Curso curso = null;

			PreparedStatement stmt = connection.prepareStatement("select * from curso where nome=?");
			stmt.setString(1, nome);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				curso = new Curso();

				curso.setNome(rs.getString("nome"));
        		curso.setHorario(rs.getString("horario"));
        		curso.setSala(rs.getString("sala"));

				Long idProf = rs.getLong("idProf");
				if (rs.wasNull()) {
					curso.setIdProf(null);
				} else {
					curso.setIdProf(idProf);
				}

			}
			rs.close();
			stmt.close();
			return curso;
		} catch (SQLException e) {
            throw e;
		}
	}


	public List<Curso> findAll() throws SQLException {
		try {
			List<Curso> cursos = new ArrayList<Curso>();
			PreparedStatement stmt = this.connection
					.prepareStatement("select * from curso");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Curso curso = new Curso();
				curso.setNome(rs.getString("nome"));
				curso.setHorario(rs.getString("horario"));
				curso.setSala(rs.getString("sala"));
                curso.setIdProf(rs.getLong("idProf"));

				cursos.add(curso);
			}
			rs.close();
			stmt.close();
			return cursos;
		} catch (SQLException e) {
            throw e;
		}
	}

    /**
     * Retrives a list of courses and other information related to it. Courses selected by using the
     * name of the course (or part of it) and/or the name of a professor (or part of it).
     * @param nome_curso - Name (or part of name) of the course
     * @param nome_professor - Name (or part of name) of a professor
     * @return List<CursoInfo> - A list of CursoInfo, an object that aggregates information about
     * the course, the professor and the subscribed students
     */
    public List<CursoInfo> findAllSearch(String nome_curso, String nome_professor) throws SQLException {
		try {

			// search is case-insensitive
			nome_curso = nome_curso.toLowerCase();
            nome_professor = nome_professor.toLowerCase();

            List<CursoInfo> cursos_info = new ArrayList<CursoInfo>();
			PreparedStatement stmt = this.connection
					.prepareStatement("select * from curso natural join professor");
			ResultSet rs = stmt.executeQuery();

			// Storing the list of courses with info about the course and its professor
			while (rs.next()) {

                Matcher m_nome_curso = Pattern.compile(nome_curso).matcher(rs.getString("nome").toLowerCase());
                Matcher m_nome_professor = Pattern.compile(nome_professor).matcher(rs.getString("nomeProf").toLowerCase());

                if (!m_nome_curso.find() || !m_nome_professor.find()) {
                    continue;
                }

				Curso curso = new Curso();
				curso.setNome(rs.getString("nome"));
				curso.setHorario(rs.getString("horario"));
				curso.setSala(rs.getString("sala"));
                curso.setIdProf(rs.getLong("idProf"));

                Professor professor = new Professor();
                professor.setIdProf(rs.getLong("idProf"));
                professor.setNomeProf(rs.getString("nomeProf"));
                professor.setIdDepto(rs.getLong("idDepto"));

                CursoInfo curso_info = new CursoInfo();
                curso_info.setCurso(curso);
                curso_info.setProfessor(professor);

				cursos_info.add(curso_info);
			}
			rs.close();
			stmt.close();

            // Adding info about the subscribed students on the courses stores inside list
            addAlunosInfo(cursos_info);
			return cursos_info;

		} catch (SQLException e) {
            throw e;
		}
    }

    /**
     * Stores information about subscribed students on a list of courses
     * @param cursos_info - List of 'CursoInfo' objects, where we are storing new information about subscribed students
     */
    private void addAlunosInfo(List<CursoInfo> cursos_info) throws SQLException {
        try {
            PreparedStatement stmt = this.connection.prepareStatement(

                "select distinct aluno.* " +
                "from Aluno natural join Matriculado " +
                "where nroAluno in ( " +
                "    select distinct nroAluno" +
                "	from Curso inner join Matriculado " +
                "		on nome = nomeCurso " +
                "   where nome = ?)"
            );

            for (CursoInfo curso_info : cursos_info) {

                List<Aluno> alunos = new ArrayList<Aluno>();

                stmt.setString(1, curso_info.getCurso().getNome());
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

                curso_info.setAlunos(alunos);

                rs.close();
                stmt.clearParameters();
            }
            stmt.close();

        } catch (SQLException e) {
            throw e;
		}
    }


}
