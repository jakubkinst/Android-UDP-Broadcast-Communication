package cz.kinst.jakub.diploma.broadcastchat;

import java.net.DatagramPacket;

/**
 * Created by jakubkinst on 12/11/14.
 */
public class PacketEvent {
	private final DatagramPacket mPacket;

	public PacketEvent(DatagramPacket packet) {
		mPacket = packet;
	}

	public DatagramPacket getPacket() {
		return mPacket;
	}

	@Override
	public String toString() {
		return new String(mPacket.getData()).trim();
	}
}
