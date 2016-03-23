package flint.engine.io;

// Core Java classes
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 *
 * @author Philip Bowditch
 */
public class RawWriter extends AbstractWriter {
    
    /**
     * Wraps writing to the raw output stream
     */
    protected BufferedWriter m_writer;
    
    
    //--------------------------------------------------------------------------
    
    public RawWriter() {
        super();
        
        m_writer = null;
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public void writeRecord( String[] fields ) throws IOException {
        
        for ( int i = 0; i < fields.length; i++ ) {
            m_out.write( fields[i].getBytes() );
        }
        
        m_out.write( System.get().getBytes() );
    }
    
    @Override
    public void writeMappedRecord( Map<String, String> fields ) throws Exception {
        String[] rows = StringUtils.align( fields, m_columns );
        
        writeRecord( rows );
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
            
            switch ( key ) {
            
                case "header"   : hasHeader = Boolean.parseBoolean( value );
                                  break;
                case "encoding" : encoding = value;
                                  break;
                                  
            }
        }
        
        // Setup the input stream
        m_writer = new BufferedReader( new InputStreamReader( getInputStream(), encoding ) );
        
        // Set the columns as the first record read, by default this is a single field
        if ( hasHeader ) {
            writeRecord( getColumns() );
        }
    }
        
    //--------------------------------------------------------------------------
}