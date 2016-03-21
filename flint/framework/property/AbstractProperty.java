package flint.framework.property;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

/**
 *
 * @author Philip Bowditch
 */
public abstract class AbstractProperty {
    
    /**
     * Holds the main name by which this AbstractProperty is known by
     */
    protected String m_name;
    
    protected Map<String, Object> m_attributes;
    
    //--------------------------------------------------------------------------
    
    public AbstractProperty( String name ) {
        m_name       = name;
        m_attributes = new LinkedHashMap<String, Object>();
    }
    
    //--------------------------------------------------------------------------
    
    public String getName() {
        return m_name;
    }
    
    public void setName(String name) {
        m_name = name;
    }
    
    public Map<String, Object> getAttributes() {
        return m_attributes;
    }
    
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
    
    @Override
    public String toString() {
        return JSONValue.toJSONString( m_attributes );
    }
}