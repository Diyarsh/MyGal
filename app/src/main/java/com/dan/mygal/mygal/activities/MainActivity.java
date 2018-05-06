package com.dan.mygal.mygal.activities;

import android.app.ProgressDialog;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dan.mygal.mygal.R;
import com.dan.mygal.mygal.adapters.myGalleryAdapter;
import com.dan.mygal.mygal.apps.myAppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.dan.mygal.mygal.models.myImage;

// Главная активность с галереей - используем две главных библиотеки Glide для отображения (скроллинга и кеширования)
// и работы с галлереей и volley для загрузки

public class MainActivity extends AppCompatActivity {

    // Свойства класса и необходимые глобальные объекты
    private String TAG = MainActivity.class.getSimpleName();
    // Ссылка на API метод забора JSON массива картинок
    private static final String endpoint = "https://pixabay.com/api/?key=8897957-1c85a72bdb59f2c64e2628d38&q=cats&image_type=photo";
    private ArrayList<myImage> images;
    // Диалог загрузки
    private ProgressDialog pDialog;
    private myGalleryAdapter mAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Запуск приложения для просотра картинок на pixabay по тэгу cats


        // Иниицианализируем переменные и срздаем адаптер для RW
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new myGalleryAdapter(getApplicationContext(), images);

        // Подготваливаем RecyclerView
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // Добавляем листнер на клик/тач по изображению и переводим пользователя на следующую активность с полным изображением
        recyclerView.addOnItemTouchListener(new myGalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new myGalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                // Отправляем параметры в активность ShowFull
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ShowFull newFragment = ShowFull.newInstance();
                newFragment.setArguments(bundle);
                // Отображаем отдельную картинку с возможностью прокрутки
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchImages();
    }

    private void fetchImages() {

        // Метод для загрузки галлереи

        pDialog.setMessage("Загрузка галлереи..");
        pDialog.show();
        JSONObject reqObj = new JSONObject();
        // Загрузка json со списком изображений
        JsonObjectRequest req = new JsonObjectRequest(endpoint,reqObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pDialog.hide();

                        images.clear();

                        JSONArray ar = null;

                        // Получаем массив объектов из JSON объекта (hits)
                        try {
                            ar = response.getJSONArray("hits");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Проверяем что ошибки не было и объект присвоен

                        if (ar == null)
                        {
                            Toast.makeText(MainActivity.this,"По каким-то причинам ничего не вышло =(",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (int i = 0; i < ar.length(); i++) {
                            try {
                                JSONObject object = ar.getJSONObject(i);
                                myImage image = new myImage();
                                image.setName(object.getString("tags"));

                                // Забираем ссылки на разные типы изображения
                                String url = object.getString("largeImageURL");
                                String urlSm = object.getString("previewURL");
                                String urlMe = object.getString("webformatURL");

                                image.setSmall(urlSm);
                                image.setLarge(url);
                                image.setMedium(urlMe);


                                images.add(image);

                            } catch (JSONException e) {
                                // Обработка ошибок
                                Toast.makeText(MainActivity.this,"Ошибка парсинга " + e.getMessage(),Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Ошибка парсинга: " + e.getMessage());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // обработчик ошибок
                Toast.makeText(MainActivity.this,"Ошибка загрузки " + error.getMessage(),Toast.LENGTH_SHORT).show();

                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Запрос ставим в очередь
        myAppController.getInstance().addToRequestQueue(req);
    }
}
