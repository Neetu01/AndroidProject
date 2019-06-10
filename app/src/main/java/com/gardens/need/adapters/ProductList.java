package com.gardens.need.adapters;

/**
 * Created by Manish on 3/13/2017.
 */

public class ProductList {

    public int CategoryId, ParentId;
    public String Name, Description, ImagePath;
    public boolean isChecked;

    public ProductList(int categoryId, int parentId, String name, String description, String imagePath, boolean isChecked) {
        CategoryId = categoryId;
        ParentId = parentId;
        Name = name;
        Description = description;
        ImagePath = imagePath;
        this.isChecked = isChecked;
    }
}
