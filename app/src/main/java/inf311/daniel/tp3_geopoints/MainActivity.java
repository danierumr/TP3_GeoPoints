package inf311.daniel.tp3_geopoints;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends ListActivity {

    private final LatLng CASA = new LatLng(-20.75553442922549, -42.87802558671229);
    private final LatLng CANADA = new LatLng(45.51866729013082, -73.71249427723781);
    private final LatLng DPI = new LatLng(-20.76450768533006, -42.8680712796368);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        String menu [] = new String [] {"Casa", "Canada", "Departamento", "Fechar"};
        ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        setListAdapter(arrAdapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Intent it = new Intent(getBaseContext(), MapActivity.class);
        it.putExtra("casa_coord", CASA);
        it.putExtra("canada_coord", CANADA);
        it.putExtra("casa_dpi", DPI);

        switch (position){
            case 0:
                it.putExtra("coord", CASA);
                startActivity(it);
                break;
            case 1:
                it.putExtra("coord", CANADA);
                startActivity(it);
                break;
            case 2:
                it.putExtra("coord", DPI);
                startActivity(it);
                break;

            default:
                finish();

        }

    }

}