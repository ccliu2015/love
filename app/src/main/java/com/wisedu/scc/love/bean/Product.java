package com.wisedu.scc.love.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable使得一个对象可以被写入Parcel,
 * Parcel是一个消息容器，可以通过IBinder传递
 */
public class Product implements Parcelable {

    //商品名称
    private String name;
    //规格型号
    private String specification;
    //描述
    private String description;
    //商品图片
    private Bitmap image;
    //商标
    private String brand;
    //工厂
    private String factory;

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    /*
	 * 实现Parcelable接口的方法
	 * 1.getCreator()
	 * 2.setCreator()
	 * 3.Parcelable.Creator() 构造方法
	 * 4.describeContents()
	 * 5.writeToParcel()
	 */
    public static Creator<Product> getCreator() {
        return CREATOR;
    }

    public static void setCreator(Creator<Product> creator) {
        CREATOR = creator;
    }

    public static Creator<Product> CREATOR = new Creator<Product>() {
        public Product createFromParcel(Parcel source) {
            Product product = new Product();
            product.name = source.readString();
            product.description = source.readString();
            product.factory = source.readString();
            product.brand = source.readString();
            product.specification = source.readString();
            product.image =  source.readParcelable(Bitmap.class.getClassLoader());
            return product;
        }
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(image, flags);
        dest.writeString(specification);
        dest.writeString(description);
        dest.writeString(brand);
        dest.writeString(factory);
    }
}
