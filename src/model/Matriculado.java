package model;

import java.util.Objects;
import java.util.Properties;
import java.util.LinkedHashMap;

public class Matriculado implements GenericObjInterface {

    // Attributes from database
    private Long nroAluno;
    private String nomeCurso;

    // HashMap of attribute and key attributes. Key = Attribute name, Value = Atrribute description
    public static final LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();
    public static final LinkedHashMap<String, String> primary_key_attributes = new LinkedHashMap<String, String>();

    static {
        attributes.put("nroAluno", "Numero do aluno");
        attributes.put("nomeCurso", "Nome do curso");

        primary_key_attributes.put("nroAluno", (String) attributes.get("nroAluno"));
        primary_key_attributes.put("nomeCurso", (String) attributes.get("nomeCurso"));
    }

    // From string, try inserting into the object attribute (not a string)
    private void trySetNroAluno(String val) {
        if (val == null || val.equals("")) {
            this.setNroAluno(null);
        } else {
            try {
                this.setNroAluno(Long.parseLong(val));
            } catch(NumberFormatException e) {
                throw new NumberFormatException("Atributo 'nroAluno' (" + attributes.get("nroAluno") + ") igual a '" + val + "' nao e um valor inteiro");
            }
        }
    }

    public Matriculado () {}

    // Building a Matriculado object from Properties
    public Matriculado (Properties props) {

        props = GenericObj.parseUserInputProperties(props);

        this.trySetNroAluno(props.getProperty("nroAluno"));
        this.setNomeCurso(GenericObj.emptyFieldToNull(props.getProperty("nomeProf")));
    }

    @Override
    // Calling getters by the name of the attribute
    public Object genericGet(String attribute) {
        switch (attribute) {
            case "nroAluno":
                return this.getNroAluno();
            case "nomeCurso":
                return this.getNomeCurso();
            default:
                throw new IllegalArgumentException("No attribute " + attribute);
        }
    }

    @Override
    // Calling setters by the name of the attribute
    public void genericSet(String attribute, String val) {

        val = GenericObj.parseUserInputString(val);
        switch (attribute) {
            case "idProf":
                this.trySetNroAluno(val);
                break;
            case "nomeCurso":
                this.setNomeCurso(val);
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

    public Long getNroAluno() {
        return this.nroAluno;
    }

    public void setNroAluno(Long nroAluno) {
        this.nroAluno = nroAluno;
    }

    public String getNomeCurso() {
        return this.nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Matriculado)) {
            return false;
        }
        Matriculado matriculado = (Matriculado) o;
        return Objects.equals(nroAluno, matriculado.nroAluno) && Objects.equals(nomeCurso, matriculado.nomeCurso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nroAluno, nomeCurso);
    }

    @Override
    public String toString() {
        return "{" +
            " nroAluno='" + getNroAluno() + "'" +
            ", nomeCurso='" + getNomeCurso() + "'" +
            "}";
    }

}