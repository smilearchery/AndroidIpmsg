package com.smilesword.AndroidIpmsg;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.smilesword.AndroidIpmsg.MsgWindowActivity.MyMsgRecvBcReceiver;

import ipmsg.domain.DataPacket;
import ipmsg.domain.IpMsgConstant;
import ipmsg.domain.SystemVar;
import ipmsg.domain.UsersVo;
import ipmsg.thread.FileTransfer;
import ipmsg.thread.MsgRecv;
import ipmsg.util.NetUtil;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class BackgroundService extends Service
{
	private BroadcastReceiver receiver;

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		Thread msgRecvT = new Thread(new MsgRecv(this));
		msgRecvT.start();
		Login();
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		receiver = new FileTransferBcReceiver();
		registerReceiver(receiver, new IntentFilter("com.smilesword.AndroidIpmsg.FileTransferBc"));
	}

	private void Login()
	{
		DataPacket dp = new DataPacket(IpMsgConstant.IPMSG_BR_ENTRY);
		dp.setAdditional(SystemVar.USER_NAME + '\0' + "");
		dp.setIp(NetUtil.getLocalHostIp());
		UsersVo user = UsersVo.changeDataPacket(dp);
		SystemVar.addUsers(user);
		NetUtil.broadcastUdpPacket(dp);
	}

	public class FileTransferBcReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			// TODO Auto-generated method stub
			DataPacket data = new DataPacket(IpMsgConstant.IPMSG_GETFILEDATA);
			String packetNo = intent.getStringExtra("packetNo");
			data.setAdditional(packetNo+":0:0:");
			String add = data.toString();
			Thread fileRecvT = new Thread(new FileTransfer(add));
			fileRecvT.start();
		}

	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
