package sx.darkchecklist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Item> dataModel;
    ListView listView;
    private CLAdapter adapter;
    Button add;
    Button delete;
    Switch screenSwitch;
    Context mContext;
    SharedPreferences preferences;
    Type itemList = new TypeToken<ArrayList<Item>>() {}.getType();

    //save context
    @Override
    protected void onPause() {
        super.onPause();

        String strItems = new Gson().toJson(dataModel, itemList);
        preferences.edit().putString("DarkCLSave", strItems).apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        add = findViewById(R.id.add);
        delete = findViewById(R.id.delete);
        screenSwitch = findViewById(R.id.screenSwitch);
        listView = findViewById(R.id.myList);

        //load context
        if (preferences.contains("DarkCLSave"))
            dataModel = new Gson().fromJson(preferences.getString("DarkCLSave", ""), itemList);
        else
            dataModel = new ArrayList<>();

        //custom adapter
        adapter = new CLAdapter(dataModel, mContext);
        listView.setAdapter(adapter);

        //touching an item will toggle check mark
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Item data = dataModel.get(position);
                data.checked = !data.checked;
                adapter.notifyDataSetChanged();
            }
        });

        //add items
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemButton();
            }
        });

        //delete all checked items
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItems();
            }
        });

        //switch to keep screen on
        screenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                else
                    getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });
    }

    private void addItemButton() {
        final EditText addItem = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add new item:")
                .setView(addItem)
                .setPositiveButton("Add More", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = String.valueOf(addItem.getText());
                        if (!(item.length() > 0 )) {
                            return;
                        }
                        dataModel.add(new Item(item));
                        addItemButton();
                    }
                })
                .setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = String.valueOf(addItem.getText());
                        if (!(item.length() > 0 )) {
                            return;
                        }
                        dataModel.add(new Item(item));
                    }
                })
                .setNeutralButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void removeItems() {
        for (int i = dataModel.size() - 1; i >= 0; i-- ) {
            if (dataModel.get(i).checked) {
                dataModel.remove(dataModel.get(i));
                adapter.notifyDataSetChanged();
            }
        }
    }
}
