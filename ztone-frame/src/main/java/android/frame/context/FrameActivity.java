package android.frame.context;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.snackbar.Snackbar;

public abstract class FrameActivity<V extends ViewBinding> extends AppCompatActivity {

    @NonNull
    public V binding;

    public FrameActivity() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onInitData();

        binding = onViewBinding();
        setContentView(binding.getRoot());

        onCreateView();
    }

    protected abstract V onViewBinding();

    protected void onInitData() {

    }

    protected void onCreateView() {

    }


    public FragmentManager fmgr() {
        return getSupportFragmentManager();
    }

    public final void makeToast(final String text, final Object... args) {
        runOnUiThread(() -> {
            if (!isFinishing()) {
                Toast.makeText(FrameActivity.this, String.format(text, args), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void makeSnack(final String text, final Object... args) {
        runOnUiThread(() -> {
            if (!isFinishing()) {
                Snackbar.make(binding.getRoot(), String.format(text, args), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
