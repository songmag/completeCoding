package mobileprog.mylocation;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng startLatLng;

    //세종대
    static double mLatitude = 37.550487;  //위도
    static double mLongitude = 127.073846;  //경도

    //어린이 대공원
    static double testLat1 = 37.547732;
    static double testLong1 = 127.074429;
    String dis;
    private GPSInfo gps;

    //건국대 일감호
    static double testLat2 = 37.540707;
    static double testLong2 = 127.076646;

    //내 위치
    static double test1;
    static double test2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        LatLng position = new LatLng(mLatitude, mLongitude);

        CircleOptions circle1KM = new CircleOptions().center(position).radius(300).strokeWidth(0f).fillColor(Color.parseColor("#880000ff"));

        gps = new GPSInfo(this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            test1 = gps.getLatitude();
            test2 = gps.getLongitude();
        }

        LatLng pos2 = new LatLng(test1,test2);

        mMap.addMarker(new MarkerOptions().position(position).title("세종대학교"));
        mMap.addMarker(new MarkerOptions().position(pos2).title("testData"));
        mMap.addCircle(circle1KM);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));

        dis = getDistance(test1, test2);

        double num = Double.parseDouble(dis);

        if (num <= 300.0)
            Toast.makeText(getApplicationContext(), dis + "/ 300m 안에 있음", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), dis + "/ 300m 밖에 있음", Toast.LENGTH_SHORT).show();
    }

    public String getDistance(double gps1, double gps2) {
        String distance;

        Location locationA = new Location("point A");
        locationA.setLatitude(gps1);
        locationA.setLongitude(gps2);

        Location locationB = new Location("point B");
        locationB.setLatitude(mLatitude);
        locationB.setLongitude(mLongitude);

        distance = Double.toString(locationB.distanceTo(locationA));

        return distance;
    }

}
