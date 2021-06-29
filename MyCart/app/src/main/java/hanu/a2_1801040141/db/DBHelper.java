package hanu.a2_1801040141.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

import hanu.a2_1801040141.MainActivity;
import hanu.a2_1801040141.models.Cart;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 1;

    // Db Table
    private static final String TABLE_NAME = "cartItem";

    private static final String ID = "cart_id";
    private static final String PRODUCT_ID = "p_id";
    private static  final String PRODUCT_NAME = "name";
    private static  final String THUMBNAIL = "thumbnail";
    private static  final String QUANTITY = "quantity";
    private static  final String UNITPRICE = "unitPrice";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRODUCT_ID + " INTEGER, "
                + PRODUCT_NAME + " TEXT, "
                + THUMBNAIL + " TEXT, "
                + UNITPRICE + " INTEGER,"
                + QUANTITY + " INTEGER"
                +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }

    public void addCartItem(Cart cart){

        int quantity;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(isExist(cart) == 0){
            quantity = 1;

            cv.put(PRODUCT_ID, cart.getProductId());
            cv.put(PRODUCT_NAME, cart.getName());
            cv.put(THUMBNAIL, cart.getThumbnail());
            cv.put(UNITPRICE, cart.getUnitPrice());
            cv.put(QUANTITY, quantity);

            long result = db.insert(TABLE_NAME, null, cv);
            if(result == -1){
                Log.d("ERROR", "Failed");
            }else{
                Log.d("PASS", "Added to cart");
            }

        }else{
            quantity = isExist(cart) + 1;
            Log.d("NEW-QUANTITY", quantity + "");

            updateExistCart(String.valueOf(cart.getProductId()), String.valueOf(quantity));
        }

    }

    public ArrayList<Cart> getAllCartItem(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Cart> storeCarts = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                int cart_id = Integer.parseInt(cursor.getString(0));
                int p_id = Integer.parseInt(cursor.getString(1));
                String name = cursor.getString(2);
                String thumbnail = cursor.getString(3);
                int unitPrice = Integer.parseInt(cursor.getString(4));
                int quantity = Integer.parseInt(cursor.getString(5));

                storeCarts.add(new Cart(cart_id, p_id, name, thumbnail, unitPrice, quantity));
            }while (cursor.moveToNext());
        }
        cursor.close();

        return storeCarts;
    }

    public void updateCart(String cart_id, String quantity){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(QUANTITY, quantity);

        long result = db.update(TABLE_NAME, cv, ID + " = ?", new String[]{cart_id});
        if(result == -1){
            Log.d("ERROR", "Update failed");
        }else{
            Log.d("PASS", "Update successfully");
        }
    }

    public void updateExistCart(String p_id, String quantity){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(QUANTITY, quantity);

        long result = db.update(TABLE_NAME, cv, PRODUCT_ID + " = ?", new String[]{p_id});
        if(result == -1){
            Log.d("ERROR", "Update failed");
        }else{
            Log.d("PASS", "Update successfully");
            Log.d("UPDATE-Q", quantity);
        }
    }

    public void deleteCartItem(String cartId){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, ID + " = ?", new String[]{cartId});
        if(result == -1){
            Log.d("ERROR", "Delete failed");
        }else{
            Log.d("PASS", "Delete successfully");
        }

    }

    public int isExist(Cart cart){
        int quantity;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PRODUCT_ID + " = " + cart.getProductId() + " LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return 0;
        }

        cursor.moveToFirst();
        quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
        cursor.close();

        return quantity;
    }

}
