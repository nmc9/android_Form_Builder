package caruso.nicholas.com.formbuilder;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Nick:2/20/2018
 * WorkOrder.
 */

public class Validation implements Parcelable {
    private List<Field> failed_fields;
    public static final int FIELD_WITH_PAGE = 1;
    public static final int FIELD_WITHOUT_PAGE = 2;
    public static final int JUST_PAGE = 3;
    public static final int LIST_NONE_SELECTED = 4;

    private Validation(Parcel in) {
        failed_fields = new ArrayList<>();
        in.readList(failed_fields, Field.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(failed_fields);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Validation> CREATOR = new Creator<Validation>() {
        @Override
        public Validation createFromParcel(Parcel in) {
            return new Validation(in);
        }

        @Override
        public Validation[] newArray(int size) {
            return new Validation[size];
        }
    };

//    public static boolean PAGE_TYPE(int type) {
//        return type == JUST_PAGE;
//    }

    public static void Print_Array(Validation[] val) {
        StringBuilder printable = new StringBuilder("INVALID FRAGMENTS\n");
        if (val != null) {
            for (int i = 0; i < val.length; i++) {
                StringBuilder include = new StringBuilder("(" + i + "): ");
                if (val[i] == null) {
                    include.append(" null");
                } else {
                    for (int f = 0; f < val[i].failed_fields.size(); f++) {
                        String deep = "[" + f + "]" + val[i].failed_fields.get(f).dump();
                        include.append(deep);
                    }
                }
                include.append("\n");
                printable.append(include);
            }
        }
        Log.d("TAG", printable.toString());
    }

    public String ArrayofTypes() {
        StringBuilder x = new StringBuilder("TYPES:");
        for (Field y : failed_fields) {
            if (y.type == FIELD_WITH_PAGE) {
                x.append("W/PAGE,");
            } else if (y.type == FIELD_WITHOUT_PAGE) {
                x.append("WO/PAGE,");

            } else if (y.type == JUST_PAGE) {
                x.append("JUST PAGE,");
            }
        }
        return x.toString();
    }

    public String ArrayofData() {
        StringBuilder x = new StringBuilder("Data:");
        for (Field y : failed_fields) {
            x.append(y.dump());
        }
        return x.toString();
    }


    public Validation addValidation(Validation validation) {
        failed_fields.addAll(validation.failed_fields);
        return this;
    }

    public Validation addValidationArray(Validation[] validation) {
        for (int i = 0; i < validation.length; i++) {
            if (validation[i] != null) {
                failed_fields.addAll(validation[i].failed_fields);
            } else {
                failed_fields.add(new Field(i));
            }
        }
        return this;
    }

    public void clear() {
        failed_fields.clear();
    }

    public Validation() {
        failed_fields = new ArrayList<>();
    }

    public boolean isValid() {
        return failed_fields.isEmpty();
    }

    public void addFail(int id, String reason) {
        failed_fields.add(new Field(id, reason));
    }

    public void addFailPage(int id, String reason, int page) {
        Field f = new Field(id, reason);
        f.page = page;
        failed_fields.add(f);
    }

    public void addFail(int id, String reason, int type) {
        Field f = new Field(id, reason);
        f.setType(type);
        failed_fields.add(f);
    }

    public int getFirst() {
        if (isValid()) {

            return -1;
        }
        return failed_fields.get(0).id;
    }

    public String getFirstReason() {
        if (isValid()) {

            return null;
        }
        return failed_fields.get(0).reason;

    }

    public int getFirstPage() {
        if (isValid()) {

            return -1;
        }
        return failed_fields.get(0).page;
    }

    public Validation withPage(int page) {
        for (Field f : failed_fields) {
            f.addPage(page);
        }
        return this;
    }

    public int first_error_type() {
        return isValid() ? -1 : failed_fields.get(0).type;
    }

    private static class Field implements Parcelable {
        private int id = -1;
        private String reason = null;
        private int page = -1;
        public int type;

        Field(int id, String reason) {
            this.id = id;
            this.reason = reason;
            this.page = -1;
            this.type = FIELD_WITHOUT_PAGE;
        }

        Field(int id, String reason, int page) {
            this.id = id;
            this.reason = reason;
            this.page = page;
            this.type = FIELD_WITH_PAGE;
        }

        Field(int page) {
            this.id = -1;
            this.reason = null;
            this.page = page;
            this.type = JUST_PAGE;
        }

        Field(Parcel in) {
            id = in.readInt();
            reason = in.readString();
            page = in.readInt();
            type = in.readInt();
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(reason);
            dest.writeInt(page);
            dest.writeInt(type);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Field> CREATOR = new Creator<Field>() {
            @Override
            public Field createFromParcel(Parcel in) {
                return new Field(in);
            }

            @Override
            public Field[] newArray(int size) {
                return new Field[size];
            }
        };

        void addPage(int page) {
            this.page = page;
            if (this.id != -1) {
                this.type = FIELD_WITH_PAGE;
            } else {
                this.type = JUST_PAGE;
            }
        }

        String dump() {
            String x = " id " + id;
            x += " reason " + reason;
            x += " page " + page;
            x += " type " + type;
            return x;
        }

    }

    public static Validation Null() {
        return new Validation();
    }
}
