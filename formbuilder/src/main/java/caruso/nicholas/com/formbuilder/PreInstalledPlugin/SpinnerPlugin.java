package caruso.nicholas.com.formbuilder.PreInstalledPlugin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Nick:4/2/2018
 * WorkOrder.
 */
public class SpinnerPlugin {
    Fragment fragment;
    Listener listener;
    Context context;

    public interface Listener {
    }

    public static SpinnerPlugin create(@NonNull Fragment fragment) {
        Listener listener;
        try {
            listener = (Listener) fragment;
        } catch (ClassCastException e) {
            listener = new Listener() {
            };
        }
        return new SpinnerPlugin(fragment, listener);

    }


    private SpinnerPlugin(Fragment fragment, @NonNull Listener listener) {
        this.fragment = fragment;
        this.context = fragment.getContext();
        this.listener = listener;
    }


}
