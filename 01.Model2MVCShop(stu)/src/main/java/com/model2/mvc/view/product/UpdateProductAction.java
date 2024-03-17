package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;


public class UpdateProductAction extends Action {

	@Override
	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {
		int prodNo =(Integer.parseInt(request.getParameter("prodNo")));
		
		ProductVO prodVO=new ProductVO();
		
		prodVO.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
		prodVO.setProdName(request.getParameter("prodName"));
		prodVO.setProdDetail(request.getParameter("prodDetail"));
		prodVO.setFileName(request.getParameter("fileName"));
		prodVO.setManuDate(request.getParameter("manuDate").replace("-", ""));
		prodVO.setPrice(Integer.parseInt(request.getParameter("price")));
		
		ProductService service=new ProductServiceImpl();
		
		service.updateProduct(prodVO);
		request.setAttribute("prodVO", prodVO);
	
		
		return "redirect:/getProduct.do?prodNo="+prodNo;
	}
}