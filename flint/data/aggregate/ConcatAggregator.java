package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class ConcatAggregator extends AbstractAggregator {

    protected String m_amount;
    
    public ConcatAggregator() {
        super();
        
        m_name       = "Concatenate";
        m_amount     = "";
    }
    
    @Override
    public void reset() {
        m_amount = "";
    }
    
    @Override
    public void aggregate( String value ) {
        
        if ( m_amount == null ) {
            return;
        }
        
        String newValue = value;
        if ( newValue == null ) {
            if ( m_ignoreNull ) {
                newValue = "";
            }
            else {
                m_amount = null;
                return;
            }
        }
        
        
        try {
            m_amount += value;
        }
        catch ( Exception ex ) {}
        
    }
    
    @Override
    public String getResult() {
        return m_amount;
    }
    
    @Override
    public void setResult( String result ) {
        m_amount = result;
    }
    
}