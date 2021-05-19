package android.frame.context;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.snackbar.Snackbar;

public abstract class FrameFragment<V extends ViewBinding> extends Fragment {

    @NonNull
    public V binding;

    public FrameFragment() {
        super();
    }

    @Nullable
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = onViewBinding();
    }

    protected abstract V onViewBinding();

    public final void makeToast(final String text, final Object... args) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                if (!activity.isFinishing()) {
                    Toast.makeText(activity, String.format(text, args), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    protected void makeSnack(final String text, final Object... args) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                if (!activity.isFinishing()) {
                    Toast.makeText(activity, String.format(text, args), Toast.LENGTH_SHORT).show();
                    Snackbar.make(binding.getRoot(), String.format(text, args), Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }
}
