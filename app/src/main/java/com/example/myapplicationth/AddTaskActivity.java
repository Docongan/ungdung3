package com.example.myapplicationth;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etTaskTitle;
    private EditText etTaskTime;
    private RadioButton rbToday;
    private EditText etTaskNote;
    private Button btnCancel;
    private Button btnSave;

    private DatabaseHelper dbHelper;
    private String username = "abc123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        if (getIntent() != null && getIntent().hasExtra("EXTRA_USERNAME")) {
            username = getIntent().getStringExtra("EXTRA_USERNAME");
        }

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etTaskTitle = findViewById(R.id.etTaskTitle);
        etTaskTime = findViewById(R.id.etTaskTime);
        rbToday = findViewById(R.id.rbToday);
        etTaskNote = findViewById(R.id.etTaskNote);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        etTaskTime.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    etTaskTime.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void saveTask() {
        String title = etTaskTitle.getText() != null ? etTaskTitle.getText().toString().trim() : "";
        String inputTime = etTaskTime.getText() != null ? etTaskTime.getText().toString().trim() : "";
        String note = etTaskNote.getText() != null ? etTaskNote.getText().toString().trim() : "";
        boolean isToday = rbToday.isChecked();

        if (TextUtils.isEmpty(title)) {
            etTaskTitle.setError(getString(R.string.empty_task_name_error));
            etTaskTitle.requestFocus();
            return;
        }

        String finalTime = TextUtils.isEmpty(inputTime) ? (isToday ? "Hôm nay" : "Sắp tới") : inputTime;

        Task newTask = new Task(title, finalTime, note, isToday, false, username);
        long result = dbHelper.addTask(newTask);

        if (result != -1) {
            Toast.makeText(this, getString(R.string.add_task_success), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra khi lưu công việc!", Toast.LENGTH_SHORT).show();
        }
    }
}
