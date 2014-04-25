package com.example.myfirstapp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class MainActivity extends Activity {
	private EditText numberText;
	private EditText contentText;
	private TextView textview;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		
		ButtonClickListener bc=new ButtonClickListener();
		ButtonClickListener bc2=new ButtonClickListener();
		textview=(TextView) this.findViewById(R.id.display);
		numberText=(EditText) this.findViewById(R.id.To);
		contentText=(EditText) this.findViewById(R.id.edit_message);
		Button button=(Button) this.findViewById(R.id.button_send);
		button.setOnClickListener(bc);
		Button showExression=(Button) this.findViewById(R.id.edit);
		showExression.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				int randomID=new Random().nextInt(9)+1;
				try{
					Field field=R.drawable.class.getDeclaredField("face"+randomID);
					int resourceId=Integer.parseInt(field.get(null).toString());
					Bitmap bitmap=BitmapFactory.decodeResource(getResources(), resourceId);
					ImageSpan is=new ImageSpan(MainActivity.this,bitmap);
					SpannableString ss=new SpannableString("face");
					ss.setSpan(is,0,4,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
					textview.append(ss);
				}catch (Exception e){
					e.printStackTrace();
				}	
			}
		});
	}

	public void sendMessage(View view){
	   	String message=contentText.getText().toString();
		if(textview!=null){
			textview.append("address£º ");
			textview.append(message.trim());
			textview.append("\n");
		} else {
			System.out.println("textview is null!");
		}
	}
	
	private final class ButtonClickListener implements View.OnClickListener{

		public void onClick(View v){
			String number=numberText.getText().toString();
			String content=contentText.getText().toString();
			SmsManager manager=SmsManager.getDefault();
			ArrayList<String> texts=manager.divideMessage(content);
			for(String text:texts){
				manager.sendTextMessage(number,null,text,null,null);
			}
			sendMessage(v);
			Toast.makeText(MainActivity.this,R.string.success,Toast.LENGTH_LONG).show();;
		}

	}
}