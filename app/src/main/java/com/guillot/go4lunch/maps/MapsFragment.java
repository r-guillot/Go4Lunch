package com.guillot.go4lunch.maps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guillot.go4lunch.BaseFragment;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.Restaurant.DetailsRestaurant;
import com.guillot.go4lunch.Restaurant.model.Restaurant;
import com.guillot.go4lunch.Utils;

import java.util.List;

public class MapsFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {
    private final String TAG = MapsFragment.class.getSimpleName();

    private MapsViewModel mMapsViewModel;
    private Context context;
    private GoogleMap mMap;
    private View mView;
    private MapView mMapView;
    public List<Restaurant> restaurantListRV;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.maps_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        // init map
        // TODO: 03/09/2020 viewbindind mapview
        mMapView = view.findViewById(R.id.fragment_map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    public void initViewModel() {
        mMapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationButtonDesign();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getWebService();
            }
        },5000);
        onMarkerClick();
    }

    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(context, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }


    @SuppressLint("MissingPermission")
    private void getWebService() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LocationUser, 40f));
                mMap.setMyLocationEnabled(true);
                Log.d("list", "parametre :" + LocationUser);

                mMapsViewModel.setRetrofit(LocationUser, radius, type, fields, key);
                Log.d("list", "parametre fragment :" + LocationUser + " " + radius + " " + type + " " + key);
                mMapsViewModel.getRestaurants();
                createMarker();
    }

    private void createMarker() {
        Bitmap markerRestaurantGreen = BitmapFactory.decodeResource(getResources(), R.drawable.marker_restaurant_green);
        markerRestaurantGreen = Utils.scaleBitmap(markerRestaurantGreen, 260, 152);
        Bitmap finalMarkerRestaurantGreen = markerRestaurantGreen;
        mMapsViewModel.RestaurantListLiveData.observe(this, liveDataListRestaurant -> {
                Log.d("locationUpdate", "observer");
                if (liveDataListRestaurant != null) {
                    restaurantListRV = liveDataListRestaurant;
                    Log.d("locationUpdate", "list size base " + restaurantListRV.size());

                    for (int i = 0; i < restaurantListRV.size(); i++) {
                        Restaurant markerRestaurant = restaurantListRV.get(i);
                        double lat = markerRestaurant.getGeometry().getLocation().getLat();
                        double lng = markerRestaurant.getGeometry().getLocation().getLng();
                        LatLng positionRestaurant = new LatLng(lat, lng);

                        mMap.addMarker(new MarkerOptions()
                                .position(positionRestaurant)
                                .title(markerRestaurant.getName())
                                .icon(BitmapDescriptorFactory.fromBitmap(finalMarkerRestaurantGreen)));
                    }
                }
        });
    }

    private void onMarkerClick() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerId = marker.getId();
                String markerName = marker.getTitle();

                Intent detailIntent = new Intent(getActivity(), DetailsRestaurant.class);

                detailIntent.putExtra("markerId", markerId);
                detailIntent.putExtra("markerName", markerName);
                startActivity(detailIntent);
                return false;
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void locationButtonDesign() {
        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

        RelativeLayout.LayoutParams rLP = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rLP.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rLP.setMargins(0,0,30,180);
    }
}