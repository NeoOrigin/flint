package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class NoOpAggregator extends AbstractAggregator {

    protected String m_amount;
    
    
    //--------------------------------------------------------------------------
    
    public NoOpAggregator() {
        super();
        
        m_name       = "Value";
        m_amount     = "";
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public void reset() {
        m_amount = "";
    }
    
    @Override
    public void aggregate( String value ) {
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