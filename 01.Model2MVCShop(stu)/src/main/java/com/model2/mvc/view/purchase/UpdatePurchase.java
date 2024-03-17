package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdatePurchase extends Action {
	
	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		PurchaseVO purchaseVO = new PurchaseVO();
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		
		
		return "forward:/getPurchase.do?tranNo=" + request.getParameter("tranNo");
	}

}
