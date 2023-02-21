package com.apnitor.arete.pro.createjob;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.util.PreferenceHandler;
import com.shawnlin.numberpicker.NumberPicker;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    String LOG_TAG = "MapsActivity";
    double mLatitude, mLongitude;
    String prevActivity;
    String mSqft;
    private GetAddress mGetAddress;
    private GoogleMap mMap;
    private PlaceAutocompleteFragment autocompleteFragment;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    String mCleaningType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        searchLocation();
        configureCameraIdle();
        try {
            Bundle mBundle = getIntent().getExtras();
            assert mBundle != null;
            prevActivity = mBundle.getString("prevActivity", "");
            if (prevActivity.equalsIgnoreCase("CreateJob")) {
                mCleaningType = mBundle.getString("cleaning", "");
            }
            mLatitude = Double.parseDouble(mBundle.getString("latitude", "41.7145569"));
            mLongitude = Double.parseDouble(mBundle.getString("longitude", "-93.6764076"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureCameraIdle() {
        onCameraIdleListener = () -> {
            LatLng latLng = mMap.getCameraPosition().target;
            Geocoder geocoder = new Geocoder(MapsActivity.this);
            try {
                List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    String locality = addressList.get(0).getAddressLine(0);
                    String country = addressList.get(0).getCountryName();
                    if (!locality.isEmpty() && !country.isEmpty())
                        Toast.makeText(MapsActivity.this, "" + locality + "  " + country, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void searchLocation() {
        try {
            autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));
                    Log.d(LOG_TAG, " Place is " + place);
                    getAddress(place.getLatLng().latitude, place.getLatLng().longitude);
                }

                @Override
                public void onError(Status status) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap mGoogleMap) {
        mMap = mGoogleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (mLatitude != 0.0 && mLongitude != 0.0)
            setPreviousLocationOnMap(mLatitude, mLongitude);


       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }*/
        // mMap.setMyLocationEnabled(true);

        // Enable / Disable zooming controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable / Disable my location button
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        // Enable / Disable Compass icon
        mMap.getUiSettings().setCompassEnabled(false);

        // Enable / Disable Rotate gesture`enter code here`
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        // Enable / Disable zooming functionality
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnCameraIdleListener(onCameraIdleListener);

        // Add a marker in Sydney and move the camera
        LatLng mohali = new LatLng(41.7145569, -93.6764076);
        mMap.addMarker(new MarkerOptions().position(mohali).title("Marker in Mohali"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mohali));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
//        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//            @Override
//            public void onCameraMoveStarted(int i) {
//              /*  mDragTimer.start();
//                mTimerIsRunning = true;*/
//            }
//        });

        mMap.setOnCameraIdleListener(() -> {
            // Cleaning all the markers.
            if (mMap != null) {
                mMap.clear();
            }

            LatLng mPosition = mMap.getCameraPosition().target;
            Log.d(LOG_TAG, " setOnCameraIdleListener Latitude " + mPosition.latitude + " longitude " + mPosition.longitude);
            //   float mZoom = mMap.getCameraPosition().zoom;
            getAddress(mPosition.latitude, mPosition.longitude);
           /* if (mTimerIsRunning) {
                mDragTimer.cancel();
            }
*/  /*CameraPosition cameraPosition = new CameraPosition.Builder()
                   .zoom(14).target(mPosition).build();
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));*/
        });

        //

    }

    public String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            System.out.println("get address");
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                System.out.println("size====" + addresses.size());
                Address address = addresses.get(0);
                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                    if (i == addresses.get(0).getMaxAddressLineIndex()) {
                        result.append(addresses.get(0).getAddressLine(i));
                    } else {
                        result.append(addresses.get(0).getAddressLine(i) + ",");
                    }
                }
                System.out.println("ad==" + address.toString() + ",");
                mGetAddress = new GetAddress();
                String startingAddress[] = address.getAddressLine(0).split(",");
                String[] newArray = Arrays.copyOf(startingAddress, startingAddress.length - 2);

                mGetAddress.street = TextUtils.join(",", newArray);
                Log.e("", mGetAddress.street + " new " + newArray.toString());

                mGetAddress.city = address.getLocality();
                mGetAddress.state = address.getAdminArea();
                mGetAddress.country = address.getCountryName();
                mGetAddress.zipCode = Integer.valueOf(address.getPostalCode());
                mGetAddress.latitude = Double.valueOf(address.getLatitude());
                mGetAddress.longitude = Double.valueOf(address.getLongitude());
                // mGetAddress.houseNo = address.getFeatureName();
                System.out.println("result---" + result.toString());
                Log.e("Values", "adminArea" + address.getAdminArea() + ", address line," + address.getAddressLine(0) + "addressLocality" + address.getLocality());
                autocompleteFragment.setText(result.toString()); // Here is you AutoCompleteTextView where you want to set your string address (You can remove it if you not need it)
            }
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        return result.toString();
    }


    public void onSubmitAddress(View view) {
        if (prevActivity.equals("Profile")) {
            Intent mIntent = new Intent();
            mIntent.putExtra("Address", mGetAddress);
            setResult(101, mIntent);
            finish();
        } else {
            if (mCleaningType.equalsIgnoreCase("carpet") || mCleaningType.equalsIgnoreCase("window")) {
                Intent mIntent = new Intent();
                mIntent.putExtra("Address", mGetAddress);
                setResult(101, mIntent);
                finish();
            } else
                showDialogWithLayout();
        }
    }

    void setPreviousLocationOnMap(double latitude, double longitude) {
        try {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 19.0f));
            getAddress(latitude, longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showDialogWithLayout() {
        try {

            View customView = View.inflate(MapsActivity.this, R.layout.dialog_task_size, null);
            MaterialDialog builder;
            builder = new MaterialDialog.Builder(MapsActivity.this)
                    .customView(customView, true)
                    .cancelable(true).title("How Big is Your House?").build();

            TextView startText = customView.findViewById(R.id.txtStart);
            TextView endText = customView.findViewById(R.id.txtEnd);
            IndicatorSeekBar seekBar = customView.findViewById(R.id.seekBar);
            NumberPicker bedroomPicker = customView.findViewById(R.id.noOFBedrooms);
            NumberPicker kitchenPicker = customView.findViewById(R.id.noOfKitchens);
            NumberPicker bathroomPicker = customView.findViewById(R.id.noOfBathroom);
            NumberPicker otherPicker = customView.findViewById(R.id.noOfOther);
            AppCompatButton mSubmitButton = customView.findViewById(R.id.btnTaskSize);
            setTotalTaskSize(bedroomPicker, PreferenceHandler.PREF_TOTAL_BEDROOM);
            setTotalTaskSize(bathroomPicker, PreferenceHandler.PREF_TOTAL_BATHROOM);
            setTotalTaskSize(kitchenPicker, PreferenceHandler.PREF_TOTAL_KITCHEN);
            setTotalTaskSize(otherPicker, PreferenceHandler.PREF_TOTAL_OTHER);

            // TODO Delete This Code after testing the Scenario
            // Float sqft = PreferenceHandler.readFloat(this, PreferenceHandler.PREF_TOTAL_SQFT, 0.0f); //
           /* mSqft.setText(String.valueOf(sqft)+" Sqft");
            //mSqft.setText(String.valueOf(sqft));
            String sqftValue = String.valueOf(sqft);
            mSqft.setSelection(String.valueOf(sqft).length());
           */
            seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
                @Override
                public void onSeeking(SeekParams seekParams) {
                    String sqft = seekParams.progress + "";
                    mSqft = sqft;
                    startText.setText(seekParams.progress + "");
                }

                @Override
                public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

                }
            });

            mSubmitButton.setOnClickListener(v -> {
                String sqft = mSqft;
                int bedroom = getSelectedValue(bedroomPicker);
                int bathroom = getSelectedValue(bathroomPicker);
                int kitchen = getSelectedValue(kitchenPicker);
                int other = getSelectedValue(otherPicker);
                if (sqft == null || sqft.isEmpty()) {
                    Toast.makeText(MapsActivity.this, "Please choose a valid sqft value", Toast.LENGTH_SHORT).show();
                } else if (sqft.startsWith("0")) {
                    Toast.makeText(MapsActivity.this, "Please choose a valid sqft value", Toast.LENGTH_SHORT).show();
                } else {
                    PreferenceHandler.writeInteger(MapsActivity.this, PreferenceHandler.PREF_TOTAL_BEDROOM, bedroom);
                    PreferenceHandler.writeInteger(MapsActivity.this, PreferenceHandler.PREF_TOTAL_BATHROOM, bathroom);
                    PreferenceHandler.writeInteger(MapsActivity.this, PreferenceHandler.PREF_TOTAL_KITCHEN, kitchen);
                    PreferenceHandler.writeInteger(MapsActivity.this, PreferenceHandler.PREF_TOTAL_OTHER, other);
                    Float sqrFootArea = Float.valueOf(mSqft);
                    PreferenceHandler.writeFloat(MapsActivity.this, PreferenceHandler.PREF_TOTAL_SQFT, sqrFootArea);//M1 4
                    Intent mIntent = new Intent();
                    mIntent.putExtra("Address", mGetAddress);
                    /*
                      Add House Details here
                      So that it can go back to Create Job Activity
                     */
                    mIntent.putExtra("Bedroom", bedroom);
                    mIntent.putExtra("Bathroom", bathroom);
                    mIntent.putExtra("Kitchen", kitchen);
                    mIntent.putExtra("Other", other);
                    mIntent.putExtra("SQFT", sqrFootArea);
                    setResult(101, mIntent);
                    //
                    builder.dismiss();
                    finish();
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer getSelectedValue(NumberPicker numberPicker) {

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);

        // Set fading edge enabled
        numberPicker.setFadingEdgeEnabled(true);

        // Set scroller enabled
        numberPicker.setScrollerEnabled(true);

        // Set wrap selector wheel
        numberPicker.setWrapSelectorWheel(true);
        return numberPicker.getValue();
    }

    private void setTotalTaskSize(NumberPicker numberPicker, String key) {
        int total = PreferenceHandler.readInteger(MapsActivity.this, key, 1);
        numberPicker.setValue(total);
    }
}
