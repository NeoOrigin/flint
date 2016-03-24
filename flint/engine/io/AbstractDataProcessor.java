package flint.engine.io;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * A basic implementation that performs a 
 * large number of the abstract functionality
 * for both readers and writers
 * @author Philip Bowditch
 */
public abstract class AbstractDataProcessor implements IDataProcessor {
    
    /**
     * The settings to configure this processor
     */
    protected Map<String, String> m_settings;
    
    /**
     * Any columns found
     */
    protected String[] m_columns;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the AbstractDataProcessor class
     */
    public AbstractDataProcessor() {
        m_settings  = null;
        
        m_columns   = new String[]{};
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the columns being processed in this format
     * @return 
     * @throws java.io.IOException
     */
    @Override
    public String[] getColumns() throws IOException {
        return m_columns;
    }
    
    /**
     * Sets the column names used when processing a data source
     * @param cls The columns to process
     */
    @Override
    public void setColumns( String[] cls ) {
        m_columns = cls;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Sets the configuration parameters for the processor
     * @param settings The configuration for the processor
     */
    @Override
    public void setConfig( Map<String, String> settings ) {
        m_settings = settings;
    }
    
    /**
     * Returns the settings used by the processor
     * @return 
     */
    @Override
    public Map<String, String> getConfig() {
        return m_settings;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Resizes an array to match the size of another, will not downsize, any missing vlue will be null
     * @param src The array to resize
     * @param other The array to match to
     * @return 
     */
    public static String[] matchLengthOf( String[] src, String[] other ) {
        return matchLengthOf( src, other, null );
    }
    
    /**
     * Resizes an array to match the size of another, will not downsize nor add any increment value
     * @param src The array to resize
     * @param other The array to match to
     * @param defaultValue Value to use if resizing
     * @return 
     */
    public static String[] matchLengthOf( String[] src, String[] other, String defaultValue ) {
        return matchLengthOf( src, other, defaultValue, false );
    }
    
    /**
     * Resizes an array to match the size of another, will not downsize
     * @param src The array to resize
     * @param other The array to match to
     * @param defaultValue Value to use if resizing
     * @param incrementValue True if missing values should be appended with cell index
     * @return 
     */
    public static String[] matchLengthOf( String[] src, String[] other, String defaultValue, boolean incrementValue ) {
        return matchLengthOf( src, other, defaultValue, incrementValue, false );
    }
    
    /**
     * Resizes an array to match the size of another
     * @param src The array to resize
     * @param other The array to match to
     * @param defaultValue Value to use if resizing
     * @param incrementValue True if missing values should be appended with cell index
     * @param truncate If set will reduce the array size if necessary
     * @return 
     */
    public static String[] matchLengthOf( String[] src, String[] other, String defaultValue, boolean incrementValue, boolean truncate ) {
        
        String[] cols = src;
        
        if ( other == null ) {
            return cols;
        }
        
        // Create a blank array if null
        if ( cols == null ) {
            cols = new String[]{};
        }
        
        // If src is smaller than target, we need to pad
        if ( cols.length < other.length ) {
            
            // Create the resized array
            String[] newCols = new String[other.length];
            
            // Start at the beginning, if within the src bounds we copy
            // else we populate with null or a number
            for ( int i = 0; i < newCols.length; i++ ) {
                
                // Copy
                if ( i < cols.length ) {
                    newCols[i] = cols[i];
                    continue;
                }
                
                newCols[i] = defaultValue;
                
                if ( incrementValue ) { 
                    newCols[i] = newCols[i] + Integer.toString( i );
                }
            }
            
            cols = newCols;
        }
        else if ( truncate && cols.length > other.length ) {
        
            // Create the resized array
            String[] newCols = new String[other.length];
            
            // Start at the beginning, if within the src bounds we copy
            // else we populate with null or a number
            System.arraycopy(cols, 0, newCols, 0, newCols.length); // Copy
            
            cols = newCols;
        }
        
        return cols;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Give a key value map of some data will attempt to align that data to match the
     * sequential order presented by the keys array.  
     * @param data The map of data to align
     * @param keys The array of keys determining the sequential order
     * @return
     * @throws Exception 
     */
    public static String[] align( Map<String, String> data, String[] keys ) throws Exception {
        return align( data, keys, true );
    }
    
    /**
     * Give a key value map of some data will attempt to align that data to match the
     * sequential order presented by the keys array.  
     * @param data The map of data to align
     * @param keys The array of keys determining the sequential order
     * @param case_sensitive Perform matching to keys as case sensitive
     * @return
     * @throws Exception 
     */
    public static String[] align( Map<String, String> data, String[] keys, boolean case_sensitive ) throws Exception {
        return align( data, keys, case_sensitive, "column" );
    }
    
    /**
     * Give a key value map of some data will attempt to align that data to match the
     * sequential order presented by the keys array.  
     * @param data The map of data to align
     * @param keys The array of keys determining the sequential order
     * @param case_sensitive Perform matching to keys as case sensitive
     * @param defaultValue The default column to give if columns do not match the size of keys
     * @return
     * @throws Exception 
     */
    public static String[] align( Map<String, String> data, String[] keys, boolean case_sensitive, String defaultValue ) throws Exception {
        
        // Used to hold column names and associated data elements / cells
        String[] cols  = new String[data.size()];
        String[] cells = new String[data.size()];
        
        // Extract columns and values from the map
        Iterator it = data.keySet().iterator();
        for ( int i = 0; it.hasNext(); i++ ) {
            cols[i]  = (String)it.next();
            cells[i] = (String)data.get( cols[i] );
        }
        
        // Ensure the columns match the keys in size and then the values
        // Also emsure we have as many keys as we do columns
        cols  = matchLengthOf( cols,  keys, defaultValue, true  );
        cols  = matchLengthOf( keys,  cols, defaultValue, true  );
        
        cells = matchLengthOf( cells, cols, null,         false );
        
        String tmp;
        boolean found;
        
        // Acts like a bubble sort, yikes
        for ( int i = 0; i < cols.length; i++ ) {
            
            found = false;
            
            // Go through each of the keys to find match
            for ( int j = 0; j < keys.length; j++ ) {
            
                // Check against the keys, taking care of case if needs be
                if ( case_sensitive ) {
                    found = cols[i].equals( keys[j] );
                }
                else {
                    found = cols[i].equalsIgnoreCase( keys[j] );
                }
                
                // Does this equal the same key, if so swap indexes
                if ( found ) {
                    tmp      = cols[j];
                    cols[j]  = cols[i];
                    cols[i]  = tmp;
                    
                    tmp      = cells[j];
                    cells[j] = cells[i];
                    cells[i] = tmp;
                    break;
                }
            }
            
            if ( !found ) {
                throw new Exception( "Column '" + cols[i] + "' is not found" );
            }
        }
        
        return cells;
    }
}