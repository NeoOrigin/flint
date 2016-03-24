package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public class SumAggregator implements IAggregator {

    protected String m_name;
    
    boolean m_ignoreNull;
    
    protected real m_amount;
    
    public SumAggregator() {
        m_name       = "Total";
        m_ignoreNull = false;
        m_amount     = 0;
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
    
}