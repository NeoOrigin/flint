package flint.engine.io;

// Core Java classes
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;


/**
 * A simple writer that writes lines of data
 * based on the platform dependent newline character
 * @author Philip Bowditch
 */
public class RawWriter extends AbstractWriter {
    
    /**
     * Wraps writing to the raw output stream
     */
    protected BufferedWriter m_writer;
    
    /**
     * For performance holds a reference to the line ending character to use
     */
    protected byte[] m_lineEnding;
    
    
    //--------------------------------------------------------------------------
    
    public RawWriter() {
        super();
        
        m_writer     = null;
        m_lineEnding = System.getProperty( "line.separator" ).getBytes();
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Closes the output stream
     * @throws java.io.IOException
     */
    @Override
    public void close() throws IOException {
        m_writer.close();
        
        super.close();
    }
    
    public byte[] getLineEnding() {
        return m_lineEnding;
    }
    
    public void setLineEnding( String lineEnding ) {
        setLineEnding( lineEnding.getBytes() );
    }
    
    public void setLineEnding( byte[] lineEnding ) {
        m_lineEnding = lineEnding;
    }
    
    @Override
    public void writeRecord( String[] fields ) throws IOException {
        
        for (String field : fields) {
            m_out.write(field.getBytes());
        }
        
        m_out.write( m_lineEnding );
    }
    
    @Override
    public void writeMappedRecord( Map<String, String> fields ) throws Exception {
        String[] rows = align( fields, m_columns );
        
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
            
                case "header"    : hasHeader = Boolean.parseBoolean( value );
                                   break;
                case "encoding"  : encoding = value;
                                   break;
                case "separator" : setLineEnding( value );
                                   break;
                                   
                                  
            }
        }
        
        // Setup the input stream
        m_writer = new BufferedWriter( new OutputStreamWriter( getOutputStream(), encoding ) );
        
        // Set the columns as the first record read, by default this is a single field
        if ( hasHeader ) {
            writeRecord( getColumns() );
        }
    }
        
    //--------------------------------------------------------------------------
}