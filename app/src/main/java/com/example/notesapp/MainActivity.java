package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView mFloatingButton;
    Adapter mAdapter;
    List<Model> noteslist;
    Database database;
    TextView mTextview;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextview = findViewById(R.id.textView_id);
        recyclerView = findViewById(R.id.recycler_id);
        mFloatingButton = findViewById(R.id.fab);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layout_main);

        noteslist = new ArrayList<>();
        database = new Database(this);

        fetchAllNotes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter(this, MainActivity.this,noteslist);
        recyclerView.setAdapter(mAdapter);

        if ( noteslist.isEmpty()){
            mTextview.setText("No hay notas.");
        } else {
            mTextview.setVisibility(View.GONE);
        }

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddNotesActivity.class));
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Buscar notas...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void fetchAllNotes(){
        Cursor cursor = database.readNotes();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No hay notas", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                noteslist.add(new Model(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
                System.out.println("Notes:-"+noteslist.indexOf(1));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.delete_all_notes){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Quieres borrar todas las notas?")
            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteAllNotes();

                }
            })
             .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                 }
             })
             .show();

        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteAllNotes(){
        Database db = new Database(MainActivity.this);
        db.deleteAllNotes();
        recreate();
        Toast.makeText(this, "Borrada Exitosamente !", Toast.LENGTH_SHORT).show();
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Model item = mAdapter.getList().get(position);
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            Database database = new Database(MainActivity.this);
            database. deleteSingleItem(item.getId());
        }
    };
}