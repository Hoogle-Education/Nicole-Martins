package dao.impl;

import dao.DaoFactory;
import dao.ProductsDao;
import dao.UserDao;
import db.DbAccess;
import exceptions.DbException;
import models.Product;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private Connection connection;
    private ProductsDao productRepository;
    public UserDaoImpl(Connection connection) {
        this.connection = connection;
        productRepository = DaoFactory.getProductDao();
    }
    @Override
    public List<User> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sqlUsersQuery = "SELECT * from users";
            String sqlRelationQuery = "SELECT * from users_products";
            st = connection.prepareStatement(sqlUsersQuery);
            rs = st.executeQuery();

            List<User> users = new ArrayList<>();

            while(rs.next()) {
                User user = new User(rs);
                users.add(user);
            }

            st = connection.prepareStatement(sqlRelationQuery);
            rs = st.executeQuery();

            while(rs.next()) {
                int userId = rs.getInt("user_id");
                int productId = rs.getInt("product_id");
                Product product = productRepository.findById(productId);
                users.get(userId - 1).addProduct(product);
            }

            return users;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbAccess.closeResultSet(rs);
            DbAccess.closePreparedStatement(st);
        }
    }

    @Override
    public User findById(int id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sqlUserQuery = "SELECT * from users" +
                              " WHERE id = ?";
            String sqlRelationQuery = "SELECT * from users_products " +
                                        " WHERE user_id = ?";
            st = connection.prepareStatement(sqlUserQuery);
            st.setInt(1, id);
            rs = st.executeQuery();
            User user = null;

            if(rs.next()) {
                user = new User(rs);
            }

            if(user == null) {
                throw new SQLException("User not found");
            }

            st = connection.prepareStatement(sqlRelationQuery);
            st.setInt(1, id);
            rs = st.executeQuery();

            while(rs.next()){
                int productId = rs.getInt("product_id");
                Product product = productRepository.findById(productId);
                user.addProduct(product);
            }

            return user;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbAccess.closeResultSet(rs);
            DbAccess.closePreparedStatement(st);
        }
    }

    @Override
    public void save(User user) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sqlQuery = "insert into users (name, username, email, password, blocked) " +
                    "values (?, ?, ?, ?, ?);";

            st = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, user.getName());
            st.setString(2, user.getUsername());
            st.setString(3, user.getEmail());
            st.setString(4, user.getPassword());
            st.setInt(5, user.isBlocked() ? 1 : 0);
            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if(rs.next()) {
                    int id = rs.getInt(1);
                    user.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbAccess.closeResultSet(rs);
            DbAccess.closePreparedStatement(st);
        }
    }

    @Override
    public void update(User user) {
        PreparedStatement st = null;

        try{
            String sqlQuery = "UPDATE users " +
                    "SET name=?, username=?, email=?, password=? " +
                    "WHERE id = ?";

            st = connection.prepareStatement(sqlQuery);
            st.setString(1, user.getName());
            st.setString(2, user.getUsername());
            st.setString(3, user.getEmail());
            st.setString(4, user.getPassword());
            st.setInt(5, user.getId());

            st.executeUpdate();
        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbAccess.closePreparedStatement(st);
        }
    }

    @Override
    public void deleteById(int id) {
        PreparedStatement st = null;

        try {
            String[] sqlQueries = {
                    "DELETE FROM users WHERE id = ?;",
                    "DELETE FROM users_products WHERE user_id = ?;"
            };

            for (var query : sqlQueries) {
                st = connection.prepareStatement(query);
                st.setInt(1, id);
                st.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbAccess.closePreparedStatement(st);
        }
    }

    @Override
    public void addProduct(int userId, int productId) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            Product product = productRepository.findById(productId);
            User user = findById(userId);

            if(product == null || user == null) {
                throw new SQLException("Invalid id has been informed.");
            }

            String sqlQuery = "insert into users_products (user_id, product_id) " +
                    "values (?, ?);";

            st = connection.prepareStatement(sqlQuery);
            st.setInt(1, userId);
            st.setInt(2, productId);
            int rowsAffected = st.executeUpdate();

            if(rowsAffected < 1) {
                throw new SQLException("Any rows affected.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbAccess.closeResultSet(rs);
            DbAccess.closePreparedStatement(st);
        }
    }

    @Override
    public void removeProduct(int userId, int productId) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            Product product = productRepository.findById(productId);
            User user = findById(userId);

            if(product == null || user == null) {
                throw new SQLException("Invalid id has been informed.");
            }

            String sqlQuery = "delete from users_products " +
                    "where user_id = ? and product_id = ? limit 1;";


            st = connection.prepareStatement(sqlQuery);
            st.setInt(1, userId);
            st.setInt(2, productId);
            int rowsAffected = st.executeUpdate();

            if(rowsAffected < 1) {
                throw new SQLException("Any rows affected.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbAccess.closeResultSet(rs);
            DbAccess.closePreparedStatement(st);
        }
    }
}
