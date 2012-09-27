package ipmsg.thread;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class FileTransfer implements Runnable
{
	private static final String HOST = "10.14.4.67";
	private static final int PORT = 2425;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String cs = null;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private DataOutputStream fileOut;

	public FileTransfer(String cs)
	{
		this.cs = cs;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		getFileTest(cs);
	}

	public void getFileTest(String str)
	{

		Socket socket;
		byte[] buf = new byte[65535];
		int read = -1, readonce = 8092;
		long rest = 53760;
		Log.d("file", str);
		try
		{
			socket = new Socket(HOST, PORT);
			Log.d("file", str);
			DataOutputStream fileOut;
			this.outputStream = new DataOutputStream(socket.getOutputStream());
			this.inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			outputStream.write(str.getBytes("gbk"));
			outputStream.flush();
			fileOut = new DataOutputStream(new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream("/sdcard/test.doc"))));
			while (true)
			{
				if (inputStream != null)
				{
					if (readonce > rest)
						readonce = (int) rest;
					read = inputStream.read(buf, 0, readonce);
				}
				if (read == -1)
				{
					break;
				}
				rest -= read;
				fileOut.write(buf, 0, read);
				if (rest <= 0)
				{
					break;
				}
			}
			fileOut.flush();
			fileOut.close();
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
