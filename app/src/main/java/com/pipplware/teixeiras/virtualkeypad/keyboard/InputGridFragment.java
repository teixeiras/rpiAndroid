package com.pipplware.teixeiras.virtualkeypad.keyboard;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.pipplware.teixeiras.virtualkeypad.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InputGridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InputGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputGridFragment extends Fragment {
    public static InputGridFragment newInstance(String param1, String param2) {
        InputGridFragment fragment = new InputGridFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    List<GridOptions> options = new ArrayList<>();
    public InputGridFragment() {
        // Required empty public constructor
    }


    private class GridOptions {
        public Drawable icon;
        public View.OnClickListener action;
        public GridOptions(Drawable icon,View.OnClickListener action ) {
            this.icon = icon;
            this.action = action;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options.add(new GridOptions(getResources().getDrawable(R.drawable.device_keyboard_wireless_icon),
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(InputGridFragment.this.getActivity(), InputContainerActivity.class);
                        intent.putExtra(InputContainerActivity.INPUT_TYPE, InputContainerActivity.INPUT_TYPE_KEYBOARD);
                        startActivity(intent);
                    }
                }));

        options.add(new GridOptions(getResources().getDrawable(R.drawable.gamepad_icon),
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(InputGridFragment.this.getActivity(), InputContainerActivity.class);
                        intent.putExtra(InputContainerActivity.INPUT_TYPE, InputContainerActivity.INPUT_TYPE_GAMEPAD);
                        startActivity(intent);
                    }
                }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_grid, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);


        gridview.setAdapter(new BaseAdapter(){


            @Override
            public int getCount() {
                return options.size();
            }

            @Override
            public GridOptions getItem(int position) {
                return options.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_input_list, parent, false);
                }
                ImageView imageview = (ImageView)convertView.findViewById(R.id.icon);
                imageview.setImageDrawable(this.getItem(position).icon);
                convertView.setOnClickListener(this.getItem(position).action);
                return convertView;
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
