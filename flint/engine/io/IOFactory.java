package flint.engine.io;

// Core Java classes
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A singleton factory class used to register data formats and returns an appropriate reader/writer class on request.
 * @author Philip Bowditch
 */
public class IOFactory {
    
    /**
     * Registered readers
     */
    protected HashMap<String, Class<IReader>> m_readers = new LinkedHashMap<>();
    
    /**
     * Registered writers
     */
    protected HashMap<String, Class<IWriter>> m_writers = new LinkedHashMap<>();
    
    /**
     * Singleton instance
     */
    protected static IOFactory m_factory = null;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the IOFactory class
     */
    private IOFactory(){}
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Create a writer instance for the given format with default settings
     * @param format The format to write
     * @param out The data stream to write
     */
    public IWriter newWriter( String format, OutputStream out ) {
        return this.newWriter( format, out, new LinkedHashMap<>() );
    }
    
    /**
     * Create a writer instance for the given format with the given settings
     * @param format The format to write
     * @param out The data stream to write
     * @param settings Configuration for the  writer
     */
    public IWriter newWriter( String format, OutputStream out, Map<String, String> settings ) {
        
        Class<IWriter> c = m_writers.get( format );
        IWriter        w = null;

        try {
            //w = c.newInstance();
            w = c.newInstance();
            w.setOutputStream( out );
            w.setConfig( settings );
            w.initialise();
        }
        catch ( Exception ex ) {

        }
        
        return w;
    }
    
    /**
     * Create a reader instance for the given format with default settings
     * @param format The format to read
     * @param in The data stream to read
     */
    public IReader newReader( String format, InputStream in ) {
        return this.newReader( format, in, new LinkedHashMap<>() );
    }
    
    /**
     * Create a reader instance for the given format with the given settings
     * @param format The format to read
     * @param in The data stream to read
     * @param setting Configuration for the reader
     */
    public IReader newReader( String format, InputStream in, Map<String, String> settings ) {
        
        Class<IReader> c = m_readers.get( format );
        IReader        r = null;
        
        try {
            r = c.newInstance();
            r.setInputStream( in );
            r.setConfig( settings );
            r.initialise();
        }
        catch ( Exception ex ) {

        }
        
        return r;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Register a reader of a particular format
     * @param format The format that can be read
     * @param reader The reader to register
     */
    public void registerReader( String format, Class reader ) {
        m_readers.put(format, reader);
    }
    
    /**
     * @param format The format that can be written
     * @param writer The writer to register
     */
    public void registerWriter( String format, Class writer ) {
        m_writers.put(format, writer);
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Factory method, returns a singleton IOFactory
     */
    public static IOFactory newInstance() {
        if ( m_factory == null ) {
            m_factory = new IOFactory();
        }
        
        return m_factory;
    }
}
