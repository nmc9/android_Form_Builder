package caruso.nicholas.com.formbuilder.PreInstalledPlugin;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewStub;

/**
 * Nick:2/28/2018
 * WorkOrder.
 */

public class IncludePlugin {
    private Listener listener;

    public static IncludePlugin create(@NonNull Fragment fragment) {
        Listener listener;
        try {
            listener = (Listener) fragment;
        } catch (ClassCastException e) {
            listener = new Listener() {
                @Override
                public int LayoutIncludeKey() {
                    return -1;
                }

                @Override
                public int HolderIncludeKey() {
                    return -1;
                }
            };
        }
        return new IncludePlugin(listener);
    }

    private IncludePlugin(@NonNull Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        /*
            Stub that contains Included Layout;
         */
        int HolderIncludeKey();

        /*
            The included Layout;
         */
        int LayoutIncludeKey();

    }

    public final void inflateInclude(View rootView) {
        int stubLayout = listener.LayoutIncludeKey();
        if (stubLayout != -1) {
            ViewStub stub = rootView.findViewById(listener.HolderIncludeKey());
            stub.setLayoutResource(stubLayout);
            stub.inflate();
        }
    }


}
