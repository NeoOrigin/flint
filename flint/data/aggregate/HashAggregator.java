package flint.data.aggregate;

// Core Java classes
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


/**
 *
 * @author Philip Bowditch
 */
public class HashAggregator extends AbstractAggregator {

    protected MessageDigest m_messageDigest;
    
    protected boolean m_isNull;
    
    
    //--------------------------------------------------------------------------
    
    public HashAggregator() {
        super();
        
        try {
            m_messageDigest = MessageDigest.getInstance( "MD5" );
        }
        catch (Exception ex){}
        
        m_name          = "MD5";
        m_isNull        = false;
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public void reset() {
        m_messageDigest.reset();
        m_isNull  = false; // required as a doule cant be null
    }
    
    @Override
    public void aggregate( String value ) {
        
        if ( m_isNull ) {
            return;
        }
        
        String newValue = value;
        if ( newValue == null ) {
            if ( m_ignoreNull ) {
                newValue = "";
            }
            else {
                m_isNull = true;
                return;
            }
        }
        
        
        try {
            byte[] bytesOfMessage = newValue.getBytes("UTF-8");
            
            m_messageDigest.update( bytesOfMessage );
        }
        catch ( Exception ex ) {}
        
    }
    
    @Override
    public String getResult() {
        
        if ( m_isNull ) {
            return null;
        }
        
        byte[] bytes = m_messageDigest.digest();
        return new String(bytes, StandardCharsets.UTF_8);
    }
    
    @Override
    public void setResult( String result ) {
        
        m_isNull = false;
        if ( result == null ) {
            m_isNull = true;
            return;
        }
        // throw new NotImplementedException();
        //m_amount = result;
    }
    
}
