package cz.kinst.jakub.diploma.broadcastchat;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {


	@InjectView(R.id.messageText)
	EditText mMessageText;
	@InjectView(R.id.sendButton)
	Button mSendButton;

	private PacketReceiverTask mListener;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
	}

	public void onEventMainThread(PacketEvent event) {
		Toast.makeText(this, "Received packet: " + event, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mListener = new PacketReceiverTask();
		mListener.execute();
		BusProvider.get().register(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mListener.cancel(true);
		BusProvider.get().unregister(this);
	}

	@OnClick(R.id.sendButton)
	void onSendClicked() {
		Broadcast.send(this, mMessageText.getText().toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
