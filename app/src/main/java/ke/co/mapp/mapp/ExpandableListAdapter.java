package ke.co.mapp.mapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by phares on 3/8/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }


    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int il) {
        return listHashMap.get(listDataHeader.get(i)).get(il); //i = group item, il = Childitem
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int il) {
        return il;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final String jsonObj = (String) getGroup(i);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group, null);
        }
        TextView tvhname = (TextView) view.findViewById(R.id.name);
        ImageView headerImage = (ImageView) view.findViewById(R.id.image);
        tvhname.setTypeface(null, Typeface.BOLD);

        String hname = null;
        String himage = null;
        try {
            hname = jsonObject.getString("name");
            himage = jsonObject.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tvhname.setText(hname);
        Picasso.with(context).load(himage).into(headerImage);


        return view;
    }

    @Override
    public View getChildView(int i, int il, boolean b, View view, ViewGroup viewGroup) {
        final String jsonObj = (String) getChild(i, il);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        TextView textListName = (TextView) view.findViewById(R.id.lbiListItem);
        TextView textListPrice = (TextView) view.findViewById(R.id.price);
        ImageView textListImage = (ImageView) view.findViewById(R.id.image);
        final Button orderb = (Button) view.findViewById(R.id.order);


        String name = null;
        String price = null;
        String image = null;
        String iid = null;

        try {
            name = jsonObject.getString("name");
            price = jsonObject.getString("price");
            image = jsonObject.getString("image");
            iid = jsonObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textListName.setText(name);
        orderb.setText("ORDER");
        textListPrice.setText("Ksh." + " " + price);
        Picasso.with(context).load(image).into(textListImage);

        final String finalIid = iid;
        final String finalName = name;
        final String finalPrice = price;
        final String finalImage = image;

        final DBHandler db = new DBHandler(view.getContext());
        List<Product> items = db.getAllProducts();
        for (Product item : items) {
            String pid = item.getImage();
            if (pid.equals(finalImage)) {
                orderb.setBackgroundColor(Color.RED);
            } else {
//                orderb.setBackgroundColor(Color.CYAN);
            }
        }

        orderb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Integer c = 0;
                List<Product> items = db.getAllProducts();
                for (Product item : items) {
                    String idr = item.getIdremote();
                    if (finalIid.equals(idr)) {
                        c += 1;
                    }
                }
                if (c==0) {
                    db.addProduct(new Product(0, finalIid, finalName, finalPrice, 1, finalImage));
                    orderb.setBackgroundColor(Color.RED);
                }
            }
        });

            return view;
        }


    @Override
    public boolean isChildSelectable(int i, int il) {
        return true;
    }
}
