package thesugarchris.nitro.utils;

import thesugarchris.nitro.Nitro;

import java.sql.*;

public class Database {
    private final String host = "localhost";
    private final String database = "nitro";
    private final String username = "root";
    private final String password = "password";
    private final int port = 3306;
    private Connection connection;

    public Database() throws SQLException, ClassNotFoundException {
        openConnection();
        setup();
    }

    public static String convertKeysToString(String[] list) {
        if (list.length > 0) {
            StringBuilder nameBuilder = new StringBuilder();

            for (String n : list) nameBuilder.append(n).append(",");
            nameBuilder.deleteCharAt(nameBuilder.length() - 1);

            return nameBuilder.toString();
        } else {
            return "";
        }
    }

    public static String convertValuesToString(String[] list) {
        if (list.length > 0) {
            StringBuilder nameBuilder = new StringBuilder();

            for (String n : list) nameBuilder.append("'").append(n.replace("'", "\\'")).append("',");
            nameBuilder.deleteCharAt(nameBuilder.length() - 1);

            return nameBuilder.toString();
        } else {
            return "";
        }
    }

    public static String convertUpdatesToString(String[] keys, String[] values) {
        if (keys.length > 0) {
            StringBuilder nameBuilder = new StringBuilder();

            for (int i = 0; i < keys.length; i++) {
                nameBuilder.append(keys[i]).append("=").append(values[i]).append(",");
            }

            nameBuilder.deleteCharAt(nameBuilder.length() - 1);

            return nameBuilder.toString();
        } else {
            return "";
        }
    }

    private void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void setup() {
        // TODO: run mysql migration scripts
        Nitro.lgm("Successfully setup initial tables...");
    }

    public ResultSet query(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public boolean update(String query) {
        try {
            Statement statement = connection.createStatement();
            int updateCount = statement.executeUpdate(query);
            return updateCount > 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean update(String table, String[] keys, String[] values) {
        try {
            Statement statement = connection.createStatement();

            // "INSERT INTO player_data (player, nickname) VALUES ('%s', '%s') ON DUPLICATE KEY UPDATE nickname = '%s'"

            String query = "INSERT INTO TABLE_NAME QUERY_KEYS VALUES QUERY_VALUES ON DUPLICATE KEY UPDATE UPDATE_KEYS";
            query = query.replace("TABLE_NAME", table);
            query = query.replace("QUERY_KEYS", "(" + convertKeysToString(keys) + ")");
            query = query.replace("QUERY_VALUES", "(" + convertValuesToString(values) + ")");
            query = query.replace("UPDATE_KEYS", convertUpdatesToString(keys, values));

            int updateCount = statement.executeUpdate(query);
            return updateCount > 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
