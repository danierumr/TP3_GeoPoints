package inf311.daniel.tp3_geopoints;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Report extends ListActivity {

    BancoDados bd;
    ArrayList<Integer> logIds; // Para guardar os IDs dos logs exibidos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bd = new BancoDados(this);
        logIds = new ArrayList<>();

        // Busca todos os Logs para exibir na lista
        Cursor c = bd.buscar("Logs", null, "", "timestamp DESC");

        ArrayList<String> logs = new ArrayList<>();
        int colMsg = c.getColumnIndexOrThrow("msg");
        int colTime = c.getColumnIndexOrThrow("timestamp");
        int colId = c.getColumnIndexOrThrow("id");

        if (c.moveToFirst()) {
            do {
                String msg = c.getString(colMsg);
                String time = c.getString(colTime);
                logs.add(msg + " - " + time);
                logIds.add(c.getInt(colId));
            } while (c.moveToNext());
        }

        ArrayAdapter<String> arrAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, logs);
        setListAdapter(arrAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        int logId = logIds.get(position);

        Cursor c = bd.buscar(
                "Logs l, Location loc",
                new String[]{"loc.latitude", "loc.longitude"},
                "l.id_location = loc.id AND l.id = " + logId,
                ""
        );

        if (c.moveToFirst()) {
            double lat = c.getDouble(c.getColumnIndexOrThrow("latitude"));
            double lng = c.getDouble(c.getColumnIndexOrThrow("longitude"));

            String msg = "Latitude: " + lat + "\nLongitude: " + lng;
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Coordenadas não encontradas", Toast.LENGTH_SHORT).show();
        }

        c.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bd.fechar();
    }
}
