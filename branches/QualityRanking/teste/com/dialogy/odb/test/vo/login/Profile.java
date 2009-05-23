package com.dialogy.odb.test.vo.login;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private String name;

    private List<Function> functions;

    public Profile() {
    }

    public Profile(String name){
    	super();
    	this.name = name;
    }
    public Profile(String name, List<Function> functions) {
        super();
        this.functions = functions;
        this.name = name;
    }

    public Profile(String name, Function function) {
        super();
        this.functions = new ArrayList<Function>();
        this.functions.add(function);
        this.name = name;
    }

    public void addFunction(Function function) {
        if(functions==null){
            functions = new ArrayList<Function>();
        }
        functions.add(function);
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name + " - " + functions.toString();
    }

}
