package com.example.homepwner;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public class HomeListActivity extends SingleFragmentActivity implements HomeListFragment.Callbacks, HomeFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new HomeListFragment();
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }
    @Override
    public void onItemSelected(Home item) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = itemsPagerActivity.newIntent(this, item.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = HomeFragment.newInstance(item.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }


    }

    public void onItemUpdated(Home item) {
        HomeListFragment listFragment = (HomeListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}