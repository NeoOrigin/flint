
package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class AvgAggregator extends AbstractAggregator {
    
    protected real m_amount;
    protected long m_count;
    
    public AvgAAggregator() {
        super();
        
        m_name       = "Average";
        m_amount     = 0;
        m_count      = 0;
    }
    
    @Override
    public void reset() {
        m_amount = 0;
        m_count  = 0;
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
        
        return Real.toString( m_amount / cnt );
    }
    
    @Override
    public void setResult( String result ) {
        m_amount = Real.valueOf( result;
        m_count  = 1;
    }
    
}