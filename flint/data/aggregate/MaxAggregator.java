
package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class MaxAggregator implements IAggregator {

    protected String m_name;
    
    boolean m_ignoreNull;
    
    protected real m_amount;
    
    boolean m_assigned;
    
    public MaxAggregator() {
        m_name       = "Max";
        m_ignoreNull = false;
        m_amount     = null;
        m_assigned   = false;
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
        
        if ( ! m_assigned ) {
            m_assigned = true;
        }
        else if ( m_amount == null ) {
            return;
        }
        
        String newValue = value;
        if ( newValue == null ) {
        
            if ( !m_ignoreNull ) {
                m_amount = null;
            }
            
            return;
        }
        
        
        try {
        
            real tmp = Real.valueOf( newValue );
            if ( tmp > m_amount ) {
                m_amount = tmp;
            }
            
        }
        catch ( Exception ex ) {}
        
    }
    
    @Override
    public String getResult() {
        return Real.toString( m_amount );
    }
    
}