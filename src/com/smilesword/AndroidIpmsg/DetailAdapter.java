package com.smilesword.AndroidIpmsg;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 适配器
 * 
 * @author Administrator
 * 
 */
public class DetailAdapter extends BaseAdapter
{

	private List<DetailEntity> coll;

	private Context ctx;

	// 消息
	DetailEntity entity;

	LinearLayout layout;

	// 加载布局
	LayoutInflater vi;

	// ------------------------------

	// 背景
	LinearLayout layout_bj;

	TextView tvName;
	TextView tvDate;
	TextView tvText;

	public DetailAdapter(Context context, List<DetailEntity> coll)
	{
		ctx = context;
		this.coll = coll;
	}

	public boolean areAllItemsEnabled()
	{
		return true;
	}

	/**
	 * 为true监听Item
	 */
	public boolean isEnabled(int arg0)
	{
		return true;
	}

	public int getCount()
	{
		return coll.size();
	}

	public Object getItem(int position)
	{
		return coll.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
		entity = coll.get(position);
		int itemLayout = entity.getLayoutID();

		layout = new LinearLayout(ctx);
		vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vi.inflate(itemLayout, layout, true);

		// layout.setBackgroundColor(0xffB4B4B4);

		// 图片背景
		layout_bj = (LinearLayout) layout.findViewById(R.id.layout_bj);

		tvName = (TextView) layout.findViewById(R.id.messagedetail_row_name);
		tvName.setText(entity.getName());

		tvDate = (TextView) layout.findViewById(R.id.messagedetail_row_date);
		tvDate.setText(entity.getDate());

		tvText = (TextView) layout.findViewById(R.id.messagedetail_row_text);
		tvText.setText(entity.getText());

		addListener(tvName, tvDate, tvText, layout_bj);

		return layout;
	}

	public boolean hasStableIds()
	{
		return true;
	}

	public boolean isEmpty()
	{
		return true;
	}

	/**
	 * 监听
	 * 
	 * @param convertView
	 */
	public void addListener(final TextView tvName, final TextView tvDate, final TextView tvText, LinearLayout layout_bj)
	{

		layout_bj.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

			}
		});

		layout_bj.setOnLongClickListener(new OnLongClickListener()
		{

			@Override
			public boolean onLongClick(View v)
			{
				tvName.setTextColor(0xffffffff);
				tvDate.setTextColor(0xffffffff);
				tvText.setTextColor(0xffffffff);
				new AlertDialog.Builder(ctx).setTitle("当前是长按操作").setMessage("准备写个菜单").create().show();

				return true;
			}
		});

		layout_bj.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_MOVE:
						tvName.setTextColor(0xffffffff);
						tvDate.setTextColor(0xffffffff);
						tvText.setTextColor(0xffffffff);
						break;
					default:
						tvName.setTextColor(0xff000000);
						tvDate.setTextColor(0xff000000);
						tvText.setTextColor(0xff0000ff);
						break;
				}
				return false;
			}
		});
	}



}
