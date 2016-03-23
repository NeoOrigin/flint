package flint.engine.io;

// Core Java classes
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Philip Bowditch
 */
public class IOFactory {
    
    protected HashMap<String, Class<IReader>> m_readers = new LinkedHashMap<>();
    protected HashMap<String, Class<IWriter>> m_writers = new LinkedHashMap<>();
    
    //--------------------------------------------------------------------------
    
    private IOFactory(){}
    
    //--------------------------------------------------------------------------
    
    public IWriter newWriter( String format, OutputStream out ) {
        return this.newWriter( format, out, new LinkedHashMap<>() );
    }
    
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
    
    public IReader newReader( String format, InputStream in ) {
        return this.newReader( format, in, new LinkedHashMap<>() );
    }
    
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
    
    public void registerReader( String format, Class reader ) {
        m_readers.put(format, reader);
    }
    
    public void registerWriter( String format, Class writer ) {
        m_writers.put(format, writer);
    }
    
    //--------------------------------------------------------------------------
    
    public static IOFactory newInstance() {
        return new IOFactory();
    }
}
