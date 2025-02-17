package baseConfig;

import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class GlobalConditionRule implements TestRule {
    private final boolean shouldRun;

    public GlobalConditionRule(String environment) {
        this.shouldRun = !environment.equals("prod");
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                if (description.getAnnotation(SkipOnGlobalCondition.class) != null) {
                    Assume.assumeTrue(shouldRun);
                }
                base.evaluate();
            }
        };
    }
}