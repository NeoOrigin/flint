package flint.framework.type;

// Core Java classes
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import flint.framework.property.AbstractProperty;
import flint.framework.property.CoreProperty;

/**
 *
 * @author Philip Bowditch
 */
public class TypeDefinition {
    
    /**
     * Holds all the attribute definitions
     */
    protected Map<String, AbstractProperty> m_properties;
    
    //--------------------------------------------------------------------------
    
    public TypeDefinition() {
        m_properties = new LinkedHashMap<String, AbstractProperty>();
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Creates a TypeInstance based off this definition
     * 
     * @param overrides - Instance specific overrides to the base definition attributes
     * @return The new TypeInstance
     */
    public TypeInstance newInstance( Properties overrides ) {
        TypeInstance t = new TypeInstance( this );
        t.setOverrides(overrides);
        
        return t;
    }
    
    //--------------------------------------------------------------------------

    public Map<String, AbstractProperty> getProperties() {
        return m_properties;
    }
    
    public void setProperties( Map<String, AbstractProperty> properties ) {
        m_properties = properties;
    }
    
    public static Map<String, String> expandProperties( Map<String, AbstractProperty> properties ) {
        HashMap<String, String> newDefs = new LinkedHashMap<String, String>();
        Iterator it  = properties.entrySet().iterator();
        String   val = null;
        
        while ( it.hasNext() ) {
            Map.Entry entry = (Map.Entry )it.next();
            String           name  = (String)entry.getKey();
            AbstractProperty value = (AbstractProperty)entry.getValue();
            
            if ( name.startsWith( "." )) {
                name = name.split( ".", 2 )[ 1 ];
            }
            
            if ( value instanceof CoreProperty ) {
                val = ((CoreProperty)value).getValue();
                newDefs.put( name, val );
            }
            else {
                Map<String, Object> atts = value.getAttributes();
                Iterator            it2  = atts.entrySet().iterator();
                
                while ( it2.hasNext() ) {
                    Map.Entry entry2 = (Map.Entry)it2.next();
                    
                    String key = (String)entry2.getKey();
                    val        = (String)entry2.getValue();
                    
                    if ( !( (String)entry2.getKey()).isEmpty() ) {
                        key = name + "." + key;
                    }
                    
                    newDefs.put( key + "", val );
                }
            }
            
        }
        
        return newDefs;
    }
    
    @Override
    public String toString() {
        
        Iterator it     = m_properties.entrySet().iterator();
        StringBuilder b = new StringBuilder();
        
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            
            b.append( pairs.getKey() );
            b.append( " = " );
            b.append( pairs.getValue() );
            b.append( "\n" );
        }
        
        return b.toString();
    }
}