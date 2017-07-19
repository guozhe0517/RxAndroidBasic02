package com.guozhe.android.rxandroidbasic02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class MainActivity extends AppCompatActivity{

    private TextView textView;
    private RecyclerView recyclerView;

    private List<String> data = new ArrayList<>();
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initObservable();

    }

    private void initView() {
        textView = (TextView) findViewById(R.id.textView);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    Observable<String> forFrom;
    Observable<Memo> forJust;
    Observable<String> forDefer;
    private void initObservable(){
        String formData[] = {"aaa","bbb","ccc","ddd","eee"};
        forFrom = Observable.fromArray(formData);
        Memo memo = new Memo("Hello");
        Memo memo1 = new Memo("Android");
        forJust = Observable.just(memo,memo1);
        forDefer = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just("monday","tuesday","wednsday");
            }
        });
    }
    public void from(View view){
       forFrom.subscribe(
               str->data.add(str),
               t->{},
               ()->adapter.notifyDataSetChanged()
       );
    }
    public void just(View view){
        forJust.subscribe(
                obj -> data.add(obj.memo),
                t->{},
                ()->adapter.notifyDataSetChanged()
        );
    }
    public void defer(View view){
        forDefer.subscribe(
                str->data.add(str),
                t->{},
                ()->adapter.notifyDataSetChanged()
        );
    }

}
//just 생성자를 위한 클래스
class Memo{
    String memo;
    public Memo(String memo){
        this.memo = memo;
    }
}
class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder>{
        List<String> data;

        public CustomAdapter(List<String> data){
            this.data = data;
        }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textView;
        public Holder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.textView);
        }
    }
}
