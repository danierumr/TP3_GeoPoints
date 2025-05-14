package inf311.daniel.tp3_geopoints;



import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    private LatLng CASA = new LatLng(0,0);
    private LatLng CANADA = new LatLng(0,0);
    private LatLng DPI = new LatLng(0,0);
    private LatLng coord = new LatLng(0,0);

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_map);

        Intent it = getIntent();
        coord = (LatLng) it.getParcelableExtra("coord");

        CASA = (LatLng) it.getParcelableExtra("casa_coord");
        CANADA = (LatLng) it.getParcelableExtra("canada_coord");
        DPI = (LatLng) it.getParcelableExtra("dpi_coord");

        SupportMapFragment sMapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        sMapFrag.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        map = gMap;

        if (coord != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(coord, 16);
            map.animateCamera(update);
        }

        map.addMarker(new MarkerOptions().position(CASA).title("CASA"));
        map.addMarker(new MarkerOptions().position(CANADA).title("CASA_IRMAO"));
        map.addMarker(new MarkerOptions().position(DPI).title("DPI"));
    }

    public void onClick_Home(View v) {
        if (map == null) return;

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CASA, 21);
        map.animateCamera(update);

    }

    public void onClick_Canada(View v) {
        if (map == null) return;

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CANADA, 21);
        map.animateCamera(update);

    }

    public void onClick_DPI(View v) {
        if (map == null) return;

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(DPI, 21);
        map.animateCamera(update);

    }

    public void onClick_Local(View v) {
        if (map == null) return;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        map.setMyLocationEnabled(true);

        map.getUiSettings().setMyLocationButtonEnabled(true);

        map.setOnMyLocationChangeListener(location -> {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(userLocation)
                    .title("Você está aqui"))
                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));

        });

    }


}
