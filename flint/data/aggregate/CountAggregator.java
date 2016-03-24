package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class CountAggregator implements IAggregator {

    protected String m_name;
    
    boolean m_ignoreNull;
    
    protected long m_amount;
    
    public CountAggregator() {
        m_name       = "Count";
        m_ignoreNull = false;
        m_amount     = 0;
    }

    @Override
    public String getName() {
        return m_name;
    }
    
    @Override
    public void setName( String name ) {
         m_name = name;
    }
    
    @Override
    public void setNullIgnored( boolean ignore ) {
        m_ignoreNull = ignore;
    }
    
    @Override
    public boolean isNullIgnored() {
        return m_ignoreNull;
    }
    
    @Override
    public void initialise() {
    }
    
    @Override
    public void aggregate( String value ) {
        
        try {
            if ( value != null || m_ignoreNull ) {
                m_amount += 1;
            }
        }
        catch ( Exception ex ) {}
        
    }
    
    @Override
    public String getResult() {
        return Long.toString( m_amount );
    }
    
}