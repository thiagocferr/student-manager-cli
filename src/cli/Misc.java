package cli;

import java.sql.SQLException;

import java.util.LinkedHashMap;
import java.util.Properties;

import model.GenericObj;
import db.AlunoDAO;
import model.Aluno;


public class Misc {

    private static final AlunoDAO aluno_dao = RegistrationSystem.getAlunoDAO();

    //! param 'mode': 0 = Todos atributos, 1 = Atributos que são chaves primárias, 2 = Atributos que não sao chaves primárias
    /**
     * Get atributes for the specified table from user input (terminal)
     * @param table - String representing the table where we are insert into
     * @param mode - If 0, get all atributes from table. If 1, all primary key attributes. If 2, all
     * non-primary-key attributes
     * @return Properties - Map between strings, where key = DB column name (or the object
     * attribute) ad val = attribute description
     */
    private static Properties getAtributesFromInput(String table,  int mode) {

        LinkedHashMap<String, String> attr = null;

        if (mode == 0)
            attr = GenericObj.getAllAttributesFrom(table);
        else if (mode == 1)
            attr = GenericObj.getKeyAttributesFrom(table);
        else if (mode == 2)
            attr = GenericObj.getNonKeyAttributesFrom(table);
        else
            throw new IllegalArgumentException("Argumento 'mode' so pode ser 0 (todos atributos), 1 (atributos-chave) ou 2 (atributos nao-chave)");

        Properties input_values = new Properties();

        attr.entrySet().forEach(entry -> {
            String db_field = entry.getKey();
            String description = entry.getValue();

            System.out.print(">> " + description + ": ");
            String input = System.console().readLine();

            input_values.setProperty(db_field, input);
        });

        return input_values;
    }

    /**
     * Get all atributes for the specified table from user input (terminal)
     * @param table - String representing the table where we are insert into
     * @return Properties - Map between strings, where key = DB column name (or the object
     * attribute) ad val = attribute description
     */
    protected static Properties getAllAtributesFromInput(String table) {
        try {
            return getAtributesFromInput(table, 0);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get all primary key atributes for the specified table from user input (terminal)
     * @param table - String representing the table where we are insert into
     * @return Properties - Map between strings, where key = DB column name (or the object
     * attribute) ad val = attribute description
     */
    protected static Properties getKeyAtributesFromInput(String table) {
        try {
            return getAtributesFromInput(table, 1);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * From user input on terminal, find a student
     * @return Aluno - The found student object
     */
    protected static Aluno findAlunoByConsole() throws SQLException {

        System.out.println("Selecione um aluno");
        System.out.println();

        System.out.print(">> Numero do aluno: ");

        String numero_aluno = System.console().readLine();
        Long nroAluno = Long.parseLong(numero_aluno);

        System.out.println("Procurando aluno na base de dados...");
        Aluno aluno = aluno_dao.find(nroAluno);

        if (aluno == null)
            System.out.println("Error: Aluno de numero " + Long.toString(nroAluno) + " nao foi encontrado!");
        else
            System.out.println("Aluno encontrado!");

        return aluno;
    }


    /**
     * Generic confirmation method. Loops while user input from 's' or 'n'
     * @param message - Message to be displayed while waiting for confirmation
     * @return boolean - If user confirmed
     */
    protected static boolean isConfirmationConsole(String message) {
        String confirmation_string = "";

        while (!(confirmation_string.equals("s") || confirmation_string.equals("n"))) {
            System.out.print(">> " + message + " (s/n) ");
            confirmation_string = System.console().readLine().toLowerCase();
        }

        if (confirmation_string.equals("n"))
            return false;
        else
            return true;
    }
}