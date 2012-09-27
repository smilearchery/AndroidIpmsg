/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ipmsg.util;

import ipmsg.domain.DataPacket;
import ipmsg.domain.IpMsgConstant;
import ipmsg.domain.SocketManage;
import ipmsg.domain.SystemVar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * 网络工具类
 * 
 * @author Sheldon wang
 */
public class NetUtil
{

	/**
	 * 判断端口是否被占用
	 * 
	 * @return
	 */
	public static boolean checkPort()
	{
		try
		{
			new DatagramSocket(IpMsgConstant.IPMSG_DEFAULT_PORT).close();
			return true;
		}
		catch (SocketException ex)
		{
			return false;
		}
	}

	/**
	 * 发送UDP数据包
	 * 
	 * @param dataPacket
	 *            封装的数据包
	 * @param targetIp
	 *            目标IP
	 */
	public static void sendUdpPacket(DataPacket dataPacket, String targetIp)
	{
		try
		{
			byte[] dataBit = dataPacket.toString().getBytes("gbk");
			DatagramPacket sendPacket = new DatagramPacket(dataBit, dataBit.length, InetAddress.getByName(targetIp), IpMsgConstant.IPMSG_DEFAULT_PORT);
			SocketManage.getInstance().getUdpSocket().send(sendPacket);
		}
		catch (UnsupportedEncodingException ex)
		{
			Logger.getLogger(NetUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		catch (IOException ex)
		{
			Logger.getLogger(NetUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * 发送局域网内广播
	 * 
	 * @param dataPacket
	 */
	public static void broadcastUdpPacket(DataPacket dataPacket)
	{
		sendUdpPacket(dataPacket, "255.255.255.255");
	}

	/**
	 * 获得本机ip
	 * 
	 * @return 本机ip
	 */
	public static String getLocalHostIp()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress();
					}
				}
			}
		}
		catch (SocketException ex)
		{
			// Log.e(LOG_TAG, ex.toString());
		}
		return null;
	}

}
