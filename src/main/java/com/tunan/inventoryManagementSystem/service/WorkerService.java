package com.tunan.inventoryManagementSystem.service;

import com.tunan.inventoryManagementSystem.entity.VO.PurchaserVO;
import com.tunan.inventoryManagementSystem.entity.VO.SalesmanVO;

import java.util.List;

public interface WorkerService {

    int getPurchaserCounts();

    int getSalesmanCounts();

    List<PurchaserVO> pageQueryPurchasers(Integer pageNum, Integer pageSize);

    List<PurchaserVO> getConditionalPurchasers(PurchaserVO purchaserVO, Integer pageNum, Integer pageSize);

    boolean addPurchaser(PurchaserVO purchaserVO);

    boolean updatePurchaser(PurchaserVO purchaserVO);

    boolean deletePurchaser(Long id);

    List<SalesmanVO> pageQuerySalesman(Integer pageNum, Integer pageSize);

    List<SalesmanVO> getConditionalSalesman(SalesmanVO salesmanVO, Integer pageNum, Integer pageSize);

    boolean addSalesman(SalesmanVO salesmanVO);

    boolean updateSalesman(SalesmanVO salesmanVO);

    boolean deleteSalesman(Long id);

}
