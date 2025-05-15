package inf311.daniel.tp3_geopoints;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends ListActivity {

    public BancoDados bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        // Banco de dados
        bd = new BancoDados(this);

        String menu [] = new String [] {"Casa", "Canada", "Departamento", "Relatorio", "Fechar"};
        ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        setListAdapter(arrAdapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Intent it = new Intent(getBaseContext(), MapActivity.class);
        it.putExtra("casa_coord", getLocationById(1));
        it.putExtra("canada_coord", getLocationById(2));
        it.putExtra("dpi_coord", getLocationById(3));

        int locationId = 0;
        String msg = "";

        LatLng coord = new LatLng(0,0);

        switch (position){
            case 0:
                locationId = 1;
                coord = getLocationById(locationId);
                it.putExtra("coord", coord);
                msg = "Casa";
                bd.inserirLog(msg, locationId);
                startActivity(it);
                break;
            case 1:
                locationId = 2;
                coord = getLocationById(locationId);
                it.putExtra("coord", coord);
                msg = "Canada";
                bd.inserirLog(msg, locationId);
                startActivity(it);
                break;
            case 2:
                locationId = 3;
                coord = getLocationById(locationId);
                it.putExtra("coord", coord);
                msg = "Departamento";
                bd.inserirLog(msg, locationId);
                startActivity(it);
                break;
            case 3:
                it = new Intent(getBaseContext(), Report.class);
                startActivity(it);
                break;
            default:
                finish();
                return;
        }


    }

    private LatLng getLocationById(int id) {
        Cursor c = bd.buscar("Location", null, "id = " + id, "");
        if (c.moveToFirst()) {
            double lat = c.getDouble(c.getColumnIndexOrThrow("latitude"));
            double lng = c.getDouble(c.getColumnIndexOrThrow("longitude"));
            return new LatLng(lat, lng);
        }
        return new LatLng(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bd.fechar();
    }

}