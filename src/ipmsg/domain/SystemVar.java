package ipmsg.domain;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 
 * 系统环境变量
 * 
 */
public class SystemVar
{

	// 信号量
	// public static Semaphore PACKET_QUEUE_FULL = new Semaphore(0);
	// public static Semaphore PACKET_QUEUE_EMPTY = new Semaphore(100); //
	// 队列最大容量

	// 运行时可变变量
	public static String USER_NAME = "Tester"; // 显示的用户名称
	public static String HOST_NAME = "Tester"; // 显示的主机名称
	public static String HOST_IP;

	private static List<UsersVo> USER_LIST; // 用户列表

	// private static IUserListGui userListGui; // 用户列表界面操作

	// 运行时不变参数
	// public static String USER_HOME; // 用户工作路径
	public static String DEFAULT_CHARACT; // 默认编码
	public static String LINE_SEPARATOR; // 换行标识
	public static String FILE_SEPARATOR; // 文件分割标识
	public static char OS; // 操作系统

	/**
	 * 系统参数初始化
	 */
	public SystemVar()
	{
		LINE_SEPARATOR = System.getProperty("line.separator");
		FILE_SEPARATOR = System.getProperty("file.separator");

		if (System.getProperty("os.name").equalsIgnoreCase("linux"))
		{
			OS = IpMsgConstant.OS_LINUX;
		}
		else if (System.getProperty("os.name").equalsIgnoreCase("window"))
		{
			OS = IpMsgConstant.OS_WINDOWS;
		}
		else
		{
			OS = IpMsgConstant.OS_OTHER;
		}

		DEFAULT_CHARACT = "gbk";

		HOST_IP = getLocalIpAddress();

		USER_LIST = new ArrayList<UsersVo>();

	}

	public String getLocalIpAddress()
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

	/*
	 * 获得在线用户的集合
	 */
	public static List<UsersVo> getUserList()
	{
		return SystemVar.USER_LIST;
	}

	/*
	 * 设置在线用户集合
	 */
	public static void setUserList(List<UsersVo> userList)
	{
		SystemVar.USER_LIST = userList;
	}

	/*
	 * 向在线用户集合中添加用户
	 */
	public static boolean addUsers(UsersVo user)
	{
		for (int i = 0; i < USER_LIST.size(); i++)
		{
			if (USER_LIST.get(i).getIp().equals(user.getIp()))
			{
				USER_LIST.set(i, user);
				return false;
			}
		}
		USER_LIST.add(user);
		return true;
	}

	/*
	 * 清空现在用户集合
	 */
	public static void clearUsers()
	{
		USER_LIST.clear();
	}

	/*
	 * 获得有该ip的用户所在的索引号
	 */
	public static int getUserIndex(String ip)
	{
		for (int i = 0; i < USER_LIST.size(); i++)
		{
			if (USER_LIST.get(i).getIp().equals(ip))
			{
				return i;
			}
		}
		return 0;
	}

}
