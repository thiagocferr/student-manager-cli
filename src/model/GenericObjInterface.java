package model;

import java.util.LinkedHashMap;

// Interface to allow getting and setting given a string with the name of the table/model object
public interface GenericObjInterface {
    Object genericGet(String attribute);
    void genericSet(String attribute, String val);
}