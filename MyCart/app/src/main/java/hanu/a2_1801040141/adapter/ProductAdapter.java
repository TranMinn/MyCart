package hanu.a2_1801040141.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hanu.a2_1801040141.MainActivity;
import hanu.a2_1801040141.R;
import hanu.a2_1801040141.db.DBHelper;
import hanu.a2_1801040141.models.Cart;
import hanu.a2_1801040141.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private static List<Product> products;
    private static List<Product> filteredProducts;

    private Context context;

    public ProductAdapter(List<Product> products, Context context){
        this.products = products;
        this.filteredProducts = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.shop_item, parent, false);

        return new ProductViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText("₫ " + product.getUnitPrice());

        // Load image
        ImageLoader imageLoader = new ImageLoader(holder);
        String imgUrl = product.getThumbnail();
        imageLoader.execute(imgUrl);

        // Add to cart button
        holder.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,product.getName(), Toast.LENGTH_SHORT).show();

                Cart cart = new Cart(product.getId(), product.getName(), product.getThumbnail(), product.getUnitPrice(), 1);
                DBHelper dbHelper = new DBHelper(context);
                dbHelper.addCartItem(cart);

                Toast.makeText(context,"Added to cart", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        private TextView productName, productPrice;
        private ImageView productImage, cartBtn;
        private Context context;

        private ProductViewHolder holder;

        public ProductViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            this.context = context;

            productName = itemView.findViewById(R.id.itemName);
            productPrice = itemView.findViewById(R.id.itemPrice);
            productImage = itemView.findViewById(R.id.itemImage);

            cartBtn = itemView.findViewById(R.id.cartBtn);

        }
    }

    public static class ImageLoader extends AsyncTask<String, Void, Bitmap> {

        private ProductViewHolder holder;

        public ImageLoader(ProductViewHolder holder){
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

            holder.productImage.setImageBitmap(bitmap);
        }
    }

    // Filter
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String key = charSequence.toString();
                if(key.isEmpty()){
                    filteredProducts = products;
                }else{
                    List<Product> filteredList = new ArrayList<>();
                    for(Product p : products){
                        if(p.getName().toLowerCase().contains(key.toLowerCase())){
                            filteredList.add(p);
                        }
                    }

                    filteredProducts = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredProducts;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                filteredProducts = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
