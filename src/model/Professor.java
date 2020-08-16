package model;

import java.util.Objects;
import java.util.Properties;
import java.util.LinkedHashMap;

public class Professor implements GenericObjInterface {

    // Attributes from database
    private Long idProf;
    private String nomeProf;
    private Long idDepto;

    // HashMap of attribute and key attributes. Key = Attribute name, Value = Atrribute description
    public static final LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();
    public static final LinkedHashMap<String, String> primary_key_attributes = new LinkedHashMap<String, String>();

    static {
        attributes.put("idProf", "Id de Professor");
        attributes.put("nomeProf", "Nome");
        attributes.put("idDepto", "Id do departamento");

        primary_key_attributes.put("idProf", (String) attributes.get("idProf"));
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

    // From string, try inserting into the object attribute (not a string)
    private void trySetIdDepto(String val) {
        if (val == null || val.equals("")) {
            this.setIdDepto(null);
        } else {
            try {
                this.setIdDepto(Long.parseLong(val));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Atributo 'idDepto' (" + attributes.get("idDepto") + ") igual a '" + val + "' nao e um valor inteiro");
            }
        }
    }

    public Professor() {}

    // Building a Professor object from Properties
    public Professor(Properties props) {

        props = GenericObj.parseUserInputProperties(props);

        this.trySetIdProf(props.getProperty("idProf"));
        this.setNomeProf(GenericObj.emptyFieldToNull(props.getProperty("nomeProf")));
        this.trySetIdDepto(props.getProperty("idDepto"));
    }

    @Override
    // Calling getters by the name of the attribute
    public Object genericGet(String attribute) {
        switch (attribute) {
            case "idProf":
                return this.getIdProf();
            case "nomeProf":
                return this.getNomeProf();
            case "idDepto":
                return this.getIdDepto();
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
                this.trySetIdProf(val);
                break;
            case "nomeProf":
                this.setNomeProf(val);
                break;
            case "idDepto":
                this.trySetIdDepto(val);
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

    public Long getIdProf() {
        return this.idProf;
    }

    public void setIdProf(Long idProf) {
        this.idProf = idProf;
    }

    public String getNomeProf() {
        return this.nomeProf;
    }

    public void setNomeProf(String nomeProf) {
        this.nomeProf = nomeProf;
    }

    public Long getIdDepto() {
        return this.idDepto;
    }

    public void setIdDepto(Long idDepto) {
        this.idDepto = idDepto;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Professor)) {
            return false;
        }
        Professor professor = (Professor) o;
        return Objects.equals(idProf, professor.idProf) && Objects.equals(nomeProf, professor.nomeProf) && Objects.equals(idDepto, professor.idDepto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProf, nomeProf, idDepto);
    }

    @Override
    public String toString() {
        return "{" +
            " idProf='" + getIdProf() + "'" +
            ", nomeProf='" + getNomeProf() + "'" +
            ", idDepto='" + getIdDepto() + "'" +
            "}";
    }

}