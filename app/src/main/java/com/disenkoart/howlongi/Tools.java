package com.disenkoart.howlongi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Артём on 20.09.2016.
 */
public class Tools {

    /**
     * Делает скриншот View и сохраняет его в общую папку с изображениями и возвращает сохраненный файл.
     * @param context Контекст приложения.
     * @param savingView View, скриншот которого будет выолняьтся.
     * @return Файл сохраненного скриншота.
     */
    public static File saveScreenShotOfView(Context context, View savingView){
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), String.valueOf(DateTime.now().getMillis()) + ".png");
        try {
            if (!file.getParentFile().mkdirs()){
                Log.d("ERROR", "Файл не создан");
            }
            FileOutputStream stream = new FileOutputStream(file);
            getScreenShotView(savingView).compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Возвращает скриншот View.
     * @param view View, скриншот которого будет создаваться.
     * @return Полученный скриншот View.
     */
    private static Bitmap getScreenShotView(View view){
        Bitmap bitmapView = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapView);
        view.draw(canvas);

        //Обрезаем тень от изображения
        return Bitmap.createBitmap(bitmapView, 2, 0, bitmapView.getWidth() - 4, bitmapView.getHeight() - 7);
    }
}
