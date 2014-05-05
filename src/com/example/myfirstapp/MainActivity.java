package com.example.myfirstapp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private static final String TASK_FRAGMENT = "task_fragment";
	private EditText numberText;
	private EditText contentText;
	private TextView textView;
	private TcpSender tcpSender;
	private Thread senderThread;
	private TcpReceiver tcpReceiver;
	private Thread receiverThread;
	private Queue<String> uiMessageQueue;
	private Handler mHandler;
	private Fragment mFragment;
	private View fragmentFace;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FragmentManager fm = getSupportFragmentManager();
		mFragment = fm.findFragmentByTag(TASK_FRAGMENT);

		if (mFragment == null) {
			mFragment = new PlaceholderFragment();
			fm.beginTransaction().add(mFragment, TASK_FRAGMENT).commit();
		}
		
		setContentView(R.layout.fragment_main);
		
		ButtonClickListener bc=new ButtonClickListener();
		ButtonClickListener bc2=new ButtonClickListener();
		textView=(TextView) this.findViewById(R.id.display);
		numberText=(EditText) this.findViewById(R.id.To);
		contentText=(EditText) this.findViewById(R.id.edit_message);
		Button button=(Button) this.findViewById(R.id.button_send);
		button.setOnClickListener(bc);
		Button showExression=(Button) this.findViewById(R.id.edit);
		
		final ViewGroup linear = (ViewGroup) this.findViewById(R.id.linear);
		fragmentFace = this.getLayoutInflater().inflate(R.layout.fragment_face, linear, false);
		// Set fragmentFace's visibility to GONE so it won't show when added
		fragmentFace.setVisibility(View.GONE);
		// Add fragmentFace to fragment_main
		linear.addView(fragmentFace);
		// Added click listener for button 'image'
		showExression.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				if(fragmentFace.getVisibility() == View.GONE) {
					// Toggle the view to visible if it is not
					fragmentFace.setVisibility(View.VISIBLE);
				} else {
					// Toggle the view to hidden if it is visible
					fragmentFace.setVisibility(View.GONE);
				}
			}
		});
		// Bind Handler with main Looper
		mHandler = new Handler(Looper.getMainLooper()) {
			// Tell main looper to poll uiMessageQueue
			@Override
			public void handleMessage(Message signalMessage) {
				super.handleMessage(signalMessage);
				String message = uiMessageQueue.poll();
				Activity mainActivity = mFragment.getActivity();
				if(mainActivity != null) {
					textView=(TextView) mainActivity.findViewById(R.id.display);
					System.out.println("::handler: textView = " + textView);
				} else {
					System.out.println("::mainActivity is null");
				}
				String myIP = "127.0.0.1:";
				if(message != null) {
					if(textView != null) {
						if(message.startsWith(myIP)) {
							message = "Me (127.0.0.1):"+ 
									message.substring(myIP.length());
						}
						textView.append(message);
					} else {
						System.out.println("::textView is null");						
					}
				} else {
					System.out.println("::No new message");
				}
			}
		};
		// Create a queue to store incoming message
		uiMessageQueue = new ArrayBlockingQueue<String>(50);
		// Launch TCP Sender
		tcpSender = new TcpSender();
		senderThread = new Thread(tcpSender);
		senderThread.start();
		// Launch TCP Receiver
		tcpReceiver = new TcpReceiver(tcpSender, uiMessageQueue, mHandler);
		receiverThread = new Thread(tcpReceiver);
		receiverThread.start();
		// Added self IP for debugging purpose
		new Thread(new Runnable() {

			@Override
			public void run() {
				InetAddress ip = null;
				try {
					ip = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				tcpSender.addReceiver(ip);
			}
			
		}).start();
	}
	
	private final class ButtonClickListener implements View.OnClickListener{
		
		public void onClick(View v){
			String number=numberText.getText().toString();
			String content=contentText.getText().toString();
			tcpSender.send(content);
			contentText.setText("");
			Toast.makeText(MainActivity.this,R.string.success,Toast.LENGTH_LONG).show();;
		}
		
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		Activity mainActivity;
		
		public PlaceholderFragment() {
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
			System.out.println("::Fragment created");
		}
		
		@Override
		public void onAttach(Activity activity) {
		    super.onAttach(activity);
		    mainActivity = activity;
			System.out.println("::Fragment attached");
		    
		}
		
		@Override
		  public void onDetach() {
		    super.onDetach();
		    mainActivity = null;
			System.out.println("::Fragment detached");
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
		
		public Activity getMainActivity() {
			return this.mainActivity;
		}
	}
		
	

}
