package android.frame.context;

import android.math.MD5;
import android.text.TextUtils;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragArrays {

    // ---

    public static String tag(Class<? extends Fragment> fragmentClazz) {
        String name = fragmentClazz.getName();
        String tag = MD5.encrypt(name);
        return !TextUtils.isEmpty(tag) ? tag : name;
    }

    // ---

    @SuppressWarnings("unchecked")
    public static <F extends Fragment> F find(@NonNull FragmentManager fm, @NonNull Class<F> clazz) {
        Fragment fragment = fm.findFragmentByTag(tag(clazz));
        return fragment != null ? (F) fragment : null;
    }

    // ---

    public static <F extends Fragment> void add(@NonNull FragmentManager fm, @IdRes int containerViewId, @NonNull F fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(containerViewId, fragment, tag(fragment.getClass()));
        ft.commit();
        fm.executePendingTransactions();
    }

    public static <F extends Fragment> void replace(@NonNull FragmentManager fm, @IdRes int containerViewId, F fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(containerViewId, fragment, tag(fragment.getClass()));
        ft.commit();
        fm.executePendingTransactions();
    }

    public static void remove(@NonNull FragmentManager fm, Fragment... fragments) {
        if (fragments != null && fragments.length > 0) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    ft.remove(fragment);
                }
            }
            ft.commit();
            fm.executePendingTransactions();
        }
    }

    public static void show(@NonNull FragmentManager fm, Fragment... fragments) {
        if (fragments != null && fragments.length > 0) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    ft.show(fragment);
                }
            }

            ft.commit();
            fm.executePendingTransactions();
        }
    }

    public static void hide(@NonNull FragmentManager fm, Fragment... fragments) {
        if (fragments != null && fragments.length > 0) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    ft.hide(fragment);
                }
            }

            ft.commit();
            fm.executePendingTransactions();
        }
    }
}
