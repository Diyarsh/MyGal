package com.dan.mygal.mygal.adapters;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dan.mygal.mygal.models.myImage;
import java.util.List;
import com.dan.mygal.mygal.R;

// Класс-адаптер для отображения картинок в recyclerView и обработки событий-жестов

public class myGalleryAdapter extends RecyclerView.Adapter<myGalleryAdapter.MyViewHolder> {

    //Список изображений
    private List<myImage> images;
    private Context mContext;


    // Встроенный класс для холдера мини-картинки
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public myGalleryAdapter(Context context, List<myImage> images) {
        // Конструктор
        mContext = context;
        this.images = images;
    }

    //Событие при создании (инициализация)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    // Обработка и загрузка изображения в конкретную ячейку
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        myImage image = images.get(position);
        // Загрузка с кешированием
        Glide.with(mContext).load(image.getMedium())
                .thumbnail(0.3f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
    }

    // Возращаем кол-во
    @Override
    public int getItemCount() {
        return images.size();
    }
    // Листнер на клик
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    // Метод для обработки прикасновений
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        // Объект для "опознания жестов"
        private GestureDetector gestureDetector;
        private myGalleryAdapter.ClickListener clickListener;

        // Добавляем оброаботчик жеста в RecyclerTouchListener
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final myGalleryAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }


        // Процедура при касании - передвем в clickListener
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}