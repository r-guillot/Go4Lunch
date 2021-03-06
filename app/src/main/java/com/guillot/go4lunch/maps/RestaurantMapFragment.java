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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guillot.go4lunch.base.BaseFragment;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.databinding.RestaurantMapFragmentBinding;
import com.guillot.go4lunch.model.Restaurant;
import com.guillot.go4lunch.details.RestaurantDetailActivity;

import java.util.List;

public class RestaurantMapFragment extends BaseFragment implements OnMapReadyCallback {

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
                if (viewModel.getUserIdList().getValue() != null && !viewModel.getUserIdList().getValue().isEmpty()){
                    List<String> listId = viewModel.getUserIdList().getValue();
                    if (listId.contains(restaurant.getRestaurantID())) {
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
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationUser));
            float ZOOM_USER_LOCATION_VALUE = 17;
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