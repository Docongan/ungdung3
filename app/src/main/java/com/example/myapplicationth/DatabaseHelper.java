package com.example.myapplicationth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_IS_TODAY = "is_today";
    public static final String COLUMN_IS_COMPLETED = "is_completed";
    public static final String COLUMN_USERNAME = "username";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_IS_TODAY + " INTEGER, " +
                COLUMN_IS_COMPLETED + " INTEGER, " +
                COLUMN_USERNAME + " TEXT" +
                ")";
        db.execSQL(createTableQuery);

        // Insert sample initial tasks
        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Today tasks
        ContentValues values1 = new ContentValues();
        values1.put(COLUMN_TITLE, "Hoàn thành báo cáo môn Lập trình Android");
        values1.put(COLUMN_TIME, "17:00 Hôm nay");
        values1.put(COLUMN_NOTE, "Nộp file PDF và mã nguồn qua LMS");
        values1.put(COLUMN_IS_TODAY, 1);
        values1.put(COLUMN_IS_COMPLETED, 1);
        values1.put(COLUMN_USERNAME, "abc123");
        db.insert(TABLE_TASKS, null, values1);

        ContentValues values2 = new ContentValues();
        values2.put(COLUMN_TITLE, "Học bài Kotlin & Java RecyclerView");
        values2.put(COLUMN_TIME, "20:00 Hôm nay");
        values2.put(COLUMN_NOTE, "Xem lại Adapter và ViewHolder pattern");
        values2.put(COLUMN_IS_TODAY, 1);
        values2.put(COLUMN_IS_COMPLETED, 0);
        values2.put(COLUMN_USERNAME, "abc123");
        db.insert(TABLE_TASKS, null, values2);

        // Upcoming tasks
        ContentValues values3 = new ContentValues();
        values3.put(COLUMN_TITLE, "Nộp bài tập lớn To-Do List App");
        values3.put(COLUMN_TIME, "18/10/2026");
        values3.put(COLUMN_NOTE, "Chuẩn bị slide thuyết trình demo");
        values3.put(COLUMN_IS_TODAY, 0);
        values3.put(COLUMN_IS_COMPLETED, 0);
        values3.put(COLUMN_USERNAME, "abc123");
        db.insert(TABLE_TASKS, null, values3);

        ContentValues values4 = new ContentValues();
        values4.put(COLUMN_TITLE, "Họp nhóm đồ án cuối kỳ");
        values4.put(COLUMN_TIME, "20/10/2026");
        values4.put(COLUMN_NOTE, "Phân công công việc các màn hình tiếp theo");
        values4.put(COLUMN_IS_TODAY, 0);
        values4.put(COLUMN_IS_COMPLETED, 0);
        values4.put(COLUMN_USERNAME, "abc123");
        db.insert(TABLE_TASKS, null, values4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_TIME, task.getTime());
        values.put(COLUMN_NOTE, task.getNote());
        values.put(COLUMN_IS_TODAY, task.isToday() ? 1 : 0);
        values.put(COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(COLUMN_USERNAME, task.getUsername());

        long result = db.insert(TABLE_TASKS, null, values);
        db.close();
        return result;
    }

    public List<Task> getTasks(boolean isToday, String username) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_IS_TODAY + " = ? AND (" + COLUMN_USERNAME + " = ? OR " + COLUMN_USERNAME + " = 'abc123')";
        String[] selectionArgs = new String[]{isToday ? "1" : "0", username != null ? username : "abc123"};

        Cursor cursor = db.query(
                TABLE_TASKS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                COLUMN_ID + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE));
                boolean today = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_TODAY)) == 1;
                boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1;
                String user = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));

                Task task = new Task(id, title, time, note, today, completed, user);
                taskList.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return taskList;
    }

    public void updateTaskCompletion(int taskId, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_COMPLETED, isCompleted ? 1 : 0);

        db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
    }
}
