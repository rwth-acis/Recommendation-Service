/**
 * 
 */
package i5.las2peer.services.servicePackage.datamodel;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcDatabaseConnection;
import com.j256.ormlite.support.ConnectionSource;

/**
 * @author sathvik
 *
 */
public abstract class MySqlOpenHelper {
	private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/";

	private String mUserName;
	private String mPassword;
	private String mDbName;
	private ConnectionSource mConnectionSource;

	public MySqlOpenHelper(String dbName, String username, String password) {
		mDbName = dbName;
		mUserName = username;
		mPassword = password;
	}

	private void createDatabase()
			throws SQLException {
		JdbcDatabaseConnection connection = new JdbcDatabaseConnection(
				DriverManager.getConnection(DB_URL, mUserName, mPassword));
		connection.executeStatement("CREATE DATABASE IF NOT EXISTS "
				+ mUserName, -1);
	}

	public ConnectionSource getConnectionSource()
			throws SQLException {
		createDatabase();

		mConnectionSource = new JdbcConnectionSource(DB_URL + mDbName);
		((JdbcConnectionSource) mConnectionSource).setUsername(mUserName);
		((JdbcConnectionSource) mConnectionSource).setPassword(mPassword);
		return mConnectionSource;
	}

	public void close() {

	}
}
