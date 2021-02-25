package com.hhsfbla.hhs_fbla_mad_2021.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hhsfbla.hhs_fbla_mad_2021.R;
import com.hhsfbla.hhs_fbla_mad_2021.classes.Business;
import com.hhsfbla.hhs_fbla_mad_2021.classes.User;
import com.hhsfbla.hhs_fbla_mad_2021.recyclerviews.notifs.NotificationsRVAdapter;
import com.hhsfbla.hhs_fbla_mad_2021.recyclerviews.notifs.NotificationsRVModel;
import com.hhsfbla.hhs_fbla_mad_2021.recyclerviews.search.SearchRVAdapter;
import com.hhsfbla.hhs_fbla_mad_2021.recyclerviews.search.SearchRVModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    private RecyclerView searchResults;
    private Button backButton;
    private SearchRVAdapter searchRVAdapter;
    private SearchView searchView;
    private ArrayList<SearchRVModel> searches;
    private List<User> userList;
    private List<Business> businessList;

    /**
     * Creates the page and initializes all page components, such as textviews, image views, buttons, and dialogs,
     *
     * @param savedInstanceState the save state of the activity or page
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = FirebaseFirestore.getInstance();

        //Setting up the searches recycler view. Repeats search after search in rows.
        searchResults = findViewById(R.id.search_results);
        searches = new ArrayList<>();
        userList = new ArrayList<>();
        businessList = new ArrayList<>();
        searchResults.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        searchRVAdapter = new SearchRVAdapter(searches);
        searchResults.setAdapter(searchRVAdapter);

        // adding users
        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                final User u = snapshot.toObject(User.class);
                userList.add(u);
                searches.add(new SearchRVModel(u, snapshot.getId()));
                searchRVAdapter.setSearches(searches);
                searchRVAdapter.notifyDataSetChanged();
            }
        });

        // adding businesses
        db.collection("businesses").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                final Business b = snapshot.toObject(Business.class);
                businessList.add(b);
                searches.add(new SearchRVModel(b, snapshot.getId()));
                searchRVAdapter.setSearches(searches);
                searchRVAdapter.notifyDataSetChanged();
            }
        });

//        searches.add(new SearchRVModel(new User("Joe mama", "joemama@gmail.com")));
//        searches.add(new SearchRVModel(new Business("Joe mama", "joemama@gmail.com")));
//        searches.add(new SearchRVModel(new User("Ayush S", "joemama@gmail.com")));
//        searches.add(new SearchRVModel(new Business("Joe mama", "joemama@gmail.com")));
//        searches.add(new SearchRVModel(new User("Joe mama", "joemama@gmail.com")));
//        searches.add(new SearchRVModel(new Business("Joe mama", "joemama@gmail.com")));
//        searches.add(new SearchRVModel(new User("Joe mama", "joemama@gmail.com")));
//        searches.add(new SearchRVModel(new Business("Joe mama", "joemama@gmail.com")));
//        searches.add(new SearchRVModel(new User("Joe mama", "joemama@gmail.com")));
//        searches.add(new SearchRVModel(new Business("Joe mama", "joemama@gmail.com")));
//        searches.add(new SearchRVModel(new User("Joe mama", "joemama@gmail.com")));
//        searches.add(new SearchRVModel(new Business("Joe mama", "joemama@gmail.com")));

        //Back button functionality, go back to home page.
        backButton = findViewById(R.id.search_back);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, HomeActivity.class));
        });

        //Search Filtering
        searchView = findViewById(R.id.search_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchRVAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchRVAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
}
