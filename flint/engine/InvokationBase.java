package flint.engine;

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
     * Holds the data
     */
    private Map<String, List<String[]>> m_source;
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the InvokationBase class
     */
    public InvokationBase(){
        m_source = new LinkedHashMap<String, List<String[]>>();
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the value associated with tbe given key e.g. "STDOUT"
     */
    public List<String[]> get( String key ) {
        return m_source.get(key);
    }
    
    /**
     * Adds/replaces the value associated with the given key
     * @param key The key to associate with
     */
    public void put( String key, List<String[]> data ) {
        m_source.put(key, data);
    }
    
    /**
     * Bulk put method
     * @param mp 
     */
    public void putAll( Map<String, List<String[]>> mp ) {
        m_source.putAll( mp );
    }
    
    public void add( String key, String[] arr ) {
        ((List<String[]>)m_source.get( key )).add(arr);
    }
    
    public void addAll( String key, List<String[]> arr ) {
        ((List<String[]>)m_source.get( key )).addAll(arr);
    }
    
    //--------------------------------------------------------------------------
    
    public static Map<String, String> arrayToMap( List<String[]> arr ) {
        return arrayToMap( arr, null );
    }
    
    public static Map<String, String> arrayToMap( List<String[]> arr, String prefix ) {
        return arrayToMap( arr, prefix, true );
    }
    
    public static Map<String, String> arrayToMap( List<String[]> arr, String prefix, boolean uppercase ) {
        
        LinkedHashMap<String, String> mp = new LinkedHashMap<String, String>();
        
        if ( arr != null ) {
            
            String newPrefix = prefix;
            if ( prefix == null ) {
                newPrefix = "";
            }
            else if ( uppercase ) {
                newPrefix = newPrefix.toUpperCase();
            }
            
            String key = null;
            String value = null;
            int len = arr.size();
            for ( int i = 0; i < len; i++ ) {
                key   = arr.get(i)[0];
                value = arr.get(i)[1];
                if ( uppercase ) {
                    key = key.toUpperCase();
                }
                if ( value == null ) {
                    value = "";
                }
                
                key = key.replaceAll( "\\.", "__");
                
                mp.put( newPrefix + key, value );
            }
            
        }
        
        return mp;
    }
    
    public static Properties arrayToProperties( List<String[]> arr ) {
        return arrayToProperties( arr, null );
    }
    
    public static Properties arrayToProperties( List<String[]> arr, String prefix ) {
        return arrayToProperties( arr, prefix, true );
    }
    
    public static Properties arrayToProperties( List<String[]> arr, String prefix, boolean uppercase ) {
        
        Properties mp = new Properties();
        
        if ( arr != null ) {
            
            String newPrefix = prefix;
            if ( prefix == null ) {
                newPrefix = "";
            }
            else if ( uppercase ) {
                newPrefix = newPrefix.toUpperCase();
            }
            
            String key = null;
            String value = null;
            int len = arr.size();
            for ( int i = 0; i < len; i++ ) {
                key   = arr.get(i)[0];
                value = arr.get(i)[1];
                if ( uppercase ) {
                    key = key.toUpperCase();
                }
                if ( value == null ) {
                    value = "";
                }
                
                key = key.replaceAll( "\\.", "__");
                
                mp.put( newPrefix + key, value );
            }
            
        }
        
        return mp;
    }
    
    public static List<String[]> mapToArray( Map<String, String> mp ) {
        return mapToArray( mp, null );
    }
    
    public static List<String[]> mapToArray( Map<String, String> mp, String prefix ) {
        
        ArrayList<String[]> arr = new ArrayList<String[]>();
        
        if ( mp != null ) {
            
            Iterator  it    = mp.entrySet().iterator();
            Map.Entry entry = null;
            
            if ( prefix == null ) {
                
                while ( it.hasNext() ) {
                    entry = (Map.Entry)it.next();
            
                    arr.add( new String[]{  (String)entry.getKey()
                                          , (String)entry.getValue() } );
                }
                
            }
            else {
                
                String key = null;
                while ( it.hasNext() ) {
                    entry = (Map.Entry)it.next();
                    key   = (String)entry.getKey();
                    
                    if ( key.startsWith( prefix ) ) {
                        key = key.replaceFirst( prefix, "" );
                    }
                    
                    arr.add( new String[]{  key
                                          , (String)entry.getValue() } );
                }
                
            }
        }
        
        return arr;
    }
    
    public static List<String[]> propertiesToArray( Properties mp ) {
        return propertiesToArray( mp, null );
    }
    
    public static List<String[]> propertiesToArray( Properties mp, String prefix ) {
        
        ArrayList<String[]> arr = new ArrayList<String[]>();
        
        if ( mp != null ) {
            
            Enumeration e   = mp.propertyNames();
            String      key = null;
                
            if ( prefix == null ) {
                
                while ( e.hasMoreElements() ) {
                    key = (String)e.nextElement();
            
                    arr.add( new String[]{  key
                                          , mp.getProperty(key) } );
                }
                
            }
            else {
                
                while ( e.hasMoreElements() ) {
                    key = (String)e.nextElement();
                    
                    if ( key.startsWith( prefix ) ) {
                        key = key.replaceFirst( prefix, "" );
                    }
                    
                    arr.add( new String[]{  key
                                          , mp.getProperty(key) } );
                }
                
            }
        }
        
        return arr;
    }
}