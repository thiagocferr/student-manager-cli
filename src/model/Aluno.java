/* Exemplo adaptado de:  http://www.caelum.com.br/apostila-java-web/bancos-de-dados-e-jdbc/  */

package model;

import java.util.Objects;
import java.util.Properties;
import java.util.LinkedHashMap;

public class Aluno implements GenericObjInterface
{
    // Attributes from database
    private Long nroAluno;
    private String nomeAluno;
    private String formacao;
    private String nivel;
    private Integer idade;

    // HashMap of attribute and key attributes. Key = Attribute name, Value = Atrribute description
    private static final LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();
    private static final LinkedHashMap<String, String> primary_key_attributes = new LinkedHashMap<String, String>();

    static {
        attributes.put("nroAluno", "Numero de Aluno");
        attributes.put("nomeAluno", "Nome");
        attributes.put("formacao", "Formacao");
        attributes.put("nivel", "Nivel");
        attributes.put("idade", "Idade");

        primary_key_attributes.put("nroAluno", (String) attributes.get("nroAluno"));
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

    // From string, try inserting into the object attribute (not a string)
    private void trySetIdade(String val) {
        if (val == null || val.equals("")) {
            this.setIdade(null);
        } else {
            try {
                this.setIdade(Integer.parseInt(val));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Atributo 'idade' (" + attributes.get("idade") + ") igual a '" + val + "' nao e um valor inteiro");
            }
        }
    }

    public Aluno() {}

    // Building an Aluno object from Properties
    // This is suposed to only be run for inserting into the database
    public Aluno(Properties props) {
        props = GenericObj.parseUserInputProperties(props);

        this.trySetNroAluno(props.getProperty("nroAluno"));

        // Property value cannot be null, so after getting it, if it is empty string, convert to null
        this.setNomeAluno(GenericObj.emptyFieldToNull(props.getProperty("nomeAluno")));
        this.setFormacao(GenericObj.emptyFieldToNull(props.getProperty("formacao")));
        this.setFormacao(GenericObj.emptyFieldToNull(props.getProperty("nivel")));

        this.trySetIdade(props.getProperty("idade"));

    }


    @Override
    // Calling getters by the name of the attribute
    public Object genericGet(String attribute) {
        switch (attribute) {
            case "nroAluno":
                return getNroAluno();
            case "nomeAluno":
                return getNomeAluno();
            case "formacao":
                return getFormacao();
            case "nivel":
                return getNivel();
            case "idade":
                return getIdade();
            default:
                throw new IllegalArgumentException("No attribute " + attribute);
        }
    }

    @Override
    // Calling setters by the name of the attribute
    public void genericSet(String attribute, String val) {

        val = GenericObj.parseUserInputString(val);
        switch (attribute) {
            case "nroAluno":
                this.trySetNroAluno(val);
                break;
            case "nomeAluno":
                this.setNomeAluno(val);
                break;
            case "formacao":
                this.setFormacao(val);
                break;
            case "nivel":
                this.setNivel(val);
                break;
            case "idade":
                this.trySetIdade(val);
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

    public String getNomeAluno() {
        return this.nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getFormacao() {
        return this.formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public String getNivel() {
        return this.nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Integer getIdade() {
        return this.idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Aluno)) {
            return false;
        }
        Aluno aluno = (Aluno) o;
        return Objects.equals(nroAluno, aluno.nroAluno) && Objects.equals(nomeAluno, aluno.nomeAluno) && Objects.equals(formacao, aluno.formacao) && Objects.equals(nivel, aluno.nivel) && Objects.equals(idade, aluno.idade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nroAluno, nomeAluno, formacao, nivel, idade);
    }

    @Override
    public String toString() {
        return "{" +
            " nroAluno='" + getNroAluno() + "'" +
            ", nomeAluno='" + getNomeAluno() + "'" +
            ", formacao='" + getFormacao() + "'" +
            ", nivel='" + getNivel() + "'" +
            ", idade='" + getIdade() + "'" +
            "}";
    }
}