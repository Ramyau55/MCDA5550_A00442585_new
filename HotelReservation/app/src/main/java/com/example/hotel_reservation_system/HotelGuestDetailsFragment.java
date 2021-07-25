package com.example.hotel_reservation_system;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class HotelGuestDetailsFragment extends Fragment  {

    View view;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    String hotelName;
    LinearLayoutManager llm;
    Button send;
    public static final String myPreference = "myPref";
    public static final String guestsCount = "guestsCount";
    public static final String checkIn = "checkIn";
    public static final String checkOut = "checkOut";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotel_guest_details_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView hotelRecapLabelTextView = view.findViewById(R.id.hotel_recap_hotel_label_text_view);
        TextView hotelRecapValueTextView = view.findViewById(R.id.hotel_recap_hotel_value_text_view);
        TextView hotelRecapPriceLabelTextView = view.findViewById(R.id.hotel_recap_price_label_text_view);
        TextView hotelRecapPriceValueView = view.findViewById(R.id.hotel_recap_price_value_text_view);

        TextView hotelRecapAvailabilityLabelTextView = view.findViewById(R.id.hotel_recap_availability_label_text_view);
        TextView hotelRecapAvailabilityValueTextView = view.findViewById(R.id.hotel_recap_availability_value_text_view);

        send = view.findViewById(R.id.send);
        hotelName = getArguments().getString("hotel name");
        String hotelPrice = getArguments().getString("hotel price");
        String hotelAvailability = getArguments().getString("hotel availability");

        hotelRecapLabelTextView.setText("Hotel Name:");
        hotelRecapValueTextView.setText(hotelName);
        hotelRecapPriceLabelTextView.setText("Price");
        hotelRecapPriceValueView.setText(hotelPrice);
        hotelRecapAvailabilityLabelTextView.setText("Availability");
        hotelRecapAvailabilityValueTextView.setText(hotelAvailability);
        setupRecyclerView();

        send.setOnClickListener(new View.OnClickListener() {
            JSONObject obj;
            int f=0;
            TypedInput in;
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
                recyclerView = view.findViewById(R.id.guest_list_recyclerView);
                JSONArray array = new JSONArray();

                for(int i = 0; i<(llm.getItemCount()); i++){
                    View firstViewItem = llm.findViewByPosition(i);
                    EditText guestName=firstViewItem.findViewById(R.id.guestName);
                    EditText guestGender=firstViewItem.findViewById(R.id.gender);
                    obj = new JSONObject();
                    if(guestName.getText().toString().isEmpty() && guestGender.getText().toString().isEmpty()){
                        Toast.makeText(getActivity(),"Please enter the Guest details",Toast.LENGTH_LONG).show();
                        f=1;
                    }
                    else{
                        try {
                            obj.put("guest_name",guestName.getText().toString());
                            obj.put("gender",guestGender.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        array.put(obj);
                    }
                }
                obj = new JSONObject();
                try {
                    obj.put("hotel_name",hotelName);
                    if (sharedPreferences.contains(checkIn)) {
                        obj.put("checkin",sharedPreferences.getString(checkIn, ""));
                    }
                    if (sharedPreferences.contains(checkOut)) {
                        obj.put("checkout",sharedPreferences.getString(checkOut, ""));
                    }
                    obj.put("guests",array);
                    in = new TypedByteArray("application/json", obj.toString().getBytes("UTF-8"));

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (f==0) {
                    Api.getClient().reserveHotel(in, new Callback<Confirmation>() {

                        @Override
                        public void success(Confirmation confirmation_number, Response response) {
                            Bundle bundle = new Bundle();
                            bundle.putString("confirmation number", confirmation_number.getconfirmation_number());
                            GuestFragment guestAddedFragment=new GuestFragment();
                            guestAddedFragment.setArguments(bundle);

                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.main_layout, guestAddedFragment);
                            fragmentTransaction.remove(HotelGuestDetailsFragment.this);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }


    private void setupRecyclerView() {
        sharedPreferences = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        int guestNumber = 0;
        if (sharedPreferences.contains(guestsCount)) {
            guestNumber= Integer.parseInt(sharedPreferences.getString(guestsCount, ""));
        }
        recyclerView = view.findViewById(R.id.guest_list_recyclerView);
        llm=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        GuestAdapter guestAdapter=new GuestAdapter(getActivity(),guestNumber);
        recyclerView.setAdapter(guestAdapter);
    }

}
