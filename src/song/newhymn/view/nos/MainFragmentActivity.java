package song.newhymn.view.nos;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.admixer.CustomPopup;
import com.admixer.CustomPopupListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;
import song.newhymn.view.nos.fragment.FragmentActivity2;
public class MainFragmentActivity extends SherlockFragmentActivity implements CustomPopupListener{
	private ActionBar actionbar;
	private ViewPager viewpager;
	private Tab tab;
	public static Context context;
	private Handler handler = new Handler();
	private boolean flag;
	private NativeExpressAdView admobNative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.fragment_main);
		context = this;
		
		actionbar = getSupportActionBar();
//		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayShowTitleEnabled(true);
		viewpager = (ViewPager)findViewById(R.id.pager);
		
		FragmentManager fm = getSupportFragmentManager();
		ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				actionbar.setSelectedNavigationItem(position);
			}
		};
		viewpager.setOnPageChangeListener(ViewPagerListener);
		viewpager.setOnPageChangeListener(ViewPagerListener);
		TabContentAdapter adapter = new TabContentAdapter(fm);
		viewpager.setAdapter(adapter);
		viewpager.setCurrentItem(1);
		
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				viewpager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
			}
		};
		
		tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_1)).setTabListener(tabListener);
		actionbar.addTab(tab);

		tab = actionbar.newTab().setText(context.getString(R.string.tab_menu_2)).setTabListener(tabListener);
		actionbar.addTab(tab);
		CustomPopup.setCustomPopupListener(this);
        CustomPopup.startCustomPopup(this, "47bjv5uh");
		init_admob_naive();
		exit_handler();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		admobNative.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		admobNative.resume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		CustomPopup.stopCustomPopup();
		admobNative.destroy();
	}
	
	public class TabContentAdapter extends FragmentPagerAdapter {
		private int PAGE_COUNT = 1;

		public TabContentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				FragmentActivity2 fa1 = new FragmentActivity2();
				return fa1;
			case 1:
				FragmentActivity2 fa2 = new FragmentActivity2();
				return fa2;
				
			}
			return null;
		}

		@Override
		public int getCount() {
			return PAGE_COUNT;
		}
	}
	
	private void init_admob_naive(){
		RelativeLayout nativeContainer = (RelativeLayout) findViewById(R.id.admob_native);
		AdRequest adRequest = new AdRequest.Builder().build();	    
		admobNative = new NativeExpressAdView(this);
		admobNative.setAdSize(new AdSize(360, 100));
		admobNative.setAdUnitId("ca-app-pub-4637651494513698/3398718966");
		nativeContainer.addView(admobNative);
		admobNative.loadAd(adRequest);
	}
	
	private void exit_handler(){
    	handler = new Handler(){
    		@Override
    		public void handleMessage(Message msg) {
    			if(msg.what == 0){
    				flag = false;
    			}
    		}
    	};
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(keyCode == KeyEvent.KEYCODE_BACK){
			 if(!flag){
				 Toast.makeText(context, context.getString(R.string.txt_back) , Toast.LENGTH_SHORT).show();
				 flag = true;
				 handler.sendEmptyMessageDelayed(0, 2000);
			 return false;
			 }else{
				 try{
					 handler.postDelayed(new Runnable() {
						 @Override
						 public void run() {
							 finish();
						 }
					 },0);
				 }catch(Exception e){
				 }
			 }
            return false;	 
		 }
		return super.onKeyDown(keyCode, event);
	}
	
	//** CustomPopup �̺�Ʈ�� *************
	@Override
	public void onCloseCustomPopup(String arg0) {
	
	}

	@Override
	public void onHasNoCustomPopup() {
	
	}

	@Override
	public void onShowCustomPopup(String arg0) {
	
	}

	@Override
	public void onStartedCustomPopup() {
	
	}

	@Override
	public void onWillCloseCustomPopup(String arg0) {
	
	}

	@Override
	public void onWillShowCustomPopup(String arg0) {
	
	}

}