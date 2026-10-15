package com.example.myapplicationth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskChangeListener {

    private TextView tvUserName;
    private TextView tvUserDoc;
    private RecyclerView rvTodayTasks;
    private RecyclerView rvUpcomingTasks;
    private FloatingActionButton fabAddTask;

    private DatabaseHelper dbHelper;
    private TaskAdapter todayAdapter;
    private TaskAdapter upcomingAdapter;

    private String username = "abc123";
    private String displayName = "Nguyễn Văn A";
    private String docId = "doc123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIntentData();
        initViews();
        setupDatabaseAndLists();
        setupListeners();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("EXTRA_DISPLAY_NAME")) {
                displayName = intent.getStringExtra("EXTRA_DISPLAY_NAME");
            }
            if (intent.hasExtra("EXTRA_DOC_ID")) {
                docId = intent.getStringExtra("EXTRA_DOC_ID");
            }
            if (intent.hasExtra("EXTRA_USERNAME")) {
                username = intent.getStringExtra("EXTRA_USERNAME");
            }
        }
    }

    private void initViews() {
        TextView tvAvatar = findViewById(R.id.tvAvatar);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserDoc = findViewById(R.id.tvUserDoc);
        rvTodayTasks = findViewById(R.id.rvTodayTasks);
        rvUpcomingTasks = findViewById(R.id.rvUpcomingTasks);
        fabAddTask = findViewById(R.id.fabAddTask);

        tvUserName.setText(displayName);
        tvUserDoc.setText(docId);
        if (displayName != null && !displayName.isEmpty()) {
            tvAvatar.setText(displayName.substring(0, 1).toUpperCase());
        }
    }

    private void setupDatabaseAndLists() {
        dbHelper = new DatabaseHelper(this);

        rvTodayTasks.setLayoutManager(new LinearLayoutManager(this));
        rvUpcomingTasks.setLayoutManager(new LinearLayoutManager(this));

        loadTasks();
    }

    private void loadTasks() {
        List<Task> todayTasks = dbHelper.getTasks(true, username);
        List<Task> upcomingTasks = dbHelper.getTasks(false, username);

        if (todayAdapter == null) {
            todayAdapter = new TaskAdapter(todayTasks, this);
            rvTodayTasks.setAdapter(todayAdapter);
        } else {
            todayAdapter.updateTasks(todayTasks);
        }

        if (upcomingAdapter == null) {
            upcomingAdapter = new TaskAdapter(upcomingTasks, this);
            rvUpcomingTasks.setAdapter(upcomingAdapter);
        } else {
            upcomingAdapter.updateTasks(upcomingTasks);
        }
    }

    private void setupListeners() {
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            intent.putExtra("EXTRA_USERNAME", username);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    @Override
    public void onTaskStatusChanged(Task task, boolean isCompleted) {
        dbHelper.updateTaskCompletion(task.getId(), isCompleted);
    }

    @Override
    public void onTaskDeleted(Task task) {
        dbHelper.deleteTask(task.getId());
        Toast.makeText(this, "Đã xóa công việc!", Toast.LENGTH_SHORT).show();
        loadTasks();
    }
}
