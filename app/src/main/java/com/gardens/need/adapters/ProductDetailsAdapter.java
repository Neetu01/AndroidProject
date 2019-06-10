package com.gardens.need.adapters;

import android.app.Activity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gardens.need.R;
import com.gardens.need.activites.Homescreen;

import java.util.List;

/**
 * Created by Manish on 3/14/2017.
 */

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolder> {

    private List<ProductList> productList;
    private Homescreen home;

    public ProductDetailsAdapter(Activity activity, List<ProductList> productList) {
        this.productList = productList;
        home = (Homescreen) activity;
    }

    @Override
    public ProductDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_details, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProductDetailsAdapter.ViewHolder holder, int position) {
        ProductList data = productList.get(position);

        holder.tv_productTitle.setText(data.Name);

        if (!data.Description.isEmpty())
            holder.tv_productDesc.setText(data.Description);

        Glide.with(holder.iv_productDetial.getContext()).load(data.ImagePath).into(holder.iv_productDetial);

        if(data.isChecked)
            holder.cb_productDetail.setChecked(true);
        else
            holder.cb_productDetail.setChecked(false);

        holder.cb_productDetail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = holder.getAdapterPosition();
                if (isChecked && !home.selectedProductsId.contains(productList.get(pos).CategoryId)) {
                    productList.get(pos).isChecked = true;
                    home.selectedProducts.add(productList.get(pos));
                    home.selectedProductsId.add(productList.get(pos).CategoryId);
                }
                else {
                    productList.get(pos).isChecked = false;
                    home.selectedProducts.remove(productList.get(pos));
                    home.selectedProductsId.remove((Integer) productList.get(pos).CategoryId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_productTitle, tv_productDesc;
        ImageView iv_productDetial;
        AppCompatCheckBox cb_productDetail;

        ViewHolder(View itemView) {
            super(itemView);

            tv_productTitle = (TextView) itemView.findViewById(R.id.tv_productTitle);
            tv_productDesc = (TextView) itemView.findViewById(R.id.tv_productDesc);
            iv_productDetial = (ImageView) itemView.findViewById(R.id.iv_productDetail);
            cb_productDetail = (AppCompatCheckBox) itemView.findViewById(R.id.cb_productDetail);
        }
    }

}
