
package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class AvgAggregator implements IAggregator {

    protected String m_name;
    
    boolean m_ignoreNull;
    
    protected real m_amount;
    
    public AvgAAggregator() {
        m_name       = "Average";
        m_ignoreNull = false;
        m_amount     = 0;
        m_count      = 0;
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
        
        if ( m_amount == null ) {
            return;
        }
        
        String newValue = value;
        if ( newValue == null ) {
            if ( m_ignoreNull ) {
                newValue = "0";
            }
            else {
                m_amount = null;
                return;
            }
        }
        
        m_count += 1;
        
        try {
        
            real tmp = Real.valueOf( newValue );
            m_amount += tmp;
            
        }
        catch ( Exception ex ) {}
        
    }
    
    @Override
    public String getResult() {
        if ( m_amount == null ) {
            return null;
        }
        
        long cnt = m_count;
        if ( cnt <= 0 ) {
            cnt = 1;
        }
        
        return Real.toString( m_amount ) / cnt;
    }
    
}