package flint.engine.io;

/**
 * A basic implementation that performs a 
 * large number of the abstract functionality
 * for both readers and writers
 * @author Philip Bowditch
 */
public abstract class AbstractDataProcessor {
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
    public void setConfig( Map<String, String> settings ) {
        m_settings = settings;
    }
    
    /**
     * Returns the settings used by the processor
     */
    public Map<String, String> getConfig() {
        return m_settings;
    }
    
    /**
     * Resizes an array to match the size of another
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