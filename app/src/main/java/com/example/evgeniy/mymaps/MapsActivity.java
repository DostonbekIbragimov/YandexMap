package com.example.evgeniy.mymaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.evgeniy.mymaps.Interfaces.OnCompleteCallBack;
import com.example.evgeniy.mymaps.Tasks.GetterJSON;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.mapview.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnCompleteCallBack, View.OnClickListener {

    private String TAG = "MapsActivity";
    private String Url = "https://test.www.estaxi.ru/route.txt";

    private MapView mapView;
    private Button btnSetRoute;

    private MapObjectCollection mapObjects;
    private Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("99281920-4445-4559-a8ba-66ea5d006caf");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_maps);

        mapView = findViewById(R.id.mapview);

        btnSetRoute = findViewById(R.id.btnSetRoute);
        btnSetRoute.setOnClickListener(this);

        /*mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);*/
        map = mapView.getMap();
        mapObjects = map.getMapObjects().addCollection();
    }

    @Override
    public void onComplete(String Json) {
        if (Json != null) {
            ArrayList<Point> points = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(Json);
                JSONArray coords = object.getJSONArray("coords");
                for (int i = 0; i < coords.length(); i++) {
                    JSONObject coord = coords.getJSONObject(i);
                    points.add(new Point(coord.getDouble("la"), coord.getDouble("lo")));
                }
            } catch (JSONException e) {
                Log.e(TAG, "onComplete: ");
            }
            mapObjects.addPolyline(new Polyline(points));
            map.move(new CameraPosition(points.get(0), 11.0f, 0.0f, 0.0f), new Animation(Animation.Type.SMOOTH, 0), null);
        } else {
            Toast.makeText(this, "Запрос не выполенен, см Логи", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSetRoute:
                GetterJSON getterJSON = new GetterJSON(null, btnSetRoute, this);
                getterJSON.execute(Url);
                break;
        }
    }
}
