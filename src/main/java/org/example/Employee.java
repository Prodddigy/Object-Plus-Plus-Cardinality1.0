package org.example;

import java.util.ArrayList;
import java.util.List;

public class Employee extends OPP{

    public String name;

    public Company company;

    public Employee(String name)
    {
        super();
        this.name = name;

    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                '}';
    }
}
