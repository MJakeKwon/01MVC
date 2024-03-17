package com.model2.mvc.framework;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class RequestMapping {
	//Singleton 패턴을 구현하기 위한 정적 변수로, RequestMapping 클래스의 유일한 인스턴스를 저장합니다.
	private static RequestMapping requestMapping;
	//URL 경로와 그에 해당하는 Action 객체를 매핑하기 위한 Map입니다
	private Map<String, Action> map;
	private Properties properties;
	
	//생성자로, 주어진 자원 파일로부터 URL 경로와 그에 해당하는 처리를 매핑합니다.
	private RequestMapping(String resources) {
		map = new HashMap<String, Action>();
		InputStream in = null;
		try{
			in = getClass().getClassLoader().getResourceAsStream(resources);
			properties = new Properties();
			properties.load(in);
		}catch(Exception ex){
			System.out.println(ex);
			throw new RuntimeException("actionmapping.properties 파일 로딩 실패 :"  + ex);
		}finally{
			if(in != null){
				try{ in.close(); } catch(Exception ex){}
			}
		}
	}
	
	//Singleton 인스턴스를 얻기 위한 정적 메서드로, 인스턴스가 없는 경우에만 새로운 인스턴스를 생성합니다.
	public synchronized static RequestMapping getInstance(String resources){
		if(requestMapping == null){
			requestMapping = new RequestMapping(resources);
		}
		return requestMapping;
	}
	
	//주어진 URL 경로에 해당하는 Action 객체를 반환합니다.
	//만약 map에서 해당 경로에 대한 Action 객체가 없는 경우, 
	//properties에서 클래스 이름을 가져와 인스턴스를 생성합니다.
	//생성된 인스턴스를 map에 저장하고 반환합니다.
	public Action getAction(String path){
		Action action = map.get(path);
		if(action == null){
			String className = properties.getProperty(path);
			System.out.println("prop : " + properties);
			System.out.println("path : " + path);			
			System.out.println("className : " + className);
			className = className.trim();
			try{
				Class c = Class.forName(className);
				Object obj = c.newInstance();
				if(obj instanceof Action){
					map.put(path, (Action)obj);
					action = (Action)obj;
				}else{
					throw new ClassCastException("Class형변환시 오류 발생  ");
				}
			}catch(Exception ex){
				System.out.println(ex);
				throw new RuntimeException("Action정보를 구하는 도중 오류 발생 : " + ex);
			}
		}
		return action;
	}
}