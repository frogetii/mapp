package ke.co.mapp.mapp;

/**
 * Created by phares on 7/26/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "shoppingInfo";
    // Contacts table name
    private static final String TABLE_PRODUCTS = "products";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ID_REMOTE = "id_remote";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_IMAGE = "image";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ID_REMOTE + " INTEGER," + KEY_NAME +  " TEXT," + KEY_PRICE +  " TEXT," + KEY_QUANTITY +  " INTEGER," + KEY_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
// Creating tables again
        onCreate(db);
    }
    // Adding new product
    public void addProduct(Product product) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_REMOTE, product.getIdremote());
        values.put(KEY_NAME, product.getName());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_QUANTITY, product.getQuantity());
        values.put(KEY_IMAGE, product.getImage());

// Inserting Row
        db.insert(TABLE_PRODUCTS, null, values);
        db.close(); // Closing database connection
    }
    // Getting one product
    public Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{KEY_ID,
                KEY_ID_REMOTE, KEY_NAME, KEY_PRICE, KEY_QUANTITY, KEY_IMAGE}, KEY_ID + "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Product product = new Product(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                cursor.getString(3), Integer.parseInt(cursor.getString(4)),cursor.getString(5));

// return shop
        return product;
    }
    // Getting All Shops
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<Product>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(Integer.parseInt(cursor.getString(0)));
                product.setIdremote(cursor.getString(1));
                product.setName(cursor.getString(2));
                product.setPrice(cursor.getString(3));
                product.setQuantity(cursor.getInt(4));
                product.setImage(cursor.getString(5));
// Adding contact to list
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();

// return contact listshop
        return productList;
    }
    // Getting shops Count
    public int getProductsCount() {
        String countQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

// return count
        return cursor.getCount();
    }
    // Updating a shop
    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_ID_REMOTE, product.getIdremote());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_QUANTITY, product.getQuantity());
        values.put(KEY_IMAGE, product.getImage());

// updating row
        return db.update(TABLE_PRODUCTS, values, KEY_ID + " = ?",
        new String[]{String.valueOf(product.getId())});
    }

    // Deleting a product
    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_ID + " = ?",
        new String[] { String.valueOf(product.getId()) });
        db.close();
    }
}
