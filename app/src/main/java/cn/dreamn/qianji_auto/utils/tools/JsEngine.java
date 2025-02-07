package cn.dreamn.qianji_auto.utils.tools;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class JsEngine {
    public static String run(String jsCode) {

        Logs.d(jsCode);

        Context rhino = Context.enter();

        rhino.setOptimizationLevel(-1);
        Object result;
        try {

            Scriptable scope = rhino.initStandardObjects();
            result = rhino.evaluateString(scope, jsCode, "JavaScript", 1, null);

        } finally {

            Context.exit();

        }
        if (result != null) return result.toString();
        return null;
    }
}
