package org.example;

import java.util.ArrayList;
import java.util.List;

public class Company extends OPP{

    public String name;



    public Company(String name)
    {
        super();
        this.name = name;

    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                '}';
    }
}
