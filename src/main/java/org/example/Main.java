package org.example;

public class Main {
    public static void main(String[] args) throws Exception {

        Employee e = new Employee("Kyle");
        Employee e2 = new Employee("Cyle");
        Employee e3 = new Employee("Byle");

        Company c = new Company("Microsoft");

        OP.showExtent(Employee.class);
        OP.showExtent(Company.class);

        //c.addLink("emps","comps",e,3,4); // company has max of 4 emps and min of 3 emps

        c.addLink("emps","comps",e,2,6,0,3);
        //comp may have from 2 to 6 emps, while emps may have 0 or up to 3


        c.addLink("emps","comps",e2);
        c.addLink("emps","comps",e3);

        c.showLinks("emps",System.out);

        e.showLinks("comps",System.out);

        c.removeLink("emps","comps",e3);

        System.out.println("----------");

        c.showLinks("emps",System.out);

        e.showLinks("comps",System.out);

        c.addLink("emps","comps",e3);
        System.out.println("----------");

        c.showLinks("emps",System.out);

        e.showLinks("comps",System.out);

        c.setCards("emps","comps",2,6);


        System.out.println(OPP.maxCard.get("emps->comps"));
        System.out.println(OPP.minCard.get("emps->comps"));
        System.out.println(OPP.maxCard.get("comps->emps"));
        System.out.println(OPP.minCard.get("comps->emps"));


        c.setCards("emps","comps",3,6);


        c.addLink("emps","comps",e3);


    }
}