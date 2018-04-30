package caruso.nicholas.com.formbuilder.PreInstalledPlugin;

import android.support.v4.app.Fragment;

import caruso.nicholas.com.formbuilder.Validation;

/**
 * Nick:3/9/2018
 * WorkOrder.
 */

public class ValidationPlugin {

    public interface Listener {
        //        void ValidationFailure(int field, String reason, int Page);
//        void ValidationFailure(int field, String reason);

    }

    public static boolean isFIELD_TYPE(int error_type) {
        return (error_type == Validation.FIELD_WITH_PAGE || error_type == Validation.FIELD_WITHOUT_PAGE);
    }

//    public static ValidationPlugin create(@NonNull Fragment fragment) {
//        Listener listener;
//        try {
//            listener = (Listener) fragment;
//        } catch (ClassCastException e) {
//            listener = null;
//        }
//        return new ValidationPlugin(fragment, listener);
//    }

    public ValidationPlugin() {
    }

//    private ValidationPlugin(@NonNull Fragment fragment, @NonNull Listener listener) {
//        this.listener = listener;
//        this.fragment = fragment;
//        validation = new Validation();
//    }

    public static Validation Null() {
        return new Validation();
    }

    public static boolean is(Fragment frag) {
        return frag instanceof Listener;
    }

    public static boolean checkValidation(Validation validation) {
        return validation.isValid();
    }

//    public void doInvalid(DataBox rootDataBox) {
//        rootDataBox.resetValidation();
//        error_type = validation.first_error_type();
////        listener.ValidationFailure(validation.getFirst(), validation.getFirstReason());
//
////        listener.ValidationFailure(validation.getFirst(), validation.getFirstReason(), validation.getFirstPage());
//    }


    public static Validation combineValidation(Validation validation1, Validation validation2) {
        return validation1.addValidation(validation2);
    }

    public static Validation combineValidation(Validation validation1, Validation[] validation2) {
        return validation1.addValidationArray(validation2);
    }

}
