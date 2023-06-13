package com.example.diplom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.diplom.model.EstimatedTimeTab;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {

    String detailDesc, detailTitle, detailInfo;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";
    String userId = "";
    String formattedDate = "";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private FloatingActionButton addTabButton;
    MainTabFragment fragment;
    private List<String> tabTitles;
    private List<EstimatedTimeTab> estimatedTimeTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailImage = findViewById(R.id.detailImage);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        tabLayout = findViewById(R.id.tab_layout);

        viewPager = findViewById(R.id.view_pager);
        addTabButton = findViewById(R.id.add_tab_button);
        fragment = MainTabFragment.newInstance();
        estimatedTimeTabs = new ArrayList<>();
        tabTitles = new ArrayList<>();
        tabTitles.add("Main tab");

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
            formattedDate = bundle.getString("FormattedDate");
        }

        bundle = getIntent().getExtras();
        if (bundle != null) {
            detailDesc = bundle.getString("Description");
            detailTitle = bundle.getString("Name");
            detailInfo = bundle.getString("Additional Info");
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

        DatabaseReference tabsRef = FirebaseDatabase.getInstance().getReference("Estimates").child(userId).child(formattedDate)
                .child("tabs");

        tabsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> typeIndicator =
                        new GenericTypeIndicator<HashMap<String, Object>>() {
                        };

                if (isMap(dataSnapshot.getValue())) {
                    HashMap<String, Object> hashMap = dataSnapshot.getValue(typeIndicator);
                    Set<String> keys = hashMap.keySet();
                    for (String key : keys) {
                        tabTitles.add(key);
                    }
                    initializeTabs(tabTitles);
                }
                viewPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        tabsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, Object>> typeIndicator =
                        new GenericTypeIndicator<HashMap<String, Object>>() {
                        };

                if (isMap(snapshot.getValue())) {
                    HashMap<String, Object> hashMap = snapshot.getValue(typeIndicator);
                    estimatedTimeTabs = calculateTimeInTabs(hashMap);
                    fragment.setEstimatedTimeTabs(estimatedTimeTabs);
                }
                viewPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Title", detailTitle)
                        .putExtra("Description", detailDesc)
                        .putExtra("Additional Info", detailInfo)
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key)
                        .putExtra("FormattedDate", formattedDate)
                        .putExtra("UserId", userId);
                startActivity(intent);
            }
        });

        FloatingActionMenu fabMenu = findViewById(R.id.fabMenu);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabIndex = tabLayout.getSelectedTabPosition();
                TabLayout.Tab selectedTab = tabLayout.getTabAt(selectedTabIndex);
                String tabTitle = selectedTab.getText().toString();
                if (tabTitle.equals("Main tab")) {
                    fabMenu.setVisibility(View.VISIBLE);
                } else {
                    fabMenu.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private List<EstimatedTimeTab> calculateTimeInTabs(HashMap<String, Object> hashMap) {
        Set<String> keys = hashMap.keySet();
        int sumTimeFrom = 0;
        int sumTimeTo = 0;
        String tabName = "";
        List<EstimatedTimeTab> estimatedTimeTabs = new ArrayList<>();
        Map<String, Object> nestedMap = new HashMap<>();
        for (String key : keys) {
            sumTimeFrom = 0;
            sumTimeTo = 0;
            Object hh = hashMap.get(key);
            nestedMap = convertToMap(hh);
            Set<String> subKeys = nestedMap.keySet();
            for (String subKey : subKeys) {
                Map<String, Object> another = new HashMap<>();
                hh = nestedMap.get(subKey);
                another = convertToMap(hh);

                tabName = another.get("tabName").toString();
                sumTimeFrom = sumTimeFrom + Integer.parseInt(another.get("subtaskFrom").toString());
                sumTimeTo = sumTimeTo + Integer.parseInt(another.get("subtaskTo").toString());
            }
            EstimatedTimeTab estimatedTimeTab = new EstimatedTimeTab(tabName, sumTimeFrom, sumTimeTo);
            estimatedTimeTabs.add(estimatedTimeTab);
        }

        return estimatedTimeTabs;
    }

    public static Map<String, Object> convertToMap(Object object) {
        if (object instanceof Map) {
            return (Map<String, Object>) object;
        }
        return null;
    }

    public static boolean isMap(Object obj) {
        return obj instanceof java.util.Map;
    }

    private void initializeTabs(List<String> tabNames) {
        viewPagerAdapter.notifyDataSetChanged();
        for (String tabName : tabNames) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabName);
            tabLayout.addTab(tab);
            viewPagerAdapter.notifyDataSetChanged();
        }
    }

    private void addNewTab() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_tab, null);
        final EditText tabNameEditText = dialogView.findViewById(R.id.tab_name_edit_text);

        ContextThemeWrapper themedContext = new ContextThemeWrapper(this, R.style.AlertDialogCustom);
        AlertDialog.Builder builder = new AlertDialog.Builder(themedContext);
        builder.setView(dialogView)
                .setTitle("Adding a tab")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tabName = tabNameEditText.getText().toString();
                        if (!tabName.isEmpty()) {
                            tabTitles.add(tabName);
                            viewPagerAdapter.notifyDataSetChanged();
                            viewPager.setCurrentItem(tabTitles.size() - 1);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        int selectedTabIndex = tabLayout.getSelectedTabPosition();
        TabLayout.Tab selectedTab = tabLayout.getTabAt(selectedTabIndex);
        String tabTitle = selectedTab.getText().toString();
        if (tabTitles.size() > 1 && !tabTitle.equals("Main tab")) {
            showDeleteConfirmationDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showDeleteConfirmationDialog() {
        ContextThemeWrapper themedContext = new ContextThemeWrapper(this, R.style.AlertDialogCustom);
        AlertDialog.Builder builder = new AlertDialog.Builder(themedContext);
        builder.setTitle("Deleting a tab");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
        builder.setNegativeButton("No", null);
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
                fragment.setEstimatedTimeTabs(estimatedTimeTabs);
                return fragment;
            } else {
                return TabFragment.newInstance(tabTitles.get(position), formattedDate);
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