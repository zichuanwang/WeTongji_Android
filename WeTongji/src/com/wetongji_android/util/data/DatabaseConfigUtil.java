/**
 * 
 */
package com.wetongji_android.util.data;



import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

/**
 * @author John
 *
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws SQLException, IOException{
        // FOR Eclipse
        //writeConfigFile("ormlite_config.txt");

        // FOR Android Studio: pass complete path
		writeConfigFile(new File("D:\\Projects\\WeTongji\\wetongji2_android\\WeTongji\\res\\raw\\ormlite_config.txt"));
	}

}
