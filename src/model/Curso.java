package model;

import java.util.Objects;
import java.util.Properties;
import java.util.LinkedHashMap;

public class Curso implements GenericObjInterface {

    // Attributes from database
    private String nome;
    private String horario;
    private String sala;
    private Long idProf;

    // HashMap of attribute and key attributes. Key = Attribute name, Value = Atrribute description
    public static final LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();
    public static final LinkedHashMap<String, String> primary_key_attributes = new LinkedHashMap<String, String>();

    static {
        attributes.put("nome", "Nome");
        attributes.put("horario", "Horario");
        attributes.put("sala", "Sala");
        attributes.put("idProf", "Id do professor");

        primary_key_attributes.put("nome", (String) attributes.get("nome"));
    }

    // From string, try inserting into the object attribute (not a string)
    private void trySetIdProf(String val) {
        if (val == null || val.equals("")) {
            this.setIdProf(null);
        } else {
            try {
                this.setIdProf(Long.parseLong(val));
            } catch(NumberFormatException e) {
                throw new NumberFormatException("Atributo 'idProf' (" + attributes.get("idProf") + ") igual a '" + val + "' nao e um valor inteiro");
            }
        }
    }

    public Curso () {}

    // Building a Curso object from Properties
    public Curso (Properties props) {

        props = GenericObj.parseUserInputProperties(props);

        this.setNome(GenericObj.emptyFieldToNull(props.getProperty("nome")));
        this.setHorario(GenericObj.emptyFieldToNull(props.getProperty("horario")));
        this.setHorario(GenericObj.emptyFieldToNull(props.getProperty("sala")));

        this.trySetIdProf(props.getProperty("idProf"));

    }

    @Override
    // Calling getters by the name of the attribute
    public Object genericGet(String attribute) {
        switch (attribute) {
            case "nome":
                return this.getNome();
            case "horario":
                return this.getHorario();
            case "sala":
                return this.getSala();
            case "idProf":
                return this.getIdProf();
            default:
                throw new IllegalArgumentException("No attribute " + attribute);
        }
    }

    @Override
    // Calling setters by the name of the attribute
    public void genericSet(String attribute, String val) {

        val = GenericObj.parseUserInputString(val);
        switch (attribute) {
            // Primary key - Shall not be null
            case "nome":
                this.setNome(val);
                break;
            case "horario":
                this.setHorario(val);
                break;
            case "sala":
                this.setSala(val);
                break;
            case "idProf":
                this.trySetIdProf(val);
                break;
            default:
                throw new IllegalArgumentException("No attribute " + attribute);
        }
    }

    public static LinkedHashMap<String, String> getAllAttributes() {
        return attributes;
    }

    public static LinkedHashMap<String, String> getAllKeyAttributes() {
        return primary_key_attributes;
    }

    public static LinkedHashMap<String, String> getNonKeyAttributes() {
        LinkedHashMap<String, String> non_key_attributes = new LinkedHashMap<String, String>();

        attributes.entrySet().forEach(entry -> {
            String attribute = entry.getKey();
            String description = entry.getValue();

            // This attribute is not a primary key
            if (primary_key_attributes.get(attribute) == null)
                non_key_attributes.put(attribute,description);
        });

        return non_key_attributes;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHorario() {
        return this.horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getSala() {
        return this.sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public Long getIdProf() {
        return this.idProf;
    }

    public void setIdProf(Long idProf) {
        this.idProf = idProf;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Curso)) {
            return false;
        }
        Curso curso = (Curso) o;
        return Objects.equals(nome, curso.nome) && Objects.equals(horario, curso.horario) && Objects.equals(sala, curso.sala) && Objects.equals(idProf, curso.idProf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, horario, sala, idProf);
    }

    @Override
    public String toString() {
        return "{" +
            " nome='" + getNome() + "'" +
            ", horario='" + getHorario() + "'" +
            ", sala='" + getSala() + "'" +
            ", idProf='" + getIdProf() + "'" +
            "}";
    }

}