package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class MinAggregator extends AbstractAggregator {
    
    protected real m_amount;
    
    boolean m_assigned;
    
    public MinAggregator() {
        super();
        
        m_name       = "Min";
        m_amount     = null;
        m_assigned   = false;
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
            if ( tmp < m_amount ) {
                m_amount = tmp;
            }
            
        }
        catch ( Exception ex ) {}
        
    }
    
    @Override
    public String getResult() {
        return Real.toString( m_amount );
    }
    
    @Override
    public void setResult( String result ) {
        m_amount = result;
    }
}