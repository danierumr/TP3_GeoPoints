package inf311.daniel.tp3_geopoints;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private LatLng CASA = new LatLng(0,0);
    private LatLng CANADA = new LatLng(0,0);
    private LatLng DPI = new LatLng(0,0);
    private LatLng coord = new LatLng(0,0);

    Marker lastLocation;

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // intervalo de 10 segundos
        locationRequest.setFastestInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(getApplicationContext(), "Falha ao obter localização", Toast.LENGTH_SHORT).show();
                    return;
                }

                Location location = locationResult.getLastLocation();
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                if (lastLocation != null) {
                    lastLocation.remove();
                }

                lastLocation = map.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .title("Você está aqui")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 19));

                // Após receber a atualização, pare de pedir updates para economizar bateria
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        };

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

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Toast.makeText(getBaseContext(), "Item Casa clicado", Toast.LENGTH_SHORT).show();

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CASA, 21);
        map.animateCamera(update);

    }

    public void onClick_Canada(View v) {
        if (map == null) return;

        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        Toast.makeText(getBaseContext(), "Item Canada clicado", Toast.LENGTH_SHORT).show();

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(CANADA, 21);
        map.animateCamera(update);

    }

    public void onClick_DPI(View v) {
        if (map == null) return;

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        Toast.makeText(getBaseContext(), "Item DPI clicado", Toast.LENGTH_SHORT).show();

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(DPI, 21);
        map.animateCamera(update);

    }

    public void onClick_Local(View v) {
        if (map == null) {
            Toast.makeText(this, "Mapa ainda não carregado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }


}
