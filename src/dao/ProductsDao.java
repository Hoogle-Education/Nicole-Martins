package dao;

import models.Product;
import models.User;

import java.util.List;

public interface ProductsDao {
    List<Product> findAll();
    Product findById(int id);
    void save(Product product);
    void update(Product product);
    void deleteById(int id);

}
