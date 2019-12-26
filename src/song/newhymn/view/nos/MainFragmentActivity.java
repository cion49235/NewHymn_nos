package song.newhymn.view.nos;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;
import song.newhymn.view.nos.dao.Const;
import song.newhymn.view.nos.fragment.FragmentActivity2;
import song.newhymn.view.nos.util.PreferenceUtil;
import song.newhymn.view.nos.widget.DialogMainPopup;
public class MainFragmentActivity extends SherlockFragmentActivity implements  AdViewListener{
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
		alert_view = true;
		today_date();
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "47bjv5uh");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/9445252567");
    	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1921985766");
		
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
//		init_admob_naive();
		if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_ISSUBSCRIBED, Const.isSubscribed).equals("true")){
			addBannerView();	
		}
        if(PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_RECOMMEND_STATUS, "N").equals("Y")){
            main_popup();
        }
		exit_handler();
	}
	
	
	private void today_date(){
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String date_today =  date.format(today);
//        Log.i("dsu","저장된날짜 ======================>: "+ PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_DATE_TODAY, date_today));
//        Log.i("dsu","오늘날짜 ======================>: "+ date_today);
        if(!PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_DATE_TODAY, date_today).equals(date_today)){
            PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_RECOMMEND_POPUP, false);
        }
        PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_DATE_TODAY, date_today);
//        Log.i("dsu","그리고 오늘날짜저장 ======================>: "+ date_today);
    }
	
	
	private void main_popup(){
        if(PreferenceUtil.getBooleanSharedData(context, PreferenceUtil.PREF_RECOMMEND_POPUP, false) == false){
            dialog_recommend_popup();
        }
    }
	
	
	private boolean alert_view = false;
	 private void dialog_recommend_popup(){
	        DialogMainPopup dialogClose =  new DialogMainPopup(context, MainFragmentActivity.this);
	        dialogClose.setCanceledOnTouchOutside(false);
	        dialogClose.setCancelable(true);
	        if(alert_view) dialogClose.show();
	    }
	
	
	public static RelativeLayout ad_layout;
	public void addBannerView() {
    	AdInfo adInfo = new AdInfo("47bjv5uh");
    	adInfo.setTestMode(false);
        AdView adView = new AdView(this);
        adView.setAdInfo(adInfo, this);
        adView.setAdViewListener(this);
        ad_layout = (RelativeLayout)findViewById(R.id.ad_layout);
        if(ad_layout != null){
        	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ad_layout.addView(adView, params);	
        }
    }
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		alert_view = false;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, false);
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
							 PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, true);
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
	
	@Override
	public void onClickedAd(String arg0, AdView arg1) {
		
	}

	@Override
	public void onFailedToReceiveAd(int arg0, String arg1, AdView arg2) {
		
	}

	@Override
	public void onReceivedAd(String arg0, AdView arg1) {
		
	}

}
