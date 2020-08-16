package cli;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.LinkedHashMap;
import java.util.Map;

import model.*;
import db.*;

// Implements the required operations
public class Operations {

    private static final AlunoDAO aluno_dao = RegistrationSystem.getAlunoDAO();
    private static final ProfessorDAO professor_dao = RegistrationSystem.getProfessorDAO();
    private static final CursoDAO curso_dao = RegistrationSystem.getCursoDAO();
    private static final MatriculadoDAO matriculado_dao = RegistrationSystem.getMatriculadoDAO();

    /**
     * Get all key atributes from a specific model
     * @param table - The table name that has a specific model object counterpart
     * @return LinkedHashMap<String, String> - Key = Attribute name, Val = Attribute description
     */
    public static void insertInto(String table) {

        try {
            table = table.toLowerCase();
            Properties input_values = Misc.getAllAtributesFromInput(table);

            // Adding inputed values to DB
            if (table.equals("aluno")) {
                Aluno aluno = new Aluno(input_values);
                aluno_dao.add(aluno);
            }
            else if (table.equals("professor")) {
                Professor professor = new Professor(input_values);
                professor_dao.add(professor);
            }
            else if (table.equals("curso")) {
                Curso curso = new Curso(input_values);
                curso_dao.add(curso);
            }
            else {
                throw new UnsupportedOperationException("Metodo de insercao para tabela" + table + " nao foi implemenatado na camada de apresentacao");
            }

            System.out.println("Operacao de insercao completa!");

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.out.println("Nao foi possivel completar operacao de insercao!");
        }
    }


    /**
     * Starts an interective routine on terminal, asking for information about an entry to be found
     * on the specified table and, if found, update its non-primary-key columns
     * @param table - String representing the table where we are getting an entry
     */
    public static void update(String table) {

        try {
            table = table.toLowerCase();

            System.out.println("Insira os proximos atributos para encontrar uma entrada do banco de dados");
            System.out.println("----------------------------");

            Properties input_key_values = Misc.getKeyAtributesFromInput(table);

            GenericObjInterface update_obj = null;

            // Finding entry on DB to update
            if (table.equals("aluno"))
                update_obj = aluno_dao.find(input_key_values);
            else if (table.equals("professor"))
                update_obj = professor_dao.find(input_key_values);
            else if (table.equals("curso"))
                update_obj = curso_dao.find(input_key_values);
            else
                throw new UnsupportedOperationException("Metodo de atualizacao para tabela" + table + " nao foi implemenatado na camada de apresentacao");

            if (update_obj == null) {
                System.out.println("Nao foi encontrada nenhuma entrada no banco de dados!");
                return;
            }


            System.out.println("----------------------------");

            // Get all attributes to be changed (only the ones considered not primary keys) inside object retrieved from DB
            for (Map.Entry<String, String> entry : GenericObj.getNonKeyAttributesFrom(table).entrySet()) {
                String db_field = entry.getKey();
                String description = entry.getValue();

                // User input info
                System.out.print(">> " + description + " (" + update_obj.genericGet(db_field) + "): ");
                String input = System.console().readLine();

                // If user inputs "null", nullifies field
                if (input.equals("null"))
                    input = null;
                // If input is empty, maintain current value
                else if (input.equals("")) {
                    Object tmp = update_obj.genericGet(db_field);
                    input = tmp == null ? null : tmp.toString();
                }
                update_obj.genericSet(db_field, input);
            }

            boolean isConfirmed = Misc.isConfirmationConsole("Aplicar as mudancas especificadas?");

            if (!isConfirmed) {
                System.out.println("Operacao de atualizacao cancelada!");
                return;
            }

            // Updating on DB from updated object
            if (table.equals("aluno")){
                aluno_dao.update(((Aluno)update_obj));
            } else if (table.equals("professor")) {
                professor_dao.update(((Professor)update_obj));
            } else if (table.equals("curso")) {
                curso_dao.update(((Curso)update_obj));
            }

            System.out.println("Operacao de atualização completa!");

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.out.println("Nao foi possivel completar operacao de atualizacao!");
        }


    }


    /**
     * Starts an interective routine on terminal, asking for information about an entry to be
     * removed and, if acepted, remove entry from database
     * @param table - String representing the table where we are removing an entry from
     */
    public static void remove(String table) {

        try {
            table = table.toLowerCase();

            System.out.println("Insira os próximos atributos");
            System.out.println("----------------------------");

            Properties input_key_values = Misc.getKeyAtributesFromInput(table);

            GenericObjInterface update_obj = null;

            if (table.equals("aluno"))
                update_obj = aluno_dao.find(input_key_values);
            else if (table.equals("professor"))
                update_obj = professor_dao.find(input_key_values);
            else if (table.equals("curso"))
                update_obj = curso_dao.find(input_key_values);
            else
                throw new UnsupportedOperationException("Metodo de remocao para tabela" + table + " nao foi implemenatado na camada de apresentacao");


            if (update_obj == null) {
                System.out.println("Nao foi encontrada nenhuma entrada no banco de dados!");
                return;
            }

            System.out.println("----------------------------");

            System.out.println(update_obj);

            boolean isConfirmed = Misc.isConfirmationConsole("A entrada acima será removida do banco de dados. Tem certeza?");

            if (!isConfirmed) {
                System.out.println("Operacao de remocao cancelada!");
                return;
            }

            // Removing from DB the specified entry
            if (table.equals("aluno"))
                aluno_dao.remove(((Aluno)update_obj));
            else if (table.equals("professor"))
                professor_dao.remove(((Professor)update_obj));
            else if (table.equals("curso"))
                curso_dao.remove(((Curso)update_obj));

            System.out.println("Operacao de remocao bem sucedida!");

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.out.println("Nao foi possivel completar operacao de remocao!");
        }

    }

    /**
     * Starts an interective routine on terminal. Tries to find courses by its name and/or by the
     * name of the professor that lectures it (we can serach by part of these field as well). Prints
     * all information of the courses found
     */
    public static void findCursoInfo() {
        try {
            System.out.println("Para busca de cursos, entre os campos de pesquisa abaixo:");
            System.out.println();

            System.out.print(">> Nome do curso: ");
            String nome_curso = System.console().readLine();

            System.out.print(">> Nome do professor ministrante: ");
            String nome_professor = System.console().readLine();


            // Get all courses found on search
            List<CursoInfo> cursos_info = curso_dao.findAllSearch(nome_curso, nome_professor);

            System.out.println();

            // Show information about courses found
            for (CursoInfo curso_info : cursos_info) {

                Curso curso = curso_info.getCurso();
                Professor professor = curso_info.getProfessor();
                List<Aluno> alunos = curso_info.getAlunos();

                System.out.println("-------------------------------------------");

                System.out.println("Nome do curso: " + curso.getNome());
                System.out.println("Horário: " + curso.getHorario());
                System.out.println("Sala: " + curso.getSala());
                System.out.println("Professor: " + professor.getNomeProf());
                System.out.println("Lista de alunos matriculados: [");

                for (Aluno aluno : alunos) {
                    System.out.println("\t" + aluno.getNomeAluno() + ",");
                }
                System.out.println("]");

            }
            System.out.println("-------------------------------------------");
            System.out.println(Integer.toString(cursos_info.size()) + " resultados encontrados");

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.out.println("Nao foi possivel completar operacao de busca de cursos!");
        }
    }

    /**
     * Starts an interective routine on terminal. After providing information to find a specific student, asks for information to search courses (the same way as in method 'findCursoInfo') and tries to subscribe the student to the set of courses found.
     */
    public static void matricularAluno() {

        try {
            Aluno aluno = Misc.findAlunoByConsole();
            if (aluno == null)
                return;

            System.out.println();

            System.out.println("Entre os termos de busca por cursos: ");

            System.out.print(">> Nome do curso: ");
            String nome_curso = System.console().readLine();

            System.out.print(">> Nome do professor ministrante: ");
            String nome_professor = System.console().readLine();
            System.out.println();


            List<CursoInfo> cursos_info = curso_dao.findAllSearch(nome_curso, nome_professor);

            if (cursos_info.size() == 0) {
                System.out.println("Erro: Nenhum curso com os termos de busca introduzidos foram encontrados");
                return;
            }

            System.out.println();

            System.out.println("O aluno '" + aluno.getNomeAluno() + "', com numero de aluno " + Long.toString(aluno.getNroAluno()) + " sera isncrito nos seguintes cursos: [");

            for (CursoInfo curso_info : cursos_info) {
                System.out.println("\t" + curso_info.getCurso().getNome());
            }

            System.out.println("]\n");

            boolean isConfirmed = Misc.isConfirmationConsole("Tem certeza?");
            if (!isConfirmed) {
                System.out.println("Operacao de matricula cancelada!");
                return;
            }

            // Add student subscription to all found courses
            for (CursoInfo curso_info : cursos_info) {
                Curso curso = curso_info.getCurso();

                Matriculado matriculado = new Matriculado();
                matriculado.setNroAluno(aluno.getNroAluno());
                matriculado.setNomeCurso(curso.getNome());

                try {
                    matriculado_dao.add(matriculado);
                } catch (Exception e) {
                    System.err.println("Erro: " + e.getMessage());
                    continue;
                }
            }

            System.out.println("Operacao de matriculas concluida!");

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.out.println("Nao foi possivel completar operacao de busca de matricula!");
        }
    }
}