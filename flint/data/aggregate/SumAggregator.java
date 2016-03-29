package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class SumAggregator extends AbstractAggregator {

    protected double m_amount;
    
    protected boolean m_isNull;
    
    
    //--------------------------------------------------------------------------
    
    public SumAggregator() {
        super();
        
        m_name       = "Total";
        m_amount     = 0;
        m_isNull     = false;  // Double cant be null
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public void reset() {
        m_amount = 0;
        m_isNull = false; // required as a doule cant be null
    }
    
    @Override
    public void aggregate( String value ) {
        
        if ( m_isNull ) {
            return;
        }
        
        String newValue = value;
        if ( newValue == null ) {
            if ( m_ignoreNull ) {
                newValue = "0";
            }
            else {
                m_isNull = true;
                return;
            }
        }
        
        
        try {
        
            m_amount += Double.valueOf( newValue );
            
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