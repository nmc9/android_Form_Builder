package caruso.nicholas.com.android_form_builder;

import caruso.nicholas.com.formbuilder.Form_Fragment;
import caruso.nicholas.com.formbuilder.Form_Type;

/**
 * Nick:4/30/2018
 * android_form_builder.
 */
public class FormType extends Form_Type {
    private static final int GENERAL = 1;
    private static final int METER_READ = 2;
    private static final int JUST_METER_READ = 200;

    private static final int METER_SWAP = 3;
    private static final int METER_INSTALL = 4;
    private static final int MXU_SWAP = 5;
    private static final int DUMMY = 6;
    public static final int NEW_WORKORDER = -1;
    public static final int NEW_WORKORDER_UPDATE = -2;

    public static FormType get() {
        return new FormType();
    }

    @Override
    public Form_Fragment getFragment(int type) {
        return null;
//        switch (type) {
//            case NEW_WORKORDER:
//            case NEW_WORKORDER_UPDATE:
//                return new New_WorkOrder_Fragment();
//            case GENERAL:
//                return new General_Fragment();
//            case METER_READ:
//                return new Meter_Read_Fragment();
//            case METER_SWAP:
//                return new Meter_Swap_Fragment();
//            case METER_INSTALL:
//                return new Meter_Install_Fragment();
//            case MXU_SWAP:
//                return new MXU_Swap_Fragment();
//            case JUST_METER_READ:
//                return new Just_Meter_Read_Fragment();
//        }
//        return new General_Fragment();
    }
}
