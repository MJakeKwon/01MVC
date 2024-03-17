package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.user.vo.UserVO;

public class ProductDao {
	
	public ProductDao() {
	}
	
	public void insertProduct(ProductVO prodVO) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "INSERT INTO product VALUES(seq_product_prod_no.nextval,?,?,?,?,?,SYSDATE)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setString(1, prodVO.getProdName());
		stmt.setString(2, prodVO.getProdDetail());
		stmt.setString(3, prodVO.getManuDate());
		stmt.setInt(4, prodVO.getPrice());
		stmt.setString(5, prodVO.getFileName());
		
		System.out.println(prodVO);
		System.out.println("상품추가");
		
		stmt.executeUpdate();
		
		con.close();
	}
	
	public ProductVO findProduct(int prodNo) throws Exception{
		
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM product WHERE prod_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();
		ProductVO prodVO = null;
		while (rs.next()) {
			prodVO = new ProductVO();
			prodVO.setProdNo(rs.getInt("prod_no"));
			prodVO.setProdName(rs.getString("prod_name"));
			prodVO.setProdDetail(rs.getString("prod_detail"));
			prodVO.setManuDate(rs.getString("manufacture_day"));
			prodVO.setPrice(rs.getInt("price"));
			prodVO.setFileName(rs.getString("image_file"));
			prodVO.setRegDate(rs.getDate("REG_DATE"));
			System.out.println("find");
		}
		con.close();
		System.out.println("상품찾기완료");
		return prodVO;
	}
	public HashMap<String,Object> getProductList(SearchVO searchVO) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM product";
		if (searchVO.getSearchCondition() != null) {
			if (searchVO.getSearchCondition().equals("0") &&  !searchVO.getSearchKeyword().equals("")) {
				sql += " WHERE prod_no='" + searchVO.getSearchKeyword()
						+ "'";
			} else if (searchVO.getSearchCondition().equals("1") &&  !searchVO.getSearchKeyword().equals("")) {
				sql += "WHERE prod_name LIKE '%" + searchVO.getSearchKeyword()  + "%'";
				
			}else if (searchVO.getSearchCondition().equals("2") &&  !searchVO.getSearchKeyword().equals("")){
				sql += "WHERE price='"+searchVO.getSearchKeyword()+"'";
			}
		}
			sql += " ORDER BY prod_no";
		
			PreparedStatement stmt = 
			con.prepareStatement(	sql, ResultSet.TYPE_SCROLL_INSENSITIVE, 
												 ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery();
			rs.last();
			int total = rs.getRow();

			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("count", new Integer(total));

			rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
			System.out.println("searchVO.getPage():" + searchVO.getPage());
			System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());

			ArrayList<ProductVO> list = new ArrayList<ProductVO>();
			if (total > 0) {
				for (int i = 0; i < searchVO.getPageUnit(); i++) {
					ProductVO prodVO = new ProductVO();
					prodVO.setProdNo(rs.getInt("prod_no"));
					prodVO.setProdName(rs.getString("prod_name"));
					prodVO.setProdDetail(rs.getString("prod_detail"));
					prodVO.setManuDate(rs.getString("manufacture_day"));
					prodVO.setPrice(rs.getInt("price"));
					prodVO.setFileName(rs.getString("image_file"));
					prodVO.setRegDate(rs.getDate("REG_DATE"));

					list.add(prodVO);
					if (!rs.next())
						break;
				}
			}
			System.out.println("list test :"+list);
			System.out.println("list.size() : "+ list.size());
			map.put("list", list);
			System.out.println("map().size() : "+ map.size());

			con.close();
		System.out.println("찾기완료");
		return map;
	}
	
public void updateProduct(ProductVO prodVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE product SET prod_name=?,prod_detail=?,price=?,manufacture_day=?, image_file =?,reg_date=SYSDATE WHERE prod_no=?";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, prodVO.getProdName());
		stmt.setString(2, prodVO.getProdDetail());
		stmt.setInt(3, prodVO.getPrice());
		stmt.setString(4, prodVO.getManuDate());
		stmt.setString(5, prodVO.getFileName());
		stmt.setInt(6, prodVO.getProdNo());
		
		stmt.executeUpdate();
		System.out.println("업데이트완료");
		con.close();
	}
}
