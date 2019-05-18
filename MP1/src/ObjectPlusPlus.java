import java.io.PrintStream;
import java.io.Serializable;
import java.util.*;

public class ObjectPlusPlus extends ObjectPlus implements Serializable {

    /**
     * Stores information about all parts connected with any objects.
     */
    private static Set<ObjectPlusPlus> allParts = new HashSet<>();
    /**
     * Stores information about all connections of this object.
     */
    private Map<String, Map<Object, ObjectPlusPlus>> links = new Hashtable<>();

    /**
     * The constructor.
     */
    public ObjectPlusPlus() {
        super();
    }

    private void addLink(String roleName, String reverseRoleName, ObjectPlusPlus targetObject, Object qualifier, int counter) {
        Map<Object, ObjectPlusPlus> objectLinks;
        // Protection for the reverse connection
        if (counter < 1) {
            return;
        }
        // Find a collection of links for the role
        if (links.containsKey(roleName)) {
            // Get the links
            objectLinks = links.get(roleName);
        } else {
            // No links ==> create them
            objectLinks = new HashMap<>();
            links.put(roleName, objectLinks);
        }
        // Check if there is already the connection
        // If yes, then ignore the creation
        if (!objectLinks.containsKey(qualifier)) {
            // Add a link for the target object
            objectLinks.put(qualifier, targetObject);
            // Add the reverse connection
            targetObject.addLink(reverseRoleName, roleName, this, qualifier, counter - 1);
        }
    }

    public void addLink(String roleName, String reverseRoleName, ObjectPlusPlus targetObject, Object qualifier) {
        addLink(roleName, reverseRoleName, targetObject, qualifier, 2);
    }

    protected void addLink(String roleName, String reverseRoleName, ObjectPlusPlus targetObject) {
        addLink(roleName, reverseRoleName, targetObject, targetObject);
    }

    protected void addPart(String roleName, String reverseRoleName, ObjectPlusPlus partObject) throws Exception {
// Check if the part exist somewhere
        if (allParts.contains(partObject)) {
            throw new Exception("The part is already connected to a whole!");
        }
        addLink(roleName, reverseRoleName, partObject);
// Store adding the object as a part
        allParts.add(partObject);
    }

    public ObjectPlusPlus[] getLinks(String roleName) throws RuntimeException {
        Map<Object, ObjectPlusPlus> objectLinks;
        if (!links.containsKey(roleName)) {
// No links for the role
            throw new RuntimeException("No links for the role: " + roleName);
        }
        objectLinks = links.get(roleName);
        return (ObjectPlusPlus[]) objectLinks.values().toArray(new ObjectPlusPlus[0]);
    }

    public void showLinks(String roleName, PrintStream stream) throws RuntimeException {
        Map<Object, ObjectPlusPlus> objectLinks;
        if (!links.containsKey(roleName)) {
// No links
            throw new RuntimeException("No links for the role: " + roleName);
        }
        objectLinks = links.get(roleName);
        Collection<ObjectPlusPlus> col = objectLinks.values();
        stream.println(this.getClass().getSimpleName() + " links, role '" + roleName + "':");
        for (ObjectPlusPlus obj : col) {
            stream.println(" " + obj);
        }
    }

    public ObjectPlusPlus getLinkedObject(String roleName, Object qualifier) throws Exception {
        Map<Object, ObjectPlusPlus> objectLinks;
        if (!links.containsKey(roleName)) {
// No links
            throw new Exception("No links for the role: " + roleName);
        }
        objectLinks = links.get(roleName);
        if (!objectLinks.containsKey(qualifier)) {
// No link for the qualifer
            throw new Exception("No link for the qualifer: " + qualifier);
        }
        return objectLinks.get(qualifier);
    }

    @Override
    public void destroyObject() {
        //usunięcie z ekstensji
        super.destroyObject();
        //ma usuwać relacje również z globalnej listy

        //muszę usunąć powiązania do obiektu - poszukać wszystkich relacji do tego obiektu - w obie strony i wyrzucic
        //Map<Object, ObjectPlusPlus> objectsLinks = links.

    }

}