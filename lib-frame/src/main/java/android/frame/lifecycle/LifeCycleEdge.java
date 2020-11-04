package android.frame.lifecycle;

import android.os.Bundle;
import androidx.annotation.NonNull;

public interface LifeCycleEdge {

	void onPrepareData(@NonNull Bundle extraBundle, boolean fromInstanceState) throws Exception;

	void onPrepareView() throws Exception;

	boolean isDestroyed();
}
