package app.example.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.example.attendancemanager.adapter.ClassListAdapter;
import app.example.attendancemanager.model.CreateClassModel;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Dialog loadingDialog;

    public static int selectedClassIndex = 0;

    //for dialog
    private Dialog addClassDialog, joinClassDialog;
    private EditText classNameDialog;
    private Button createClassDialogBtn;
    private TextView randomTvDialog;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseUser currentUser;

    public static List<CreateClassModel> classList;
    RecyclerView recyclerView;
    ClassListAdapter classListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Welcome...");

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Class");
        currentUser = mAuth.getCurrentUser();


        loadingDialog = new Dialog(MainActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        addClassDialog = new Dialog(MainActivity.this);
        addClassDialog.setContentView(R.layout.add_class_dialog);
        addClassDialog.setCancelable(true);
        addClassDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        addClassDialog = new Dialog(MainActivity.this);
        addClassDialog.setContentView(R.layout.add_class_dialog);
        addClassDialog.setCancelable(true);
        addClassDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        randomTvDialog = addClassDialog.findViewById(R.id.randomTv);
        classNameDialog = addClassDialog.findViewById(R.id.ac_class_name);
        createClassDialogBtn = addClassDialog.findViewById(R.id.ac_create_btn);

        recyclerView = findViewById(R.id.class_recycler);

        randomTvDialog.setText(generateRandomString(6));

        createClassDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (classNameDialog.getText().toString().isEmpty()){
                    classNameDialog.setError("Please enter class name!");
                    return;
                }
                //randomTvDialog.setText(generateRandomString(6));
                addNewClass(classNameDialog.getText().toString());
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();

    }

    private void loadData() {
        loadingDialog.show();

        classList = new ArrayList<>();
        classListAdapter = new ClassListAdapter(this,classList);
        recyclerView.setAdapter(classListAdapter);

        Query query = FirebaseDatabase.getInstance().getReference("Class")
                .orderByChild("userId")
                .equalTo(currentUser.getEmail());
        query.addListenerForSingleValueEvent(valueEventListener);

    }


    ValueEventListener valueEventListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            classList.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CreateClassModel classModel = snapshot.getValue(CreateClassModel.class);
                    classList.add(classModel);

                    classListAdapter.notifyDataSetChanged();
                }
            }
            loadingDialog.dismiss();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void addNewClass(String title) {
        addClassDialog.dismiss();
        loadingDialog.show();

        String classTitle = classNameDialog.getText().toString().trim();

        if (!TextUtils.isEmpty(classTitle)){

            String classKey = randomTvDialog.getText().toString();

            currentUser = mAuth.getCurrentUser();

            String id = classKey;

            CreateClassModel blogModel = new CreateClassModel(id, currentUser.getEmail(), classTitle);

            reference.child(classKey).setValue(blogModel);

            classNameDialog.setText("");

            Toast.makeText(this,"Class created successfully",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
        loadingDialog.dismiss();

        loadData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.createClass:

                generateRandomString(6);

                classNameDialog.getText().clear();
                addClassDialog.show();

                break;

            case R.id.logOut:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public static String generateRandomString(int i) {

        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder randomStr = new StringBuilder();

        while (i > 0){

            Random rand = new Random();
            randomStr.append(characters.charAt(rand.nextInt(characters.length())));
            i--;

        }
        return randomStr.toString();
    }

}