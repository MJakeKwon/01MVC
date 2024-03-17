package com.model2.mvc.service.product.impl;

import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.dao.ProductDao;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.user.vo.UserVO;

public class ProductServiceImpl implements ProductService {

	private ProductDao proDAO;
	
	public ProductServiceImpl() {
		proDAO = new ProductDao();
	}
	
	public void addProduct(ProductVO prodVO) throws Exception{
		proDAO.insertProduct(prodVO);
	}
	
	public ProductVO getProduct(int prodNo) throws Exception{
		return proDAO.findProduct(prodNo);
	}
	
	public HashMap<String,Object> getProductList(SearchVO searchVO) throws Exception {
		return proDAO.getProductList(searchVO);
	}
	
	public void updateProduct (ProductVO prodVO) throws Exception {
		proDAO.updateProduct(prodVO);
	}
	
	
}
