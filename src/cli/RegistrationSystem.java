package cli;

import cli.*;

import db.AlunoDAO;
import db.ProfessorDAO;
import db.CursoDAO;
import db.MatriculadoDAO;

// Entrypoint class
public class RegistrationSystem {

    // Loading DAO connections from the beggining
    private static final AlunoDAO aluno_dao = new AlunoDAO();
    private static final ProfessorDAO professor_dao = new ProfessorDAO();
    private static final CursoDAO curso_dao = new CursoDAO();
    private static final MatriculadoDAO matriculado_dao = new MatriculadoDAO();

    protected static AlunoDAO getAlunoDAO() {
        return aluno_dao;
    }
    protected static ProfessorDAO getProfessorDAO() {
        return professor_dao;
    }
    protected static CursoDAO getCursoDAO() {
        return curso_dao;
    }
    protected static MatriculadoDAO getMatriculadoDAO() {
        return matriculado_dao;
    }

    private static String parseTableName(String input_table_name) {
        return input_table_name.toLowerCase().trim().replaceAll(" +", " ");

    }

    public static void main(String[] args) {

        System.out.println();
        System.out.println("---------- Sistema de cadastro ------------");

        // Main loop
        while (true) {
            System.out.print("> ");

            String input = System.console().readLine();
            String[] split_input = input.split("\\s+");

            // Insertion operation
            if (split_input[0].equals("inserir")) {
                Operations.insertInto(parseTableName(split_input[1]));
            }
            // Update operation
            else if (split_input[0].equals("alterar")) {
                Operations.update(parseTableName(split_input[1]));
            }
            // Remove operation
            else if (split_input[0].equals("remover")) {
                Operations.remove(parseTableName(split_input[1]));
            }
            // Course search operation (by certain keywords)
            else if (input.equals("procurar curso")) {
                Operations.findCursoInfo();
            }
            // Course subcription operation
            else if (input.equals("matricular aluno")) {
                Operations.matricularAluno();
            }
            // Exit operation
            else if (input.equals("sair")){

                try {
                    aluno_dao.endConnection();
                    professor_dao.endConnection();
                    curso_dao.endConnection();
                    matriculado_dao.endConnection();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }

                return;
            }
            // Command not found operation
            else {
                System.out.println("Operacao '" + input + "' nao encontrada");
            }
        }
    }
}