package dao;

import dao.impl.ProductDaoImpl;
import dao.impl.UserDaoImpl;
import db.DbAccess;

public class DaoFactory {

    public static UserDao getUserDao () {
        return new UserDaoImpl(DbAccess.getConnection());
    }

    public static ProductsDao getProductDao() {
        return new ProductDaoImpl(DbAccess.getConnection());
    }

}
