package com.elyeproj.demosimpleprotobuf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static tutorial.Dataformat.Person;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient = new OkHttpClient();

    private Observable observable = Observable.just("http://elyeproject.x10host.com/experiment/protobuf")
            .map(new Function<String, Person>() {
        @Override
        public Person apply(String url) throws Exception {

            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();

            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    return Person.parseFrom(responseBody.byteStream());
                }
            }

            return null;
        }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object object) throws Exception {
                showResult((Person) object);
            }
        });

    }

    private void showResult(Person result) {
        TextView textView = findViewById(R.id.txt_main);
        textView.setText(result.toString());
    }
}
