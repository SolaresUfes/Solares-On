package com.solares.calculadorasolar.components;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.solares.calculadorasolar.R;
import java.util.ArrayList;
import java.util.List;

public class CustomSpinner {
    private Context context;
    private View rootView;
    private Button spinnerButton;
    private AlertDialog currentDialog; // Track the current dialog

    private List<String> items;
    private ArrayAdapter<String> adapter;
    private String selectedItem;
    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void onItemSelected(String item);
    }

    public CustomSpinner(Context context, ViewGroup parent) {
        this.context = context;
        this.rootView = LayoutInflater.from(context).inflate(R.layout.searchable_spinner, parent, false);
        initializeViews();
        parent.addView(rootView);
    }

    private void initializeViews() {
        spinnerButton = rootView.findViewById(R.id.spinnerButton);

        if (spinnerButton != null) {
            spinnerButton.setOnClickListener(v -> showSpinnerDialog());
        } else {
            Log.e("CustomSpinner", "Spinner button not found in layout");
        }
    }

    public View getView() {
        return rootView;
    }

    public void setItems(List<String> items) {
        this.items = items;
        this.adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(items.get(position));
                return textView;
            }
        };
    }

    public void setSelectedItem(String item) {
        this.selectedItem = item;
        spinnerButton.setText(item);
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    private void showSpinnerDialog() {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.spinner_dialog_layout, null);

        EditText searchEditText = dialogView.findViewById(R.id.searchEditText);
        ListView itemsListView = dialogView.findViewById(R.id.itemsListView);

        List<String> filteredItems = new ArrayList<>(items);
        ArrayAdapter<String> dialogAdapter = new ArrayAdapter<>(
                context,
                R.layout.spinner_item,
                filteredItems
        );

        itemsListView.setAdapter(dialogAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dialogAdapter.getFilter().filter(s.toString(), count1 -> {
                    filteredItems.clear();
                    for (int i = 0; i < dialogAdapter.getCount(); i++) {
                        filteredItems.add(dialogAdapter.getItem(i));
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        itemsListView.setOnItemClickListener((parent, view, position, id) -> {
            view.post(() -> {
                view.setPressed(true);

                view.postDelayed(() -> {
                    view.setPressed(false);

                    String selected = dialogAdapter.getItem(position);
                    selectedItem = selected;
                    spinnerButton.setText(selected);

                    if (listener != null) {
                        listener.onItemSelected(selected);
                    }

                    new Handler().postDelayed(() -> {
                        if (currentDialog != null && currentDialog.isShowing()) {
                            currentDialog.dismiss();
                        }
                    }, 100);
                }, 100);
            });
        });

        builder.setView(dialogView);
        currentDialog = builder.create();
        currentDialog.show();
    }
}