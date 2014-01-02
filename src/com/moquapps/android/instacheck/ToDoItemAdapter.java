package com.moquapps.android.instacheck;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.*;


 
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.*;

public class ToDoItemAdapter extends ArrayAdapter<FoodItem> {

  int resource;
  Context c;
  private boolean wheelScrolled = false;
  
  public ToDoItemAdapter(Context _context, 
                             int _resource, 
                             List<FoodItem> _items) {
    super(_context, _resource, _items);
    resource = _resource;
    
    c =_context;
    

  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LinearLayout todoView;

    FoodItem item = getItem(position);
    
     
  

// spinner.setAdapter(adapter);
   // String taskString = item.getTask();
    String orderCount = item.getOcount();
  //  Date createdDate = item.getCreated();
    String orderName = item.getOname();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    String orderPrice = item.getOprice();
  //  String dateString = sdf.format(createdDate);
    

    if (convertView == null) {
      todoView = new LinearLayout(getContext());
      String inflater = Context.LAYOUT_INFLATER_SERVICE;
      LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
      vi.inflate(resource, todoView, true);
    } else {
      todoView = (LinearLayout) convertView;
    }

    TextView priceView = (TextView)todoView.findViewById(R.id.rowDate);
    TextView nameView = (TextView)todoView.findViewById(R.id.row);
    TextView countView = (TextView)todoView.findViewById(R.id.rowPrice); 
    WheelView wheel = (WheelView)todoView.findViewById(R.id.wheel1);
    //final EditText myPerson = (EditText)todoView.findViewById(R.id.person);
     
    
    
    
    
    
    priceView.setText(orderPrice);
    nameView.setText(orderName);
    countView.setText(orderCount);
    wheel.setViewAdapter(new SlotMachineAdapter(c));
    wheel.setCurrentItem(0);
    wheel.setCyclic(true);
    wheel.setEnabled(true);
    
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }
        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
          //  updateStatus();
        }
    };
    
    // Wheel changed listener
    OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!wheelScrolled) {
            	Log.v("Selected Wheel Item "," -->"+newValue);
                //updateStatus();
            }
        }
    };
    
    
    wheel.addChangingListener(changedListener);
    wheel.addScrollingListener(scrolledListener);
   
   
   
    return todoView;
  }
  
  private class SlotMachineAdapter extends AbstractWheelAdapter {
      // Image size
      final int IMAGE_WIDTH = 60;
      final int IMAGE_HEIGHT = 46;
      
      // Slot machine symbols
      private final int items[] = new int[] {
    	  	  R.drawable.hulk,
    		  R.drawable.jj, 
    		  R.drawable.gobble, 
    		  R.drawable.delicious
       };
      
      // Cached images
      private List<SoftReference<Bitmap>> images;
      
      // Layout inflater
      private Context context;
      
      /**
       * Constructor
       */
      public SlotMachineAdapter(Context context) {
          this.context = context;
          images = new ArrayList<SoftReference<Bitmap>>(items.length);
          for (int id : items) {
              images.add(new SoftReference<Bitmap>(loadImage(id)));
          }
      }
      
      /**
       * Loads image from resources
       */
      private Bitmap loadImage(int id) {
          Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
          Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true);
          bitmap.recycle();
          return scaled;
      }

      @Override
      public int getItemsCount() {
          return items.length;
      }

      // Layout params for image view
      final LayoutParams params = new LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);
      
      @Override
      public View getItem(int index, View cachedView, ViewGroup parent) {
          ImageView img;
          if (cachedView != null) {
              img = (ImageView) cachedView;
          } else {
              img = new ImageView(context);
          }
          img.setLayoutParams(params);
          SoftReference<Bitmap> bitmapRef = images.get(index);
          Bitmap bitmap = bitmapRef.get();
          if (bitmap == null) {
              bitmap = loadImage(items[index]);
              images.set(index, new SoftReference<Bitmap>(bitmap));
          }
          img.setImageBitmap(bitmap);
          
          return img;
      }
  }
  
}

