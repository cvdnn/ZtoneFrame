package android.frame.flow;

import android.assist.Assert;
import android.frame.Args;
import android.function.ToBooleanFunction;
import android.os.Bundle;

import androidx.arch.core.util.Function;
import androidx.core.util.Consumer;

public class ChainFlow {
    private final String mPuzzleTag;
    private final int mRepeatTotal;

    private Function mPrepareFunction;
    private ToBooleanFunction mInferFunction, mNegativeConsumer;
    private Consumer mPositiveConsumer;
    private Runnable mRepeatRunnable;

    private ChainFlow mNextFlow;

    public final Bundle Extras = new Bundle();

    public ChainFlow() {
        mPuzzleTag = "";
        mRepeatTotal = 0;
    }

    public ChainFlow(String tag, int c) {
        mPuzzleTag = tag;
        mRepeatTotal = c;
    }

    public void apply() {
        apply(null);
    }

    public void apply(Object in) {
        Object obj = applyPrepare(in);
        if (mInferFunction != null) {
            // 进行条件判断
            boolean result = mInferFunction.apply(obj);
            if (result) {
                applyPositive(obj);

                // 进行循环次数判断
            } else if (mRepeatTotal > 0) {
                int index = Args.Env.Cfg.get(mPuzzleTag, 0);
                if (index < mRepeatTotal) {
                    // 循环次数内，进行执行AndThan推断
                    Args.Env.Cfg.put(mPuzzleTag, ++index);

                    acceptRepeat();
                } else {
                    acceptNegative(obj);
                }

                // 当没有设定循环标签或次数时，直接执行负面推断
            } else {
                acceptNegative(obj);
            }

            // 无条件判断时，直接执行正面推断
        } else {
            applyPositive(obj);
        }
    }

    public final Object applyPrepare(Object in) {
        Object result = null;

        if (mPrepareFunction != null) {
            result = mPrepareFunction.apply(in);
        }

        return result;
    }

    public final ChainFlow applyPositive(Object obj) {
        if (mPositiveConsumer != null) {
            mPositiveConsumer.accept(obj);
        }

        jump();

        return this;
    }

    public final ChainFlow acceptNegative(Object obj) {
        if (mNegativeConsumer == null || mNegativeConsumer.apply(obj)) {
            jump();
        }

        return this;
    }

    public final ChainFlow acceptRepeat() {
        if (mRepeatRunnable != null) {
            mRepeatRunnable.run();
        }

        return this;
    }

    /**
     * 跳转
     *
     * @return
     */
    public final ChainFlow jump() {
        if (mNextFlow != null) {
            mNextFlow.apply();
        }

        // 清理重新开始计数
        clean();

        return this;
    }

    public final void clean() {
        if (Assert.notEmpty(mPuzzleTag)) {
            Args.Env.Cfg.remove(mPuzzleTag);
        }
    }

    public final ChainFlow destroy() {
        ChainFlow chain = this, next = null;
        while (chain != null) {
            next = chain.mNextFlow;

            chain.clean();

            chain.mPrepareFunction = null;
            chain.mInferFunction = null;
            chain.mNegativeConsumer = null;
            chain.mPositiveConsumer = null;
            chain.mRepeatRunnable = null;
            chain.mNextFlow = null;

            chain = next;
        }

        chain = null;
        next = null;

        return this;
    }

    public <I, O> ChainFlow prepare(Function<I, O> f) {
        mPrepareFunction = f;

        return this;
    }

    /**
     * 判断器
     *
     * @param tbf
     *
     * @return
     */
    public <T> ChainFlow dispatch(ToBooleanFunction<T> tbf) {
        mInferFunction = tbf;

        return this;
    }

    /**
     * 正面推断
     *
     * @param pc
     *
     * @return
     */
    public <T> ChainFlow positive(Consumer<T> pc) {
        mPositiveConsumer = pc;

        return this;
    }

    /**
     * 负面推断
     *
     * @param nf
     * @param <T>
     *
     * @return
     */
    public <T> ChainFlow negative(ToBooleanFunction<T> nf) {
        mNegativeConsumer = nf;

        return this;
    }

    public ChainFlow repeat(Runnable rr) {
        mRepeatRunnable = rr;

        return this;
    }

    public ChainFlow next(ChainFlow np) {
        mNextFlow = np;

        return this;
    }
}
