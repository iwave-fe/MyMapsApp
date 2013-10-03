package com.iwavefe.mymapsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
//import android.widget.Button;
//import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

	protected static final String LOGTAG = "MyMap";

	/**
	 * Google Map
	 */
	GoogleMap mGoogleMap;

	/**
	 */
	private OnMarkerClickListener mOnMarkerClickListener = new OnMarkerClickListener() {
		@Override
		public boolean onMarkerClick(Marker marker) {
			marker.showInfoWindow(); 
			return false;
		}
	};

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;
	
	private String[] mTitanTitles;
	private ListView mDrawerList;
	
	private int gMapType;
	
	/**
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gMapType = GoogleMap.MAP_TYPE_NORMAL;

		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		mTitanTitles = getResources().getStringArray(R.array.titans_array);
		mDrawerList = (ListView) findViewById(R.id.drawer_list);
	
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
					R.layout.drawer_list_item, mTitanTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
				R.drawable.ic_drawer, R.string.drawer_open, 
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				Log.i(LOGTAG, "onDrawerClosed");
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				Log.i(LOGTAG, "onDrawerOpened");
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				Log.i(LOGTAG, "onDrawerSlide : " + slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				Log.i(LOGTAG, "onDrawerStateChanged new state : " + newState);
			}
		};

		mDrawer.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);

	}

	/**
	 * Navigation Drawer Click
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){

			String item = (String) parent.getItemAtPosition(position);
			int itemId = (int) parent.getItemIdAtPosition(position);
			String itemIds = String.valueOf(itemId);
			
			Toast.makeText(MainActivity.this, item + ":" + itemIds, Toast.LENGTH_LONG).show();
					
			selectItem(position, item, itemId);
		}
	}
	
	/**
	 * Navigation Drawer ListView Item action
	 */
	private void selectItem(int position, String item, int itemId){

		if (itemId == 0){

			BitmapDescriptor bitmapDroid = 
					BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
			
			MarkerOptions markerOptionsMakiminato = new MarkerOptions();
			markerOptionsMakiminato.title("droid"); 
			markerOptionsMakiminato.snippet("marking"); 
			LatLng latlngMakiminato = new LatLng(26.263588,127.72308);
			markerOptionsMakiminato.position(latlngMakiminato); 
			markerOptionsMakiminato.icon(bitmapDroid); 
			markerOptionsMakiminato.describeContents();
			
			mGoogleMap.addMarker(markerOptionsMakiminato);

			Toast.makeText(MainActivity.this, "droid Marker", Toast.LENGTH_LONG).show();

		} else {

			BitmapDescriptor bitmapTitan = 
				BitmapDescriptorFactory.fromResource(R.drawable.syon_super_titan);
		
			MarkerOptions markerOptionsMakiminato = new MarkerOptions();
			markerOptionsMakiminato.title("巨人"); 
			markerOptionsMakiminato.snippet("５０ｍ級出現！"); 
			LatLng latlngMakiminato = new LatLng(26.261654,127.722812);
			markerOptionsMakiminato.position(latlngMakiminato); 
			markerOptionsMakiminato.icon(bitmapTitan); 
			markerOptionsMakiminato.describeContents();
		
			mGoogleMap.addMarker(markerOptionsMakiminato);

			Toast.makeText(MainActivity.this, "Titan Marker", Toast.LENGTH_LONG).show();
		}
		
		mDrawer.closeDrawers();
	}

	/**
	 * Navigation Drawer action
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void onClick(View v) {
		mDrawer.closeDrawers();
	}


	/**
	 * Map View initial
	 */
	@Override
	protected void onResume() {
		super.onResume();

  		// Google
  		if (mGoogleMap == null) {
  			SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
  
  			mGoogleMap = (fragment != null) ? fragment.getMap() : null;
  
  			if (mGoogleMap != null) {
//  				mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);  
  				mGoogleMap.setMapType(gMapType);  
  				mGoogleMap.setOnMarkerClickListener(mOnMarkerClickListener); 
  
  				//  26.262102,127.723869
  				final LatLng makiminato = new LatLng(26.262102,127.723869);
  				mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(makiminato));
  				mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
  				// 
  				drawFields();
  			}
  		}
	}

	/**
	 * Map View initial
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Menu action
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			// license 
			case R.id.action_license:

			final String message = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this);

			new AlertDialog.Builder(this).setTitle(R.string.action_license).setMessage(message)
					.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
			
				break;

			// default map
			case R.id.default_map:
				gMapType = GoogleMap.MAP_TYPE_NORMAL;
				mGoogleMap.setMapType(gMapType);  
				
				break;
			
			// hybrid map
			case R.id.hybrid_map:
				gMapType = GoogleMap.MAP_TYPE_HYBRID;
				mGoogleMap.setMapType(gMapType);  
				
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * field setting
	 */
	private void drawFields() {

		{
			Field field = new Field("ウォール・シーナ", new LatLng[]{
					new LatLng(26.262116,127.723638),
					new LatLng(26.262501,127.724003),
					new LatLng(26.262126,127.7244),
					new LatLng(26.261866,127.723863),
			});
			field.setMemo("憲兵団が暗躍中・・・");
			field.setColorRgb(255, 0, 0);
			drawField(field);
		}

		{
			Field field = new Field("ウォール・ローゼ", new LatLng[]{
					new LatLng(26.261654,127.722812),
					new LatLng(26.263112,127.723526),
					new LatLng(26.26266,127.725087),
					new LatLng(26.261433,127.724507),
			});
			field.setMemo("駐屯兵団が警戒中・・・");
			field.setColorRgb(0, 255, 0);
			drawField(field);
		}
		
		{
			Field field = new Field("ウォール・マリア", new LatLng[]{
					new LatLng(26.261409,127.722077),
					new LatLng(26.263588,127.72308),
					new LatLng(26.262823,127.725875),
					new LatLng(26.260692,127.725693),
			});
			field.setMemo("調査兵団が探索中・・・");
			field.setColorRgb(0, 0, 255);
			drawField(field);
		}

	}
	
	/**
	 * field initial
	 */
	private void drawField(Field field) {
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.title(field.getName()); 
		markerOptions.snippet(field.getMemo()); 
		markerOptions.position(calcCenter(field.getVertexes())); 
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(field.getColorHue())); 
		
		mGoogleMap.addMarker(markerOptions); 
		
		final LatLng[] vertexes = field.getVertexes();
		if(vertexes != null && vertexes.length > 3) {
	        PolygonOptions polygonOptions = new PolygonOptions();

	        final int[] colorRgb = field.getColorRgb();
	        int colorRed = colorRgb[0];
	        int colorGreen = colorRgb[1];
	        int colorBlue = colorRgb[2];

	        polygonOptions.strokeColor(Color.argb(0x255, colorRed, colorGreen, colorBlue));
	        polygonOptions.strokeWidth(5);

	        polygonOptions.fillColor(Color.argb(0x40, colorRed, colorGreen, colorBlue));

	        polygonOptions.add(vertexes);

	        mGoogleMap.addPolygon(polygonOptions);
		}
	}

	/**
	 * polygon center mark -->> polygon end point mark
	 */
	private LatLng calcCenter(LatLng[] vertexes) {
		if(vertexes.length == 1) {
			return vertexes[0];
		} else if (vertexes.length > 2) {
//			double latSum = 0;
//			double lngSum = 0;
			
			double latLast = 0;
			double lngLast = 0;
			
			for(LatLng latLng: vertexes) {
//				latSum += latLng.latitude;
//				lngSum += latLng.longitude;
				latLast = latLng.latitude;
				lngLast = latLng.longitude;
			}
			
	//		return new LatLng(latSum/vertexes.length, lngSum/vertexes.length);
			return new LatLng(latLast, lngLast);
		}
		
		throw new IllegalArgumentException();
	}

}
