package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UprdateTranCodeAction extends Action {

	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String tranNo =request.getParameter("tranNo");
		String tranCode = request.getParameter("tranCode");
		
		PurchaseService service = new PurchaseServiceImpl();
		
		PurchaseVO purchaseVO = service.getPurchase(Integer.parseInt(tranNo));
		purchaseVO.setTranCode(tranCode);
		System.out.println(purchaseVO+"업데이트 시작");
		
		service.updateTranCode(purchaseVO);
		System.out.println("업데이트 끝"+purchaseVO);
		
		return "redirect:/listPurchase.do";
	}
}
