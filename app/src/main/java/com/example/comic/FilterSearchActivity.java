package com.example.comic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.comic.Common.Common;
import com.example.comic.Model.Comic;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

public class FilterSearchActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RecyclerView recycler_filter_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        recycler_filter_search=(RecyclerView)findViewById(R.id.recycler_filter_search) ;
        recycler_filter_search.setHasFixedSize(true);
        recycler_filter_search.setLayoutManager(new GridLayoutManager(this,2 ));



        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_nav);
        bottomNavigationView.inflateMenu(R.menu.main_menu);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
               switch (item.getItemId())
               {
                   case R.id.action_filter:
                       showFilterDialog();
                       break;
                   case R.id.action_search:
                       showSearchDialog();
                       break;
                        default:
                            break;
               }
                return ;
            }
        });
    }

    private void showSearchDialog() {
    }

    private void showFilterDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterSearchActivity.this);
        alertDialog.setTitle("Select Caregory");

        LayoutInflater inflater =this.getLayoutInflater();
        View filter_layout = inflater.inflate(R.layout.dialog_option,null);

        AutoCompleteTextView txt_category =(AutoCompleteTextView)filter_layout.findViewById(R.id.txt_categaory);
        ChipGroup chipGroup =(ChipGroup)filter_layout.findViewById(R.id.chipGroup);


        ArrayAdapter<String>adapter =new ArrayAdapter<>(this, android.R.layout.select_dialog_item, Common.categories );
        txt_category.setAdapter(adapter);
        txt_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txt_category.setText("");


                Chip chip =(Chip) inflater.inflate(R.layout.chip_item,null,false);
                chip.setText(((TextView)view).getText());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chipGroup.removeView(v);

                    }
                });
                 chipGroup.addView(chip);
            }
        });
        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String>filter_key=new ArrayList<>();
                StringBuilder filter_query=new StringBuilder("");
                for (int j=0;j<chipGroup.getChildCount();j++)
                {
                    Chip chip =(Chip)chipGroup.getChildAt(j);
                    filter_key.add(chip.getText().toString());
                }
                Collections.sort(filter_key);

                for (String key:filter_key)
                {
                    filter_query.append(key).append(" ,");

                }
                filter_query.setLength(filter_query.length()-1);

                fetchFilterCatagory(filter_query.toString());
            }
        });
        alertDialog.show();
    }

    private void fetchFilterCatagory(String query) {
        List<Comic> comic_filterd=new ArrayList<>();



    }

}