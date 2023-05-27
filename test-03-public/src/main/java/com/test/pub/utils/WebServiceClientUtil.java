/**
 * AsiaInfo-Linkage, Inc.<BR>
 * Copyright 2009-2013. All right reserved.
 */

package com.test.pub.utils;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import java.net.URL;

/**
 * webservice 连接工具类
 * 
 * @author zhukun (AILK No.77150)
 * @version 1.0
 * @since 2021年5月13日
 * @category com.linkage.application.business.js.vlan.newassgin.util
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class WebServiceClientUtil
{

	static Logger logger = LoggerFactory.getLogger(WebServiceClientUtil.class);
	public static final String XMLSCHEMA = "http://www.w3.org/2001/XMLSchema";

	/**
	 * webservice
	 */
	public static String sendWsdl(String param, String url, String method, String qname)
	{
		logger.info("--------调用webservice接口begin-------");
		// 创建动态客户端
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		logger.info("webservice 动态接口调用方式： " + url + "method ： " + method + "qname : "
				+ qname);
		// 对方的wsdl地址
		final Client client = dcf.createClient(url);
		String json = null;
		try
		{
			QName qName = new QName(qname, method);
			Object[] objects1 = client.invoke(qName, param); // 参数1，参数2，参数3......按顺序放就看可以
			json = String.valueOf(objects1[0]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.info("服务器断开连接，请稍后再试");
			return reSendWsdl(param,url,method,qname,1);
		}
		finally
		{
			try
			{
//				client.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		logger.info("--------调用webservice接口end-------");
		return json;
	}

	public static String reSendWsdl(String param, String url, String method, String qname,int count) {
		try {
			Thread.sleep(30000);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("重试次数="+count);
		logger.info("--------调用webservice接口begin-------");
		// 创建动态客户端
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		logger.info("webservice 动态接口调用方式： " + url + "method ： " + method + "qname : "
				+ qname);
		// 对方的wsdl地址
		final Client client = dcf.createClient(url);
		String json = null;
		try
		{
			QName qName = new QName(qname, method);
			Object[] objects1 = client.invoke(qName, param); // 参数1，参数2，参数3......按顺序放就看可以
			json = String.valueOf(objects1[0]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.info("服务器断开连接，请稍后再试");
			if(++ count > 2){
				return reSendWsdl(param,url,method,qname,count);
			}
		}
		finally
		{
			try
			{
//				client.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		logger.info("--------调用webservice接口end-------");
		return json;
	}

	/**
	 * webservice
	 */
	public static String sendWsdl2(String param, String url, String method, String qname)
	{
		String code;
		Service service = new Service();
		Call _call = null;
		String returnParam = "";
		try
		{
			_call = (Call) service.createCall();
			_call.setTargetEndpointAddress(new URL(url));
			QName qn = new QName(qname, method);
			// call.setTimeout(1000*60*30);
			_call.addParameter("arg1", new QName(XMLSCHEMA, "string"),
					ParameterMode.IN);
			_call.setOperationName(qn);
			_call.setReturnType(new QName(XMLSCHEMA, "string"));
			// String msgId = params.get("msgId");
			returnParam = (String) _call.invoke(new Object[] { param });
			logger.info("==========查询光猫是否在线入参：" + param);
			logger.info("=========返回结果：" + returnParam);
		}
		catch (Exception e)
		{
			logger.info(e.getMessage(), e);
		}
		return returnParam;
	}

}
