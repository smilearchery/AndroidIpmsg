package ipmsg.domain;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataPacket
{

	private String version;
	private int commandNo;
	private int packetNo;
	private String senderName = null;
	private String senderHost = null;
	private String additional = null;
	private String ip = null;
	private String fileNo = null;
	private String fileName = null;
	private String length = null;
	private String lastEditTime = null;
	private String filePro = null;

	public DataPacket()
	{

	}

	public DataPacket(int commandNo)
	{
		this.commandNo = commandNo;
		this.packetNo = (int) new Date().getTime();
		this.version = IpMsgConstant.IPMSG_VERSION;
		this.senderName = SystemVar.USER_NAME;
		this.senderHost = SystemVar.HOST_NAME;
	}

	public String getAdditional()
	{
		return additional;
	}

	public void setAdditional(String additional)
	{
		this.additional = additional;
	}

	public int getCommandNo()
	{
		return commandNo;
	}

	public void setCommandNo(int commandNo)
	{
		this.commandNo = commandNo;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public int getPacketNo()
	{
		return packetNo;
	}

	public void setPacketNo(int packetNo)
	{
		this.packetNo = packetNo;
	}

	public String getSenderHost()
	{
		return senderHost;
	}

	public void setSenderHost(String senderHost)
	{
		this.senderHost = senderHost;
	}

	public String getSenderName()
	{
		return senderName;
	}

	public void setSenderName(String senderName)
	{
		this.senderName = senderName;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getFilePro()
	{
		return filePro;
	}

	public void setFilePro(String filePro)
	{
		this.filePro = filePro;
	}

	public String getLastEditTime()
	{
		return lastEditTime;
	}

	public void setLastEditTime(String lastEditTime)
	{
		this.lastEditTime = lastEditTime;
	}

	public String getLength()
	{
		return length;
	}

	public void setLength(String length)
	{
		this.length = length;
	}

	public String getFileNo()
	{
		return fileNo;
	}

	public void setFileNo(String fileNo)
	{
		this.fileNo = fileNo;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/*
	 * Low 8 bits from command number 32 bits
	 */
	public int getCommandFunction()
	{
		return commandNo & 0x000000FF;
	}

	/*
	 * (High 24 bits from command number 32 bits)
	 */
	public int getOption()
	{
		return commandNo & 0xFFFFFF00;
	}

	/*
	 * 转换输入的二进制流为数据包
	 */
	public static DataPacket createDataPacket(byte[] data, String ip)
	{
		if (data == null || ip == null)
		{
			return null;
		}
		try
		{
			char a = '\0';
			String dataStr = new String(data, SystemVar.DEFAULT_CHARACT);
			String[] dataArrs = dataStr.split(Character.toString(a));
			String[] dataArr = dataArrs[0].split(":");
			if (dataArr == null)
			{
				return null;
			}
			else
			{
				DataPacket packet = new DataPacket();
				packet.setVersion(dataArr.length >= 1 ? dataArr[0] : "");
				packet.setPacketNo((dataArr.length >= 2 ? Integer.parseInt(dataArr[1]) : -1));
				packet.setSenderName(dataArr.length >= 3 ? dataArr[2] : "");
				packet.setSenderHost(dataArr.length >= 4 ? dataArr[3] : "");
				packet.setCommandNo(dataArr.length >= 5 ? Integer.parseInt(dataArr[4]) : 0);
				packet.setAdditional(dataArr.length >= 6 ? dataArr[5] : "");
				packet.setIp(ip);
				if (dataArrs.length > 1)
				{
					String[] fileArr = dataArrs[1].split(":");
					if (fileArr.length > 2)
					{
						packet.setFileNo(fileArr[0]);
						packet.setFileName(fileArr[1]);
						packet.setLength(fileArr[2]);
						packet.setLastEditTime(fileArr[3]);
						packet.setFilePro(fileArr[4]);
					}
				}
				return packet;
			}
		}
		/*
		 * catch (UnsupportedEncodingException ex) {
		 * Logger.getLogger(DataPacket.class.getName()).log(Level.SEVERE, null,
		 * ex); return null; }
		 */
		catch (Exception e)
		{
			Logger.getLogger(DataPacket.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
	}

	public String toString()
	{
		return version + ":" + packetNo + ":" + senderName + ":" + senderHost + ":" + commandNo + ":" + additional;
	}

}