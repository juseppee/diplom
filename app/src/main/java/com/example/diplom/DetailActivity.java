package com.example.diplom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.grpc.Context;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailInfo;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";
    String userId = "";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private FloatingActionButton addTabButton;

    private List<String> tabTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailInfo = findViewById(R.id.detailInfo);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        addTabButton = findViewById(R.id.add_tab_button);

        tabTitles = new ArrayList<>();
        tabTitles.add("Главная вкладка");

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabTitles);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        addTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTab();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("Key");
            userId = bundle.getString("UserId");
            imageUrl = bundle.getString("Image");
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Estimates/" + userId);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        finish();
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Title", detailTitle.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Additional Info", detailInfo.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key)
                        .putExtra("UserId", userId);
                startActivity(intent);
            }
        });
    }

    private void addNewTab() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_tab, null);
        final EditText tabNameEditText = dialogView.findViewById(R.id.tab_name_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Добавление вкладки")
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tabName = tabNameEditText.getText().toString();
                        if (!tabName.isEmpty()) {
                            tabTitles.add(tabName);
                            viewPagerAdapter.notifyDataSetChanged();
                            viewPager.setCurrentItem(tabTitles.size() - 1);
                            saveData(tabName);
                        }
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    public void saveData(String tabName) {
        FirebaseDatabase.getInstance().getReference("Estimates").child(userId).child(key)
                .child("tabs").child(tabName).setValue(tabName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DetailActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "NOT Saved", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailActivity.this, "AAAAAAAAAAAA", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (tabTitles.size() > 1) {
            showDeleteConfirmationDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удаление вкладки");
        builder.setMessage("Вы уверены, что хотите удалить текущую вкладку?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int currentPosition = viewPager.getCurrentItem();
                tabTitles.remove(currentPosition);
                viewPagerAdapter.notifyDataSetChanged();

                if (currentPosition > 0) {
                    viewPager.setCurrentItem(currentPosition - 1);
                } else {
                    viewPager.setCurrentItem(0);
                }
            }
        });
        builder.setNegativeButton("Нет", null);
        builder.show();
    }

    protected class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<String> tabTitles;

        public ViewPagerAdapter(FragmentManager fm, List<String> tabTitles) {
            super(fm);
            this.tabTitles = tabTitles;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return MainTabFragment.newInstance();
            } else {
                return TabFragment.newInstance(tabTitles.get(position));
            }
        }

        @Override
        public int getCount() {
            return tabTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }
    }
}