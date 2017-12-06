package com.search.tenaz.searchsnapdeal;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private SearchView searchBox;
    private RecyclerView recyclerView;
    private ProgressBar mainProgressBar;
    private ImageAdapter imageAdapter;
    private TextView noListText;
    private String searchQuery;
    private ArrayList<Model> products;
    private boolean loadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        searchBox = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        mainProgressBar = findViewById(R.id.progressBar3);
        noListText = findViewById(R.id.no_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //create asynctask
                GetSearchResults getSearchResults = new GetSearchResults();
                searchQuery = query;
                String url = HtmlParserJSoup.getFirstUrl(searchQuery);
                getSearchResults.execute(url);
                //update UI
                imageAdapter = null;
                refreshList(products);
                noListText.setVisibility(View.INVISIBLE);
                mainProgressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void refreshList(ArrayList<Model> strings) {
        if (null != strings && strings.size() > 0) {
            if (null == imageAdapter) {
                imageAdapter = new ImageAdapter(strings, MainActivity.this);
                recyclerView.setAdapter(imageAdapter);

            } else {
                imageAdapter.setList(strings);
                imageAdapter.notifyDataSetChanged();
            }
            recyclerView.setVisibility(View.VISIBLE);

        } else if (loadMore) {
        } else {
            noListText.setVisibility(View.VISIBLE);
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoadMoreEvent event) {
        loadMore = true;
        products.remove(products.size() - 1);
        String url = HtmlParserJSoup.getUrlForLoadMore(searchQuery, Integer.toString(products.size()));
        refreshList(new ArrayList<Model>());
        GetSearchResults getSearchResults = new GetSearchResults();
        getSearchResults.execute(url);
    }

    private class GetSearchResults extends AsyncTask<String, Void, ArrayList<Model>> {

        @Override
        protected ArrayList<Model> doInBackground(String... strings) {
            if (loadMore) {
                products = HtmlParserJSoup.getDetailsForLoadMore(strings[0]);
                loadMore = false;
            } else {
                products = HtmlParserJSoup.getDetails(strings[0]);
            }
            Log.i("poducts", "" + products);

            return products;
        }

        @Override
        protected void onPostExecute(ArrayList<Model> strings) {
            super.onPostExecute(strings);
            mainProgressBar.setVisibility(View.INVISIBLE);
            if (strings.size() > 0) {
                Model footer = new Model();
                footer.setFooter(true);
                strings.add(footer);
            }
            refreshList(strings);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
