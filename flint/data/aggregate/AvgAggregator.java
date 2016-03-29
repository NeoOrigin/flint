
package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class AvgAggregator extends AbstractAggregator {
    
    protected double m_amount;
    protected long m_count;
    
    protected boolean m_isNull;
    
    
    //--------------------------------------------------------------------------
    
    public AvgAggregator() {
        super();
        
        m_name       = "Average";
        m_amount     = 0;
        m_count      = 0;
        m_isNull     = false; // required as a doule cant be null
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public void reset() {
        m_amount = 0;
        m_count  = 0;
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
        
        m_count += 1;
        
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
        
        long cnt = m_count;
        if ( cnt <= 0 ) {
            cnt = 1;
        }
        
        return Double.toString( m_amount / cnt );
    }
    
    @Override
    public void setResult( String result ) {
        
        m_isNull = false;
        if ( result == null ) {
            m_isNull = true;
            return;
        }
        
        m_amount = Double.valueOf( result );
        m_count  = 1;
    }
    
}