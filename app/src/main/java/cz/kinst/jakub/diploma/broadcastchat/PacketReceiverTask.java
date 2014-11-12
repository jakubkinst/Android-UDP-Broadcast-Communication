package cz.kinst.jakub.diploma.broadcastchat;

import android.os.AsyncTask;

/**
 * Created by jakubkinst on 12/11/14.
 */
public class PacketReceiverTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		Broadcast.startReceive();
		return null;
	}
}
