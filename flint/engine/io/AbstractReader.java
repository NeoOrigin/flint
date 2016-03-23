package flint.engine.io;

// Core Java classes
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A basic implementation of the IReader interface that performs a large number
 * of the abstract functionality
 * @author Philip Bowditch
 */
public abstract class AbstractReader implements IReader {
    
    /**
     * The stream to read from
     */
    protected InputStream m_in;
    
    /**
     * The settings to configure this reader
     */
    protected Map<String, String> m_settings;
    
    /**
     * Any columns found
     */
    protected String[] m_columns;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the AbstractReader class
     */
    public AbstractReader() {
        m_in        = null;
        m_settings  = null;
        
        m_columns   = new String[]{};
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Closes the input stream
     */
    @Override
    public void close() throws IOException {
        m_in.close();
    }
    
    /**
     * Returns the columns being read in this format
     */
    @Override
    public String[] getColumns() throws IOException {
        return m_columns;
    }
    
    /**
     * Sets the column names used when reading a data source
     * @param cls The columns to read
     */
    @Override
    public void setColumns( String[] cls ) {
        m_columns = cls;
    }
    
    /**
     * Reads a record from the dataset and maps it to columbs
     */
    @Override
    public Map<String, String> readRecordMapped() throws IOException {
        String[] rec = readRecord();
        
        // Nothing to do
        if ( rec == null ) {
            return null;
        }
        
        Map<String, String> m = new LinkedHashMap<>();
        String[] cols = matchLengthOf( m_columns, rec,  "column", true, false  ); // Add indexes if column names missing
        String[] rows = matchLengthOf( rec,       cols, null,     false, false ); // Set missing cells to null
        
        // Add each field with its column name
        for ( int i = 0; i < cols.length; i++ ) {
            m.put( cols[i], rows[i] );
        }
        
        return m;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the input stream being read
     */
    @Override
    public InputStream getInputStream() {
        return m_in;
    }
    
    /**
     * Sets the input stream to be read
     * @param in The input stream to read
     */
    @Override
    public void setInputStream( InputStream in ) {
        m_in = in;
    }
    
    /**
     * Sets the configuration parameters for the reader
     * @param settings The configuration for the reader
     */
    @Override
    public void setConfig( Map<String, String> settings ) {
        m_settings = settings;
    }
    
    /**
     * Returns the settings used tby the reader
     */
    @Override
    public Map<String, String> getConfig() {
        return m_settings;
    }
    
    /**
     * Resizes an array to match tbe size of another
     * @param src The array to resize
     * @param other The array to match to
     * @param defaultValue Value to use if resizing
     * @param incrementValue True if missing values should be appended with cell index
     * @param truncate If set will reduce the array size if necessary
     */
    public static String[] matchLengthOf( String[] src, String[] other, String defaultValue, boolean incrementValue, boolean truncate ) {
        String[] cols = src;
        
        // Create a blank array if null
        if ( cols == null ) {
            if ( truncate && other == null ) {
                return null;
            }
            
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
            for ( int i = 0; i < newCols.length; i++ ) {
                
                // Copy
                newCols[i] = cols[i];
            }
            
            cols = newCols;
        }
        
        return cols;
    }
}
