package hanu.a2_1801040141.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import hanu.a2_1801040141.CartActivity;
import hanu.a2_1801040141.R;
import hanu.a2_1801040141.db.DBHelper;
import hanu.a2_1801040141.models.Cart;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<Cart> listCartItems;

    private int pQuantity, pSumPrice, itemSumPrice, sp;
    private String cart_id;

    public CartAdapter(Context context, ArrayList<Cart> listCartItems){
        this.context = context;
        this.listCartItems = listCartItems;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.cart_item, parent, false);

        return new CartAdapter.CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        final Cart cart = listCartItems.get(position);

        itemSumPrice = cart.getQuantity() * cart.getUnitPrice();

        holder.pName.setText(cart.getName());
        holder.quantity.setText(String.valueOf(cart.getQuantity()));
        holder.pPrice.setText("₫ " + cart.getUnitPrice());
        holder.sumPrice.setText(String.valueOf(itemSumPrice));

        // Load image
        String imgUrl = String.valueOf(cart.getThumbnail());
        CartAdapter.ImageLoader imageLoader = new ImageLoader(holder);
        imageLoader.execute(imgUrl);

//      Picasso.with(this.context).load(imgUrl).into(holder.pThumbnail);

    }

    @Override
    public int getItemCount() {
        return listCartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView minusBtn, plusBtn, pThumbnail;
        private TextView pName, pPrice, quantity, sumPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            minusBtn = itemView.findViewById(R.id.minus);
            plusBtn = itemView.findViewById(R.id.plus);

            pThumbnail = itemView.findViewById(R.id.pThumbnail);
            pName = itemView.findViewById(R.id.pName);
            pPrice = itemView.findViewById(R.id.pPrice);
            quantity = itemView.findViewById(R.id.quantity);
            sumPrice = itemView.findViewById(R.id.sumPrice);

            // Plus - Minus button click
            plusBtn.setOnClickListener(this);
            minusBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            cart_id = String.valueOf(listCartItems.get(getAdapterPosition()).getCartId());

            DBHelper db = new DBHelper(context);

            pSumPrice = listCartItems.get(getAdapterPosition()).getUnitPrice();
            pQuantity = Integer.parseInt(quantity.getText().toString());

            if(v.getId() == plusBtn.getId()){
                pQuantity++;

            }else if(v.getId() == minusBtn.getId()){
                pQuantity--;

                if(pQuantity == 0){

                    listCartItems.remove(cart_id);
                    notifyItemRemoved(Integer.parseInt(cart_id));

                    db.deleteCartItem(cart_id);
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();

                    Intent intent = ((CartActivity)context).getIntent();
                    ((CartActivity)context).finish();
                    ((CartActivity)context).startActivity(intent);
                }
            }

            // Set quantity
            quantity.setText(String.valueOf(pQuantity));

            // Set sum price
            sp = pSumPrice * pQuantity;
            sumPrice.setText(String.valueOf(sp));

            // Update cart quantity
            db.updateCart(cart_id, String.valueOf(pQuantity));

            Intent intent = ((CartActivity)context).getIntent();
            ((CartActivity)context).finish();
            ((CartActivity)context).startActivity(intent);

//            Toast.makeText(context, "Total Price updated", Toast.LENGTH_SHORT).show();

        }
    }


    public static class ImageLoader extends AsyncTask<String, Void, Bitmap> {

        private CartAdapter.CartViewHolder holder;

        public ImageLoader(CartAdapter.CartViewHolder holder){
            this.holder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            holder.pThumbnail.setImageBitmap(bitmap);
        }
    }
}
