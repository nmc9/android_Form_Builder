package caruso.nicholas.com.formbuilder.PreInstalledUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import caruso.nicholas.com.formbuilder.DataBox;
import caruso.nicholas.com.itm_database.Item;

/**
 * Nick:1/10/2018
 * WorkOrder.
 */

public class SubItemFragment extends Fragment {
    Item item;
    View view;
    DataBox dataBox = new DataBox(null, null);
    Context mContext;
    subItemListener subItemCommander;
    private static final String ITEM_KEY = "item";
    private static final String LAYOUT_PAGER_KEY = "layout";

    public interface subItemListener {
        DataBox    fillAdapterFieldsWithData(Item item, View adapterView);

        Item getDataFromAdapterFields(Item item, View adapterView);

        void buildInflatedView(View view);

    }

    public static SubItemFragment newInstance(Item item, int layout) {
        SubItemFragment frag = new SubItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ITEM_KEY, item);
        args.putInt(LAYOUT_PAGER_KEY, layout);
        frag.setArguments(args);
        return (frag);
    }

    public SubItemFragment() {
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        try {
            subItemCommander = (subItemListener) childFragment;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(childFragment.toString()
                    + " must implement Listener");
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachFragment(getParentFragment());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        subItemCommander = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            int layout = getArguments().getInt(LAYOUT_PAGER_KEY);
            view = inflater.inflate(layout, container, false);
            item = (Item) getArguments().getSerializable(ITEM_KEY);
            dataBox = subItemCommander.fillAdapterFieldsWithData(item, view);
            mContext = getContext();
            subItemCommander.buildInflatedView(view);
        }
        return view;
    }

    public DataBox getDatabox() {
        return dataBox;
    }


    public Item getItem() {
        return subItemCommander.getDataFromAdapterFields(item, view);
    }

    public View getView() {
        return view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.put
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}