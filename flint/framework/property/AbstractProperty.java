package flint.framework.property;

// Core Java classes
import java.util.LinkedHashMap;
import java.util.Map;

// 3rd Party classes
//import org.json.simple.JSONValue;

/**
 * Base class for all property types. Must have a name and any attributes
 * @author Philip Bowditch
 */
public abstract class AbstractProperty {
    
    /**
     * Holds the main name by which this AbstractProperty is known by
     */
    protected String m_name;
    
    /**
     * Various attributes on this property e.g.
     * is it read only, nullable etc
     */
    protected Map<String, Object> m_attributes;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the AbstractProperty class
     * @param name The name of the property
     */
    public AbstractProperty( String name ) {
        m_name       = name;
        m_attributes = new LinkedHashMap<>();
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the name of this property
     * @return
     */
    public String getName() {
        return m_name;
    }
    
    /**
     * Sets the name of this property
     * @param name The name to set to
     * @return
     */
    public void setName(String name) {
        m_name = name;
    }
    
    /**
     * Returns the attributes of this property.
     * @return
     */
    public Map<String, Object> getAttributes() {
        return m_attributes;
    }
    
    /**
     * Sets the attributes on this property.
     * @param attribs
     * @return
     */
    public void setAttributes( Map<String, Object> attribs ) {
        m_attributes = attribs;
    }
    
    //--------------------------------------------------------------------------
    
    /*
    private static Map sortByComparator(Map unsortMap) {

        List list = new LinkedList(unsortMap.entrySet());

        // sort list based on comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getKey())
                                       .compareTo(((Map.Entry) (o2)).getKey());
            }
        });

        // put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }*/
    
    //@Override
    //public String toString() {
    //    return JSONValue.toJSONString( m_attributes );
    //}
}