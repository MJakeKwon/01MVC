package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	public Map<String,Object> getProductList(SearchVO searchVO) throws Exception{
		
//		Connection con = DBUtil.getConnection();
//		
//		String sql = "SELECT ptj.rn, pd.prod_no, pd.prod_name, pd.price, pd.reg_date, NVL(ts.tran_status_code,0) tran_code, ptj.count "
//				           + "FROM product pd LEFT JOIN transaction ts ON pd.prod_no = ts.prod_no, "
//				           + "(SELECT prod_no, ROWNUM rn, COUNT(*) OVER() count FROM product ";
//		
//		if (searchVO.getSearchCondition() != null) {
//			if (searchVO.getSearchCondition().equals("0") &&  !searchVO.getSearchKeyword().equals("")) {
//				sql += " WHERE prod_no='" + searchVO.getSearchKeyword()
//						+ "'";
//			} else if (searchVO.getSearchCondition().equals("1") &&  !searchVO.getSearchKeyword().equals("")) {
//				sql += "WHERE prod_name LIKE '%" + searchVO.getSearchKeyword()  + "%'";
//				
//			}else if (searchVO.getSearchCondition().equals("2") &&  !searchVO.getSearchKeyword().equals("")){
//				sql += "WHERE price='"+searchVO.getSearchKeyword()+"'";
//			}
//		}
//			sql += ") ptj WHERE ptj.prod_no = pd.prod_no AND ptj.rn BETWEEN ? AND ? ORDER BY pd.prod_no";
//		
//			PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//
//			// 매개변수 할당
//			stmt.setInt(1, searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit() + 1);
//			stmt.setInt(2, searchVO.getPage() * searchVO.getPageUnit());
//
//			ResultSet rs = stmt.executeQuery();
//
//			rs.last();
//			int total = rs.getRow();
//			System.out.println(total);
//			Map<String,Object> map = new HashMap<String,Object>();
//			map.put("count", new Integer(total));
//
//			rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
//			System.out.println("searchVO.getPage():" + searchVO.getPage());
//			System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());
//
//			ArrayList<ProductVO> list = new ArrayList<ProductVO>();
		
		//DB 연결
	      Connection con = DBUtil.getConnection();
	      
	      //SQL 작성
	      String sql = "select * from PRODUCT ";
	      
	      //검색어 확인
	      if (searchVO.getSearchCondition() != null) 
	      {
	         if(searchVO.getSearchCondition().equals("0")) 
	         {
	            //상품번호
	            sql += " where PROD_NO LIKE('%" + searchVO.getSearchKeyword() + "%')";
	            
	         }else if (searchVO.getSearchCondition().equals("1")) 
	         {
	            //상품이름
	            sql += " where PROD_NAME LIKE('%" + searchVO.getSearchKeyword() + "%')";
	            
	         }else if (searchVO.getSearchCondition().equals("2")) 
	         {
	            //상품가격
	            sql += " where PRICE = '" + searchVO.getSearchKeyword() + "'";
	            
	         }
	      }
	      
	      sql += " order by PROD_NO";

	      //DB SQL 전송
	      PreparedStatement stmt1      
	         = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	      
	      ResultSet rs   = stmt1.executeQuery();

	      //마지막 결과로 커서 이동
	      rs.last();
	      
	      //총 갯수
	      int total = rs.getRow();
	      System.out.println("로우의 수:" + total);

	      //맵에 count로 총 개수 맵핑
	      HashMap<String,Object> map = new HashMap<String,Object>();
	      map.put("count", new Integer(total));
	      
	      //페이지번호 X 
	      rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
	      
	      System.out.println("searchVO.getPage():"       + searchVO.getPage()      );
	      System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit()   );

	      ArrayList<ProductVO> list = new ArrayList<ProductVO>();
		
			if (total > 0) {
				for (int i = 0; i < searchVO.getPageUnit(); i++) 
		         {
		            
		            ProductVO prodVO = new ProductVO();
		            
		            //데이터 VO에 set
		            prodVO.setProdNo      (rs.getInt      ("PROD_NO"         ));
		            prodVO.setProdName      (rs.getString   ("PROD_NAME"      ));
		            prodVO.setProdDetail   (rs.getString   ("PROD_DETAIL"      ));
		            prodVO.setManuDate      (rs.getString   ("MANUFACTURE_DAY"   ));
		            prodVO.setPrice         (rs.getInt      ("prod_no"         ));
		            prodVO.setFileName      (rs.getString   ("IMAGE_FILE"      ));
		            prodVO.setRegDate      (rs.getDate      ("REG_DATE"         ));
		            
		            String sqlPurchase = "select TRAN_STATUS_CODE from transaction where prod_no = ?";
		            PreparedStatement stmt2   = con.prepareStatement(sqlPurchase);
		            
		            stmt2.setInt(1, prodVO.getProdNo());
		            ResultSet rs1   = stmt2.executeQuery();
		            
		            if (!rs1.next())
		            {
		               prodVO.setProTranCode("0");
		            }else
		            {
		               prodVO.setProTranCode(rs1.getString("TRAN_STATUS_CODE").trim());
		            }
		            
		            System.out.println(prodVO);
		            
		            list.add(prodVO);
		            
		            if (!rs.next())
		            {
		               break;
		            }
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
