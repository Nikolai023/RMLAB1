package Database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class DatabaseManager {
    private static Connection serverConnection;
    private static Statement serverStatement;
    private static MessageDigest encoder;


    public static void initDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            encoder = MessageDigest.getInstance("SHA-256");

            serverConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "postgres");
            serverStatement = serverConnection.createStatement();

            ResultSet hasDatabase = serverStatement.executeQuery("SELECT * FROM pg_database WHERE datname = 'users_database'");
            if (!hasDatabase.next()) {
                serverStatement.execute("CREATE DATABASE users_database;");
            }
            serverConnection.close();
            serverStatement.close();

            serverConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/users_database", "postgres", "postgres");
            serverStatement = serverConnection.createStatement();
            serverStatement.execute("CREATE TABLE IF NOT EXISTS users_table" +
                    "(" +
                    "login text PRIMARY KEY, " +
                    "password_hash bytea, " +
                    "user_rights integer" +
                    ");");

            PreparedStatement preparedStatement = serverConnection.prepareStatement("INSERT INTO users_table VALUES (?, ?, ?) ON CONFLICT (login) DO UPDATE SET login = ?, password_hash = ?, user_rights = ?;");
            preparedStatement.setString(1, "admin");
            encoder.update("admin".getBytes());
            preparedStatement.setBytes(2, encoder.digest());
            preparedStatement.setInt(3, 1);
            preparedStatement.setString(4, "admin");
            encoder.update("admin".getBytes());
            preparedStatement.setBytes(5, encoder.digest());
            preparedStatement.setInt(6, 1);
            preparedStatement.execute();

            preparedStatement = serverConnection.prepareStatement("INSERT INTO users_table VALUES (?, ?, ?) ON CONFLICT (login) DO UPDATE SET login = ?, password_hash = ?, user_rights = ?;");
            preparedStatement.setString(1, "user");
            encoder.update("user".getBytes());
            preparedStatement.setBytes(2, encoder.digest());
            preparedStatement.setInt(3, 2);
            preparedStatement.setString(4, "user");
            encoder.update("user".getBytes());
            preparedStatement.setBytes(5, encoder.digest());
            preparedStatement.setInt(6, 2);
            preparedStatement.execute();


            serverStatement.close();
            serverConnection.close();
        } catch (SQLException | NoSuchAlgorithmException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if (serverStatement != null) {
                    serverStatement.close();
                }
                if (serverConnection != null) {
                    serverConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dropDatabase() {
        try {
            System.out.println("Dropping...");
            serverConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/users_database", "postgres", "postgres");
            serverStatement = serverConnection.createStatement();
            serverStatement.execute("DROP TABLE users_table;");
            serverStatement.close();
            serverConnection.close();
            serverConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "postgres");
            serverStatement = serverConnection.createStatement();
            serverStatement.execute("DROP DATABASE users_database;");
            serverStatement.close();
            serverConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverStatement != null) {
                    serverStatement.close();
                }
                if (serverConnection != null) {
                    serverConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static int checkUser(String login, String password) {
        int userRights = -1;
        try {
            serverConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/users_database", "postgres", "postgres");
            serverStatement = serverConnection.createStatement();

            PreparedStatement preparedStatement = serverConnection.prepareStatement("SELECT * FROM users_table WHERE login = ? AND password_hash = ?;");
            preparedStatement.setString(1, login);
            encoder.update(password.getBytes());
            preparedStatement.setBytes(2, encoder.digest());
            ResultSet hasUser = preparedStatement.executeQuery();
            if (hasUser.next()) {
                userRights = hasUser.getInt("user_rights");
            }
            serverStatement.close();
            serverConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverStatement != null) {
                    serverStatement.close();
                }
                if (serverConnection != null) {
                    serverConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userRights;
    }

    public static int checkUser(String login) {
        int userRights = -1;
        try {
            serverConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/users_database", "postgres", "postgres");
            serverStatement = serverConnection.createStatement();

            PreparedStatement preparedStatement = serverConnection.prepareStatement("SELECT * FROM users_table WHERE login = ?;");
            preparedStatement.setString(1, login);
            ResultSet hasUser = preparedStatement.executeQuery();
            if (hasUser.next()) {
                userRights = hasUser.getInt("user_rights");
            }
            serverStatement.close();
            serverConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverStatement != null) {
                    serverStatement.close();
                }
                if (serverConnection != null) {
                    serverConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userRights;
    }

    public static boolean addUser(String login, String password) {
        try {
            serverConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/users_database", "postgres", "postgres");
            serverStatement = serverConnection.createStatement();

            PreparedStatement preparedStatement = serverConnection.prepareStatement("SELECT * FROM users_table WHERE login = ?;");
            preparedStatement.setString(1, login);
            ResultSet hasUser = preparedStatement.executeQuery();
            if (!hasUser.next()) {
                preparedStatement = serverConnection.prepareStatement("INSERT INTO users_table VALUES (?, ?, ?);");
                preparedStatement.setString(1, login);
                encoder.update(password.getBytes());
                preparedStatement.setBytes(2, encoder.digest());
                preparedStatement.setInt(3, 2);
                preparedStatement.execute();

                serverStatement.close();
                serverConnection.close();
                return true;
            }

            serverStatement.close();
            serverConnection.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (serverStatement != null) {
                    serverStatement.close();
                }
                if (serverConnection != null) {
                    serverConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static int deleteUser(String login) {
        try {
            serverConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/users_database", "postgres", "postgres");
            serverStatement = serverConnection.createStatement();

            PreparedStatement preparedStatement = serverConnection.prepareStatement("SELECT * FROM users_table WHERE login = ?;");
            preparedStatement.setString(1, login);
            ResultSet hasUser = preparedStatement.executeQuery();
            if (hasUser.next()) {
                if (hasUser.getInt("user_rights") != 1) {
                    preparedStatement = serverConnection.prepareStatement("DELETE FROM users_table WHERE login = ?;");
                    preparedStatement.setString(1, login);
                    preparedStatement.execute();
                    serverStatement.close();
                    serverConnection.close();
                    return 0;
                } else {
                    return 1;
                }
            }

            serverStatement.close();
            serverConnection.close();
            return 2;
        } catch (SQLException e) {
            e.printStackTrace();
            return 3;
        } finally {
            try {
                if (serverStatement != null) {
                    serverStatement.close();
                }
                if (serverConnection != null) {
                    serverConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}