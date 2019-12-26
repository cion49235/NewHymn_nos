package song.newhymn.view.nos.activity;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import song.newhymn.view.nos.MainFragmentActivity;
import song.newhymn.view.nos.R;
import song.newhymn.view.nos.util.PreferenceUtil;
import song.newhymn.view.nos.widget.DialogServicePopup;



public class IntroActivity extends Activity{
	public Handler handler;
	public Context context;
	public boolean retry_alert = false;
	public static Activity activity;
	public static LinearLayout bg_intro;
    public static int background_type = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);
        context = this;
        activity = this;
        retry_alert = true;
        alert_view = true;
//        billing_process();//인앱정기결제체크
        
        
        adstatus_async = new Adstatus_Async();
        adstatus_async.execute();
    }
    
    
    private Adstatus_Async adstatus_async = null;
    public class Adstatus_Async extends AsyncTask<String, Integer, String> {
    	String version;
        String service_status;
        String recommend_status;
        String tv_service;
        String tv_recommend;
        String pk_recommend_name;
        HttpURLConnection localHttpURLConnection;
        public Adstatus_Async(){
        }
        @Override
        protected String doInBackground(String... params) {
            String sTag;
            try{
                String str = "http://cion49235.cafe24.com/cion49235/newhymn_nos/ad_status2.php";
                localHttpURLConnection = (HttpURLConnection)new URL(str).openConnection();
                localHttpURLConnection.setFollowRedirects(true);
                localHttpURLConnection.setConnectTimeout(15000);
                localHttpURLConnection.setReadTimeout(15000);
                localHttpURLConnection.setRequestMethod("GET");
                localHttpURLConnection.connect();
                InputStream inputStream = new URL(str).openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inputStream, "EUC-KR");
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                    }else if (eventType == XmlPullParser.END_DOCUMENT) {
                    }else if (eventType == XmlPullParser.START_TAG){
                        sTag = xpp.getName();
                        if(sTag.equals("version")){
                            version = xpp.nextText()+"";
                            PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_VERSION, version);
                            Log.i("dsu", "version : " + version);
                        }else if(sTag.equals("service_status")){
                            service_status = xpp.nextText()+"";
                            PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_SERVICE_STATUS, service_status);
                            Log.i("dsu", "service_status : " + service_status);
                        }else if(sTag.equals("recommend_status")){
                            recommend_status = xpp.nextText()+"";
                            PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_RECOMMEND_STATUS, recommend_status);
                            Log.i("dsu", "recommend_status : " + recommend_status);
                        }else if(sTag.equals("tv_service")){
                            tv_service = xpp.nextText()+"";
                            PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_TV_SERVICE, tv_service);
                            Log.i("dsu", "tv_service : " + tv_service);
                        }else if(sTag.equals("tv_recommend")){
                            tv_recommend = xpp.nextText()+"";
                            PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_TV_RECOMMEND, tv_recommend);
                            Log.i("dsu", "tv_recommend : " + tv_recommend);
                        }else if(sTag.equals("pk_recommend_name")){
                            pk_recommend_name = xpp.nextText()+"";
                            PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_PK_RECOMMEND_NAME, pk_recommend_name);
                            Log.i("dsu", "pk_recommend_name : " + pk_recommend_name);
                        }
                    } else if (eventType == XmlPullParser.END_TAG){
                        sTag = xpp.getName();
                        if(sTag.equals("Finish")){
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                    }
                    eventType = xpp.next();
                }
            }
            catch (SocketTimeoutException localSocketTimeoutException)
            {
            }
            catch (ClientProtocolException localClientProtocolException)
            {
            }
            catch (IOException localIOException)
            {
            }
            catch (Resources.NotFoundException localNotFoundException)
            {
            }
            catch (java.lang.NullPointerException NullPointerException)
            {
            }
            catch (Exception e)
            {
            }
            return service_status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            version_check();
        }
        @Override
        protected void onPostExecute(String service_status) {
            super.onPostExecute(service_status);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
    
    int versionCode;
    @SuppressWarnings("deprecation")
	private void version_check(){
        PackageInfo pi=null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NullPointerException e){
        } catch (Exception e){
        }
        if ( (versionCode < Integer.parseInt(PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_VERSION, "1"))) && (versionCode > 0) ) {
           android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
//            builder.setIcon(R.drawable.icon128);
            builder.setTitle(context.getString(R.string.alert_update_01));
            builder.setMessage(context.getString(R.string.alert_update_02));
            builder.setCancelable(false);
            builder.setPositiveButton(Html.fromHtml("<font color='#ff6d00'>'"+context.getString(R.string.alert_update_03)+"'</font>"), new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int whichButton){
                    String packageName = "";
                    try {
                        @SuppressWarnings("unused")
						PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        packageName = getPackageName();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    } catch (PackageManager.NameNotFoundException e) {
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                    }
                }
            });
            builder.setNegativeButton(Html.fromHtml("<font color='#ff6d00'>'"+context.getString(R.string.alert_update_05)+"'</font>"), new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int whichButton){
                    finish();
                }
            });
            android.app.AlertDialog myAlertDialog = builder.create();
            myAlertDialog.show();
        }
        else {
            handler = new Handler();
            handler.postDelayed(runnable, 2000);
        }
    }
    
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	alert_view = false;
    	retry_alert = false;
    	if(handler != null){
    		handler.removeCallbacks(runnable);
    	}
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    }
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    }
    
    
    public String MillToDate(long mills) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String date = (String) formatter.format(new Timestamp(mills));
        return date;
    }
    
    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d("dsu", "Showing alert dialog: " + message);
        bld.create().show();
    }
    
    public void go_main(){
    	Intent intent = new Intent(context, MainFragmentActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		//fade_animation
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
    
    
    Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if(PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_SERVICE_STATUS, "Y").equals("Y")){
				Intent intent = new Intent(context, MainFragmentActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				//fade_animation
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);	
			}else {
				service_popup();
			}
			
		}
	};
	
	private boolean alert_view = false;
	private void service_popup(){
        DialogServicePopup dialog =  new DialogServicePopup(context, activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if(alert_view) dialog.show();
    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(handler != null) handler.removeCallbacks(runnable);
		finish();
	}
}
