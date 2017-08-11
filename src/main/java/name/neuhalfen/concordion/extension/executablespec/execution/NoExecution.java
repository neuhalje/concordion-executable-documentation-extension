package name.neuhalfen.concordion.extension.executablespec.execution;

import org.concordion.api.Evaluator;

public class NoExecution implements ScriptExecution {

    @Override
    public ScriptResult run(String script, Evaluator evaluator) {
        return ScriptResult.forUnsupportedLanguage();
    }
}
