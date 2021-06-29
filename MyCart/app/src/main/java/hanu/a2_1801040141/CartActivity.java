package hanu.a2_1801040141;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hanu.a2_1801040141.adapter.CartAdapter;
import hanu.a2_1801040141.db.DBHelper;
import hanu.a2_1801040141.models.Cart;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCart;
    private CartAdapter cartAdapter;

    DBHelper dbHelper;

    public TextView totalPrice;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Back to home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Recyclerview Cart
        rvCart = findViewById(R.id.rvCart);
        totalPrice = findViewById(R.id.totalPrice);

        dbHelper = new DBHelper(CartActivity.this);

        ArrayList<Cart> allCartItems = dbHelper.getAllCartItem();

        cartAdapter = new CartAdapter(CartActivity.this, allCartItems);
        rvCart.setAdapter(cartAdapter);
        rvCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));

        // Calculate Total Price
        total = 0;
        for(int i = 0; i < allCartItems.size(); i++){
            total += (allCartItems.get(i).getQuantity() * allCartItems.get(i).getUnitPrice());
        }

        totalPrice.setText("₫ " + total);

    }

}