package flint.engine;

// Core Java classes
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Used to pass key value pairs to and from processes / tasks. Where the values themselves are often lists such as lines of stdout
 * @author Philip Bowditch
 */
public abstract class InvokationBase {
    
    /**
     * Holds the data, key value pairs where the value is a possible ordered list of items e.g. stdin
     */
    private Map<String, List<String[]>> m_source;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the InvokationBase class
     */
    public InvokationBase(){
        this( new LinkedHashMap<>() );
    }
    
    /**
     * Constructor for the InvokationBase class
     * @param data The data being encapsulated
     */
    public InvokationBase( Map<String, List<String[]>> data ){
        m_source = data;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the value associated with the given key e.g. "STDOUT"
     * @param key The key to find
     * @return
     */
    public List<String[]> get( String key ) {
        return m_source.get(key);
    }
    
    /**
     * Adds/replaces the value associated with the given key
     * @param key The key to associate with
     * @param data The list of key value pairs
     */
    public void put( String key, List<String[]> data ) {
        m_source.put(key, data);
    }
    
    /**
     * Bulk put method
     * @param mp The map whose whole contents will be added
     */
    public void putAll( Map<String, List<String[]>> mp ) {
        m_source.putAll( mp );
    }
    
    /**
     * Will add the contents of arr to an already defined key value list
     * @param key The key to add to
     * @param arr The array to add
     */
    public void add( String key, String[] arr ) {
        List<String[]> value = (List<String[]>)m_source.get( key );
        value.add(arr);
    }
    
    public void addAll( String key, List<String[]> arr ) {
        List<String[]> value = (List<String[]>)m_source.get( key );
        value.addAll(arr);
    }
    
    
    //--------------------------------------------------------------------------
    
    public static Map<String, String> arrayToMap( List<String[]> arr ) {
        return arrayToMap( arr, null );
    }
    
    public static Map<String, String> arrayToMap( List<String[]> arr, String prefix ) {
        return arrayToMap( arr, prefix, true );
    }
    
    public static Map<String, String> arrayToMap( List<String[]> arr, String prefix, boolean uppercase ) {
        
        LinkedHashMap<String, String> mp = new LinkedHashMap<>();
        
        // Exit immediately
        if ( arr == null ) {
            return mp;
        }
            
        String newPrefix = prefix;
        if ( prefix == null ) {
            newPrefix = "";
        }
        else if ( uppercase ) {
            newPrefix = newPrefix.toUpperCase();
        }
            
        String        key;
        String        value;
        String[]      items;
        StringBuilder b;
        int           len   = arr.size();
            
        for ( int i = 0; i < len; i++ ) {
            items = arr.get(i);
            key   = items[0];
            value = items[1];
                
            if ( uppercase ) {
                key = key.toUpperCase();
            }
            if ( value == null ) {
                value = "";
            }
                
            key = key.replaceAll( "\\.", "__");
                
            b = new StringBuilder( newPrefix );
            b.append( key );
            
            mp.put( b.toString(), value );
        }
        
        return mp;
    }
    
    /**
     * Converts a list of key value pairs into a Properties object
     * @param arr The array to convert
     * @return
     */
    public static Properties arrayToProperties( List<String[]> arr ) {
        return arrayToProperties( arr, null );
    }
    
    /**
     * Converts a list of key value pairs into a Properties object
     * @param arr The array to convert
     * @param prefix An optional prefix to add to the front of the key
     * @return
     */
    public static Properties arrayToProperties( List<String[]> arr, String prefix ) {
        return arrayToProperties( arr, prefix, true );
    }
    
    /**
     * Converts a list of key value pairs into a Properties object
     * @param arr The array to convert
     * @param prefix An optional prefix to add to the front of the key
     * @param uppercase Uppercase the prefix before adding
     * @return
     */
    public static Properties arrayToProperties( List<String[]> arr, String prefix, boolean uppercase ) {
        
        Properties mp = new Properties();
        
        // Exit immediately
        if ( arr == null ) {
            return mp;
        }
            
        String newPrefix = prefix;
        if ( prefix == null ) {
            newPrefix = "";
        }
        else if ( uppercase ) {
            newPrefix = newPrefix.toUpperCase();
        }
            
        String        key;
        String        value;
        String[]      items;
        StringBuilder b;
        int           len   = arr.size();
            
        for ( int i = 0; i < len; i++ ) {
            items = arr.get(i);
            key   = items[0];
            value = items[1];
                
            if ( uppercase ) {
                key = key.toUpperCase();
            }
            if ( value == null ) {
                value = "";
            }
                
            key = key.replaceAll( "\\.", "__");
            
            b = new StringBuilder( newPrefix );
            b.append( key );
            
            mp.put( b.toString(), value );
        }
        
        return mp;
    }
    
    public static List<String[]> mapToArray( Map<String, String> mp ) {
        return mapToArray( mp, null );
    }
    
    public static List<String[]> mapToArray( Map<String, String> mp, String prefix ) {
        
        ArrayList<String[]> arr = new ArrayList<>();
        
        // Exit immediately if null
        if ( mp == null ) {
            return arr;
        }
            
        Iterator  it    = mp.entrySet().iterator();
        Map.Entry entry;
        String    key;
        
        if ( prefix == null ) {
                
            while ( it.hasNext() ) {
                entry = (Map.Entry)it.next();
                key   = (String)entry.getKey();
                
                arr.add( new String[]{  key
                                      , (String)entry.getValue() } );
            }
            
            return arr;
        }
                
        while ( it.hasNext() ) {
            entry = (Map.Entry)it.next();
            key   = (String)entry.getKey();
            
            // Remove the prefix if found
            if ( key.startsWith( prefix ) ) {
                key = key.replaceFirst( prefix, "" );
            }
                    
            arr.add( new String[]{  key
                                  , (String)entry.getValue() } );
        }
        
        return arr;
    }
    
    /**
     * Will convert a Properties object of key value pairs into a list of 2 element
     * string arrays consisting of key value.  
     * @param mp The Properties to convert
     * @return
     */
    public static List<String[]> propertiesToArray( Properties mp ) {
        return propertiesToArray( mp, null );
    }
    
    /**
     * Will convert a Properties object of key value pairs into a list of 2 element
     * string arrays consisting of key value.  
     * @param mp The Properties to convert
     * @param prefix If found at the front of a property name it will be removed
     * @return
     */
    public static List<String[]> propertiesToArray( Properties mp, String prefix ) {
        
        ArrayList<String[]> arr = new ArrayList<>();
        
        // exit immediately if no properties to convert
        if ( mp == null ) {
            return arr;
        }
            
        Enumeration e   = mp.propertyNames();
        String      key;
        
        // For performance we seperate this out into 2 loop implementations
        // rather than having a redundant if statement inside the loop, executed
        // for each and every iteration
        if ( prefix == null ) {
                
            while ( e.hasMoreElements() ) {
                key = (String)e.nextElement();
            
                arr.add( new String[]{  key
                                      , mp.getProperty(key) } );
            }
            
            return arr;
        }

        
        while ( e.hasMoreElements() ) {
            key = (String)e.nextElement();
                
            // Remove the prefix if found
            if ( key.startsWith( prefix ) ) {
                key = key.replaceFirst( prefix, "" );
            }
                    
            arr.add( new String[]{  key
                                  , mp.getProperty(key) } );
        }

        
        return arr;
    }
}
