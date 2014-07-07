package com.moquapps.android.instacheck;

//import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

 /**
     * Adapter for countries
     */
/*This file excluded from build - ak - July7,2014*/
public class PersonAdapterWheel extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] =
            new String[] {"Hulk", "Famished", "Gobble", "Pinky"};
        // Countries flags
        private int flags[] =
            new int[] {R.drawable.hulk, R.drawable.jj, R.drawable.gobble, R.drawable.delicious};
        
        /**
         * Constructor
         */
        
        protected PersonAdapterWheel(Context context) {
            super(context, R.layout.country_layout, NO_RESOURCE);
            
            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            ImageView img = (ImageView) view.findViewById(R.id.flag);
            img.setImageResource(flags[index]);
            return view;
        }
        
        @Override
        public int getItemsCount() {
            return countries.length;
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
}