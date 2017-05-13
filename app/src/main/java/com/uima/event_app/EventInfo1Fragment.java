package com.uima.event_app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.uima.event_app.R.color.white;

public class EventInfo1Fragment extends Fragment {
    View rootView;
    private String eventID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //Gallery gallery;

    private ViewPager iconViewPager;
    CustomSwipeAdapter adapter;

    TextView date;
    TextView time;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        eventID = getArguments().getString("eventID");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_info1, container, false);
        date = (TextView) rootView.findViewById(R.id.bottom_event_board_event_date);
        time = (TextView) rootView.findViewById(R.id.bottom_event_board_event_time);

        iconViewPager = (ViewPager) rootView.findViewById(R.id.iconViewPager);
        adapter = new CustomSwipeAdapter(getActivity());
        iconViewPager.setAdapter(adapter);

        //Handles Gallery View
        //gallery = (Gallery) rootView.findViewById(R.id.icon_view);
        //gallery.setBackgroundColor(R.color.backgroundColor);
        //final GalleryImageAdapter galleryImageAdapter= new GalleryImageAdapter(getActivity());
        //gallery.setAdapter(galleryImageAdapter);

        //gallery.setAdapter(new GalleryImageAdapter(getContext()));
        /*gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,long id)
            {
                TextView iconText = (TextView) rootView.findViewById(R.id.icon_name);
                //iconText.setText();
            }
        });*/

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        populateData();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateData();
    }

    public EventInfo1Fragment() {
        // Required empty public constructor
    }

    public static EventInfo1Fragment newInstance() {
        EventInfo1Fragment f = new EventInfo1Fragment();
        return f;
    }

    private void populateData() {
        myRef.child("events").child(eventID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event thisEvent = dataSnapshot.getValue(Event.class);
                date.setText("Date: " + thisEvent.getDate());
                time.setText("Time: " + thisEvent.getStart_time() + " - " + thisEvent.getEnd_time());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /* Swipe Stuff Here! */
    public class CustomSwipeAdapter extends PagerAdapter {
        //Need to figure out how to store and populate image view in one swoop.
        private Context ctx;
        private int iconCount;
        private LayoutInflater layoutInflater;
        public HashMap<String, Integer> mImageIds = new HashMap<>();
        public String[] icon_titles = populateAttributeTitles();
        public Integer[] icon_images = populateAttributeIcons();

        public CustomSwipeAdapter(Context ctx) {
            this.ctx = ctx;
            //this.iconCount = iconCount;
        }

        @Override
        public int getCount() {
            return this.icon_images.length;
        }

        public void populateIcons() {

        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View item_view = layoutInflater.inflate(R.layout.swipe_layout, container, false);
            ImageView imageView = (ImageView) item_view.findViewById(R.id.icon_image);
            TextView textView = (TextView) item_view.findViewById(R.id.icon_name);
            imageView.setImageResource(icon_images[position]);
            textView.setText(icon_titles[position]);
            container.addView(item_view);
            return item_view;
        }

        public void populateAttributes() {
            mImageIds.put("Free Admission", R.drawable.list_admission_icon);
            mImageIds.put("Alcohol Available", R.drawable.list_alcohol_icon);
            mImageIds.put("For a Good Cause", R.drawable.list_cause_icon);
            mImageIds.put("Live Entertainment", R.drawable.list_entertainment_icon);
            mImageIds.put("Food Available", R.drawable.list_food_icon);
            mImageIds.put("Free Admission", R.drawable.list_free_icon);
            mImageIds.put("Free Item Giveaways", R.drawable.list_giveaway_icon);
            mImageIds.put("Indoor Activities", R.drawable.list_indoor_icon);
            mImageIds.put("Key Note Speaker", R.drawable.list_keynote_icon);
            mImageIds.put("Family Friendly", R.drawable.list_kid_icon);
            mImageIds.put("Music Available", R.drawable.list_music_icon);
            mImageIds.put("Networking Opportunities", R.drawable.list_network_icon);
            mImageIds.put("Outdoor Activities", R.drawable.list_outside_icon);
            mImageIds.put("Parking", R.drawable.list_parking_icon);
            mImageIds.put("Pet Friendly", R.drawable.list_pet_icon);
            mImageIds.put("Seating Available", R.drawable.list_seating_icon);
            mImageIds.put("Public Transit", R.drawable.list_transit_icon);
            mImageIds.put("Local Vendors Attending", R.drawable.list_vendor_icon);
            mImageIds.put("Volunteers Needed", R.drawable.list_volunteers_icon);
        }

        public String[] populateAttributeTitles(){
            String[] array= {
                    "Free Admission",
                    "Alcohol Available",
                    "For a Good Cause",
                    "Live Entertainment",
                    "Food Available",
                    "Free Admission",
                    "Free Item Giveaways",
                    "Indoor Activities",
                    "Key Note Speaker",
                    "Family Friendly",
                    "Music Available",
                    "Networking Opportunities",
                    "Outdoor Activities",
                    "Parking",
                    "Pet Friendly",
                    "Seating Available",
                    "Public Transit",
                    "Local Vendors Attending",
                    "Volunteers Needed"
            };
            return array;
        }

        public Integer[] populateAttributeIcons() {
            Integer[] array= {
                    R.drawable.list_admission_icon,
                    R.drawable.list_alcohol_icon,
                    R.drawable.list_cause_icon,
                    R.drawable.list_entertainment_icon,
                    R.drawable.list_food_icon,
                    R.drawable.list_free_icon,
                    R.drawable.list_giveaway_icon,
                    R.drawable.list_indoor_icon,
                    R.drawable.list_keynote_icon,
                    R.drawable.list_kid_icon,
                    R.drawable.list_music_icon,
                    R.drawable.list_network_icon,
                    R.drawable.list_outside_icon,
                    R.drawable.list_parking_icon,
                    R.drawable.list_pet_icon,
                    R.drawable.list_seating_icon,
                    R.drawable.list_transit_icon,
                    R.drawable.list_vendor_icon,
                    R.drawable.list_volunteers_icon
            };
            return array;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return (view == (LinearLayout)o);
        }

    }


    /* Image Gallery Here!*/
    public class GalleryImageAdapter extends BaseAdapter
    {
        private Context mContext;
        public HashMap<String, Integer> mImageIds = new HashMap<>();
        public ArrayList<Integer> attrImageIds = new ArrayList<>();

        public GalleryImageAdapter(Context context)
        {
            mContext = context;
        }

        public int getCount() {
            return mImageIds.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        // Override this method according to your need
        public View getView(int index, View view, ViewGroup viewGroup)
        {
            // TODO Auto-generated method stub

            System.out.println("I got here!");
            ImageView i = new ImageView(mContext);
            populateAttributes();
            populateGalleryView(i, index);
            //i.setImageResource(mImageIds[index]);
            i.setLayoutParams(new Gallery.LayoutParams(40, 40));
            i.setScaleType(ImageView.ScaleType.FIT_XY);
            return i;

        }

        public void populateAttributes() {
            mImageIds.put("Free Admission", R.drawable.list_admission_icon);
            mImageIds.put("Alcohol Available", R.drawable.list_alcohol_icon);
            mImageIds.put("For a Good Cause", R.drawable.list_cause_icon);
            mImageIds.put("Live Entertainment", R.drawable.list_entertainment_icon);
            mImageIds.put("Food Available", R.drawable.list_food_icon);
            mImageIds.put("Free Admission", R.drawable.list_free_icon);
            mImageIds.put("Free Item Giveaways", R.drawable.list_giveaway_icon);
            mImageIds.put("Indoor Activities", R.drawable.list_indoor_icon);
            mImageIds.put("Key Note Speaker", R.drawable.list_keynote_icon);
            mImageIds.put("Family Friendly", R.drawable.list_kid_icon);
            mImageIds.put("Music Available", R.drawable.list_music_icon);
            mImageIds.put("Networking Opportunities", R.drawable.list_network_icon);
            mImageIds.put("Outdoor Activities", R.drawable.list_outside_icon);
            mImageIds.put("Parking", R.drawable.list_parking_icon);
            mImageIds.put("Pet Friendly", R.drawable.list_pet_icon);
            mImageIds.put("Seating Available", R.drawable.list_seating_icon);
            mImageIds.put("Public Transit", R.drawable.list_transit_icon);
            mImageIds.put("Local Vendors Attending", R.drawable.list_vendor_icon);
            mImageIds.put("Volunteers Needed", R.drawable.list_volunteers_icon);
        }

        public void populateGalleryView(ImageView imgv, int idx) {

            final ImageView i = imgv;
            final int index = idx;
            myRef.child("events").child(eventID).child("tags").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> tags = new ArrayList<>();
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        String tag = child.getValue(String.class);
                        if (mImageIds.containsKey(tag)) {
                            attrImageIds.add(mImageIds.get(tag));
                        }
                    }

                    System.out.println("(1) Image Size: " + attrImageIds.size());

                    Integer tagIconArray[] = new Integer[attrImageIds.size()];
                    for (int i = 0; i < attrImageIds.size(); i++) {
                        tagIconArray[i] = attrImageIds.get(i);
                    }

                    System.out.println("(2) Image Size: " + tagIconArray.length);

                    i.setImageResource(tagIconArray[index]);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }
}
