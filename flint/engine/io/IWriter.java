package flint.engine.io;

// Core Java classes
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * A generic interface to be implemented by all format writers
 * @author Philip Bowditch
 */
public interface IWriter {
    
    void suggestColumns( String[] cols ) throws IOException;
    
    /**
     * Writes a collection of fields (a record) to the dataset in sequentisl order
     * @param fields The fields to write
     */
    void writeRecord( String[] fields ) throws IOException;
    
    /**
     * Writes a collection of fields based on the order of the columns names
     * @param fields The fields to write
     */
    void writeMappedRecord( Map<String, String> fields ) throws Exception;
    
    /**
     * Ensures no more data is waiting to be delivered
     */
    void flush() throws IOException;
    
    /**
     * Closes the dataset
     */
    void close() throws IOException;
    
    /**
     * Runs any post constructor, pre write logic on initialization
     */
    void initialise() throws Exception;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Sets the output stream to write to
     * @param out The output stream to write
     */
    void setOutputStream( OutputStream out );
    
    /**
     * Set settings used to configure any implementing writer
     * @param settings The settings to configure
     */
    void setConfig( Map<String, String> settings );
}
