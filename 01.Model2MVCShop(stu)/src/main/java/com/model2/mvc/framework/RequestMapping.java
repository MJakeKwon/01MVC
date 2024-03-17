package com.model2.mvc.framework;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class RequestMapping {
	//Singleton ������ �����ϱ� ���� ���� ������, RequestMapping Ŭ������ ������ �ν��Ͻ��� �����մϴ�.
	private static RequestMapping requestMapping;
	//URL ��ο� �׿� �ش��ϴ� Action ��ü�� �����ϱ� ���� Map�Դϴ�
	private Map<String, Action> map;
	private Properties properties;
	
	//�����ڷ�, �־��� �ڿ� ���Ϸκ��� URL ��ο� �׿� �ش��ϴ� ó���� �����մϴ�.
	private RequestMapping(String resources) {
		map = new HashMap<String, Action>();
		InputStream in = null;
		try{
			in = getClass().getClassLoader().getResourceAsStream(resources);
			properties = new Properties();
			properties.load(in);
		}catch(Exception ex){
			System.out.println(ex);
			throw new RuntimeException("actionmapping.properties ���� �ε� ���� :"  + ex);
		}finally{
			if(in != null){
				try{ in.close(); } catch(Exception ex){}
			}
		}
	}
	
	//Singleton �ν��Ͻ��� ��� ���� ���� �޼����, �ν��Ͻ��� ���� ��쿡�� ���ο� �ν��Ͻ��� �����մϴ�.
	public synchronized static RequestMapping getInstance(String resources){
		if(requestMapping == null){
			requestMapping = new RequestMapping(resources);
		}
		return requestMapping;
	}
	
	//�־��� URL ��ο� �ش��ϴ� Action ��ü�� ��ȯ�մϴ�.
	//���� map���� �ش� ��ο� ���� Action ��ü�� ���� ���, 
	//properties���� Ŭ���� �̸��� ������ �ν��Ͻ��� �����մϴ�.
	//������ �ν��Ͻ��� map�� �����ϰ� ��ȯ�մϴ�.
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
					throw new ClassCastException("Class����ȯ�� ���� �߻�  ");
				}
			}catch(Exception ex){
				System.out.println(ex);
				throw new RuntimeException("Action������ ���ϴ� ���� ���� �߻� : " + ex);
			}
		}
		return action;
	}
}