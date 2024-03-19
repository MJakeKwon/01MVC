package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.dao.ProductDao;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.dao.UserDAO;

public class PurchaseDao {

	public PurchaseVO findPurchase(int tranNo) throws Exception{
		
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM transaction WHERE tran_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);

		ResultSet rs = stmt.executeQuery();
		
		PurchaseVO purchaseVO = null;
		while (rs.next()) {
		    if (purchaseVO == null) {
		        purchaseVO = new PurchaseVO(); // PurchaseVO 객체 생성
		    }
		    purchaseVO.setTranNo(rs.getInt("tran_no"));
			purchaseVO.setPurchaseProd(new ProductDao().findProduct(rs.getInt("prod_no")));
			purchaseVO.setBuyer(new UserDAO().findUser(rs.getString("buyer_id")));
			purchaseVO.setPaymentOption(rs.getString("payment_option"));
			purchaseVO.setReceiverName(rs.getString("receiver_name"));
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
			purchaseVO.setDivyAddr(rs.getString("demailaddr"));
			purchaseVO.setDivyRequest(rs.getString("dlvy_request"));
			purchaseVO.setTranCode(rs.getString("tran_status_code").trim());
			purchaseVO.setOrderDate(rs.getDate("order_data"));
			purchaseVO.setDivyDate(rs.getString("dlvy_date"));

		    System.out.println("find");
		}

//		while (rs.next()) {
//			purchaseVO.setTranNo(rs.getInt("tran_no"));
//			purchaseVO.setPurchaseProd(new ProductDao().findProduct(rs.getInt("prod_no")));
//			purchaseVO.setBuyer(new UserDAO().findUser(rs.getString("buyer_id")));
//			purchaseVO.setPaymentOption(rs.getString("payment_option"));
//			purchaseVO.setReceiverName(rs.getString("receiver_name"));
//			purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
//			purchaseVO.setDivyAddr(rs.getString("demailaddr"));
//			purchaseVO.setDivyRequest(rs.getString("dlvy_request"));
//			purchaseVO.setTranCode(rs.getString("tran_status_code").trim());
//			purchaseVO.setOrderDate(rs.getDate("order_data"));
//			purchaseVO.setDivyDate(rs.getString("dlvy_date"));
//
//			System.out.println("find");
//		}
		con.close();
		System.out.println("purchaseFind: " + purchaseVO);
		return purchaseVO;
	}
	
	public Map<String, Object> getPurchaseList(SearchVO searchVO, String buyerId) throws Exception{
		
		Connection con = DBUtil.getConnection();

		String sql = 
				"SELECT * FROM "
				+ "("
				+ "	SELECT ROW_NUMBER() OVER (ORDER BY user_id) AS rn, "
				+ "	ts.*, u.user_id ,NVL(ts.tran_status_code, 0) AS tran_code, "
				+ "	COUNT(*) OVER () AS count "
				+ "	FROM users u "
				+ "	INNER JOIN transaction ts ON "
				+ "	u.user_id = ts.buyer_id "
				+ "	WHERE u.user_id = ? "
				+ ") "
				+ "WHERE rn BETWEEN ? AND ?";
		
		PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		// 매개변수 할당
		stmt.setString(1, buyerId); // buyerId 매개변수 할당
		stmt.setInt(2, searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit() + 1); // 페이지네이션 시작 값
		stmt.setInt(3, searchVO.getPage() * searchVO.getPageUnit()); // 페이지네이션 종료 값
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery();
		
		rs.last();
		int total = rs.getRow();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", new Integer(total));
		
		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
		System.out.println("searchVO.getPage():" + searchVO.getPage());
		System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());
		
		List<PurchaseVO> list = new ArrayList<PurchaseVO>();
		if (total > 0) {
			for (int i = 0; i < searchVO.getPageUnit(); i++) {
				PurchaseVO purchaseVO = new PurchaseVO();
				purchaseVO.setTranNo(rs.getInt("tran_no"));
				purchaseVO.setPurchaseProd(new ProductDao().findProduct(rs.getInt("prod_no")));
				purchaseVO.setBuyer(new UserDAO().findUser(rs.getString("user_id")));
				purchaseVO.setPaymentOption(rs.getString("payment_option"));
				purchaseVO.setReceiverName(rs.getString("receiver_name"));
				purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
				purchaseVO.setDivyAddr(rs.getString("demailaddr"));
				purchaseVO.setDivyRequest(rs.getString("dlvy_request"));
				purchaseVO.setTranCode(rs.getString("tran_code").trim());
				purchaseVO.setOrderDate(rs.getDate("order_data"));
				purchaseVO.setDivyDate(rs.getString("dlvy_date"));

				list.add(purchaseVO);
				if (!rs.next())
					break;
			}
		}
		System.out.println("list.size() : "+ list.size());
		System.out.println("map().size() : "+ map.size());
		
		map.put("list", list);

		con.close();
		System.out.println("찾기완료");
		
		return map;
	}
	
	public Map<String, Object> getSaleList(SearchVO searchVO) throws Exception{
		
		Connection con = DBUtil.getConnection();

		PreparedStatement stmt = con.prepareStatement("SELECT * FROM transaction", ResultSet.TYPE_SCROLL_INSENSITIVE,
											ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt.executeQuery();
		
		rs.last();
		int total = rs.getRow();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", new Integer(total));

		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit() + 1);
		List<PurchaseVO> list = new ArrayList<PurchaseVO>();

		if (total > 0) {
			for (int i = 0; i < searchVO.getPageUnit(); i++) {
				PurchaseVO purchaseVO = new PurchaseVO();

				purchaseVO.setTranNo(rs.getInt("tran_no"));
				purchaseVO.setBuyer(new UserDAO().findUser(rs.getString("buyer_id")));
				purchaseVO.setDivyAddr(rs.getString("demailaddr"));
				purchaseVO.setDivyDate(rs.getString("dlvy_date"));
				purchaseVO.setDivyRequest(rs.getString("dlvy_request"));
				purchaseVO.setOrderDate(rs.getDate("order_date"));
				purchaseVO.setPaymentOption(rs.getString("payment_option"));
				purchaseVO.setPurchaseProd(new ProductDao().findProduct(rs.getInt("prod_no")));
				purchaseVO.setReceiverName(rs.getString("receiver_name"));
				purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
				purchaseVO.setTranCode(rs.getString("tran_status_code"));

				list.add(purchaseVO);

				if (!rs.next())
					break;
			}
		}

		map.put("list", list);
		con.close();

		return map;
	}
	
	public void insertPurhcase(PurchaseVO purchaseVO) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "INSERT INTO transaction VALUES (seq_transaction_tran_no.nextval,?,?,?,?,?,?,?,1,SYSDATE,?)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, purchaseVO.getPurchaseProd().getProdNo());
		stmt.setString(2,  purchaseVO.getBuyer().getUserId());
		stmt.setString(3,  purchaseVO.getPaymentOption());
		stmt.setString(4,  purchaseVO.getReceiverName());
		stmt.setString(5,  purchaseVO.getReceiverPhone());
		stmt.setString(6,  purchaseVO.getDivyAddr());
		stmt.setString(7,  purchaseVO.getDivyRequest());
		stmt.setString(8,  purchaseVO.getDivyDate());
	
		stmt.executeUpdate();
		System.out.println("거래 등록 완료");
		con.close();
	}
	
	public void updatePurchase(PurchaseVO purchaseVO) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "UPDATE transaction SET payment_option=?,receiver_name=?,receiver_phone=?,demailaddr=?,dlvy_request=?,dlvy_date=? WHERE tran_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, purchaseVO.getPaymentOption());
		stmt.setString(2, purchaseVO.getReceiverName());
		stmt.setString(3, purchaseVO.getReceiverPhone());
		stmt.setString(4, purchaseVO.getDivyAddr());
		stmt.setString(5, purchaseVO.getDivyRequest());
		stmt.setString(6, purchaseVO.getDivyDate());
		stmt.setInt(7, purchaseVO.getTranNo());

		stmt.executeUpdate();

		System.out.println("DB Update 완료");

		con.close();
	}
	
	public void updateTranCode(PurchaseVO purchaseVO) throws Exception{
		
		Connection con = DBUtil.getConnection();
		String sql ="UPDATE transaction SET tran_status_code=? WHERE tran_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, purchaseVO.getTranCode());
		stmt.setInt(2, purchaseVO.getTranNo());
		
		stmt.executeUpdate();
		System.out.println("배송상태 번호 : "+purchaseVO.getTranCode());
		con.close();
	}
	
}
