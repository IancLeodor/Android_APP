package com.example.gustoheaven;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<Recipe> recipes;
    private ArrayList<Recipe> searchRecipe;
    private JSONArray arr;
    private TextView emptyView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchView searchView;
    private NestedScrollView nestedSV;
    public static int TIMEOUT_MS = 10000;
    int nr, limit;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View RootView = inflater.inflate(R.layout.fragment_home, container, false);
        recipes = new ArrayList<>();
        nr = 0;
        limit = 21;
        progressBar = RootView.findViewById(R.id.progressBar3);
        emptyView = RootView.findViewById(R.id.empty_view);
        nestedSV = RootView.findViewById(R.id.idNestedSV);
        searchView = RootView.findViewById(R.id.searchView);
        recyclerView = RootView.findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        getRandomRecipes(nr, limit);

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    nr = nr + 20;
                    if (searchView.getQuery().toString().isEmpty()) {
                        getRandomRecipes(nr, limit);
                    }
                }
            }
        });

        searchView.clearFocus();
        ImageView clearButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        clearButton.setOnClickListener(v -> {
            searchView.setQuery("", false);
            recyclerView.setVisibility(View.GONE);
            getRandomRecipes(0, 21);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                searchRecipe(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return RootView;
    }

    private void getRandomRecipes(int nr, int limit) {
        progressBar.setVisibility(View.VISIBLE);
        if (nr > limit) {
            Toast.makeText(getActivity(), "That's all the data..", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        String URL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=a";

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            arr = response.getJSONArray("drinks");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject jsonObject = arr.getJSONObject(i);
                                recipes.add(new Recipe(
                                        jsonObject.optString("idDrink"),
                                        jsonObject.optString("strDrink"),
                                        jsonObject.optString("strDrinkThumb"),
                                        jsonObject.optString("strCategory"),
                                        jsonObject.optString("strGlass")
                                ));
                            }
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                            RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(getContext(), recipes);
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", error.toString());
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }

    private void searchRecipe(String search) {
        searchRecipe = new ArrayList<>();
        String URL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + search;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            arr = response.getJSONArray("drinks");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject jsonObject = arr.getJSONObject(i);
                                searchRecipe.add(new Recipe(
                                        jsonObject.optString("idDrink"),
                                        jsonObject.optString("strDrink"),
                                        jsonObject.optString("strDrinkThumb"),
                                        jsonObject.optString("strCategory"),
                                        jsonObject.optString("strGlass")
                                ));
                            }
                            progressBar.setVisibility(View.GONE);
                            if (searchRecipe.isEmpty()) {
                                recyclerView.setAlpha(0);
                                progressBar.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                emptyView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(getContext(), searchRecipe);
                                recyclerView.setAdapter(myAdapter);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", error.toString());
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }
}