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

    public List<Order> getOrderBasket(String userPhone){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        String[] select = {"UserPhone", "ProductId", "RestID","ProductName","Quantity", "Price"};
        String sqlTable = "OrderDetail";

        final List<Order> result = new ArrayList<>();
        sqLiteQueryBuilder.setTables(sqlTable);
        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, select, "UserPhone = ?", new String[]{userPhone}, null, null, null);

//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT ProductId, ProdName, Quantity, Price FROM OrderDetail", null);
        if(cursor.moveToFirst()){
            do{
                result.add(new Order(cursor.getString(cursor.getColumnIndex("UserPhone")),cursor.getString(cursor.getColumnIndex("ProductId")),cursor.getString(cursor.getColumnIndex("RestID")),
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                       cursor.getString(cursor.getColumnIndex("Quantity")), cursor.getString(cursor.getColumnIndex("Price"))));
            } while (cursor.moveToNext());
        }
        return result;


    }

    public void addToBasket(Order order){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("INSERT OR REPLACE INTO OrderDetail(UserPhone, ProductId, RestID, ProductName, Quantity, Price) VALUES('%s', '%s', '%s','%s','%s');",
                order.getUserPhone(), order.getProductId(), order.getRestID(), order.getProductName(), order.getQuantity(), order.getPrice());
        sqLiteDatabase.execSQL(query);

    }

    public void deleteFromBasket(String userPhone){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone='%s'", userPhone);
        sqLiteDatabase.execSQL(query);

    }

    public int getAmount(String userPhone) {
        int count = 0;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail WHERE UserPhone = '%s'", userPhone);
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                count = cursor.getInt(0);

            }while (cursor.moveToNext());
        }
        return count;
    }

    public void editBasket(Order order){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sqlQuery = String.format("UPDATE OrderDetail SET Quantity = '%s' WHERE UserPhone = '%s' AND ProductId = '%s'", order.getQuantity(), order.getUserPhone(), order.getProductId());
        sqLiteDatabase.execSQL(sqlQuery);
    }




}
