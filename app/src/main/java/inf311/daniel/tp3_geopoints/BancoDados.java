package inf311.daniel.tp3_geopoints;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import java.time.Instant;

public class BancoDados {

    protected SQLiteDatabase db;

    private final String NOME_BANCO = "logs_location";

    private final String [] SCRIPT_DATABASE_CREATE = new String[] {
      "CREATE TABLE IF NOT EXISTS Logs (id INTEGER PRIMARY KEY AUTOINCREMENT, msg TEXT, timestamp TEXT NOT NULL, id_location INTEGER NOT NULL, " +
      "FOREIGN KEY (id_location) REFERENCES Location (id) );",
      "CREATE TABLE IF NOT EXISTS Location (id INTEGER PRIMARY KEY AUTOINCREMENT, descricao TEXT, latitude REAL, longitude REAL);"
    };

    public BancoDados(Context ctx) {

        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);

        Cursor c = buscar("sqlite_master", null, "type= 'table'", "");

        if(c.getCount() == 1) {
            for (String s : SCRIPT_DATABASE_CREATE) {
                db.execSQL(s);
            }
            Log.i("BANCO_DADOS", "Criou tabelas do banco e as populou");
        }

        popularLocation();

    }

    public long inserir(String tabela, ContentValues valores) {

        if (tabela == "Location"){
            Cursor c = buscar("Location", null, "", "");
            if (c.getCount() > 3) {
                Log.w("BANCO_DADOS", "Nao pode inserir nova Location");
                return -1;
            }
        }


        long id = db.insert(tabela, null, valores);

        Log.i("BANCO_DADOS", "Cadastrou registro com o id [" + id + "]");
        return id;
    }

    public int atualizar(String tabela, ContentValues valores, String where) {
        int count = db.update(tabela, valores, where, null);

        Log.i("BANCO_DADOS", "Atualizou [" + count + "] registros");
        return count;
    }

    public int deletar(String tabela, String where) {
        int count = db.delete(tabela, where, null);

        Log.i("BANCO_DADOS", "Deletou [" + count + "] registros");
        return count;
    }

    public Cursor buscar(String tabela, String colunas[], String where, String orderBy) {

        Cursor c;
        if(!where.isEmpty())
            c = db.query(tabela, colunas, where, null, null, null, orderBy);
        else
            c = db.query(tabela, colunas, null, null, null, null, orderBy);
        Log.i("BANCO_DADOS", "Realizou uma busca e retornou [" + c.getCount() + "] registros.");

        return c;
    }

    public void abrir(Context ctx) {
        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
        Log.i("BANCO_DADOS", "Abriu conexao com o banco");
    }

    public void fechar() {
        if (db != null) {
            db.close();
            Log.i("BANCO_DADOS", "Fechou conexao com o banco");
        }
    }

    public void popularLocation() {
        Cursor c = buscar("Location", null, "", "");
        if (c.getCount() == 0) {
            ContentValues vals = new ContentValues();

            vals.put("descricao", "Local da casa");
            vals.put("latitude", -20.75553442922549);
            vals.put("longitude", -42.87802558671229);
            inserir("Location", vals);

            vals = new ContentValues();
            vals.put("descricao", "Local da casa irmao");
            vals.put("latitude", 45.51866729013082);
            vals.put("longitude", -73.71249427723781);
            inserir("Location", vals);

            vals = new ContentValues();
            vals.put("descricao", "Local do DPI");
            vals.put("latitude", -20.76450768533006);
            vals.put("longitude", -42.8680712796368);
            inserir("Location", vals);

            Log.i("BANCO_DADOS", "Populou tabela Location");
        }
    }

    public void inserirLog(String msg, int id_location) {
        ContentValues vals = new ContentValues();
        vals.put("msg", msg);

        Instant time = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time = Instant.now();
        }

        vals.put("timestamp", time + "" );

        vals.put("id_location", id_location);
        inserir("Logs", vals);
    }

}
