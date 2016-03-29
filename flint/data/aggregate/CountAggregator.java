package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class CountAggregator extends AbstractAggregator {
    
    protected long m_amount;
    
    
    //--------------------------------------------------------------------------
    
    public CountAggregator() {
        super();
        
        m_name       = "Count";
        m_amount     = 0;
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public void reset() {
        m_amount = 0;
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
    
    @Override
    public void setResult( String result ) {
        m_amount = Long.valueOf( result );
    }
}