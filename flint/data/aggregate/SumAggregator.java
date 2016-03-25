package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class SumAggregator extends AbstractAggregator {

    protected real m_amount;
    
    public SumAggregator() {
        super();
        
        m_name       = "Total";
        m_amount     = 0;
    }
    
    @Override
    public void reset() {
        m_amount = 0;
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
        
        
        try {
        
            real tmp = Real.valueOf( newValue );
            m_amount += tmp;
            
        }
        catch ( Exception ex ) {}
        
    }
    
    @Override
    public String getResult() {
        return Real.toString( m_amount );
    }
    
    @Override
    public void setResult( String result ) {
        m_amount = Real.valueOf( result );
    }
    
}