package com.deep.app.kids;

import java.util.Arrays;
import java.util.List;

import com.viniciusmo.androidtextspeech.Language;
import com.viniciusmo.androidtextspeech.Speaker;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends Activity implements OnItemClickListener , TextToSpeech.OnInitListener {
	
	private ActionBar actionBar = null;
	private ListView grid = null;
	private MenuDrawer mDrawer;
	Typeface font = null;
	
	private TextToSpeech mTts;
	private static final int MY_DATA_CHECK_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mDrawer = MenuDrawer.attach(this);
//        mDrawer.setContentView(R.layout.activity_main);
//        mDrawer.setMenuView(R.layout.activity_main);
        
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
        
        actionBar = getActionBar();
        actionBar.setTitle("ABCD");
        grid = (ListView)findViewById(R.id.grid);
        ListAdapter listAdapter = new SampleListAdapter(this, Arrays.asList(getResources().getStringArray(R.array.names)) , font);
        grid.setAdapter(listAdapter);
        grid.setOnItemClickListener(this);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MY_DATA_CHECK_CODE)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);
            }
            else
            {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
    
    private static class SampleListAdapter extends ArrayAdapter<String> {
    	
    	public Typeface fontHindi = null;

        public SampleListAdapter(Context context, List<String> objects , Typeface face) {
            super(context, R.layout.list_item, objects);
            fontHindi = face;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String name = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }

            LetterImageView letterImageView = (LetterImageView) convertView.findViewById(R.id.iv_avatar);
//            letterImageView.setOval(true);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_name);
            letterImageView.setLetter(name.charAt(0));
            textView.setText(name);
            if(null != fontHindi){
            	textView.setTypeface(fontHindi);
            }
            return convertView;
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.main, menu);
        return false;
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		String s = (String) parent.getAdapter().getItem(position);
		if(null != s){
			speak(s);
		}
	}
	
	private void speak(String s){
		s = s.charAt(0) + " for " + s;
		mTts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onInit(int status) {
		
	}
}
