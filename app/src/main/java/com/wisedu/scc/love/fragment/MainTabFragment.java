package com.wisedu.scc.love.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisedu.scc.love.R;

public class MainTabFragment extends Fragment {

    private View view;

    public MainTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        doCut();
        return view;
    }

    public void doCut() {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    ClipImageLayout mClipImageLayout =
                            (ClipImageLayout)view.findViewById(R.id.id_clipImageLayout);
                    Bitmap bitmap = mClipImageLayout.clip();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    Intent intent = new Intent(MainTabFragment.this.getActivity(),
                            ImageShowActivity_.class);
                    intent.putExtra("bitmap", bytes);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

}
