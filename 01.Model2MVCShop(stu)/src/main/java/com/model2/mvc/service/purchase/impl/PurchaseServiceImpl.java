package com.model2.mvc.service.purchase.impl;

import java.util.Map;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDao;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class PurchaseServiceImpl implements PurchaseService{
	
	private PurchaseDao purchaseDao;
	
	public PurchaseServiceImpl() {
		purchaseDao = new PurchaseDao();
	}
	
	public void insertPurchase(PurchaseVO purchaseVO) throws Exception{
		purchaseDao.insertPurhcase(purchaseVO);
	}
	
	public PurchaseVO getPurchase(int tranNo) throws Exception{
		return purchaseDao.findPurchase(tranNo);
	}

	public Map<String, Object> getPurchaseList(SearchVO searchVO, String buyerId) throws Exception{
		return purchaseDao.getPurchaseList(searchVO, buyerId);
	}
	
	public Map<String, Object> getSaleList(SearchVO searchVO) throws Exception{
		return purchaseDao.getSaleList(searchVO);
	}

	public void updatePurchase(PurchaseVO purchaseVO) throws Exception {
		purchaseDao.updatePurchase(purchaseVO);
	}

	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
		purchaseDao.updateTranCode(purchaseVO);
	}

}
