package sx.darkchecklist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class CLAdapter extends ArrayAdapter {

    private ArrayList<Item> dataSet;
    private Context mContext;

    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
    }

    CLAdapter(ArrayList<Item> data, Context context) {
        super(context, R.layout.row, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public Item getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.rowTextView);
            viewHolder.checkBox = convertView.findViewById(R.id.rowCheckBox);


            result = convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Item item = getItem(position);

        if (item != null) {
            viewHolder.txtName.setText(item.name);
            viewHolder.checkBox.setChecked(item.checked);
        }

        return result;
    }
}