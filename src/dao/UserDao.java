package dao;

import models.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();
    User findById(int id);
    void save(User user);
    void update(User user);
    void deleteById(int id);
    void addProduct(int userId, int productId);
    void removeProduct(int userId, int productId);
}
