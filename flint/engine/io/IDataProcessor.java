package flint.engine.io;

// Core Java classes
import java.io.IOException;
import java.util.Map;

/**
 * Data processors need to be able to configure themselves with key value pairs
 * and should have a concept of structure
 * @author Philip Bowditch
 */
public interface IDataProcessor {
    
    /**
     * Runs any post constructor, pre write logic on initialisation e.g.
     * parsing the settings
     * @throws java.lang.Exception
     */
    void initialise() throws Exception;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the columns being processed in this format
     * @return 
     * @throws java.io.IOException
     */
    String[] getColumns() throws IOException;
    
    /**
     * Sets the column names used when processing a data source
     * @param cls The columns to process
     */
    void setColumns( String[] cls );
    
    /**
     * Sets the configuration parameters for the processor
     * @param settings The configuration for the processor
     */
    void setConfig( Map<String, String> settings );
    
    /**
     * Returns the settings used by the processor
     * @return 
     */
    Map<String, String> getConfig();
}
