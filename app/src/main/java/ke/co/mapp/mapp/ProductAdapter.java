package ke.co.mapp.mapp;

/**
 * Created by phares on 7/27/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    ProductAdapter mAdapter;
    private List<Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public  TextView name, price, display;
        public Button remove;
        public ImageButton increment, decrement;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.lbiListItem);
            price = (TextView) view.findViewById(R.id.price);
            remove = (Button) view.findViewById(R.id.order);
            display = (TextView) view.findViewById(R.id.display);
            increment = (ImageButton) view.findViewById(R.id.increment);
            decrement = (ImageButton) view.findViewById(R.id.decrement);

        }
    }


    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,    int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Product product = productList.get(position);
        Picasso.with(context).load(product.getImage()).into(holder.image);
        holder.name.setText(product.getName());
        String total_price = String.valueOf(Integer.valueOf(product.getQuantity()) * Integer.valueOf(product.getPrice()));
        holder.price.setText(total_price);
        holder.display.setText(String.valueOf(product.getQuantity()));
        final Integer prod = product.getId();
        holder.remove.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                final DBHandler db = new DBHandler(view.getContext());
                List<Product> products = db.getAllProducts();
                for (Product p : products) {
                    Integer pid = p.getId();
                    if (pid.equals(prod)) {
                        db.deleteProduct(p);
                    }
                }
                holder.remove.setBackgroundColor(Color.DKGRAY);
                holder.remove.setText("Removed");
                holder.remove.setClickable(false);
                Toast.makeText(view.getContext(),
                        product.getName() + " Removed", Toast.LENGTH_SHORT).show();
            }
        });

        holder.increment.setOnClickListener (new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 final DBHandler db = new DBHandler(v.getContext());
                 List<Product> products = db.getAllProducts();
                 for (Product p : products) {
                     Integer pid = p.getId();
                     if (pid.equals(prod)) {
                         Integer qplus = (p.getQuantity()+1);
                         db.updateProduct(new Product(p.getId(), p.getIdremote(), p.getName(), p.getPrice(), qplus , p.getImage()));
                         holder.display.setText(String.valueOf(qplus));
                         holder.price.setText(String.valueOf(qplus * Integer.valueOf(p.getPrice())));
                         Toast.makeText(v.getContext(),
                                 product.getName() + " Increment", Toast.LENGTH_SHORT).show();
                     }
                 }
             }
         });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DBHandler db = new DBHandler(v.getContext());
                List<Product> products = db.getAllProducts();
                for (Product p : products) {
                    Integer pid = p.getId();
                    if (pid.equals(prod)) {
                        Integer qminus = (p.getQuantity()-1);
                        if (qminus > 0) {
                            db.updateProduct(new Product(p.getId(), p.getIdremote(), p.getName(), p.getPrice(), qminus, p.getImage()));
                            holder.display.setText(String.valueOf(qminus));
                            holder.price.setText(String.valueOf(qminus * Integer.valueOf(p.getPrice())));
                            Toast.makeText(v.getContext(),
                                    product.getName() + " Decrement", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(v.getContext(),
                                    product.getName() + " Min reached", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
