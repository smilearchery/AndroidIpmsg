package ipmsg.thread;

import ipmsg.domain.DataPacket;
import ipmsg.domain.IpMsgConstant;
import ipmsg.domain.SocketManage;
import ipmsg.domain.SystemVar;
import ipmsg.domain.UsersVo;
import ipmsg.util.NetUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MsgRecv implements Runnable
{

	private Context ctx;

	public MsgRecv(Context ctx)
	{
		this.ctx = ctx;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		try
		{
			DatagramSocket defaultSocket = SocketManage.getInstance().getUdpSocket();
			DatagramPacket pack = new DatagramPacket(new byte[IpMsgConstant.PACKET_LENGTH], IpMsgConstant.PACKET_LENGTH);
			while (true)
			{
				// 接收数据
				defaultSocket.receive(pack);
				byte[] buffer = new byte[pack.getLength()];
				System.arraycopy(pack.getData(), 0, buffer, 0, buffer.length);
				DataPacket dataPacket = DataPacket.createDataPacket(buffer, pack.getAddress().getHostAddress());
				Log.d("comemsg", dataPacket.toString());
				if (dataPacket != null)
				{
					dateAnalyse(dataPacket);
				}
			}
		}
		catch (SocketException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void dateAnalyse(DataPacket dataPacket)
	{
		String tag = "com.smilesword.AndroidIpmsg.MsgRecvBc";
		Intent intent = new Intent(tag);
		switch (dataPacket.getCommandFunction())
		{ // 命令功能判定
			case IpMsgConstant.IPMSG_ANSENTRY:
				// 登录后应答信息
				// 添加用户信息
				if (SystemVar.addUsers(UsersVo.changeDataPacket(dataPacket)))
				{
					// 添加成功设置在线用户数
					// Toast.makeText(this, dataPacket.getIp() + "谁在线",
					// Toast.LENGTH_SHORT).show();

					intent.putExtra("type", "online");
					intent.putExtra("UserName", dataPacket.getSenderName());
					intent.putExtra("IP", dataPacket.getIp());
					ctx.sendBroadcast(intent);
					Log.d("online", dataPacket.getIp() + "谁在线");
				}
				break;
			case IpMsgConstant.IPMSG_SENDMSG:
				if ((IpMsgConstant.IPMSG_SENDCHECKOPT & dataPacket.getOption()) != 0)
				{
					// 需要发送检查
					DataPacket tmpPacket = new DataPacket(IpMsgConstant.IPMSG_RECVMSG);
					tmpPacket.setAdditional(Integer.toString(dataPacket.getPacketNo()));
					tmpPacket.setIp(dataPacket.getIp());
					NetUtil.sendUdpPacket(tmpPacket, tmpPacket.getIp());
				}
				if (dataPacket.getFileName()!=null)
				{
					String tagf = "com.smilesword.AndroidIpmsg.FileTransferBc";
					Intent intentf = new Intent(tagf);
					String addPacketNo = Integer.toHexString(dataPacket.getPacketNo());
					intentf.putExtra("packetNo", addPacketNo);
					ctx.sendBroadcast(intentf);
				}
				SystemVar.addUsers(UsersVo.changeDataPacket(dataPacket));
				// Toast.makeText(this, dataPacket.getIp() + "说：" +
				// dataPacket.getAdditional(), Toast.LENGTH_SHORT).show();
				intent.putExtra("type", "message");
				intent.putExtra("msg", dataPacket.getAdditional());
				intent.putExtra("UserName", dataPacket.getSenderName());
				intent.putExtra("IP", dataPacket.getIp());
				ctx.sendBroadcast(intent);
				Log.d("message", dataPacket.getIp() + "说：" + dataPacket.getAdditional());
				break;
			case IpMsgConstant.IPMSG_BR_ENTRY:
				// 其他用户登录
				SystemVar.addUsers(UsersVo.changeDataPacket(dataPacket));
				// 添加成功设置在线用户数
				DataPacket dp = new DataPacket(IpMsgConstant.IPMSG_ANSENTRY);
				dp.setAdditional(SystemVar.USER_NAME + '\0' + "");
				dp.setIp(NetUtil.getLocalHostIp());
				UsersVo user = UsersVo.changeDataPacket(dp);
				SystemVar.addUsers(user); // 此句添加自己
				NetUtil.sendUdpPacket(dp, dataPacket.getIp());
				// Toast.makeText(this, dataPacket.getIp() + "来了",
				// Toast.LENGTH_SHORT).show();
				intent.putExtra("type", "online");
				intent.putExtra("UserName", dataPacket.getSenderName());
				intent.putExtra("IP", dataPacket.getIp());
				ctx.sendBroadcast(intent);
				Log.d("come on", dataPacket.getIp() + "来了");
				break;

		}

	}

}
