package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class MinAggregator extends AbstractAggregator {
    
    protected double m_amount;
    
    protected boolean m_assigned;
    
    protected boolean m_isNull;
    
    
    //--------------------------------------------------------------------------
    
    public MinAggregator() {
        super();
        
        m_name       = "Min";
        m_amount     = 0;
        m_assigned   = false;
        m_isNull     = false; // required as a doule cant be null
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public void reset() {
        m_amount   = 0;
        m_assigned = false;
        m_isNull   = false; // required as a doule cant be null
    }
    
    @Override
    public void aggregate( String value ) {
        
        if ( ! m_assigned ) {
            m_assigned = true;
        }
        else if ( m_isNull ) {
            return;
        }
        
        String newValue = value;
        if ( newValue == null ) {
        
            if ( !m_ignoreNull ) {
                m_isNull = true;
            }
            
            return;
        }
        
        
        try {
        
            double tmp = Double.valueOf( newValue );
            if ( tmp < m_amount ) {
                m_amount = tmp;
            }
            
        }
        catch ( Exception ex ) {}
        
    }
    
    @Override
    public String getResult() {
        if ( m_isNull ) {
            return null;
        }
        
        return Double.toString( m_amount );
    }
    
    @Override
    public void setResult( String result ) {
        
        m_isNull = false;
        if ( result == null ) {
            m_isNull = true;
            return;
        }
        
        m_amount = Double.valueOf( result );
    }
}