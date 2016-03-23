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
public abstract class AbstractReader extends AbstractDataProcessor implements IReader {
    
    /**
     * The stream to read from
     */
    protected InputStream m_in;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the AbstractReader class
     */
    public AbstractReader() {
        super();
        
        m_in = null;
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
}
