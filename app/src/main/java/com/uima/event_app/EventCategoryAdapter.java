package com.uima.event_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sidneyjackson on 4/18/17.
 */

public class EventCategoryAdapter extends ArrayAdapter<String> {
    int res;

    public EventCategoryAdapter(Context ctx, int res, List<String> items)  {
        super(ctx, res, items);
        this.res = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout categoryView;
        String category = getItem(position);

        if (convertView == null) {
            categoryView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(res, categoryView, true);
        } else {
            categoryView = (LinearLayout) convertView;
        }

        TextView category_name = (TextView) categoryView.findViewById(R.id.category_name);
        TextView category_desc = (TextView) categoryView.findViewById(R.id.category_description);
        ImageView categoryTypeImage = (ImageView) categoryView.findViewById(R.id.category_image);


        if (category.equals("Local Culture")) {
            categoryTypeImage.setImageResource(R.drawable.local_culture);
            category_desc.setText(R.string.local_culture_desc);
        } else if (category.equals("Social Activism")) {
            categoryTypeImage.setImageResource(R.drawable.activism);
            category_desc.setText(R.string.social_activism_desc);
        } else if (category.equals("Popular Culture")) {
            categoryTypeImage.setImageResource(R.drawable.pop_culture);
            category_desc.setText(R.string.popular_culture_desc);
        } else if (category.equals("Community Outreach")) {
            categoryTypeImage.setImageResource(R.drawable.charity);
            category_desc.setText(R.string.community_outreach_desc);
        } else if (category.equals("Education & Learning")) {
            categoryTypeImage.setImageResource(R.drawable.education);
            category_desc.setText(R.string.education_and_learning_desc);
        } else if (category.equals("Shopping & Market Events")) {
            categoryTypeImage.setImageResource(R.drawable.shopping);
            category_desc.setText(R.string.shopping_and_markets_desc);
        } else if (category.equals("Miscellaneous")) {
            categoryTypeImage.setImageResource(R.drawable.miscellaneous);
            category_desc.setText(R.string.miscellaneous_desc);
        }

        category_name.setText(category);
        return categoryView;
    }
}
