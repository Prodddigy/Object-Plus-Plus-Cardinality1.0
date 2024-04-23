package org.example;


import java.io.PrintStream;
import java.io.Serializable;
import java.util.*;

/**
 * The class which simplifies connections between objects. Because it inherits from the ObjectPlus, it also helps with extent management.
 *
 * @author Mariusz Trzaska
 * Fill free to send me any remarks: mtrzaska@pjwstk.edu.pl
 *
 * The code could be improved - see the homework in the lecture.
 */
public abstract class OPP extends OP implements Serializable {

    public static Map<String,Integer> maxCard = new HashMap<>();
    public static Map<String,Integer> minCard = new HashMap<>();


    /**
     *              <emps->comps, 3>
     *
     *
     *
     * Stores information about all connections of this object.
     */
    private Map<String, Map<Object, OPP>> links = new Hashtable<>();

    /**
     * Stores information about all parts connected with any objects.
     */
    private static Set<OPP> allParts = new HashSet<>();

    /**
     * The constructor.
     *
     */
    public OPP() {
        super();
    }

    /**
     * Creates a new link (private, utility method).
     * @param roleName
     * @param reverseRoleName
     * @param targetObject
     * @param qualifier
     * @param counter
     */
    private int addLink(String roleName, String reverseRoleName, OPP targetObject, Object qualifier, int counter,
                        Integer min, Integer max ,Integer revMin, Integer revMax) {
        Map<Object, OPP> objectLinks;
        int flag = 0;

        if(counter < 1) {
            return 0;
        }

        if(links.containsKey(roleName)) {
            objectLinks = links.get(roleName);
        }
        else {
            objectLinks = new HashMap<>();
            links.put(roleName, objectLinks);
        }
        // Check if there is already the connection
        // If yes, then ignore the creation
        if(!objectLinks.containsKey(qualifier)) {

        cardinalityCheckAndSet(roleName,reverseRoleName,min,max,revMin,revMax);

            if(objectLinks.size()+1 <= maxCard.get(roleName+"->"+reverseRoleName) || maxCard.get(roleName+"->"+reverseRoleName) ==0 )
            {
               if(counter ==1)
               {
                   objectLinks.put(qualifier, targetObject);
                   return 0;
               }
                // Add the reverse connection
               flag =  targetObject.addLink(reverseRoleName, roleName, this, this, counter - 1, revMin,  revMax , min,  max);
               if(flag >=0 && counter ==2)
               {
                   objectLinks.put(qualifier, targetObject);
               }
            }
            else
            {
                System.out.println("For "+roleName+"->"+reverseRoleName+" max is: "+ maxCard.get(roleName+"->"+reverseRoleName));
                return -1;
            }

        }
        return 0;
    }
    public void addLink(String roleName, String reverseRoleName, OPP targetObject)
    {
        addLink(roleName, reverseRoleName,targetObject,targetObject,2,null,null,null,null);
    }

    public void cardinalityCheckAndSet(String roleName, String reverseRoleName, Integer min , Integer max, Integer revMin, Integer revMax)
    {
        if(checkNullCard(new Integer[]{max,min,revMax,revMin})) {
            if (!maxCard.containsKey(roleName + "->" + reverseRoleName)) {

                maxCard.put(roleName + "->" + reverseRoleName, max);
            }
            if (!minCard.containsKey(roleName + "->" + reverseRoleName)) {
                minCard.put(roleName + "->" + reverseRoleName, min);
            }
        }
        else
        {
            if(!maxCard.containsKey(roleName + "->" + reverseRoleName))
            {

                throw new IllegalArgumentException("Can't add link when no cardinallity has been selected for: " +
                        ""+roleName + "->" + reverseRoleName);
            }
        }
    }

    public boolean checkNullCard(Integer[] cards)
    {
        int tmp=0;
        for(Integer i : cards)
        {
            if(i ==null || i <0)
            {
                tmp++;
            }
        }
        if(tmp==4)
        {
            return false;
        } else if (tmp ==0)
        {
            return true;
        }
        else
        {
            throw new IllegalArgumentException("cardinalities are either all null or have some kind of value");
        }
    }

    public void setCards(String roleName,String reverseRoleName,Integer min,Integer max)
    {
            maxCard.put(roleName + "->" + reverseRoleName, max);

            minCard.put(roleName + "->" + reverseRoleName, min);
    }


    private int removeLink(String roleName, String reverseRoleName, OPP targetObject, Object qualifier, int counter)
    {
        Map<Object, OPP> objectLinks;
        int flag =0;
        // Protection for the reverse connection
        if(counter < 1) {
            return 0;
        }

        // Find a collection of links for the role
        if(links.containsKey(roleName)) {
            // Get the links
            objectLinks = links.get(roleName);//
        }
        else {
            // no role no association no need to remove anything
           return 0;
        }

        if(objectLinks.containsKey(qualifier))
        {
            if(objectLinks.size()-1 >= minCard.get(roleName+"->"+reverseRoleName) )
            {
                if(counter ==1)
                {
                    objectLinks.remove(qualifier, targetObject);
                    return 0;
                }
                // Add the reverse connection
                flag =  targetObject.removeLink(reverseRoleName, roleName, this, this, counter - 1);
                if(flag >=0 && counter ==2)
                {
                    objectLinks.remove(qualifier, targetObject);

                }
            }
            else {
                System.out.println("For "+roleName+"->"+reverseRoleName+" min is: "+ minCard.get(roleName+"->"+reverseRoleName));
                return -1;
            }
        }
        return 0;}
    public void removeLink(String roleName, String reverseRoleName, OPP targetObject) {
        removeLink(roleName,reverseRoleName,targetObject,targetObject);
    }

    public void removeLink(String roleName, String reverseRoleName, OPP targetObject, Object qualifier) {
        removeLink(roleName, reverseRoleName, targetObject, qualifier, 2);
    }

    /**
     * Creates a new link to the given target object (optionally as quilified connection).
     * @param roleName
     * @param reverseRoleName
     * @param targetObject
     * @param qualifier Jezeli rozny od null to tworzona jest asocjacja kwalifikowana.
     */
    public void addLink(String roleName, String reverseRoleName, OPP targetObject, Object qualifier,
                        Integer min, Integer max ,Integer revMin, Integer revMax) {
        addLink(roleName, reverseRoleName, targetObject, qualifier, 2, min,  max , revMin,  revMax);
    }

    /**
     * Creates a new link to the given target object (as an ordinary association, not the quilified one).
     * @param roleName
     * @param reverseRoleName
     * @param targetObject
     */

    public void addLink(String roleName, String reverseRoleName, OPP targetObject,Integer min, Integer max ,Integer revMin, Integer revMax) {
        addLink(roleName, reverseRoleName, targetObject, targetObject, min,  max , revMin,  revMax);
    }



    /**
     * Adds an information about a connection (using a "semi" composition).
     * @param roleName
     * @param reverseRoleName
     * @throws Exception
     */
    public void addPart(String roleName, String reverseRoleName, OPP partObject,Integer min,Integer max ,Integer revMin,Integer  revMax) throws Exception {
        // Check if the part exist somewhere
        if(allParts.contains(partObject)) {
            throw new Exception("The part is already connected to a whole!");
        }

        addLink(roleName, reverseRoleName, partObject, min,  max , revMin,  revMax);//może bez cardinality?

        // Store adding the object as a part
        allParts.add(partObject);
    }

    /**
     * Gets an array of connected objects for the given role name.
     * @param roleName
     * @return
     * @throws Exception
     */
    public OPP[] getLinks(String roleName) throws Exception {
        Map<Object, OPP> objectLinks;

        if(!links.containsKey(roleName)) {
            // No links for the role
            throw new Exception("No links for the role: " + roleName);
        }

        objectLinks = links.get(roleName);

        return (OPP[]) objectLinks.values().toArray(new OPP[0]);
    }

    /**
     * Shows links to the given stream.
     * @param roleName
     * @param stream
     * @throws Exception
     */
    public void showLinks(String roleName, PrintStream stream) throws Exception {
        Map<Object, OPP> objectLinks;

        if(!links.containsKey(roleName)) {
            // No links
            throw new Exception("No links for the role: " + roleName);
        }

        objectLinks = links.get(roleName);

        Collection col = objectLinks.values();

        stream.println(this.getClass().getSimpleName() + " links, role '" + roleName + "':");

        for(Object obj : col) {
            stream.println("   " + obj);
        }
    }

    /**
     * Gets an object for the given qualifier (a qualified association).
     * @param roleName
     * @param qualifier
     * @return
     * @throws Exception
     */
    public OPP getLinkedObject(String roleName, Object qualifier) throws Exception {
        Map<Object, OPP> objectLinks;

        if(!links.containsKey(roleName)) {
            // No links
            throw new Exception("No links for the role: " + roleName);
        }

        objectLinks = links.get(roleName);
        if(!objectLinks.containsKey(qualifier)) {
            // No link for the qualifer
            throw new Exception("No link for the qualifer: " + qualifier);
        }

        return objectLinks.get(qualifier);
    }

    /**
     * Checks if there are any links for the given role name.
     * @param nazwaRoli
     * @return
     */
    public boolean anyLink(String nazwaRoli) {
        if(!links.containsKey(nazwaRoli)) {
            return false;
        }

        Map<Object, OPP> links = this.links.get(nazwaRoli);
        return links.size() > 0;
    }

    /**
     * Checks if there is a link to a given object as a given role.
     * @param roleName
     * @param targetObject
     * @return
     */
    public boolean isLink(String roleName, OPP targetObject) {
        Map<Object, OPP> objectLink;

        if(!links.containsKey(roleName)) {
            // No links for the role
            return false;
        }

        objectLink = links.get(roleName);

        return objectLink.containsValue(targetObject);
    }
}
