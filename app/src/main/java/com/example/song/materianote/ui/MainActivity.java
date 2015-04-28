package com.example.song.materianote.ui;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.song.materianote.R;
import com.melnykov.fab.FloatingActionButton;

import data.Note;
import data.NoteColumns;
import data.NoteDatabaseHelper;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener,
        OnItemLongClickListener ,OnItemClickListener{

    private static final String TAG = "mainActivity";
    private FloatingActionButton fab ;

    private ListView  notes_list;

    private List<Note> contentList = new ArrayList<>();

    private NoteDatabaseHelper dbOpenHelper;

    private Cursor cursor;
    private ArrayAdapter mAdapter;




    // private String[] fromColumns = { NoteColumns.CONTENT, NoteColumns.TIME };
    // private int[] toViews = { android.R.id.text1 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notes_list = (ListView) findViewById(R.id.notes_list);
        dbOpenHelper = NoteDatabaseHelper.getInstance(MainActivity.this);
        cursor = getAllNote();
        // mAdapter = new SimpleCursorAdapter(this,
        // android.R.layout.simple_list_item_1, cursor, fromColumns,
        // toViews, 0);
        mAdapter = new ArrayAdapter<Note>(this,
                R.layout.note_title, getData());
        notes_list.setAdapter(mAdapter);
        notes_list.setOnItemLongClickListener(this);
        notes_list.setOnItemClickListener(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(notes_list);
        fab.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDataForListView();
    }


    /*
     * 更新ListView中的数据
     */
    private void updateDataForListView() {
        mAdapter = new ArrayAdapter<Note>(this,
                R.layout.note_title, getData());
        notes_list.setAdapter(mAdapter);
    }

    private List<Note> getData() {
        List<Note> noteList = new ArrayList<>();
        Cursor cursor = getAllNote();
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.set_id(cursor.getInt(cursor.getColumnIndex(NoteColumns.ID)));
                note.setContent(cursor.getString(cursor
                        .getColumnIndex(NoteColumns.CONTENT)));
                note.set_time(cursor.getString(cursor
                        .getColumnIndex(NoteColumns.TIME)));
                noteList.add(note);
            } while (cursor.moveToNext());

        }
        return noteList;
    }

    private Cursor getAllNote() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + NoteDatabaseHelper.TABLE_NOTE, null);
        return cursor;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                   final int position, long id) {
        new AlertDialog.Builder(MainActivity.this).setTitle("删除")
                .setMessage("您要删除所选的一条便签吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Note note = (Note) parent.getItemAtPosition(position);
                        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
                        db.delete(NoteDatabaseHelper.TABLE_NOTE,
                                "_id = ? and content = ? and _time = ? ",
                                new String[] { String.valueOf(note.get_id()),
                                        String.valueOf(note.getContent()),
                                        String.valueOf(note.get_time()) });
                        updateDataForListView();
                        Toast.makeText(MainActivity.this, "删除成功",
                                Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", null).show();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Note note = (Note) parent.getItemAtPosition(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }




}
