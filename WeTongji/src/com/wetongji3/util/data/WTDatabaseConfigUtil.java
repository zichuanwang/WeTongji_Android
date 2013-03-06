/**
 * 
 */
package com.wetongji3.util.data;



import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

/**
 * @author John
 *
 */
public class WTDatabaseConfigUtil extends OrmLiteConfigUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws SQLException, IOException{
		writeConfigFile("ormlite_config.txt");
	}

}
