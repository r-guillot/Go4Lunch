package com.guillot.go4lunch.maps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guillot.go4lunch.authentication.SignInActivity;
import com.guillot.go4lunch.base.BaseFragment;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.RestaurantMapFragmentBinding;
import com.guillot.go4lunch.main.CoreActivity;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.details.RestaurantDetailActivity;

import java.util.List;
import java.util.Objects;

public class RestaurantMapFragment extends BaseFragment implements OnMapReadyCallback {
/*
test push
 */
    private final String TAG = RestaurantMapFragment.class.getSimpleName();

    private final float ZOOM_USER_LOCATION_VALUE = 17;
    public final static String RESTAURANT = "RESTAURANT_ID";

    private RestaurantMapFragmentBinding binding;
    private RestaurantMapViewModel viewModel;
    private GoogleMap googleMap;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_map_fragment, container, false);
        configureBinding(view);
        createMapView(savedInstanceState);
        initViewModel();
        return view;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        Log.d(TAG, "onResume: " +mapView);
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        googleMap.clear();
        mapView.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void getLocationUser(LatLng locationUser) {
        Log.d(TAG, "getLocationUser: " + locationUser.toString());
        String location = locationUser.latitude + "," + locationUser.longitude;
        viewModel.init();
        viewModel.executeNetworkRequest(locationUser);
        viewModel.getRestaurantsList().observe(this, this::initRestaurantMarker);
        centerCameraOnGPSLocation(locationUser);
        locationButtonDesign();
        viewModel.updateUserLocation(location);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void initRestaurantMarker(List<Restaurant> restaurants) {
        int icon;
        if (googleMap != null) {
            googleMap.clear();
            for (Restaurant restaurant : restaurants) {
//                double latitude = restaurant.getLatitude();
//                double longitude = restaurant.getLongitude();
//                LatLng positionRestaurant = new LatLng(latitude, longitude);
//                viewModel.getUserIdList().getValue();
                Log.d(TAG, "initRestaurantMarker1: " + viewModel.getUserIdList().getValue());
//                Log.d(TAG, "initRestaurantMarker2: " + viewModel.getAllOccupiedRestaurant());
                if (viewModel.getUserIdList().getValue() != null && !viewModel.getUserIdList().getValue().isEmpty()){
//                if (viewModel.getAllOccupiedRestaurant().getValue() != null && !viewModel.getAllOccupiedRestaurant().getValue().isEmpty()) {
//                    List<String> listId = viewModel.getAllOccupiedRestaurant().getValue();
                    List<String> listId = viewModel.getUserIdList().getValue();
                    Log.d(TAG, "listId: " + listId);
                    Log.d(TAG, "getRestaurantID: " + restaurant.getRestaurantID() );
                    if (listId.contains(restaurant.getRestaurantID())) {
                        Log.w(TAG, "orange: " + restaurant.getName()+ " + " +restaurant.getRestaurantID() + " + " + listId);
                        icon = R.drawable.marker_restaurant_orange_48px;
                    }
                    else {
                        icon = R.drawable.marker_restaurant_green_48px;
                    }
                } else {
                    icon = R.drawable.marker_restaurant_green_48px;
                }
                setIconMarker(restaurant, icon);
            }
            onMarkerClick();
        }
    }

    private void setIconMarker(Restaurant restaurant, int icon) {
        double latitude = restaurant.getLatitude();
        double longitude = restaurant.getLongitude();
        LatLng positionRestaurant = new LatLng(latitude, longitude);
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(positionRestaurant)
                .title(restaurant.getName())
                .icon(BitmapDescriptorFactory.fromResource(icon)));
        marker.setTag(restaurant.getRestaurantID());
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(RestaurantMapViewModel.class);
    }

    private void createMapView(Bundle savedInstanceState) {
        mapView = (MapView) binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void configureBinding(View view) {
        binding = RestaurantMapFragmentBinding.bind(view);
    }

    @SuppressLint("MissingPermission")
    private void centerCameraOnGPSLocation(LatLng locationUser) {
        if (locationUser != null && googleMap != null) {
            Log.d(TAG, "centerCameraOnGPSLocation: " +locationUser);
            Log.d(TAG, "centerCameraOnGPSLocation: " + googleMap);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationUser));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_USER_LOCATION_VALUE));
            googleMap.setMyLocationEnabled(true);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    centerCameraOnGPSLocation(locationUser);
                }
            }, 50);
        }
    }

    private void onMarkerClick() {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String placeId = (String) marker.getTag();
                Log.d(TAG, "onMarkerClick: " + placeId);

                Intent detailIntent = new Intent(getActivity(), RestaurantDetailActivity.class);
                detailIntent.putExtra(RESTAURANT, placeId);
                startActivity(detailIntent);
                return false;
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void locationButtonDesign() {
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

        RelativeLayout.LayoutParams rLP = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rLP.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rLP.setMargins(0,0,30,180);
    }

}