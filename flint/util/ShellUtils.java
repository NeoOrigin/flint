package flint.util;

// Core Java classes
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Philip Bowditch
 */
public class ShellUtils {
    
    public static String expandCommandLine( final String cmd, final Map<String, String> mp, String shell ) {
        
        Pattern vars = Pattern.compile( "[$]([a-zA-Z0-9._]+)" );
        Matcher m = vars.matcher(cmd);

        StringBuilder sb = new StringBuilder();
        int lastMatchEnd = 0;
        
        while (m.find()) {
            
            sb.append(cmd.substring(lastMatchEnd, m.start()));
            
            String envVar = m.group(1);
            String envVal = mp.get(envVar);
            
            if (envVal == null)
                sb.append(cmd.substring(m.start(), m.end()));
            else
                sb.append(envVal);
            
            lastMatchEnd = m.end();
        }
        sb.append(cmd.substring(lastMatchEnd));

        return sb.toString();
    }
    
    public static String eval( String str, Map<String, String> params ) {
        StringBuilder builder = new StringBuilder();
        StringBuilder param   = new StringBuilder();
        
        boolean inBlock = false;
        char c;
        char c2;
        for ( int i = 0; i < str.length(); i++ ) {
            
            c = str.charAt(i);
            
            if ( c == '$' ) {
                
                for ( int j = i + 1; j < str.length(); j++ ) {
                
                    c2 = str.charAt(j);
                    if ( j == ( i + 1 ) && c2 == '{' ) {
                        inBlock = true;
                    }
                    else if ( c2 >= '0' && c2 <= '9' ) {
                        param.append( c2 );
                    }
                    else if ( c2 >= 'a' && c2 <= 'Z' ) {
                        param.append( c2 );
                    }
                    else if ( c2 >= 'A' && c2 <= 'Z' ) {
                        param.append( c2 );
                    }
                    else if ( c2 == '_' ) {
                        param.append( c2 );
                    }
                    
                }
                
                inBlock = false;
                
                continue;
            }
            
            builder.append( c );
        }

        return builder.toString();
    }
}
