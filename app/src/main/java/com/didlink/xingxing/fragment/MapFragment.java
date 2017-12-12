package com.didlink.xingxing.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.didlink.xingxing.R;
import com.didlink.xingxing.databinding.FragmentMapBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private Location currentLocation = null;
    private MapView mMapview = null;
    private Resources mRes = null;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        Context ctx = getActivity().getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        mRes = getResources();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentMapBinding fragmentMapBinding = FragmentMapBinding.inflate(inflater, container, false);
       //set variables in Binding
        mMapview = fragmentMapBinding.map;

        mMapview.setTileSource(TileSourceFactory.MAPNIK);

        //mMapview.setBuiltInZoomControls(true);
        mMapview.setMultiTouchControls(true);
        mMapview.setTilesScaledToDpi(true);

        CompassOverlay mCompassOverlay = new CompassOverlay(getActivity().getApplicationContext(), new InternalCompassOrientationProvider(getActivity().getApplicationContext()),
                mMapview);
        mCompassOverlay.enableCompass();
        mMapview.getOverlays().add(mCompassOverlay);

        //MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity().getApplicationContext()),mMapview);
        //mLocationOverlay.enableMyLocation();
        //mMapview.getOverlays().add(mLocationOverlay);
        MyLocationOverlayWithClick overlay = new MyLocationOverlayWithClick(mMapview);
        overlay.enableFollowLocation();
        overlay.enableMyLocation();

        Drawable currentDraw = ResourcesCompat.getDrawable( mRes, R.drawable.center, null);
        Bitmap currentIcon = null;
        currentIcon = ((BitmapDrawable) currentDraw).getBitmap();
        overlay.setPersonIcon(currentIcon);
        mMapview.getOverlayManager().add(overlay);


        IMapController mapController = fragmentMapBinding.map.getController();
        mapController.setZoom(9);
        //GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        //mapController.setCenter(startPoint);

        return fragmentMapBinding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation=location;
        GeoPoint myPosition = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMapview.getController().animateTo(myPosition);

   }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        //Configuration.getInstance().save(getActivity().getApplicationContext(), prefs);
        Configuration.getInstance().load(this.getActivity(), PreferenceManager.getDefaultSharedPreferences(this.getActivity()));
    }

    public static class MyLocationOverlayWithClick extends MyLocationNewOverlay{

        public MyLocationOverlayWithClick(MapView mapView) {
            super(mapView);
       }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e, MapView map) {
            if (getLastFix() != null)
                Toast.makeText(map.getContext(), "Tap! I am at " + getLastFix().getLatitude() + "," + getLastFix().getLongitude(), Toast.LENGTH_LONG).show();
            return true;

        }
    }

}
