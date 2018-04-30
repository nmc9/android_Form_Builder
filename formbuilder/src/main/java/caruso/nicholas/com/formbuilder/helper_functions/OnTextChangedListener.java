package caruso.nicholas.com.formbuilder.helper_functions;

import android.text.TextWatcher;

/**
 * Nick:2/22/2018
 * WorkOrder.
 * This Class is just to minimize code in other locations
 * Usually the only method Needed is The OnPageSelected() method
 * This method abstracts that so only that method is shown
 */

public abstract class OnTextChangedListener implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
}
