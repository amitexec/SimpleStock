package com.gbce.dao;

/**
 * Entity Manager
 */
public class EntityManager {
    private StockDAO stockDAO;
    private static  EntityManager entityManager;

    private EntityManager(){}

    /**
     * returns singleton entity manager
     * @return
     */
    public static EntityManager getEntityManager(){
        if(entityManager==null){
            entityManager=new EntityManager();
        }
        return entityManager;
    }

    public StockDAO getStockDAO() {
        if(stockDAO==null){
            stockDAO=new StockDAO();
        }
        return stockDAO;
    }
}
