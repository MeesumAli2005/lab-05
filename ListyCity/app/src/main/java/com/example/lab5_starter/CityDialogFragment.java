package com.example.lab5_starter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class CityDialogFragment extends DialogFragment {
    interface CityDialogListener {
        void updateCity(City city, String title, String year);

        void addCity(City city);

        void deleteCity(City city);
    }

    private CityDialogListener listener;
    public static CityDialogFragment newInstance(City city) {
        Bundle args = new Bundle();
        args.putSerializable("City", city);

        CityDialogFragment fragment = new CityDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CityDialogListener) {
            listener = (CityDialogListener) context;
        } else {
            throw new RuntimeException("Activity must implement CityDialogListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_city_details, null);
        EditText editCityName = view.findViewById(R.id.edit_city_name);
        EditText editCityProvince = view.findViewById(R.id.edit_province);

        String tag = getTag();
        Bundle bundle = getArguments();
        City city = null;

        if (Objects.equals(tag, "City Details") && bundle != null) {
            city = (City) bundle.getSerializable("City");
            if (city != null) {
                editCityName.setText(city.getName());
                editCityProvince.setText(city.getProvince());
            }
        }

        final City finalCity = city;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("City Details")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Continue", (dialog, which) -> {
                    String title = editCityName.getText().toString();
                    String province = editCityProvince.getText().toString();

                    if (finalCity != null) {
                        listener.updateCity(finalCity, title, province);
                    } else {
                        listener.addCity(new City(title, province));
                    }
                });

        if (finalCity != null) {
            builder.setNeutralButton("Delete", (dialog, which) -> {
                listener.deleteCity(finalCity);
            });
        }

        return builder.create();
    }
}