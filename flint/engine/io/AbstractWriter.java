package flint.engine.io;

// Core Java classes
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A basic implementation of the IWriter interface that performs a large number
 * of the abstract functionality
 * @author Philip Bowditch
 */
public abstract class AbstractWriter extends AbstractDataProcessor implements IWriter {
    
    /**
     * The stream to write to
     */
    protected OutputStream m_out;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the AbstractWriter class
     */
    public AbstractWriter() {
        super();
        
        m_out = null;
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public void flush() throws IOException {
        m_out.flush();
    }
    
    /**
     * Closes the output stream
     */
    @Override
    public void close() throws IOException {
        m_out.close();
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public void suggestColumns( String[] cols ) throws IOException {
        m_columns = cols;
    }
    
    /**
     * Returns the output stream being written
     */
    @Override
    public OutputStream getOutputStream() {
        return m_out;
    }
    
    /**
     * Sets the output stream to be written
     * @param in The output stream to written
     */
    @Override
    public void setOutputStream( OutputStream out ) {
        m_out = out;
    }
}