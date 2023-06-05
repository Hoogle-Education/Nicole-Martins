package dao.impl;

import dao.ProductsDao;
import db.DbAccess;
import exceptions.DbException;
import models.Product;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductsDao {
    private Connection connection;
    public ProductDaoImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<Product> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sqlQuery = "SELECT * from products";
            st = connection.prepareStatement(sqlQuery);
            rs = st.executeQuery();

            List<Product> products = new ArrayList<>();

            while(rs.next()) {
                Product product = new Product(rs);
                products.add(product);
            }

            return products;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbAccess.closeResultSet(rs);
            DbAccess.closePreparedStatement(st);
        }
    }

    @Override
    public Product findById(int id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sqlQuery = "SELECT * from products" +
                    " WHERE id = ?";
            st = connection.prepareStatement(sqlQuery);
            st.setInt(1, id);
            rs = st.executeQuery();
            Product product = null;

            if(rs.next()) {
                product = new Product(rs);
            }

            return product;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbAccess.closeResultSet(rs);
            DbAccess.closePreparedStatement(st);
        }
    }

    @Override
    public void save(Product product) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sqlQuery = "insert into products (name, price) " +
                    "values (?, ?);";

            st = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, product.getName());
            st.setDouble(2, product.getPrice());
            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if(rs.next()) {
                    int id = rs.getInt(1);
                    product.setId(id);
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
    public void update(Product product) {
        PreparedStatement st = null;

        try{
            String sqlQuery = "UPDATE products " +
                    "SET name=?, price=? " +
                    "WHERE id = ?";

            st = connection.prepareStatement(sqlQuery);
            st.setString(1, product.getName());
            st.setDouble(2, product.getPrice());

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
                "DELETE FROM products WHERE id = ?;",
                "DELETE FROM users_products WHERE product_id = ?;"
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

}
