package org.ezcomputing.server.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
/**
 * @author Bo Zhao
 *
 */
public class DateUtil {
	
	public  static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	
	public static DateUtil defaultInstance = new DateUtil();
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	

	public static long getTimestamp() {
        return Instant.now().toEpochMilli() ;
    }

	public static String getCurrentTimestampString() {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		
		return df.format(new Date());
		
	}
	
	public static String getTimestampString(long timestamp) {
		Date date = new java.util.Date((long)timestamp);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		
		return df.format(date);
		
	}
	
	public  String getFormattedTime(Long timestamp) {
		Long diff = ( getTimestamp() - timestamp)/1000;
		
		if (diff < 60){
            return String.format( "%d seconds ago", diff);
            
        }else if(diff<3600){
        	return String.format( "%d minutes ago", diff/60);
            
            
        }else if(diff<86400){
        	return String.format( "%d hours ago", diff/3600);
           
        }else if(diff<604800){ // less than one week
        	return String.format( "%d days ago", diff/86400);
            
        }else{
            
            return df.format(timestamp);
            
        }
	}
	/*-
	func getFormattedTime(timestamp: Int64) -> String {
        //let epoch = Date().millisecondsSince1970
        let diff = (  Date().millisecondsSince1970 - timestamp) / Int64(1000.0)
        
        //print("diff:%d,timestamp:%d,epoch:%d", diff, self.timestamp, epoch)
        
        if diff < 60{
            return String(format: "%d seconds ago", diff)
            
        }else if(diff<3600){
            return String(format: "%d minutes ago", (diff/60))
            
        }else if(diff<86400){
            return String(format: "%d hours ago", (diff/3600))
            
        }else if(diff<604800){ // less than one week
            return String(format: "%d days ago", (diff/86400))
            
        }else{
            
            return getLocalDateFormat(timestamp:timestamp)
            
        }
        
        
    }
    */
}
