package com.example.song.materianote.ui;

import java.text.SimpleDateFormat;
import java.util.Date;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.example.song.materianote.R;

import data.Note;
import data.NoteColumns;
import data.NoteDatabaseHelper;

public class EditNoteActivity extends Activity {


    private static final String TAG = "editNoteActivity";
    private EditText mEditText;
    private NoteDatabaseHelper dbOpenHelper;
    private Note note;
    private TextView time_edit_tv;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        mEditText = (EditText) findViewById(R.id.note_edit_view);
        time_edit_tv = (TextView) findViewById(R.id.time_edit_tv);
        dbOpenHelper = NoteDatabaseHelper.getInstance(EditNoteActivity.this);
        note = (Note) getIntent().getSerializableExtra("note");
        setTitle("");
        mEditText.setText(note.getContent());
        time_edit_tv.setText(note.get_time());
    }


    @Override
    protected void onPause() {
        super.onPause();
        String content = mEditText.getText().toString();
        if (content != null && content.trim() != "" && content != "" && content.length() != 0) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NoteColumns.CONTENT, content);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
            values.put(NoteColumns.TIME, sdf.format(new Date()));
            db.update(NoteDatabaseHelper.TABLE_NOTE, values, "_id = ?", new String[]{String.valueOf(note.get_id())});
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        mShareActionProvider.setShareIntent(getDefaultIntent());
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String content = mEditText.getText().toString();
        intent.putExtra(Intent.EXTRA_TEXT, "\n-----------来自简洁标签")
                .setType("text/plain");
        Log.d(TAG, mEditText.getText().toString());
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_item_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                String content = mEditText.getText().toString();
                intent.putExtra(Intent.EXTRA_TEXT, content + "\n------------来自简洁便签");
                intent.setType("text/plain");
                setShareIntent(intent);
                return true;

        }
        return true;
    }
}
