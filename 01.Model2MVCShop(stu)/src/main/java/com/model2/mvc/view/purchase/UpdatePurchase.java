package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdatePurchase extends Action {
	
	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		PurchaseVO purchaseVO = new PurchaseVO();
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDivyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDivyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setDivyDate(request.getParameter("divyDate"));
		purchaseVO.setTranNo(Integer.parseInt(request.getParameter("tranNo")));
		
		PurchaseService service = new PurchaseServiceImpl();
		service.updatePurchase(purchaseVO);
		
		return "forward:/getPurchase.do?tranNo=" + request.getParameter("tranNo");
	}

}
