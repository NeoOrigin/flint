package flint.engine.io;

// Core Java classes
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Philip Bowditch
 */
public class RawReader extends AbstractReader {
    
    protected BufferedReader m_reader;
    
    //--------------------------------------------------------------------------
    
    public RawReader() {
        
        super();
        
        m_reader    = null;
    }
    
    //--------------------------------------------------------------------------
    
    @Override
    public String[] readRecord() throws IOException {
        
        // Reads a line of data, as this is raw we dont have a concept of columns
        // so return as a 1 field array
        String data = m_reader.readLine();
        if ( data == null ) {
            return null;
        }
        
        return new String[]{ data };
    }
    
    @Override
    public void initialise() throws Exception {
        
        boolean hasHeader = true;
        String encoding = "UTF-8";

        Iterator it = getConfig().entrySet().iterator();
        Map.Entry entry;
        String key;
        String value;

        // Go through the config to determine what options to set
        // currently only header and encoding are supported
        while ( it.hasNext() ) {
            entry = (Map.Entry)it.next();
            
            key = (String)entry.getKey();
            key = key.toLowerCase();
            
            value = (String)entry.getValue();
            
            if ( key.equalsIgnoreCase( "header" ) ) {
                hasHeader = Boolean.parseBoolean( value );
            }
            else if ( key.equalsIgnoreCase( "encoding" ) ) {
                encoding = value;
            }
        }
        
        // Setup the input stream
        m_reader = new BufferedReader( new InputStreamReader( getInputStream(), encoding ) );
        
        // Set the columns as the first record read, by default this is a single field
        if ( hasHeader ) {
            setColumns( readRecord() );
        }
    }
}
