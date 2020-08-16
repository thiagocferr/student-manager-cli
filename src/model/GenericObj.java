package model;

import java.util.LinkedHashMap;
import java.util.Properties;

// Static class that implements useful functions for models and for users that want to acess models
// in a general way
public class GenericObj {

    /**
     * Parse properties
     * @param props - A Properties object
     * @return Properties - Properties with parsed value (parsed = trimmed and removed extra
     * whitespaces)
     */
    public static Properties parseUserInputProperties(Properties props) {
        Properties parsed_props = new Properties();

        props.forEach((key, val) -> {
            String parsed_val = (String) val;

            if (!val.equals("")) {
                parsed_val = parseUserInputString((String) val);
            }

            parsed_props.setProperty((String) key, parsed_val);
        });

        return parsed_props;
    }

    /**
     * Parse a string
     * @param input - A String
     * @return String - String with parsed value (parsed = trimmed and removed extra whitespaces)
     */
    public static String parseUserInputString(String input) {
        if (input == null) {
            return null;
        }

        String parsed_val = input.trim().replaceAll(" +", " ");

        if (parsed_val.equals(""))
            parsed_val = null;

        return parsed_val;
    }

    /**
     * Get all atributes from a specific model
     * @param table - The table name that has a specific model object counterpart
     * @return LinkedHashMap<String, String> - Key = Attribute name, Val = Attribute description
     */
    public static LinkedHashMap<String, String> getAllAttributesFrom(String table) {
        switch (table.toLowerCase()) {
            case "aluno":
                return Aluno.getAllAttributes();
            case "professor":
                return Professor.getAllAttributes();
            case "curso":
                return Curso.getAllAttributes();
            case "matriculado":
                return Matriculado.getAllAttributes();
            default:
                throw new UnsupportedOperationException("Classe " + table + " nao consta na classe GenericObj");
        }
    }

    /**
     * Get all primary key atributes from a specific model
     * @param table - The table name that has a specific model object counterpart
     * @return LinkedHashMap<String, String> - Key = Attribute name, Val = Attribute description
     */
    public static LinkedHashMap<String, String> getKeyAttributesFrom(String table) {
        switch (table.toLowerCase()) {
            case "aluno":
                return Aluno.getAllKeyAttributes();
            case "professor":
                return Professor.getAllKeyAttributes();
            case "curso":
                return Curso.getAllKeyAttributes();
            case "matriculado":
                return Matriculado.getAllKeyAttributes();
            default:
                throw new UnsupportedOperationException("Classe " + table + " nao consta na classe GenericObj");
        }
    }

    /**
     * Get all non-primary-key atributes from a specific model
     * @param table - The table name that has a specific model object counterpart
     * @return LinkedHashMap<String, String> - Key = Attribute name, Val = Attribute description
     */
    public static LinkedHashMap<String, String> getNonKeyAttributesFrom(String table) {
        switch (table.toLowerCase()) {
            case "aluno":
                return Aluno.getNonKeyAttributes();
            case "professor":
                return Professor.getNonKeyAttributes();
            case "curso":
                return Curso.getNonKeyAttributes();
            case "matriculado":
                return Matriculado.getNonKeyAttributes();
            default:
                throw new UnsupportedOperationException("Classe " + table + " nao consta na classe GenericObj");
        }
    }

    public static String emptyFieldToNull(String str) {
        if (str.equals("")) {
            return null;
        } else {
            return str;
        }
    }

}