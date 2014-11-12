package cz.kinst.jakub.diploma.broadcastchat;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by jakubkinst on 12/11/14.
 */
public class Broadcast {
	public static void send(Context context, String messageStr) {
		// Hack Prevent crash (sending should be done using an async task)
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			//Open a random port to send the package
			DatagramSocket socket = new DatagramSocket();
			socket.setBroadcast(true);
			byte[] sendData = messageStr.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getBroadcastAddress(context), Config.PORT);
			socket.send(sendPacket);
			Log.i(Config.TAG, "Broadcast packet sent to: " + getBroadcastAddress(context).getHostAddress());
		} catch (IOException e) {
			Log.e(Config.TAG, "IOException: " + e.getMessage());
		}
	}

	static InetAddress getBroadcastAddress(Context context) throws IOException {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	/*
	This has to be run in a background thread
	 */
	public static void startReceive() {
		try {
			//Keep a socket open to listen to all the UDP trafic that is destined for this port
			DatagramSocket socket = new DatagramSocket(Config.PORT, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);

			while (true) {
				Log.i(Config.TAG, "Ready to receive broadcast packets!");

				//Receive a packet
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);

				//Packet received
				Log.i(Config.TAG, "Packet received from: " + packet.getAddress().getHostAddress());
				String data = new String(packet.getData()).trim();
				Log.i(Config.TAG, "Packet received; data: " + data);
				BusProvider.get().post(new PacketEvent(packet));
			}
		} catch (IOException ex) {
			Log.i(Config.TAG, "Oops" + ex.getMessage());
		}
	}
}
