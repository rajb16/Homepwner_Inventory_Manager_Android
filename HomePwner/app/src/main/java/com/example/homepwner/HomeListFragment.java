package com.example.homepwner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mItemsRecyclerView;
    private ItemsAdapter mAdapter;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onItemSelected(Home item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);
        mItemsRecyclerView = (RecyclerView) view
                .findViewById(R.id.items_recycler_view);
        mItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        return view;
    }

    private class ItemsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Home item;
        private TextView nameTV;
        private TextView valueTV;

        public ItemsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_home, parent, false));

            nameTV = (TextView) itemView.findViewById(R.id.item_name);
            valueTV = (TextView) itemView.findViewById(R.id.item_value);
            itemView.setOnClickListener(this);

        }

        public void bind(Home item)
        {
            this.item = item;
            nameTV.setText(item.getName());
            String valueInDollars = "$" + item.getValue();
            valueTV.setText(valueInDollars);
        }
        @Override
        public void onClick(View view) {

            /*Toast.makeText(getActivity(),
                            item.getName() + " clicked!", Toast.LENGTH_SHORT)
                    .show();*/

            mCallbacks.onItemSelected(item);
        }
    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemsHolder> {
        private List<Home> mItem;
        public ItemsAdapter(List<Home> item) {
            mItem = item;
        }

        @Override
        public ItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ItemsHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(ItemsHolder holder, int position) {
            Home item = mItem.get(position);
            holder.bind(item);

        }
        @Override
        public int getItemCount() {
            return mItem.size();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_item_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_item:
                Home items = new Home();
                HomePwnLab.get(getActivity()).addItem(items);
                updateUI();
                mCallbacks.onItemSelected(items);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateUI() {
        HomePwnLab act = HomePwnLab.get(getActivity());
        List<Home> item = act.getmItems();
        if (mAdapter == null) {
            mAdapter = new ItemsAdapter(item);
            mItemsRecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();


    }

    private void updateSubtitle() {
        HomePwnLab homelab = HomePwnLab.get(getActivity());
        int itemscount = homelab.getmItems().size();
        String subtitle = getString(R.string.subtitle_format, itemscount);
        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

}
