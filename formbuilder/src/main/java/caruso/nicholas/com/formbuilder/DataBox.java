package caruso.nicholas.com.formbuilder;

import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;

/**
 * Nick:1/9/2018
 * DataBox Class Used to effortlessly insert and read data
 * from multiple EditText's contained with a View
 * Includes Validation Capabilities
 */

public class DataBox implements Serializable {
    /**
     * The view holding all the fields to push and pull from
     */
    private View rootView;
    /**
     * Execute this TextWatcher when text changes on
     * Fields set tot do so
     */
    private TextWatcher keyPress;
    /**
     * Holds Validation information for form submission
     */
    private Validation validation;

    /**
     * @param rootView The View holding all the fields to pull and push from
     * @param keyPress Optional: TextWatcher class to execute when data is changed
     */
    public DataBox(View rootView, TextWatcher keyPress) {
        this.rootView = rootView;
        this.keyPress = keyPress;
        validation = new Validation();
    }

    /**
     * @return View holding fields to pull and push from
     */
    public View getRootView() {
        return rootView;
    }

    /**
     * @return Class holding what to do when text is changed
     */
    public TextWatcher getKeyPress() {
        return keyPress;
    }

    /**
     * Push String to DataBox
     *
     * @param data     Data to put in Field (String)
     * @param model_id Field to put Data in
     * @param listen   True: set textChangedListener, False: Don't
     * @return The Field with Data
     */
    public EditText push(String data, int model_id, boolean listen) {
        final EditText editTextField = rootView.findViewById(model_id);
        editTextField.setText(data);
        if (listen) {
            editTextField.addTextChangedListener(keyPress);
        }
        return editTextField;
    }

    /**
     * Push int to DataBox
     *
     * @param data     Data to put in Field (int)
     * @param model_id Field to put Data in
     * @param listen   True: set textChangedListener, False: Don't
     * @return The Field with Data
     */
    public EditText push(Integer data, int model_id, boolean listen) {
        String data_1 = data + "";
        if (data == null) {
            data_1 = "";
        }
        return push(data_1, model_id, listen);
    }

    /**
     * Push String to DataBox: (Default: adds textWatcher)
     *
     * @param data     Data to put in Field (String)
     * @param model_id Field to put Data in
     * @return The Field with Data
     */
    public EditText push(String data, int model_id) {
        return push(data, model_id, true);
    }

    /**
     * Push int to DataBox: (Default: adds textWatcher)
     *
     * @param data     Data to put in Field (int)
     * @param model_id Field to put Data in
     * @return The Field with Data
     */
    public EditText push(Integer data, int model_id) {
        return push(data, model_id, true);
    }

    /**
     * Push String to DataBox with special Parameters
     *
     * @param data     Data to put in Field (String)
     * @param filters  Special Conditions of the Field
     * @param model_id Field to put Data in
     * @param listen   True: set textChangedListener, False: Don't
     * @return The Field with Data
     */
    public EditText pushWithFilter(String data, int model_id, InputFilter[] filters, boolean listen) {
        EditText e = push(data, model_id, listen);
        e.setFilters(filters);
        return e;
    }

    /**
     * Push String to DataBox with special Parameters
     * (Default: adds textWatcher)
     *
     * @param data     Data to put in Field (String)
     * @param filters  Special Conditions of the Field
     * @param model_id Field to put Data in
     * @return The Field with Data
     */
    public EditText pushWithFilter(String data, int model_id, InputFilter[] filters) {
        return pushWithFilter(data, model_id, filters, true);
    }

    /**
     * Push int to DataBox with special Parameters
     *
     * @param data     Data to put in Field (int)
     * @param filters  Special Conditions of the Field
     * @param model_id Field to put Data in
     * @param listen   True: set textChangedListener, False: Don't
     * @return The Field with Data
     */
    public EditText pushWithFilter(Integer data, int model_id, InputFilter[] filters, boolean listen) {
        String data_1 = data + "";
        if (data == null) {
            data_1 = "";
        }
        return pushWithFilter(data_1, model_id, filters, listen);

    }

    /**
     * Push int to DataBox with special Parameters
     * (Default: adds textWatcher)
     *
     * @param data     Data to put in Field (int)
     * @param filters  Special Conditions of the Field
     * @param model_id Field to put Data in
     * @return The Field with Data
     */
    public EditText pushWithFilter(Integer data, int model_id, InputFilter[] filters) {
        String data_1 = data + "";
        if (data == null) {
            data_1 = "";
        }
        return pushWithFilter(data_1, model_id, filters, true);
    }


    /**
     * @param editText The Field to Add Listener
     * @param listener The Listener to Add;
     * @return The EditText now with an Attached Listener.
     */
    public EditText addFocusListener(EditText editText, View.OnFocusChangeListener listener) {
        editText.setOnFocusChangeListener(listener);
        return editText;
    }


    /**
     * Pull Data from Field (Optional: Not part of validation)
     *
     * @param model_id The id of the field to pull Data
     * @return Data From Field (String)
     */
    public String pull(int model_id) {
        return pull(model_id, false);
    }

    /**
     * Pull Data from Field
     *
     * @param model_id The id of the field to pull Data
     * @param required True: Adds to Validation, False: Ignores Validation
     * @return Data From Field (String)
     */
    public String pull(int model_id, boolean required) {
        String pullable = ((EditText) rootView.findViewById(model_id)).getText().toString();
        if (required && TextUtils.isEmpty(pullable)) {
            validation.addFail(model_id, "required");
        }
        return pullable;
    }

    /**
     * Pull Data from Field (Optional: Not part of validation)
     *
     * @param model_id The id of the field to pull Data
     * @return Data From Field (Int)
     */
    public int pullInt(int model_id) {
        return pullInt(model_id, false);
    }

    /**
     * Pull Data from Field
     *
     * @param model_id The id of the field to pull Data
     * @param required True: Adds to Validation, False: Ignores Validation
     * @return Data From Field (Int): -1 error return;
     */
    public int pullInt(int model_id, boolean required) {
        String pullable = ((EditText) rootView.findViewById(model_id)).getText().toString();
        if (required && TextUtils.isEmpty(pullable)) {
            validation.addFail(model_id, "required");
        }
        int i = -1;
        try {
            i = Integer.parseInt(pullable);
        } catch (Exception e) {
            return i;
        }
        return i;
    }

    /**
     * Resets Validation;
     */
    public void resetValidation() {
        validation = new Validation();
    }

    /**
     * @return gets Validation;
     */
    public Validation getValidation() {
        return validation;
    }
}
