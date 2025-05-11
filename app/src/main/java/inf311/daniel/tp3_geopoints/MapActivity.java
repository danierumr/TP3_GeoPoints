package inf311.daniel.tp3_geopoints;



import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        MapFragment f = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        f.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        map = gMap;

        Intent it = getIntent();
        LatLng coord = (LatLng) it.getParcelableExtra("coord");

        if (coord != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(coord, 16);
            map.animateCamera(update);
        }
    }


}
