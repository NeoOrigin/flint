package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public interface IAggregator {

    String getName();
    
    void setName( String name );
    
    void setNullIgnored( boolean ignore );
    
    boolean isNullIgnored();
    
    void initialise();
    
    void aggregate( String value );
    
    String getResult();
    
}