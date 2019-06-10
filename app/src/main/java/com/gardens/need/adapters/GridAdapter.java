package com.gardens.need.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gardens.need.R;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Activity activity;
    private List<ProductList> productList;
    private GridItemsListener gridItemsListener;

    public GridAdapter(Activity activity, List<ProductList> productList, GridItemsListener gridItemsListener) {
        super();
        this.activity = activity;
        this.productList = productList;
        this.gridItemsListener = gridItemsListener;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        GridViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();

        if (convertView == null) {
            view = new GridViewHolder();
            convertView = inflator.inflate(R.layout.layout_dashboard_grid, null);
            view.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
            view.tv_product = (TextView) convertView.findViewById(R.id.tv_product);
            view.cb_product = (CheckBox) convertView.findViewById(R.id.cb_product);
            view.rv_product = (RelativeLayout) convertView.findViewById(R.id.rl_product);
            convertView.setTag(view);
        } else {
            view = (GridViewHolder) convertView.getTag();
        }

        Glide.with(activity).load(productList.get(position).ImagePath).crossFade().into(view.iv_product);
        view.tv_product.setText(productList.get(position).Name);

        final View finalConvertView = convertView;
        view.rv_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridItemsListener.onItemClicked(position, finalConvertView);
            }
        });

        view.cb_product.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gridItemsListener.onCheckChanged(position, productList.get(position), isChecked);
            }
        });

        if (productList.get(position).isChecked) {
            view.cb_product.setChecked(true);
        } else {
            view.cb_product.setChecked(false);
        }


        return convertView;
    }

    private static class GridViewHolder {
        ImageView iv_product;
        TextView tv_product;
        CheckBox cb_product;
        RelativeLayout rv_product;
    }

    /*
    * Abstract class to notify about checkbox checkChangedListener and GridView itemClickLIstener
    * */
    public static abstract class GridItemsListener {
        // Check Changed listener for checkbox
        public abstract void onCheckChanged(int position, ProductList productListData, boolean isChecked);

        // Item click listener for gridview
        public abstract void onItemClicked(int position, View view);
    }
}
