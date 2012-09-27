package com.smilesword.AndroidIpmsg;

import ipmsg.domain.DataPacket;
import ipmsg.domain.IpMsgConstant;
import ipmsg.util.NetUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MsgWindowActivity extends Activity
{
	private ListView talkView;
	private Button bt_sendMsg;
	private DetailEntity d;
	private EditText et_sendMsg;
	private List<DetailEntity> list = null;
	private String userIP = null;
	private BroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msgwindow);
		userIP = this.getIntent().getStringExtra("UserIP");
		talkView = (ListView) findViewById(R.id.list);
		et_sendMsg = (EditText) findViewById(R.id.et_sendMsg);
		bt_sendMsg = (Button) findViewById(R.id.bt_sendMsg);
		bt_sendMsg.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMsg(userIP, et_sendMsg.getText().toString());
			}
		});
		talkView.setDividerHeight(30);

		// talkView.setDivider(divider)
		list = new ArrayList<DetailEntity>();
		talkView.setAdapter(new DetailAdapter(MsgWindowActivity.this, list));
		initFromNoti();
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		receiver = new MyMsgRecvBcReceiver();
		registerReceiver(receiver, new IntentFilter("com.smilesword.AndroidIpmsg.MsgRecvBc"));
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onPause();

	}

	private void initFromNoti()
	{
		if (this.getIntent().getStringExtra("UserIPN") != null)
		{
			userIP = this.getIntent().getStringExtra("UserIPN");
			String msg = this.getIntent().getStringExtra("msg");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date datef = new Date();
			String strDate = dateFormat.format(datef);
			d = new DetailEntity(userIP, strDate, msg, R.layout.list_say_he_item);
			list.add(d);
			DetailAdapter sAdapter = (DetailAdapter) talkView.getAdapter();
			sAdapter.notifyDataSetChanged();
		}
	}

	private void sendMsg(String userIP, String msg)
	{
		String ips[] = new String[] { userIP };
		String Text = msg;
		for (int i = 0; i < ips.length; i++)
		{
			DataPacket data = new DataPacket(IpMsgConstant.IPMSG_SENDMSG);
			data.setIp(ips[i]);
			data.setAdditional(Text);
			NetUtil.sendUdpPacket(data, data.getIp());
		}
		et_sendMsg.setText("");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date datef = new Date();
		String strDate = dateFormat.format(datef);
		d = new DetailEntity(userIP, strDate, msg, R.layout.list_say_me_item);
		list.add(d);
		DetailAdapter sAdapter = (DetailAdapter) talkView.getAdapter();
		sAdapter.notifyDataSetChanged();
	}

	public class MyMsgRecvBcReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			// TODO Auto-generated method stub
			NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
			String type = intent.getStringExtra("type");
			String ip = intent.getStringExtra("IP");
			if (type.equals("message") && ip.equals(userIP))
			{
				nm.cancel(1);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date datef = new Date();
				String strDate = dateFormat.format(datef);
				d = new DetailEntity(userIP, strDate, intent.getStringExtra("msg"), R.layout.list_say_he_item);
				list.add(d);
			}
			DetailAdapter sAdapter = (DetailAdapter) talkView.getAdapter();
			sAdapter.notifyDataSetChanged();
		}

	}

}
