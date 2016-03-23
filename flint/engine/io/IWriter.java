package flint.engine.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author Philip Bowditch
 */
public interface IWriter {
    
    void suggestColumns( String[] cols ) throws IOException;
    
    void writeRecord( String[] fields ) throws IOException;
    
    void writeMappedRecord( Map<String, String> fields ) throws Exception;
    
    void flush() throws IOException;
    
    void close() throws IOException;
    
    void initialise() throws Exception;
        
    //--------------------------------------------------------------------------
    
    void setOutputStream( OutputStream out );
    
    void setConfig( Map<String, String> settings );
}
