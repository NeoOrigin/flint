package flint.engine.io;

// Core Java classes
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * A general interface to provide consistency in reading external data files.  
 * @author Philip Bowditch
 */
public interface IReader {
    
    /**
     * Reads a single record (collection of fields) from the data source, ordered
     * by their occurence within the data
     * @return
     * @throws IOException 
     */
    String[] readRecord() throws IOException;
    
    /**
     * Reads a single record (collection of fields) from the data source, mapped
     * to the column names if appropriate
     * @return
     * @throws IOException 
     */
    Map<String, String> readRecordMapped() throws IOException;
    
    /**
     * Closes the data source
     * @throws IOException 
     */
    void close() throws IOException;
    
    /**
     * Used for any initialisation pre-read logic such as opening database connections etc
     * @throws Exception 
     */
    void initialise() throws Exception;
        
    //--------------------------------------------------------------------------
    
    /**
     * Sets the input stream to read from
     * @param in 
     */
    void setInputStream( InputStream in );
    
    /**
     * Returns the input stream being read from
     * @return 
     */
    InputStream getInputStream();
    
    /**
     * Sets configuration parameters used by any reader implementations
     * @param settings 
     */
    void setConfig( Map<String, String> settings );
    
    /**
     * Sets configuration parameters to be used by the readers
     * @return 
     */
    Map<String, String> getConfig();
    
    /**
     * Returns the column names found in the data source
     * @return
     * @throws IOException 
     */
    String[] getColumns() throws IOException;
    
    /**
     * Sets the column names to be found in the data source
     * @param cls 
     */
    void setColumns( String[] cls );
}
