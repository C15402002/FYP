package com.example.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.myapplication.model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper{
    private static final String DB_NAME = "OuiDB.db";
    private static final int DB_VER = 1;

    public Database(Context con){
        super(con, DB_NAME,null, DB_VER);
    }

    public List<Order> getOrderBasket(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        String[] select = {"ProductName", "ProductId","Quantity", "Price"};
        String sqlTable = "OrderDetail";

        final List<Order> result = new ArrayList<>();
        sqLiteQueryBuilder.setTables(sqlTable);
        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, select, null, null, null, null, null);

//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT ProductId, ProdName, Quantity, Price FROM OrderDetail", null);
        if(cursor.moveToFirst()){
            do{
                result.add(new Order(cursor.getString(cursor.getColumnIndex("ProductId")), cursor.getString(cursor.getColumnIndex("ProductName")),
                       cursor.getString(cursor.getColumnIndex("Quantity")), cursor.getString(cursor.getColumnIndex("Price"))));
            } while (cursor.moveToNext());
        }
        return result;


    }

    public void addToBasket(Order order){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId, ProductName, Quantity, Price) VALUES('%s', '%s','%s','%s');",
                order.getProductId(), order.getProdName(), order.getQuantity(), order.getPrice());
        sqLiteDatabase.execSQL(query);

    }

    public void deleteFromBasket(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        sqLiteDatabase.execSQL(query);

    }

    public int getAmount() {
        int count = 0;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail");
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                count = cursor.getInt(0);

            }while (cursor.moveToNext());
        }
        return count;
    }
}
