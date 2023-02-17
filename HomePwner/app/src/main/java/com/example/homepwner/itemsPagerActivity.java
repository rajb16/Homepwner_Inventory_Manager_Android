package com.example.homepwner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class itemsPagerActivity extends AppCompatActivity implements HomeFragment.Callbacks{
    private static final String EXTRA_ITEM_ID = "com.example.homepwner.item_id";

    private ViewPager mViewPager;
    private List<Home> mItem;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_pager);

        UUID itemID = (UUID) getIntent().getSerializableExtra(EXTRA_ITEM_ID);

        mViewPager = (ViewPager) findViewById(R.id.item_view_pager);
        mItem = HomePwnLab.get(this).getmItems();
        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Home item = mItem.get(position);
                return HomeFragment.newInstance(item.getId());
            }
            @Override
            public int getCount() {
                return mItem.size();
            }
        });

        for (int i = 0; i < mItem.size(); i++) {
            if (mItem.get(i).getId().equals(itemID)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
    public static Intent newIntent(Context packageContext, UUID itemID) {
        Intent intent = new Intent(packageContext, itemsPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemID);
        return intent;
    }
    @Override
    public void onItemUpdated(Home item) {
    }
}
