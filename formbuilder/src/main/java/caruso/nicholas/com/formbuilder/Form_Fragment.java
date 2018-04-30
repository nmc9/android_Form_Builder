package caruso.nicholas.com.formbuilder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import caruso.nicholas.com.itm_database.Item;

/**
 * Nick:2/28/2018
 * WorkOrder.
 */

public abstract class Form_Fragment extends Fragment {
    public static final String ARG_ID = "item_id";
    public static final String ARG_TYPE = "form_type";
    public static final String ARG_BUNDLE = "bundle";

    /*
        Data associated with the data object
        And how to store it
     */
    public int itemID = -1;
    public int form_type = -1;
    public Bundle bundle = null;
    public View rootView;
    public DataBox rootDataBox = new DataBox(null, null);
    public Context mContext;

    //Talk back the the calling Activity
    public FormListener formCommander;

    private Form_Fragment fragment;

    //To show or not to show the cancel dialog
    public boolean updatedCalled = false;
public abstract Form_Fragment getTheForm(int type);


    public static Form_Fragment newInstance(Form_Type form_type, int item_id, int type, Bundle bundle) {
        Form_Fragment fragment = form_type.getFragment(type);
        Bundle arguments = new Bundle();
        arguments.putInt(Form_Fragment.ARG_ID, item_id);
        arguments.putInt(Form_Fragment.ARG_TYPE, item_id);
        arguments.putBundle(Form_Fragment.ARG_BUNDLE, bundle);
        fragment.setArguments(arguments);
        return fragment;
    }

    public final Form_Fragment getFragment() {
        return fragment;
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
        mContext = getContext();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_ID)) {
                itemID = savedInstanceState.getInt(ARG_ID);
            }
            if (savedInstanceState.containsKey(ARG_TYPE)) {
                form_type = savedInstanceState.getInt(ARG_TYPE);
            }
            if (savedInstanceState.containsKey(ARG_BUNDLE)) {
                bundle = savedInstanceState.getBundle(ARG_BUNDLE);
            }

            restoreInstance(savedInstanceState);
        }
        if (itemID == -1 && getArguments() != null) {
            itemID = getArguments().getInt(ARG_ID);
            form_type = getArguments().getInt(ARG_TYPE);
            bundle = getArguments().getBundle(ARG_BUNDLE);
        }

        setItem(getContext());
        formCommander.setTitle(getTitle());
        create();
    }

    public void create() {
        //In case they want to do more when calling onCreate();
    }

    public abstract void restoreInstance(@NonNull Bundle savedInstanceState);

    public abstract void setItem(Context context);

    public abstract String getTitle();

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(LayoutKey(), container, false);
        rootDataBox = new DataBox(rootView, getTextWatcher());
        createView(rootView);
        fillFieldsWithData();
        setSubmitButton(rootView);
        return rootView;
    }

    public abstract void setSubmitButton(View rootView);

    public abstract void fillFieldsWithData();


    public void createView(View rootView) {

    }

    public abstract int LayoutKey();


    @Override
    public final void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_ID, itemID);
        outState.putInt(ARG_TYPE, form_type);
        outState.putBundle(ARG_BUNDLE, bundle);
        saveInstance(outState);
    }

    public abstract void saveInstance(@NonNull Bundle outState);

    public final void SubmitForm(Item item) {
        formCommander.onFormSubmit(item);
    }

    public void beforeFormSubmit(Item item) {
        retrieveDataFromFields();
        SubmitForm(item);
    }

    public abstract Validation retrieveDataFromFields();


    public interface FormListener {
        void updated();

        void toggleList(boolean On);

        void setTitle(String title);

        void onFormSubmit(Item item);

    }

    public final TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!updatedCalled) {
                    formCommander.updated();
                }
                updatedCalled = true;
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            formCommander = (FormListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement Listener");
        }
    }


}
