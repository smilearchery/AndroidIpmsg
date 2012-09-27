package com.smilesword.AndroidIpmsg;

import ipmsg.domain.SystemVar;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class AndroidIpmsgActivity extends Activity
{
	private ListView lv_users;
	private List<Map<String, Object>> items;
	private ListAdapter adapter;
	private BroadcastReceiver receiver;
	private Intent ibc;
	private OnItemClickListener l;
	private String UserIP;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		lv_users = (ListView) findViewById(R.id.lv_users);
		String[] columns = new String[] { "UserName", "UserIP" };
		int[] names = new int[] { R.id.tv_userName, R.id.tv_userIp };
		SystemVar sVar = new SystemVar();
		l = new ListViewOnItemClickListener();

		receiver = new MsgRecvBcReceiver();
		registerReceiver(receiver, new IntentFilter("com.smilesword.AndroidIpmsg.MsgRecvBc"));

		Log.v("encode", SystemVar.DEFAULT_CHARACT);
		ibc = new Intent();
		ibc.setClass(AndroidIpmsgActivity.this, BackgroundService.class);
		startService(ibc);

		adapter = new SimpleAdapter(this, SystemVar.getUserList(), R.layout.user_list, columns, names);
		lv_users.setAdapter(adapter);
		lv_users.setOnItemClickListener(l);
	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		stopService(ibc);
		unregisterReceiver(receiver);
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 0, 0, "刷新");
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	public class MsgRecvBcReceiver extends BroadcastReceiver
	{
		private Context ctx;

		@Override
		public void onReceive(Context context, Intent intent)
		{

			// TODO Auto-generated method stub
			this.ctx = context;
			String type = intent.getStringExtra("type");
			String IP = intent.getStringExtra("IP");
			if (type.equals("message"))
			{
				// 顶栏通知
				String message = intent.getStringExtra("msg");
				playVibrator();
				sendNotification(IP, message);
			}
			SimpleAdapter sAdapter = (SimpleAdapter) lv_users.getAdapter();
			sAdapter.notifyDataSetChanged();
		}

		public void playVibrator()
		{
			Vibrator vibrator;
			vibrator = (Vibrator) getSystemService(ctx.VIBRATOR_SERVICE);
			long[] pattern = { 800, 100, 400, 100 };
			vibrator.vibrate(pattern, -1);
		}

		public void sendNotification(String IP, String message)
		{
			NotificationManager nm = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
			Notification n = new Notification(R.drawable.ic_launcher, "新飞秋消息", System.currentTimeMillis());
			n.flags = Notification.FLAG_AUTO_CANCEL;
			Intent i = new Intent();
			i.putExtra("UserIPN", IP);
			i.putExtra("msg", message);
			i.setClass(AndroidIpmsgActivity.this, MsgWindowActivity.class);
			PendingIntent pi = PendingIntent.getActivity(ctx, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
			n.setLatestEventInfo(ctx, IP, message, pi);
			nm.notify(1, n);
		}

	}

	private class ListViewOnItemClickListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Intent msgIntent = new Intent();
			//msgIntent.addFlags(Intent.)
			UserIP = SystemVar.getUserList().get(position).getIp();
			msgIntent.setClass(AndroidIpmsgActivity.this, MsgWindowActivity.class);
			msgIntent.putExtra("UserIP", UserIP);
			startActivity(msgIntent);
			Toast.makeText(AndroidIpmsgActivity.this, UserIP, Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * public List<Map<String, Object>> getList() { items = new
	 * ArrayList<Map<String, Object>>(); Map<String, Object> map;
	 * 
	 * for (int i = 15; i > 0; i--) { map = new HashMap<String, Object>();
	 * map.put("userName", "HelloWorld"); map.put("userIp", "10.14.4.23");
	 * items.add(map); }
	 * 
	 * return items; }
	 */

}
